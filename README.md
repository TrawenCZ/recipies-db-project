# PV168 Project: Aplikace pro správu a užití databáze receptů

Jednoduchá funkční desktopová single-user aplikace pro práci s databází receptů a jejich kategorií. Aplikace je naprogramována v jazyce Java (JDK 17). Grafické uživatelské rozhraní běží na frameworku SWING. Automatizace buildů je řízena přes Apache Maven.

## Team Information

| Seminar Group | Team |
|-------------- | ---- |
| PV168/05      | 7    |

### Members

| Role           | Person               |
|----------------|----------------------|
|Team Lead       | [Marek Skácelík](https://is.muni.cz/auth/osoba/511762) |
|Member          | [Adam Slíva](https://is.muni.cz/auth/osoba/511768)     |
|Member          | [Jan Martinek](https://is.muni.cz/auth/osoba/484967)   |
|Member          | [Radim Stejskal](https://is.muni.cz/auth/osoba/514102) |

### Evaluators

| Role           | Person               |
|----------------|----------------------|
|Customer        | [Michael Koudela](https://is.muni.cz/auth/osoba/485441) |
|Technical Coach | [Jakub Smadiš](https://is.muni.cz/auth/osoba/445405)    |

***

## Milníky

- [ ] 03.10. = **GUI design** (plně klikatelný prototyp bez navazane logiky)
- [ ] 24.10. = **Business logic, import, export** implemented
- [ ] 14.11. = **DB Operations** implemented
- [ ] 05.12. = **Non-blocking GUI**, completion of the project

***

### 1.Meeting - Introduction

#### Požadavky na aplikaci

- [ ] na konci semestru mít **kompletní single-user aplikaci** (připravenou k užití, bez účtů)
- [ ] ukládání, mazání, aktualizace, import, export **receptů**
- [ ] ukládání, mazání, aktualizace, import, export **ingrediencí**
- [ ] ukládání, mazání, aktualizace, import, export **vlastních jednotek**
- [ ] **základní jednotky** zavedeny defaultně (litr, ml, gram, kus, ...)
- [x] možnost **kategorizace** receptů (každý recept maximálně **jednu** kategorii)
- [ ] **vyhledávání** s **filtrováním** (možnost dle více podmínek):
    podle nutriční hodnoty
    podle ingrediencí
    podle kategorií
    podle času na přípravu = od-do
    podle počtu porcí = od-do
- [ ] tlačítko **reset** pro filtrování a vyhledávání
- [ ] přiřazování barev kazdé kategorii

##### Recepty

- název (unikátní)
- kategorie (max. 1)
- popis (krátký, např. "Tohle je můj koláč, který mi doporučila bábinka")
- potřebný čas
- počet porcí
- ingredience
- instrukce k přípravě

##### Ingredience

- název (unikátní)
- energetická hodnota  (stačí jedna jednotka, ale musí existovat možnost vzít hodnotu z obalu, kde bývá např. na 90g a automaticky převést na 1g)

##### Základní jednotky

- název (unikátní)
- hodnota (je-li převoditelná do základu)

##### Uživatelské jednotky

- název (unikátní i s jednotkami)
- hodnota v nějaké základní jednotce

***

### 2.Meeting - GUI

#### Poznámky

##### Formulář k editaci/add receptů

- [ ] přeuspořádat (jméno nahoru, porce k trvání, dolní tlačítka blíže k sobě na střed, zarovnat texty)
- [ ] opravit layout ingrediencí (skáče při 1-3 prvcích, není úplně zarovnané)
- [ ] přidat popis(název) sloupců ingrediencí
- [ ] zvětšit nebo povolit resize+minSize
- [ ] přenastavit defaultní pozici, aby se nemusel při otevření posouvat
- [ ] povolit null kategorii

##### Formulář k editaci/add jednotek

- [ ] přidat ke které základní jednotce nová patří (combo box)
- [ ] zvětšit a zarovnat (see poznámky k formuláři receptů)

##### Formulář k editaci/add kategorií

- [ ] přidat color picker
- [ ] zvětšit a zarovnat (see poznámky k formuláři receptů)

##### Formulář k editaci/add ingrediencí

- [ ] u custom energy value přidat zjasnit popisky (tzn. že zakládní je na 1 základní jednotku, zatímco tohle se dá navolit) = uvážit existenci ostatních základních jednotek

##### Detaily receptů

- [ ] zvětšit
- [ ] hlavní nadpis více odsatit zvrchu i zespod
- [ ] přidat ingredience

##### Tab receptů

- [ ] zobrazit jednotku u required time (buď přímo v headeru, nebo v každém záznamu)
- [ ] přeuspořádat horní panel (
    zmenšit šírku lupy a resetu,
    přesunout tlačítka filter categories a ingredients - zavádějící v relaci s rangeTextFieldy,
    větší mezery mezi položkami)
- [ ] přidat popis k tlačítkům (lupa, reset)
- [ ] zvážit přídání tlačítka APPLY FILTERS (nebo zapracování tohoto konceptu do lupy) místo automatického filtrování po změně hodnoty filtrovacích polí

##### Tab jednotek

- [ ] přidat sloupec "relative to" (může být null pro základní jednotky)
- [ ] základní jednotky zvýraznit barevně

##### Tab kategorií

- [ ] přidat sloupec s barvami, asi lepší zabarvit jen tento než celou kategorii (možná s hex hodnotou barvy)

##### Tab ingrediencí

- [ ] přidat spojitost s nějakou jednotkou pro přepočet na množství
- [ ] ohlídat si převody / zavedené ingredience
- [ ] jméno stále unikátní = pouze jeden typ převodu povolen

##### Hlavní window

- [ ] nastavit minimální velikost (tzn. takovou při které layout ještě funguje)

#### Požadavky na další milestone

- opravit zmíněné věci
- implementovat základní bussiness logiku (tlačítka by měla dělat to, k čemu jsou určena, ale zatím bez napojení na databázi)
- import + export (stačí JSON formát, nevalidní data chyba + žádné změny)
- kontrola duplicit (jak v přidávání tak v importu = jde spojit)

***

### 3.Meeting - Bussiness logic

***

### 4.Meeting - DB operations

***

### 5.Meeting - Non-blocking GUI