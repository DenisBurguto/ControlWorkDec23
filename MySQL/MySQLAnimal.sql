
/* В подключенном MySQL репозитории создать базу данных “Друзья человека” */
DROP DATABASE IF EXISTS human_friend;
CREATE DATABASE human_friend;
USE human_friend;


/* Создать таблицы с иерархией из диаграммы в БД
 Заполнить низкоуровневые таблицы именами(животных), командами которые они выполняют и датами рождения */


DROP TABLE IF EXISTS group_animal;
CREATE TABLE group_animal (
	id SERIAL PRIMARY KEY, 
    animal_group VARCHAR(50)
);

INSERT INTO group_animal (animal_group)
VALUES
      ('pet'),
      ('pack_animal');



DROP TABLE IF EXISTS class_animal;
CREATE TABLE class_animal (
	id SERIAL PRIMARY KEY, 
    animal_group_id BIGINT UNSIGNED NOT NULL,
    animal_class VARCHAR(50),
    FOREIGN KEY (animal_group_id) REFERENCES group_animal(id) ON UPDATE CASCADE ON DELETE CASCADE
);


INSERT INTO class_animal (animal_group_id,animal_class)
VALUES
	  (1, 'cat'),
      (1,'dog'),
      (1,'hamster'),
      (2,'horse'),
      (2,'camel'),
      (2,'donkey');

DROP TABLE IF EXISTS cat;
CREATE TABLE cat(
	id SERIAL PRIMARY KEY, 
    animal_class_id BIGINT UNSIGNED NOT NULL DEFAULT 1,
	animal_name VARCHAR(50),
    animal_birthdate DATE,
    animal_command VARCHAR(255),
    FOREIGN KEY (animal_class_id) REFERENCES class_animal(id) ON UPDATE CASCADE ON DELETE CASCADE
);



INSERT INTO cat (animal_name, animal_birthdate, animal_command)
VALUES
      ( 'pupsik', '2023-01-01', 'eat, sleep'),
      ( 'kotya', '2023-01-02', 'run, jump');
      
      
DROP TABLE IF EXISTS dog;
CREATE TABLE dog(
	id SERIAL PRIMARY KEY, 
    animal_class_id BIGINT UNSIGNED NOT NULL DEFAULT 2,
	animal_name VARCHAR(50),
    animal_birthdate DATE,
    animal_command VARCHAR(255),
     FOREIGN KEY (animal_class_id) REFERENCES class_animal(id) ON UPDATE CASCADE ON DELETE CASCADE
);



INSERT INTO dog (animal_name, animal_birthdate, animal_command)
VALUES
      ( 'reks', '2022-01-01', 'eat, come'),
      ( 'keks', '2022-01-02', 'bring me toalet paper, say "gav"');
      
      
DROP TABLE IF EXISTS hamster;
CREATE TABLE hamster(
	id SERIAL PRIMARY KEY, 
    animal_class_id BIGINT UNSIGNED NOT NULL DEFAULT 3,
	animal_name VARCHAR(50),
    animal_birthdate DATE,
    animal_command VARCHAR(255),
	FOREIGN KEY (animal_class_id) REFERENCES class_animal(id) ON UPDATE CASCADE ON DELETE CASCADE
);



INSERT INTO hamster (animal_name, animal_birthdate, animal_command)
VALUES
      ( 'mouse1', '2023-07-01', 'run'),
      ( 'mouse2', '2021-08-02', NULL);	

DROP TABLE IF EXISTS horse;
CREATE TABLE horse(
	id SERIAL PRIMARY KEY, 
    animal_class_id BIGINT UNSIGNED NOT NULL DEFAULT 4,
	animal_name VARCHAR(50),
    animal_birthdate DATE,
    animal_command VARCHAR(255),
	FOREIGN KEY (animal_class_id) REFERENCES class_animal(id) ON UPDATE CASCADE ON DELETE CASCADE
);



INSERT INTO horse (animal_name, animal_birthdate, animal_command)
VALUES
      ( 'lisa', '2020-07-01', 'go, stop'),
      ( 'mona', '2019-08-02', 'go, stop');	
      
DROP TABLE IF EXISTS camel;
CREATE TABLE camel(
	id SERIAL PRIMARY KEY, 
    animal_class_id BIGINT UNSIGNED NOT NULL DEFAULT 5,
	animal_name VARCHAR(50),
    animal_birthdate DATE,
    animal_command VARCHAR(255),
	FOREIGN KEY (animal_class_id) REFERENCES class_animal(id) ON UPDATE CASCADE ON DELETE CASCADE
);



INSERT INTO camel (animal_name, animal_birthdate, animal_command)
VALUES
      ( 'King of Sahara', '2023-07-01', 'sit, stand, go, stop'),
      ( 'Sheih', '2022-08-02', 'sit, stand, go, stop');	
      
