import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import moment from 'moment';
import 'moment/locale/ko'; // 한국어 로케일 추가
import './storepage.css';

const BASE_API_URL = 'http://3.34.86.246:8082/api';
const AUTH_API_URL = 'http://3.34.86.246:8082';

const customAxios = axios.create({
    baseURL: BASE_API_URL
});

const authAxios = axios.create({
    baseURL: AUTH_API_URL
});

function StorePage() {
    const navigate = useNavigate();
    const { id } = useParams(); // URL 파라미터에서 가게 ID를 가져옵니다.
    const [storeData, setStoreData] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [currentUserCommentWriter, setCurrentUserCommentWriter] = useState(null); // 현재 로그인한 사용자 commentWriter
    const [liked, setLiked] = useState(false); // 좋아요 상태 관리

    useEffect(() => {
        // 현재 로그인한 사용자 commentWriter 가져오기 (예: 토큰에서 가져오기)
        const token = localStorage.getItem('authToken');
        if (token) {
            authAxios.get('/comment/user', {
                headers: {
                    Authorization: token.replace(/^Bearer\s*/, '')
                }
            })
                .then(response => {
                    const userData = response.data;
                    setCurrentUserCommentWriter(userData[1].commentWriter);
                })
                .catch(error => {
                    console.error('Error fetching user data:', error);
                });
        }

        customAxios.get(`/store/${id}`)
            .then(response => {
                console.log('Server response:', response.data);
                setStoreData(response.data);
                setLiked(response.data.liked); // 서버로부터 좋아요 상태를 받아옴
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, [id]);

    useEffect(() => {
        customAxios.get(`/store/${id}/comments`)
            .then(response => {
                console.log('Reviews:', response.data);
                setReviews(response.data);
            })
            .catch(error => {
                console.error('Error fetching reviews:', error);
            });
    }, [id]);

    const handleReviewClick = (reviewId) => {
        console.log('Review ID:', reviewId);
    };

    const handleDeleteClick = async (reviewId) => {
        if (window.confirm('리뷰를 삭제하시겠습니까?')) {
            const token = localStorage.getItem('authToken');
            try {
                await authAxios.delete(`/comment/delete/${reviewId}`, {
                    headers: {
                        Authorization: token.replace(/^Bearer\s*/, '')
                    }
                });
                // 삭제 후 리뷰 목록 갱신
                setReviews(reviews.filter(review => review.id !== reviewId));
            } catch (error) {
                console.error('Error deleting review:', error);
            }
        }
    };

    const formatDate = (date) => {
        const now = moment();
        const created = moment(date);
        const diffDays = now.diff(created, 'days');
        const diffHours = now.diff(created, 'hours');

        if (diffDays > 0) {
            return `${created.format('YYYY년 M월 D일')} (${diffDays}일 전)`;
        } else {
            return `${created.format('YYYY년 M월 D일')} (${diffHours}시간 전)`;
        }
    };

    const renderStars = (grade) => {
        const stars = [];
        for (let i = 1; i <= 5; i++) {
            stars.push(
                <label key={i} className={i <= grade ? "filled" : ""}>★</label>
            );
        }
        return <div className="stars">{stars}</div>;
    };

    const maskCommentWriter = (commentWriter) => {
        if (!commentWriter) return '';
        if (commentWriter.length <= 4) return commentWriter; // 4글자 이하일 때는 그대로 표시
        return commentWriter.slice(0, 4) + '*'.repeat(commentWriter.length - 4);
    };

    const toggleLike = async () => {
        const token = localStorage.getItem('authToken').replace(/^Bearer\s*/, '');
        try {
            await customAxios.put(`/store/${id}/like`, {}, {
                headers: {
                    Authorization: token
                }
            });
            setLiked(!liked); // 좋아요 상태를 토글
        } catch (error) {
            console.error('Error toggling like:', error);
        }
    };

    return (
        <div className="store-container">
            <div className="store-image">
                {storeData && storeData.imageUrl ? (
                    <img src={storeData.imageUrl} alt={storeData.storeContent} className="store-img" />
                ) : (
                    <img src="/notready.png" alt="Not Ready" className="store-img" />
                )}
            </div>
            {storeData ? (
                <div className="store-info">
                    <div className="store-header">
                        <h2 className="store-name">
                            {storeData.storeName}
                            <div className="heart-container" title="Like" onClick={toggleLike}>
                                <input type="checkbox" className="checkbox" id="Give-It-An-Id" checked={liked} readOnly />
                                <div className="svg-container">
                                    <svg viewBox="0 0 24 24" className="svg-outline" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M17.5,1.917a6.4,6.4,0,0,0-5.5,3.3,6.4,6.4,0,0,0-5.5-3.3A6.8,6.8,0,0,0,0,8.967c0,4.547,4.786,9.513,8.8,12.88a4.974,4.974,0,0,0,6.4,0C19.214,18.48,24,13.514,24,8.967A6.8,6.8,0,0,0,17.5,1.917Zm-3.585,18.4a2.973,2.973,0,0,1-3.83,0C4.947,16.006,2,11.87,2,8.967a4.8,4.8,0,0,1,4.5-5.05A4.8,4.8,0,0,1,11,8.967a1,1,0,0,0,2,0,4.8,4.8,0,0,1,4.5-5.05A4.8,4.8,0,0,1,22,8.967C22,11.87,19.053,16.006,13.915,20.313Z">
                                        </path>
                                    </svg>
                                    <svg viewBox="0 0 24 24" className="svg-filled" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M17.5,1.917a6.4,6.4,0,0,0-5.5,3.3,6.4,6.4,0,0,0-5.5-3.3A6.8,6.8,0,0,0,0,8.967c0,4.547,4.786,9.513,8.8,12.88a4.974,4.974,0,0,0,6.4,0C19.214,18.48,24,13.514,24,8.967A6.8,6.8,0,0,0,17.5,1.917Z">
                                        </path>
                                    </svg>
                                    <svg className="svg-celebrate" width="100" height="100" xmlns="http://www.w3.org/2000/svg">
                                        <polygon points="10,10 20,20"></polygon>
                                        <polygon points="10,50 20,50"></polygon>
                                        <polygon points="20,80 30,70"></polygon>
                                        <polygon points="90,10 80,20"></polygon>
                                        <polygon points="90,50 80,50"></polygon>
                                        <polygon points="80,80 70,70"></polygon>
                                    </svg>
                                </div>
                            </div>
                        </h2>
                        <button className="write-review-button" onClick={() => navigate(`/writeReview/${id}`)}>리뷰 남기기 ✏️</button>
                    </div>
                    <span className="store-content">{storeData.storeContent}</span>
                    <div></div>
                    <p className="store-address">{storeData.storeCode}</p>
                    <p className="store-metrics">
                        ⭐ {storeData.rating || '0점'}
                        <span className="rating-separator"></span> {/* 간격을 위한 span 요소 */}
                        ❤️ {storeData.storeHits + "개" || '0개'}
                        <span className="rating-separator"></span> {/* 간격을 위한 span 요소 */}
                        {"빈자리:" + storeData.capacity}
                    </p>
                </div>
            ) : (
                <p>Loading...</p>
            )}

            <hr className="divider" />

            <div className="reviews-container">
                {reviews.length > 0 ? (
                    reviews.map(review => (
                        <div
                            key={review.id}
                            className="review"
                            onClick={() => handleReviewClick(review.id)}
                        >
                            <div className="review-header">
                                <span>{formatDate(review.commentCreatedTime)}</span>
                                <span className="review-writer">작성자: {maskCommentWriter(review.commentWriter)}</span>
                            </div>
                            <div className="review-grade">
                                {renderStars(review.grade)}
                            </div>
                            <div className="review-content-box">
                                <p>{review.commentContents}</p>
                                {review.imageUrl && (
                                    <>
                                        <div className="review-divider"></div>
                                        <div className="review-image">
                                            <img src={review.imageUrl} alt="Review" />
                                        </div>
                                    </>
                                )}
                            </div>
                            <div className="reviewFooter">
                                {review.commentWriter === currentUserCommentWriter && (
                                    <button className="delete-button" onClick={(e) => { e.stopPropagation(); handleDeleteClick(review.id); }}>
                                        <img src="/garbage.png" alt="Delete" />
                                    </button>
                                )}
                            </div>
                        </div>
                    ))
                ) : (
                    <p>리뷰가 없어요</p>
                )}
            </div>
        </div>
    );
}

export default StorePage;
