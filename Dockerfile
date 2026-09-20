FROM tomcat:9.0-jdk21

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY DigitalBorrowTracker.war /usr/local/tomcat/webapps/DigitalBorrowTracker.war

EXPOSE 10000

CMD ["sh", "-c", "sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT:-10000}\\\"/\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]