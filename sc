#!/bin/zsh
############################################
# show syscall table for the Linux kernel
############################################

Usage(){
  echo "Usage: "
  echo "   $0 <system call function name>"
  exit -1
}
if [ $# -lt 1 ];then
  Usage
fi
f_name=$1
if [ $# -gt 1 ];then
  f_name=$1
  shift
  f_args=$@
else
  f_args="32"
fi
# echo "f_args: $f_args"
f_filename=$HOME/profile/syscall.txt
if [ $f_args = "64" ];then
  f_filename=$HOME/profile/syscall64.txt
  fmt_str="%-4s%-10s%-5s%-24s%-20s%-15s%-10s%-10s%-10s%-10s"
elif [ $f_args = "arm" ];then
  f_filename=$HOME/profile/syscall_arm.txt
  fmt_str="%-10s%-10s%-10s%-24s%-20s%-15s%-10s%-10s%-10s%-10s"
else
  f_filename=$HOME/profile/syscall.txt
  fmt_str="%-4s%-10s%-5s%-24s%-20s%-15s%-10s%-10s%-10s%-10s"
fi

# echo "f_filename: $f_filename"
if [ ! -f $f_filename ];then
  echo "File $f_filename not exist."
  exit -1
fi

line=`cat $f_filename|awk '{print $2}'|grep -n -w $f_name |awk -F':' '{print $1}'`
# echo "line: $line"

if [ -z $line ];then
  echo "No such function"
  exit -1
fi

h_content=`cat $f_filename|head -n 1|awk '{printf "'${fmt_str}'",$1,$2,$3,$4,$5,$6,$7,$8,$9,$10}'`
echo $h_content
contents=`cat $f_filename| sed -n "${line},${line}p"| awk -F'\t' '{printf "'${fmt_str}'",$1,$2,$3,$4,$5,$6,$7,$8,$9,$10}'`
echo $contents
