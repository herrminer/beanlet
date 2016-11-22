#!/bin/sh

version_file="/home/ec2-user/currentGitVersion"
beanlet_source_dir="/home/ec2-user/source/beanlet"
restart_trigger_file="/home/ec2-user/restart"

# check the current version in git
cd $beanlet_source_dir
echo "doing git pull"
git pull
current_version=`git log | head -n 1 | awk '{print $2}'`
echo "current version is $current_version"

# make sure the version file exists
if [ ! -e $version_file ]
then
	echo "$version_vile does not exist, creating..."
	echo $current_version >  $version_file
fi

previous_version=`cat $version_file`

echo "previous version was $previous_version"

# if nothing has changed, exit
if [ "$current_version" == "$previous_version" ]
then
	echo "versions are same, doing nothing"
	exit 0
fi

# update current version file to current git hash
echo "updating $version_file to $current_version"
echo "$current_version" > $version_file

# perform maven build
echo "doing build"
/opt/apache-maven-3.3.9/bin/mvn clean package -Dmaven.test.skip=true
echo "build complete"

# touch restart file so root job knows to restart the app
echo "creating restart file"
touch "$restart_trigger_file"

exit 0
