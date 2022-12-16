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

@RestController
@RequestMapping("api/v1/famille/infos")
public class FamilleResource {
    private final FamilleService familleService;

    @Autowired
    private ModelMapper modelMapper;
    public FamilleResource(FamilleService familleService) {
        this.familleService = familleService;
    }


    @GetMapping("/get-all")
    public List<FamilleDTO> getAllFamilles() {
        return familleService.getAllFamilles().stream().map(item -> modelMapper.map(item, FamilleDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/get")
    public ResponseEntity<FamilleDTO> getFamilleById() throws Exception {
        Famille famille = familleService.getFamilleByEmail();

        //Convertir l'entité en DTO
        FamilleDTO familleResponse = modelMapper.map(famille, FamilleDTO.class);
        familleResponse.setMail(famille.getEmail());
        familleResponse.setPseudo(famille.getPseudo());

        return ResponseEntity.ok().body(familleResponse);
    }

    @PutMapping("/update")
    public void updateFamille(@RequestBody FamilleDTO famille) throws Exception {
        Famille familleEntity = modelMapper.map(famille, Famille.class);
        familleEntity.setPrenomRepresentant(famille.getPrenom());
        familleEntity.setEmail(famille.getMail());
        familleEntity.setNumeroTelephone(famille.getTelephone());

        familleService.updateFamille(familleEntity);
    }
}