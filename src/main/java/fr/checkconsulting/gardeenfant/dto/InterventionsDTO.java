package fr.checkconsulting.gardeenfant.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionsDTO {
    private LocalDateTime timeIntervention;
    private Integer jour;
    private Integer matin;
    private Integer midi;
    private Integer soir;
    private String emailFamille;
    private String emailNounou;
}
