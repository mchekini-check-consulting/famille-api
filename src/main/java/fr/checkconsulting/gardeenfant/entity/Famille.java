package fr.checkconsulting.gardeenfant.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Famille implements Serializable {

    @Id
    private String email;
    private String nom;
    private String prenomRepresentant;
    private String rue;
    private String codePostal;
    private String ville;
    private String numeroTelephone;
    private String pseudo;
}