DROP TABLE IF EXISTS donkey;
CREATE TABLE donkey(
	id SERIAL PRIMARY KEY, 
    animal_class_id BIGINT UNSIGNED NOT NULL DEFAULT 6,
	animal_name VARCHAR(50),
    animal_birthdate DATE,
    animal_command VARCHAR(255),
    FOREIGN KEY (animal_class_id) REFERENCES class_animal(id) ON UPDATE CASCADE ON DELETE CASCADE
);



INSERT INTO donkey (animal_name, animal_birthdate, animal_command)
VALUES
      ( 'ihak1', '2022-07-01', 'go, stop'),
      ( 'ihak2', '2021-08-02', 'go, stop');	
      
	
/* Удалив из таблицы верблюдов, т.к. верблюдов решили перевезти в другой питомник на зимовку.
Можно DROP TABLE camel; но это удалит саму таблицу. можно через DELETE FROM camel; но будет требовать отключения безопасного режима
удалим  class_animal  c id =5 отвечающий за верблюдов - каскадно очистятся зависимые записи
 */
 
DELETE FROM class_animal 
WHERE id = 5;

/*Объединить таблицы лошади, и ослы в одну таблицу.
*/

DROP TABLE IF EXISTS horse_donkey;
CREATE TABLE horse_donkey AS (SELECT animal_class_id, animal_name, animal_birthdate, animal_command FROM horse UNION SELECT animal_class_id, animal_name, animal_birthdate, animal_command FROM donkey);
ALTER TABLE  horse_donkey
ADD id SERIAL PRIMARY KEY;
DROP TABLE horse, donkey;

SELECT * FROM human_friend.horse_donkey;


/*Создать новую таблицу “молодые животные” в которую попадут все животные старше 1 года, но младше 3 лет и в отдельном 
столбце с точностью до месяца подсчитать возраст животных в новой таблице
В условии указано создать - трактую как CREATE , хотя разумнее возможно просто сделать выборку с JOIN из существующих таблиц без создания отдельной
*/

DROP TABLE IF EXISTS yang_animals;
CREATE TABLE yang_animals AS
( SELECT animal_class_id, animal_name, animal_birthdate, animal_command
    FROM cat
   WHERE TIMESTAMPDIFF(DAY, animal_birthdate, CURDATE()) BETWEEN 367 AND 1094 -- считаю старше года -значит на один день хотябы 
UNION
  SELECT animal_class_id, animal_name, animal_birthdate, animal_command
    FROM dog
   WHERE TIMESTAMPDIFF(DAY, animal_birthdate, CURDATE()) BETWEEN 367 AND 1094
UNION
  SELECT animal_class_id, animal_name, animal_birthdate, animal_command
    FROM hamster
   WHERE TIMESTAMPDIFF(DAY, animal_birthdate, CURDATE()) BETWEEN 367 AND 1094
UNION
  SELECT animal_class_id, animal_name, animal_birthdate, animal_command
    FROM horse_donkey
   WHERE TIMESTAMPDIFF(DAY, animal_birthdate, CURDATE())  BETWEEN 367 AND 1094);
ALTER TABLE  yang_animals
ADD id SERIAL PRIMARY KEY;
ALTER TABLE  yang_animals
ADD age_in_month INT;
UPDATE yang_animals
SET age_in_month = TIMESTAMPDIFF(MONTH, animal_birthdate, CURDATE())
WHERE id BETWEEN 1 AND 100; -- обновление без отключения безопасного режима

SELECT * FROM human_friend.yang_animals;

/* Объединить все таблицы в одну, при этом сохраняя поля, указывающие на прошлую принадлежность к старым таблицам.
тут не совсем ясно что значит все таблицы - будем считать что речь про  начальные первичные таблицы
*/

DROP TABLE IF EXISTS final_animal;
CREATE TABLE final_animal AS 
(SELECT animal_name, animal_birthdate, animal_command, animal_class, animal_group  
    FROM cat 
    JOIN class_animal c ON cat.animal_class_id = c.id
    JOIN group_animal g ON g.id = c.animal_group_id
UNION 
 SELECT animal_name, animal_birthdate, animal_command, animal_class, animal_group  
    FROM dog 
    JOIN class_animal c ON dog.animal_class_id = c.id
    JOIN group_animal g ON g.id = c.animal_group_id
UNION 
 SELECT animal_name, animal_birthdate, animal_command, animal_class, animal_group  
    FROM hamster 
    JOIN class_animal c ON hamster.animal_class_id = c.id
    JOIN group_animal g ON g.id = c.animal_group_id
UNION 
 SELECT animal_name, animal_birthdate, animal_command, animal_class, animal_group  
    FROM horse_donkey
    JOIN class_animal c ON horse_donkey.animal_class_id = c.id
    JOIN group_animal g ON g.id = c.animal_group_id);
ALTER TABLE  final_animal
ADD id SERIAL PRIMARY KEY;

SELECT * FROM human_friend.final_animal;



   
   

  
   



