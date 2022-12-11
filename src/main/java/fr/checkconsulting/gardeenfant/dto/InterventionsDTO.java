package fr.checkconsulting.gardeenfant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionsDTO {
    private LocalDateTime timeIntervention;
    private LocalDateTime debut_intervention;
    private LocalDateTime fin_intervention;
    private String jour;
    private String matin;
    private String midi;
    private String soir;
    private String emailFamille;
    private String emailNounou;
    private String etat;
}
