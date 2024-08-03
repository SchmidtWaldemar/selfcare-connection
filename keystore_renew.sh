#!/bin/bash

## change it by your own before using live
KEYSTORE_PASS="changeit"
DOMAIN="your-domain.net"
WORKDIR="~/selfcare-connection"

cat /var/log/certbot-cron.log | grep "No renewals were attempted"
if [[ $? -eq "0" ]]; then
    date
    echo "keystore not renewed yet"
    exit 0
else
    cd $WORKDIR
    docker-compose down
    openssl pkcs12 -export -out $WORKDIR/certs/localhost.p12 -inkey /etc/letsencrypt/live/$DOMAIN/privkey.pem -in /etc/letsencrypt/live/$DOMAIN/cert.pem -certfile /etc/letsencrypt/live/$DOMAIN/chain.pem -password pass:$KEYSTORE_PASS
    date
    echo "tomcat keystore file renewed"
    docker-compose -f docker-compose.yml up --build -d
fi