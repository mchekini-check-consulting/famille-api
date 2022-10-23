package fr.checkconsulting.gardeenfant.resources;

import fr.checkconsulting.gardeenfant.entity.Famille;
import fr.checkconsulting.gardeenfant.services.FamilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/v1/famille")
public class FamilleResource {
    private final FamilleService familleService;

    @Autowired
    public FamilleResource(FamilleService familleService) {
        this.familleService = familleService;
    }


    @GetMapping("get-all")
    public List<Famille> getAllFamilles() {
        return familleService.getAllFamilles();
    }

    @GetMapping("getById/{famille-email}")
    public Famille getFamilleById(@PathVariable("famille-email") String email) {
        return familleService.getFamilleByEmail(email);
    }

    @PutMapping("update")
    public void updateFamille(@RequestBody Famille famille) {
        familleService.updateFamille(famille);
    }
}
