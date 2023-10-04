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

import co.edu.uniandes.dse.vecindarioamigo.dto.VecinoDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.VecinoDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.VecindarioVecinoService;

@RestController
@RequestMapping("/vecindarios")
public class VecindarioVecinoController {

	@Autowired
	private VecindarioVecinoService VecindarioVecinoService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda un Vecino dentro de una vecindario con la informacion que recibe el/la
	 * URL. Se devuelve el Vecino que se guarda en el vecindario.
	 *
	 * @param vecindarioId Identificador del vecindario que se esta actualizando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param VecinoId      Identificador del Vecino que se desea guardar. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link VecinoDTO} - El Vecino guardado en el vecindario.
	 */
	@PostMapping(value = "/{vecindarioId}/vecinos/{vecinoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public VecinoDTO addVecino(@PathVariable("vecindarioId") Long vecindarioId, @PathVariable("vecinoId") Long VecinoId)
			throws EntityNotFoundException {
		VecinoEntity VecinoEntity = VecindarioVecinoService.addVecino(VecinoId, vecindarioId);
		return modelMapper.map(VecinoEntity, VecinoDTO.class);
	}

	/**
	 * Busca y devuelve todos los Vecinos que existen en el vecindario.
	 *
	 * @param vecindarioId Identificador de la vecindario que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @return JSONArray {@link VecinoDetailDTO} - Los Vecinos encontrados en el
	 *         vecindario. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{vecindarioId}/vecinos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<VecinoDetailDTO> getVecinos(@PathVariable("vecindarioId") Long vecindarioId) throws EntityNotFoundException {
		List<VecinoEntity> VecinoList = VecindarioVecinoService.getVecinos(vecindarioId);
		return modelMapper.map(VecinoList, new TypeToken<List<VecinoDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el Vecino con el id asociado dentro del vecindario con id asociado.
	 *
	 * @param vecindarioId Identificador del vecindario que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @param VecinoId      Identificador del Vecino que se esta buscando. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link VecinoDetailDTO} - El Vecino buscado
	 */
	@GetMapping(value = "/{vecindarioId}/vecinos/{VecinoId}")
	@ResponseStatus(code = HttpStatus.OK)
	public VecinoDetailDTO getVecino(@PathVariable("vecindarioId") Long vecindarioId, @PathVariable("VecinoId") Long VecinoId)
			throws EntityNotFoundException, IllegalOperationException {
		VecinoEntity VecinoEntity = VecindarioVecinoService.getVecino(vecindarioId, VecinoId);
        System.out.println("enter to get request");
		return modelMapper.map(VecinoEntity, VecinoDetailDTO.class);
	}

	/**
	 * Remplaza las instancias de Vecino asociadas a una instancia de vecindario
	 *
	 * @param vecindarioId Identificador del vecindario que se esta remplazando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param Vecinos       JSONArray {@link BookDTO} El arreglo de Vecinos nuevo para
	 *                    el vecindario.
	 * @return JSON {@link VecinoDTO} - El arreglo de Vecinos guardado en el
	 *         vecindario.
	 */
	@PutMapping(value = "/{vecindarioId}/vecinos")
	@ResponseStatus(code = HttpStatus.OK)
	public List<VecinoDTO> replaceVecinos(@PathVariable("vecindarioId") Long vecindarioId,
			@RequestBody List<VecinoDTO> Vecinos) throws EntityNotFoundException {
		List<VecinoEntity> VecinosList = modelMapper.map(Vecinos, new TypeToken<List<VecinoEntity>>() {
		}.getType());
		List<VecinoEntity> result = VecindarioVecinoService.replaceVecinos(vecindarioId, VecinosList);
		return modelMapper.map(result, new TypeToken<List<VecinoDTO>>() {
		}.getType());
	}
}
