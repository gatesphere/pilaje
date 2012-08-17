#!/usr/bin/env bash
##
## pilaje run script
## This script runs pilaje with java 
## Jacob Peck
## 20120812
##

# variables
JARFILE=pilaje.jar

# run 
java -jar $JARFILE $@

# exit gracefully
exit 0
