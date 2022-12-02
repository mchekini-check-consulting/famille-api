package fr.checkconsulting.gardeenfant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

enum NotificationType {
    INFO, WARN
}

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Long id;
    private String content;
    private NotificationType type;
}
