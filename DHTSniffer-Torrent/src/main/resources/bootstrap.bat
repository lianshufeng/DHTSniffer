@echo off
cd /d %~dp0
java -Xmx100m -Xms100m -Duser.timezone=GMT+8 --add-opens java.base/java.lang=ALL-UNNAMED -jar torrent.jar