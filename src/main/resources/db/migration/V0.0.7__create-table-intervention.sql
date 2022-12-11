create table if not exists intervention (
    time_intervention timestamp primary key,
    debut_intervention timestamp,
    fin_intervention timestamp,
    jour varchar,
    matin varchar,
    midi varchar,
    soir varchar,
    email_famille varchar,
    email_nounou varchar,
    etat varchar(15)  default 'Instance'
);

ALTER TABLE intervention ADD FOREIGN KEY (email_famille) REFERENCES famille (email);