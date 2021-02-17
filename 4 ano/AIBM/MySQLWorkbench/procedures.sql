use urgency ;

 SET @last_ep = (SELECT COALESCE(MAX(U_episode), 0)  FROM Dim_Entry_Patient);
 SET @last_date = (SELECT COALESCE(MAX(id_date), -1)  FROM Dim_Date);


-- PROCEDURE - Trata dos valores nulos do Data Wearhouse.
DELIMITER $$
DROP PROCEDURE IF EXISTS handling_null_values;$$
CREATE PROCEDURE handling_null_values()
BEGIN
	START TRANSACTION;
		UPDATE Dim_Discharge SET id_prof_discharge = -1  WHERE id_prof_discharge IS NULL;
		UPDATE Dim_notes SET id_prof_cancel = -1 WHERE (note_cancel = "" and id_prof_cancel is null);
	COMMIT;
END $$


-- PROCEDURE - Insere uma entrada de um paciente.
DELIMITER $$
DROP PROCEDURE IF EXISTS ins_Entry_Patient;$$
CREATE PROCEDURE ins_Entry_Patient(sex tinyint, birthday_date datetime)
BEGIN
	DECLARE u_ep INT DEFAULT 0;
	START TRANSACTION;
		INSERT INTO Dim_Entry_Patient (U_episode,sex,date_birthday) VALUES (@last_ep + 1,sex,ins_date(birthday_date));
	COMMIT;
END $$


-- FUNCTION - Insere um entrada na triagem. ASSUME -se que são sempre diferentes devido á data ir ao segundo.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Triage;$$
CREATE FUNCTION ins_Triage(id_pt INT, painS INT,id_color INT,triage_date DATETIME )  RETURNS INT 
BEGIN
	INSERT INTO Dim_Triage (id_prof_triage,pain_scale,id_color,date_triage) VALUES (id_pt,painS,id_color,ins_date(triage_date));
	return  LAST_INSERT_ID();-- (SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'Dim_Triage' AND table_schema = DATABASE( ));
END $$

-- FUNCTION - Verifica se um nivel  existe na tabela Dim_Hierarchy_Diagnosis.
SET GLOBAL log_bin_trust_function_creators = 1;
DELIMITER $$
DROP FUNCTION IF EXISTS exist_level $$
CREATE FUNCTION exist_level(lvl VARCHAR(10)) RETURNS TINYINT 
BEGIN 
	DECLARE var TINYINT  DEFAULT false;
	IF( (SELECT  COUNT(id_level) FROM  Dim_Hierarchy_Diagnosis where id_level LIKE lvl) = 1 ) THEN 
		SET var=true;
	END IF;
	return var;
END $$


-- FUNCTION - INSERE UMA DATA SE NÂO EXISTE, e retorna o id da mesma.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_date $$
CREATE FUNCTION ins_date(date1 DATETIME) RETURNS INT 
BEGIN 
	DECLARE id_d INT DEFAULT 0;
    SET id_d = (SELECT id_date FROM Dim_Date as d WHERE d.date = date1);
	IF (id_d is NULL)  THEN 
		SET id_d =@last_date + 1;
		INSERT INTO Dim_Date  VALUES (id_d,date1);
	END IF;
	return id_d;
END $$

-- PROCEDURE - Insere um diagnostico. Assume-se que são sempre diferentes devido á data ir ao segundo.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Diagnosis;$$
CREATE FUNCTION ins_Diagnosis(note_dia TEXT,date_dia DATETIME,id_prof_dia INT,id_level  VARCHAR(10)) RETURNS INT
BEGIN
		IF ( exist_level(id_level)) THEN
			INSERT INTO Dim_Diagnosis (note_diagnosis,date_diagnosis,id_prof_diagnosis,id_level)
			VALUES (note_dia,ins_date(date_dia),id_prof_dia,id_level);
			return  LAST_INSERT_ID();-- (SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'Dim_Diagnosis' AND table_schema = DATABASE( ));
		END IF;
        return -1;
END $$





