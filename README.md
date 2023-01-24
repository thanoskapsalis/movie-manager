# Movie Manager

## Installation

Step 1 - Clean & Install:

Execute the following command:

`mvn clean install`

Step 2 - Database:

In case you do not have Oracle Database Enterprise 12.2.0.1-slim installled you have to do the following,

-Add in the “insecure-registries” of the  “daemon.json” file (C:\Users\<YOUR_USER>\.docker) the following line:
"panda.centraldg6.athens.intrasoft-intl.private:32443"

- Open your terminal/cmd and execute the command:
  docker login panda.centraldg6.athens.intrasoft-intl.private:32443
  The username and password can be found: https://confluence.intrasoft-intl.com/pages/viewpage.action?pageId=61386385

- docker pull panda.centraldg6.athens.intrasoft-intl.private:32443/ermistn-oracle-12.2.0.1-slim:latest


Run `git config --global core.autocrlf input` in order to not changing line separator for Bash script

The Movie Manager application depends on a DB. if the DB is not initialized correctly the app will not connect to the DB.
The app needs the schema "movie-project" so you have to create it.
To do this you have to open a terminal and execute the command:

`docker-compose up -d movie-project-db`

Now the DB is up and running, you can connect via the below details
HOST: localhost
PORT: 1521
SID: ORCLCDB
Username & password: movie_manager_db

The app is running at port 8081
