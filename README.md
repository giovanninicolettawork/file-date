# File Date

Questo software consente di ottenere la data di creazione di un file utilizzando diversi approcci. 
Nello specifico, dopo aver inserito in console il path del file da analizzare, la data sarà calcolata tramite:
- due funzioni della libreria *java.nio.file*:
	- `getAttribute`
	- `readAttributes`
- l'esecuzione del comando`stat` (questo risultato viene calcolato solo se il codice sta girando su sistema operativo Linux)

# Specifiche

Il codice è stato scritto in *Java (11)* utilizzando *Maven*. Nel file *pom.xml* è definita la modalità di build.

## Installazione e utilizzo
Il codice può essere eseguito utilizzando un IDE (es. su Eclipse basta eseguire *Runs As Java Application* sul file *App.java*).
È altresì possibile generare il file jar tramite il comando *Maven Install*. Verrà generato, nella cartella target, il file *filedate.jar* che potrà essere eseguito da terminale in ogni ambiente (con l'unico requisito che sia installato Java).
Per eseguire il jar lanciare sul terminale il comando

    java -jar filedate.jar

## Stato del progetto
Si tratta di un software implementato al solo scopo di acquisire confidenza con *Java*
