
# 기본 이미지로 Java 11을 사용합니다..
FROM openjdk:11-jre-slim

# 작업 디렉토리를 설정합니다.
WORKDIR /app

# 호스트 머신에서 JAR 파일을 복사합니다.
COPY build/libs/*.jar app.jar

<<<<<<< HEAD
# JAR 파일 실행
CMD ["java", "-jar", "app.jar"]
=======
# scouter agent를 복사
COPY /scouter/agent.java /app/scouter/agent.java


# JAR 파일 실행
CMD ["java", "-javaagent:scouter/agent.java/scouter.agent.jar", "-jar", "app.jar"]
>>>>>>> develop-back
