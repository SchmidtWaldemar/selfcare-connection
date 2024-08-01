# selfcare-connection
Online Plattform für Selbsthilfe Themen

## Projekt starten
Bei der Entwicklung des Projekts wird Ubuntu (22.04 oder aktueller) eingesetzt. Die Veröffentlichung (Deployment) erfolgt anschließend unter Debian 11 oder 12.

Die Konfigurationsbeschreibung basiert demnach unter den genannten Systemen. Unter Windows oder Mac kann die Syntax etwas abweichen.

Zum starten des Projekts werden zunächst folgende Anwendungen benötigt:

* Java 17 (oder aktueller)

```
sudo apt install openjdk-17-jdk
```

* Maven

```
sudo apt install maven
```

* Git

```
sudo apt install git
```

Für ein Projektdownload können nun folgenden Scripts in der Console ausführt werden:

```
cd ~
mkdir -p git
cd git/
git clone https://github.com/SchmidtWaldemar/selfcare-connection.git
```

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