-- PROCEDURE - Insere um Exame.
DELIMITER $$
DROP PROCEDURE IF EXISTS ins_Exam;$$
CREATE PROCEDURE ins_Exam(U_ep INT,id_Exam VARCHAR(60) ,desc_Exam VARCHAR(150))
BEGIN
	START TRANSACTION;
		IF EXISTS ( SELECT U_episode FROM Dim_Entry_Patient where U_episode = U_ep ) THEN
			INSERT INTO Dim_Exams (U_episode,id_exam,desc_exam) VALUES (U_ep,id_Exam,desc_Exam);
		END IF;
	COMMIT;
END $$

-- FUNCTION - Insere um distrito.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_District;$$
CREATE FUNCTION ins_District(name1 VARCHAR(50)) RETURNS INT
BEGIN
	DECLARE id_max INT DEFAULT 0;
	Set id_max = (SELECT id_district FROM Dim_District as d where d.district=name1);
    IF (id_max is NULL) THEN
		INSERT INTO Dim_District (district) VALUES (name1);
        RETURN   LAST_INSERT_ID(); -- (SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'Dim_District' AND table_schema = DATABASE( ));
    END IF;
    RETURN id_max;
END $$


-- FUNCTION - Insere uma saida do hospital.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Discharge;$$
CREATE FUNCTION ins_Discharge(id_prof_dis INT, dt DATETIME) RETURNS INT
BEGIN
	INSERT INTO Dim_Discharge (id_prof_discharge,date_discharge) VALUES (id_prof_dis,ins_date(dt));
    RETURN  LAST_INSERT_ID();-- (SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'Dim_Discharge' AND table_schema = DATABASE( ))-1;
END $$


-- FUNCTION - Inserir uma admição no hospital.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Admition;$$
CREATE FUNCTION ins_Admition(id_prof_ad INT, dt DATETIME) RETURNS INT
BEGIN
	INSERT INTO Dim_Admition (id_prof_admition,date_admition) VALUES (id_prof_ad,ins_date(dt));
    RETURN LAST_INSERT_ID(); --  (SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_name = 'Dim_Admition' AND table_schema = DATABASE( )) -1;
END $$


-- PROCEDURE - Insere um destino do doente dentro do hospital 
DELIMITER $$
DROP PROCEDURE IF EXISTS ins_Destination;$$
CREATE PROCEDURE ins_Destination(id_dest INT ,dest VARCHAR(50))
BEGIN
	INSERT INTO Dim_Destination (id_destination,desc_destination) VALUES (id_dest,dest);
END $$

-- FUNCTION - Insere um destino do doente dentro do hospital(só com descrição) 
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Destination2;$$
CREATE FUNCTION ins_Destination2(dest VARCHAR(50)) RETURNS INT
BEGIN
	DECLARE id INT DEFAULT 0;
	Set id = (SELECT id_destination FROM Dim_Destination as d where d.desc_destination=dest);
    IF (id is NULL) THEN
		SELECT MAX(id_destination)+1 INTO id  FROM  Dim_Destination;
		INSERT INTO Dim_Destination (id_destination,desc_destination) VALUES (id,dest);
		return  id;
	END IF;
    return id;
END $$

-- PROCEDURE - Insere a causa do doente estar do hospital 
DELIMITER $$
DROP PROCEDURE IF EXISTS ins_Cause;$$
CREATE PROCEDURE ins_Cause(id_c INT ,cause VARCHAR(100))
BEGIN
	INSERT INTO Dim_Cause (id_cause,desc_cause) VALUES (id_c,cause);
END $$

-- FUNCTION - Insere a causa do doente estar do hospital(so recebe a causa)
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Cause2;$$
CREATE FUNCTION ins_Cause2(cause VARCHAR(100))RETURNS INT
BEGIN
	DECLARE id_max INT DEFAULT 0;
	Set id_max = (SELECT id_cause FROM Dim_Cause as d where d.desc_cause=cause);
    IF (id_max is NULL) THEN
		SELECT MAX(id_cause)+1 INTO id_max  FROM  Dim_Cause;
		INSERT INTO Dim_Cause (id_cause,desc_cause) VALUES (id_max,cause);
        return id_max;
	END IF;
	return id_max;
END $$

-- PROCEDURE - Insere uma razão pela qual teve alta.
DELIMITER $$
DROP PROCEDURE IF EXISTS ins_Reason;$$
CREATE PROCEDURE ins_Reason(id_r INT ,reason VARCHAR(50))
BEGIN
	INSERT INTO Dim_reason (id_reason,desc_reason) VALUES (id_r,reason);
