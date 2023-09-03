package co.edu.uniandes.dse.vecindarioamigo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.CentroComercialRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;

/**
 * Clase que implementa la conexión con la persistencia para la relación entre
 * la entidad Vecindario y CentroComercial.
 *
 * @author ISIS2603
 */
@Slf4j
@Service
public class VecindarioCentroComercialService {

    //dependency injections of the two entities of the relation
    @Autowired
	private CentroComercialRepository centroComercialRepository;

    @Autowired
	private VecindarioRepository vecindarioRepository;

    /**
	 * Agregar un centro comercial al vecindario
	 *
	 * @param centroComercialId     El id del centro comercial a guardar
	 * @param vecindarioId      El id del vecindario en el cual se va a guardar el cc.
	 * @return El centro comercial creado.
	 * @throws EntityNotFoundException 
	 */
	
	@Transactional
	public CentroComercialEntity addCentroComercial(Long centroComercialId, Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of adding a shopping center to the neighborhood with id = {0}", vecindarioId);
		
		Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialId);
		if(centroComercialEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);
		
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		centroComercialEntity.get().setVecindario(vecindarioEntity.get());
		log.info("Finish process of adding a shopping center to the neighborhood with id = {0}", vecindarioId);
		return centroComercialEntity.get();
	}
    
    /**
	 * Retorna todos los centros comerciales asociados a un vecindario
	 *
	 * @param vecindarioId El ID del vecindario buscado
	 * @return La lista de centros comerciales del vecindario
	 * @throws EntityNotFoundException si el vecindario no existe
	 */
	@Transactional
	public List<CentroComercialEntity> getCentrosComerciales(Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of consulting the shopping centers associated with the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		return vecindarioEntity.get().getCentrosComerciales();
	}

    /**
	 * Retorna un centro comercial asociado a un vecindario
	 *
	 * @param vecindarioId El id del vecindario a buscar.
	 * @param centroComercialId      El id del centro comercial a buscar
	 * @return El centro comercial encontrado dentro del vecindario.
	 * @throws EntityNotFoundException Si el centro comercial no se encuentra en el vecindario
	 * @throws IllegalOperationException Si el centro comercial no está asociado a el vecindario
	 */
	@Transactional
	public CentroComercialEntity getCentroComercial(Long vecindarioId, Long centroComercialId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start the process of consulting the shopping center with id = {0} from the neighborhood with id = " + vecindarioId, centroComercialId);
		
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialId);
		if(centroComercialEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);
				
		log.info("The process of consulting the shopping center with id ends = {0} from the neighborhood with id = " + vecindarioId, centroComercialId);
		
		if(!vecindarioEntity.get().getCentrosComerciales().contains(centroComercialEntity.get()))
			throw new IllegalOperationException("The shopping center is not associated to the neighborhood");
		
		return centroComercialEntity.get();
	}

    /**
	 * Remplazar centros comerciales de un vecindario
	 *
	 * @param CentrosComerciales Lista de centros comerciales que serán los de un vecindario.
	 * @param vecindarioId El id del vecindario que se quiere actualizar.
	 * @return La lista de centros comerciales actualizada.
	 * @throws EntityNotFoundException Si el vecindario o un centro comercial de la lista no se encuentran
	 */
	@Transactional
	public List<CentroComercialEntity> replaceCentrosComerciales(Long vecindarioId, List<CentroComercialEntity> CentrosComerciales) throws EntityNotFoundException {
		log.info("Start process of updating the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		for(CentroComercialEntity CentroComercial : CentrosComerciales) {
			Optional<CentroComercialEntity> c = centroComercialRepository.findById(CentroComercial.getId());
			if(c.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);
			
			c.get().setVecindario(vecindarioEntity.get());
		}		
		return CentrosComerciales;
	}
}
