import React from 'react';
import "./footer.css";

function Footer(){
    if (window.location.pathname === "/RegisterPage") return null;
    return(
        <div className="footer">
            안보여서 color 넣어둠~
        </div>
    )
}

export default Footer

