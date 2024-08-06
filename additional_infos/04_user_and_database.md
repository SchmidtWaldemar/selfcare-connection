# Datenbank und Benutzer einrichten

Ohne Datenbank und Benutzermanagement lässt sich eine Online-Plattform nur schwer realisieren. Deshalb entscheiden wir uns für die am meisten gebräuchliche Datenbank MySQL bzw. die neuere Version davon MariaDB. Anschließend speichern wir einen Administrator Benutzer in die Datenbank, mit dem wir künftig die volle Berechtigung erhalten, um unter andrerem weitere Benutzer zu verwalten.

## Einstellungen vor dem Start anpassen

In der vorherigen Version wollte ich zur Übung die Dokumentenbasierte MongoDB Datenbank benutzen. Die Einrichtung unter Docker und Spring Boot war mit etwas Aufwand zwar möglich, doch meine Erfahrungen mit dokumentenbasierten Datenbanken ist derzeit nicht so weitreichend, dass ich mich dafür begeistern konnte. Sollte sich dennoch eine gute Gelegenheit ergeben diese Datenbank zu nutzen, kann ich mir vorstellen, diese paralell mit der MariaDB mit einzubauen. Ansonsten bleiben wir zunächst bei dem relationalen Prinzip.

Die Einstellungen bei der Datenbank sollten auf jeden Fall für den Live-Betrieb bei den folgenden Dateien geändert werden:

```
cd ~/git/selfcare-connection/
nano .env
nano init.sql
nano src/main/resources/application.properties
```

Nun kann zunächst die Entwicklungs Umgebung gestartet werden:

```
docker-compose -f docker-compose-dev.yml up --build -d
```

Nun sollte man sicherstellen, dass die veränderten Container alle korrekt gestartet werden konnten:

```
docker logs selfcare-connection-server
docker logs selfcare-connection_maria_db_1
```

Wenn alles OK zu sein scheint, ist es ratsam, auch die Datenbank zu überprüfen, ob der Admin Benutzer angelegt werden konnte:

```
docker exec -ti selfcare-connection_maria_db_1 /bin/bash

root@5a081ef9367a:/# mariadb -u root --password=123456
Welcome to the MariaDB monitor.  Commands end with ; or \g.
Your MariaDB connection id is 26
Server version: 11.4.2-MariaDB-ubu2404 mariadb.org binary distribution

Copyright (c) 2000, 2018, Oracle, MariaDB Corporation Ab and others.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

MariaDB [(none)]> use testdb
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
MariaDB [testdb]> select * from users;
+----+------------+-----------+-----------------------+----------------------------------------------------------------------+
| id | first_name | last_name | email                 | password                                                             |
+----+------------+-----------+-----------------------+----------------------------------------------------------------------+
|  1 | Super      | Admin     | admin@your-domain.net | {BCRYPT}$2a$10$DkgXnt7d6jBCFAAyStLw9.xxj9fRDdp0gtozteBDbNps4Vdu.98Fa |
+----+------------+-----------+-----------------------+----------------------------------------------------------------------+
1 row in set (0.000 sec)
```

Wenn das Ergebnis so ähnlich aussieht wie oben, haben Sie es geschafft. Nun können Sie die Applikation auch in der Live-Umgebung einspielen. Allerdings wird nach Außen hier keine Änderung z.B. für Ihre Freunde sichtbar. Sollten Sie bereits eine Version Live eingespielt haben, können Sie daher mit diesem Schritt erstmal auf die nächste Version warten.
