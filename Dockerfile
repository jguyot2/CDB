FROM tomcat

ADD ./cdb/webapp/target/cdbmaven.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]

EXPOSE 8080

