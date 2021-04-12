#!/bin/bash
# Gallery launcher


# Usage

usage_print()
{
    printf "Gallery: %s [-b]\n" "$0"
}

help_print()
{
    usage_print
    printf "    Installs and launches the Gallery project.\n"
    printf "\n"
    printf "    Options:\n"
    printf "      -b    build the whole project from scratch\n"
}


# Logs

logs_check()
{
    if [ ! -d logs ]; then
        mkdir logs
    fi
}


# Options

if [ $# -gt 0 ]; then
    if [ $# -ne 1 ]; then
        usage_print
        exit 1
    fi

    if [ "$1" = "-b" ] || [ "$1" = "--build" ]; then
        printf "Building project...\n"
        logs_check
        mvn clean install &> logs/build.log
    elif [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
        help_print
        exit 1
    else
        usage_print
        exit 1
    fi
fi


# Database

if [ "$(pgrep mysql | wc -l)" -eq 0 ]; then
    printf "Launching MySQL service...\n"
    sudo service mysql start &> /dev/null
fi

if [ "$(pgrep mysql | wc -l)" -eq 0 ]; then
    printf "Fatal error: MySQL could not launch.\n"
    printf "Please make sure MySQL is installed and functional.\n"
    exit 1
fi

printf "Initializing database...\n"
cat db.sql | sudo mysql


# Backend

printf "Launching backend...\n"
cd backend || exit 1
logs_check
mvn spring-boot:run &> ../logs/backend.log &
pids[0]=$!
cd ..


# Frontend

printf "Launching frontend...\n"
cd frontend || exit 1
logs_check
npm run serve &> ../logs/frontend.log &
pids[1]=$!
cd ..


# Loop

key="q"
printf "Gallery project launched. Check logs in the logs folder.\n"
printf "Press %c to close the server.\n" "$key"

while read -r -n1 char; do
    printf "\r \r"
    if [ "$char" = "$key" ] || [ "$char" = "${key^^}" ]; then
        break
    fi
done

pkill -INT -P ${pids[1]}
pkill -INT -P ${pids[0]}
pkill -INT -P $$

if [ "$(pgrep -f gallery | wc -l)" -gt 0 ]; then
    pkill -KILL -f gallery
fi
