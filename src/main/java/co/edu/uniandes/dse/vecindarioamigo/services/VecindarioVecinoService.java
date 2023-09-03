package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecinoRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexión con la persistencia para la relación entre
 * la entidad Vecindario y Vecino.
 *
 * @author ISIS2603
 */
@Slf4j
@Service
public class VecindarioVecinoService {
    
    

    //dependency injections of the two entities of the relation
    @Autowired
	private VecinoRepository vecinoRepository;

    @Autowired
	private VecindarioRepository vecindarioRepository;

    /**
	 * Agregar un vecino al vecindario
	 *
	 * @param vecinoId     El id del vecino a guardar
	 * @param vecindarioId      El id del vecindario en el cual se va a guardar el vecino.
	 * @return El vecino creado.
	 * @throws EntityNotFoundException 
	 */
	
	@Transactional
	public VecinoEntity addVecino(Long vecinoId, Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of adding a neighbor to the neighborhood with id = {0}", vecindarioId);
		
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if(vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBOR_NOT_FOUND);
		
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		vecinoEntity.get().setVecindario(vecindarioEntity.get());
		log.info("Finish process of adding a neighbor to the neighborhood with id = {0}", vecindarioId);
		return vecinoEntity.get();
	}
    
    /**
	 * Retorna todos los vecinos asociados a un vecindario
	 *
	 * @param vecindarioId El ID del vecindario buscado
	 * @return La lista de vecinos del vecindario
	 * @throws EntityNotFoundException si el vecindario no existe
	 */
	@Transactional
	public List<VecinoEntity> getVecinos(Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of consulting the neighbors associated with the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		return vecindarioEntity.get().getVecinos();
	}

    /**
	 * Retorna un vecino asociado a un vecindario
	 *
	 * @param vecindarioId El id del vecindario a buscar.
	 * @param vecinoId El id del vecino a buscar
	 * @return El vecino encontrado dentro del vecindario.
	 * @throws EntityNotFoundException Si el vecino no se encuentra en el vecindario
	 * @throws IllegalOperationException Si el vecino no está asociado a el vecindario
	 */
	@Transactional
	public VecinoEntity getVecino(Long vecindarioId, Long vecinoId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start the process of consulting the neighbor with id = {0} from the neighborhood with id = " + vecindarioId, vecinoId);
		
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if(vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBOR_NOT_FOUND);
				
		log.info("The process of consulting the neighbor with id ends = {0} from the neighborhood with id = " + vecindarioId, vecinoId);
		
		if(!vecindarioEntity.get().getVecinos().contains(vecinoEntity.get()))
			throw new IllegalOperationException("The neighbor is not associated to the neighborhood");
		
		return vecinoEntity.get();
	}

    /**
	 * Remplazar vecinos de un vecindario
	 *
	 * @param Vecinos Lista de vecinos que serán los de un vecindario.
	 * @param vecindarioId El id del vecindario que se quiere actualizar.
	 * @return La lista de vecinos actualizada.
	 * @throws EntityNotFoundException Si el vecindario o un vecino de la lista no se encuentran
	 */
	@Transactional
	public List<VecinoEntity> replaceVecinos(Long vecindarioId, List<VecinoEntity> Vecinos) throws EntityNotFoundException {
		log.info("Start process of updating the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		for(VecinoEntity Vecino : Vecinos) {
			Optional<VecinoEntity> v = vecinoRepository.findById(Vecino.getId());
			if(v.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.NEIGHBOR_NOT_FOUND);
			
			v.get().setVecindario(vecindarioEntity.get());
		}		
		return Vecinos;
	}
}
