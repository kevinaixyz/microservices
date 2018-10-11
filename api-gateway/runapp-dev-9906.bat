java -Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dserver.port=9906 -Dspring.profiles.active=dev -jar .\target\edge-server-0.0.1-SNAPSHOT.jar