END $$

-- FUNCTION - Insere uma razão pela qual teve alta. (São especies inserts e gets, por isso é que tem de ser funções.)
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Reason2;$$
CREATE FUNCTION ins_Reason2(reason VARCHAR(50)) RETURNS INT
BEGIN
	DECLARE id_max INT DEFAULT 0;
	Set id_max = (SELECT id_reason FROM Dim_Reason as d where d.desc_reason=reason);
    IF (id_max is NULL) THEN
		SELECT MAX(id_reason)+1 INTO id_max  FROM  Dim_reason;
		INSERT INTO Dim_reason (id_reason,desc_reason) VALUES (id_max,reason);
        return id_max;
	END IF;
    return id_max;
END $$

-- --------------------------
-- Facts table->Uepisode   --
-- --------------------------

-- PROCEDURE - Insere um facto na tabela de episódios
DELIMITER $$
DROP PROCEDURE IF EXISTS ins_Fact_Uepisode;$$
CREATE PROCEDURE ins_Fact_Uepisode
(ep INT ,district VARCHAR(50), cause VARCHAR(100),triage_prof INT,triage_pain INT,triage_color INT,triage_date DATETIME,
dia_note TEXT,dia_date DATETIME,dia_prof INT, dia_lvl  VARCHAR(10), 
dest VARCHAR(50),
discharge_prof INT,discharge_date DATETIME ,
admition_prof INT, admition_date DATETIME ,reason VARCHAR(50))
BEGIN
	START TRANSACTION;
		INSERT INTO Fact_UEpisodes (U_episode,id_district,id_cause,id_triage,id_diagnosis,id_destination,id_discharge,id_admition,id_reason)
         VALUES   (ep,ins_District(district),ins_Cause2(cause),ins_Triage(triage_prof,triage_pain,triage_color,triage_date),ins_Diagnosis(dia_note, dia_date, dia_prof,dia_lvl),ins_Destination2(dest),ins_Discharge(discharge_prof,discharge_date), ins_Admition(admition_prof,admition_date),ins_Reason2(reason));
    COMMIT;
    
END $$

-- PROCEDURE - Insere um facto na tabela de episódios, porem recebe os identificadores.
DELIMITER $$
DROP PROCEDURE IF EXISTS ins_Fact_Uepisode2;$$
CREATE PROCEDURE ins_Fact_Uepisode2(ep INT ,district INT, cause INT,triage INT,dia INT,dest INT,discharge INT,admition INT,reason INT)
BEGIN
	INSERT INTO Fact_UEpisodes (U_episode,id_district,id_cause,id_triage,id_diagnosis,id_destination,id_discharge,id_admition,id_reason)
    VALUES (ep,district, cause,triage,dia,dest,discharge,admition,reason);
END $$


-- --------------------------
--  Parte de baixo         --
-- --------------------------



-- FUNCTION - Insere uma posologia para um medicamento.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Posology;$$
CREATE FUNCTION ins_Posology(desc_p VARCHAR(400)) RETURNS INT
BEGIN
	DECLARE id INT DEFAULT 0;
	Set id = (SELECT id_posology FROM Dim_Posology as p where p.desc_posology = desc_p );
    IF (id is NULL) THEN
		INSERT INTO Dim_Posology (desc_posology) VALUES (desc_p);
		RETURN LAST_INSERT_ID(); 
	END IF;
    RETURN id;
END $$

-- FUNCTION - Insere um medicamento.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Drug;$$
CREATE FUNCTION ins_Drug(cod INT, pvp INT, comp INT, desc_d VARCHAR(300))  RETURNS INT
BEGIN
	DECLARE id INT DEFAULT 0;
	Set id = (SELECT id_Drug FROM Dim_Drug as d where d.cod_drug =cod AND d.pvp=pvp AND d.comparticipation=comp AND d.desc_drug=desc_d );
    IF (id is NULL) THEN
		INSERT INTO Dim_Drug (cod_drug,pvp,comparticipation,desc_drug) VALUES (cod, pvp, comp, desc_d);
		RETURN LAST_INSERT_ID(); 
    END IF;
    RETURN id;
END $$

