#!/bin/bash
# Gallery launcher


# Usage

usage_print()
{
    printf "Gallery: %s [-b | --build] [-r | --reset]\n" "$0"
}

help_print()
{
    usage_print
    printf "    Installs and launches the Gallery project.\n"
    printf "\n"
    printf "    Options:\n"
    printf "      -b    build the whole project from scratch\n"
    printf "      -r    reset the database to its defaults\n"
}


# Options

if [ $# -gt 0 ]; then
    if [ $# -eq 1 ]; then
        if [ "$1" = "--help" ]; then
            help_print
            exit 1
        fi
    fi

    for i; do
        if [ "$i" = "-b" ] || [ "$i" = "--build" ]; then
            build=true
        elif [ "$i" = "-r" ] || [ "$i" = "--reset" ]; then
            reset=true
        elif [ "$i" = "-f" ] || [ "$i" = "--force" ]; then
            force=true
        else
            usage_print
            printf "%s: invalid argument\n" "$i"
            exit 1
        fi
    done
fi


# Logs

if [ ! -d logs ]; then
    mkdir logs
fi


# Build

if [ "$build" ] || [ ! -f .hush ]; then
    printf "Building project...\n"
    if ! mvn clean install &> logs/build.log; then
        printf "Fatal error: 'mvn clean install &> logs/build.log' failed.\n"
        if [ ! "$force" ]; then exit 1; else skip_hush=true; fi
    fi
    if [ ! "$skip_hush" ]; then touch .hush; fi
fi


# Database

if [ "$(pgrep mysql | wc -l)" -eq 0 ]; then
    printf "Launching MySQL service...\n"
    if ! sudo service mysql start &> /dev/null; then
        printf "Fatal error: 'sudo service mysql start &> /dev/null' failed.\n"
        if [ ! "$force" ]; then exit 1; fi
    fi
fi

if [ "$(pgrep mysql | wc -l)" -eq 0 ]; then
    printf "Fatal error: MySQL could not launch.\n"
    printf "Please make sure MySQL is installed and functional.\n"
    if [ ! "$force" ]; then exit 1; fi
fi

if [ "$reset" ]; then
    printf "Reseting database...\n"
    if ! sudo mysql < database/db_reset.sql; then
        printf "Fatal error: 'sudo mysql < database/db_reset.sql' failed.\n"
        if [ ! "$force" ]; then exit 1; fi
    fi
fi

printf "Initializing database...\n"
if ! sudo mysql < database/db_init.sql; then
    printf "Fatal error: 'sudo mysql < database/db_init.sql' failed.\n"
    if [ ! "$force" ]; then exit 1; fi
fi


# Backend

printf "Launching backend...\n"
cd backend || exit 1
mvn spring-boot:run &> ../logs/backend.log &
pids[0]=$!
cd ..


# Frontend

printf "Launching frontend...\n"
cd frontend || exit 1
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
    pkill -KILL -f pdl/gallery
fi
