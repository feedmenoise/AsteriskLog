#!/bin/bash

DATE=`date +%Y-%m-%d`

cat /var/log/asterisk/cdr-csv/Master.csv | grep $DATE > temp
ITER=`cat /var/log/asterisk/cdr-csv/Master.csv | grep $DATE | wc -l`

java -jar AsteriskLog.jar temp $ITER
rm -rf temp
rm -rf temp.complete