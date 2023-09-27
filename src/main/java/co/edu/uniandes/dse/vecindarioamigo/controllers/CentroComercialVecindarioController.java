package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.CentroComercialVecindarioService;
import co.edu.uniandes.dse.vecindarioamigo.services.Zona_VerdeVecindarioService;

@RestController
@RequestMapping("/centroComercial/{centroComercialId}/vecindario")
public class CentroComercialVecindarioController {
    @Autowired
    private CentroComercialVecindarioService centroComercialVecindarioService;

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeVecindarioFromCentroComercial(@PathVariable("centroComercialId") Long zonaVerdeId)
            throws EntityNotFoundException {
        centroComercialVecindarioService.removeVecindario(zonaVerdeId);
    }
}
