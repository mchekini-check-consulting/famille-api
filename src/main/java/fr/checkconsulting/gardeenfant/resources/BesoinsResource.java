package fr.checkconsulting.gardeenfant.resources;

import fr.checkconsulting.gardeenfant.dto.BesoinsDTO;
import fr.checkconsulting.gardeenfant.entity.Besoins;
import fr.checkconsulting.gardeenfant.security.CommonData;
import fr.checkconsulting.gardeenfant.services.BesoinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/famille/besoins")
public class BesoinsResource {
    private final BesoinsService besoinsService;

    @Autowired
    public BesoinsResource(BesoinsService besoinsService) {
        this.besoinsService = besoinsService;
    }

    //Afficher les besoins d'une famille
    @GetMapping("")
    public ResponseEntity<List<Besoins>> getAllBesoinsByEmailFamille() throws Exception {
        return ResponseEntity.ok(besoinsService.getAllBesoinsByEmailFamille());
    }

    //Ajouter un nouveau besoin
    @PostMapping("/create")
    public void createBesoin(@RequestBody BesoinsDTO besoin) throws Exception {
        besoinsService.creerNouveauBesoin(besoin);
    }

    // Modifier un besoin
    @PutMapping("/update")
    public void updateBesoin(@RequestBody BesoinsDTO besoin) throws Exception {
        besoinsService.modifierBesoin(besoin);
    }

    // Supprimer un besoin par son id
    @DeleteMapping("/delete/{id}")
    public void deleteBesoin(@PathVariable("id") String id) throws Exception {
        besoinsService.supprimerBesoin(id);
    }

    // Supprimer tous les besoins d'une famille
    @DeleteMapping("/delete-all")
    public void deleteAllBesoins() throws Exception {
        besoinsService.supprimerTousBesoins();
    }
}
