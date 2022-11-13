package fr.checkconsulting.gardeenfant.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class BesoinsDTO {
    private String id_besoin;
    private Integer jour;
    private LocalTime besoin_matin_debut;
    private LocalTime besoin_matin_fin;
    private LocalTime besoin_midi_debut;
    private LocalTime besoin_midi_fin;
    private LocalTime besoin_soir_debut;
    private LocalTime besoin_soir_fin;
    private String emailFamille;
}
