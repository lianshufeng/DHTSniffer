@echo off
cd /d %~dp0
java -Xmx100m -Xms100m --add-opens java.base/java.lang=ALL-UNNAMED -jar dht.jar