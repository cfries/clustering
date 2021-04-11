Bisection
==========

****************************************

**Teilung einer Menge in zwei Gruppen basierend auf Nebenbedingungen und Präferenzen**

****************************************

Das Programm nimmt mittels "Brute-Force" eine Teilung einer Menge von Elementen (z.B. Schülern) in zwei Gruppen A und B vor.
Dabei werden zwei Arten von Bedingungen berücksichtigt:

- Nebenbedingungen, dass in der Gruppe A exakt n Schüler sein müssen für die eine Kriterium X den Wert x hat. Ein solches Kriterium kann z.B. sein X = "2. Fremdsprache", "x = Latein". Es können beliebig viele Kriterien vorgegeben werden ("2. Fremdsprache", "Religion", etc.) und für jedes Kriterium beliebig viele Ausprägungen ("Latein", "Spanisch", "Französisch").

- Präferenzen, dass ein Element (z.B. Schüler) falls möglich mit einem anderen Element (z.B. befreundeter Schüler aus Lerngruppe, etc.) in der gleichen Gruppe sein soll.

Motivation für diese Optimierung ist die Einteilung in zwei Gruppen für einen Wechselunterricht zur Reduktion der Ausbreitung einer Infektion (R-Wert). Dabei wird berücksichtig, ob Schüler außerhalb der Schule kontakt haben.

Anmerkung
-------

Es handelt sich hier um ein Projekt für den Eigengebrauch ohne Anspruch auf Benutzerfreundlichkeit. Wenn es für andere nützlich ist, kann ich es nach Feedback - sofern möglich - gerne weiter entwicklen. Hierzu könnte man ein Issue hier auf GitHub öffnen.

Warum Brute Force?
-------

Der Algorithmus enumeriert Brute-Force alle Kombinantion. Es sind hier sicher andere Ansätze möglich (e.g. auch Monte-Carlo Methoden), allerdings besitzt Brute Force den Charm, dass man garantieren kann, dass alle möglichen Szenarien betrachtet wurden. Für eine Klasse von 29 Elementen mit 500 Millionen möglichen Kombination benötigt das Programm ca. 30 Sekunden.

Benutzung
-------

Das Programm besitzt aktuell kein GUI und keine Datenschnittstelle, sondern nur den nakten, einfachen Algorithmus. Auch gibt es noch keine Dokumentation. Wer sich mit Java auskennt könnte es trotzdem schon nutzen.

In der `main()`-methode von `com.christianfries.clustering.Bisection` findet sich ein Beispiel, dass angepasst werden kann.

Es wird zuerst die Grundmenge als "Liste" definiert. Für jede `Node` wird dabei als Argument spezifiziert welche Ausprägungen einzelne Kategorien haben. Dies ist eine Liste von Integern (Ausprägung). `List.of(a,b)` bedeutet z.B., dass die erste Kategorie die Ausprägung a hat und die zweite Kategorie die Ausprägung b.

```
		final List<Node> nodes = new ArrayList<>();
		nodes.add(new Node(List.of(0,0)));
		nodes.add(new Node(List.of(1,0)));
		nodes.add(new Node(List.of(0,1)));
```
Das Beispiel oben legt 3 Schüler an. Wenn die erste Kategorie "2. Fremdsprache" ist mit den Werten 0=Französisch, 1=Latein und die zweite Katregorie "Religion" mit den Werten 0=kat., 1=ev., dann haben die Schüler (Französisch,kat.), (Latein,kat.), (Französisch,ev.).

Sodann wird definiert welche Größen die Ausprägungen in Gruppe A haben sollen. Dies wird als `int[][]`-Matrix spezifiziert. So ist z.B.

```
		final int[][] mandatorySizes = new int[][] { new int[] {9,6}, new int[] {8,6,1} };
```

Die Spezifikation, dass die erste Kategorie 9 Element mit Ausprägung 0 und 6 Elemente mit Ausprägung 1 haben soll und die zweite Kategorie 8 Elemente mit Ausprägung 0, 6 Elemente mit Ausprägung 1, 1 Element mit Ausprägung 2. Aktuell müssen exakt diese Größen getroffen werden. Eine Erwerterung, dass Bereiche erlaubt sind wäre hier einfach möglich.

Die Präferenzen der Elemente wird als `Map` definiert

```
		final Map<Node, List<Preference>> perferences = new HashMap<Node, List<Preference>>();
```
die für jede `Node` ein oder mehrere Präferenzen definiert. Eine Präferenz ist ein Paar bestehend aus einer anderen Node und einem Gewicht (das z.B. eine Priorität ausdrückt).

Das Programm gibt dann eine Tabelle mit den ersten 100 besten Aufteilungen aus.

License
-------

The code of package "com.christianfries.clustering" is distributed under the [Apache License version
2.0][], unless otherwise explicitly stated.
 
