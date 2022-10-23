package fr.checkconsulting.gardeenfant.dto;

import lombok.Data;

@Data
public class FamilleDTO {
    private String email;
    private String nom;
    private String prenomRepresentant;
    private String adresse;
    private String numeroTelephone;
    private String pseudo;
}
