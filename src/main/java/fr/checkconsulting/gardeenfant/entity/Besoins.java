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
    private String idBesoin;
    @NotEmpty(message = "Le champ Jour est obligatoire")
    private Integer jour;
    private LocalTime besoinMatinDebut;
    private LocalTime besoinMatinFin;
    private LocalTime besoinMidiDebut;
    private LocalTime besoinMidiFin;
    private LocalTime besoinSoirDebut;
    private LocalTime besoinSoirFin;
    private String emailFamille;
}
