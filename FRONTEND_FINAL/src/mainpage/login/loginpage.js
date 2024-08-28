import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './loginpage.css';
import axios from "axios";

const API_BASE_URL = 'http://3.34.86.246:8082';

function LoginPage() {
    const [id, setId] = useState('');
    const [pw, setPw] = useState('');
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        // 컴포넌트가 마운트될 때 로컬 저장소에서 토큰을 검사하여 로그인 상태를 결정
        const token = localStorage.getItem('authToken');
        if (token) {
            setIsLoggedIn(true);
        }
    }, []);

    const handleLogin = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post(`${API_BASE_URL}/login`, {
                username: id,
                password: pw
            });
            const authToken = response.headers['authorization'];
            if (authToken) {
                localStorage.setItem('authToken', authToken);
                setIsLoggedIn(true);
                alert('환영합니다!');
                console.log(authToken);
                navigate('/');  // 로그인 후 사용자를 홈페이지로 리디렉션
            } else {
                throw new Error('Authorization token not found');
            }
        } catch (error) {
            console.error('로그인 실패:', error);
            alert("로그인에 실패했습니다!");
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('authToken');  // 로컬 저장소에서 토큰 제거
        setIsLoggedIn(false);
        navigate('/');  // 로그아웃 후 홈으로 리디렉션
    };

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const handleRegisterClick = () => {
        navigate('/RegisterPage');  // 회원가입 페이지로 이동
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleLogin(event);
        }
    };

    return (
        <div className="login-container">
            {!isLoggedIn ? (
                <div className="login-form">
                    <h2 className="login-header">L O G I N</h2>
                    <div className="input-group">
                        <input
                            type="text"
                            placeholder="아이디"
                            value={id}
                            onChange={(e) => setId(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                    </div>
                    <div className="input-group">
                        <input
                            type={showPassword ? "text" : "password"}
                            placeholder="비밀번호"
                            value={pw}
                            onChange={(e) => setPw(e.target.value)}
                            onKeyDown={handleKeyDown}
                        />
                        <span onClick={togglePasswordVisibility}>
                            {showPassword ? '🙂' : '😎'}
                        </span>
                    </div>
                    <button onClick={handleLogin}>로그인</button>
                    <div className="askRegister">
                        아직 계정이 없으세요? <span className="register-link" onClick={handleRegisterClick}>회원가입</span>
                    </div>
                </div>
            ) : (
                <button className="logout-button" onClick={handleLogout}>로그아웃</button>
            )}
        </div>
    );
}

export default LoginPage;
