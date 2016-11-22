#!/bin/sh

restart_file="/home/ec2-user/restart"

if [ -e $restart_file ]
then
	today=`date`
	echo "$today"
	echo "restart file exists, deleting"
	rm $restart_file
	echo "restarting beanlet-web"
	/etc/init.d/beanlet-web restart
fi
