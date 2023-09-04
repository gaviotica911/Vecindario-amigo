package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecinoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VecinoService {
     @Autowired  
    VecinoRepository vecinoRepository;

	/**
	 * Crea un vecino en la persistencia.
	 *
	 * @param vecinoEntity La entidad que representa el vecino a persistir.
	 * @return La entidad del vecino luego de persistirla.
	 * @throws IllegalOperationException Si el vecino a persistir ya existe.
	 */

	@Transactional 

	public VecinoEntity createVecino(VecinoEntity vecinoEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("The neighbor creation process begins");
		if (!vecinoRepository.findById(vecinoEntity.getId()).isEmpty()) {
			throw new IllegalOperationException("neighbor name already exists");
		}
		log.info("End of neighbor creation process");
		return vecinoRepository.save(vecinoEntity);
		}
	
	/**
	 *
	 * Obtener todas los vecinos existentes en la base de datos.
	 *
	 * @return una lista de vecinos.
	 */
	@Transactional
	public List<VecinoEntity> getVecinos() {
		log.info("The process of consulting all the neighbors begins");
		return vecinoRepository.findAll();
	}

	/**
	 *
	 * Obtener un vecino por medio de su id.
	 *
	 * @param vecinoId: id del vecino para ser buscado.
	 * @return el vecino solicitado por medio de su id.
	 */
	@Transactional
	public VecinoEntity getVecino(Long vecinoId) throws EntityNotFoundException {
		log.info("Start the process of consulting the neighbor with id = {0}", vecinoId);
		Optional<VecinoEntity> vecino = vecinoRepository.findById(vecinoId);
		if (vecino.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);
		log.info("End process of consulting the neighbot with id = {0}", vecinoId);
		return vecino.get();
	}

	/**
	 *
	 * Actualizar un vecino.
	 *
	 * @param vecinoId:    id del vecino para buscarlo en la base de datos.
	 * @param vecino: vecino con los cambios para ser actualizado.
	 * @return el vecino con los cambios actualizados en la base de datos.
	 */
	@Transactional
	public VecinoEntity updateVecino(Long vecinoId, VecinoEntity vecino) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start process of updating the neighbor with id = {0}", vecinoId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);

		vecino.setId(vecinoId);
		log.info("Finish process of updating the neighborhood with id = {0}", vecinoId);
		return vecinoRepository.save(vecino);
	}

	/**
	 * Borrar un vecino
	 *
	 * @param vecinoId: id del vecino a borrar
	 * @throws BusinessLogicException Si el vecino a eliminar tiene:
	 *  								-publicaciones 
	 */
	@Transactional
	public void deleteVecino(Long vecinoId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Start process of deleting the neighbor with id = {0}", vecinoId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);

		
		log.info("End process of checking the neighbor constraints with id = {0}", vecinoId);

		vecinoRepository.deleteById(vecinoId);
		log.info("End process of deleting the neighbor with id = {0}", vecinoId);
	}
    
}
