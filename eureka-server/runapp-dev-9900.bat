java -Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dspring.profiles.active=dev -DSERVER_PORT=9900 -DPEER_HOSTNAME=localhost -DPEER_PORT=9901 -jar .\target\eureka-server-0.0.1-SNAPSHOT.jar
