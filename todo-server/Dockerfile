FROM openjdk:12-alpine

ENV APP_AS_JAR build/libs/todo-1.0.0-SNAPSHOT-fat.jar
ENV APP_HOME /usr/app/

RUN addgroup -S todo && adduser -S -g todo todo

COPY $APP_AS_JAR $APP_HOME

RUN chown -R todo:todo $APP_HOME
RUN chmod -R a+x $APP_HOME

EXPOSE 8081
USER todo

WORKDIR $APP_HOME

CMD ["java", "-jar", "./todo-1.0.0-SNAPSHOT-fat.jar"]
