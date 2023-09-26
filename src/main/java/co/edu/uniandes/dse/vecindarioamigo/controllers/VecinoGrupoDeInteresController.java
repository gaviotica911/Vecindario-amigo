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



/**
 * Clase que implementa el recurso "vecinos/{id}/s".
 *
 
 */
@RestController
@RequestMapping("/vecinos")
public class VecinoGrupoDeInteresController {
    @Autowired
	private VecinoGrupoDeInteresService vecinoGrupoDeInteresService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Asocia un grupo de interes existente con un vecinos existente
	 *
	 * @param grupoDeInteresId El ID del grupo de interes que se va a asociar
	 * @param vecinoId   El ID del vecinos al cual se le va a asociar el grupo de interes
	 * @return JSON {@link GrupoDeInteresDetailDTO} - El grupo de interes asociado.
	 * @throws IllegalOperationException
	 */
	@PostMapping(value = "/{vecinoId}/gruposDeInteres/{grupoDeInteresId}")
	@ResponseStatus(code = HttpStatus.OK)
	public GrupoDeInteresDetailDTO addGrupoDeInteres(@PathVariable("grupoDeInteresId") Long grupoDeInteresId, @PathVariable("vecinoId") Long vecinoId)
			throws EntityNotFoundException, IllegalOperationException {
		GruposDeInteresEntity grupoDeInteresEntity = vecinoGrupoDeInteresService.addGrupoDeInteres(vecinoId, grupoDeInteresId);
		return modelMapper.map(grupoDeInteresEntity, GrupoDeInteresDetailDTO.class);
	}

	/**
	 * Busca y devuelve el grupo de interes con el ID recibido en la URL, relativo a un vecinos.
	 *
	 * @param grupoDeInteresId El ID del grupo de interes que se busca
	 * @param vecinoId   El ID del vecinos del cual se busca el grupo de interes
	 * @return {@link GrupoDeInteresDetailDTO} - El grupo de interes encontrado en el vecinos.
	 */
	@GetMapping(value = "/{vecinoId}/gruposDeInteres/{grupoDeInteresId}")
	@ResponseStatus(code = HttpStatus.OK)
	public GrupoDeInteresDetailDTO getGrupoDeInteres(@PathVariable("grupoDeInteresId") Long grupoDeInteresId, @PathVariable("vecinoId") Long vecinoId)
			throws EntityNotFoundException, IllegalOperationException {
		GruposDeInteresEntity grupoDeInteresEntity = vecinoGrupoDeInteresService.getGrupoDeInteres(vecinoId, grupoDeInteresId);
		return modelMapper.map(grupoDeInteresEntity, GrupoDeInteresDetailDTO.class);
	}

	/**
	 * Actualiza la lista de grupo de intereses de un vecinos con la lista que se recibe en el
	 * cuerpo.
	 *
	 * @param vecinoId  El ID del vecinos al cual se le va a asociar la lista de grupo de intereses
	 * @param s JSONArray {@link GrupoDeInteresDTO} - La lista de grupo de intereses que se desea
	 *                guardar.
	 * @return JSONArray {@link GrupoDeInteresDetailDTO} - La lista actualizada.
	 * @throws IllegalOperationException
	 */
	@PutMapping(value = "/{vecinoId}/gruposDeInteres")
	@ResponseStatus(code = HttpStatus.OK)
	public List<GrupoDeInteresDetailDTO> addGrupoDeInteress(@PathVariable("vecinoId") Long vecinoId, @RequestBody List<GrupoDeInteresDTO> s)
			throws EntityNotFoundException, IllegalOperationException {
		List<GruposDeInteresEntity> entities = modelMapper.map(s, new TypeToken<List<GruposDeInteresEntity>>() {
		}.getType());
		List<GruposDeInteresEntity> sList = vecinoGrupoDeInteresService.replaceGrupoDeInteres(vecinoId, entities);
		return modelMapper.map(sList, new TypeToken<List<GrupoDeInteresDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca y devuelve todos los grupo de intereses que existen en un vecinos.
	 *
	 * @param vecinosd El ID del vecinos del cual se buscan los grupo de intereses
	 * @return JSONArray {@link GrupoDeInteresDetailDTO} - Los grupo de intereses encontrados en el
	 *         vecinos. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{vecinoId}/gruposDeInteres")
	@ResponseStatus(code = HttpStatus.OK)
	public List<GrupoDeInteresDetailDTO> getGrupoDeInteress(@PathVariable("vecinoId") Long vecinoId) throws EntityNotFoundException {
		List<GruposDeInteresEntity> grupoDeInteresEntity = vecinoGrupoDeInteresService.getGruposDeInteres(vecinoId);
		return modelMapper.map(grupoDeInteresEntity, new TypeToken<List<GrupoDeInteresDetailDTO>>() {
		}.getType());
	}

	/**
	 * Elimina la conexión entre el grupo de interes y el vecinos recibidos en la URL.
	 *
	 * @param vecinoId   El ID del vecinos al cual se le va a desasociar el grupo de interes
	 * @param grupoDeInteresId El ID del grupo de interes que se desasocia
	 */
	@DeleteMapping(value = "/{vecinoId}/gruposDeInteres/{grupoDeInteresId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeGrupoDeInteres(@PathVariable("grupoDeInteresId") Long grupoDeInteresId, @PathVariable("vecinoId") Long vecinoId)
			throws EntityNotFoundException {
		vecinoGrupoDeInteresService.removeGrupoDeInteres(vecinoId, grupoDeInteresId);
	}
    
}
