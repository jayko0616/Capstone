import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import './storereviews.css';

// API 기본 URL을 상수로 정의
const BASE_API_URL = 'http://3.34.86.246:8082/api';

function StoreReviews() {
    const { id } = useParams(); // URL 파라미터에서 가게 ID를 가져옵니다.
    const [reviews, setReviews] = useState([]);

    useEffect(() => {
        axios.get(`${BASE_API_URL}/store/${id}/comments`)
            .then(response => {
                console.log('Reviews:', response.data);
                setReviews(response.data);
            })
            .catch(error => {
                console.error('Error fetching reviews:', error);
            });
    }, [id]);

    return (
        <div className="reviews-container">
            {reviews.length > 0 ? (
                reviews.map(review => (
                    <div key={review.id} className="review">
                        <h3>{review.commentContents}</h3>
                        <p>작성자: {review.commentWriter}</p>
                        <p>평점: {review.grade}</p>
                        <p>리뷰 : {review.commentContents}</p>
                    </div>
                ))
            ) : (
                <p>No reviews available</p>
            )}
        </div>
    );
}

export default StoreReviews;
