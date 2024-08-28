
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './mypage.css'; // CSS 파일 임포트

function MyPage() {
    const navigate = useNavigate();
    const [userInfo] = useState({
        id: 'mobile',
        phone: '010-2060-7486',
        likes: 24, // 좋아요한 가게 수 예시
        reviews: 12 // 작성한 리뷰 수 예시
    });

    const handleUnsubscribe = () => {
        const isConfirmed = window.confirm('정말로 회원을 탈퇴하시겠습니까?');
        if (isConfirmed) {
            console.log('회원 탈퇴 처리');
            // 회원 탈퇴 로직 구현
        }
    };

    return (
        <div className="mypage-container">
            <div className="profile-picture"></div>
            <div className="user-info">
                <p>ID: {userInfo.id}</p>
                <p>전화번호: {userInfo.phone}</p>
                <p className="interactive-info" onClick={() => navigate('/favoritelistpage')}>좋아요한 가게
                    수: {userInfo.likes}</p>
                <p className="interactive-info" onClick={() => navigate('/reviewlistpage')}>작성한 리뷰 수: {userInfo.reviews}</p>
            </div>
            <div className="actions">
                <button onClick={handleUnsubscribe}>회원 탈퇴</button>
                <button onClick={() => navigate('/')}>홈으로</button>
            </div>
        </div>
    );
}

export default MyPage;
