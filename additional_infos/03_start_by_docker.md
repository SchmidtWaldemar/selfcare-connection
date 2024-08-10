# Server lokal und remote starten

Zum Start unserer "selfcare-connection" Plattform möchten wir Docker verwenden. Denn Docker bietet die Möglichkeit unser System ein Stück weit sauberer zu halten, indem wir nicht jeden Service wie die Datenbank oder Webserver zusätzlich installieren und pflegen müssen. Außerdem erspart Docker uns noch weitere Aufwände, die uns bei der Administration unseres Systems anfallen würden.

Unsere Ziele möchten wir daher wie folgt erfüllen:

Testumgebung nutzt folgende Dateien:

- Dockerfile
- docker-compose-dev.yml
- nginx_dev.conf

Liveumgebung nutzt folgende Dateien:

- Dockerfile
- docker-compose.yml
- nginx.conf


1. Die jeweilige Dockerfile installiert auf einem Container ein Maven Image, speichert die nötigen Dateien unserer Applikation und erzeugt ein Build. Im nächsten Stage bzw. Container wird ein Java Image installiert und die Build Dateien aus dem ersten Container (--from=build) kopiert und zum Schluss unsere Applikation gestartet.
2. Mit Docker Compose führen wir mehrere Services aus: I. Nginx Webserver, II. MongoDB Datenbank und III. unsere Applikation selbst, die innerhalb unserer Dockerfile beschrieben wird.
3. Die Konfigurationsdatei von Nginx beschreibt den Start des Webservers. Hier möchten wir mit einer gesicherten SSL Verbindung nach außen treten. Dazu benötigen wir vorher entsprechende Zertifikate und Schlüssel, damit uns der Start gelingt.

## docker installieren
Da der Server mithilfe von Docker und Docker-Compose gestartet werden soll, muss vorher Docker und docker compose installiert werden:

```
#### nur wenn nötig, ansonsten eher in der Live-Umgebung:
sudo apt-get install ca-certificates
sudo install -m 0755 -d /etc/apt/keyrings
####

curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc

chmod a+r /etc/apt/keyrings/docker.asc

echo   "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin docker-compose
```

Sollten Rechteprobleme bei der Ausführung auftreten, können die Gruppenrechte das Problem sein:

```
sudo groupadd docker
sudo usermod -aG docker <yourUsername>
```

## Testumgebung starten
Die Testumgebung ist Ihr eigener Rechner und die Applikation ist in der Regel nicht im Internet erreichbar.

```
docker-compose -f docker-compose-dev.yml up --build -d
```

Wenn hier eine Fehlermeldung auftaucht, dass ein Netzwerk fehlt, dann den folgenden Befehl aufrufen und danach wieder den Befehl davor:

```
docker network create proxy-network
```

Nach einem erfolgreichen Start, sollten Meldung unten wie folgt aussehen.

```
Creating selfcare-connection_mongodb_1 ... done
Creating selfcare-connection-server    ... done
Creating webserver                     ... done
```

Die Meldung oben kann allerdings täuschen. Vergewissern Sie sich, ob der webserver auch wirklich erfolgreich gestartet worden ist, indem Sie davon die Container-Logs ausgeben lassen:

```
docker logs webserver
```

Sollte unten etwas stehen wie:

```
nginx: [emerg] cannot load certificate "/etc/nginx/certs/nginx.crt":
```

Dann fehlt dem Webserver noch ein selbstsigniertes Zertifikat und der Schlüssel, die Sie wie folgt erzeugen können:

```
cd ~/git/selfcare-connection/
mkdir -p certs/
cd certs/
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout nginx.key -out nginx.crt
```

Nun ist es wichtig, dass auch die Keystore Datei unter Spring Boot unsere Keys und Zertifikate von Nginx kennt. Dazu führen wir nun den folgenden Script aus:

```
openssl pkcs12 -export -in nginx.crt -inkey nginx.key -out localhost.p12 -passout pass:changeit
```

Stoppen Sie anschließend alle Docker Container und starten Sie die Anwendung erneut:

```
docker-compose down && docker-compose -f docker-compose-dev.yml up --build -d
```

Nun testen Sie, ob der Server mit dem Browser erreichbar ist:

```
curl -k https://localhost
```


