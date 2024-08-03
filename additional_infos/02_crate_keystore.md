# Keystore Datei erstellen

Für eine ssl Verbindung sollte vor dem Start innerhalb der Entwicklungsumgebung eine keystore Datei erstellt werden. Dazu können folgende Aufrufe helfen:

```
cd ~/git/selfcare-connection/src/main/resources/
mkdir -p certs
cd certs/
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore localhost.p12 -validity 3650 -storepass changeit
cd ../
mvn spring-boot:run
```

Für einen schnellen Test kann der Browser curl verwendet werden:

```
sudo apt install curl
```

Und anschließend die Seite aufgerufen werden. Sollte eine html Ausgabe sichtbar sein, dann ist der Server erfolgreich gestartet:

```
curl -k https://localhost:8443
```
