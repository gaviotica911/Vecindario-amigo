package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.NegocioVecindarioService;

@RestController
@RequestMapping("/negocios/{negocioId}/vecindario")
public class NegocioVecindarioController {

    @Autowired
    private NegocioVecindarioService negocioVecindarioService;

    /**
     * Asigna un vecindario a un negocio.
     *
     * @param negocioId    El ID del negocio.
     * @param vecindarioId El ID del vecindario a asignar.
     * @throws EntityNotFoundException si el negocio o el vecindario no se
     *                                 encuentra.
     */
    @PutMapping("/{vecindarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void assignVecindarioToNegocio(@PathVariable("negocioId") Long negocioId,
            @PathVariable("vecindarioId") Long vecindarioId) throws EntityNotFoundException {
        negocioVecindarioService.replaceVecindario(negocioId, vecindarioId);
    }

    /**
     * Desvincula un negocio de su vecindario asociado.
     *
     * @param negocioId El ID del negocio a desvincular.
     * @throws EntityNotFoundException si el negocio no se encuentra.
     */
    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeVecindarioFromNegocio(@PathVariable("negocioId") Long negocioId) throws EntityNotFoundException {
        negocioVecindarioService.removeVecindario(negocioId);
    }
}