Damit sollte der folgende Aufruf eine HTML Ausgabe zeigen:

```
<!DOCTYPE HTML>
<html>
<head> 
    <title>Selbsthilfe Plattform</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    
    <link rel="stylesheet" type="text/css" href="/css/style.css" />
</head>
<body>
    <h1>Hallo, Anonym!</h1>
    <div>
        Herzlich Willkommen bei der Selbsthilfe Plattform. 
    </div>
</body>
</html>
```

Damit ist uns die erste "Hallo Welt!" Umgebung mit Docker geglückt ;-)

PS:
Auf einem Browser wie Firefox sollte ein Sicherheitsrisiko erkannt werden, wenn wir https://localhost aufrufen. Da wir uns selbst nicht als Risiko betrachten, können wir hier unter "Erweitert..." ruhig auf "Risiko akzeptieren und fortfahren" klicken.

## Live Umgebung starten
Nun möchten wir, dass  unsere Freunde die Seite auf der ganzen Welt bestaunen können. Das kann allerdings etwas komplizierter werden.

### 1. externen Server mieten

Für mich persönlich gibt es zwei Möglichkeiten einen Server mit dem Internet zu verbinden:

I. Zuerst hole ich mir einen nach möglichst wenig Stromschluckenden Rechner. Da kann auch ein einfacher Raspberry Pi sein, den ich am besten an die Fritzbox dranklemme. Wenn der Raspberry Pi ähnlich wie oben beschrieben eine Hallo Welt! Seite anzeigen kann, dann reicht das für Gewöhnlich schon für unsere Live-Umgebung. Als nächstes richte ich bei der Fritzbox eine dynamische DNS Adresse ein, die ich über das Internet aufrufen kann, um auf die Fritzbox zugreifen zu können. Die Adresse ist in der Regel schwer zu lesen und daher auch schwerer zu erraten. Wem das aber genügt, z.B. weil nur die Freunde die Adresse kennen sollen, dann bleiben wir bei dieser Lösung. Nun ist eine DNS Adresse da und damit unsere Raspberry Pi auch erricht werden kann, muss ich noch ein Port Forwording einrichten, damit die Fritzbox unsere Anfragen und Antworten aus und ins Internet durchleiten kann. Nun wären wir damit fertig für die nächsten Schritte.

II. Statt einen Server von zu Hause zu betreiben, möchten wir vielleicht einen Server mieten. Diese Lösung würde ich eher empfehlen, da es zum einen meistens sicherer ist, wenn sich die Betreiber des Rechenzentrum selbst darum kümmern, wenn z.B. meine Seite von Hackern übernommen wird. Zum anderen kann ich hier eine Web-Adresse festlegen, die wesentlich ansprechender ist. Denn bei der Fritzbox ändert sich für Gewöhnlich die IP immer mal wieder und eine DNS-Adresse müsste dann auch immer wieder eine andere IP erhalten. Das wäre sogar fatal, wenn z.B. unsere Domain auf die Fritzbox eines anderen Haushalts zeigt, usw.

Da die Vorteile bei einem gemieteten Server überwiegen, werden wir uns daher nur darauf konzentrieren. Bei IONOS von 1und1 zum Beispiel gibt es bereits Server bzw. virtuelle Hosts, die schon ab einem Euro pro Monat gemietet werden können. Wer hier eine Seite ins Internet stellen möchte, wo die Besucheranzahl überschaubar sein soll, der kann ruhig auf diese Light Version eines Servers setzen.

### 2. Domain einrichten
Nun also haben wir uns einen Server gemietet und erhalten Zugriff drauf. Als nächstes brauchen wir eine Domain-Adresse, die wir ab jetzt z.B. 'your-domain.net' nennen möchten. Auf der DNS Betreiberseite müssen wir jetzt nur noch die IP von unserem gemieteten Server eintragen. Haben wir jetzt eine IPv4 Adresse z.B. '123.234.132.231', dann tragen wir diese im Feld IPv4 und den Typ auf 'A' und lassen das Subdomain zunächst leer oder auf '@'. Nach dem speichern braucht es für gewöhnlich einige Minuten oder Stunden, bis unsere Domain die IP anzeigt. Kontrollieren können wir die Zuweisung mit dem folgenden Befehl:

