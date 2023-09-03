package co.edu.uniandes.dse.vecindarioamigo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.Zona_VerdeRepository;

/**
 * Clase que implementa la conexión con la persistencia para la relación entre
 * la entidad Vecindario y Zona_Verde.
 *
 * @author ISIS2603
 */
@Slf4j
@Service
public class VecindarioZona_VerdeService {
    
    

    //dependency injections of the two entities of the relation
    @Autowired
	private Zona_VerdeRepository zona_VerdeRepository;

    @Autowired
	private VecindarioRepository vecindarioRepository;

    /**
	 * Agregar un negocio al vecindario
	 *
	 * @param zona_VerdeId     El id de la zona verde a guardar
	 * @param vecindarioId      El id del vecindario en el cual se va a guardar el negocio.
	 * @return La zona verde creada.
	 * @throws EntityNotFoundException 
	 */
	
	@Transactional
	public Zona_VerdeEntity addZona_Verde(Long zona_VerdeId, Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of adding a green zone to the neighborhood with id = {0}", vecindarioId);
		
		Optional<Zona_VerdeEntity> zona_VerdeEntity = zona_VerdeRepository.findById(zona_VerdeId);
		if(zona_VerdeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GREEN_ZONE_NOT_FOUND);
		
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		zona_VerdeEntity.get().setVecindario(vecindarioEntity.get());
		log.info("Finish process of adding a green zone to the neighborhood with id = {0}", vecindarioId);
		return zona_VerdeEntity.get();
	}
    
    /**
	 * Retorna todas las zonas verdes asociados a un vecindario
	 *
	 * @param vecindarioId El ID del vecindario buscado
	 * @return La lista de zonas verdes del vecindario
	 * @throws EntityNotFoundException si el vecindario no existe
	 */
	@Transactional
	public List<Zona_VerdeEntity> getZonas_Verdes(Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of consulting the green zones associated with the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		return vecindarioEntity.get().getZonasVerdes();
	}

    /**
	 * Retorna una zona verde asociada a un vecindario
	 *
	 * @param vecindarioId El id del vecindario a buscar.
	 * @param zona_VerdeId El id de la zona verde a buscar
	 * @return La zona verde encontrada dentro del vecindario.
	 * @throws EntityNotFoundException Si la zona verde no se encuentra en el vecindario
	 * @throws IllegalOperationException Si la zona verde no está asociado a el vecindario
	 */
	@Transactional
	public Zona_VerdeEntity getZona_Verde(Long vecindarioId, Long zona_VerdeId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start the process of consulting the green zone with id = {0} from the neighborhood with id = " + vecindarioId, zona_VerdeId);
		
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		Optional<Zona_VerdeEntity> zona_VerdeEntity = zona_VerdeRepository.findById(zona_VerdeId);
		if(zona_VerdeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GREEN_ZONE_NOT_FOUND);
				
		log.info("The process of consulting the green zone with id ends = {0} from the neighborhood with id = " + vecindarioId, zona_VerdeId);
		
		if(!vecindarioEntity.get().getZonasVerdes().contains(zona_VerdeEntity.get()))
			throw new IllegalOperationException("The green zone is not associated to the neighborhood");
		
		return zona_VerdeEntity.get();
	}

    /**
	 * Remplazar zonas verdes de un vecindario
	 *
	 * @param Zonas_Verdes Lista de zonas verdes que serán las de un vecindario.
	 * @param vecindarioId El id del vecindario que se quiere actualizar.
	 * @return La lista de zonas verdes actualizada.
	 * @throws EntityNotFoundException Si el vecindario o una zona verde de la lista no se encuentran
	 */
	@Transactional
	public List<Zona_VerdeEntity> replaceZonas_Verdes(Long vecindarioId, List<Zona_VerdeEntity> Zonas_Verdes) throws EntityNotFoundException {
		log.info("Start process of updating the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if(vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		
		for(Zona_VerdeEntity Zona_Verde : Zonas_Verdes) {
			Optional<Zona_VerdeEntity> z = zona_VerdeRepository.findById(Zona_Verde.getId());
			if(z.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.GREEN_ZONE_NOT_FOUND);
			
			z.get().setVecindario(vecindarioEntity.get());
		}		
		return Zonas_Verdes;
	}
}
