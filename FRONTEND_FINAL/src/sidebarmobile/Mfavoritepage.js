import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function FavoritePage() {
    const navigate = useNavigate();
    const [favoriteShops, setFavoriteShops] = useState([]);

    useEffect(() => {
        setFavoriteShops([
            { name: '가게1', address: '서울시 강남구', rating: 4.5, likes: 120 },
            { name: '가게2', address: '서울시 서초구', rating: 4.7, likes: 150 },
            { name: '가게3', address: '서울시 송파구', rating: 4.3, likes: 110 },
        ]);
    }, []);

    return (
        <div>
            <table>
                <thead>
                <tr>
                    <th>가게이름</th>
                    <th>가게주소</th>
                    <th>가게평점</th>
                    <th>좋아요 수</th>
                </tr>
                </thead>
                <tbody>
                {favoriteShops.map((shop, index) => (
                    <tr key={index}>
                        <td>{shop.name}</td>
                        <td>{shop.address}</td>
                        <td>{shop.rating}</td>
                        <td>{shop.likes}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button onClick={() => navigate('/')}>홈으로</button>
        </div>
    );
}

export default FavoritePage;
