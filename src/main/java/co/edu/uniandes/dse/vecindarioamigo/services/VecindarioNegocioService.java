package co.edu.uniandes.dse.vecindarioamigo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

/**
 * Clase que implementa la conexión con la persistencia para la relación entre
 * la entidad Vecindario y Negocio.
 *
 * @author ISIS2603
 */
@Slf4j
@Service
public class VecindarioNegocioService {
    

    //dependency injections of the two entities of the relation
    @Autowired
	private NegocioRepository negocioRepository;

    @Autowired
	private VecindarioRepository vecindarioRepository;

    /**
	 * Agregar un negocio al vecindario
	 *
	 * @param negocioId     El id del negocio a guardar
	 * @param vecindarioId      El id del vecindario en el cual se va a guardar el negocio.
	 * @return El negocio creado.
	 * @throws EntityNotFoundException 
	 */
	
	@Transactional
	public NegocioEntity addNegocio(Long negocioId, Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of adding a business to the neighborhood with id = {0}", vecindarioId);
		
		Optional<NegocioEntity> negocioEntity = negocioRepository.findById(negocioId);
		if(negocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);
		
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		negocioEntity.get().setVecindario(vecindarioEntity.get());
		log.info("Finish process of adding a business to the neighborhood with id = {0}", vecindarioId);
		return negocioEntity.get();
	}
    
    /**
	 * Retorna todos los negocios asociados a un vecindario
	 *
	 * @param vecindarioId El ID del vecindario buscado
	 * @return La lista de negocios del vecindario
	 * @throws EntityNotFoundException si el vecindario no existe
	 */
	@Transactional
	public List<NegocioEntity> getNegocios(Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of consulting the businesses associated with the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		return vecindarioEntity.get().getNegocios();
	}

    /**
	 * Retorna un negocio asociado a un vecindario
	 *
	 * @param vecindarioId El id del vecindario a buscar.
	 * @param negocioId El id del negocio a buscar
	 * @return El negocio encontrado dentro del vecindario.
	 * @throws EntityNotFoundException Si el negocio no se encuentra en el vecindario
	 * @throws IllegalOperationException Si el negocio no está asociado a el vecindario
	 */
	@Transactional
	public NegocioEntity getNegocio(Long vecindarioId, Long negocioId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start the process of consulting the business with id = {0} from the neighborhood with id = " + vecindarioId, negocioId);
		
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		Optional<NegocioEntity> negocioEntity = negocioRepository.findById(negocioId);
		if(negocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);
				
		log.info("The process of consulting the business with id ends = {0} from the neighborhood with id = " + vecindarioId, negocioId);
		
		if(!vecindarioEntity.get().getNegocios().contains(negocioEntity.get()))
			throw new IllegalOperationException("The business is not associated to the neighborhood");
		
		return negocioEntity.get();
	}

    /**
	 * Remplazar negocios de un vecindario
	 *
	 * @param Negocios Lista de negocios que serán los de un vecindario.
	 * @param vecindarioId El id del vecindario que se quiere actualizar.
	 * @return La lista de negocios actualizada.
	 * @throws EntityNotFoundException Si el vecindario o un negocio de la lista no se encuentran
	 */
	@Transactional
	public List<NegocioEntity> replaceNegocios(Long vecindarioId, List<NegocioEntity> Negocios) throws EntityNotFoundException {
		log.info("Start process of updating the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		for(NegocioEntity Negocio : Negocios) {
			Optional<NegocioEntity> n = negocioRepository.findById(Negocio.getId());
			if(n.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.BUSINESS_NOT_FOUND);
			
			n.get().setVecindario(vecindarioEntity.get());
		}		
		return Negocios;
	}
}
