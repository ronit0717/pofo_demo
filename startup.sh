echo "Startup pofo-app"
if [ -z $1 ]
then
  echo "Profile detail not present... startup failed"
  exit 1
fi

echo "Initiating build... profile : "$1
mvn clean install -P$1 || exit 1
echo "Creating docker image"
docker build . -t pofo-app || exit 1
echo "Launcing application pofo-app"
java -jar -Dspring.profiles.active="$1" target/pofo-app.jar || exit 1
exit 0