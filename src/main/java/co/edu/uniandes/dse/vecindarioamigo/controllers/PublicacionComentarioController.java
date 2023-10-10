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
	private PublicacionComentarioService PublicacionComentarioService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda un libro dentro de una Publicacion con la informacion que recibe el la
	 * URL. Se devuelve el libro que se guarda en la Publicacion.
	 *
	 * @param PublicacionId Identificador de la Publicacion que se esta actualizando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param ComentarioId      Identificador del libro que se desea guardar. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link ComentarioDTO} - El libro guardado en la Publicacion.
	 */
	@PostMapping(value = "/{PublicacionId}/comentarios/{ComentarioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDTO addComentario(@PathVariable("PublicacionId") Long PublicacionId, @PathVariable("ComentarioId") Long ComentariolId)
			throws EntityNotFoundException {
		ComentarioEntity ComentarioEntity = PublicacionComentarioService.addComentario(ComentariolId, PublicacionId);
		return modelMapper.map(ComentarioEntity, ComentarioDTO.class);
	}

	/**
	 * Busca y devuelve todos los libros que existen en la Publicacion.
	 *
	 * @param PublicacionId Identificador de la Publicacion que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @return JSONArray {@link ComentarioDetailDTO} - Los libros encontrados en la
	 *         Publicacion. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{PublicacionId}/comentarios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDetailDTO> getComentarios(@PathVariable("PublicacionId") Long PublicacionId) throws EntityNotFoundException {
		List<ComentarioEntity> ComentarioList = PublicacionComentarioService.getComentarios(PublicacionId);
		return modelMapper.map(ComentarioList, new TypeToken<List<ComentarioDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el libro con el id asociado dentro de la Publicacion con id asociado.
	 *
	 * @param PublicacionId Identificador de la Publicacion que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @param ComentarioId      Identificador del libro que se esta buscando. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link ComentarioDetailDTO} - El libro buscado
	 */
	@GetMapping(value = "/{publicacionId}/comentarios/{comentarioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDetailDTO getCommment(@PathVariable("publicacionId") Long PublicacionId, @PathVariable("comentarioId") Long ComentarioId)
			throws EntityNotFoundException, IllegalOperationException {
				System.out.println("enter to get request");
		ComentarioEntity ComentarioEntity = PublicacionComentarioService.getComentario(PublicacionId, ComentarioId);
		return modelMapper.map(ComentarioEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Remplaza las instancias de Comentario asociadas a una instancia de Publicacion
	 *
	 * @param PublicacionId Identificador de la Publicacion que se esta remplazando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param Comentarios       JSONArray {@link ComentarioDTO} El arreglo de libros nuevo para
	 *                    la Publicacion.
	 * @return JSON {@link ComentarioDetailDTO} - El arreglo de libros guardado en la
	 *         Publicacion.
	 */
	@PutMapping(value = "/{PublicacionId}/comentarios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDetailDTO> replaceComentarios(@PathVariable("PublicacionId") Long PublicacionsId,
			@RequestBody List<ComentarioDetailDTO> Comentarios) throws EntityNotFoundException {
		List<ComentarioEntity> ComentariosList = modelMapper.map(Comentarios, new TypeToken<List<ComentarioEntity>>() {
		}.getType());
		List<ComentarioEntity> result = PublicacionComentarioService.replaceComentarios(PublicacionsId, ComentariosList);
		return modelMapper.map(result, new TypeToken<List<ComentarioDetailDTO>>() {
		}.getType());
	}
}