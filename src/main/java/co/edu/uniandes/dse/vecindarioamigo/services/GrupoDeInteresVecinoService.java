package co.edu.uniandes.dse.vecindarioamigo.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.repositories.*;
import co.edu.uniandes.dse.vecindarioamigo.entities.*;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.*;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class GrupoDeInteresVecinoService {
    @Autowired
	private VecinoRepository vecinoRepository;

	@Autowired
	private GrupoDeInteresRepository grupoDeInteresRepository;

	/**
	 * Asocia un Book existente a un Author
	 *
	 * @param grupoDeInteresId Identificador de la instancia de Author
	 * @param vecinoId   Identificador de la instancia de Book
	 * @return Instancia de VecinoEntity que fue asociada a Author
	 */

	@Transactional
	public VecinoEntity addVecino(Long grupoDeInteresId, Long vecinoId) throws EntityNotFoundException {
		log.info("Inicia proceso de asociarle un vecino al autor con id = {0}", grupoDeInteresId);
		Optional<GruposDeInteresEntity> grupoDeInteresEntity = grupoDeInteresRepository.findById(grupoDeInteresId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);

		if (grupoDeInteresEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);

		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBOR_NOT_FOUND);

		vecinoEntity.get().getGruposDeInteres().add(grupoDeInteresEntity.get());
		log.info("Termina proceso de asociarle un vecino al grupo con id = {0}", grupoDeInteresId);
		return vecinoEntity.get();
	}

	/**
	 * Obtiene una colección de instancias de VecinoEntity asociadas a una instancia
	 * de Author
	 *
	 * @param gruposDeInteresId Identificador de la instancia de Author
	 * @return Colección de instancias de VecinoEntity asociadas a la instancia de
	 *         Author
	 */
	@Transactional
	public List<VecinoEntity> getVecinos(Long grupoDeInteresId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar todos los vecinos del autor con id = {0}", grupoDeInteresId);
		Optional<GruposDeInteresEntity> grupoDeInteresEntity = grupoDeInteresRepository.findById(grupoDeInteresId);
		if (grupoDeInteresEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);

		log.info("Termina proceso de consultar todos los vecinos del autor con id = {0}", grupoDeInteresId);
		return grupoDeInteresEntity.get().getVecinos();
	}

	/**
	 * Obtiene una instancia de VecinoEntity asociada a una instancia de Author
	 *
	 * @param gruposDeInteresId Identificador de la instancia de Author
	 * @param vecinosId   Identificador de la instancia de Book
	 * @return La entidadd de Libro del autor
	 */
	@Transactional
	public VecinoEntity getVecino(Long grupoDeInteresId, Long vecinoId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar el vecino con id = {0} del autor con id = " + grupoDeInteresId, vecinoId);
		Optional<GruposDeInteresEntity> grupoDeInteresEntity = grupoDeInteresRepository.findById(grupoDeInteresId);
		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);

		if (grupoDeInteresEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);

		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBOR_NOT_FOUND);

		log.info("Termina proceso de consultar el vecino con id = {0} del grupo con id = " + grupoDeInteresId, vecinoId);
		if (!vecinoEntity.get().getGruposDeInteres().contains(grupoDeInteresEntity.get()))
			throw new IllegalOperationException("The vecino is not associated to the grupo");
		
		return vecinoEntity.get();
	}

	/**
	 * Remplaza las instancias de vecino asociadas a una instancia de Author
	 *
	 * @param grupoDeInteresId Identificador de la instancia de Author
	 * @param vecinos    Colección de instancias de VecinoEntity a asociar a instancia
	 *                 de Author
	 * @return Nueva colección de VecinoEntity asociada a la instancia de Author
	 */
	@Transactional
	public List<VecinoEntity> addVecinos(Long grupoDeInteresId, List<VecinoEntity> vecinos) throws EntityNotFoundException {
		log.info("Inicia proceso de reemplazar los vecinos asociados al grupo con id = {0}", grupoDeInteresId);
		Optional<GruposDeInteresEntity> grupoDeInteresEntity = grupoDeInteresRepository.findById(grupoDeInteresId);
		if (grupoDeInteresEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);

		for (VecinoEntity vecino : vecinos) {
			Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecino.getId());
			if (vecinoEntity.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.NEIGHBOR_NOT_FOUND);

			if (!vecinoEntity.get().getGruposDeInteres().contains(grupoDeInteresEntity.get()))
				vecinoEntity.get().getGruposDeInteres().add(grupoDeInteresEntity.get());
		}
		log.info("Finaliza proceso de reemplazar los vecinos asociados al grupo con id = {0}", grupoDeInteresId);
		grupoDeInteresEntity.get().setVecinos(vecinos);
		return grupoDeInteresEntity.get().getVecinos();
	}

	/**
	 * Desasocia un Book existente de un Author existente
	 *
	 * @param gruposDeInteresId Identificador de la instancia de Author
	 * @param vecinosId   Identificador de la instancia de Book
	 */
	@Transactional
	public void removeVecino(Long grupoDeInteresId, Long vecinoId) throws EntityNotFoundException {
		log.info("Inicia proceso de borrar un vecino del grupo con id = {0}", grupoDeInteresId);
		Optional<GruposDeInteresEntity> grupoDeInteresEntity = grupoDeInteresRepository.findById(grupoDeInteresId);
		if (grupoDeInteresEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);

		Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(vecinoId);
		if (vecinoEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.NEIGHBOR_NOT_FOUND);

		vecinoEntity.get().getGruposDeInteres().remove(grupoDeInteresEntity.get());
		grupoDeInteresEntity.get().getVecinos().remove(vecinoEntity.get());
		log.info("Finaliza proceso de borrar un vecino del grupo con id = {0}", grupoDeInteresId);
	}


}
