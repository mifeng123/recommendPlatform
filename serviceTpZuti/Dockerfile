FROM java:8 
#FROM zhengtengfei/recommendplatform:1.0-gatewayZuul 
ADD /target/service-tp-zuti-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
