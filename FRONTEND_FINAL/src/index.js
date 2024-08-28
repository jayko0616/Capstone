import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css'; // 전역 스타일 시트
import App from './App'; // App 컴포넌트 불러오기
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <App/>
);

// 성능 측정 함수 (필요에 따라 사용)
reportWebVitals();
