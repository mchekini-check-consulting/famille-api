package fr.checkconsulting.gardeenfant.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timeMessage;
    private String content;
    private String emailSource;
    private String emailDest;
}