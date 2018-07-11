#!/bin/bash

DATE=`date +%Y-%m-%d`

cat /var/log/asterisk/cdr-csv/Master.csv | grep $DATE > /PATH_TO_JAR/temp
ITER=`cat /var/log/asterisk/cdr-csv/Master.csv | grep $DATE | wc -l`
cd /PATH_TO_JAR/
java -jar AsteriskLog.jar temp $ITER
sleep 2
java
