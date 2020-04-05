echo "Startup pofo-app"
if [ -z $1 ]
then
  echo "Profile detail not present... startup failed"
else
  echo "Profile: "$1

  echo "Initiating build"
  mvn clean install -P$1
  if [[ "$?" -ne 0 ]] ; then
    echo 'Build failure occured'
    exit 1
  fi

  echo "Creating docker image"
  docker build . -t pofo-app
  if [[ "$?" -ne 0 ]] ; then
    echo 'Docker image creation failed'
    exit 1
  fi

  echo "Launcing application pofo-app"
  java -jar -Dspring.profiles.active="$1" target/pofo-app.jar
fi