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
    private String nom;
    private String prenom;
    private String rue;
    private String codePostal;
    private String ville;
    private String telephone;
    private String mail;
    private String pseudo;
}
