package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.ComentarioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.PublicacionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class PublicacionComentarioService {
    
    

    //dependency injections of the two entities of the relation
    @Autowired
	private ComentarioRepository comentarioRepository;

    @Autowired
	private PublicacionRepository publicacionRepository;

    /**
	 * Agregar un comentario al publicacion
	 *
	 * @param comentarioId     El id del comentario a guardar
	 * @param publicacionId      El id del publicacion en el cual se va a guardar el comentario.
	 * @return El comentario creado.
	 * @throws EntityNotFoundException 
	 */
	
	@Transactional
	public ComentarioEntity addComentario(Long comentarioId, Long publicacionId) throws EntityNotFoundException {
		log.info("Start the process of adding a comment to the post with id = {0}", publicacionId);
		
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if(comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
		
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if(publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		comentarioEntity.get().setPublicacion(publicacionEntity.get());
		log.info("Finish process of adding a comment to the post with id = {0}", publicacionId);
		return comentarioEntity.get();
	}
    
    /**
	 * Retorna todos los comentarios asociados a un publicacion
	 *
	 * @param publicacionId El ID del publicacion buscado
	 * @return La lista de comentarios del publicacion
	 * @throws EntityNotFoundException si el publicacion no existe
	 */
	@Transactional
	public List<ComentarioEntity> getComentarios(Long publicacionId) throws EntityNotFoundException {
		log.info("Start the process of consulting the comments associated with the post with id = {0}", publicacionId);
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if(publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		return publicacionEntity.get().getComentarios();
	}

    /**
	 * Retorna un comentario asociado a un publicacion
	 *
	 * @param publicacionId El id del publicacion a buscar.
	 * @param comentarioId El id del comentario a buscar
	 * @return El comentario encontrado dentro del publicacion.
	 * @throws EntityNotFoundException Si el comentario no se encuentra en el publicacion
	 * @throws IllegalOperationException Si el comentario no está asociado a el publicacion
	 */
	@Transactional
	public ComentarioEntity getComentario(Long publicacionId, Long comentarioId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start the process of consulting the comment with id = {0} from the post with id = " + publicacionId, comentarioId);
		
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if(publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if(comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
				
		log.info("The process of consulting the comment with id ends = {0} from the post with id = " + publicacionId, comentarioId);
		
		if(!publicacionEntity.get().getComentarios().contains(comentarioEntity.get()))
			throw new IllegalOperationException("The comment is not associated to the post");
		
		return comentarioEntity.get();
	}

    /**
	 * Remplazar comentarios de un publicacion
	 *
	 * @param comentarios Lista de comentarios que serán los de un publicacion.
	 * @param publicacionId El id del publicacion que se quiere actualizar.
	 * @return La lista de comentarios actualizada.
	 * @throws EntityNotFoundException Si el publicacion o un comentario de la lista no se encuentran
	 */
	@Transactional
	public List<ComentarioEntity> replaceComentarios(Long publicacionId, List<ComentarioEntity> comentarios) throws EntityNotFoundException {
		log.info("Start process of updating the post with id = {0}", publicacionId);
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if(publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		for(ComentarioEntity comentario : comentarios) {
			Optional<ComentarioEntity> v = comentarioRepository.findById(comentario.getId());
			if(v.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
			
			v.get().setPublicacion(publicacionEntity.get());
		}		
        return comentarios;
		
    
    }

}

