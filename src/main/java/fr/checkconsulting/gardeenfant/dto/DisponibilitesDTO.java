package fr.checkconsulting.gardeenfant.dto;

import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilitesDTO {
    private int id;
    private int jour;
    private LocalDateTime date_debut_matin;
    private LocalDateTime date_fin_matin;
    private LocalDateTime date_debut_midi;
    private LocalDateTime date_fin_midi;
    private LocalDateTime date_debut_soir;
    private LocalDateTime date_fin_soir;
    private String nounouId;
}