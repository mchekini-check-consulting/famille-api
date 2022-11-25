package fr.checkconsulting.gardeenfant.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Options {
    @Id
    private String emailFamille;
    @Column(nullable=false, columnDefinition = "int4 default 1")
    private int autosave;
}
