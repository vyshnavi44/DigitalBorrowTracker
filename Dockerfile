FROM tomcat:9.0-jdk21

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY DigitalBorrowTracker.war /usr/local/tomcat/webapps/DigitalBorrowTracker.war

EXPOSE 8080