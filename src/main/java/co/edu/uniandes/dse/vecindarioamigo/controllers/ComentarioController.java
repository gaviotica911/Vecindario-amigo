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

import co.edu.uniandes.dse.vecindarioamigo.dto.*;
import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.ComentarioPublicacionService;
import co.edu.uniandes.dse.vecindarioamigo.services.ComentarioService;
import co.edu.uniandes.dse.vecindarioamigo.services.GrupoDeInteresService;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {
	@Autowired
	private ComentarioService comentarioService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve todos los premios que existen en la aplicacion.
	 *
	 * @return JSONArray {@link ComentarioDetailDTO} - Los premios encontrados en la
	 *         aplicación. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ComentarioDetailDTO> findAll() {
		List<ComentarioEntity> prizes = comentarioService.getComentarios();
		return modelMapper.map(prizes, new TypeToken<List<ComentarioDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el premio con el id asociado recibido en la URL y lo devuelve.
	 *
	 * @param id Identificador del premio que se esta buscando. Este debe ser una
	 *           cadena de dígitos.
	 * @return JSON {@link ComentarioDetailDTO} - El premio buscado
	 * @throws WebApplicationException {@link WebApplicationExceptionMapper} - Error
	 *                                 de lógica que se genera cuando no se
	 *                                 encuentra el premio.
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		ComentarioEntity prizeEntity = comentarioService.getComentario(id);
		return modelMapper.map(prizeEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Crea un nuevo premio con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param prize {@link ComentarioDTO} - EL premio que se desea guardar.
	 * @return JSON {@link ComentarioDTO} - El premio guardado con el atributo id
	 *         autogenerado.
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ComentarioDTO create(@RequestBody ComentarioDTO prizeDTO) throws IllegalOperationException {
		ComentarioEntity prizeEntity = comentarioService.createComentarios(modelMapper.map(prizeDTO, ComentarioEntity.class));
		return modelMapper.map(prizeEntity, ComentarioDTO.class);
	}

	/**
	 * Actualiza el premio con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param id    Identificador del premio que se desea actualizar. Este debe ser
	 *              una cadena de dígitos.
	 * @param prize {@link ComentarioDTO} El premio que se desea guardar.
	 * @return JSON {@link ComentarioDetailDTO} - El premio guardada.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ComentarioDetailDTO update(@PathVariable("id") Long id, @RequestBody ComentarioDTO prizeDTO)
			throws EntityNotFoundException {
		ComentarioEntity prizeEntity = comentarioService.updateComentarios(id, modelMapper.map(prizeDTO, ComentarioEntity.class));
		return modelMapper.map(prizeEntity, ComentarioDetailDTO.class);
	}

	/**
	 * Borra el premio con el id asociado recibido en la URL.
	 *
	 * @param id Identificador del premio que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		comentarioService.deleteComentarios(id);
	}
}
