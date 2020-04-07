ip="$(docker inspect pofo-stage-api | grep IPAddress | tail -1 | grep -oE '\b([0-9]{1,3}\.){3}[0-9]{1,3}\b')"
counter=0
while ! curl -sf http://$ip:5555/pofo/client/test; do
  sleep 1
  echo "Server not yet started, waiting"
  counter=$((counter+1))
  if [[ "$counter" -gt 300 ]]; then
       echo "Waited for 5 minutes; terminating Server"
       docker rm -f pofo-stage-api
       docker logs pofo-stage-api
       exit 1
  fi
done
echo
echo "Server Started"