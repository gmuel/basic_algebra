#!/bin/bash
dr=$1
hm=$0
flName="list_libs.sh"
hm=${hm%$flName}
echo $hm
[ -z $dr ] && dr=$(pwd)
execs=( $(${hm}exec_in_bin.sh $dr) )
declare -a misdLib
cnt=0
for i in ${execs[@]}; do
	libs=( $(ldd $i ) )
	myLib=
	for j in ${libs[@]}; do
		[[ "$j" == "$i" && "$j" == "$i:" ]] && continue
		if [ -n $(echo $j | grep -qi .so) ]; then
			myLib=$j
		else
			echo $j | grep -qi 'not found' && misdLib[$cnt]=$myLib && cnt=$(($cnt+1))
		fi
	done
	
done
echo ${misdLib[@]}
