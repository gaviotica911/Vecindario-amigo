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
import org.springframework.web.bind.annotation.ResponseStatus;

import co.edu.uniandes.dse.vecindarioamigo.dto.NegocioDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.NegocioDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;

public class VecindarioNegocioController {
    
    

	@Autowired
	private co.edu.uniandes.dse.vecindarioamigo.services.VecindarioNegocioService vecindarioNegocioService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda un Negocio dentro de una vecindario con la informacion que recibe el/la
	 * URL. Se devuelve el Negocio que se guarda en el vecindario.
	 *
	 * @param vecindarioId Identificador del vecindario que se esta actualizando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param NegocioId      Identificador del Negocio que se desea guardar. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link NegocioDTO} - El Negocio guardado en el vecindario.
	 */
	@PostMapping(value = "/{vecindarioId}/negocios/{NegocioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public NegocioDTO addNegocio(@PathVariable("vecindarioId") Long vecindarioId, @PathVariable("negocioId") Long NegocioId)
			throws EntityNotFoundException {
		NegocioEntity NegocioEntity = vecindarioNegocioService.addNegocio(NegocioId, vecindarioId);
		return modelMapper.map(NegocioEntity, NegocioDTO.class);
	}

	/**
	 * Busca y devuelve todos los Negocios que existen en el vecindario.
	 *
	 * @param vecindarioId Identificador de la vecindario que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @return JSONArray {@link NegocioDetailDTO} - Los Negocios encontrados en el
	 *         vecindario. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{vecindarioId}/negocios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<NegocioDetailDTO> getNegocios(@PathVariable("vecindarioId") Long vecindarioId) throws EntityNotFoundException {
		List<NegocioEntity> NegocioList = vecindarioNegocioService.getNegocios(vecindarioId);
		return modelMapper.map(NegocioList, new TypeToken<List<NegocioDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el Negocio con el id asociado dentro del vecindario con id asociado.
	 *
	 * @param vecindarioId Identificador del vecindario que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @param NegocioId      Identificador del Negocio que se esta buscando. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link NegocioDetailDTO} - El Negocio buscado
	 */
	@GetMapping(value = "/{vecindarioId}/negocios/{NegocioId}")
	@ResponseStatus(code = HttpStatus.OK)
	public NegocioDetailDTO getNegocio(@PathVariable("vecindarioId") Long vecindarioId, @PathVariable("negocioId") Long NegocioId)
			throws EntityNotFoundException, IllegalOperationException {
		NegocioEntity NegocioEntity = vecindarioNegocioService.getNegocio(vecindarioId, NegocioId);
		return modelMapper.map(NegocioEntity, NegocioDetailDTO.class);
	}

	/**
	 * Remplaza las instancias de Negocio asociadas a una instancia de vecindario
	 *
	 * @param vecindarioId Identificador del vecindario que se esta remplazando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param Negocios       JSONArray {@link BookDTO} El arreglo de Negocios nuevo para
	 *                    el vecindario.
	 * @return JSON {@link NegocioDTO} - El arreglo de Negocios guardado en el
	 *         vecindario.
	 */
	@PutMapping(value = "/{vecindarioId}/negocios")
	@ResponseStatus(code = HttpStatus.OK)
	public List<NegocioDTO> replaceNegocios(@PathVariable("vecindarioId") Long vecindarioId,
			@RequestBody List<NegocioDTO> Negocios) throws EntityNotFoundException {
		List<NegocioEntity> NegociosList = modelMapper.map(Negocios, new TypeToken<List<NegocioEntity>>() {
		}.getType());
		List<NegocioEntity> result = vecindarioNegocioService.replaceNegocios(vecindarioId, NegociosList);
		return modelMapper.map(result, new TypeToken<List<NegocioDTO>>() {
		}.getType());
	}
}
