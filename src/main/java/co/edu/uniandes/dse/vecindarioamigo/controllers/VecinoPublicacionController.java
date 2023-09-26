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

import co.edu.uniandes.dse.vecindarioamigo.dto.PublicacionDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.PublicacionDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.VecinoDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.VecinoDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.VecinoPublicacionService;

@RestController
@RequestMapping("/vecinos")
public class VecinoPublicacionController {
    @Autowired
	private VecinoPublicacionService vecinoPublicacionService;

	@Autowired
	private ModelMapper modelMapper;

    /**
	 * Guarda un post dentro de una vecino con la informacion que recibe  la
	 * URL. Se devuelve el post que se guarda en el vecino.
	 *
	 * @param vecinoId Identificador del vecino que se esta actualizando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param publicacionId      Identificador del post que se desea guardar. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link publicacionDTO} - El post guardado en la vecino.
	 */
	@PostMapping(value = "/{vecinoId}/publicacion/{publicacionId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PublicacionDTO addPublicacion(@PathVariable("vecinoId") Long vecinoId, @PathVariable("publicacionId") Long publicacionId)
			throws EntityNotFoundException {
		PublicacionEntity publicacionEntity = vecinoPublicacionService.addPublicacion(publicacionId, vecinoId);
		return modelMapper.map(publicacionEntity, PublicacionDTO.class);
	}
    /**
	 * Busca y devuelve todos los libros que existen en la vecino.
	 *
	 * @param vecinoId Identificador de la vecino que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @return JSONArray {@link PublicacionDetailDTO} - Los libros encontrados en la
	 *         vecino. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{vecinoId}/books")
	@ResponseStatus(code = HttpStatus.OK)
	public List<PublicacionDetailDTO> getPublicacions(@PathVariable("vecinoId") Long vecinoId) throws EntityNotFoundException {
		List<PublicacionEntity> bookList = vecinoPublicacionService.getPublicaciones(vecinoId);
		return modelMapper.map(bookList, new TypeToken<List<PublicacionDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el libro con el id asociado dentro de la vecino con id asociado.
	 *
	 * @param vecinoId Identificador de la vecino que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @param bookId      Identificador del libro que se esta buscando. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link PublicacionDetailDTO} - El libro buscado
	 */
	@GetMapping(value = "/{vecinoId}/books/{bookId}")
	@ResponseStatus(code = HttpStatus.OK)
	public PublicacionDetailDTO getPublicacion(@PathVariable("vecinoId") Long vecinoId, @PathVariable("bookId") Long bookId)
			throws EntityNotFoundException, IllegalOperationException {
		PublicacionEntity bookEntity = vecinoPublicacionService.getPublicacion(vecinoId, bookId);
		return modelMapper.map(bookEntity, PublicacionDetailDTO.class);
	}

	/**
	 * Remplaza las instancias de Publicacion asociadas a una instancia de Editorial
	 *
	 * @param vecinoId Identificador de la vecino que se esta remplazando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param books       JSONArray {@link PublicacionDTO} El arreglo de libros nuevo para
	 *                    la vecino.
	 * @return JSON {@link PublicacionDetailDTO} - El arreglo de libros guardado en la
	 *         vecino.
	 */
	@PutMapping(value = "/{vecinoId}/books")
	@ResponseStatus(code = HttpStatus.OK)
	public List<PublicacionDetailDTO> replacePublicacions(@PathVariable("vecinoId") Long vecinosId,
			@RequestBody List<PublicacionDetailDTO> books) throws EntityNotFoundException {
		List<PublicacionEntity> booksList = modelMapper.map(books, new TypeToken<List<PublicacionEntity>>() {
		}.getType());
		List<PublicacionEntity> result = vecinoPublicacionService.replacePublicaciones(vecinosId, booksList);
		return modelMapper.map(result, new TypeToken<List<PublicacionDetailDTO>>() {
		}.getType());
	}

}
