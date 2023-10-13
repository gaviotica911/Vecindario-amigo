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
public class GrupoDeInteresService {
    @Autowired
	GrupoDeInteresRepository GrupoDeInteresRepository;

	/**
	 * Se encarga de crear un GruposDeInteres en la base de datos.
	 *
	 * @param GrupoDeInteres Objeto de GruposDeInteresEntity con los datos nuevos
	 * @return Objeto de GruposDeInteresEntity con los datos nuevos y su ID.
	 * @throws IllegalOperationException 
	 */
	@Transactional
	public GruposDeInteresEntity createGruposDeInteres(GruposDeInteresEntity GrupoDeInteres) throws IllegalOperationException {
		log.info("Inicia proceso de creación del Grupo de interes");

        if (!GrupoDeInteresRepository.findByNombre(GrupoDeInteres.getNombre()).isEmpty()) {
			throw new IllegalOperationException("El nombre del grupo de interes ya existe");
        }
		log.info("termina el proceso de creación del Grupo de interes");
		return GrupoDeInteresRepository.save(GrupoDeInteres);
	}
    /**
	 * Obtiene la lista de los registros de GruposDeInteres.
	 *
	 * @return Colección de objetos de GruposDeInteresEntity.
	 */
	@Transactional
    public List<GruposDeInteresEntity> getGruposDeInteres() {
		log.info("Inicia proceso de consultar todos los GruposDeInterees");
		return GrupoDeInteresRepository.findAll();
	}

	/**
	 * Obtiene los datos de una instancia de GruposDeInteres a partir de su ID.
	 *
	 * @param grupoDeInteresId Identificador de la instancia a consultar
	 * @return Instancia de GruposDeInteresEntity con los datos del A
    */
    @Transactional
	public GruposDeInteresEntity getGrupoDeInteres(Long grupoDeInteresId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el GruposDeIntere con id = {0}", grupoDeInteresId);
		Optional<GruposDeInteresEntity> GruposDeInteresEntity = GrupoDeInteresRepository.findById(grupoDeInteresId);
		if (GruposDeInteresEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);
		log.info("Termina proceso de consultar el GruposDeIntere con id = {0}", grupoDeInteresId);
		return GruposDeInteresEntity.get();
	}

	/**
	 * Actualiza la información de una instancia de GruposDeInteres.
	 *
	 * @param grupoDeInteresId     Identificador de la instancia a actualizar
	 * @param GruposDeInteresEntity Instancia de GruposDeInteresEntity con los nuevos datos.
	 * @return Instancia de GruposDeInteresEntity con los datos actualizados.
	 */
	@Transactional
	public GruposDeInteresEntity updateGruposDeInteres(Long grupoDeInteresId, GruposDeInteresEntity GruposDeInteres) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el GruposDeIntere con id = {0}", grupoDeInteresId);
		Optional<GruposDeInteresEntity> GruposDeInteresEntity = GrupoDeInteresRepository.findById(grupoDeInteresId);
		if (GruposDeInteresEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);
		log.info("Termina proceso de actualizar el GruposDeIntere con id = {0}", grupoDeInteresId);
		GruposDeInteres.setId(grupoDeInteresId);
		return GrupoDeInteresRepository.save(GruposDeInteres);
	}

	/**
	 * Elimina una instancia de GruposDeInteres de la base de datos.
	 *
	 * @param grupoDeInteresId Identificador de la instancia a eliminar.
	 * @throws BusinessLogicException si el GruposDeIntere tiene libros asociados.
	 */
	@Transactional
	public void deleteGruposDeInteres(Long grupoDeInteresId) throws IllegalOperationException, EntityNotFoundException {
		log.info("Inicia proceso de borrar el GruposDeIntere con id = {0}", grupoDeInteresId);
		Optional<GruposDeInteresEntity> GruposDeInteresEntity = GrupoDeInteresRepository.findById(grupoDeInteresId);
		if (GruposDeInteresEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);

		List<VecinoEntity> vecino = GruposDeInteresEntity.get().getVecinos();
		if (!vecino.isEmpty())
			throw new IllegalOperationException("Unable to delete the GruposDeInteres because he/she has associated books");

		GrupoDeInteresRepository.deleteById(grupoDeInteresId);
		log.info("Termina proceso de borrar el GruposDeIntere con id = {0}", grupoDeInteresId);
	}


    }
