# 개발 서버 실행 환경
# FROM: 이미지 지정
FROM node:18

# WORKDIR: RUN, CMD, ENTRYPOINT, ADD, COPY 에 정의된 명령어를 실행하는 작업 디렉터리 지정
WORKDIR /app

# COPY: 이미지에 파일이나 폴더를 추가
COPY package.json .

# RUN: 이미지를 빌드하며 실행할 명령어 지정
RUN npm install

# COPY: 이미지에 파일이나 폴더를 추가
COPY . .

# EXPOSE: 이미지가 통신에 사용할 포트를 명시적으로 지정
EXPOSE 3000

# CMD: 컨테이너를 실행할 때 실행할 명령어 지정
CMD ["npm", "start"]