```
host your-domain.net
```

### 3. Aplikation Live testen
Unsere Domain Adresse ist nun mit unserem gemietetem Server verbunden. Jetzt möchten wir unsere 'Hallo Welt!' Applikation einmal aus dem Internet heraus sehen. Zuvor brauchen wir unsere Dateien, die wir auch schon in der Testumgebung verwendet haben. Dazu haben wir nun zwei Möglichkeiten:

I. Wir klonen die Applikation aus dem Git heraus. Die Anleitung dazu findet ihr unter der Datei 'start_project.md'.
II. Wir haben unser Projekt bereits angepasst und möchten unsere eigenen Änderungen auf dem externen Server ausführen. Dazu kopieren wir unsere Dateien wie folgt auf den Server übertragen:

```
cd ~/git/
tar cvzf selfcare-connection.tar.gz selfcare-connection/
scp  selfcare-connection.tar.gz root@123.234.132.231:~/
ssh root@123.234.132.231
tar xvzf selfcare-connection.tar.gz
cd ~/selfcare-connection/
```

Nun haben wir unsere Applikation auf dem Server und befinden uns im korrekten Verzeichnis für unsere Ausführung. Doch zunächst müssen wir die 'nginx.conf' Datei anpassen (statt 'your-domain.net' auf Ihre Adresse ändern) und ein Zertifikat z.B. von letsencrypt installieren.

Bevor wir jedoch das Internet auf unseren Server lassen, richten wir die Firewall ein:

```
sudo apt install ufw
sudo ufw default deny incoming
sudo ufw default allow outgoing
sudo ufw allow ssh
sudo ufw allow 22
sudo ufw allow 80
sudo ufw allow 443
sudo ufw enable
```

Der https Port 443 ist nun aus dem Internet erreichbar. Jetzt können wir auch Docker installieren. Die Anleitung dazu finden Sie oben.

Kommen wir nun zu letsencrypt und erstellen uns dort ein Zertifikat:

```
apt install certbot python3-certbot-nginx
certbot --nginx --agree-tos --redirect --hsts --staple-ocsp --email test@your-domain.net -d your-domain.net
ls /etc/letsencrypt/live/
```

Sollte die Ausführung des oben genannten Scripts nicht funktionieren, muss vorher nginx als Server installiert und gestartet werden. Wenn danach der Script ausgeführt wird und die Letsencrypt Zertifikate vorhanden sind, muss Nginx wieder beendet werden, damit unser Docker auch erfolgreich wieder gestartet werden kann. Am besten ist, wenn Sie den Nginx Server komplett deaktivieren, damit dieser nicht beim Neustart des System automatisch wieder gestartet wird. ACHTUNG: sichert vorher alle vorherigen Einstallungen von Nginx falls solche vorhanden sind:

```
systemctl stop nginx.service
systemctl disable nginx.service
```

Wenn das Zertifikat erfolgreich erstellt werden konnte, so sollte die letzte ls Abfrage einige Verzeichnisse oder Dateien bereits anzeigen.
Wenn die Letsencrypt Dateien nicht erfolgreich angelegt werden konnten, dann müssen wir unsere Firewall Regeln prüfen und sicherstellen, dass auch z.B. unser Rechner daheim mit Ping sowohl mit der IP als auch der Domain erreichbar ist. 

Bevor wir nun die Anwendung mit Docker wie bei der Testumgebung starten können, müssen wir noch die 'localhost.p12' Datei erzeugen. Dazu verwnden wir die Letsencrypt Dateien:

```
cd ~/git/selfcare-connection/
mkdir -p certs/
cd certs/
openssl pkcs12 -export -out localhost.p12 -inkey /etc/letsencrypt/live/your-domain.net/privkey.pem -in /etc/letsencrypt/live/your-domain.net/cert.pem -certfile /etc/letsencrypt/live/your-domain.net/chain.pem
```


Nun ändern wir unter der 'nginx.conf' Datei die Domain 'your-domain.net' auf unsere Domain. Dies sollte an 3 Stellen der Fall sein. Danach sind wir bereit unsere Plattform zu starten:

```
cd ~/selfcare-connection/
docker-compose -f docker-compose.yml up --build -d
```

