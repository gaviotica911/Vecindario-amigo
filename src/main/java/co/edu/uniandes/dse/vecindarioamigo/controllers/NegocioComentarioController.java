package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.NegocioComentariosService;

import java.util.List;

@RestController
@RequestMapping("/negocios/{negocioId}/comentarios")
public class NegocioComentarioController {

    @Autowired
    private NegocioComentariosService negocioComentariosService;

    /**
     * Asigna un comentario a un negocio.
     *
     * @param negocioId    El ID del negocio.
     * @param comentarioId El ID del comentario a asignar.
     * @throws EntityNotFoundException si el negocio o el comentario no se
     *                                 encuentra.
     */
    @PostMapping("/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void assignComentarioToNegocio(@PathVariable("negocioId") Long negocioId,
            @PathVariable("comentarioId") Long comentarioId) throws EntityNotFoundException {
        negocioComentariosService.addComentario(comentarioId, negocioId);
    }

    /**
     * Obtiene todos los comentarios asociados a un negocio.
     *
     * @param negocioId El ID del negocio.
     * @return Lista de comentarios asociados al negocio.
     * @throws EntityNotFoundException si el negocio no se encuentra.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ComentarioEntity> getComentariosFromNegocio(@PathVariable("negocioId") Long negocioId)
            throws EntityNotFoundException {
        return negocioComentariosService.getComentarios(negocioId);
    }
}
