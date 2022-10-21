create table if not exists enfant
(
    id             integer primary key,
    prenom         varchar(50),
    date_naissance date,
    sexe           char,
    email          varchar,

    constraint fk_famille_enfant foreign key (email) references famille (email)

);
