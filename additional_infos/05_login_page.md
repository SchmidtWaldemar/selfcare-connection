# Anmeldung und Benutzerbereich

Nachdem wir einen Benutzer automatisch mit Admin Rechten angelegt haben, möchten wir uns natürlich auch damit einloggen können.

## Login

Wir nutzen die Login Maske, die von Spring Boot vorgegeben ist. Der Admin Nutzer, den wir bereits automatisch angelegt haben, muss aktiviert oder deaktiviert werden könne. 
Dazu haben wir beim User Entity ein weiters Feld 'enabled' zum speichern in der Datenbank freigegeben. Beim bestehenden Admin müssen wir unter Umständen jedoch erst den Benutzer auf enabled setzen:

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
MariaDB [testdb]> update users set enabled = true where email = 'admin@your-domain.net';
Query OK, 0 rows affected (0.000 sec)
Rows matched: 1  Changed: 0  Warnings: 0
```

Damit können wir uns nun erfolgreich bei der Plattform anmelden.

## Logout

Auch beim Logout wird der Standard von Spring Boot genutzt. Bei der nächsten Version stellen wir das Login und Logout auf eigene Methoden um.
