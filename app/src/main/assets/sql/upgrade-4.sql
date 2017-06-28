-- Alter Notes table with 2 new column time and date columns
-- Delete existing column named creation
ALTER TABLE Notes DROP COLUMN creation;
ALTER TABLE Notes ADD COLUMN time_created TEXT;
ALTER TABLE Notes ADD COLUMN date_created TEXT;
