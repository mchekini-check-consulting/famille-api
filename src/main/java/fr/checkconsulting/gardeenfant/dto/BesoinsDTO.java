package fr.checkconsulting.gardeenfant.dto;

import lombok.Data;

@Data
public class BesoinsDTO {
    private String idBesoin;
    private Integer jour;
    private Integer besoinMatinDebut;
    private Integer besoinMatinFin;
    private Integer besoinMidiDebut;
    private Integer besoinMidiFin;
    private Integer besoinSoirDebut;
    private Integer besoinSoirFin;
    private String type;
}