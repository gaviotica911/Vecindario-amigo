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
import co.edu.uniandes.dse.vecindarioamigo.services.Zona_VerdeVecindarioService;

@RestController
@RequestMapping("/zona_verde/{zonaVerdeId}/vecindario")
public class Zona_VerdeVecindarioController {

    @Autowired
    private Zona_VerdeVecindarioService zonaVerdeVecindarioService;

    /**
     * Desvincula una Zona_Verde de su vecindario asociado.
     *
     * @param zonaVerdeId El ID de la Zona_Verde a desvincular.
     * @throws EntityNotFoundException si la Zona_Verde no se encuentra.
     */
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeVecindarioFromZonaVerde(@PathVariable("zonaVerdeId") Long zonaVerdeId)
            throws EntityNotFoundException {
        zonaVerdeVecindarioService.removeVecindario(zonaVerdeId);
    }

    /**
     * Obtiene el vecindario asociado a una Zona_Verde.
     *
     * @param zonaVerdeId El ID de la Zona_Verde.
     * @return El vecindario asociado a la Zona_Verde.
     * @throws EntityNotFoundException si la Zona_Verde no se encuentra.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public VecindarioEntity getVecindarioFromZonaVerde(@PathVariable("zonaVerdeId") Long zonaVerdeId)
            throws EntityNotFoundException {
        return zonaVerdeVecindarioService.getVecindario(zonaVerdeId);
    }
}
