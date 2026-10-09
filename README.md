# BreedGlow (Fabric, Minecraft 1.21.11)

Tiere leuchten **gruen**, wenn sie zuechtbar sind, und **rot**, wenn nicht.
Mit **G** (aenderbar unter Steuerung > BreedGlow) schaltest du die Mod an und aus.

## Jar bauen

Voraussetzung: Java 21 und Gradle 8.14 oder neuer.

    gradle build

Die fertige Datei liegt dann in `build/libs/breedglow-1.0.0.jar`.
Diese Datei kommt in deinen `mods`-Ordner. Du brauchst ausserdem Fabric Loader und die Fabric API.

Ohne lokale Installation: Projekt auf GitHub hochladen, dann baut `.github/workflows/build.yml`
die Jar unter Actions > build > Artifacts.

## Wichtig

- Im **Einzelspielermodus** stimmen die Farben exakt (Abklingzeit und Liebesmodus werden beruecksichtigt).
- Auf **fremden Servern** kennt der Client die Abklingzeit nicht: dort sind Erwachsene gruen und Babys rot.
- Die Mod beruecksichtigt alle Tiere (`Animal`), also Kuehe, Schafe, Schweine, Huehner, Pferde, Woelfe usw.
