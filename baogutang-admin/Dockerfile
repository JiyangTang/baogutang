FROM openjdk:8
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY ./baogutang-admin/target/baogutang-admin-1.0.0-SNAPSHOT.jar /apps/baogutang-admin.jar
WORKDIR /apps
RUN bash -c 'touch /baogutang-admin.jar'
CMD exec java $JAVA_OPTS -jar baogutang-admin.jar
