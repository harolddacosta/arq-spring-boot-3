#!/bin/bash

docker run -d \
  --name keycloak-server \
  -p 9090:8080 \
  -e KEYCLOAK_USER=admin \
  -e KEYCLOAK_PASSWORD=letmein \
  -e DB_USER=keycloak_dev_user \
  -e DB_PASSWORD=1234 \
  -e DB_ADDR=192.168.0.50 \
  -e DB_PORT=5434 \
  -e DB_VENDOR=postgres \
  -e PROXY_ADDRESS_FORWARDING=true \
  -e KEYCLOAK_FRONTEND_URL=http://192.168.0.50:9090/auth \
  jboss/keycloak \
  -Dkeycloak.profile.feature.upload_scripts=enabled
