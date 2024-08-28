import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Writereviewpage() {
    const navigate = useNavigate();
    const [text, setText] = useState('');
    return (
        <div>
           <input type="text" placeholder="리뷰 내용" value={text} onChange={(e) => setText(e.target.value)}/>
           <button onClick={()=> navigate('/reviewpage')}>완료</button>
        </div>
    );
}
export default Writereviewpage;