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
public class ComentarioNegocioService {
    @Autowired
	private ComentarioRepository comentarioRepository;
    @Autowired
    private NegocioRepository NegocioRepository;



    /**
	 * Agregar un negocio a una oferta
	 *
	 * @param comentarioId  El id oferta a guardar
	 * @param negocioId El id del negocio al cual se le va a guardar la oferta.
	 * @return La oferta que fue agregada al negocio.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public NegocioEntity addNegocio(Long negocioId, Long comentarioId) throws EntityNotFoundException {
		log.info("Starts the process of associating the deal with id = {0} to the offer with id = " + comentarioId, negocioId);
		Optional<NegocioEntity> NegocioEntity = NegocioRepository.findById(negocioId);
		if (NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BUSINESS_NOT_FOUND);

		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		ComentarioEntity.get().setNegocio(NegocioEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = {1}", negocioId, comentarioId);
		return NegocioEntity.get();
	}

	/**
	 *
	 * Obtener un negocio por medio del id de la oferta.
	 *
	 * @param comentarioId id de la oferta a ser buscada.
	 * @return el negocio solicitado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public NegocioEntity getNegocio(Long comentarioId) throws EntityNotFoundException {
		log.info("Start the process of consulting the business of the offer with id = {0}", comentarioId);
		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		NegocioEntity NegocioEntity = ComentarioEntity.get().getNegocio();

		if (NegocioEntity == null)
			throw new EntityNotFoundException("The business was not found");

		log.info("The process of consulting the business of the offer with id ends = {0}", comentarioId);
		return NegocioEntity;
	}

	/**
	 * Reemplazar negocio de una oferta
	 *
	 * @param comentarioId  el id de la oferta que se quiere actualizar.
	 * @param negocioId El id del nuevo negocio asociado al premio.
	 * @return el nuevo autor asociado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public NegocioEntity replaceNegocio(Long comentarioId, Long negocioId) throws EntityNotFoundException {
		log.info("Start the process of updating the business of the offer with id = {0}", comentarioId);
		Optional<NegocioEntity> NegocioEntity = NegocioRepository.findById(negocioId);
		if (NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BUSINESS_NOT_FOUND);

		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		ComentarioEntity.get().setNegocio(NegocioEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = " + comentarioId, negocioId);
		return NegocioEntity.get();
	}

	/**
	 * Borrar el negocio de una oferta
	 *
	 * @param comentarioId La oferta que se desea borrar del negocio.
	 * @throws EntityNotFoundException si el oferta no tiene negocio
	 */

	@Transactional
	public void removenegocio(Long comentarioId) throws EntityNotFoundException {
		log.info("Start the process of deleting the business from the offer with id = {0}", comentarioId);
		Optional<ComentarioEntity> ComentarioEntity = comentarioRepository.findById(comentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		if (ComentarioEntity.get().getNegocio() == null) {
			throw new EntityNotFoundException("The offer has no associated business");
		}
		Optional<NegocioEntity> NegocioEntity = NegocioRepository.findById(ComentarioEntity.get().getNegocio().getId());

		NegocioEntity.ifPresent(negocio -> {
			ComentarioEntity.get().setNegocio(null);
			negocio.getComentarios().remove(ComentarioEntity.get());
		});

		log.info("Finish process of deleting the deal from the offer with id = " + comentarioId);
	}
}
