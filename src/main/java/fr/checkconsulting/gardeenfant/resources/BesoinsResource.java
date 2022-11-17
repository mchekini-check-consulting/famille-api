package fr.checkconsulting.gardeenfant.resources;


import fr.checkconsulting.gardeenfant.dto.BesoinsDTO;
import fr.checkconsulting.gardeenfant.entity.Besoins;
import fr.checkconsulting.gardeenfant.services.BesoinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/famille/besoins")
public class BesoinsResource {
    private final BesoinsService besoinsService;

    @Autowired
    public BesoinsResource(BesoinsService besoinsService) {
        this.besoinsService = besoinsService;
    }

    @GetMapping("")
    public ResponseEntity<List<BesoinsDTO>> getAllBesoinsByEmailFamille() throws Exception {
        return ResponseEntity.ok(besoinsService.getAllBesoinsByEmailFamille());
    }

    @PostMapping("/create")
    public void createBesoin(@RequestBody Besoins besoin) throws Exception {
        besoinsService.creerNouveauBesoin(besoin);
    }

    @PutMapping("/update")
    public void updateBesoin(@RequestBody Besoins besoin) throws Exception {
        besoinsService.modifierBesoin(besoin);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBesoin(@PathVariable("id") String id) throws Exception {
        besoinsService.supprimerBesoin(id);
    }

    @DeleteMapping("/delete-all")
    public void deleteAllBesoins() throws Exception {
        besoinsService.supprimerTousBesoins();
    }
}
