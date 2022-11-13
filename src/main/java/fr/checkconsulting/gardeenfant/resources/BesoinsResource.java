package fr.checkconsulting.gardeenfant.resources;


import fr.checkconsulting.gardeenfant.dto.BesoinsDTO;
import fr.checkconsulting.gardeenfant.services.BesoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/famille/besoins")
public class BesoinsResource {
    private final BesoinsService besoinsService;

    @Autowired
    public BesoinsResource(BesoinsService besoinsService) {
        this.besoinsService = besoinsService;
    }

    @GetMapping("{emailFamille}")
    public ResponseEntity<List<BesoinsDTO>> getAllBesoinsByEmailFamille(@PathVariable("emailFamille") String emailFamille) throws Exception {
        return ResponseEntity.ok(besoinsService.getAllBesoinsByEmailFamille(emailFamille));
    }
}
