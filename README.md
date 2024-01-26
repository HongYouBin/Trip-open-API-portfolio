# 👨‍💻 TRIP OPEN API
**여행 서비스를 위한 OPEN API**<br><br>


<img src="./image/page/main-page.png" width="75%" height="75%"/>  

<br>


## ⬇️⬇️프로젝트 소개 (youtube로 보러가기)⬇️⬇️

[동영상 링크](https://www.youtube.com/watch?v=DyyWZVai3vw)
  
개발에 갓 진입한 주니어 개발자들이 진행하는 서비스 개발을 위한 여행관련 open API를 제공.  
기존 여행 관련 사이트를 제작하려면 공공데이터 API를 주로 사용하지만, 이에 대한 정보와 결과는 한정되어 있다.  
이러한 단점을 극복하고자 해당 공공데이터 데이터베이스를 활용하여 이와 관련된 숙소, 음식점, 전자사전 검색 데이터를 추가로 수집하고, 유사한 검색 결과를 보정하는 API 기능을 만들어 제공하고자 하였다.  

### 주요 기능

#### 1. 🏠 여행지 관련 숙소 API
 - 여행지 이름 기준으로 주변 숙소 정보를 얻는다.
 - 위경도 기준으로 주변 숙소 정보를 얻는다.

#### 2. 🍖 여행지 관련 음식점 API
 - 여행지 이름 기준으로 주변 음식점 정보를 얻는다.
 - 위경도 기준으로 주변 숙소 정보를 얻는다.

#### 3. 🔍 검색 보정 API
 - [제목 검색] 제목의 오탈자를 보정하여 유사한 보정 결과를 반환한다.
 - [전문 검색] 입력 문장을 기준으로 데이터의 모든 필드를 환산하여 가장 유사한 내용을 가진 결과값을 스코어 순대로 반환한다.
 - 입력 문장과 거리, 점수, 입력 파라미터의 조건을 기준으로 가장 높은 검색 결과를 반환한다.

#### 4. 📊 데이터 신뢰성 측정 및 데이터 시각화, 로그 수집
 - 수집하여 저장된 데이터의 내용 유사도를 판단하여 신뢰성 있는 결과를 제공한다.
 - 수집된 인덱스의 도큐먼트 중, 가장 많은 검색 결과를 Kibana를 활용하여 차트의 형태로 제공한다.
 - logstash를 사용하여 API 요청 회수와 사용자 요청 로그를 데이터로 수집하고, 차트의 형태로 제공한다.

#### 5. 🖥 배치 프로그램을 사용한 스크래핑
 - python, Selenium을 사용한 스크래핑 코드 작성, robots.txt 를 통한 스크래핑 허가 유무를 확인하였다.
 - '나무위키', '여기어때', '다이닝코드' 사이트에서 숙소정보와 음식점정보를 서버에 부하를 주지 않는 선에서 수집하였다.
 
 <br>


## 📅프로젝트 기간

**23.10.10 ~ 23.11.16 (6주간)**    

## 🛠️ 서비스 화면 

<details>
<h3> 홈페이지 </h3><br>
<summary><h3>HomePage</h3></summary>
<img src="./image/gif/main-page-gif.gif" width="75%" height="75%"/>
<img src="./image/page/main-page.png" width="75%" height="75%"/>
</details></br>

<details>
<h3> API 사용을 위한 Docs 페이지 </h3><br>
<summary><h3>Docs</h3></summary>
<h3>API Info</h3> <br>
<img src="./image/gif/api-info-gif.gif" width="75%" height="75%"/>

<h3>요청 및 반환</h3> <br> 
<img src="./image/gif/request-response-gif.gif" width="75%" height="75%"/>  

<h3> 전체 사진 </h3> <br>
<img src="./image/page/api-docs-accommodation1.png" width="75%" height="75%"/>  

</details></br>

<details>
<h3> API 사용을 위한 Token 발급 페이지</h3> <br>
<summary><h3>TokenPage</h3></summary>
<img src="./image/page/user-api-token-page.png" width="75%" height="75%"/>
</details></br>

<details>
<h3> Kibana Dashboard를 통한 데이터시각화 </h3><br>
<summary><h3>Dashboard</h3></summary>
<img src="./image/page/dashboard_1.png" width="75%" height="75%"/>
<img src="./image/page/dashboard_2.png" width="75%" height="75%"/>
<img src="./image/page/dashboard_3.png" width="75%" height="75%"/>
</details></br>

## 🧝‍♂️팀원 및 역할  

| **팀장** | 신창학 (BE: Elastic search API, Sub server Infra, Kibana 데이터시각화, Logstash 데이터 수집, Spring API 서버 구축 및 데이터 신뢰성 처리)   |
|----------|---------------------|
| **팀원** | 강현곤 (BE: 숙소 데이터 크롤링, 숙소 데이터 API)             |
| **팀원** | 이지현 (BE: 음식점 데이터 크롤링, 음식점 데이터 API )  |
| **팀원** | 이진호 (Infra : EC2, nginx, docker, jenkins 세팅)     |
| **팀원** | 정형준 (BE: 데이터 크롤링 보조, 숙소 데이터 API, Java Global 객체, 처리율 제한, 부하테스트) |
| **팀원** | 홍유빈 (BE: Docs API, 전자사전 전처리, 처리율 제한, 부하테스트 )  |

## 👨‍👩‍👧협업 툴  

- GitLab
- Jira
- Notion
- Mattermost

## 🖥️ 기술 스택

🖱**Backend**

- Java 11
- spring boot 2.7.17
- spring-boot-jpa
- Spring Security
- Spring Data Elasticsearch
- Logback
- Logstash
- Kibana

🖱**DB**

- mysql 8.0.23
- Redis 7.2.1
- MongoDB Cloud
- Elasticsearch 7.11.2

🖱**Frontend**

- React.js 18.2.0
- node.js 18.16.1
- axios 1.4.0
- styled-components 6.0.4

🖱**Infra**

- AWS EC2
- docker
- nginx
- jenkins

## 🔧 시스템 아키텍쳐  

### System Architecture
<img src="./image/architecture/system_architecture.PNG" width="75%" height="75%"/>

### Log Data architecture
<img src="./image/architecture/data_architecture_1.PNG" width="75%" height="75%"/>
<img src="./image/architecture/data_architecture_2.PNG" width="75%" height="75%"/>

### Business Logic
<img src="./image/architecture/business_logic.png" width="75%" height="75%"/>

### Total Flow
<img src="./image/architecture/total_flow.PNG" width="75%" height="75%"/>

## 📑 API 명세서  

<br>
[명세서 링크](https://safe-bagpipe-21e.notion.site/53032bd640bf4ac2af297b882635c6e9?v=076fc8f6b75d4bd5a47a6c27a1901f28&pvs=4)

<br>
<img src="./image/api-bill/api_1.PNG" width="75%" height="75%"/>
<img src="./image/api-bill/api_2.PNG" width="75%" height="75%"/>


## ✨ERD  

<details>
<summary><h3>ERD</h3></summary>
<img src="./image/erd/total.PNG" width="75%" height="75%"/>
<img src="./image/erd/1.PNG" width="50%" height="50%"/>
<img src="./image/erd/2.PNG" width="50%" height="50%"/>
<img src="./image/erd/3.PNG" width="50%" height="50%"/>
<img src="./image/erd/4.PNG" width="50%" height="50%"/>
<img src="./image/erd/5.PNG" width="50%" height="50%"/>
<img src="./image/erd/6.PNG" width="50%" height="50%"/>
<img src="./image/erd/7.PNG" width="50%" height="50%"/>
</details></br>


## 📚 커밋 컨벤션 규칙

| Type 키워드 | 사용 시점 |
| --- | --- |
| 첫 커밋 | CREATE: start project |
| Add | 새로운 파일 추가 |
| Delete | 파일 삭제 |
| Feat | 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정 |
| Fix | 기능에 대한 버그 수정 |
| Build | 빌드 관련 수정 |
| Chore | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore |
| Ci | CI 관련 설정 수정 |
| Docs | 문서(주석) 수정 |
| Style | 코드 스타일, 포맷팅에 대한 수정 |
| Refactor | 기능의 변화가 아닌 코드 리팩터링 ex) 변수 이름 변경 |
| Test | 테스트 코드 추가/수정 |
| Release | 버전 릴리즈 |
| Rename | 파일 혹은 폴더명을 수정만 한 경우 |
| Readme | README |
| Comment | 주석관련 |

 ***commit message***
  - commit은 최대한 자세히

`키워드(대문자) :  (영어로 위치/함수/기능) + 설명`

## 🌐EC2 PORT

| 서비스                 | 포트  |
|-----------------------|-------|
| Spring Boot - Main API Server | 8080  |
| Spring Boot - ElasticSearch API Server | 8081  |
| React                 | 3000  |
| MySQL                 | 4000  |
| Jenkins               | 9090  |
| Elasticsearch         | 9091  |
| Kibana                | 5601  |

## 처리율 제한 동기화 문제 해결
###### - 문제 정의 : 사용자 별 하루 당 10만회의 API 사용을 제한하기 위한 인터셉터 Count 함수에서 동기화 문제 발생
###### -  문제 설명 : 레디스의 {토큰 : cnt} 데이터를 여러 스레드에서 수정하는 과정에서 레이스컨디션 문제 발생
###### -  문제 해결 : Java의 Synchronized ( Monitor ) / MySQL Lock / Redis Redisson ( distributed lock )의 성능 측정 후 가장 성능이 좋았던 Synchronized 활용
###### -  성능 측정 : Ngrinder를 통해 각각의 동기화에 대한 초당 처리율 (TPS) 측정 결과 Synchronized : 302 / MySQL : 167 / Redisson : 217
###### -  확장에 대한 고려 : 단일 프로세스 환경에서는 Synchronized로 충분했으나, 로드밸런싱을 고려할 경우 DB를 사용하여 Redisson으로의 전환을 고려

## 부하테스트 이슈 발생
###### -  문제 정의 : Ngrinder로 최악의 상황을 가정하여 100명의 유저가 끊임없이 request를 보내는 환경에서 error의 비율과 response 속도 측정
###### -  문제 설명 : ThreadPool을 default로 설정했을 경우 평균 응답시간 2.5초 / error 비율 26.9% 를 확인
###### -  문제 해결 : ThreadPool의 최대 Thread, 최소 동작 Tread, WaitQueue 사이즈를 조절하여 평균 응답시간 2.7초 / error 비율 0%로 개선
###### -  연구 결과 : Synchronized에 의해 대기하고있는 스레드가 많아 최대 Thread수를 조절하여 error를 줄이는 것에 성공

