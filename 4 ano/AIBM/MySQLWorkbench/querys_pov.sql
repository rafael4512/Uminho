use urgency;
SET SQL_SAFE_UPDATES = 0;


-- Tabelas dependentes da de factos
DELETE FROM urgency.Prescription_Has_Drug;
DELETE FROM urgency.Dim_UPrescription;
DELETE FROM urgency.Fact_UProcedure;
DELETE FROM urgency.Dim_Exams;

-- Tabela de factos
DELETE FROM urgency.Fact_UEpisodes;

-- Tabelas dimensão c/ datas

DELETE FROM urgency.dim_triage;
DELETE FROM urgency.Dim_Discharge;
DELETE FROM urgency.Dim_Diagnosis;
DELETE FROM urgency.Dim_Admition;
DELETE FROM urgency.Dim_Entry_Patient;
DELETE FROM urgency.Dim_Date;
DELETE FROM urgency.Dim_Hierarchy_Diagnosis;

-- Tabelas dimensão s/ datas
DELETE FROM dim_color;
DELETE FROM dim_destination;
DELETE FROM dim_cause;
DELETE FROM dim_reason;
DELETE FROM dim_district;
DELETE FROM dim_notes;
DELETE FROM dim_intervention;
DELETE FROM dim_drug;
DELETE FROM dim_posology;

-- 
ALTER TABLE Dim_district AUTO_INCREMENT = 1;
ALTER TABLE Dim_drug AUTO_INCREMENT = 1;
ALTER TABLE Dim_posology AUTO_INCREMENT = 1;
ALTER TABLE Dim_notes AUTO_INCREMENT = 1;
ALTER TABLE Dim_Diagnosis AUTO_INCREMENT = 1;
ALTER TABLE Dim_Discharge AUTO_INCREMENT = 1;
ALTER TABLE Dim_Admition AUTO_INCREMENT = 1;
ALTER TABLE Dim_Triage AUTO_INCREMENT = 1;



