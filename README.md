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


## Požadavky
- [ ] na konci semestru mít **kompletní single-user aplikaci** (připravenou k užití, bez účtů)
- [ ] ukládání, mazání, aktualizace, import, export **receptů**
- [ ] ukládání, mazání, aktualizace, import, export **ingrediencí**
- [ ] ukládání, mazání, aktualizace, import, export **vlastních jednotek**
- [ ] **základní jednotky** zavedeny defaultně (litr, ml, gram, kus, ...)
- [ ] možnost **kategorizace** receptů (každý recept maximálně **jednu** kategorii)
- [ ] **vyhledávání** s **filtrováním** (možnost dle více podmínek):
    podle nutriční hodnoty
    podle ingrediencí
    podle kategorií
    podle času na přípravu = od-do
    podle počtu porcí = od-do
- [ ] tlačítko **reset** pro filtrování a vyhledávání

#### Recepty
```
- název (unikátní)
- kategorie (max. 1)
- popis (krátký, např. "Tohle je můj koláč, který mi doporučila bábinka")
- potřebný čas
- počet porcí
- ingredience
- instrukce k přípravě
```

#### Ingredience
```
- název (unikátní)
- energetická hodnota  (stačí jedna jednotka, ale musí existovat možnost vzít hodnotu z obalu,
                        kde bývá např. na 90g a automaticky převést na 1g)
```

#### Základní jednotky
```
- název (unikátní)
- hodnota (je-li převoditelná do základu)
```

#### Uživatelské jednotky
```
- název (unikátní i s jednotkami)
- hodnota v nějaké základní jednotce
```

***

## Milníky
- [ ] 03.10. = **GUI design** (plně klikatelný prototyp bez navazane logiky)
- [ ] 24.10. = **Business logic, import, export** implemented
- [ ] 14.11. = **DB Operations** implemented
- [ ] 05.12. = **Non-blocking GUI**, completion of the project
