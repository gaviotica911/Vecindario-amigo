package co.edu.uniandes.dse.vecindarioamigo.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.dto.ComentarioDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.ComentarioDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.PublicacionComentarioService;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionComentarioController {
    

	@Autowired
	private PublicacionComentarioService PublicacionComentarioService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda una Comentario dentro de una Publicacion con la informacion que recibe el/la
	 * URL. Se devuelve la Comentario que se guarda en el Publicacion.
	 *
	 * @param PublicacionId Identificador del Publicacion que se esta actualizando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param comentarioId      Identificador de la Comentario que se desea guardar. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link ComentarioDTO} - El Comentario guardado en el Publicacion.
	 */
	@PostMapping(value = "/{PublicacionId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDTO addComentario(@PathVariable("PublicacionId") Long PublicacionId, @PathVariable("comentarioId") Long comentarioId)
			throws EntityNotFoundException {
		ComentarioEntity ComentarioEntity = PublicacionComentarioService.addComentario(comentarioId, PublicacionId);
		return modelMapper.map(ComentarioEntity, ComentarioDTO.class);
	}

	/**
	 * Busca y devuelve todos las Comentarios que existen en el Publicacion.
	 *
	 * @param PublicacionId Identificador de la Publicacion que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @return JSONArray {@link ComentarioDetailDTO} - Las Comentarios encontrados en el
	 *         Publicacion. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{PublicacionId}/comentarios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDetailDTO> getComentarios(@PathVariable("PublicacionId") Long PublicacionId) throws EntityNotFoundException {
		List<ComentarioEntity> ComentarioList = PublicacionComentarioService.getComentaros(PublicacionId);
		return modelMapper.map(ComentarioList, new TypeToken<List<ComentarioDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca la Comentario con el id asociado dentro del Publicacion con id asociado.
	 *
	 * @param PublicacionId Identificador del Publicacion que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @param comentarioId      Identificador del Comentario que se esta buscando. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link ComentarioDetailDTO} - El Comentario buscado
	 */
	@GetMapping(value = "/{PublicacionId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDetailDTO getComentario(@PathVariable("PublicacionId") Long PublicacionId, @PathVariable("comentarioId") Long comentarioId)
			throws EntityNotFoundException, IllegalOperationException {
		ComentarioEntity ComentarioEntity = PublicacionComentarioService.getComentario(PublicacionId, comentarioId);
		return modelMapper.map(ComentarioEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Remplaza las instancias de Comentario asociadas a una instancia de Publicacion
	 *
	 * @param PublicacionId Identificador del Publicacion que se esta remplazando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param Comentarios       JSONArray {@link BookDTO} El arreglo de Comentarios nuevo para
	 *                    el Publicacion.
	 * @return JSON {@link ComentarioDTO} - El arreglo de Comentarios guardado en el
	 *         Publicacion.
	 */
	@PutMapping(value = "/{PublicacionId}/comentarios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDTO> replaceComentarios(@PathVariable("PublicacionId") Long PublicacionId,
			@RequestBody List<ComentarioDTO> Comentarios) throws EntityNotFoundException {
		List<ComentarioEntity> ComentariosList = modelMapper.map(Comentarios, new TypeToken<List<ComentarioEntity>>() {
		}.getType());
		List<ComentarioEntity> result = PublicacionComentarioService.replaceComentarios(PublicacionId, ComentariosList);
		return modelMapper.map(result, new TypeToken<List<ComentarioDTO>>() {
		}.getType());
	}

}
