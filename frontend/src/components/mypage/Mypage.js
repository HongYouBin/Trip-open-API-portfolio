// src/components/mypage/Mypage.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './Mypage.module.css';

import basicHttp from '../../api/basicHttp';
import tokenHttp from '../../api/tokenHttp';
import copyImg from '../../assets/img/copy.png';

// 리덕스 사용, tokenHttp 쓸때 추가해주세요
import { useDispatch } from 'react-redux';
import { logout } from '../../redux/userInfo';

const Mypage = () => {
    const [apiToken, setApiToken] = useState('');
    const handleapiToken = (e) => {
        setApiToken(e.target.value);
    };

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const handleCopyClipBoardToken = async (text) => {
        try {
            await navigator.clipboard.writeText(text.apiToken);
        } catch (error) {}
    };

    useEffect(() => {
        const getUserInfo = async () => {
            try {
                const res = await tokenHttp.get(`/docs/user`);
                setApiToken(res.data.data.api_token);
            } catch (error) {
                console.log(error.message);
                if (error.message === 'no token' || error.message === 'expire token') {
                    dispatch(logout());
                    alert('다시 로그인해주세요(토큰 만료)');
                    navigate('/login'); // 토큰없음이나 토큰만료 에러발생시 로그인화면으로 이동
                }
            }
        };
        getUserInfo();
    }, []);

    // input value 수정되면 set해주는 함수
    async function onClickapiTokenRefreshBtn() {
        try {
            const res = await tokenHttp.put(`/docs/token`);
            setApiToken(res.data.data.api_token);
            alert('api토큰 재발급 성공');
        } catch (error) {
            if (error.message === 'no token' || error.message === 'expire token') {
                dispatch(logout());
                alert('다시 로그인해주세요(토큰 만료)');
                navigate('/login'); // 토큰없음이나 토큰만료 에러발생시 로그인화면으로 이동
            }
            console.error('api토큰 재발급  실패:', error);
            alert('api토큰 재발급  실패');
        }
    }

    return (
        <div className={styles.mypageBody}>
            <div className={styles.mypageContainer}>
                <div className={styles.logoText}>내 API 토큰</div>
                <div className={styles.textBox}>
                    {apiToken}
                    <img alt="" src={copyImg} onClick={() => handleCopyClipBoardToken({ apiToken })} />
                </div>
                <button className={styles.apiTokenRefreshBtn} onClick={onClickapiTokenRefreshBtn}>
                    토큰 재발급
                </button>
            </div>
        </div>
    );
};

export default Mypage;
