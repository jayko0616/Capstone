import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import './writereview.css';

function WriteReviewPage() {
    const navigate = useNavigate();
    const { id } = useParams(); // URL 파라미터에서 가게 ID를 가져옵니다.
    const [title, setTitle] = useState('');
    const [text, setText] = useState('');
    const [grade, setGrade] = useState(0); // 초기 평점을 0점으로 설정
    const [capacity, setCapacity] = useState('');
    const [image, setImage] = useState(null); // 이미지 상태
    const [imagePreview, setImagePreview] = useState(null); // 이미지 미리보기 상태
    const [isSubmitting, setIsSubmitting] = useState(false); // 추가: 제출 상태

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (isSubmitting) return;

        if (!title || !text || !grade || !capacity) {
            alert('모든 필드를 작성해 주세요.');
            return;
        }

        setIsSubmitting(true);

        const formData = new FormData();
        formData.append('commentContents', text);
        formData.append('storeId', id);
        formData.append('grade', grade);
        formData.append('capacity', capacity);
        formData.append('title', title);
        if (image) {
            formData.append('upload', image); // 이미지 파일을 'upload' 필드로 추가
        }

        const token = localStorage.getItem('authToken').replace(/^Bearer\s*/, ''); // Bearer 제거
        try {
            const response = await axios.post('http://3.34.86.246:8082/comment/save', formData, {
                headers: {
                    Authorization: token,
                    'Content-Type': 'multipart/form-data'
                }
            });
            console.log('Response:', response.data);
            navigate(`/storePage/${id}`); // 수정: StorePage로 이동
        } catch (error) {
            console.error('Error posting comment:', error);
        } finally {
            setIsSubmitting(false); // 제출 상태 해제
        }
    };

    const handleImageChange = (event) => {
        const file = event.target.files[0];
        setImage(file);
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setImagePreview(reader.result);
            };
            reader.readAsDataURL(file);
        } else {
            setImagePreview(null);
        }
    };

    const handleRatingChange = (event) => {
        const newGrade = Number(event.target.value);
        setGrade(newGrade);
        console.log('평점:', newGrade);
    };

    const handleImagePreviewClick = () => {
        setImage(null);
        setImagePreview(null);
    };

    return (
        <div className="review-container">
            <form className="review-form" onSubmit={handleSubmit}>
                <div className="title-grade-capacity">
                    <input
                        type="text"
                        placeholder="리뷰 제목 (최대 40자)"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        className="title-input"
                        maxLength={40} // 최대 글자수 설정
                    />
                    <input
                        type="number"
                        placeholder="빈 좌석"
                        value={capacity}
                        onChange={(e) => setCapacity(e.target.value)}
                        className="capacity-input"
                        min="0" // 최소값을 0으로 설정
                    />
                    <div className="rating">
                        <input value="5" name="rate" id="star5" type="radio" checked={grade === 5} onChange={handleRatingChange} />
                        <label title="5 stars" htmlFor="star5"></label>
                        <input value="4" name="rate" id="star4" type="radio" checked={grade === 4} onChange={handleRatingChange} />
                        <label title="4 stars" htmlFor="star4"></label>
                        <input value="3" name="rate" id="star3" type="radio" checked={grade === 3} onChange={handleRatingChange} />
                        <label title="3 stars" htmlFor="star3"></label>
                        <input value="2" name="rate" id="star2" type="radio" checked={grade === 2} onChange={handleRatingChange} />
                        <label title="2 stars" htmlFor="star2"></label>
                        <input value="1" name="rate" id="star1" type="radio" checked={grade === 1} onChange={handleRatingChange} />
                        <label title="1 star" htmlFor="star1"></label>
                    </div>
                </div>
                <textarea
                    placeholder="리뷰 내용(최대 250자)"
                    value={text}
                    onChange={(e) => setText(e.target.value)}
                    className="text-input" // Added className for the text area
                ></textarea>
                <div className="image-upload">
                    <label htmlFor="storeImage">COVER</label>
                    <input
                        id="storeImage"
                        type="file"
                        onChange={handleImageChange}
                        style={{ display: "none" }}
                    />
                    <div className="file-upload">
                        <button type="button" className="imagebuttonR"
                                onClick={() => document.getElementById('storeImage').click()}>
                            사진 선택
                        </button>
                        <span className="file-name">{image ? image.name : ""}</span>
                    </div>
                    {imagePreview && (
                        <img
                            src={imagePreview}
                            alt="미리보기"
                            className="image-preview"
                            onClick={handleImagePreviewClick}
                        />
                    )}
                </div>
                <button type="submit" disabled={isSubmitting}>리뷰 등록</button>
            </form>
        </div>
    );
}

export default WriteReviewPage;
