package co.edu.uniandes.dse.vecindarioamigo.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	private PublicacionComentarioService publicacionComentarioService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda un comentario dentro de una publicacion con la informacion que recibe
	 * URL. Se devuelve el comentario que se guarda en la publicacion.
	 *
	 * @param publicacionId Identificador de la publicacion que se esta actualizando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param comentarioId      Identificador del comentario que se desea guardar. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link ComentarioDTO} - El comentario guardado en la publicacion.
	 */
	@PostMapping(value = "/{publicacionId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDTO addComentario(@PathVariable("publicacionId") Long publicacionId, @PathVariable("comentarioId") Long comentariolId)
			throws EntityNotFoundException {
		ComentarioEntity comentarioEntity = publicacionComentarioService.addComentario(comentariolId, publicacionId);
		return modelMapper.map(comentarioEntity, ComentarioDTO.class);
	}

	/**
	 * Busca y devuelve todos los comentarios que existen en la publicacion.
	 *
	 * @param publicacionId Identificador de la publicacion que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @return JSONArray {@link ComentarioDetailDTO} - Los comentarios encontrados en la
	 *         publicacion. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{publicacionId}/comentarios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDetailDTO> getComentarios(@PathVariable("publicacionId") Long publicacionId) throws EntityNotFoundException {
		List<ComentarioEntity> comentarioList = publicacionComentarioService.getComentarios(publicacionId);
		return modelMapper.map(comentarioList, new TypeToken<List<ComentarioDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el comentario con el id asociado dentro de la publicacion con id asociado.
	 *
	 * @param publicacionId Identificador de la publicacion que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @param comentarioId      Identificador del comentario que se esta buscando. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link ComentarioDetailDTO} - El comentario buscado
	 */
	@GetMapping(value = "/{publicacionId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDetailDTO getComentario(@PathVariable("publicacionId") Long publicacionId, @PathVariable("comentarioId") Long comentarioId)
			throws EntityNotFoundException, IllegalOperationException {
		ComentarioEntity comentarioEntity = publicacionComentarioService.getComentario(publicacionId, comentarioId);
		return modelMapper.map(comentarioEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Remplaza las instancias de Comentario asociadas a una instancia de Publicacion
	 *
	 * @param publicacionId Identificador de la publicacion que se esta remplazando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param comentarios       JSONArray {@link ComentarioDTO} El arreglo de comentarios nuevo para
	 *                    la publicacion.
	 * @return JSON {@link ComentarioDetailDTO} - El arreglo de comentarios guardado en la
	 *         publicacion.
	 */
	@PutMapping(value = "/{publicacionId}/comentarios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDetailDTO> replaceComentarios(@PathVariable("publicacionId") Long publicacionsId,
			@RequestBody List<ComentarioDetailDTO> comentarios) throws EntityNotFoundException {
		List<ComentarioEntity> comentariosList = modelMapper.map(comentarios, new TypeToken<List<ComentarioEntity>>() {
		}.getType());
		List<ComentarioEntity> result = publicacionComentarioService.replaceComentarios(publicacionsId, comentariosList);
		return modelMapper.map(result, new TypeToken<List<ComentarioDetailDTO>>() {
		}.getType());
	}
    
    
}
