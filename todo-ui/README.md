# todo-ui

## Deployment
1. Follow the instructions mentioned in **workspace/subprojects/todo-app/todo-server/README.md before you continue here.
2. Copy the following from **workspace/subprojects/todo-app/todo-ui/** in your machine to **/home/username/workspace/todo/todo-ui/**
    - babel.config.js
    - nginx.conf
    - package-lock.json
    - src/
    - Dockerfile
    - jsconfig.json
    - package.json
    - public/
3. If you haven't done that in menu service yet. Then run the script **/home/username/workspace/todo/setup/create-ssl.sh** to create the ssl certificates.
4. Run the service using: ```sudo docker compose -f /home/username/workspace/todo/docker-compose.yml up todo-ui -d```

