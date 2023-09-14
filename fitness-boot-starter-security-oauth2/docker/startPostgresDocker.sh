#!/bin/bash

docker run -d \
  --name keycloak-dev-postgres \
  -p 5434:5432 \
  -e POSTGRES_USER=keycloak_dev_user \
  -e POSTGRES_PASSWORD=1234 \
  -e POSTGRES_DB=keycloak \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
  -v $HOME/keycloak_dev_pgdata:/var/lib/postgresql/data \
  -v "$PWD/custom-postgres.conf":/etc/postgresql/postgresql.conf \
  postgres \
  -c 'config_file=/etc/postgresql/postgresql.conf'
