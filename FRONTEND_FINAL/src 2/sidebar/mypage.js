import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './mypage.css';
import axios from 'axios';

// API 기본 URL을 상수로 정의
const BASE_API_URL = 'http://3.34.86.246:8082';
const USER_API_URL = `${BASE_API_URL}/user`;

function MyPage() {
    const navigate = useNavigate();
    const [userInfo, setUserInfo] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUserInfo = async () => {
            const token = localStorage.getItem('authToken');
            if (!token) {
                navigate('/LoginPage'); // 로그인하지 않은 경우 로그인 페이지로 리디렉션
                return;
            }

            // Bearer 제거 및 공백 제거
            const formattedToken = token.replace(/^Bearer\s*/, '');

            try {
                const response = await axios.post(USER_API_URL, {}, {
                    headers: {
                        'Authorization': formattedToken
                    }
                });

                const data = response.data;

                // null 체크를 추가하여 안전하게 처리
                if (!data) {
                    console.error('Received null or undefined user data.');
                    setUserInfo({
                        likedStores: [],
                        commentedStores: []
                    });
                } else {
                    const userInfoWithDefaults = {
                        ...data,
                        likedStores: data.likedStores || [],
                        commentedStores: data.commentedStores || []
                    };

                    // 배열의 첫 번째 인덱스가 -1인 경우 0으로 처리
                    userInfoWithDefaults.likedStores = userInfoWithDefaults.likedStores[0] === -1 ? [] : userInfoWithDefaults.likedStores;
                    userInfoWithDefaults.commentedStores = userInfoWithDefaults.commentedStores[0] === -1 ? [] : userInfoWithDefaults.commentedStores;

                    console.log('Liked Stores:', userInfoWithDefaults.likedStores.length);
                    console.log('Commented Stores:', userInfoWithDefaults.commentedStores.length);

                    setUserInfo(userInfoWithDefaults);
                }
            } catch (error) {
                console.error('사용자 정보를 불러오는데 실패했습니다:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchUserInfo();
    }, [navigate]);

    const handleUnsubscribe = async () => {
        const isConfirmed = window.confirm('정말로 회원을 탈퇴하시겠습니까?');
        if (isConfirmed) {
            try {
                const token = localStorage.getItem('authToken');
                if (!token) {
                    navigate('/login'); // 로그인하지 않은 경우 로그인 페이지로 리디렉션
                    return;
                }

                // Bearer 제거 및 공백 제거
                const formattedToken = token.replace(/^Bearer\s*/, '');

                await axios.delete(`${USER_API_URL}/delete`, {
                    headers: {
                        'Authorization': formattedToken
                    }
                });

                // 회원 탈퇴 성공 시 홈 페이지로 리디렉션
                localStorage.removeItem('authToken');
                navigate('/');
            } catch (error) {
                console.error('회원 탈퇴에 실패했습니다:', error);
            }
        }
    };

    if (loading) {
        return <div>로딩 중...</div>;
    }

    if (!userInfo) {
        return <div>사용자 정보를 불러올 수 없습니다.</div>;
    }

    return (
        <div className="mypage-container">
            <h3 className = "infohead">내 정보</h3>
            <div className="box1">
                <div className="profile-picture"></div>
                <div className="user-info">
                    <p>ID: {userInfo.username}</p>
                    <p>전화번호: {userInfo.phoneNumber}</p>
                </div>
            </div>
            <h3>나의 활동</h3>
            <div className="box2">
                <p className="interactive-info" onClick={() => navigate('/FavoriteListPage')}>
                    내가 좋아요한 가게 수: {userInfo.likedStores[0] === -1 ? 0 : userInfo.likedStores.length}
                </p>
            </div>
            <div className="box2">
                <p className="interactive-info" onClick={() => navigate('/ReviewListPage')}>
                    내가 작성한 리뷰 수: {userInfo.commentedStores[0] === -1 ? 0 : userInfo.commentedStores.length}
                </p>
            </div>
            <div>
                <button className="quit" onClick={handleUnsubscribe}>회원 탈퇴</button>
            </div>
        </div>
    );
}

export default MyPage;
