package fr.checkconsulting.gardeenfant.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Intervention {
    @Id
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeIntervention;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime debutIntervention;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime finIntervention;
    private String jour;
    private String matin;
    private String midi;
    private String soir;
    private String emailFamille;
    private String emailNounou;
    @Column(nullable=false, columnDefinition = "varchar(15) default 'Instance'")
    private String etat;
}
