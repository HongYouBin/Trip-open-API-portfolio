import React, { useEffect, useState } from "react";
import styles from "./APIDocs.module.css";
import SideBar from "./SideBar";
import APIContent from "./APIContent";
import wave from "../../assets/img/wave.png";

const APIDocs = () => {
  const [docsId, setDocsId] = useState("");
  const handleDocsId = (data) => {
    console.log("data_docs_id=====", data);
    setDocsId(data);
  };

  return (
    <div className={styles.backBody}>
      <div className={styles.boardContainer}>
        {/* <div className={styles.boardTop}>
          <h3>오픈 API를 이용해 창의적인 애플리케이션을 제작해보세요</h3>
        </div> */}
        <div className={styles.boardContent}>
          <SideBar onSetId={handleDocsId} />
          <APIContent data={docsId} />
        </div>
      </div>
    </div>
  );
};

export default APIDocs;
