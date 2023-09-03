package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.OfertaEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.OfertaRepository;
import lombok.extern.slf4j.Slf4j;
/**
 * Clase que implementa la conexion con la persistencia para la relación entre
 * la entidad Oferta y Negocio
 *
 * @author ISIS2603
 */
@Slf4j
@Service
public class OfertaNegocioService {
    

	@Autowired
	private NegocioRepository negocioRepository;

	@Autowired
	private OfertaRepository ofertaRepository;
	
	/**
	 * Agregar un negocio a una oferta
	 *
	 * @param ofertaId  El id oferta a guardar
	 * @param negocioId El id del negocio al cual se le va a guardar la oferta.
	 * @return La oferta que fue agregada al negocio.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public NegocioEntity addNegocio(Long negocioId, Long ofertaId) throws EntityNotFoundException {
		log.info("Starts the process of associating the deal with id = {0} to the offer with id = " + ofertaId, negocioId);
		Optional<NegocioEntity> negocioEntity = negocioRepository.findById(negocioId);
		if (negocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BUSINESS_NOT_FOUND);

		Optional<OfertaEntity> ofertaEntity = ofertaRepository.findById(ofertaId);
		if (ofertaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		ofertaEntity.get().setNegocio(negocioEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = {1}", negocioId, ofertaId);
		return negocioEntity.get();
	}

	/**
	 *
	 * Obtener un negocio por medio del id de la oferta.
	 *
	 * @param ofertaId id de la oferta a ser buscada.
	 * @return el negocio solicitado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public NegocioEntity getNegocio(Long ofertaId) throws EntityNotFoundException {
		log.info("Start the process of consulting the business of the offer with id = {0}", ofertaId);
		Optional<OfertaEntity> ofertaEntity = ofertaRepository.findById(ofertaId);
		if (ofertaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		NegocioEntity negocioEntity = ofertaEntity.get().getNegocio();

		if (negocioEntity == null)
			throw new EntityNotFoundException("The business was not found");

		log.info("The process of consulting the business of the offer with id ends = {0}", ofertaId);
		return negocioEntity;
	}

	/**
	 * Reemplazar negocio de una oferta
	 *
	 * @param ofertaId  el id de la oferta que se quiere actualizar.
	 * @param negocioId El id del nuevo negocio asociado al premio.
	 * @return el nuevo autor asociado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public NegocioEntity replaceNegocio(Long ofertaId, Long negocioId) throws EntityNotFoundException {
		log.info("Start the process of updating the business of the offer with id = {0}", ofertaId);
		Optional<NegocioEntity> negocioEntity = negocioRepository.findById(negocioId);
		if (negocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.BUSINESS_NOT_FOUND);

		Optional<OfertaEntity> ofertaEntity = ofertaRepository.findById(ofertaId);
		if (ofertaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		ofertaEntity.get().setNegocio(negocioEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = " + ofertaId, negocioId);
		return negocioEntity.get();
	}

	/**
	 * Borrar el negocio de una oferta
	 *
	 * @param ofertaId La oferta que se desea borrar del negocio.
	 * @throws EntityNotFoundException si el oferta no tiene negocio
	 */

	@Transactional
	public void removeNegocio(Long ofertaId) throws EntityNotFoundException {
		log.info("Start the process of deleting the business from the offer with id = {0}", ofertaId);
		Optional<OfertaEntity> ofertaEntity = ofertaRepository.findById(ofertaId);
		if (ofertaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		if (ofertaEntity.get().getNegocio() != null) {
			throw new EntityNotFoundException("The offer has no associated business");
		}
		Optional<NegocioEntity> negocioEntity = negocioRepository.findById(ofertaEntity.get().getNegocio().getId());

		negocioEntity.ifPresent(negocio -> {
			ofertaEntity.get().setNegocio(null);
			negocio.getOfertas().remove(ofertaEntity.get());
		});

		log.info("Finish process of deleting the deal from the offer with id = " + ofertaId);
	}
}
