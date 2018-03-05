@echo off
cd /d %~dp0
java -Xmx1000m -Xms100m -Duser.timezone=GMT+8 -Dname=toTorrent -cp  torrent.jar com.fast.dht.torrent.TorrentMain