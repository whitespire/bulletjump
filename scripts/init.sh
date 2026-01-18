#!/bin/sh

 mvn install:install-file -Dfile=$HOME/.var/app/com.hypixel.HytaleLauncher/data/Hytale/install/release/package/game/latest/Server/HytaleServer.jar -DgroupId=com.hypixel.hytale -DartifactId=HytaleServer-parent -Dversion=1.0-SNAPSHOT -Dpackaging=jar
 mvn clean package
