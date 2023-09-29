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

import co.edu.uniandes.dse.vecindarioamigo.dto.CentroComercialDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.CentroComercialDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.CentroComercialDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.CentroComercialDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.ComentarioCentroComercialService;
import co.edu.uniandes.dse.vecindarioamigo.services.ComentarioPublicacionService;

@RestController
@RequestMapping("/comentarios")
public class ComentarioCentroComercioalController {
        /**
 * Clase que implementa el recurso "prize/{id}/publicacion".
 *
 * @publicacion ISIS2603
 */

	@Autowired
	private ComentarioCentroComercialService comentarioCentroComercialService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda un publicacion dentro de un premio con la informacion que recibe el la URL.
	 *
	 * @param comentarioId  Identificador de el premio que se esta actualizando. Este
	 *                 debe ser una cadena de dígitos.
	 * @param centroComercialId Identificador del autor que se desea guardar. Este debe ser
	 *                 una cadena de dígitos.
	 * @return JSON {@link CentroComercialDTO} - El autor guardado en el premio.
	 */
	@PostMapping(value = "/{comentarioId}/centrosComerciales/{centroComercialId}")
	@ResponseStatus(code = HttpStatus.OK)
	public CentroComercialDTO addAuthor(@PathVariable("comentarioId") Long comentarioId, @PathVariable("centroComercialId") Long publicacionId)
			throws EntityNotFoundException {
		CentroComercialEntity publicacionEntity = comentarioCentroComercialService.addCentroComercial(publicacionId, comentarioId);
		return modelMapper.map(publicacionEntity, CentroComercialDTO.class);
	}

	/**
	 * Busca el autor dentro de el premio con id asociado.
	 *
	 * @param comentarioId Identificador de el premio que se esta buscando. Este debe ser
	 *                una cadena de dígitos.
	 * @return JSON {@link CentroComercialDetailDTO} - El autor buscado
	 */
	@GetMapping(value = "/{comentarioId}/centrosComerciales")
	@ResponseStatus(code = HttpStatus.OK)
	public CentroComercialDetailDTO getAuthor(@PathVariable("comentarioId") Long comentarioId) throws EntityNotFoundException {
		CentroComercialEntity publicacionEntity = comentarioCentroComercialService.getCentroComercial(comentarioId);
		return modelMapper.map(publicacionEntity, CentroComercialDetailDTO.class);
	}


	/**
	 * Elimina la conexión entre el autor y el premio recibido en la URL.
	 *
	 * @param comentarioId El ID del premio al cual se le va a desasociar el autor
	 */
	@DeleteMapping(value = "/{comentarioId}/centrosComerciales")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeAuthor(@PathVariable("comentarioId") Long comentarioId) throws EntityNotFoundException {
		comentarioCentroComercialService.removeCentroComercial(comentarioId);
	}
}
