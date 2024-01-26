import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import basicHttp from "../../api/basicHttp";

import styles from "./Signup.module.css";

const Signup = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [authCode, setAuthCode] = useState("");
  const [isAuthorized, setIsAuthorized] = useState(false);
  const [isCodeShow, setIsCodeShow] = useState(false);
  const [isBtn, setIsBtn] = useState(false);
  const [isButtonDisabled, setIsButtonDisabled] = useState(false);

  const MINUTES_IN_MS = 5 * 60 * 1000;
  const INTERVAL = 1000;
  const [timeLeft, setTimeLeft] = useState(MINUTES_IN_MS);

  const minutes = String(Math.floor((timeLeft / (1000 * 60)) % 60)).padStart(
    2,
    "0"
  );
  const seconds = String(Math.floor((timeLeft / 1000) % 60)).padStart(2, "0");

  useEffect(() => {
    if (isBtn) {
      const timer = setInterval(() => {
        setTimeLeft((prevTime) => prevTime - INTERVAL);
      }, INTERVAL);

      if (timeLeft <= 0) {
        clearInterval(timer);
        setIsBtn(false);
        console.log("타이머가 종료되었습니다.");
      }

      return () => {
        clearInterval(timer);
      };
    }
  }, [timeLeft, isCodeShow, isBtn]);

  const handleEmail = (e) => {
    setEmail(e.target.value);
  };
  const handlePassword = (e) => {
    setPassword(e.target.value);
  };
  const handlePasswordConfirm = (e) => {
    setPasswordConfirm(e.target.value);
  };
  const handleAuthCode = (e) => {
    setAuthCode(e.target.value);
  };

  async function checkEmailAuthor() {
    if (email) {
      try {
        const res = await basicHttp.get(`/docs/email/${email}`);
        console.log(res);
        alert("해당 이메일로 인증번호를 보냈습니다!");
        setIsCodeShow(true);
        setIsBtn(true);
        setTimeLeft(MINUTES_IN_MS);
        setIsButtonDisabled(true);

        setTimeout(() => {
          setIsButtonDisabled(false);
        }, 15000);// 15초 후에 버튼 활성화
      } catch (error) {
        console.log(error);
      }
    } else {
      alert("이메일을 입력해주세요.");
    }
  }
  // 이메일 인증번호 확인하기 버튼
  async function onClickAuthorize() {
    if (!authCode) {
      alert("인증번호를 입력해주세요");
      return;
    }
    try {
      const res = await basicHttp.get(`/docs/email/auth/${email}/${authCode}`);
      console.log(res);
      setIsAuthorized(true);
      alert("이메일 인증이 완료되었습니다!");
    } catch (error) {
      console.error("인증 실패:", error);
      if (error.data.code === "9004") {
        alert("인증코드 시간이 초과되었습니다. ");
      } else {
        alert("인증번호가 올바르지 않습니다.");
      }
    }
  }

  const navigate = useNavigate();

  // 회원가입 버튼 클릭
  async function onClickRegister() {
    if (!email) {
      alert("이메일을 입력해주세요");
      return;
    }
    if (!password) {
      alert("비밀번호를 입력해주세요");
      return;
    }
    if (password !== passwordConfirm) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      return;
    }
    if (!isAuthorized) {
      alert("이메일 인증을 완료해주세요");
      return;
    }

    const userData = {
      id: email,
      password: password,
      code: authCode,
    };

    try {
      const res = await basicHttp.post(`/docs/service/login/signup`, userData);
      console.log(res);
      console.log("회원가입 성공");
      navigate("/login");
      alert("회원가입 성공");
    } catch (error) {
      console.error("회원가입 실패:", error);
      alert("회원가입 실패");
    }
  }

  return (
    <div className={styles.signupBody}>
      <div className={styles.signupContainer}>
        <div className={styles.logoText}>환영합니다!</div>
        <div className={styles.signupBox}>
          <label htmlFor="email" className={styles.signupText}>
            아이디(이메일)
          </label>
          <div className={styles.signupInnerBox}>
            <input
              type="email"
              id="email"
              value={email}
              onChange={handleEmail}
              className={styles.signupInput}
              placeholder="아이디(이메일)를 입력해주세요"
            />
            <button
            className={isButtonDisabled ? styles.disabledButton : styles.checkBtn}
              onClick={checkEmailAuthor}
              disabled={isButtonDisabled}
            >
              인증요청
            </button>
          </div>
        </div>
        <div className={styles.signupBox}>
          <label htmlFor="email" className={styles.signupText}>
            인증번호
          </label>
          <div className={styles.signupInnerBox}>
            <input
              style={{ margin: "0px" }}
              type="text"
              id="authCode"
              value={authCode}
              onChange={handleAuthCode}
              className={styles.signupInput}
              placeholder="인증번호 4자리를 입력해주세요"
            />
            <button onClick={onClickAuthorize} className={styles.checkBtn}>
              본인인증
            </button>
          </div>
          <p className={isCodeShow ? styles.authTimerY : styles.authTimerN}>
            {minutes}:{seconds}
          </p>
        </div>
        <div className={styles.signupBox}>
          <label htmlFor="password" className={styles.signupText}>
            비밀번호
          </label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={handlePassword}
            className={styles.signupInput}
            placeholder="비밀번호를 입력해주세요"
          />
        </div>
        <div className={styles.signupBox}>
          <label htmlFor="passwordConfirm" className={styles.signupText}>
            비밀번호 확인
          </label>
          <input
            type="password"
            id="passwordConfirm"
            value={passwordConfirm}
            onChange={handlePasswordConfirm}
            className={styles.signupInput}
            placeholder="비밀번호를 입력해주세요"
          />
        </div>
        <button className={styles.signupBtn} onClick={onClickRegister}>
          가입하기
        </button>
      </div>
    </div>
  );
};

export default Signup;
