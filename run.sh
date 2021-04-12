#!/bin/bash
# Gallery launcher


# Logs

if [ ! -d logs ]; then
    mkdir logs
fi


# Database

if [ "$(pgrep mysql | wc -l)" -eq 0 ]; then
    printf "Launching MySQL service...\n"

    sudo service mysql start &> /dev/null
fi

printf "Initializing database...\n"

sudo mysql < db.sql


# Backend

printf "Launching backend...\n"

if [ ! -f logs/backend.log ]; then
    touch logs/backend.log
fi

cd backend
mvn spring-boot:run &> ../logs/backend.log &
pids[0]=$!
cd ..


# Frontend

printf "Launching frontend...\n"

if [ ! -f logs/frontend.log ]; then
    touch logs/frontend.log
fi

cd frontend
npm run serve &> ../logs/frontend.log &
pids[1]=$!
cd ..


# Loop

key="q"
printf "Gallery project launched. Check logs in the logs folder.\n"
printf "Press q to close the server.\n"

while read -n1 char; do
    printf "\r \r"
    if [ "$char" = "$key" ]; then
        break
    fi
done

pkill -INT -P ${pids[1]}
pkill -INT -P ${pids[0]}
pkill -INT -P $$

if [ $(pgrep -f gallery | wc -l) -gt 0 ]; then
    pkill -KILL -f gallery
fi
