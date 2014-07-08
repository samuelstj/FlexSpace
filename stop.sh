#!/bin/bash
ps aux|grep "java -c"|grep -v grep|awk -F " " '/1/ {print $2}'|xargs kill -9

