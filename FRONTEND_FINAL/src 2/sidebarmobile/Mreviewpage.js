import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function ReviewPage() {
    const navigate = useNavigate();
    const [shops, setShops] = useState([]);
    const [reviews, setReviews] = useState([]);
    useEffect(() => {
        setShops([
            { name: '가게1', address: '서울시 강남구', rating: 4.5, likes: 120 },
            { name: '가게2', address: '서울시 서초구', rating: 4.7, likes: 150 },
            { name: '가게3', address: '서울시 송파구', rating: 4.3, likes: 110 },
        ]);
    }, []);
    useEffect(() => {
        setReviews([
            { shopName: '가게1', review: '맛이 좋아요' },
            { shopName: '가게2', review: '맛이 그냥그래요' },
        ]);
    }, []);

    return (
        <div>
            <div style= {{display: 'flex'}}>
            <div style = {{margin: '20px'}}>
            <h1>가게 정보</h1>
            {shops.map((shops, index) => (
                <div key={index} style={{ marginBottom: '20px' }}>
                    <p>{shops.name}</p>
                    <p>{shops.rating}</p>
                    <p>{shops.likes}</p>
                </div>
            ))}
            </div>
           
            <div style = {{margin: '20px'}}>
            <h1>내가 쓴 리뷰</h1>
            {reviews.map((review, index) => (
                <div key={index} style={{ marginBottom: '20px' }}>
                    <h2>{review.shopName}</h2>
                    <p>{review.review}</p>
                </div>
            ))}
            </div>
            </div>
            <button onClick={() => navigate('/')}>홈으로</button>
            <button onClick={()=> navigate('/writeReview')}>작성</button>
        </div>
    );
}

export default ReviewPage;
