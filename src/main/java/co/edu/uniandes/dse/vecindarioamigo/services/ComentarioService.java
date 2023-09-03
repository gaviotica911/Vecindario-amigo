package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.repositories.*;


@Slf4j
@Service
public class ComentarioService {
    @Autowired
	ComentarioRepository ComentarioRepository;

	/**
	 * Se encarga de crear un Comentarios en la base de datos.
	 *
	 * @param Comentario Objeto de ComentarioEntity con los datos nuevos
	 * @return Objeto de ComentarioEntity con los datos nuevos y su ID.
	 * @throws IllegalOperationException 
	 */
	@Transactional
	public ComentarioEntity createComentarios(ComentarioEntity Comentario) throws IllegalOperationException {
		log.info("Inicia proceso de creación del Grupo de interes");

        if (!ComentarioRepository.findByNombre(Comentario.getNombre()).isEmpty()) {
			throw new IllegalOperationException("El nombre del grupo de interes ya existe");
        }
		return ComentarioRepository.save(Comentario);
	}
    /**
	 * Obtiene la lista de los registros de Comentarios.
	 *
	 * @return Colección de objetos de ComentarioEntity.
	 */
	@Transactional
    public List<ComentarioEntity> getComentarios() {
		log.info("Inicia proceso de consultar todos los Comentarioes");
		return ComentarioRepository.findAll();
	}

	/**
	 * Obtiene los datos de una instancia de Comentarios a partir de su ID.
	 *
	 * @param ComentarioId Identificador de la instancia a consultar
	 * @return Instancia de ComentarioEntity con los datos del A
    */
    @Transactional
	public ComentarioEntity getComentario(Long ComentarioId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el Comentario con id = {0}", ComentarioId);
		Optional<ComentarioEntity> ComentarioEntity = ComentarioRepository.findById(ComentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.GRUPO_DE_INTERES_NOT_FOUND);
		log.info("Termina proceso de consultar el Comentario con id = {0}", ComentarioId);
		return ComentarioEntity.get();
	}

	/**
	 * Actualiza la información de una instancia de Comentarios.
	 *
	 * @param ComentarioId     Identificador de la instancia a actualizar
	 * @param ComentarioEntity Instancia de ComentarioEntity con los nuevos datos.
	 * @return Instancia de ComentarioEntity con los datos actualizados.
	 */
	@Transactional
	public ComentarioEntity updateComentarios(Long ComentarioId, ComentarioEntity Comentarios) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el Comentario con id = {0}", ComentarioId);
		Optional<ComentarioEntity> ComentarioEntity = ComentarioRepository.findById(ComentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
		log.info("Termina proceso de actualizar el Comentario con id = {0}", ComentarioId);
		Comentarios.setId(ComentarioId);
		return ComentarioRepository.save(Comentarios);
	}

	/**
	 * Elimina una instancia de Comentarios de la base de datos.
	 *
	 * @param ComentarioId Identificador de la instancia a eliminar.
	 * @throws BusinessLogicException si el Comentario tiene libros asociados.
	 */
	@Transactional
	public void deleteComentarios(Long ComentarioId) throws IllegalOperationException, EntityNotFoundException {
		log.info("Inicia proceso de borrar el Comentario con id = {0}", ComentarioId);
		Optional<ComentarioEntity> ComentarioEntity = ComentarioRepository.findById(ComentarioId);
		if (ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
		ComentarioRepository.deleteById(ComentarioId);
		log.info("Termina proceso de borrar el comentario con id = {0}", ComentarioId);
	}


    
}