-- FUNCTION - Insere um prescrição.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_UPrescription;$$
CREATE FUNCTION ins_UPrescription(u_ep INT, cod INT, id_p INT, date_u DATETIME) RETURNS INT
BEGIN
		INSERT INTO Dim_UPrescription (U_episode,cod_prescription,id_prof_prescription,date_urgency_prescription) 
        VALUES (u_ep, cod, id_p, ins_date(date_u));
        RETURN cod;
END $$

-- FUNCTION - Associa uma prescrição a um medicamento.
DELIMITER $$
DROP PROCEDURE IF EXISTS ins_Prescription_Has_Drug;$$
CREATE PROCEDURE ins_Prescription_Has_Drug(cod INT, id_drug INT, quant INT, posology VARCHAR(400))
BEGIN
	DECLARE id_d INT DEFAULT 0;
	START TRANSACTION;
	Set id_d = (SELECT id_Drug FROM Dim_Drug as d where d.id_drug = id_drug );
		IF NOT (id_d is NULL) THEN
			INSERT INTO Prescription_Has_Drug (cod_prescription,id_drug,quantity,id_posology)
			VALUES (cod,id_drug,quant,ins_Posology(posology));
		END IF;
	COMMIT;
END $$


-- ----------------
-- PROCEDURES necessarios para povoar a tabela FAct_Uprocedure.
-- ----------------
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Intervention;$$
CREATE FUNCTION ins_Intervention(id INT, desc_int VARCHAR(200)) RETURNS INT
BEGIN
	DECLARE id_i INT DEFAULT 0;
	Set id_i = (SELECT id_intervention FROM Dim_Intervention where  (id_intervention = id OR desc_intervention = desc_int ));
	IF (id_i is NULL) THEN
		INSERT INTO Dim_Intervention (id_intervention,desc_intervention) VALUES (id, desc_int);
		RETURN id;
	END IF;
	RETURN id_i;
END $$


-- FUNCTION - Insere uma nota.
DELIMITER $$
DROP FUNCTION IF EXISTS ins_Note;$$
CREATE FUNCTION ins_Note(note VARCHAR(550), note_c VARCHAR(550), id_p INT) RETURNS INT
BEGIN
	DECLARE id INT DEFAULT 0;
	Set id = (SELECT id_notes FROM Dim_Notes as d where  d.note=note AND d.note_cancel=note_c AND d.id_prof_cancel=id_p);
    IF (id is NULL) THEN
		INSERT INTO Dim_Notes (note,note_cancel,id_prof_cancel) VALUES (note, note_c, id_p);
		RETURN LAST_INSERT_ID(); 
	END IF;
    RETURN id;
END $$




-- FUNCTION - Insere um procedure na tabela  de factos dos procedimentos.
DELIMITER $$
DROP PROCEDURE IF EXISTS ins_UProcedure;$$
CREATE PROCEDURE ins_UProcedure(u_ep INT, id_pro INT, date_begin DATETIME, 
note VARCHAR(550), note_cancel VARCHAR(550), id_prof_c INT, 
date_cancel DATETIME, id_i INT,Desc_Inter VARCHAR(200), date_cli DATETIME, id_pres INT)
BEGIN
	START TRANSACTION;
		INSERT INTO Fact_UProcedure (U_episode,id_professional,date_begin,id_notes,date_cancel,id_intervention,date_clinic_prescription,id_prescription)
        VALUES (u_ep,id_pro,ins_date(date_begin),ins_Note(note,note_cancel,id_prof_c),ins_date(date_cancel),ins_Intervention(id_i,Desc_Inter),ins_date(date_cli),id_pres);
	COMMIT;
END $$


-- --------------------------
--         Triggers        --
-- --------------------------

-- Serve para atualizar um inteiro reponsavel pelo ultimo id das datas inserido.(é só para ser mais eficiente).
use urgency;
DELIMITER $$
DROP TRIGGER IF EXISTS act_date;$$
CREATE TRIGGER act_date AFTER INSERT ON Dim_Date FOR EACH ROW
BEGIN
	SET @last_date = @last_date + 1 ;
END$$

-- Serve para atualizar um inteiro reponsavel pelo ultimo id do episódio inserido.
DELIMITER $$
DROP TRIGGER IF EXISTS act_Uep;$$
CREATE TRIGGER act_Uep AFTER INSERT ON Dim_Entry_Patient FOR EACH ROW
BEGIN
	SET @last_ep = @last_ep + 1 ;
END$$




