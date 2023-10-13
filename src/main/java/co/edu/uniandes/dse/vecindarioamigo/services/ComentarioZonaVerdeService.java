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
public class ComentarioZonaVerdeService {
    @Autowired
	private ComentarioRepository comentarioRepository;
    @Autowired
    private Zona_VerdeRepository ZonaVerdeRepository;



    /**
	 * Agregar un zonaVerde a una comentario
	 *
	 * @param comentarioId  El id comentario a guardar
	 * @param zonaVerdeId El id del zonaVerde al cual se le va a guardar la comentario.
	 * @return La comentario que fue agregada al zonaVerde.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public Zona_VerdeEntity addZonaVerde(Long zonaVerdeId, Long comentarioId) throws EntityNotFoundException {
		log.info("Starts the process of associating the deal with id = {0} to the offer with id = " + comentarioId, zonaVerdeId);
		Optional<Zona_VerdeEntity> Zona_VerdeEntity = ZonaVerdeRepository.findById(zonaVerdeId);
		if (Zona_VerdeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ZONA_VERDE_NOT_FOUND);

		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		ComentarioEntity.get().setZonaVerde(Zona_VerdeEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = {1}", zonaVerdeId, comentarioId);
		return Zona_VerdeEntity.get();
	}

	/**
	 *
	 * Obtener un zonaVerde por medio del id de la comentario.
	 *
	 * @param comentarioId id de la comentario a ser buscada.
	 * @return el zonaVerde solicitado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public Zona_VerdeEntity getZonaVerde(Long comentarioId) throws EntityNotFoundException {
		log.info("Start the process of consulting the business of the offer with id = {0}", comentarioId);
		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		Zona_VerdeEntity Zona_VerdeEntity = ComentarioEntity.get().getZonaVerde();

		if (Zona_VerdeEntity == null)
			throw new EntityNotFoundException("The business was not found");

		log.info("The process of consulting the business of the offer with id ends = {0}", comentarioId);
		return Zona_VerdeEntity;
	}

	/**
	 * Reemplazar zonaVerde de una comentario
	 *
	 * @param comentarioId  el id de la comentario que se quiere actualizar.
	 * @param zonaVerdeId El id del nuevo zonaVerde asociado al premio.
	 * @return el nuevo autor asociado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public Zona_VerdeEntity replacezonaVerde(Long comentarioId, Long zonaVerdeId) throws EntityNotFoundException {
		log.info("Start the process of updating the business of the offer with id = {0}", comentarioId);
		Optional<Zona_VerdeEntity> Zona_VerdeEntity = ZonaVerdeRepository.findById(zonaVerdeId);
		if (Zona_VerdeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ZONA_VERDE_NOT_FOUND);

		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		ComentarioEntity.get().setZonaVerde(Zona_VerdeEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = " + comentarioId, zonaVerdeId);
		return Zona_VerdeEntity.get();
	}
	/**
	 * Borrar el zonaVerde de una comentario
	 *
	 * @param comentarioId La comentario que se desea borrar del zonaVerde.
	 * @throws EntityNotFoundException si el comentario no tiene zonaVerde
	 */

	@Transactional
	public void removeZonaVerde(Long comentarioId) throws EntityNotFoundException {
		log.info("Start the process of deleting the business from the offer with id = {0}", comentarioId);
		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		if (ComentarioEntity.get().getZonaVerde() == null) {
			throw new EntityNotFoundException("The offer has no associated business");
		}
		Optional<Zona_VerdeEntity> Zona_VerdeEntity = ZonaVerdeRepository.findById(ComentarioEntity.get().getZonaVerde().getId());

		Zona_VerdeEntity.ifPresent(zonaVerde -> {
			ComentarioEntity.get().setZonaVerde(null);
			zonaVerde.getReviews().remove(ComentarioEntity.get());
		});

		log.info("Finish process of deleting the deal from the offer with id = " + comentarioId);
	}
}
