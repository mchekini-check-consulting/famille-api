package fr.checkconsulting.gardeenfant.resources;


import fr.checkconsulting.gardeenfant.dto.DisponibiliteDTO;
import fr.checkconsulting.gardeenfant.dto.FamilleDTO;
import fr.checkconsulting.gardeenfant.dto.NounouDto;
import fr.checkconsulting.gardeenfant.services.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/search")
public class SearchResource {

    private final SearchService searchService;

    public SearchResource(SearchService searchService) {
        this.searchService = searchService;
    }


    @GetMapping("nounou")
    public List<NounouDto> getNounouByCriteria(@RequestParam("nom") String nom,
                                               @RequestParam("prenom") String prenom,
                                               @RequestParam("ville") String ville,
                                               @RequestParam("jour") int jour,
                                               @RequestParam("heureDebut") String heureDebut,
                                               @RequestParam("heureFin") String heureFin) {
        return searchService.getNounouByCriteria(nom, prenom, ville, jour, heureDebut, heureFin);
    }

    @GetMapping("dispo-nounou")
    public List<DisponibiliteDTO> getDispoNounou(@RequestParam("email") String email) {
        return searchService.getDispoNounou(email);
    }

    @GetMapping("famille")
    public List<FamilleDTO> getFamilleByCriteria(
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("ville") String ville,
            @RequestParam("jour") int jour,
            @RequestParam("heureDebut") String heureDebut,
            @RequestParam("heureFin") String heureFin
    ) {
        return searchService.getFamilleByCriteria(nom, prenom, ville, jour, heureDebut, heureFin);
    }
}
