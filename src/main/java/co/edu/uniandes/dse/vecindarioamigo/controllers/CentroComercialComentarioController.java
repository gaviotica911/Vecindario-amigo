package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.CentroComercialComentarioService;

@RestController
@RequestMapping("/centrosComerciales/{centroComercialId}/comentarios")
public class CentroComercialComentarioController {
    @Autowired
    private CentroComercialComentarioService centroComercialComentarioService;

    @PostMapping("/{centroComercialId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void assignComentarioToCentroComercial(@PathVariable("centroComercialId") Long centroComercialId,
            @PathVariable("comentarioId") Long comentarioId) throws EntityNotFoundException {
        centroComercialComentarioService.addComentario(comentarioId, centroComercialId);
    }
}
