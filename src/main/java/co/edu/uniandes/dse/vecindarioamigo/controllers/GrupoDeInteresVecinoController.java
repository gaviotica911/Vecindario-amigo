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
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.GrupoDeInteresVecinoService;

public class GrupoDeInteresVecinoController {
	/**
	 * Clase que implementa el recurso "gruposdeinteres".
	 *
	 * @author ISIS2603
	 */
	@RestController
	@RequestMapping("/gruposDeInteres")
	public class AuthorBookController {

		@Autowired
		private GrupoDeInteresVecinoService grupoDeInteresVecinoService;

		@Autowired
		private ModelMapper modelMapper;

		/**
		 * Busca y devuelve el libro con el ID recibido en la URL, relativo a un autor.
		 *
		 * @param grupoDeInteresId El ID del autor del cual se busca el libro
		 * @param vecinoId         El ID del libro que se busca
		 * @return {@link VecinoDetailDTO} - El libro encontrado en el autor.
		 */
		@GetMapping(value = "/{grupoDeInteresId}/vecinos/{vecinoId}")
		@ResponseStatus(code = HttpStatus.OK)
		public VecinoDetailDTO getBook(@PathVariable("grupoDeInteresId") Long grupoDeInteresId,
				@PathVariable("vecinoId") Long vecinoId)
				throws EntityNotFoundException, IllegalOperationException {
			VecinoEntity bookEntity = grupoDeInteresVecinoService.getVecino(grupoDeInteresId, vecinoId);
			return modelMapper.map(bookEntity, VecinoDetailDTO.class);
		}

		/**
		 * Busca y devuelve todos los libros que existen en un autor.
		 *
		 * @param authorsId El ID del autor del cual se buscan los libros
		 * @return JSONArray {@link VecinoDetailDTO} - Los libros encontrados en el
		 *         autor.
		 *         Si no hay ninguno retorna una lista vacía.
		 */
		@GetMapping(value = "/{grupoDeInteresId}/vecinos")
		@ResponseStatus(code = HttpStatus.OK)
		public List<VecinoDetailDTO> getBooks(@PathVariable("grupoDeInteresId") Long grupoDeInteresId)
				throws EntityNotFoundException {
			List<VecinoEntity> bookEntity = grupoDeInteresVecinoService.getVecinos(grupoDeInteresId);
			return modelMapper.map(bookEntity, new TypeToken<List<VecinoDetailDTO>>() {
			}.getType());
		}

		/**
		 * Asocia un libro existente con un autor existente
		 *
		 * @param grupoDeInteresId El ID del autor al cual se le va a asociar el libro
		 * @param vecinoId         El ID del libro que se asocia
		 * @return JSON {@link VecinoDetailDTO} - El libro asociado.
		 */
		@PostMapping(value = "/{grupoDeInteresId}/vecinos/{vecinoId}")
		@ResponseStatus(code = HttpStatus.OK)
		public VecinoDetailDTO addBook(@PathVariable("grupoDeInteresId") Long grupoDeInteresId,
				@PathVariable("vecinoId") Long vecinoId)
				throws EntityNotFoundException {
			VecinoEntity bookEntity = grupoDeInteresVecinoService.addVecino(grupoDeInteresId, vecinoId);
			return modelMapper.map(bookEntity, VecinoDetailDTO.class);
		}

		/**
		 * Actualiza la lista de libros de un autor con la lista que se recibe en el
		 * cuerpo
		 *
		 * @param grupoDeInteresId El ID del autor al cual se le va a asociar el libro
		 * @param vecinos          JSONArray {@link VecinoDTO} - La lista de libros que
		 *                         se desea
		 *                         guardar.
		 * @return JSONArray {@link VecinoDetailDTO} - La lista actualizada.
		 */
		@PutMapping(value = "/{grupoDeInteresId}/vecinos")
		@ResponseStatus(code = HttpStatus.OK)
		public List<VecinoDetailDTO> replaceBooks(@PathVariable("grupoDeInteresId") Long grupoDeInteresId,
				@RequestBody List<VecinoDTO> vecinos)
				throws EntityNotFoundException {
			List<VecinoEntity> entities = modelMapper.map(vecinos, new TypeToken<List<VecinoEntity>>() {
			}.getType());
			List<VecinoEntity> vecinosList = grupoDeInteresVecinoService.addVecinos(grupoDeInteresId, entities);
			return modelMapper.map(vecinosList, new TypeToken<List<VecinoDetailDTO>>() {
			}.getType());

		}

		/**
		 * Elimina la conexión entre el libro y e autor recibidos en la URL.
		 *
		 * @param grupoDeInteresId El ID del autor al cual se le va a desasociar el
		 *                         libro
		 * @param vecinoId         El ID del libro que se desasocia
		 */
		@DeleteMapping(value = "/{grupoDeInteresId}/vecinos/{vecinoId}")
		@ResponseStatus(code = HttpStatus.NO_CONTENT)
		public void removeBook(@PathVariable("grupoDeInteresId") Long grupoDeInteresId,
				@PathVariable("vecinoId") Long vecinoId)
				throws EntityNotFoundException {
			grupoDeInteresVecinoService.removeVecino(grupoDeInteresId, vecinoId);
		}
	}
}
