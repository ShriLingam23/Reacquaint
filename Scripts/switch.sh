#!/usr/bin/bash

echo -e "\nEnter a fruit:"
read fruit

case $fruit in 
	'apple')
		echo "This is a Red fruit"
		;;
	'banana')
		echo "This is a Yellow fruit"
		;;
	'orange')
		echo "This is a Orange fruit"
		;;
	*)
		echo "Unknown fruit"
		;;
esac
