web: java $JAVA_OPTS -jar build/libs/jewel-0.0.1-SNAPSHOT.jar -Dserver.port=$PORT $JAR_OPTS
heroku buildpacks:clear
heroku buildpacks:add
heroku ps:scale web=1