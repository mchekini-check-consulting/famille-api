package fr.checkconsulting.gardeenfant.resources;

import fr.checkconsulting.gardeenfant.dto.FamilleDTO;
import fr.checkconsulting.gardeenfant.entity.Famille;
import fr.checkconsulting.gardeenfant.services.FamilleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/v1/famille")
public class FamilleResource {
    private final FamilleService familleService;

    @Autowired
    private ModelMapper modelMapper;
    public FamilleResource(FamilleService familleService) {
        this.familleService = familleService;
    }


    @GetMapping("get-all")
    public List<FamilleDTO> getAllFamilles() {
        return familleService.getAllFamilles().stream().map(item -> modelMapper.map(item, FamilleDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("getById/{famille-email}")
    public ResponseEntity<FamilleDTO> getFamilleById(@PathVariable("famille-email") String email) {
        Famille famille = familleService.getFamilleByEmail(email);

        //Convertir l'entité en DTO
        FamilleDTO familleResponse = modelMapper.map(famille, FamilleDTO.class);

        return ResponseEntity.ok().body(familleResponse);
    }

    @PutMapping("update")
    public void updateFamille(@RequestBody Famille famille) {
        familleService.updateFamille(famille);
    }
}
