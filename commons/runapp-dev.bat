java -Xms256m -Xmx512m -XX:MaxDirectMemorySize=1G -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dspring.profiles.active=dev -DSERVER_PORT=10002 -jar .\target\cdms-data-extraction-0.0.1-SNAPSHOT.jar
