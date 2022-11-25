create table if not exists besoins (
    id_besoin varchar primary key,
    jour int,
    besoin_matin_debut time,
    besoin_matin_fin time,
    besoin_midi_debut time,
    besoin_midi_fin time,
    besoin_soir_debut time,
    besoin_soir_fin time,
    email_famille varchar
);

ALTER TABLE besoins ADD FOREIGN KEY (email_famille) REFERENCES famille (email);