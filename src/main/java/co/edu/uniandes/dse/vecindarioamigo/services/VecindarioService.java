package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;


/**
 * Pruebas de logica de Authors
 *
 * @author ISIS2603
 */

@Slf4j
@Service
public class VecindarioService {
    
    @Autowired //used to automatically inject JPA-related components
	//(such as repositories) into Spring-managed beans, simplifying
	//dependency management in your application
    VecindarioRepository vecindarioRepository;

	/**
	 * Crea un vecindario en la persistencia.
	 *
	 * @param VecindarioEntity La entidad que representa el vecindario a persistir.
	 * @return La entidad del vecindario luego de persistirla.
	 * @throws IllegalOperationException Si el vecindario a persistir ya existe.
	 */

	@Transactional //ensures that if an exception is thrown within the method,
	//the transaction will be rolled back to its initial state, preventing partial
	//updates to the database

	public VecindarioEntity createVecindario(VecindarioEntity vecindarioEntity) throws IllegalOperationException {
		log.info("The neighborhood creation process begins");
		if (!vecindarioRepository.findByNombre(vecindarioEntity.getNombre()).isEmpty()) {
			throw new IllegalOperationException("Neighborhood name already exists");
		}
		log.info("End of neighborhood creation process");
		return vecindarioRepository.save(vecindarioEntity);
		}
	
	/**
	 *
	 * Obtener todas los vecindarios existentes en la base de datos.
	 *
	 * @return una lista de vecindarios.
	 */
	@Transactional
	public List<VecindarioEntity> getVecindarios() {
		log.info("The process of consulting all the neighborhoods begins");
		return vecindarioRepository.findAll();
	}

	/**
	 *
	 * Obtener un vecindario por medio de su id.
	 *
	 * @param vecindarioId: id del vecindario para ser buscado.
	 * @return el vecindario solicitado por medio de su id.
	 */
	@Transactional
	public VecindarioEntity getVecindario(Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of consulting the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindario = vecindarioRepository.findById(vecindarioId);
		if (vecindario.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINDARIO_NOT_FOUND);
		log.info("End process of consulting the neighborhood with id = {0}", vecindarioId);
		return vecindario.get();
	}

	/**
	 *
	 * Actualizar un vecindario.
	 *
	 * @param vecindarioId:    id del vecindario para buscarlo en la base de datos.
	 * @param vecindario: vecindario con los cambios para ser actualizado.
	 * @return el vecindario con los cambios actualizados en la base de datos.
	 */
	@Transactional
	public VecindarioEntity updateVecindario(Long vecindarioId, VecindarioEntity vecindario) throws EntityNotFoundException {
		log.info("Start process of updating the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if (vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINDARIO_NOT_FOUND);

		vecindario.setId(vecindarioId);
		log.info("Finish process of updating the neighborhood with id = {0}", vecindarioId);
		return vecindarioRepository.save(vecindario);
	}

	/**
	 * Borrar un vecindario
	 *
	 * @param vecindarioId: id del vecindario a borrar
	 * @throws BusinessLogicException Si el vecindario a eliminar tiene:
	 *  								-centros comerciales
	 * 									-zonas verdes
	 * 									-negocios.
	 */
	@Transactional
	public void deleteVecindario(Long vecindarioId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start process of deleting the neighborhood with id = {0}", vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if (vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINDARIO_NOT_FOUND);

		//checking business logic constraints
			//checking no relation with malls
			List<CentroComercialEntity> shopping_malls = vecindarioEntity.get().getCentrosComerciales();
			if (!shopping_malls.isEmpty()) {
				throw new IllegalOperationException(
					"Unable to delete 'vecindario' because it has associated shopping_malls");
			}

			//checking no relation with green zones
			List<Zona_VerdeEntity> green_zones = vecindarioEntity.get().getZonasVerdes();
			if (!green_zones.isEmpty()) {
				throw new IllegalOperationException(
					"Unable to delete 'vecindario' because it has associated green zones");
			}	

			//checking no relation with businesses
			List<NegocioEntity> businesses = vecindarioEntity.get().getNegocios();
			if (!businesses.isEmpty()) {
				throw new IllegalOperationException(
					"Unable to delete 'vecindario' because it has associated businesses");
			}	
		log.info("End process of checking the neighborhood constraints with id = {0}", vecindarioId);

		vecindarioRepository.deleteById(vecindarioId);
		log.info("End process of deleting the neighborhood with id = {0}", vecindarioId);
	}

}

	


