# Todo-App

## Requirements
    - Java 11
    - gradle 7.3.2
    - Docker 23.0.1
    - Docker Compose v2.16.0
    - Node v16.14.2
    - npm v8.5.0

## Setup
- clone the Repo and enter the backend folder
  ```
  git clone https://github.com/Mieneas/experience.git && cd experience/todo-server
  ```
- start a postgresql DB using docker
  ```
  sudo docker run -d --name todoapp-db -p 5432:5432 -e POSTGRES_PASSWORD=todoapp -e POSTGRES_USER=todoapp -e POSTGRES_DB=todoapp postgres
  ```
- build the service
  ```
  gradle build
  ```
- remove previously created docker container
  ```
  sudo docker rm -f todoapp-db
  ```
- Go to service UI and install all dependencies
  ```
  cd ../todo-ui && npm install
  ```
- build the todo UI
  ```
  npm run build
  ```
- Go to the service root and run the docker-compose file to start the service
  ```
  cd .. && sudo docker compose up -d
  ```
- Now open a browser and search for
  ```
  localhost:8080
  ```
  to start using the service.
- Shutdown the service and delete all data
  ```
  sudo docker compose down && docker volume rm experience_todo-postgres
  ```


## My linkedin profile [here](https://de.linkedin.com/in/zead-alshukairi-190a0922a)