import React from 'react';
import { useNavigate } from 'react-router-dom';

function ReviewlistPage() {
    const navigate = useNavigate();

    return (
        <div>
            <h1 onClick={() => navigate('/ReviewPage')} style={{ cursor: 'pointer' }}>this is review page?</h1>
        </div>
    );
}

export default ReviewlistPage;
