#!/bin/bash

usage ()
{
    echo "$0 enable|disable|status"
    exit 1
}

if [ "$#" -ne 1 ]; then
    usage
fi

if [ "$1" == "enable" ]; then
    echo "Enable Android port forwarding"
    adb reverse tcp:8000 tcp:8000
elif [ "$1" == "disable" ]; then
    echo "Disable Android port forwarding"
    adb reverse --remove-all
elif [ "$1" == "status" ]; then
    adb reverse --list
else
    usage
fi