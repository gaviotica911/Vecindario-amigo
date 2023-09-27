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
import co.edu.uniandes.dse.vecindarioamigo.services.Zona_VerdeComentarioService;

import java.util.List;

@RestController
@RequestMapping("/zonas_verdes/{zonaVerdeId}/comentarios")
public class Zona_VerdeComentarioController {

    @Autowired
    private Zona_VerdeComentarioService zonaVerdeComentarioService;

    /**
     * Asigna un comentario a una Zona_Verde.
     *
     * @param zonaVerdeId  El ID de la Zona_Verde.
     * @param comentarioId El ID del comentario a asignar.
     * @throws EntityNotFoundException si la Zona_Verde o el comentario no se
     *                                 encuentran.
     */
    @PostMapping("/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void assignComentarioToZonaVerde(@PathVariable("zonaVerdeId") Long zonaVerdeId,
            @PathVariable("comentarioId") Long comentarioId) throws EntityNotFoundException {
        zonaVerdeComentarioService.addComentario(comentarioId, zonaVerdeId);
    }

    /**
     * Obtiene todos los comentarios asociados a una Zona_Verde.
     *
     * @param zonaVerdeId El ID de la Zona_Verde.
     * @return Lista de comentarios asociados a la Zona_Verde.
     * @throws EntityNotFoundException si la Zona_Verde no se encuentra.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ComentarioEntity> getComentariosFromZonaVerde(@PathVariable("zonaVerdeId") Long zonaVerdeId)
            throws EntityNotFoundException {
        return zonaVerdeComentarioService.getComentarios(zonaVerdeId);
    }
}
