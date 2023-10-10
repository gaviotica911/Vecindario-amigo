package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.GruposDeInteresEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecinoRepository;
import lombok.extern.slf4j.Slf4j;
import co.edu.uniandes.dse.vecindarioamigo.repositories.GrupoDeInteresRepository;


@Slf4j
@Service
public class VecinoGrupoDeInteresService {
    
    //dependency injections of the two entities of the relation
    @Autowired
	private GrupoDeInteresRepository grupoDeInteresRepository;

    @Autowired
	private VecinoRepository vecinoRepository;

    /**
	 * Agregar un grupoDeInteres al vecino
	 *
	 * @param grupoDeInteresId     El id del grupoDeInteres a guardar
	 * @param vecinoId      El id del vecino en el cual se va a guardar el grupoDeInteres.
	 * @return El grupoDeInteres creado.
	 * @throws EntityNotFoundException 
     * @throws IllegalOperationException
	 */
	
	@Transactional
	public GruposDeInteresEntity addGrupoDeInteres(Long grupoDeInteresId, Long vecinoId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start the process of adding a neighbor to the neighborhood with id = {0}", vecinoId);
		if (grupoDeInteresId ==null)
		throw new IllegalOperationException("Id null");

	

		Optional<GruposDeInteresEntity> gruposDeInteresEntity = grupoDeInteresRepository.findById(grupoDeInteresId);
		if (gruposDeInteresEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);
		
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);

		if (vecinoEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);
			
		if ((vecinoEntity.get()).getGruposDeInteres().contains(gruposDeInteresEntity.get()))
			throw new IllegalOperationException("el vecino ya hace parte de este grupo");
		
		vecinoEntity.get().getGruposDeInteres().add(gruposDeInteresEntity.get());
	
		log.info("Finish process of adding a interest group to a neighbor with id = {0}", vecinoId);
		return gruposDeInteresEntity.get();
	}
    
    /**
	 * Retorna todos los grupoDeInteress asociados a un vecino
	 *
	 * @param vecinoId El ID del vecino buscado
	 * @return La lista de grupoDeInteress del vecino
	 * @throws EntityNotFoundException si el vecino no existe
	 */
	@Transactional
	public List<GruposDeInteresEntity> getGruposDeInteres(Long vecinoId) throws EntityNotFoundException {
		log.info("Start the process of consulting the groups associated with the neighbor with id = {0}", vecinoId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if(vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);
		
		return vecinoEntity.get().getGruposDeInteres();
	}

    /**
	 * Retorna un grupoDeInteres asociado a un vecino
	 *
	 * @param vecinoId El id del vecino a buscar.
	 * @param grupoDeInteresId El id del grupoDeInteres a buscar
	 * @return El grupoDeInteres encontrado dentro del vecino.
	 * @throws EntityNotFoundException Si el grupoDeInteres no se encuentra en el vecino
	 * @throws IllegalOperationException Si el grupoDeInteres no está asociado a el vecino
	 */
	@Transactional
	public GruposDeInteresEntity getGrupoDeInteres(Long vecinoId, Long grupoDeInteresId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start the process of consulting the group with id = {0} from the neighbor with id = " +  grupoDeInteresId,vecinoId);
		
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if(vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);
		
		Optional<GruposDeInteresEntity> gruposDeInteresEntity = grupoDeInteresRepository.findById(grupoDeInteresId);
		if(gruposDeInteresEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);
				
		log.info("The process of consulting the group with id ends = {0} from the neighbor with id = " + grupoDeInteresId, vecinoId);
		
		if(!vecinoEntity.get().getGruposDeInteres().contains(gruposDeInteresEntity.get()))
			throw new IllegalOperationException("The group is not associated to the neighbor");
		
		return gruposDeInteresEntity.get();
	}

    /**
	 * Remplazar los grupos de interes de un vecino
	 *
	 * @param grupoDeInteresId Lista de grupoDeInteress que serán los de un vecino.
	 * @param vecinoId El id del vecino que se quiere actualizar.
	 * @return La lista de grupoDeInteress actualizada.
	 * @throws EntityNotFoundException Si el vecino o un grupoDeInteres de la lista no se encuentran
     * @throws IllegalOperationException
	 */
	@Transactional
	public List<GruposDeInteresEntity> replaceGrupoDeInteres(Long vecinoId, List<GruposDeInteresEntity> gruposDeInteres) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start process of updating the group with id = {0}", vecinoId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if(vecinoEntity.isEmpty() )
			throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);
		if(gruposDeInteres== null )
			throw new IllegalOperationException("Lista invalida");
		
		for(GruposDeInteresEntity grupoDeInteres : gruposDeInteres) {
			Optional<GruposDeInteresEntity> gruposDeInteresEntity = grupoDeInteresRepository.findById(grupoDeInteres.getId());
			if (gruposDeInteresEntity.isEmpty() )
				throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);

			if (!gruposDeInteresEntity.get().getVecinos().contains(vecinoEntity.get()))
				gruposDeInteresEntity.get().getVecinos().add(vecinoEntity.get());
		}		
		log.info("Finaliza proceso de reemplazar los grupos asociados al vecino con id = {0}", vecinoId);
		vecinoEntity.get().setGruposDeInteres(gruposDeInteres);
		return vecinoEntity.get().getGruposDeInteres();
	}
	@Transactional
    public void removeGrupoDeInteres(Long vecinoId, Long grupoId) throws EntityNotFoundException {
            log.info("Inicia proceso de borrar un grupo del vecino con id = {0}", vecinoId);
            Optional<GruposDeInteresEntity> grupoEntity = grupoDeInteresRepository.findById(grupoId);
            Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);

            if (grupoEntity.isEmpty())
                throw new EntityNotFoundException("No se ha encontrado el grupo con ese id.");

            if (vecinoEntity.isEmpty())
                    throw new EntityNotFoundException("No se ha encontrado el vecino con ese id.");

            vecinoEntity.get().getGruposDeInteres().remove(grupoEntity.get());

            log.info("Termina proceso de borrar un grupo de interes  del vecino con id = {0}", vecinoId);
    }

	@Transactional
	public List<GruposDeInteresEntity> addGruposDeInteres(Long authorId, List<GruposDeInteresEntity> books) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los libros asociados al author con id = {0}", authorId);
		Optional<VecinoEntity> authorEntity = vecinoRepository.findById(authorId);
		if (authorEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);

		for (GruposDeInteresEntity book : books) {
			Optional<GruposDeInteresEntity> bookEntity = grupoDeInteresRepository.findById(book.getId());
			if (bookEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);

			if (!bookEntity.get().getVecinos().contains(authorEntity.get()))
				bookEntity.get().getVecinos().add(authorEntity.get());
		}
		log.info("Finaliza proceso de reemplazar los libros asociados al author con id = {0}", authorId);
		authorEntity.get().setGruposDeInteres(books);
		return authorEntity.get().getGruposDeInteres();
	}
}
