--
-- Unit table definition
--
CREATE TABLE IF NOT EXISTS Unit
(
    `id`        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `name`      VARCHAR(100) NOT NULL UNIQUE,
    `value`     DOUBLE PRECISION NOT NULL,
    `baseUnitId` BIGINT NOT NULL,
    FOREIGN KEY (`baseUnitId`) REFERENCES Unit(`id`)
);

--
-- Ingredient table definition
--
CREATE TABLE IF NOT EXISTS Ingredient
(
    `id`           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `name`         VARCHAR(100) NOT NULL UNIQUE,
    `kcalPerUnit`  DOUBLE PRECISION NOT NULL,
    `baseUnitId`   BIGINT NOT NULL,
    FOREIGN KEY (`baseUnitId`) REFERENCES Unit(`id`)
);

--
-- Category table definition
--
CREATE TABLE IF NOT EXISTS Category
(
    `id`           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `name`         VARCHAR(100) NOT NULL UNIQUE,
    `color`        VARCHAR(20) NOT NULL -- not really sure how to color is saved
);

--
-- Recipe table definition
--
CREATE TABLE IF NOT EXISTS Recipe
(
    `id`           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `name`         VARCHAR(100) NOT NULL UNIQUE,
    `description`  VARCHAR(256) NOT NULL UNIQUE,
    `categoryId`   BIGINT, -- can be null !
    `portions`     BIGINT NOT NULL,
    `duration`     BIGINT NOT NULL,
    `instruction`  VARCHAR(512) NOT NULL,
    FOREIGN KEY (`categoryId`) REFERENCES Category(`id`)
);

CREATE TABLE IF NOT EXISTS IngredientList
(
    `id`           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    --
    `recipeId`     BIGINT NOT NULL, -- both of these attributes are not primary keys (blokové schéma)
    `ingredientId` BIGINT NOT NULL, -- so that means that they can more than one duplicates of these keys (double)
                                    -- if you wish to make them unique (double), delete `id` atr and make a double primary key
    --
    `amount`       BIGINT NOT NULL,
    `unitId`       BIGINT NOT NULL,
    FOREIGN KEY (`recipeId`) REFERENCES Recipe(`id`),
    FOREIGN KEY (`ingredientId`) REFERENCES Ingredient(`id`),
    FOREIGN KEY (`unitId`) REFERENCES Unit(`id`)
);

