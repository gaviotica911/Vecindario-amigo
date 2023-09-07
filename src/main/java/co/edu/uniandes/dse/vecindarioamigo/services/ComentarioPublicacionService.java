package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.repositories.*;
import co.edu.uniandes.dse.vecindarioamigo.entities.*;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ComentarioPublicacionService {
    @Autowired
	private ComentarioRepository comentarioRepository;
    @Autowired
    private PublicacionRepository PublicacionRepository;



    /**
	 * Agregar un publicacion a una publicacion
	 *
	 * @param comentarioId  El id publicacion a guardar
	 * @param publicacionId El id del publicacion al cual se le va a guardar la publicacion.
	 * @return La publicacion que fue agregada al publicacion.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public PublicacionEntity addpublicacion(Long publicacionId, Long comentarioId) throws EntityNotFoundException {
		log.info("Starts the process of associating the deal with id = {0} to the offer with id = " + comentarioId, publicacionId);
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(publicacionId);
		if (PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);

		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		ComentarioEntity.get().setPublicacion(PublicacionEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = {1}", publicacionId, comentarioId);
		return PublicacionEntity.get();
	}

	/**
	 *
	 * Obtener un publicacion por medio del id de la publicacion.
	 *
	 * @param comentarioId id de la publicacion a ser buscada.
	 * @return el publicacion solicitado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public PublicacionEntity getPublicacion(Long comentarioId) throws EntityNotFoundException {
		log.info("Start the process of consulting the business of the offer with id = {0}", comentarioId);
		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		PublicacionEntity PublicacionEntity = ComentarioEntity.get().getPublicacion();

		if (PublicacionEntity == null)
			throw new EntityNotFoundException("The business was not found");

		log.info("The process of consulting the business of the offer with id ends = {0}", comentarioId);
		return PublicacionEntity;
	}

	/**
	 * Reemplazar publicacion de una publicacion
	 *
	 * @param comentarioId  el id de la publicacion que se quiere actualizar.
	 * @param publicacionId El id del nuevo publicacion asociado al premio.
	 * @return el nuevo autor asociado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public PublicacionEntity replacepublicacion(Long comentarioId, Long publicacionId) throws EntityNotFoundException {
		log.info("Start the process of updating the business of the offer with id = {0}", comentarioId);
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(publicacionId);
		if (PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);

		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		ComentarioEntity.get().setPublicacion(PublicacionEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = " + comentarioId, publicacionId);
		return PublicacionEntity.get();
	}

	/**
	 * Borrar el publicacion de una publicacion
	 *
	 * @param comentarioId La publicacion que se desea borrar del publicacion.
	 * @throws EntityNotFoundException si el publicacion no tiene publicacion
	 */

	@Transactional
	public void removepublicacion(Long comentarioId) throws EntityNotFoundException {
		log.info("Start the process of deleting the business from the offer with id = {0}", comentarioId);
		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		if (ComentarioEntity.get().getPublicacion() == null) {
			throw new EntityNotFoundException("The offer has no associated business");
		}
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(ComentarioEntity.get().getPublicacion().getId());

		PublicacionEntity.ifPresent(publicacion -> {
			ComentarioEntity.get().setPublicacion(null);
			publicacion.getComentarios().remove(ComentarioEntity.get());
		});

		log.info("Finish process of deleting the deal from the offer with id = " + comentarioId);
	}   
}
