echo -e "\nEnter a number:"
read num

if [ $num -gt 0 ]; then
	echo "$num greater than zero"
elif [ $num -lt 0 ]; then
	echo "$num is less than zero"
else
	echo "Number entered is zero"
fi


