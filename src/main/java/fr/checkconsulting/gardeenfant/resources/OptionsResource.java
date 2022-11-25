package fr.checkconsulting.gardeenfant.resources;

import fr.checkconsulting.gardeenfant.entity.Options;
import fr.checkconsulting.gardeenfant.entity.OptionsDTO;
import fr.checkconsulting.gardeenfant.services.OptionsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/famille/")
public class OptionsResource {
    private final OptionsService optionsService;

    @Autowired
    private ModelMapper modelMapper;
    public OptionsResource(OptionsService optionsService) {
        this.optionsService = optionsService;
    }


    @GetMapping("/get")
    public ResponseEntity<OptionsDTO> getOptionsFamilleById() throws Exception {
        Options options = optionsService.getOptionsFamilleById();

        //Convertir l'entité en DTO
        OptionsDTO optionsResponse = modelMapper.map(options, OptionsDTO.class);

        return ResponseEntity.ok().body(optionsResponse);
    }

    @PutMapping("/update")
    public void updateOptionsFamille(@RequestBody OptionsDTO options) throws Exception {
        optionsService.updateOptionsFamille(options);
    }
}
