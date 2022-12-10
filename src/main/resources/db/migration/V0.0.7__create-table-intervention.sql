create table if not exists intervention (
    time_intervention timestamp primary key,
    jour int,
    matin int,
    midi int,
    soir int,
    email_famille varchar,
    email_nounou varchar
);

ALTER TABLE intervention ADD FOREIGN KEY (email_famille) REFERENCES famille (email);