# JumpingSumoProt

Dieser Prototyp verbindet sich aktuell statisch zu einem JumpingSumo, d.h. 192.168.2.1 Port 44444.

Beinhaltet die folgenden 4 Modi:

```java -jar jumpingSumo-jar-with-dependencies.jar <keyboard|program|file|swing>```

1. Keyboard-Driven (KeyboardDriver)
  - Pfeiltasten: Vorwärts, Rückwärts, Links, Rechts
  - Springen: 
	  - (H) Hoch
	  - (W) Weit
  - Animationen: 
	  - (1) Drehen
	  - (2) Tippen
	  - (3) Schütteln
	  - (4) Metronome
	  - (5) Ondulation
	  - (6) Drehsprung
	  - (8) Spirale
	  - (9) Slalom
  - Sound:
      - (Y) Laut
      - (X) Ohne Sound
      - (I) Monster Theme
      - (O) Insect Theme
      - (P) Robot Theme

2. Java-Driven (ProgrammaticDriver)
  
3. Datei-Driven (FileBasedProgrammaticDriver)

```programm.txt``` wird nach Command-Änderung gepollt, die direkt auf dem JumpingSumo weitergeleitet werden
  - Vor: Fährt eine Einheit nach vorne
  - Zurueck: Fährt eine Einheit nach hinten
  - Links: Linksdrehung 90°
  - Links x: Linksdrehung um x Grad (Hinweis: 90° entspricht 25)
  - Rechts x: Rechtsdrehung um x Grad (Hinweis: 90° entspricht 25)
  - Springe hoch: Führt einen Hochsprung aus
  - Springe weit: Führt einen Weitsprung aus
