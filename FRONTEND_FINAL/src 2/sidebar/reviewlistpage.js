import React, { useState, useEffect, memo } from 'react';
import axios from 'axios';
import moment from 'moment';
import './reviewlistpage.css';
import { useNavigate } from 'react-router-dom';

// API 기본 URL을 상수로 정의
const BASE_API_URL = 'http://3.34.86.246:8082';
const COMMENT_API_URL = `${BASE_API_URL}/comment`;
const STORE_API_URL = `${BASE_API_URL}/api/store`;

function ReviewListPage() {
    const navigate = useNavigate();
    const [reviews, setReviews] = useState([]);

    useEffect(() => {
        const fetchUserReviews = async () => {
            const token = localStorage.getItem('authToken');
            if (!token) {
                navigate('/LoginPage');
                return;
            }

            try {
                const response = await axios.get(`${COMMENT_API_URL}/user`, {
                    headers: {
                        'Authorization': token.replace(/^Bearer\s*/, '')
                    }
                });

                console.log("User Reviews API response:", response);
                console.log(token);

                const reviewData = response.data;
                const storeDetailsPromises = reviewData.map(async (review) => {
                    const storeResponse = await axios.get(`${STORE_API_URL}/${review.storeId}`);
                    return {
                        ...review,
                        storeName: storeResponse.data.storeName,
                        storeContent: storeResponse.data.storeContent,
                        commentCreatedTime: moment(review.commentCreatedTime).format('YYYY-MM-DD'),
                        formattedCreatedTime: formatTime(review.commentCreatedTime),
                        timeAgo: formatTimeAgo(review.commentCreatedTime)
                    };
                });

                const fetchedReviews = await Promise.all(storeDetailsPromises);

                const uniqueReviews = fetchedReviews.filter((review, index, self) =>
                        index === self.findIndex((r) => (
                            r.storeId === review.storeId && r.commentCreatedTime === review.commentCreatedTime && r.commentContents === review.commentContents
                        ))
                );

                setReviews(uniqueReviews);
            } catch (error) {
                console.error('Error fetching user reviews or store details:', error);
            }
        };

        fetchUserReviews();
    }, [navigate]);

    const formatTime = (date) => {
        return moment(date).format('YYYY년 M월 D일');
    };

    const formatTimeAgo = (date) => {
        const now = moment();
        const then = moment(date);
        const diffDays = now.diff(then, 'days');
        const diffHours = now.diff(then, 'hours');

        if (diffDays > 0) {
            return `${diffDays}일 전`;
        } else {
            return `${diffHours}시간 전}`;
        }
    };

    const handleStoreClick = (storeId) => {
        console.log("Store ID:", storeId);
        navigate(`/storePage/${storeId}`);
    };

    const handleDeleteClick = async (reviewId) => {
        const token = localStorage.getItem('authToken');
        try {
            await axios.delete(`${COMMENT_API_URL}/delete/${reviewId}`, {
                headers: {
                    'Authorization': token.replace(/^Bearer\s*/, '')
                }
            });
            setReviews(reviews.filter(review => review.id !== reviewId));
        } catch (error) {
            console.error('Error deleting review:', error);
        }
    };

    const renderStars = (grade) => {
        const stars = [];
        for (let i = 1; i <= 5; i++) {
            stars.push(
                <label key={i} className={i <= grade ? "filled" : ""}>★</label>
            );
        }
        return stars;
    };

    return (
        <div className="review-list-page">
            <div className="review-cards-container">
                {reviews.map((review) => (
                    <div className={`review-card ${review.grade >= 2.5 ? 'good-grade' : 'bad-grade'}`} key={review.id} onClick={() => handleStoreClick(review.storeId)}>
                        <div className="review-card-header">
                            <div>{review.formattedCreatedTime} ({review.timeAgo})</div>
                            <div className="store-name-container">
                                <div>{review.storeContent}</div>
                            </div>
                        </div>
                        <div className="review-card-storeName">
                            <div>{review.storeName}</div>
                        </div>
                        <div className="review-card-body">
                            <textarea readOnly value={review.commentContents}></textarea>
                        </div>
                        <div className="review-card-footer">
                            <div className="footer-left">
                                <div className="stars">
                                    {renderStars(review.grade)}
                                </div>
                            </div>
                            <button className="delete-button" onClick={(e) => { e.stopPropagation(); handleDeleteClick(review.id); }}>
                                <img src="/garbage.png" alt="Delete" className="delete-icon" />
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default memo(ReviewListPage);
