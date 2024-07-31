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

Für einen schnellen Start dann den folgenden Script in der Console ausführen:

```
cd ~
mkdir -p git
cd git/
git clone https://github.com/SchmidtWaldemar/selfcare-connection.git
cd selfcare-connection/
mvn spring-boot:run
```
