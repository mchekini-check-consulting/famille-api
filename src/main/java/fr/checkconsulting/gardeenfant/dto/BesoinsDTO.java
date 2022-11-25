package fr.checkconsulting.gardeenfant.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class BesoinsDTO {
    private String id_besoin;
    private Integer jour;
    private Integer besoin_matin_debut;
    private Integer besoin_matin_fin;
    private Integer besoin_midi_debut;
    private Integer besoin_midi_fin;
    private Integer besoin_soir_debut;
    private Integer besoin_soir_fin;
    private String emailFamille;
    private String type;
}