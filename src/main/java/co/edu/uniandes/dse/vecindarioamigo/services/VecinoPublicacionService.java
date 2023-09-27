package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecinoRepository;
import lombok.extern.slf4j.Slf4j;
import co.edu.uniandes.dse.vecindarioamigo.repositories.PublicacionRepository;


@Slf4j
@Service
public class VecinoPublicacionService {
    
    @Autowired
	private PublicacionRepository publicacionRepository;

    @Autowired
	private VecinoRepository vecinoRepository;

    /**
	 * Agregar una publicacion al vecino
	 *
	 * @param publicacionId     El id de la publicacion a guardar
	 * @param vecinoId      El id del vecino en el cual se va a guardar la publicacion.
	 * @return La publicacion creada.
	 * @throws EntityNotFoundException 
	 */
	
	@Transactional
	public PublicacionEntity addPublicacion(Long publicacionId, Long vecinoId) throws EntityNotFoundException {
		log.info("Start the process of adding a post to the neighbor with id = {0}", vecinoId);
		
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if(publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if(vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);
		
		publicacionEntity.get().setVecino(vecinoEntity.get());
		log.info("Finish process of adding a post to the neighbor with id = {0}", vecinoId);
		return publicacionEntity.get();
	}
    
    /**
	 * Retorna todas las publicaciones asociados a un vecino
	 *
	 * @param vecinoId El ID del vecino buscado
	 * @return La lista de publicaciones del vecino
	 * @throws EntityNotFoundException si el vecino no existe
	 */
	@Transactional
	public List<PublicacionEntity> getPublicaciones(Long vecinoId) throws EntityNotFoundException {
		log.info("Start the process of consulting the posts associated with the neighbor with id = {0}", vecinoId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if(vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);
		
		return vecinoEntity.get().getPublicaciones();

	}

    /**
	 * Retorna una publicacion asociada a un vecino
	 *
	 * @param vecinoId El id del vecino a buscar.
	 * @param publicacionId El id de la publicacion a buscar
	 * @return La publicacion encontrada dentro del vecino.
	 * @throws EntityNotFoundException Si la publicacion no se encuentra en el vecino
	 * @throws IllegalOperationException Si la publicacion no está asociado a el vecino
	 */
	@Transactional
	public PublicacionEntity getPublicacion(Long vecinoId, Long publicacionId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start the process of consulting the post with id = {0} from the neighbor with id = " + vecinoId, publicacionId);
		
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if(vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);
		
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if(publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
				
		log.info("The process of consulting the post with id ends = {0} from the neighbor with id = " + vecinoId, publicacionId);
		
		if(!vecinoEntity.get().getPublicaciones().contains(publicacionEntity.get()))
			throw new IllegalOperationException("The post is not associated to the neighbor");
		
		return publicacionEntity.get();
	}

    /**
	 * Remplazar publicaciones de un vecino
	 *
	 * @param publicaciones Lista de publicaciones que serán las de un vecino.
	 * @param vecinoId El id del vecino que se quiere actualizar.
	 * @return La lista de publicaciones actualizada.
	 * @throws EntityNotFoundException Si el vecino o una publicacion de la lista no se encuentran
	 */
	@Transactional
	public List<PublicacionEntity> replacePublicaciones(Long vecinoId, List<PublicacionEntity> publicaciones) throws EntityNotFoundException {
		log.info("Start process of updating the neighbor with id = {0}", vecinoId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if(vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);
		
		for(PublicacionEntity publicacion : publicaciones) {
			Optional<PublicacionEntity> z = publicacionRepository.findById(publicacion.getId());
			if(z.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
			
			z.get().setVecino(vecinoEntity.get());
		}		
		return publicaciones;
	}
    
}
