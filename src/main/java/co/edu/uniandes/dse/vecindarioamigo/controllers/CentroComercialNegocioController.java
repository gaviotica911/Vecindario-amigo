package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.CentroComercialComentarioService;
import co.edu.uniandes.dse.vecindarioamigo.services.CentroComercialNegocioService;
import co.edu.uniandes.dse.vecindarioamigo.services.NegocioCentroComercialService;
import co.edu.uniandes.dse.vecindarioamigo.services.NegocioComentariosService;

@RestController
@RequestMapping("/centrosComerciales/{centroComercialId}/negocios")
public class CentroComercialNegocioController {
    @Autowired
    private CentroComercialNegocioService centroComercialNegocioService;

    @PostMapping("/{centroComercialId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void assignComentarioToCentroComercial(@PathVariable("centroComercialId") Long centroComercialId,
            @PathVariable("negocioId") Long negocioId) throws EntityNotFoundException {
        centroComercialNegocioService.addNegocio(negocioId, centroComercialId);
    }
}