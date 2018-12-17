#!/bin/bash
dr=$1
[ -z $dr ] && dr=$(pwd)
drs=( $(ls $dr) )
#echo "$dr"
#echo "${drs[@]}"
sz=${#drs[@]}
for((i=0;i<sz;i++)); do
	[[ -x ${drs[$i]} && ! -d ${drs[$i]} ]] && echo "${drs[$i]}"
done
