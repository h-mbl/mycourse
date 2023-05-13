-- SQLite
CREATE TABLE student (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(50) NOT NULL,
  prenom VARCHAR(50) NOT NULL,
  age INT NOT NULL,
  programme_etude VARCHAR(50) NOT NULL,
  matricule VARCHAR(50) NOT NULL,
  mot_de_passe VARCHAR(50) NOT NULL
);