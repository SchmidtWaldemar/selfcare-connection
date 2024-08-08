# Anmeldung individualisieren

Wir haben uns viel Arbeit durch Spring Boot gespart, indem wir die vorgegebene Standard Anmeldung verwendet haben. In sehr vielen Fällen kann das sogar ausreichen, doch wir möchten unsere Anmeldeseite später weiter verändern. Zum Beispiel brauchen wir eine Passwort Vergessen Funktion, die wir aus der Login Seite erreichen möchten. Aber auch das Look and Feel Prinzip, indem wir alle Seiten ähnlich aussehen lassen, kommt uns später zugute. Dazu werden wir später auch s.g. Templates verwenden, die über alle Seiten gleich verteilt werden.

## Individueler Look

Für ein angenehmes Look and Feel innerhalb der Seiten ist Bootstrap wohl die sinnvollste Wahl. Dazu verwenden wir die minimale Variante von Bootstrap, die wir als Javascript und CSS herunterladen und bei allen Seiten inkludieren.
Entscheident bei Bootstrap sind die 'class' Attribute. Hier hilft oft die Dokumentation von Bootstrap weiter.

## Benutzerbereich nach Anmeldung

Eigentlich steht es uns frei, auf welche Seite wir nach dem Login wechseln. Bei unserem Beispiel bleiben wir auf der Startseite und listen darin weitere Seiten auf. Die Übersichtseite wäre hier ebenfalls eine gute Wahl. Denn diese Seite können wir nur sehen, wenn wir uns erfolgreich anmelden konnten.

## Admin Bereich

Der Admin Bereich ist nur für Nutzer zugänglich, die Admin Rechte besitzen. Die Auflistung aller Nutzer können damit nur die Bentuzer mit dieser erweitereten Rechte sehen. Hier sollte man in Zukunft vorsichtig sein, wer die Rolle eines Admins erhält. Denn diese Rolle kann für Gewöhnlich alles steuern und bei Bedarf sogar Nutzer für weitere Anmeldungen blockieren.
