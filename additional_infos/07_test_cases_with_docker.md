# JUnit Tests

Es gibt mehrere Möglichkeiten die einzelnen Klassen und Methoden automatisiert zu testen. Bei der Plattform beschränken wir uns allerdings auf 3 wichtige Tests:

1. gesicherte Datenbank Verbindung mit SSL aufbauen
2. Benutzer in dei Datenbank speichern und auslesen
3. UI Test auf die Login Seite

Wir haben außerdem auf Tests mit Mockito verzichtet, da wir einen echten Verbindungsaufbau testen wollten.

## Verbindungen aufbauen

Unter Docker Compose gestalten sich die Unit-Tests etwas schwieriger als gewöhnlich. Dabei entscheiden wir uns die Tests nur unter der Entwicklungsumgebung auszuführen und unter Docker Compose im eigenständigen Container laufen zu lassen. Die Datenbank und alle Entity-Tabellen werden eigenständig im Test-Container neu erstellt und anschließend mit Daten beschrieben. Zu den weiteren Herausforderungen gehört die SSL Verbindung, die wir mit einem separatem Truststore Zertifikat aufbauen können. Dazu müssen wir zunächst ein Zertifikat mit 'keytool' wie folgt erstellen:

```
cd ~/git/selfcare-connection/certs
keytool -genkey -noprompt -alias tomcat -dname "CN=localhost, OU=Private, O=, L=, ST=, C=DE" -keystore keystore_test.jks -storepass password -KeySize 2048 -keypass password -keyalg RSA -validity 3650
```

Sollte Docker das Zertifikat nicht finden, können Sie es direkt in den Ressource Ordner kopieren:

```
cd ~/git/selfcare-connection/src/main/resources/
mkdir -p certs
cp ~/git/selfcare-connection/certs/* certs/
```

Danach können wir unsere Entwicklungsumgebung starten:

```
cd ~/git/selfcare-connection
docker-compose -f docker-compose-dev.yml up --build -d
```

Da die Tests nun auf einem eigenständigen Container laufen, müssen wir den Erfolg der Tests prüfen:

```
docker logs selfcare-connection_tests_1
```

Taucht nach einiger Zeit ein 'BUILD SUCESSFUL' auf, dann hat alles soweit geklappt.

TIPP: sollte der Test-Container nicht mehr gestoppt werden können, lässt sich der Stopp mit der folgenden Parameter Erweiterung erzwingen:

```
docker logs selfcare-connection_tests_1
```

## Datenbank schreiben

Den Test in die Datenbank zu schreiben haben zusammen mit der SSL Verbindung Test verbunden. Das lag eher an der SSL Verbindung, wobei auch Spring Boot beim Start ebenfalls versucht einen Datenpool aufzubauen. Dazu müssen wir auch die Datenbank vorher starten, damit alles korrekt funktioniert. Eine bessere ist auf die Schnelle nicht gefunden worden.

## UI Test

Leider scheint Docker keine UI Tests zu beherrschen. Zumindest muss bei solchem innerhalb der ControllerAndJpaTest Klasse der Test auskomentiert werden und z.B. unter Eclipse gestartet werden. Hier öffnent sich entweder ein Crome oder Cromium Browser. Da wir einen selbstsignierten SSL Zertifikat verwenden, kann der Browser die Seite nicht automatisiert korrekt anzeigen. 
