# Todo
Is just a small todo program for showing Java, Vertx, VUE, SSL, JWT using keystore, docker and docker compose.

## Deployment
1. Build the todo-server project using gradle.
2. Create the following folder structure:
   - ```mkdir workspace/todo```
   - ```mkdir workspace/todo/setup```
   - ```mkdir workspace/todo/todo-server```
   - ```mkdir workspace/todo/tudo-ui```
   - ```mkdir workspace/todo/todo-server/build```
   - ```mkdir workspace/todo/todo-server/build/libs```
3. Copy the .sh files from **workspace/subprojects/todo-app/todo-ui/setup/&ast;** in  your machine to **/home/username/workspace/todo/setup/**
4. Copy docker-compose.yml file from **workspace/subprojects/todo-app/todo-server/docker-compose.yml** in your machine to **/home/username/workspace/todo/**
5. Copy Dockerfile file from **workspace/subprojects/todo-app/todo-server/Dockerfile** and setup.sh file from **workspace/subprojects/todo-app/todo-server/setup.sh** in your machine to **/home/username/workspace/todo/todo-server/**
6. Copy .jar file from **workspace/subprojects/todo-app/todo-server/build/libs/** in your machine to **/home/username/workspace/todo/todo-server/build/libs/**
7. Now you have all needed files.
8. execute the following commands in your remote server to set up the keystore for JWT.
   - ```chmod +x /home/username/workspace/todo/todo-server/setup.sh```
   - ```bash /home/username/workspace/todo/todo-server/setup.sh```
9. Now set up the Front end.
10. Run the services using: ```sudo docker compose -f /home/username/workspace/todo/docker-compose.yml up todo-db todo-app -d```
