Bei Bestellungen muss geprüft werden, ob Rabatte gelten.
Ausserdem werden bei einem hohen Warenwert keine Versandkosten berechnet.

!-- Falls die Story in Eclipse nicht richtig dargestellt wird:
!-- 1. JBehave-Eclipse-Plugin runterladen.
!-- 2. Im Plugin Sprache auf Deutsch stellen.

Meta:
@Komponente Bestellung
@Anforderung OOS-1859
@Verfasser Guybrush Threepwood

Erzählung:
Um Angebote und Rechnungen zu erstellen
Als ein Sachbearbeiter im Verkauf
Möchte ich Rabatte, Versandkosten und Lagerbestand berücksichtigen

VorgegebeneStories:
  shop/stories/Login.story

Szenario: Kleine Menge wird bestellt
Gegeben im Lager sind 300 T-Shirts
Wenn ein Kunde 5 T-Shirts bestellt
Dann ist die Bestellung auf Lager
Und gilt eine Ermässigung von 0 Prozent
Und kostet ein T-Shirt pro Stück 10 Euro
Und betragen die Versandkosten 7,50 Euro
Und ist der Bestellwert 57,50 Euro

Szenario: Versandkosten fallen weg
Wenn ein Kunde 20 T-Shirts bestellt
Dann betragen die Versandkosten 0 Euro
Und ist der Bestellwert 200 Euro

Szenario: Rabattgrenzen werden erreicht
Wenn ein Kunde <Anzahl> T-Shirts bestellt
Dann gilt eine Ermässigung von <Rabatt> Prozent
Und kostet ein T-Shirt pro Stück <Preis> Euro
!-- TODO: Gesamtbestellwert könnte auch noch geprüft werden.
Beispiele:
|------|----- |-----|
|Anzahl|Rabatt|Preis|
|------|------|-----|
|49    |0     |10   |
|50    |10    |9    |
|99    |10    |9    |
|100   |20    |8    |
|------|------|-----|
