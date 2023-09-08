package co.edu.uniandes.dse.vecindarioamigo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.CentroComercialRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.ComentarioRepository;

/**
 *
 * @author ISIS2603
 */
@Slf4j
@Service

public class CentroComercialComentarioService {
    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private CentroComercialRepository centroComercialRepository;

    @Transactional
    public ComentarioEntity addComentario(Long comentarioID, Long centroComercialID) throws EntityNotFoundException {
        log.info("Start the process of adding a comment to the shopping centre with id = {0}", centroComercialID);

        Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioID);
        if (comentarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        comentarioEntity.get().setCentroComercial(centroComercialEntity.get());
        log.info("Finish process of adding a comment to the shopping centre with id = {0}", centroComercialID);
        return comentarioEntity.get();
    }

    @Transactional
    public List<ComentarioEntity> getComentarios(Long centroComercialID) throws EntityNotFoundException {
        log.info("Start the process of consulting the comments associated with the shopping centre with id = {0}",
                centroComercialID);
        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        return centroComercialEntity.get().getComentarios();
    }

    @Transactional
    public ComentarioEntity getComentario(Long centroComercialID, Long comentarioID)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Start the process of consulting the comment with id = {0} from the shopping centre with id = "
                + centroComercialID, comentarioID);

        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioID);
        if (comentarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        log.info("The process of consulting the comment with id ends = {0} from the shopping centre with id = "
                + centroComercialID, comentarioID);

        if (!centroComercialEntity.get().getComentarios().contains(comentarioEntity.get()))
            throw new IllegalOperationException("The comment is not associated to the shopping centre");

        return comentarioEntity.get();
    }

    @Transactional
    public List<ComentarioEntity> replaceComentarios(Long centroComercialID, List<ComentarioEntity> comentarios)
            throws EntityNotFoundException {
        log.info("Start process of updating the shopping centre with id = {0}", centroComercialID);
        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        for (ComentarioEntity comentario : comentarios) {
            Optional<ComentarioEntity> c = comentarioRepository.findById(comentario.getId());
            if (c.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

            c.get().setCentroComercial(centroComercialEntity.get());
        }
        return comentarios;
    }

    @Transactional
    public void removeComentario(Long centroComercialId, Long comentarioId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un comentario del centro comercial con id = {0}", centroComercialId);
        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialId);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
        if (comentarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

        centroComercialEntity.get().getComentarios().remove(comentarioEntity.get());
        log.info("Finaliza proceso de borrar un comentario del centro comercial con id = {0}", centroComercialId);
    }
}
