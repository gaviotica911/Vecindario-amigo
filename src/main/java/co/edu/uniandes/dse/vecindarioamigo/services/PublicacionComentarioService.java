package co.edu.uniandes.dse.vecindarioamigo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.PublicacionRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.ComentarioRepository;

/**
 * Clase que implementa la conexión con la persistencia para la relación entre
 * la entidad Publicacion y Comentario.
 *
 * @Comentario ISIS2603
 */
@Slf4j
@Service
public class PublicacionComentarioService {
    
    

    //dependency injections of the two entities of the relation
    @Autowired
	private ComentarioRepository ComentarioRepository;

    @Autowired
	private PublicacionRepository PublicacionRepository;

    /**
	 * Agregar unA ZONA al Publicacion
	 *
	 * @param ComentarioId     El id de la zona verde a guardar
	 * @param PublicacionId      El id del Publicacion en el cual se va a guardar el negocio.
	 * @return La zona verde creada.
	 * @throws EntityNotFoundException 
	 */
	
	@Transactional
	public ComentarioEntity addComentario(Long ComentarioId, Long PublicacionId) throws EntityNotFoundException {
		log.info("Start the process of adding a commnet to the post with id = {0}", PublicacionId);
		
		Optional<ComentarioEntity> ComentarioEntity = ComentarioRepository.findById(ComentarioId);
		if(ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
		
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(PublicacionId);
		if(PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		ComentarioEntity.get().setPublicacion(PublicacionEntity.get());
		log.info("Finish process of adding a commnet to the post with id = {0}", PublicacionId);
		return ComentarioEntity.get();
	}
    
    /**
	 * Retorna todas las zonas verdes asociados a un Publicacion
	 *
	 * @param PublicacionId El ID del Publicacion buscado
	 * @return La lista de zonas verdes del Publicacion
	 * @throws EntityNotFoundException si el Publicacion no existe
	 */
	@Transactional
	public List<ComentarioEntity> getComentaros(Long PublicacionId) throws EntityNotFoundException {
		log.info("Start the process of consulting the commnets associated with the post with id = {0}", PublicacionId);
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(PublicacionId);
		if(PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		return PublicacionEntity.get().getComentarios();
	}

    /**
	 * Retorna una zona verde asociada a un Publicacion
	 *
	 * @param PublicacionId El id del Publicacion a buscar.
	 * @param ComentarioId El id de la zona verde a buscar
	 * @return La zona verde encontrada dentro del Publicacion.
	 * @throws EntityNotFoundException Si la zona verde no se encuentra en el Publicacion
	 * @throws IllegalOperationException Si la zona verde no está asociado a el Publicacion
	 */
	@Transactional
	public ComentarioEntity getComentario(Long PublicacionId, Long ComentarioId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start the process of consulting the commnet with id = {0} from the post with id = " + PublicacionId, ComentarioId);
		
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(PublicacionId);
		if(PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		Optional<ComentarioEntity> ComentarioEntity = ComentarioRepository.findById(ComentarioId);
		if(ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
				
		log.info("The process of consulting the commnet with id ends = {0} from the post with id = " + PublicacionId, ComentarioId);
		
		if(!PublicacionEntity.get().getComentarios().contains(ComentarioEntity.get()))
			throw new IllegalOperationException("The commnet is not associated to the post");
		
		return ComentarioEntity.get();
	}

    /**
	 * Remplazar zonas verdes de un Publicacion
	 *
	 * @param Zonas_Verdes Lista de zonas verdes que serán las de un Publicacion.
	 * @param PublicacionId El id del Publicacion que se quiere actualizar.
	 * @return La lista de zonas verdes actualizada.
	 * @throws EntityNotFoundException Si el Publicacion o una zona verde de la lista no se encuentran
	 */
	@Transactional
	public List<ComentarioEntity> replaceComentarios(Long PublicacionId, List<ComentarioEntity> Zonas_Verdes) throws EntityNotFoundException {
		log.info("Start process of updating the post with id = {0}", PublicacionId);
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(PublicacionId);
		if(PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		for(ComentarioEntity Comentario : Zonas_Verdes) {
			Optional<ComentarioEntity> z = ComentarioRepository.findById(Comentario.getId());
			if(z.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
			
			z.get().setPublicacion(PublicacionEntity.get());
		}		
		return Zonas_Verdes;
	}
	
}
