import React, {useState} from 'react';
import InputField from './InputField';
import CheckboxField from './CheckboxField';
import './registerpage.css';
import axios from "axios";
import {useNavigate} from "react-router-dom";

const API_BASE_URL = 'http://3.34.86.246:8082';

function RegisterPage() {
    const [termsAgreed, setTermsAgreed] = useState({
        serviceTerms: true,
        privacyPolicy: true,
        thirdParty: true,
    });
    const [id, setId] = useState('');
    const [password, setPassword] = useState('');
    const [phoneNumber, setPhone] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const navigate = useNavigate();

    const handleSignup = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post(`${API_BASE_URL}/join`, {
                username: id,
                password: password,
                phoneNumber: phoneNumber
            });
            console.log(response.data);
            alert('회원가입이 완료되었습니다!');
            navigate('/LoginPage'); // 회원가입 후 로그인 페이지로 리디렉션
        } catch (error) {
            alert("회원가입에 실패했습니다!");
        }
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        if (Object.values(termsAgreed).every(Boolean)) {
            alert('가입이 완료되었습니다!');
        } else {
            alert('모든 필수 항목을 확인해주세요.');
        }
    };

    const handleIdCheck = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post(`${API_BASE_URL}/checkUsername`, {
                username: id
            });
            console.log(response.data); // 서버로부터의 응답을 콘솔에 출력합니다.
            alert('사용 가능한 아이디입니다');
        } catch (error) {
            console.error('Error during ID check:', error);
            alert('이미 사용중인 아이디입니다');
        }
    };

    const handlePasswordCheck = (event) => {
        event.preventDefault();
        console.log(`Password: ${password}, ConfirmPassword: ${confirmPassword}`);  // 콘솔로 확인
        if (password !== confirmPassword) {
            alert('비밀번호가 일치하지 않습니다.');
        } else {
            alert('인증 완료');
        }
    };

    const handleCheckboxChange = (event) => {
        setTermsAgreed({ ...termsAgreed, [event.target.name]: event.target.checked });
    };

    return (
        <div className="signup">
            <form onSubmit={handleSubmit}>
                <InputField type="RegisterId" name="id" placeholder="ID" value={id} onChange={(e) => setId(e.target.value)}/>
                <button type="button" className="checkId" onClick={handleIdCheck}>아이디중복확인</button>
                <InputField type="tel" name="phoneNumber" placeholder="전화번호" value={phoneNumber} onChange={(e)=>setPhone(e.target.value)}/>
                <InputField type="RegisterPw" name="password" placeholder="Password" value={password}
                            onChange={(e) => setPassword(e.target.value)}/>
                <InputField type="RePw" name="confirmPassword" placeholder="비밀번호 재확인" value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}/>
                <button type="button" className="verify-button" onClick={handlePasswordCheck}>비밀번호 확인</button>

                {['이용약관', '개인정보 이용약관', '제3자 정보제공'].map(term => (
                    <CheckboxField key={term} label={`[필수] ${term.replace(/([A-Z])/g, ' $1').trim()}`}
                                   checked={termsAgreed[term]} onChange={handleCheckboxChange} name={term}/>
                ))}
                <button type="submit" className="signup-button" onClick={handleSignup}>회원 가입</button>
            </form>
        </div>
    );
}

export default RegisterPage;
