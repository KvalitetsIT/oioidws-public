#!/bin/bash

if [ "$CONTAINER_TIMEZONE" = "" ]
then
	echo "Using Europe/Copenhagen timezone"
	CONTAINER_TIMEZONE="Europe/Copenhagen"
fi

TZFILE="/usr/share/zoneinfo/$CONTAINER_TIMEZONE"
if [ ! -e "$TZFILE" ]
then
	echo "requested timezone $CONTAINER_TIMEZONE doesn't exist"
else
	cp /usr/share/zoneinfo/$CONTAINER_TIMEZONE /etc/localtime
	echo "$CONTAINER_TIMEZONE" > /etc/timezone
	echo "using timezone $CONTAINER_TIMEZONE"
fi

./wait-for-it.sh -t 30 -h mysql -p 3306
java -Djava.security.egd=file:/dev/./urandom -Dloader.path=config/* -Xmx128m -jar sts-1.0.0.jar

