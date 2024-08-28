import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Link } from 'react-router-dom';
import './header.css';

function Header() {
    const [sidebarVisible, setSidebarVisible] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isMaster, setIsMaster] = useState(false);
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        const checkLoginStatus = () => {
            const token = localStorage.getItem('authToken');
            setIsLoggedIn(!!token);
            if (token) {
                const user = JSON.parse(atob(token.split('.')[1]));
                setIsMaster(user.id === 'master');
            }
        };
        checkLoginStatus();
        const interval = setInterval(checkLoginStatus, 1000);
        return () => clearInterval(interval);
    }, []);

    useEffect(() => {
        // 페이지 경로가 변경될 때마다 사이드바를 닫기
        closeSidebar();
    }, [location]);

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        setIsLoggedIn(false);
        setIsMaster(false);
        navigate('/');
    };

    const toggleSidebar = () => {
        setSidebarVisible(!sidebarVisible);
    };

    const closeSidebar = () => {
        setSidebarVisible(false);
    };

    return (
        <div className="header">
            <button onClick={toggleSidebar} className="sidebar-toggle">☰</button>
            <div className="logo">
                <Link to="/"><img src='/homeicon.png' alt="logo" /></Link>
            </div>
            <div className="centerImage">
                <img src='/threeback.png' alt="centered" />
            </div>
            <div className="menuLogin">
                {isLoggedIn ? (
                    <div className="loginBox">
                        {isMaster && <button className="loginbutton" onClick={() => navigate('/RegisterStore')}>가게등록</button>}
                        <button className="loginbutton" onClick={handleLogout}>로그아웃</button>
                    </div>
                ) : (
                    <div className="loginBox">
                        <button className="loginbutton" onClick={() => navigate('/LoginPage')}>로그인</button>
                        <button className="loginbutton" onClick={() => navigate('/RegisterPage')}>회원가입</button>
                    </div>
                )}
            </div>

            <div className={`sidebar ${sidebarVisible ? 'sidebar-visible' : ''}`}>
                <Link to="/mypage" className="sidebar-link" onClick={closeSidebar}><p>My Page</p></Link>
                <Link to="/FavoriteListPage" className="sidebar-link" onClick={closeSidebar}><p>Favorites</p></Link>
                <Link to="/ReviewListPage" className="sidebar-link" onClick={closeSidebar}><p>Reviews</p></Link>
                <button onClick={closeSidebar} className="close-button">
                    <img src='/door.png' alt="닫기" />
                </button>
            </div>
        </div>
    );
}

export default Header;
