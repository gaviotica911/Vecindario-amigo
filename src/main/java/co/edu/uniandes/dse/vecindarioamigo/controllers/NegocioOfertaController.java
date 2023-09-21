package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.entities.OfertaEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.NegocioOfertaService;

import java.util.List;

@RestController
@RequestMapping("/negocios/{negocioId}/ofertas")
public class NegocioOfertaController {

    @Autowired
    private NegocioOfertaService negocioOfertaService;

    /**
     * Asigna una oferta a un negocio.
     *
     * @param negocioId El ID del negocio.
     * @param ofertaId  El ID de la oferta a asignar.
     * @throws EntityNotFoundException si el negocio o la oferta no se encuentran.
     */
    @PostMapping("/{ofertaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void assignOfertaToNegocio(@PathVariable("negocioId") Long negocioId,
            @PathVariable("ofertaId") Long ofertaId) throws EntityNotFoundException {
        negocioOfertaService.addOferta(ofertaId, negocioId);
    }

    /**
     * Obtiene todas las ofertas asociadas a un negocio.
     *
     * @param negocioId El ID del negocio.
     * @return Lista de ofertas asociadas al negocio.
     * @throws EntityNotFoundException si el negocio no se encuentra.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<OfertaEntity> getOfertasFromNegocio(@PathVariable("negocioId") Long negocioId)
            throws EntityNotFoundException {
        return negocioOfertaService.getOfertas(negocioId);
    }

    /**
     * Desvincula una oferta de un negocio.
     *
     * @param negocioId El ID del negocio.
     * @param ofertaId  El ID de la oferta a desvincular.
     * @throws EntityNotFoundException si el negocio o la oferta no se encuentran o
     *                                 la oferta no está asociada con el negocio.
     */
    @DeleteMapping("/{ofertaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeOfertaFromNegocio(@PathVariable("negocioId") Long negocioId,
            @PathVariable("ofertaId") Long ofertaId) throws EntityNotFoundException {
        negocioOfertaService.removeOferta(ofertaId, negocioId);
    }
}
