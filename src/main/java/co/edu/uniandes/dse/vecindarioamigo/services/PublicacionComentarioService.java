package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.ComentarioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.PublicacionRepository;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PublicacionComentarioService {

	@Autowired
	private ComentarioRepository ComentarioRepository;

	@Autowired
	private PublicacionRepository PublicacionRepository;
	
	/**
	 * Agregar un Comentario a la Publicacion
	 *
	 * @param ComentarioId      El id libro a guardar
	 * @param PublicacionId El id de la Publicacion en la cual se va a guardar el libro.
	 * @return El libro creado.
	 * @throws EntityNotFoundException 
	 */
	
	@Transactional
	public ComentarioEntity addComentario(Long ComentarioId, Long PublicacionId) throws EntityNotFoundException {
		log.info("Inicia proceso de agregarle un libro a la Publicacion con id = {0}", PublicacionId);
		
		Optional<ComentarioEntity> ComentarioEntity = ComentarioRepository.findById(ComentarioId);
		if(ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
		
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(PublicacionId);
		if(PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		ComentarioEntity.get().setPublicacion(PublicacionEntity.get());
		log.info("Termina proceso de agregarle un libro a la Publicacion con id = {0}", PublicacionId);
		return ComentarioEntity.get();
	}

	/**
	 * Retorna todos los Comentarios asociados a una Publicacion
	 *
	 * @param PublicacionId El ID de la Publicacion buscada
	 * @return La lista de libros de la Publicacion
	 * @throws EntityNotFoundException si la Publicacion no existe
	 */
	@Transactional
	public List<ComentarioEntity> getComentarios(Long PublicacionId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar los libros asociados a la Publicacion con id = {0}", PublicacionId);
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(PublicacionId);
		if(PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		return PublicacionEntity.get().getComentarios();
	}

	/**
	 * Retorna un Comentario asociado a una Publicacion
	 *
	 * @param PublicacionId El id de la Publicacion a buscar.
	 * @param ComentarioId      El id del libro a buscar
	 * @return El libro encontrado dentro de la Publicacion.
	 * @throws EntityNotFoundException Si el libro no se encuentra en la Publicacion
	 * @throws IllegalOperationException Si el libro no está asociado a la Publicacion
	 */
	@Transactional
	public ComentarioEntity getComentario(Long PublicacionId, Long ComentarioId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de consultar el libro con id = {0} de la Publicacion con id = " + PublicacionId, ComentarioId);
		
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(PublicacionId);
		if(PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		Optional<ComentarioEntity> ComentarioEntity = ComentarioRepository.findById(ComentarioId);
		if(ComentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
				
		log.info("Termina proceso de consultar el libro con id = {0} de la Publicacion con id = " + PublicacionId, ComentarioId);
		
		if(!PublicacionEntity.get().getComentarios().contains(ComentarioEntity.get()))
			throw new IllegalOperationException("The Comentario is not associated to the Publicacion");
		System.out.println(ComentarioEntity);
		return ComentarioEntity.get();
	}

	/**
	 * Remplazar Comentarios de una Publicacion
	 *
	 * @param Comentarios        Lista de libros que serán los de la Publicacion.
	 * @param PublicacionId El id de la Publicacion que se quiere actualizar.
	 * @return La lista de libros actualizada.
	 * @throws EntityNotFoundException Si la Publicacion o un libro de la lista no se encuentran
	 */
	@Transactional
	public List<ComentarioEntity> replaceComentarios(Long PublicacionId, List<ComentarioEntity> Comentarios) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar la Publicacion con id = {0}", PublicacionId);
		Optional<PublicacionEntity> PublicacionEntity = PublicacionRepository.findById(PublicacionId);
		if(PublicacionEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.PUBLICACION_NOT_FOUND);
		
		for(ComentarioEntity Comentario : Comentarios) {
			Optional<ComentarioEntity> b = ComentarioRepository.findById(Comentario.getId());
			if(b.isEmpty())
				throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
			
			b.get().setPublicacion(PublicacionEntity.get());
		}		
		return Comentarios;
	}
}