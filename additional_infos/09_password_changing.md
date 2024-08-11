# Passwort Änderungen

Wer sich schon einmal damit beschäftigt hat eine Passwort geschützte Umgebung aufzubauen, kennt vielleicht die ganzen Fallstricke, auf die man bei jeder Kleinigkeit beachten muss. Denn es braucht nur einen Fehler, der Angreifern Tür und Tor öffnen können. Dieser Commit enthällt 2 Faktor Authentifizierung, die sowohl für die Registrierung als auch für das Passwort Vergessen greift.

## Passwort Vergessen

Ähnlich wie bei der Registrierung kann der Benutzer die Passwort Vergessen Funktion erst nutzen, wenn eine E-Mail empfangen werden kann. Der Link innerhalb der E-Mail leitet den Benutzer auf eine weitere Seite um, wo ein neues Passwort vergeben werden kann. Erst wenn der dafür verwendete Token bereits abgelaufen ist, kann ein neuer Token beantragt werden. Dadurch soll verhindert werden, dass Hacker durch wiederholte Eingaben SPAM Attaken durchführen können.

Sollte Jemandem (gravierende) Fehler auffallen, informiert mich bitte dazu bei anonym-chat.de unter Kontakt.