#!/bin/bash
cd `dirname $0`
java -Xmx100m -Xms100m -Duser.timezone=GMT+8 -jar torrent.jar