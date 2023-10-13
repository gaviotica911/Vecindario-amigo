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

public class ComentarioCentroComercialService {
    @Autowired
	private ComentarioRepository comentarioRepository;
    @Autowired
    private CentroComercialRepository centroComercialRepository;



    /**
	 * Agregar un centroComercial a una oferta
	 *
	 * @param comentarioId  El id oferta a guardar
	 * @param centroComercialId El id del centroComercial al cual se le va a guardar la oferta.
	 * @return La oferta que fue agregada al centroComercial.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public CentroComercialEntity addCentroComercial(Long centroComercialId, Long comentarioId) throws EntityNotFoundException {
		log.info("Starts the process of associating the deal with id = {0} to the offer with id = " + comentarioId, centroComercialId);
		Optional<CentroComercialEntity> CentroComercialEntity = centroComercialRepository.findById(centroComercialId);
		if (CentroComercialEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CENTROCOMERCIAL_NOT_FOUND);

		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		ComentarioEntity.get().setCentroComercial(CentroComercialEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = {1}", centroComercialId, comentarioId);
		return CentroComercialEntity.get();
	}

	/**
	 *
	 * Obtener un centroComercial por medio del id de la oferta.
	 *
	 * @param comentarioId id de la oferta a ser buscada.
	 * @return el centroComercial solicitado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public CentroComercialEntity getCentroComercial(Long comentarioId) throws EntityNotFoundException {
		log.info("Start the process of consulting the business of the offer with id = {0}", comentarioId);
		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		CentroComercialEntity CentroComercialEntity = ComentarioEntity.get().getCentroComercial();

		if (CentroComercialEntity == null)
			throw new EntityNotFoundException("The business was not found");

		log.info("The process of consulting the business of the offer with id ends = {0}", comentarioId);
		return CentroComercialEntity;
	}

	/**
	 * Reemplazar centroComercial de una oferta
	 *
	 * @param comentarioId  el id de la oferta que se quiere actualizar.
	 * @param centroComercialId El id del nuevo centroComercial asociado al premio.
	 * @return el nuevo autor asociado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public CentroComercialEntity replacecentroComercial(Long comentarioId, Long centroComercialId) throws EntityNotFoundException {
		log.info("Start the process of updating the business of the offer with id = {0}", comentarioId);
		Optional<CentroComercialEntity> CentroComercialEntity = centroComercialRepository.findById(centroComercialId);
		if (CentroComercialEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CENTROCOMERCIAL_NOT_FOUND);

		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		ComentarioEntity.get().setCentroComercial(CentroComercialEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = " + comentarioId, centroComercialId);
		return CentroComercialEntity.get();
	}

	/**
	 * Borrar el centroComercial de una oferta
	 *
	 * @param comentarioId La oferta que se desea borrar del centroComercial.
	 * @throws EntityNotFoundException si el oferta no tiene centroComercial
	 */

	@Transactional
	public void removeCentroComercial(Long comentarioId) throws EntityNotFoundException {
		log.info("Start the process of deleting the business from the offer with id = {0}", comentarioId);
		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);

		if (ComentarioEntity.get().getCentroComercial() == null) {
			throw new EntityNotFoundException("The offer has no associated business");
		}
		Optional<CentroComercialEntity> CentroComercialEntity = centroComercialRepository.findById(ComentarioEntity.get().getCentroComercial().getId());

		CentroComercialEntity.ifPresent(centroComercial -> {
			ComentarioEntity.get().setCentroComercial(null);
			centroComercial.getComentarios().remove(ComentarioEntity.get());
		});

		log.info("Finish process of deleting the deal from the offer with id = " + comentarioId);
	}
}
