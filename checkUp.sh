ip="$(docker inspect pofo-stage-api-new | grep IPAddress | tail -1 | grep -oE '\b([0-9]{1,3}\.){3}[0-9]{1,3}\b')"
counter=0
while ! curl -sf http://$ip:5555/pofo/client/test; do
  sleep 1
  docker logs -t --tail 1 pofo-stage-api
  counter=$((counter+1))
  if [[ "$counter" -gt 420 ]]; then
       echo "Waited for 7 minutes; terminating Server"
       docker rm -f pofo-stage-api
       exit 1
  fi
done
echo
echo "Server Started"