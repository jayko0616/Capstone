import React from "react";
import {useNavigate} from "react-router-dom";


function FavoritelistPage(){
    const navigate = useNavigate();

    return (
        <div>
            <h1 onClick={() => navigate('/FavoritePage')} style={{ cursor: 'pointer' }}>this is Favoritelist page?</h1>
        </div>
    );
}

export default FavoritelistPage;