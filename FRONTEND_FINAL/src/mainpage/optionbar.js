import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';
import "./optionbar.css";

const BASE_API_URL = 'http://3.34.86.246:8082/api';
const AUTH_API_URL = 'http://3.34.86.246:8082';

const customAxios = axios.create({
    baseURL: BASE_API_URL
});

const authAxios = axios.create({
    baseURL: AUTH_API_URL
});

function OptionBar() {
    const navigate = useNavigate();
    const [stores, setStores] = useState([]);
    const [filteredStores, setFilteredStores] = useState([]);
    const [options, setOptions] = useState({
        screen: false,
        drink: false,
        street: false,
    });
    const [isMaster, setIsMaster] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [storeName, setStoreName] = useState('');
    const [storeAddress, setStoreAddress] = useState('');
    const [storeCategory, setStoreCategory] = useState('');
    const [storeImage, setStoreImage] = useState(null);
    const [storeImagePreview, setStoreImagePreview] = useState(null);
    const [storeImageName, setStoreImageName] = useState(null);
    const [imageButtonText, setImageButtonText] = useState("사진을 골라주세요!");
    const [recommendedStores, setRecommendedStores] = useState(null);
    const [userID, setUserID] = useState([]);

    useEffect(() => {
        const fetchStores = async (likedStores = []) => {
            try {
                const storeResponse = await customAxios.get("/store/");
                const storesWithLikes = storeResponse.data.map(store => ({
                    ...store,
                    liked: likedStores.includes(store.id)
                }));
                storesWithLikes.sort((a, b) => b.liked - a.liked || b.rating - a.rating);
                setStores(storesWithLikes);
                setFilteredStores(storesWithLikes);
            } catch (error) {
                console.error('Error fetching store data:', error);
            }
        };

        const fetchUserID = async () => {
            const token = localStorage.getItem('authToken');
            console.log("token", token);

            try {
                const response = await axios.post('http://3.34.86.246:8082/user/findId', {}, {
                    headers: {
                        'Authorization': token.replace(/^Bearer\s*/, '')
                    }
                });
                console.log("response", response.data);
                setUserID(response.data); // 필요한 데이터 형식에 맞게 response.data로 설정합니다.
            } catch (error) {
                console.error('Error fetching user ID:', error);
            }
        };
        fetchUserID();
        console.log(userID)
        const fetchArray = async () => {
            try {
                const res = await axios.get(`http://3.39.59.1:5000/recommend?user_id=${userID}`);
                console.log(res.data);
                const recommendedStorePromises = res.data.map(async (id) => {
                    const storeResponse = await axios.get(`http://3.34.86.246:8082/api/store/${id}`);
                    return storeResponse.data;
                });
                const fetchedStores = await Promise.all(recommendedStorePromises);
                console.log("fetchedStores", fetchedStores);
                setRecommendedStores(fetchedStores);
            } catch (error) {
                console.error('Error fetching recommended array:', error);
            }
        };
        fetchArray();

        const fetchUserInfo = async () => {
            const token = localStorage.getItem('authToken');
            if (!token) {
                fetchStores();
                return;
            }

            try {
                const response = await authAxios.post('/user', {}, {
                    headers: {
                        'Authorization': token.replace(/^Bearer\s*/, '')
                    }
                });

                const data = response.data;
                setIsLoggedIn(true);

                if (data.username === 'master') {
                    setIsMaster(true);
                }

                fetchStores(data.likedStores);
            } catch (error) {
                console.error('Error fetching user data:', error);
                fetchStores();
            }
        };

        const fetchRecommendedStore = async () => {
            try {

            } catch (error) {
                console.error('Error fetching recommended stores:', error);
            }
        };
        fetchRecommendedStore();
        fetchUserInfo();
    }, [userID]);


    const toggleLike = async (index) => {
        if (!isLoggedIn) {
            alert("로그인 후 이용해주세요");
            return;
        }
        const store = filteredStores[index];
        const token = localStorage.getItem('authToken').replace(/^Bearer\s*/, '');

        try {
            await customAxios.put(`/store/${store.id}/like`, {}, {
                headers: {
                    'Authorization': token
                }
            });
            setFilteredStores(prevStores => {
                const newStores = prevStores.map((store, i) =>
                    i === index ? { ...store, liked: !store.liked } : store
                );
                newStores.sort((a, b) => b.liked - a.liked || b.rating - a.rating);
                return newStores;
            });
        } catch (error) {
            console.error('Error toggling like:', error);
        }
    };

    const handleStoreClick = (id) => {
        console.log('Store ID:', id);
    };

    const handleOptionChange = (option) => {
        setOptions(prevOptions => {
            const newOptions = { ...prevOptions, [option]: !prevOptions[option] };
            filterStores(stores, newOptions);
            return newOptions;
        });
    };

    const filterStores = (stores, options) => {
        const { screen, drink, street } = options;
        if (!screen && !drink && !street) {
            setFilteredStores(stores);
        } else {
            const filtered = stores.filter(store =>
                (!screen || store.screen) &&
                (!drink || store.drink) &&
                (!street || store.street)
            );
            setFilteredStores(filtered);
        }
    };

    const handleStoreSubmit = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('authToken').replace(/^Bearer\s*/, '');

        const formData = new FormData();
        formData.append('storeName', storeName);
        formData.append('storeCode', storeAddress);
        formData.append('storeContent', storeCategory);
        formData.append('screen', options.screen ? 1 : 0);
        formData.append('drink', options.drink ? 1 : 0);
        formData.append('street', options.street ? 1 : 0);
        if (storeImage) {
            formData.append('upload', storeImage);
        }

        try {
            await customAxios.post('/store/save', formData, {
                headers: {
                    'Authorization': token,
                    'Content-Type': 'multipart/form-data'
                }
            });
            alert('가게 등록이 완료되었습니다.');
            resetForm();
        } catch (error) {
            console.error('Error registering store:', error);
            alert('가게 등록 중 오류가 발생했습니다.');
        }
    };
    const handleStore = (storeId) => {
        console.log("Store ID:", storeId);
        navigate(`/storePage/${storeId}`);
    };

    const resetForm = () => {
        setStoreName('');
        setStoreAddress('');
        setStoreCategory('');
        setStoreImage(null);
        setStoreImagePreview(null);
        setStoreImageName(null);
        setImageButtonText("사진을 골라주세요!");
        setOptions({ screen: false, drink: false, street: false });
    };

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setStoreImage(file);
            setStoreImageName(file.name);
            setImageButtonText("이 사진으로 하시겠어요?");
            const reader = new FileReader();
            reader.onloadend = () => {
                setStoreImagePreview(reader.result);
            };
            reader.readAsDataURL(file);
        } else {
            resetImage();
        }
    };

    const resetImage = () => {
        setStoreImage(null);
        setStoreImagePreview(null);
        setStoreImageName(null);
        setImageButtonText("사진을 골라주세요!");
    };

    const handleImagePreviewClick = () => {
        resetImage();
    };

    return (
        <div className="option-bar">
            {isLoggedIn && isMaster ? (
                <div className="store-registration">
                    <h2>✏️ 가게 등록</h2>
                    <form onSubmit={handleStoreSubmit}>
                        <div className="form-group">
                            <label>가게 이름</label>
                            <input
                                type="text"
                                value={storeName}
                                onChange={(e) => setStoreName(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>가게 주소</label>
                            <input
                                type="text"
                                value={storeAddress}
                                onChange={(e) => setStoreAddress(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>카테고리</label>
                            <input
                                type="text"
                                value={storeCategory}
                                onChange={(e) => setStoreCategory(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>OPTIONS</label>
                            <div className="options_master">
                                <div className="option-row">
                                    <label className={options.screen ? "option-selected" : "option"}>
                                        <input
                                            type="checkbox"
                                            checked={options.screen}
                                            onChange={() => handleOptionChange("screen")}
                                        />스크린
                                    </label>
                                    <label className={options.drink ? "option-selected" : "option"}>
                                        <input
                                            type="checkbox"
                                            checked={options.drink}
                                            onChange={() => handleOptionChange("drink")}
                                        /> 드링크
                                    </label>
                                    <label className={options.street ? "option-selected" : "option"}>
                                        <input
                                            type="checkbox"
                                            checked={options.street}
                                            onChange={() => handleOptionChange("street")}
                                        /> 거리응원
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div className="form-group image-upload">
                            <label htmlFor="storeImage">COVER</label>
                            <input
                                id="storeImage"
                                type="file"
                                onChange={handleImageChange}
                                style={{ display: "none" }}
                            />
                            <div className="file-upload">
                                <button type="button" className="imagebutton"
                                        onClick={() => document.getElementById('storeImage').click()}>
                                    {imageButtonText}
                                </button>
                                <span className="file-name">{storeImageName || ""}</span>
                            </div>
                            {storeImagePreview && (
                                <img
                                    src={storeImagePreview}
                                    alt="미리보기"
                                    className="image-preview"
                                    onClick={handleImagePreviewClick}
                                />
                            )}
                        </div>
                        <button type="submit" className="submit-button">등록</button>
                    </form>
                </div>
            ) : (
                <>
                    <h2>🎯 여기 어때요?</h2>
                    {recommendedStores &&
                        <div className="store-list">
                            {recommendedStores.map((store) => (
                                <div className="store" key={store.id} onClick={() => handleStore(store.id)}>
                                    <div className="store-header">
                                        <span className="store-name">{store.storeName}</span>
                                        <span className="store-rating">
                                            ⭐ {store.rating === 0 ? '평가 없음' : store.rating}
                                        </span>
                                    </div>
                                    <div className="store-info">
                                        <span>주소: {store.storeCode}</span>
                                    </div>
                                    <div
                                        className="like-icon"
                                        onClick={(e) => {
                                            e.stopPropagation(); // 부모 요소의 클릭 이벤트 방지
                                            toggleLike(store.id);
                                        }}
                                        style={{
                                            color: store.liked ? 'red' : 'black',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        {/*{store.liked ? '♥' : '♡'}*/}
                                    </div>
                                </div>
                            ))}
                        </div>
                    }
                    <h2 className="options-header">OPTIONS</h2>
                    <div className="options">
                        <div className="option-row">
                            <label className={options.screen ? "option-selected" : "option"}>
                                <input
                                    type="checkbox"
                                    checked={options.screen}
                                    onChange={() => handleOptionChange("screen")}
                                />스크린
                            </label>
                            <label className={options.drink ? "option-selected" : "option"}>
                                <input
                                    type="checkbox"
                                    checked={options.drink}
                                    onChange={() => handleOptionChange("drink")}
                                /> 드링크
                            </label>
                            <label className={options.street ? "option-selected" : "option"}>
                                <input
                                    type="checkbox"
                                    checked={options.street}
                                    onChange={() => handleOptionChange("street")}
                                /> 거리응원
                            </label>
                        </div>
                    </div>
                    <h2>🫕 STORES</h2>
                    <div className="store-list">
                        {filteredStores.map((store, index) => (
                            <div className="store" key={index} onClick={() => handleStoreClick(store.id)}>
                                <div className="store-header">
                                    <span className="store-name" onClick={() => handleStore(store.id)} >{store.storeName}</span>
                                    <span className="store-rating">
                                        ⭐ {store.rating === 0 ? '평가 없음' : store.rating}
                                    </span>
                                </div>
                                <div className="store-info">
                                    <span>주소: {store.storeCode}</span>
                                </div>
                                <div
                                    className="like-icon"
                                    onClick={(e) => {
                                        e.stopPropagation(); // 부모 요소의 클릭 이벤트 방지
                                        toggleLike(index);
                                    }}
                                    style={{
                                        color: store.liked ? 'red' : 'black',
                                        cursor: 'pointer'
                                    }}
                                >
                                    {store.liked ? '♥' : '♡'}
                                </div>
                            </div>
                        ))}
                    </div>
                </>
            )}
        </div>
    );
}

export default OptionBar;
