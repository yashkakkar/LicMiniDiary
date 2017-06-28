-- Alter table add two new columns image_name and image_file
ALTER TABLE Members ADD COLUMN image_name TEXT;
ALTER TABLE Members ADD COLUMN image_file BLOB;