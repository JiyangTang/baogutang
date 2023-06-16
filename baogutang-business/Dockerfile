FROM openjdk:8
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY ./baogutang-business/target/baogutang-business-1.0.0-SNAPSHOT.jar /apps/baogutang-business.jar
WORKDIR /apps
RUN bash -c 'touch /baogutang-business.jar'
CMD exec java $JAVA_OPTS -jar baogutang-business.jar
