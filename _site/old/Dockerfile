FROM openjdk:8

WORKDIR /usr/src/webstreams

MAINTAINER rictomm@gmail.com

ARG stream

ADD ./webstreams.jar ./webstreams.jar 
ADD ./props/$stream.properties ./user.properties
ADD start.sh ./start.sh

RUN chmod u+x start.sh

RUN ls .

EXPOSE 8081 8080

ENTRYPOINT ["./start.sh"]
CMD ["user.properties"]

