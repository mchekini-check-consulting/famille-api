create table if not exists famille (
  email varchar primary key,
  nom varchar(50),
  prenom_representant varchar(50),
  adresse varchar(300),
  numero_telephone varchar(10),
  pseudo varchar(50)
);
