package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.OfertaEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.OfertaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OfertaService {
    
    
    @Autowired //used to automatically inject JPA-related components
	//(such as repositories) into Spring-managed beans, simplifying
	//dependency management in your application
    OfertaRepository ofertaRepository;

	/**
	 * Crea un vecindario en la persistencia.
	 *
	 * @param OfertaEntity La entidad que representa la oferta a persistir.
	 * @return La entidad de la oferta luego de persistirla.
	 * @throws IllegalOperationException Si la oferta a persistir ya existe.
	 */

	@Transactional //ensures that if an exception is thrown within the method,
	//the transaction will be rolled back to its initial state, preventing partial
	//updates to the database

	public OfertaEntity createOffer(OfertaEntity ofertaEntity) throws IllegalOperationException {
		log.info("The offer creation process begins");
		if (ofertaEntity.getId() != null && !ofertaRepository.findById(ofertaEntity.getId()).isEmpty()) {
			throw new IllegalOperationException("Offer id already exists");
		}
		log.info("End of offer creation process");
		return ofertaRepository.save(ofertaEntity);
		}
	
	/**
	 *
	 * Obtener todas las ofertas existentes en la base de datos.
	 *
	 * @return una lista de ofertas.
	 */
	@Transactional
	public List<OfertaEntity> getOfertas() {
		log.info("The process of consulting all the offers begins");
		return ofertaRepository.findAll();
	}

	/**
	 *
	 * Obtener una oferta por medio de su id.
	 *
	 * @param ofertaId: id de la oferta para ser buscada.
	 * @return ela oferta solicitada por medio de su id.
	 */
	@Transactional
	public OfertaEntity getOferta(Long ofertaId) throws EntityNotFoundException {
		log.info("Start the process of consulting the offer with id = {0}", ofertaId);
		Optional<OfertaEntity> oferta = ofertaRepository.findById(ofertaId);
		if (oferta.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);
		log.info("End process of consulting the offer with id = {0}", ofertaId);
		return oferta.get();
	}

	/**
	 *
	 * Actualizar una oferta.
	 *
	 * @param ofertaId:    id de la oferta para buscarla en la base de datos.
	 * @param oferta: oferta con los cambios para ser actualizada.
	 * @return la oferta con los cambios actualizados en la base de datos.
	 */
	@Transactional
	public OfertaEntity updateOferta(Long ofertaId, OfertaEntity oferta) throws EntityNotFoundException {
		log.info("Start process of updating the offer with id = {0}", ofertaId);
		Optional<OfertaEntity> OfertaEntity = ofertaRepository.findById(ofertaId);
		if (OfertaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		oferta.setId(ofertaId);
		log.info("Finish process of updating the neighborhood with id = {0}", ofertaId);
		return ofertaRepository.save(oferta);
	}

	/**
	 * Borrar una oferta
	 *
	 * @param ofertaId: id de la oferta a borrar
	 * @throws BusinessLogicException Si la oferta a eliminar tiene:
	 *  								-negocio asociado
	 */
	@Transactional
	public void deleteOferta(Long ofertaId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start process of deleting the offer with id = {0}", ofertaId);
		Optional<OfertaEntity> OfertaEntity = ofertaRepository.findById(ofertaId);
		if (OfertaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFFER_NOT_FOUND);

		//checking business logic constraints
			//checking no relation with business
			NegocioEntity business = OfertaEntity.get().getNegocio();
			if (business != null) {
				throw new IllegalOperationException(
					"Unable to delete 'offer' because it has associated business");
			}
		log.info("End process of checking the offer constraints with id = {0}", ofertaId);

		ofertaRepository.deleteById(ofertaId);
		log.info("End process of deleting the offer with id = {0}", ofertaId);
	}

    
}
