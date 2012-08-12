#!/usr/bin/env bash
##
## pilaje run script
## This script runs pilaje with java 
## Jacob Peck
## 20120812
##

# variables
CLASSDIR=classes/
MAIN=suschord.pilaje.Main

# run 
cd $CLASSDIR
java $MAIN $1
cd ..

# exit gracefully
exit 0
