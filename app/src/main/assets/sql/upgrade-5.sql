BEGIN TRANSACTION;

-- Rename the existing table
ALTER TABLE Notes RENAME TO Temp_Notes;

-- Create new table
CREATE
    TABLE Notes
    (
        note_id TEXT PRIMARY KEY,
        title TEXT,
        content TEXT,
        creation TEXT,
        time_created TEXT,
        date_created TEXT,
        last_modification TEXT
    );

-- COPY ALL THE COLUMNS DATA TO NEW TABLE
-- INSERT INTO Notes SELECT note_id, title, content FROM Temp_Notes

-- Drop the old table
DROP TABLE Temp_Notes;

COMMIT;