FROM openjdk:8

WORKDIR /usr/src/webstreams

MAINTAINER rictomm@gmail.com

ADD ./webstreams.jar ./webstreams.jar 
ADD ./user.properties ./user.properties
ADD start.sh ./start.sh

RUN chmod u+x start.sh

RUN ls .

EXPOSE 8081 8080

ENTRYPOINT ["./start.sh"]
CMD ["user.properties"]

