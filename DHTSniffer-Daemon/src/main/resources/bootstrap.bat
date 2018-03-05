@echo off
cd /d %~dp0
java -Dname=Deamon -cp  daemon.jar com.fast.dht.main.DeamonMain