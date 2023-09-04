package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.repositories.PublicacionRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecinoRepository;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class PublicacionVecinoService {
    
	@Autowired
	private VecinoRepository vecinoRepository;

	@Autowired
	private PublicacionRepository publicacionRepository;
	
	/**
	 * Agregar un vecino a una publicacion
	 *
	 * @param publicacionId  El id de la publicacion a guardar
	 * @param vecinoId El id del vecino al cual se le va a guardar la publicacion.
	 * @return La publicacion que fue agregada al vecino.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public VecinoEntity addVecino(Long vecinoId, Long publicacionId) throws EntityNotFoundException {
		log.info("Starts the process of associating the deal with id = {0} to the post with id = " + publicacionId, vecinoId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);

		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if (publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);

		publicacionEntity.get().setVecino(vecinoEntity.get());
		log.info("End process of associating the deal with id = {0} to the post with id = {1}", vecinoId, publicacionId);
		return vecinoEntity.get();
	}

	/**
	 *
	 * Obtener un Vecino por medio del id de la publicacion.
	 *
	 * @param publicacionId id de la publicacion a ser buscada.
	 * @return el Vecino solicitado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public VecinoEntity getVecino(Long publicacionId) throws EntityNotFoundException {
		log.info("Start the process of consulting the neighbor of the post with id = {0}", publicacionId);
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if (publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);

		VecinoEntity vecinoEntity = publicacionEntity.get().getVecino();

		if (vecinoEntity == null)
			throw new EntityNotFoundException("The neighbor was not found");

		log.info("The process of consulting the neighbor of the post with id ends = {0}", publicacionId);
		return vecinoEntity;
	}

	/**
	 * Reemplazar el Vecino de una publicacion
	 *
	 * @param publicacionId  el id de la publicacion que se quiere actualizar.
	 * @param vecinoId El id del nuevo Vecino asociado al post.
	 * @return el nuevo vecino asociado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public VecinoEntity replaceVecino(Long publicacionId, Long vecinoId) throws EntityNotFoundException {
		log.info("Start the process of updating the neighbor of the post with id = {0}", publicacionId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);

		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if (publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);

		publicacionEntity.get().setVecino(vecinoEntity.get());
		log.info("End process of associating the deal with id = {0} to the post with id = " + publicacionId, vecinoId);
		return vecinoEntity.get();
	}

	/**
	 * Borrar la publicacion de un vecino
	 *
	 * @param publicacionId La publicacion que se desea borrar del Vecino.
	 * @throws EntityNotFoundException si la publicacion no tiene Vecino
	 */

	@Transactional
	public void removeVecino(Long publicacionId) throws EntityNotFoundException {
		log.info("Start the process of deleting the neighbor from the post with id = {0}", publicacionId);
		Optional<PublicacionEntity> publicacionEntity = publicacionRepository.findById(publicacionId);
		if (publicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);

		if (publicacionEntity.get().getVecino() != null) {
			throw new EntityNotFoundException("The post has no associated neighbor");
		}
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(publicacionEntity.get().getVecino().getId());

		vecinoEntity.ifPresent(vecino -> {
			publicacionEntity.get().setVecino(null);
			vecino.getPublicaciones().remove(publicacionEntity.get());
            
		});

		log.info("Finish process of deleting the deal from the post with id = " + publicacionId);
	}
}
