package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;

import co.edu.uniandes.dse.vecindarioamigo.repositories.VecinoRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VecinoVecindarioService {
    
	@Autowired
	private VecinoRepository vecinoRepository;

	@Autowired
	private VecindarioRepository vecindarioRepository;
	
	/**
	 * Agregar un vecindario a un vecino
	 *
	 * @param vecinoId  
	 * @param vecindarioId 
	 * @return el vecino que fue agregada al vecindario.
	 * @throws EntityNotFoundException
	 */
	@Transactional
	public VecindarioEntity addVecindario(Long vecindarioId, Long vecinoId) throws EntityNotFoundException {
		log.info("Starts the process of associating the deal with id = {0} to the offer with id = " + vecinoId, vecindarioId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if (vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINDARIO_NOT_FOUND);

		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);

	    vecinoEntity.get().setVecindario(vecindarioEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = {1}", vecindarioId, vecinoId);
		return vecindarioEntity.get();
	}

	/**
	 *
	 * Obtener un vecindario por medio del id del vecino.
	 *
	 * @param vecinoId 
	 * @return el vecindario solicitado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public VecindarioEntity getVecindario(Long vecinoId) throws EntityNotFoundException {
		log.info("Start the process of consulting the neighborhood of the neighbor with id = {0}", vecinoId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);

		VecindarioEntity vecindarioEntity = vecinoEntity.get().getVecindario();

		if (vecindarioEntity == null)
			throw new EntityNotFoundException("The neighborhood was not found");

		log.info("The process of consulting the neighborhood of the offer with id ends = {0}", vecinoId);
		return vecindarioEntity;
	}

	/**
	 * Reemplazar el vecindario de un vecino
	 *
	 * @param vecinoId  el id del vecino que se quiere actualizar.
	 * @param vecindarioId El id del nuevo vecindario asociado al vecino.
	 * @return el nuevo vecindario asociado.
	 * @throws EntityNotFoundException
	 */

	@Transactional
	public VecindarioEntity replaceVecindario(Long vecinoId, Long vecindarioId) throws EntityNotFoundException {
		log.info("Start the process of updating the neighborhood of the neighbor with id = {0}", vecinoId);
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioId);
		if (vecindarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINDARIO_NOT_FOUND);

		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);

		vecinoEntity.get().setVecindario(vecindarioEntity.get());
		log.info("End process of associating the deal with id = {0} to the offer with id = " + vecinoId, vecindarioId);
		return vecindarioEntity.get();
	}

	/**
	 * Borrar el vecindario de un vecino
	 *
	 * @param vecinoId el vecino que se desea borrar del vecindario.
	 * @throws EntityNotFoundException si el vecino no tiene vecindario
	 */

	@Transactional
	public void removeVecindario(Long vecinoId) throws EntityNotFoundException {
		log.info("Start the process of deleting the neighborhood from the neighbor with id = {0}", vecinoId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.VECINO_NOT_FOUND);

		if (vecinoEntity.get().getVecindario() == null) {
			throw new EntityNotFoundException("The neighbor has no associated neighborhood");
		}
		Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecinoEntity.get().getVecindario().getId());

		vecindarioEntity.ifPresent(vecindario -> {
			vecinoEntity.get().setVecindario(null);
			vecindario.getVecinos().remove(vecinoEntity.get());
		});

		log.info("Finish process of deleting the deal from the offer with id = " + vecinoId);
	}
}

    
