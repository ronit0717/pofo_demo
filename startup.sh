echo "Launcing application pofo-api"
java -jar -Dspring.profiles.active="$1" target/pofo-api.jar || echo "Server Start Failed";exit 1