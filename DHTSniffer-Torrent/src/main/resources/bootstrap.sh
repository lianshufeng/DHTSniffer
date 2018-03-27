#!/bin/bash
cd `dirname $0`
java -Xmx500m -Xms100m -Duser.timezone=GMT+8 -Dname=toTorrent -cp  torrent.jar com.fast.dht.torrent.TorrentMain