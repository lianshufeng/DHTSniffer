#!/bin/bash
cd `dirname $0`
java -Xmx200m -Xms100m -Duser.timezone=GMT+8 -Dname=PushDataMain -cp  pushdata.jar com.fast.dev.push.PushDataMain