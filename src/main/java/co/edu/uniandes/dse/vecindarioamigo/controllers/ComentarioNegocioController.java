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
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.dto.NegocioDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.NegocioDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.NegocioDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.NegocioDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.ComentarioNegocioService;
import co.edu.uniandes.dse.vecindarioamigo.services.ComentarioPublicacionService;
@RestController
@RequestMapping("/comentarios")
public class ComentarioNegocioController {
        /**
 * Clase que implementa el recurso "prize/{id}/publicacion".
 *
 * @publicacion ISIS2603
 */

	@Autowired
	private ComentarioNegocioService comentarioNegocioService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda un publicacion dentro de un premio con la informacion que recibe el la URL.
	 *
	 * @param comentarioId  Identificador de el premio que se esta actualizando. Este
	 *                 debe ser una cadena de dígitos.
	 * @param negocioId Identificador del autor que se desea guardar. Este debe ser
	 *                 una cadena de dígitos.
	 * @return JSON {@link NegocioDTO} - El autor guardado en el premio.
	 */
	@PostMapping(value = "/{comentarioId}/negocios/{negocioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public NegocioDTO addAuthor(@PathVariable("comentarioId") Long comentarioId, @PathVariable("negocioId") Long negocioId)
			throws EntityNotFoundException {
		NegocioEntity publicacionEntity = comentarioNegocioService.addNegocio(negocioId, comentarioId);
		return modelMapper.map(publicacionEntity, NegocioDTO.class);
	}

	/**
	 * Busca el autor dentro de el premio con id asociado.
	 *
	 * @param comentarioId Identificador de el premio que se esta buscando. Este debe ser
	 *                una cadena de dígitos.
	 * @return JSON {@link NegocioDetailDTO} - El autor buscado
	 */
	@GetMapping(value = "/{comentarioId}/negocios")
	@ResponseStatus(code = HttpStatus.OK)
	public NegocioDetailDTO getAuthor(@PathVariable("comentarioId") Long comentarioId) throws EntityNotFoundException {
		NegocioEntity publicacionEntity = comentarioNegocioService.getNegocio(comentarioId);
		return modelMapper.map(publicacionEntity, NegocioDetailDTO.class);
	}


	/**
	 * Elimina la conexión entre el autor y el premio recibido en la URL.
	 *
	 * @param comentarioId El ID del premio al cual se le va a desasociar el autor
	 */
	@DeleteMapping(value = "/{comentarioId}/negocios")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAuthor(@PathVariable("comentarioId") Long comentarioId) throws EntityNotFoundException {
		comentarioNegocioService.removenegocio(comentarioId);
	}
}
