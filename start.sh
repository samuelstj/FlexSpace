#!/bin/bash
for i in 0 1 2 3 4 5
do
./smartrun.sh br.unb.cic.replication.server.ServerLaunch $i
sleep 1
done
