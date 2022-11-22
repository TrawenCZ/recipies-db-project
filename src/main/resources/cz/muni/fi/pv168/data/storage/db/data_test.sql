-- #########################
-- @author Marek Skácelík
-- FILE: TEST DATA DUMP
-- LAST UPDATE: 22-11-2022
-- #########################
-- CATEGORIES:
-- #########################
INSERT INTO Category(`name`, `color`) VALUES
('polívky', 'FFCFBD00'),
('pečivo', 'FFCF6F08'),
('sladkosti', 'FFCF047A'),
('vegan', 'FF287800'),
('pití', 'FF1C37FF'),
('italské jídla', 'FF37FF00'),
('indické jídla', 'FFBD3F00'),
('mexické jídla', 'FF630A00'),
('jídla pro bodybuildery', 'FF7094CF'),
('pokrm', 'FF663300');
-- #########################
-- UNITS:
-- #########################
SET @gramId = (SELECT `id` FROM Unit WHERE `name`='g');
SET @milliliterId = (SELECT `id` FROM Unit WHERE `name`='ml');
SET @pieceId = (SELECT `id` FROM Unit WHERE `name`='pc(s)');

INSERT INTO Unit(`name`, `amount`, `baseUnitId`) VALUES
('dkg', 10.0, @gramId),
('kg', 1000.0, @gramId),
('tuna', 1000000.0, @gramId),
('cl', 10.0, @milliliterId),
('dl', 100.0, @milliliterId),
('l', 1000.0, @milliliterId),
('tucet', 12.0, @pieceId);
-- #########################
-- INGREDIENTS:
-- #########################
INSERT INTO Ingredient(`name`, `kcalPerUnit`, `baseUnitId`) VALUES
('sůl', 3.0, @gramId),
('česnek', 130.0, @gramId),
('cibule', 43.0, @gramId),
('prášek na pečení', 70.0, @gramId),
('tofu', 145.0, @gramId),
('máslo', 748.0, @gramId),
('sladká chilli omáčka', 230.0, @milliliterId),
('cuketa', 22.0, @gramId),
('hořká čokoláda', 592.0, @gramId),
('karamel', 245.0, @gramId),
('pórek', 44.0, @gramId),
('meloun kantalupe', 33.0, @gramId),
('fazole cannellini', 93.0, @gramId),
('jahody', 34.0, @gramId),
('maliny', 73.0, @gramId),
('červené víno', 80.0, @milliliterId),
('kukuřičný sirup', 314.0, @milliliterId),
('kukuřičné lupínky', 357.0, @gramId),
('tortilla', 219.0, @gramId),
('brambory', 76.0, @gramId),
('hrášek', 95.0, @gramId),
('houba portabello', 35.0, @gramId),
('vejce', 151.0, @pieceId);
-- #########################
-- RECIPES
-- #########################
INSERT INTO Recipe(`name`, `description`, `categoryId`, `portions`, `duration`, `instruction`) VALUES
('Špagety s kuřecím masem', 'Pod slovem špagety se nám vybaví těstoviny s rajčatovou omáčkou, bohatě posypané sýrem? Ale špagety se dají připravit dalšími nápaditými způsoby. Podle receptu je vyzkoušíme s kuřecím masem, doplněné rajčaty, paprikou a provensálským kořením.', (SELECT `id` FROM Category WHERE `name`='italské jídla'), 5, 40,'1.) Na oleji osmažíme na drobno nakrájenou cibuli, vložíme kuřecí maso nakrájené na nudličky, osolíme, opepříme.
2.) K osmaženému masu přidáme nakrájenou zeleninu, koření a protlak.
3.) Podlijeme cca 2 dl vody a omáčku provaříme do změknutí masa, podle potřeby dochutíme.
4.) Mezitím uvaříme špagety, podáváme sypané sýrem.'),
('Babiččina kmínová polévka', 'Naše babičky dokázaly vykouzlit výbornou polévku i z několika málo surovin, které našly ve spíži. Stačí na kousku sádla opražit trochu mouky a kmínu, zalít vodou a nakonec přidat rozšlehané vejce. Pro vylepšení můžeme přidat brambory a kořenovou zeleninu.', (SELECT `id` FROM Category WHERE `name`='polívky'), 1, 25,'1.) Na sádle opražíme kmín a lžíci mouky. Pak vmícháme papriku (to kvůli barvě), jen kratince orestujeme, aby nezhořkla.
2.) Zalijeme vodou, přidáme vegetu a povaříme cca 10 minut.
3.) Nakonec vmícháme rozšlehané vejce. Pro obměnu můžeme přidat petrželku, popřípadě trochu nastrouhané zeleniny (mrkev, celer, petržel, ap.).'),
('Játrová paštika domácí','Játrová paštika namazaná na plátku čerstvého chleba je velmi oblíbená svačinka na rodinné výlety i během klidného víkendu. Připravte si jednoduchou a jemnou paštiku podle našeho receptu a překvapte své blízké.', (SELECT `id` FROM Category WHERE `name`='pokrm'), 1, 210,'1.) Bůček rozkrojíme na menší kusy, prosolíme a pečeme 1 hodinu při 200 °C s cibulí rozkrojenou na čvtrtky.
2.) Necháme zchladnout a umeleme na masovém strojku.
3.) Přidáme 1 kg pomletých syrových jater.
4.) Ochutíme novým kořením, pepřem, paštikovým kořením, promícháme a plníme do 3/4 do připravených sklenic.
5.) Uzavřeme a sterilujeme při 95 °C 2 hodiny.'),
('Kuřecí stehna pečená s rýží z jednoho pekáčku','Jídla z jednoho pekáčku jsou hodně oblíbená ve dnech, kdy nemáme moc času pobývat v kuchyni, a přesto potřebujeme nasytit rodinu výživně a chutně. Recept na kuřecí stehna s rýží z jednoho pekáčku je praktický a určitě ho budete chtít zopakovat.', (SELECT `id` FROM Category WHERE `name`='jídla pro bodybuildery'), 2, 70, '1.) Cibuli nakrájíme na měsíčky.
2.) Omytá stehna položíme na cibuli a česnek do pekáčku.
3.) Okořeníme a nahoru položíme plátek másla. Podlijeme trochou vody a vložíme na 20 minut do trouby vyhřáté na 200 °C. Pečeme přikryté.
4.) Mezitím na oleji orestujeme rýži do sklovata. Přidáme všechnu zeleninu nakrájenou na kousky, ochutíme a zalijeme vodou.
5.) Z pekáčku vytáhneme stehna a vlijeme rýži se zeleninou.
6.) Nahoru položíme stehna a přikryté pečeme dalších přibližně 25-30 minut do změknutí rýže a masa. Voda se vsákne do rýže a spojí se chuť masa a rýže.
7.) Před koncem odkryjeme, aby zezlátla kůžička. Řiďte se zkušeností se svou troubou.'),
('Valašská kyselica ze Vsetína','Hospodyňky ze Vsetína vědí, jak připravit opravdu skvostnou valašskou kyselicu. Vydatná polévka z kysaného zelí, brambor a uzeniny vás nepochybně zahřeje i zasytí, což oceníte zejména v zimě.', (SELECT `id` FROM Category WHERE `name`='polívky'), 6, 60,'1.) Ještě před vařením zelí prolijeme vodou, aby nebylo tolik kyselé.
2.) Do vyššího hrnce dáme vařit kysané zelí s nadrobno nakrájenou cibulí, osolíme, opepříme (může být i celý pepř), dále přidáme nové koření, bobkový list a ještě nakrájené klobásky a vaříme.
3.) Do vedlejšího hrnce dáme vařit nakrájené brambory na kostičky (přidáme sůl a kmín drcený).
4.) Až je zelí měkčí, přidáme brambory (jak se slijí, tak v nich necháme trochu vody z brambor navíc pro lepší chuť).
5.) Nakonec přidáme šlehačku s rozmíchanou moukou na zahuštění.
6.) To vlijeme do hrnce, přisypeme majoránku a ještě chvíli vaříme, aby se provařila mouka.
7.) Nakonec přidáme prolisovaný česnek.');
-- #########################
-- INGREDIENT LIST(S)
-- #########################
SET @spagetyId = (SELECT `id` FROM Recipe WHERE `name`='Špagety s kuřecím masem');
SET @kminovaPolivkaId = (SELECT `id` FROM Recipe WHERE `name`='Babiččina kmínová polévka');
SET @pastikaId = (SELECT `id` FROM Recipe WHERE `name`='Játrová paštika domácí');
SET @stehnaId = (SELECT `id` FROM Recipe WHERE `name`='Kuřecí stehna pečená s rýží z jednoho pekáčku');
SET @kyselicaId = (SELECT `id` FROM Recipe WHERE `name`='Valašská kyselica ze Vsetína');

