-- Create table for MEMBERS
CREATE
    TABLE Members
    (
        member_id TEXT PRIMARY KEY,
        name TEXT,
        phone_number TEXT,
        email_id TEXT,
        gender TEXT,
        image_path TEXT,
        image_name TEXT,
        image_file BLOB,
        fav INTEGER DEFAULT 0
    );


-- Create table for POLICIES
CREATE
    TABLE Policies
    (
        policy_id TEXT PRIMARY KEY,
        member_id TEXT,
        policy_name TEXT,
        policy_number TEXT,
        doc_date TEXT,
        dlp_date TEXT,
        dom_date TEXT,
        dob_date TEXT,
        fup TEXT,
        term TEXT,
        mode TEXT,
        sum_assured_amount TEXT,
        premium_amount TEXT,
        nominee_name TEXT,
        marked INTEGER DEFAULT 0,
        book_marked INTEGER DEFAULT 0,
        policy_status INTEGER DEFAULT 5
    );

-- Create table for NOTES
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


