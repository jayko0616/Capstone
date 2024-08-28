import React, { useEffect, useState } from "react";
import axios from "axios";
import moment from "moment";
import 'moment/locale/ko'; // moment의 한국어 로케일을 추가합니다.
import './favoritelistpage.css';
import { useNavigate } from 'react-router-dom';

// API 기본 URL을 상수로 정의
const BASE_API_URL = 'http://3.34.86.246:8082/api';
const BASE_USER_URL = 'http://3.34.86.246:8082';

const customAxios = axios.create({
    baseURL: BASE_API_URL
});

const userAxios = axios.create({
    baseURL: BASE_USER_URL
});

function FavoriteListPage() {
    const [favoriteShops, setFavoriteShops] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        moment.locale('ko'); // 한국어 로케일을 설정합니다.

        const fetchFavoriteShops = async () => {
            const token = localStorage.getItem('authToken');
            if (!token) {
                navigate('/LoginPage');
                return;
            }

            try {
                const response = await userAxios.post('/user', {}, {
                    headers: {
                        'Authorization': token.replace(/^Bearer\s*/, '')
                    }
                });

                const data = response.data;
                console.log("Liked stores from user info:", data.likedStores);

                const likedStores = data.likedStores;

                const shopPromises = likedStores.map(storeId =>
                    customAxios.get(`/store/${storeId}`)
                );

                const shopResponses = await Promise.all(shopPromises);

                shopResponses.forEach((res, index) => {
                    console.log(`Store data for ID ${likedStores[index]}:`, res.data);
                });

                const fetchedShops = shopResponses
                    .map(res => res.data)
                    .filter(shop => shop && Object.keys(shop).length > 0)
                    .map(shop => ({
                        ...shop,
                        myheart: true,
                        boardCreatedTime: shop.boardCreatedTime ? moment(shop.boardCreatedTime).format('YYYY년 M월 D일') + ` (${moment(shop.boardCreatedTime).fromNow(true)} 전)` : '0',
                        boardCreatedTimestamp: moment(shop.boardCreatedTime).valueOf(), // 시간 순 정렬을 위한 타임스탬프 추가
                        storeContent: shop.storeContent || '0',
                        storeName: shop.storeName || '0',
                        storeCode: shop.storeCode || '0',
                        rating: shop.rating || 0,
                        storeHits: shop.storeHits || 0
                    }))
                    .sort((a, b) => b.boardCreatedTimestamp - a.boardCreatedTimestamp); // 시간 순 정렬

                setFavoriteShops(fetchedShops);
            } catch (error) {
                console.error('Error fetching favorite shops:', error);
            }
        };

        fetchFavoriteShops();
    }, [navigate]);

    const toggleHeart = async (id) => {
        const shopIndex = favoriteShops.findIndex(shop => shop.id === id);
        const shop = favoriteShops[shopIndex];
        const token = localStorage.getItem('authToken').replace(/^Bearer\s*/, '');

        try {
            await customAxios.put(`/store/${shop.id}/like`, {}, {
                headers: {
                    'Authorization': token
                }
            });

            setFavoriteShops(prevShops => {
                // 해당 가게를 목록에서 제거
                const newShops = prevShops.filter(shop => shop.id !== id);
                return newShops;
            });
        } catch (error) {
            console.error('Error toggling like:', error);
        }
    };

    const handleStoreClick = (id) => {
        console.log(id); // 개발 끝나고 디버깅할 때 지워 @@
        navigate(`/storePage/${id}`);
    };

    return (
        <div className='favoriteCards'>
            {favoriteShops.map((shop) => (
                <div key={shop.id} className="card" onClick={() => handleStoreClick(shop.id)}>
                    <div className="card-header">
                        <span className="date">{shop.boardCreatedTime}</span>
                        <span className="heart" onClick={(e) => { e.stopPropagation(); toggleHeart(shop.id); }}>
                            {shop.myheart ? '❤️' : '♡'}
                        </span>
                    </div>
                    <div className="card-body">
                        <h3 className="store-name">{shop.storeName}</h3>
                        <p></p>
                        주소 🏢
                        <p className="address">{shop.storeCode}</p>
                        <p></p>
                    </div>
                    <div className="rating-likes">
                        <div className="rating">
                            평점 ⭐ : <span>{shop.rating}</span>
                        </div>
                        <div className="likes">
                            좋아요 ❣️: {shop.storeHits}
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default FavoriteListPage;
