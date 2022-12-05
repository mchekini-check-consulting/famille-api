package fr.checkconsulting.gardeenfant.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Besoins {
    @Id
    private String id_besoin;
    @NotEmpty(message = "Le champ Jour est obligatoire")
    private Integer jour;
    private LocalTime besoin_matin_debut;
    private LocalTime besoin_matin_fin;
    private LocalTime besoin_midi_debut;
    private LocalTime besoin_midi_fin;
    private LocalTime besoin_soir_debut;
    private LocalTime besoin_soir_fin;
    private String emailFamille;
}
