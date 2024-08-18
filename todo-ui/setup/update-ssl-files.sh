#!/bin/bash

sudo docker compose -f /home/ziad/workspace/todo/docker-compose.yml exec todo-ui nginx -s reload
