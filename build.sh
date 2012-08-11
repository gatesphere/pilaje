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

# compile
if [ "$PLATFORM" == "cygwin" ]; then
  CP=`cygpath -wp $CP`
fi
javac -cp $CP -d $CLASSDIR $SRCFILES $1

# exit gracefully
exit 0
