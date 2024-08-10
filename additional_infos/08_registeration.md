# Benutzerregistrierung

Ab heute möchten wir neue Benutzer auf unserer Plattform begrüßen können. Dazu wird eine klassische Registrierungsseite, bei der eine E-Mail Verifizierung stattfinden muss. 

## E-Mail Versand einrichten

Um E-Mails bei den Registrierungen zu versenden braucht man einen E-Mail Server (genauer gesagt einen SMTP Server). Wenn man sich so überlegt, könnte man ja Einfachheit halber sich einen Account z.B. bei GMX.de erstellen und diese E-Mail dann für den Versand verwenden. Das geht zwar bei einem persönlichen Client wie dem Thunderbird sehr gut, doch mit dem JavaMailer leider nicht. Diesen Test habe ich auch mal versucht und folgende Fehlermeldung erhalten:

```
550 Sender address is not allowed.
```

Deshalb habe ich mich entschieden, einen E-Mail Account mit samt einer eigenen Domain-Adresse zu mieten. Also ähnlich wie bei dem Web-Server, den ich hier für den Live Betrieb nutze. Eine andere Möglichkeit wäre ein Raspebrry Pi und ... Scherz beiseite :-)

Klar lässt sich auch ein E-Mail Server zwar selbst erstellen, doch ich spreche hier aus Erfahrung, dass soetwas ziemlich komplex werden kann, vor allem wenn man den Server öffentlich nutzt. Deshalb kann ich das eher nicht empfehlen. Stattdessen würde ich daher z.B. bei Strato mieten und verwenden.

Wer allerdings auf einen E-Mail Server verzichten möchte, kann unter aplication.properties den Flag 'config.service.mailSenderActive=true' auf false setzen.i
Ansonsten konfiguiert die Parameter in der 'aplication.properties' und die Registrierung sollte zwar direkt funktionieren, doch die Account Verifizierung ist zum Zeitpunkt noch nicht möglich.


 