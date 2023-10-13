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

import co.edu.uniandes.dse.vecindarioamigo.dto.GrupoDeInteresDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.GrupoDeInteresDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.GruposDeInteresEntity;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.VecinoGrupoDeInteresService;

@RestController
@RequestMapping("/vecinos")
public class VecinoGrupoDeInteresController {

	@Autowired
	private VecinoGrupoDeInteresService vecinogruposDeIntereservice;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve el libro con el ID recibido en la URL, relativo a un autor.
	 *
	 * @param vecinoId         El ID del autor del cual se busca el libro
	 * @param grupoDeInteresId El ID del libro que se busca
	 * @return {@link grupoDeInteresDetailDTO} - El libro encontrado en el autor.
	 */
	
	/**
	 * Busca y devuelve todos los libros que existen en un autor.
	 *
	 * @param vecinosId El ID del autor del cual se buscan los libros
	 * @return JSONArray {@link grupoDeInteresDetailDTO} - Los libros encontrados en
	 *         el autor.
	 *         Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{vecinoId}/gruposDeInteres")
	@ResponseStatus(code = HttpStatus.OK)
	public List<GrupoDeInteresDetailDTO> getgruposDeInteres(@PathVariable("vecinoId") Long vecinoId)
			throws EntityNotFoundException {
		List<GruposDeInteresEntity> grupoDeInteresEntity = vecinogruposDeIntereservice.getGruposDeInteres(vecinoId);
		return modelMapper.map(grupoDeInteresEntity, new TypeToken<List<GrupoDeInteresDetailDTO>>() {
		}.getType());
	}

	@GetMapping(value = "/{vecinoId}/gruposDeInteres/{grupoDeInteresId}")
	@ResponseStatus(code = HttpStatus.OK)
	public GrupoDeInteresDetailDTO getAuthor(@PathVariable("grupoDeInteresId") Long authorId, @PathVariable("vecinoId") Long bookId)
			throws EntityNotFoundException, IllegalOperationException {
		GruposDeInteresEntity authorEntity = vecinogruposDeIntereservice.getGrupoDeInteres(bookId, authorId);
		return modelMapper.map(authorEntity, GrupoDeInteresDetailDTO.class);
	}

	
	@PostMapping(value = "/{vecinoId}/gruposDeInteres/{grupoDeInteresId}")
	@ResponseStatus(code = HttpStatus.OK)
	public GrupoDeInteresDetailDTO addGrupoDeInteres(@PathVariable("vecinoId") Long VecinoId,
			@PathVariable("grupoDeInteresId") Long GrupoDeInteresId)
			throws EntityNotFoundException, IllegalOperationException {
		GruposDeInteresEntity GrupoDeInteresEntity = vecinogruposDeIntereservice.addGrupoDeInteres(VecinoId,
				GrupoDeInteresId);
		
		return modelMapper.map(GrupoDeInteresEntity, GrupoDeInteresDetailDTO.class);
	}

	/**
	 * Actualiza la lista de libros de un autor con la lista que se recibe en el
	 * cuerpo
	 *
	 * @param vecinoId        El ID del autor al cual se le va a asociar el libro
	 * @param gruposDeInteres JSONArray {@link grupoDeInteresDTO} - La lista de
	 *                        libros que se desea
	 *                        guardar.
	 * @return JSONArray {@link grupoDeInteresDetailDTO} - La lista actualizada.
	 */
	@PutMapping(value = "/{vecinoId}/gruposDeInteres")
	@ResponseStatus(code = HttpStatus.OK)
	public List<GrupoDeInteresDetailDTO> replacegruposDeInteres(@PathVariable("vecinoId") Long vecinoId,
			@RequestBody List<GrupoDeInteresDTO> gruposDeInteres)
			throws EntityNotFoundException {
		List<GruposDeInteresEntity> entities = modelMapper.map(gruposDeInteres,
				new TypeToken<List<GruposDeInteresEntity>>() {
				}.getType());
		System.out.println(entities);
		List<GruposDeInteresEntity> gruposDeInteresList = vecinogruposDeIntereservice.addGruposDeInteres(vecinoId,
				entities);
		return modelMapper.map(gruposDeInteresList, new TypeToken<List<GrupoDeInteresDetailDTO>>() {
		}.getType());

	}

	/**
	 * Elimina la conexión entre el libro y e autor recibidos en la URL.
	 *
	 * @param vecinoId         El ID del autor al cual se le va a desasociar el
	 *                         libro
	 * @param grupoDeInteresId El ID del libro que se desasocia
	 */
	@DeleteMapping(value = "/{vecinoId}/gruposDeInteres/{grupoDeInteresId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removegrupoDeInteres(@PathVariable("vecinoId") Long vecinoId,
			@PathVariable("grupoDeInteresId") Long grupoDeInteresId)
			throws EntityNotFoundException {
		vecinogruposDeIntereservice.removeGrupoDeInteres(vecinoId, grupoDeInteresId);
	}
}