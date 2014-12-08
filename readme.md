ReceiptScanner
==============

Cieľom projektu je detekcia dát z oskenovaných bločkov z obchodu. Predovšetkým je to názov obchodu a celková suma za nákup. 

Pomocou klasifikácie bločkov do skupín (napr. podľa loga) je možné pre rôzne skupiny prispôsobiť rozpoznanie textu.

Zdrojový kód, príklady a dokumentácia: [gitlab.fit.cvut.cz/zitnyjak/receiptscanner](https://gitlab.fit.cvut.cz/zitnyjak/receiptscanner/)

Spôsob riešenia
---------------

Klasický prístup k OCR ráta s výrazným predspracovaním a prečistením dát pred samotným rozpoznávaním znakov. Pri rôznorodých dokumentoch, ako sú bločky, je takmer nemožné minimalizovať chyby pri rozpoznávaní úplne, aj pri použití komplexných predspracovacích metód. Táto práca predkladá (a testuje) možnosť klasifikácie obrázkov pred ich rozpoznávaním. Na základe výrazných čŕt, akými sú napríklad logá firiem/obchodov sa oskenované bločky dajú klasifikovať do skupín. Jednotlivé skupiny potom môžu využiť manuálne naprogamované alebo automaticky naučené "custom" vylepšovania úspešnosti.

#### Predspracovanie
Pred detekovaním loga:

   - desaturácia
   - zvýšenie kontrastu
    
Pred rozpoznaním znakov:

   - rotácia
   - desaturácia
   - zvýšenie kontrastu
   - orezanie (TODO)

#### Detekcia loga
Detekcia loga je detekciou najväčšej/najvýraznejšej časti bločku. V praxi to pokrýva výrazné percento typov bločkov. Po predspracovaní sa rekurzívne prehľadajú všetky pixely a ich susedia, či spolu tvoria súvislé celky. Najväčšie z nich sa potom "spoja" a vystrihnú z pôvodného obrázku.

#### Klasifikácia
Na orezanom logu sa SURF algoritmom detekujú významné featury a porovnajú s featurami tréningového setu. Logu a teda aj aktuálnemu bločku bude pridelená skupina s najväčšou podobnosťou.

#### OCR
Potom ako program pozná do akej skupiny rozpoznávaný blok patrí, jednoduchšie detekuje z rozpoznaného textu podstatné údaje, napríklad celkovú cenu nákupu.

OCR knižnica Tesseract je kombinácia mnohých predspracovaní a algoritmov, ktoré klasifikujú pixely na písmená v rôznych písmach a jazykoch. Oproti iným konkurenčným open-source knižniciam alebo moderným algoritmom má výhodu vďaka mnohým detailom, pri detekcii riadkov, sklonu písma alebo používaní slovníkov.

Implementácia
---------------

ReceiptScanner je webová aplikácia postavená na Spring MVC frameworku.
Užívateľ môže na webe uploadnuť oscanovaný blok a v lepšom prípade na ňom aplikácia rozpozná nejaké dáta.

Použité knižnice a frameworky:

   - jOpenSurf - SURF detekcia featur, implementácia openSurf v Jave
   - Tesseract - open-source OCR
   - tess4j - java wrapper pre Tesseract
   - Spring framework - Webový fwk pre Javu SE
   - LightCouch - java wrapper pre komunikáciu s CouchDB
   - Gson - knižnica na prácu s formátom Json
   - Jade - extrémne coolový html šablónovací systém
   - jade4j - java implementácia Jade html šablón


Príklad výstupov, experimenty
---------------

1) Detekcia loga

![logos](https://gitlab.fit.cvut.cz/zitnyjak/receiptscanner/raw/master/src/main/webapp/static/img/logos.png)

2) Detekcia featur, podobnosť obrázkov 

![features](https://gitlab.fit.cvut.cz/zitnyjak/receiptscanner/raw/master/src/main/webapp/static/img/features.png)

3) OCR

![ocr](https://gitlab.fit.cvut.cz/zitnyjak/receiptscanner/raw/master/src/main/webapp/static/img/ocr.png)

4) WebApp

![webapp](https://gitlab.fit.cvut.cz/zitnyjak/receiptscanner/raw/master/src/main/webapp/static/img/webapp.png)

Diskusia
--------------

#### Vylepšenia

1. Pridať inteligentnejšie orezávanie bločkov odfotených na tmavom pozadí.

2. Trénovacie dáta ukladať do CouchDB napojené na ElasticSearch. Pomocou pluginov dokáže ES indexovať a dotazovať obrázky (elasticsearch-image plugin) a automaticky indexovať pridávané dokumenty do CouchDB (elasticsearch-river-couchdb). Na klasifikáciu obrázkov by sa vďaka tomu dali využiť komplexnejšie algoritmy z knižnice Lucene, na ktorej je ES postavený, ktoré sú nielen účinnejšie ale aj rýchlejšie.

3. Pri detekcii loga by sa dala zakomponovať ďalšia "subklasifikácia", či je vôbec daná časť obrázku logo alebo nie, podľa nejakých typických vlastnotí log alebo dokonca podľa online databáz existujúcich log. Pri detekcii výrazných častí bločkov, ktoré by neboli logami by sa dali použiť na klasifikáciu celé obrázky (nielen orezané časti s logom).

4. Automatické učenie detekovania zaujímavých dát na rozpoznanom texte, pomocou genetiky alebo iných vhodných ML algoritmov.

Záver
--------------