Überprüfen Sie nun auch, ob alle Docker Container mit unseren Services richtig gestartet werden konnten:

```
docker logs selfcare-connection_mongodb_1
docker logs selfcare-connection_server
docker logs webserver
```

Wenn alles korrekt läuft, können wir nun unsere Seite aufrufen und ohne irgendwelche Warnungen sehen können:

```
curl https://your-domain.net
```

Nun können wir all unsere Freunde anrufen und ihnen unsere neue Seite presentieren. Doch damit unsere Freunde auch ein paar Monate später davon überzeugt bleiben sollen, dass wir unsere Seite voll unter Kontrolle haben, sollten wir dafür sorgen, dass das Zertifikat bei Letsencrypt immer schön aktuell bleibt. 
Doch so einfach wie in den ganzen Beschreibungen, die wir bei Google finden wird es für uns nicht werden. Das liegt an dem von uns zuletzt ausgeführten 'openssl' Script. Denn sobald wir das letsencrypt Zertifikat erneuert haben, müssen wir auch unsere localhost.p12 Datei ebenfalls ändern. Tun wir das nicht, haben wir dann bei unserem nginx Server zwar ein gültiges Zertifikat, doch unser Tomcat Server unter Spring Boot nimmt die Anfragen vom nginx nicht mehr an bzw. es entsteht ein Ungültigkeitsproblem innerhalb der Kommunikation. Deshalb müssen wir bei unserem wiederkehrendem Cronjob erst prüfen, ob ein neues Zertifikat seitens letsencrypt vorliegt und in dem Fall muss Tomcat gestoppt, localhost.p12 erneuert und anschließend wieder gestartet werden.

Alles kein Problem mit unserem Script unter innerhalb unserer Datei 'keystore_renew.sh'. Dazu installieren wir erst Crontab und fügen darin die folgenden Befehle hinzu:

```
chmod a+x ~/keystore_renew.sh
apt install cron
(crontab -l || true; echo "0 6 2 * * certbot renew > /var/log/certbot-cron.log") | crontab -
(crontab -l || true; echo "5 6 2 * * ~/keystore_renew.sh > /var/log/keystore-cron.log") | crontab -
```

Die oberen Cron Einträge sorgen dafür, dass am 02. jeden Monats punkt 6 Uhr der 'certbot' Script letsencrypt fragt, ob eine Neuerung ansteht und die Zertifikate in diesem Fall erneuert. Nur einmal im Monat ist es eigentlich schon OK, da die Zertifikate für gewöhnlich etwa 3 Monate gültig sind. Anonsten informiert Letsencrypt euch per E-Mail, dass eine Erneuerung noch offen steht. Sogar wenn der keystore von Tomcat nicht mehr gültig ist, testet Letsencrypt von außen, ob der Zertifikat noch aktuell ist. D.h. auch, dass der Zertifikat von Tomcat der ist, den man eigentlich von Außen sieht und nicht der Zertifikat innerhalb der /etc/letsencrypt. Daher sollte man sich nicht viele Sorgen machen, dass die Seite irgendwann plötzlich von außen nicht erreichbar ist. 
Der zweite Cron Script wiederum wird 5 Minutne nach dem ersten aufgerufen. Sollte hier was schiefgehen, könnt ihr die folgende Datei '/var/log/keystore-cron.log' prüfen, ob darin 'keystore not renewed yet' steht, obwohl in der Datei '/var/log/certbot-cron.log' ein anderer Hinweis als 'No renewals were attempted' steht:

```
cat /var/log/certbot-cron.log
cat /var/log/keystore-cron.log
```

Viele der genannten Scripte wie zum Beispiel unter '~/keystore_renew.sh' habe ich Freihand eingefügt und nicht ausprobiert. Sollten sich so irgenwo Fehler eingeschlichen haben, könnt Ihr mir gerne z.B. unter Kontakte bei anonym-chat.de einen Kommentar dazu hinterlassen, damit ich es bei Zeiten korrigieren kann.

#### Troubleshooting

Funktioniert der Aufruf mit Curl weiterhin nicht, dann vermutlich wegen dem Zertifikat. Kopieren Sie das Zertifikat aus dem certs Ordner in den resource Bereich und starten Sie Docker neu:

```
cp certs/localhost.p12 src/main/resources/certs/
```
