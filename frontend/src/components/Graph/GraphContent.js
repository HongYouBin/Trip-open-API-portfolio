import React, { useEffect, useState } from "react";
import styles from "./GraphContent.module.css";

//더미 데이터
const data = [
  {
    index: 101,
    url: "https://k9b205a.p.ssafy.io/kibana/app/dashboards#/view/6aa799b0-883f-11ee-9867-c70f1e3dc34b?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(description:'%EA%B3%B5%EA%B3%B5%EB%8D%B0%EC%9D%B4%ED%84%B0%20%EA%B3%B5%EC%8B%9D%20%EC%84%A4%EB%AA%85%20%ED%82%A4%EC%9B%8C%EB%93%9C',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(enhancements:()),gridData:(h:15,i:f8cb9ce0-6b86-4c8c-9a48-debac5aa826e,w:24,x:0,y:0),id:'1b94fc50-883f-11ee-9867-c70f1e3dc34b',panelIndex:f8cb9ce0-6b86-4c8c-9a48-debac5aa826e,type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:()),gridData:(h:15,i:'5a417b2c-c867-40d1-8570-1d253e0e84b8',w:24,x:24,y:0),id:'397fd460-883f-11ee-9867-c70f1e3dc34b',panelIndex:'5a417b2c-c867-40d1-8570-1d253e0e84b8',type:lens,version:'7.11.2')),query:(language:kuery,query:''),tags:!(),timeRestore:!f,title:%5Bwiki%5Doverview,viewMode:view)&hide-filter-bar=true",
    width: "600",
    height: "450",
    dec: "숙소에 관한 대시보드",
  },
  {
    index: 201,
    url: "https://k9b205a.p.ssafy.io/kibana/app/dashboards#/view/c0792de0-8812-11ee-b533-b13160bfdd44?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(description:'%EC%9D%8C%EC%8B%9D%EC%A0%90%20%EC%A7%80%EB%AA%85%EC%97%90%20%EB%8C%80%ED%95%9C%20%EB%8C%80%EC%8B%9C%EB%B3%B4%EB%93%9C',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(enhancements:(),hidePanelTitles:!f),gridData:(h:15,i:d0378e7d-3147-4b7a-a706-717b409c80e7,w:24,x:24,y:0),id:'7b5180d0-880f-11ee-b533-b13160bfdd44',panelIndex:d0378e7d-3147-4b7a-a706-717b409c80e7,title:'%EC%83%81%EC%9C%84%2010%EA%B0%9C%20%EC%A0%9C%EB%AA%A9%20%ED%83%9C%EA%B7%B8',type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:(),hidePanelTitles:!f),gridData:(h:15,i:f0d2ff63-b26c-4cfb-b89b-04c6045a9e2c,w:24,x:0,y:0),id:f06de3b0-880d-11ee-b533-b13160bfdd44,panelIndex:f0d2ff63-b26c-4cfb-b89b-04c6045a9e2c,title:'%EC%83%81%EC%9C%84%2010%EA%B0%9C%20%EC%A0%9C%EB%AA%A9%20%ED%83%9C%EA%B7%B8',type:lens,version:'7.11.2')),query:(language:kuery,query:''),tags:!(),timeRestore:!f,title:%5Bfood%5DfoodName,viewMode:view)&hide-filter-bar=true",
    width: "1000",
    height: "450",
    dec: "음식점 지명에 대한 대시보드",
  },
  {
    index: 202,
    url: "https://k9b205a.p.ssafy.io/kibana/app/dashboards#/view/5404ed30-8834-11ee-9867-c70f1e3dc34b?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(description:'fodd_type_main%20%EB%8C%80%EC%8B%9C%EB%B3%B4%EB%93%9C',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(enhancements:(),hidePanelTitles:!t),gridData:(h:15,i:'80b2d52c-f4ee-4551-bb5e-a2d86a785efe',w:24,x:0,y:0),id:'659ceef0-8832-11ee-9867-c70f1e3dc34b',panelIndex:'80b2d52c-f4ee-4551-bb5e-a2d86a785efe',type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:(),hidePanelTitles:!t),gridData:(h:15,i:'2671fe61-7e92-4e4a-93f3-90cdacfe5f47',w:24,x:24,y:0),id:a315c7c0-8832-11ee-9867-c70f1e3dc34b,panelIndex:'2671fe61-7e92-4e4a-93f3-90cdacfe5f47',type:lens,version:'7.11.2')),query:(language:kuery,query:''),tags:!(),timeRestore:!f,title:%5Bfood%5DfoodTypeMain,viewMode:view)&hide-filter-bar=true",
    width: "600",
    height: "450",
    dec: "food_type_main 대시보드",
  },
  {
    index: 203,
    url: "https://k9b205a.p.ssafy.io/kibana/app/dashboards#/view/502c4a10-8838-11ee-9867-c70f1e3dc34b?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(description:'%EB%B6%80%EC%A0%9C%EB%AA%A9%EB%AA%85%20%EC%A0%90%EC%88%98%EB%B6%84%ED%8F%AC',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(enhancements:(),hidePanelTitles:!t),gridData:(h:15,i:bff4221b-37d0-4002-8678-6107218f839e,w:24,x:0,y:0),id:'1d078e30-8836-11ee-9867-c70f1e3dc34b',panelIndex:bff4221b-37d0-4002-8678-6107218f839e,type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:()),gridData:(h:15,i:c3dfbc1d-800a-44cc-82b9-bbd911986e96,w:24,x:24,y:0),id:'481fe520-8838-11ee-9867-c70f1e3dc34b',panelIndex:c3dfbc1d-800a-44cc-82b9-bbd911986e96,type:lens,version:'7.11.2')),query:(language:kuery,query:''),tags:!(),timeRestore:!f,title:%5Bfood%5DfoodTypeSub,viewMode:view)&hide-filter-bar=true",
    width: "600",
    height: "450",
    dec: "부제목명 점수분포",
  },
  {
    index: 204,
    url: "https://k9b205a.p.ssafy.io/kibana/app/dashboards#/view/cd39c4e0-883a-11ee-9867-c70f1e3dc34b?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(description:'%EC%9D%8C%EC%8B%9D%EC%A0%90%EC%9D%98%20%EC%A0%90%EC%88%98%EC%99%80%20%EB%B3%84%EC%A0%90%EC%9D%98%20%EB%B6%84%ED%8F%AC%20%EC%B0%A8%ED%8A%B8',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(enhancements:(),hidePanelTitles:!t),gridData:(h:17,i:'5a3316e6-f0e3-4c6a-ba19-182d6efabf71',w:19,x:0,y:0),id:'6b5d63e0-8839-11ee-9867-c70f1e3dc34b',panelIndex:'5a3316e6-f0e3-4c6a-ba19-182d6efabf71',type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:(),hidePanelTitles:!t),gridData:(h:17,i:'1af050cd-a3a9-4bc5-8379-9a0b33abe4f6',w:19,x:19,y:0),id:'5f300f90-883a-11ee-9867-c70f1e3dc34b',panelIndex:'1af050cd-a3a9-4bc5-8379-9a0b33abe4f6',type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:()),gridData:(h:5,i:fc558815-6340-417e-b2ae-ea8f6e772c86,w:19,x:0,y:17),id:'8a892060-8839-11ee-9867-c70f1e3dc34b',panelIndex:fc558815-6340-417e-b2ae-ea8f6e772c86,type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:()),gridData:(h:5,i:'4614bd01-2996-4249-9c3d-141829512bc8',w:19,x:19,y:17),id:'77b47560-883a-11ee-9867-c70f1e3dc34b',panelIndex:'4614bd01-2996-4249-9c3d-141829512bc8',type:lens,version:'7.11.2')),query:(language:kuery,query:''),tags:!(),timeRestore:!f,title:%5Bfood%5Dscore_and_star,viewMode:view)&hide-filter-bar=true",
    width: "600",
    height: "650",
    dec: "음식점의 점수와 별점의 분포 차트",
  },
  {
    index: 301,
    url: "https://k9b205a.p.ssafy.io/kibana/app/dashboards#/view/cefcf150-883d-11ee-9867-c70f1e3dc34b?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(description:'%EC%97%AC%ED%96%89%EC%A7%80%EB%AA%85%EC%97%90%20%EB%8C%80%ED%95%9C%20%EC%A2%85%ED%95%A9%20%EB%B6%84%ED%8F%AC%EB%8F%84',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(enhancements:()),gridData:(h:15,i:'0fe5f817-8eca-4d92-bbec-d1b7d2144d61',w:20,x:0,y:0),id:b82371d0-883c-11ee-9867-c70f1e3dc34b,panelIndex:'0fe5f817-8eca-4d92-bbec-d1b7d2144d61',type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:()),gridData:(h:15,i:'2ab684f2-f1ca-4ec0-8842-8dc96746f54c',w:13,x:21,y:0),id:'2d9a65e0-883d-11ee-9867-c70f1e3dc34b',panelIndex:'2ab684f2-f1ca-4ec0-8842-8dc96746f54c',type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:(),hidePanelTitles:!f),gridData:(h:8,i:'0a87838e-d3f9-46dc-8194-68cd5c36d6bc',w:9,x:35,y:0),id:'766ea790-883d-11ee-9867-c70f1e3dc34b',panelIndex:'0a87838e-d3f9-46dc-8194-68cd5c36d6bc',type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:()),gridData:(h:7,i:f3051146-839c-4d09-95c7-da1127f005ff,w:9,x:35,y:8),id:ea174440-883d-11ee-9867-c70f1e3dc34b,panelIndex:f3051146-839c-4d09-95c7-da1127f005ff,type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:()),gridData:(h:22,i:'8c10a506-0e92-4a2a-9e7d-89f9835a75a3',w:33,x:0,y:15),id:b7889bf0-883d-11ee-9867-c70f1e3dc34b,panelIndex:'8c10a506-0e92-4a2a-9e7d-89f9835a75a3',type:lens,version:'7.11.2')),query:(language:kuery,query:''),tags:!(),timeRestore:!f,title:%5Bwiki%5DAttractionName,viewMode:view)&hide-filter-bar=true",
    width: "600",
    height: "800",
    dec: "여행지명에 대한 종합 분포도",
  },
  {
    index: 302,
    url: "https://k9b205a.p.ssafy.io/kibana/app/dashboards#/view/6aa799b0-883f-11ee-9867-c70f1e3dc34b?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-15m,to:now))&_a=(description:'%EA%B3%B5%EA%B3%B5%EB%8D%B0%EC%9D%B4%ED%84%B0%20%EA%B3%B5%EC%8B%9D%20%EC%84%A4%EB%AA%85%20%ED%82%A4%EC%9B%8C%EB%93%9C',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),panels:!((embeddableConfig:(enhancements:()),gridData:(h:15,i:f8cb9ce0-6b86-4c8c-9a48-debac5aa826e,w:24,x:0,y:0),id:'1b94fc50-883f-11ee-9867-c70f1e3dc34b',panelIndex:f8cb9ce0-6b86-4c8c-9a48-debac5aa826e,type:lens,version:'7.11.2'),(embeddableConfig:(enhancements:()),gridData:(h:15,i:'5a417b2c-c867-40d1-8570-1d253e0e84b8',w:24,x:24,y:0),id:'397fd460-883f-11ee-9867-c70f1e3dc34b',panelIndex:'5a417b2c-c867-40d1-8570-1d253e0e84b8',type:lens,version:'7.11.2')),query:(language:kuery,query:''),tags:!(),timeRestore:!f,title:%5Bwiki%5Doverview,viewMode:view)&hide-filter-bar=true",
    width: "600",
    height: "450",
    dec: "공공데이터 공식 설명 키워드",
  },
];

const GraphContent = (props) => {
  // 렌더링 시 스크롤을 최상단으로 이동하는 함수
  const handleScroll = () => {
    window.scrollTo(0, 0);
  };

  const [api_docs_id, setApiId] = useState("");
  const [apiContent, setApiContent] = useState({});

  useEffect(() => {
    handleScroll(); // 초기 렌더링 시 스크롤을 최상단으로 이동
    setApiId(props.data);
    // console.log(props.data);
  }, [props]);

  useEffect(() => {
    const getApiDocsData = (api_docs_id) => {
      const responseData = data.find((item) => item.index === api_docs_id);

      if (responseData) {
        setApiContent(responseData);
      }
    };

    if (api_docs_id) {
      getApiDocsData(api_docs_id);
    }
  }, [api_docs_id]);

  return (
    <div className={styles.contentBody}>
      <h2 className={styles.Title}>{apiContent.dec}</h2>
      <div className={styles.iframeBody}>
        <iframe
          src={apiContent.url}
          height={apiContent.height}
          width={apiContent.width}
        ></iframe>
      </div>
    </div>
  );
};

export default GraphContent;
