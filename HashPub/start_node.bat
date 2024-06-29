@echo off
cd /d E:\HashPub\target
set /p port=Enter the port to run the HashPub-store on: 
java -jar -Dserver.port=%port% jmsmessagetester-0.0.1-SNAPSHOT.jar
