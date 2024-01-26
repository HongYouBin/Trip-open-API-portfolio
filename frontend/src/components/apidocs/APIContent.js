import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import styles from "./APIContent.module.css";
import basicHttp from "../../api/basicHttp";
//
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { vscDarkPlus } from "react-syntax-highlighter/dist/esm/styles/prism";
import { vs } from "react-syntax-highlighter/dist/esm/styles/prism";

//더미 데이터
const dummy = [
  {
    bearer: "token",
    name: "비슬산자연휴양림",
    distance: "10",
    sorted: "distance",
    maxResults: "10",
    page: "1",
  },
  {
    bearer: "token",
    longitude: "128.99565180000000000",
    latitude: "37.03918876000000000",
    distance: "5",
    sorted: "like",
    maxResults: "5",
    page: "1",
  },
];

const APIContent = (props) => {
  // 렌더링 시 스크롤을 최상단으로 이동하는 함수
  const handleScroll = () => {
    window.scrollTo(0, 0);
  };

  const [api_docs_id, setApiId] = useState("");
  const [apiContent, setApiContent] = useState([]);
  const [apiData, setApiData] = useState([]);
  // 상태 설정
  const [authorizationValue, setAuthorizationValue] = useState("");
  const [requestParameterValues, setRequestParameterValues] = useState([]);
  const [testResponseData, setTestResponseData] = useState([]);
  // 값 변경 핸들러
  const handleAuthorizationChange = (event) => {
    setAuthorizationValue(event.target.value);
  };

  const handleRequestParameterChange = (index, event) => {
    const updatedValues = [...requestParameterValues];
    updatedValues[index] = event.target.value;
    setRequestParameterValues(updatedValues);
  };

  useEffect(() => {
    handleScroll(); // 초기 렌더링 시 스크롤을 최상단으로 이동
    setApiId(props.data);
    console.log(props.data);
  }, [props]);

  useEffect(() => {
    setAuthorizationValue("");
    setRequestParameterValues([]);
    if (api_docs_id == 4) {
      setAuthorizationValue("token");
      setRequestParameterValues([
        dummy[1].longitude,
        dummy[1].latitude,
        dummy[1].distance,
        dummy[1].sorted,
        dummy[1].maxResults,
        dummy[1].page,
      ]);
    } else if (api_docs_id == 1) {
      setAuthorizationValue("token");
      setRequestParameterValues([
        dummy[0].name,
        dummy[0].distance,
        dummy[0].sorted,
        dummy[0].maxResults,
        dummy[0].page,
      ]);

      //   console.log(requestParameterValues);
    }

    setTestResponseData([]);
    const getApiDocsData = async (api_docs_id) => {
      try {
        const response = await basicHttp.get(
          `/docs/data/api/info/${api_docs_id}`
        );
        const responseData = response.data;

        if (responseData.data) {
          setApiContent(responseData.data);
          setApiData(responseData.data.variable_info);
        }
      } catch (error) {
        console.log("Error fetching API data", error);
      }
    };

    if (api_docs_id) {
      getApiDocsData(api_docs_id);
    }
  }, [api_docs_id]);

  const isRequestTrueData = [];
  const isRequestFalseData = [];

  apiData.forEach((item) => {
    if (item.is_request) {
      isRequestTrueData.push(item);
    } else {
      isRequestFalseData.push(item);
    }
  });

  // 5.test button function
  const handleApiRequest = () => {
    const baseURL = apiContent.endpoint;
    const queryParams = new URLSearchParams();

    isRequestTrueData.forEach((dataItem, index) => {
      const value = requestParameterValues[index] || "";
      queryParams.append(dataItem.title, value);
    });

    const fullUrl = `${baseURL}?${queryParams.toString()}`;
    // const fullUrl = `https://k9b205.p.ssafy.io/api/accommodation/by-name?name=비슬산자연휴양림&distance=2&sorted=distance`;
    // const fullUrl = `https://k9b205.p.ssafy.io/api/accommodation/by-name?${queryParams.toString()}`;

    // Axios를 사용하여 POST 요청 보내기
    axios
      .get(fullUrl, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + authorizationValue,
          // Authorization: `Bearer ${authorizationValue}`
          // Authorization: `Bearer 31da9460a4be6a0e82022fd1d10a3ed0d72c77289f036f8c2f5dff4c559973d07177657240676d61696c2e636f6d`,
        },
      })
      .then((response) => {
        // setTestResponseData(response.data);
        console.log(response);
        setTestResponseData(response.data);
      })
      .catch((error) => {
        console.error("API 요청 에러:", error);
        setTestResponseData({ error: "API 요청 중 에러가 발생했습니다." });
      });
  };

  return (
    <div className={styles.contentBody}>
      <div>
        <h2>{apiContent.title}</h2>
        <div className={styles.contentText}>{apiContent.content}</div>
        <h3>1. 기본 설명</h3>

        <table className={styles.firstTable}>
          <thead>
            <tr>
              <th></th>
              <th>설명</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>헤더</td>
              <td>
                저희 서비스에서는 API 요청을 인증하기 위해 Bearer 타입의 토큰을
                사용합니다. Bearer 토큰은 로그인 후 우측상단의 '내 API 토큰'
                버튼을 눌러 확인 하실 수 있습니다. 토큰은 API 요청의 헤더의
                Authorization 필드에 담아 보내주세요, 이를 통해 사용자를
                식별합니다.
              </td>
            </tr>
            <tr>
              <td>메서드</td>
              <td>{apiContent.method}</td>
            </tr>
            <tr>
              <td>호출경로</td>
              <td>{apiContent.endpoint}</td>
            </tr>
            <tr>
              <td>응답형식</td>
              <td>{apiContent.return_type}</td>
            </tr>
          </tbody>
        </table>

        <h3>2. 요청</h3>
        <h4>* 요청 헤더(Header)</h4>
        <table className={styles.apiRequestDataTable}>
          <thead>
            <tr>
              <th>HTTP</th>
              <th>필드</th>
              <th> 타입 </th>
              <th>설명</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>Header</td>
              <td>Authorization</td>
              <td>Bearer</td>
              <td>
                우측 상단 '내 API 토큰'을 입력합니다.
                <br />
                예시: 'Authorization': `Bearer [내 API 토큰]`
              </td>
            </tr>
          </tbody>
        </table>

        {isRequestTrueData.length > 0 && (
          <div>
            <h4>* 요청 변수(Request Parameter)</h4>
          </div>
        )}
        {isRequestTrueData.length > 0 && (
          <table className={styles.apiRequestDataTable}>
            <thead>
              <tr>
                <th>HTTP</th>
                <th>항목명</th>
                <th> 필수 </th>
                <th>타입</th>
                <th>설명</th>
              </tr>
            </thead>
            <tbody>
              {isRequestTrueData.map((dataItem, index) => (
                <tr key={index}>
                  {index === 0 && (
                    <td rowSpan={isRequestTrueData.length}>
                      {dataItem.is_parameter ? "Parameter" : "Body"}
                    </td>
                  )}
                  <td>{dataItem.title}</td>
                  <td
                    style={{ color: dataItem.is_essential ? "red" : "black" }}
                  >
                    {dataItem.is_essential ? " O " : " X "}
                  </td>
                  <td>{dataItem.type}</td>
                  <td>
                    {/* {dataItem.detail} */}
                    {dataItem.detail.split("\n").map((line) => {
                      return (
                        <>
                          {line}
                          <br />
                        </>
                      );
                    })}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
        <div>
          <h3>3. 응답</h3>
          <h4>* 본문(Body)</h4>
        </div>
        <table className={styles.apiResponseDataTable}>
          <thead>
            <tr>
              <th>항목명</th>
              <th>타입</th>
              <th>설명</th>
            </tr>
          </thead>
          <tbody>
            {isRequestFalseData.map((dataItem, index) => (
              <tr key={index}>
                <td>{dataItem.title}</td>
                <td>{dataItem.type}</td>
                <td>
                  {/* {dataItem.detail} */}
                  {dataItem.detail.split("\n").map((line) => {
                    return (
                      <>
                        {line}
                        <br />
                      </>
                    );
                  })}
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <h3>4. 예제</h3>
        <h4>* 요청</h4>
        <pre id="bash" className={styles.code}>
          <SyntaxHighlighter language="bash" style={vs}>
            {apiContent.requestUrlExample}
          </SyntaxHighlighter>
        </pre>
        <h4>* 응답</h4>
        <pre id="json" className={styles.code}>
          <SyntaxHighlighter language="json" style={vs}>
            {apiContent.return_example}
          </SyntaxHighlighter>
        </pre>
        <div className={styles.testDiv}>
          <h3>5. 테스트(미리 보기)</h3>
          <h4>* 요청 헤더(Header)</h4>
          <table className={styles.apiRequestDataTable}>
            <thead>
              <tr>
                <th>HTTP</th>
                <th>필드</th>
                <th> 타입 </th>
                <th>샘플 데이터</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Header</td>
                <td>Authorization</td>
                <td>Bearer</td>
                <td>
                  <input
                    value={authorizationValue}
                    onChange={handleAuthorizationChange}
                  />
                </td>
              </tr>
            </tbody>
          </table>
          <h4>* 요청 변수(Request Parameter)</h4>
          {isRequestTrueData.length > 0 && (
            <table
              style={{ marginBottom: "2%" }}
              className={styles.apiRequestDataTable}
            >
              <thead>
                <tr>
                  <th>HTTP</th>
                  <th>항목명</th>
                  <th> 필수 </th>
                  <th>타입</th>
                  <th>샘플 데이터</th>
                </tr>
              </thead>
              <tbody>
                {isRequestTrueData.map((dataItem, index) => (
                  <tr key={index}>
                    {index === 0 && (
                      <td rowSpan={isRequestTrueData.length}>
                        {dataItem.is_parameter ? "Parameter" : "Body"}
                      </td>
                    )}
                    <td>{dataItem.title}</td>
                    <td
                      style={{ color: dataItem.is_essential ? "red" : "black" }}
                    >
                      {dataItem.is_essential ? " O " : " X "}
                    </td>
                    <td>{dataItem.type}</td>
                    <td>
                      <input
                        value={requestParameterValues[index] || ""}
                        onChange={(event) =>
                          handleRequestParameterChange(index, event)
                        }
                      />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
          <button className={styles.testRequestBtn} onClick={handleApiRequest}>
            요청 보내기
          </button>
          <h4>* 응답</h4>
          <pre id="json" className={styles.code}>
            <SyntaxHighlighter language="json" style={vs}>
              {JSON.stringify(testResponseData, null, 2)}
            </SyntaxHighlighter>
          </pre>
        </div>
      </div>
    </div>
  );
};

export default APIContent;
