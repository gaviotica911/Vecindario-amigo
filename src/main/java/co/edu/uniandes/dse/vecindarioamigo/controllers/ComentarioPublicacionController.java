package co.edu.uniandes.dse.vecindarioamigo.controllers;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.edu.uniandes.dse.vecindarioamigo.dto.*;
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.ComentarioPublicacionService;

@RequestMapping("/comentarios")
public class ComentarioPublicacionController {
	/**
	 * Clase que implementa el recurso "prize/{id}/publicacion".
	 *
	 * @publicacion ISIS2603
	 */

	@Autowired
	private ComentarioPublicacionService comentarioPublicacionService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda un publicacion dentro de un premio con la informacion que recibe el la
	 * URL.
	 *
	 * @param comentarioId  Identificador de el premio que se esta actualizando.
	 *                      Este
	 *                      debe ser una cadena de dígitos.
	 * @param publicacionId Identificador del autor que se desea guardar. Este debe
	 *                      ser
	 *                      una cadena de dígitos.
	 * @return JSON {@link PublicacionDTO} - El autor guardado en el premio.
	 */
	@PostMapping(value = "/{comentarioId}/publicaciones/{publicacionId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PublicacionDTO addAuthor(@PathVariable("comentarioId") Long comentarioId,
			@PathVariable("publicacionId") Long publicacionId)
			throws EntityNotFoundException {
		PublicacionEntity publicacionEntity = comentarioPublicacionService.addpublicacion(publicacionId, comentarioId);
		return modelMapper.map(publicacionEntity, PublicacionDTO.class);
	}

	/**
	 * Busca el autor dentro de el premio con id asociado.
	 *
	 * @param comentarioId Identificador de el premio que se esta buscando. Este
	 *                     debe ser
	 *                     una cadena de dígitos.
	 * @return JSON {@link PublicacionDetailDTO} - El autor buscado
	 */
	@GetMapping(value = "/{comentarioId}/publicaciones")
	@ResponseStatus(code = HttpStatus.OK)
	public PublicacionDetailDTO getAuthor(@PathVariable("comentarioId") Long comentarioId)
			throws EntityNotFoundException {
		PublicacionEntity publicacionEntity = comentarioPublicacionService.getPublicacion(comentarioId);
		return modelMapper.map(publicacionEntity, PublicacionDetailDTO.class);
	}

	/**
	 * Elimina la conexión entre el autor y el premio recibido en la URL.
	 *
	 * @param comentarioId El ID del premio al cual se le va a desasociar el autor
	 */
	@DeleteMapping(value = "/{comentarioId}/publicaciones")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAuthor(@PathVariable("comentarioId") Long comentarioId) throws EntityNotFoundException {
		comentarioPublicacionService.removepublicacion(comentarioId);
	}

}
