#!/usr/bin/bash

echo "Today is " `date`

echo -e "\nEnter the Path :"
read the_path

echo -e "\nPath has following contents :"
ls $the_path
