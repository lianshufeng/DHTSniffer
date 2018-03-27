#!/bin/bash
cd `dirname $0`
java -Xmx500m -Xms100m -Duser.timezone=GMT+8 -Dname=Deamon -cp  daemon.jar com.fast.dht.main.DeamonMain