INSERT INTO IngredientList(`recipeId`, `ingredientId`, `amount`, `unitId`) VALUES
(@spagetyId, (SELECT `id` FROM Ingredient WHERE `name` = 'cibule'), 1.5, (SELECT `id` FROM Unit WHERE `name` = 'kg')),
(@spagetyId, (SELECT `id` FROM Ingredient WHERE `name` = 'červené víno'), 4.9, (SELECT `id` FROM Unit WHERE `name` = 'dl')),
(@spagetyId, (SELECT `id` FROM Ingredient WHERE `name` = 'česnek'), 25.5, @gramId),
(@spagetyId, (SELECT `id` FROM Ingredient WHERE `name` = 'cibule'), 0.8, (SELECT `id` FROM Unit WHERE `name` = 'tuna')),
(@kminovaPolivkaId, (SELECT `id` FROM Ingredient WHERE `name` = 'červené víno'), 0.5, (SELECT `id` FROM Unit WHERE `name` = 'l')),
(@kminovaPolivkaId, (SELECT `id` FROM Ingredient WHERE `name` = 'vejce'), 1, @pieceId),
(@kminovaPolivkaId, (SELECT `id` FROM Ingredient WHERE `name` = 'maliny'), 9, (SELECT `id` FROM Unit WHERE `name` = 'dkg')),
(@pastikaId, (SELECT `id` FROM Ingredient WHERE `name` = 'červené víno'), 0.5, (SELECT `id` FROM Unit WHERE `name` = 'l')),
(@pastikaId, (SELECT `id` FROM Ingredient WHERE `name` = 'vejce'), 1, @pieceId),
(@stehnaId, (SELECT `id` FROM Ingredient WHERE `name` = 'pórek'), 3.5, (SELECT `id` FROM Unit WHERE `name` = 'kg')),
(@stehnaId, (SELECT `id` FROM Ingredient WHERE `name` = 'brambory'), 10, (SELECT `id` FROM Unit WHERE `name` = 'kg')),
(@kyselicaId, (SELECT `id` FROM Ingredient WHERE `name` = 'vejce'), 4, @pieceId),
(@kyselicaId, (SELECT `id` FROM Ingredient WHERE `name` = 'pórek'), 2.5, (SELECT `id` FROM Unit WHERE `name` = 'dkg')),
(@kyselicaId, (SELECT `id` FROM Ingredient WHERE `name` = 'brambory'), 11, (SELECT `id` FROM Unit WHERE `name` = 'tuna'));