#!/usr/bin/env bash
##
## pilaje build script
## This script builds pilaje with javac 
## Jacob Peck
## 20120811
##

# variables
SRCFILES=src/suschord/pilaje/*.java
CLASSDIR=classes/
CP=src/suschord/pilaje:.

# determine platform
PLATFORM=${OSTYPE//[0-9.]/}

# make CLASSDIR if it doesn't already exist
if [ ! -e $CLASSDIR ]; then
  mkdir $CLASSDIR
fi

# remove .jar if it already exists
rm *.jar >> .mkjar_log

# compile
if [ "$PLATFORM" == "cygwin" ]; then
  CP=`cygpath -wp $CP`
fi
javac -cp $CP -d $CLASSDIR $SRCFILES $1

# make jar
cd $CLASSDIR
jar cvfm ../pilaje.jar Manifest.txt suschord/* >> ../.mkjar_log
cd ..

# exit gracefully
exit 0
