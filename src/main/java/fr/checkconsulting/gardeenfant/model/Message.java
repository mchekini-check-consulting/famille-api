package fr.checkconsulting.gardeenfant.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private String content;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime time;
    private String emailSource;
    private String emailDest;
}