import React from "react";
import styles from "./Footer.module.css";

const Footer = () => {
  return (
    <div className="Footer">
      <div className={styles.footer}>
        <div className={styles.info}>
        © 2023 - SSAFY(), Daejeon, i5i.
        </div>
        <div className={styles.names}>
        | 강현곤 | 신창학 | 이지현 | 이진호 | 정형준 | 홍유빈 |
        </div>
      </div>
    </div>
  );
};

export default Footer;
