import React from "react";
import styles from "./NavBar.module.css";

import { Link, useNavigate, useLocation } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../redux/userInfo";

import Logo from "../../assets/img/Logo.png";

const NavBar = () => {
  const dispatch = useDispatch();
  const userInfo = useSelector((state) => {
    return state.persistedReducer.userInfo;
  });
  const navigate = useNavigate();
  const location = useLocation();

  // 특정 경로에서는 NavBar를 숨깁니다.
  const hideNavBar =
    location.pathname === "/exexchange" ||
    location.pathname === "/excard" ||
    location.pathname === "/excardcontent";

  const onClickLogout = () => {
    localStorage.removeItem("access-token");
    localStorage.removeItem("refresh-token");
    dispatch(logout());
    navigate("/");
  };

  const onClickMypage = () => {
    navigate("/mypage");
  };

  const onClickLogin = () => {
    navigate("/login");
  };

  const onClickSignup = () => {
    navigate("/signup");
  };
  return (
    <div className="NavBar">
      {!hideNavBar && (
        // NavBar 내용
        <div className={styles.mainNav}>
          <Link className={styles.navMenu} to="/">
            <img className={styles.navLogo} src={Logo} alt="" />
          </Link>
          <div className={styles.navLeft}>
            <Link className={styles.navMenu} to="/">
              Home
            </Link>
            <Link className={styles.navMenu} to="/apidocs/accommodation">
              API문서
            </Link>
            <Link className={styles.navMenu} to="/graphs/accommodation">
              DashBoard
            </Link>
          </div>
          {/* 로그인 상태에 따라 Login 또는 Mypage로 링크 변경 */}
          {userInfo === null || JSON.stringify(userInfo).length === 2 ? (
            <div className={styles.navRight}>
              <button className={styles.navBtn} onClick={onClickLogin}>
                로그인
              </button>
              <button className={styles.navBtn} onClick={onClickSignup}>
                회원가입
              </button>
            </div>
          ) : (
            <div className={styles.navRight}>
              <button className={styles.navBtn} onClick={onClickMypage}>
                내 API 토큰
              </button>
              <button className={styles.navBtn} onClick={onClickLogout}>
                로그아웃
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default NavBar;
