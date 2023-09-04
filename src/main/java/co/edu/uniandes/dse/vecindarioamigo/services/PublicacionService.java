package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.PublicacionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PublicacionService {
      @Autowired 
    PublicacionRepository publicacionRepository;

	/**
	 * Crea una publicacion  en la persistencia.
	 *
	 * @param PublicacionEntity La entidad que representa la Publicacion a persistir.
	 * @return La entidad de la Publicacion luego de persistirla.
	 * @throws IllegalOperationException Si la Publicacion a persistir ya existe.
	 */

	@Transactional 

	public PublicacionEntity createPublicacion(PublicacionEntity publicacionEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("The post creation process begins");
		if (!publicacionRepository.findById(publicacionEntity.getId()).isEmpty()) {
			throw new IllegalOperationException("post name already exists");
		}
		log.info("End of post creation process");
		return publicacionRepository.save(publicacionEntity);
		}
	
	/**
	 *
	 * Obtener todas las publicaciones existentes en la base de datos.
	 *
	 * @return una lista de publicaciones.
	 */
	@Transactional
	public List<PublicacionEntity> getPublicacion() {
		log.info("The process of consulting all the posts begins");
		return publicacionRepository.findAll();
	}

	/**
	 *
	 * Obtener una publicacion por medio de su id.
	 *
	 * @param publicacionId: id del post para ser buscado.
	 * @return el post solicitado por medio de su id.
	 */
	@Transactional
	public PublicacionEntity getPublicacion(Long publicacionId) throws EntityNotFoundException {
		log.info("Start the process of consulting the neighborhood with id = {0}", publicacionId);
		Optional<PublicacionEntity> publicacion = publicacionRepository.findById(publicacionId);
		if (publicacion.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		log.info("End process of consulting the post with id = {0}", publicacionId);
		return publicacion.get();
	}

	/**
	 *
	 * Actualizar una publicacion.
	 *
	 * @param publicacionId:    id del post para buscarlo en la base de datos.
	 * @param publicacion: publicacion con los cambios para ser actualizado.
	 * @return el post con los cambios actualizados en la base de datos.
	 */
	@Transactional
	public PublicacionEntity updatePublicacion(Long publicacionId, PublicacionEntity publicacion) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start process of updating the post with id = {0}", publicacionId);
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if (publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);

		publicacion.setId(publicacionId);
		log.info("Finish process of updating the post with id = {0}", publicacionId);
		return publicacionRepository.save(publicacion);
	}

	/**
	 * Borrar una publicacion
	 *
	 * @param publicacionId: id del post a borrar
	 * @throws BusinessLogicException 
	 */
	@Transactional
	public void deletePublicacion(Long publicacionId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start process of deleting the post with id = {0}", publicacionId);
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if (publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);

		
		log.info("End process of checking the post constraints with id = {0}", publicacionId);

		publicacionRepository.deleteById(publicacionId);
		log.info("End process of deleting the post with id = {0}", publicacionId);
	}

}

	


