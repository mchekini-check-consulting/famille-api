package fr.checkconsulting.gardeenfant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FamilleDTO {
    private String email;
    private String nom;
    private String prenomRepresentant;
    private String adresse;
    private String numeroTelephone;
    private String pseudo;
}
