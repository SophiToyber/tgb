#!/bin/bash
if [ "$( docker service ps -f 'desired-state=Running' --format '{{.DesiredState}}' infrastructure_bf_telegram-api-service )" == "Running" ];
then docker service update --force --no-resolve-image --image bf_telegram-api infrastructure_bf_telegram-api-service;
else docker service create --network infrastructure_net -p 8086:8086 --name infrastructure_bf_telegram-api-service bf_telegram-api;
fi
