package co.edu.uniandes.dse.vecindarioamigo.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
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
import org.modelmapper.TypeToken;

import co.edu.uniandes.dse.vecindarioamigo.dto.CentroComercialDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.CentroComercialDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.VecindarioCentroComercialService;

/**
 * Clase que implementa el recurso "vecindarios/{id}/centroscomerciales".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/vecindarios")
public class VecindarioCentroComercialController {
    

	@Autowired
	private VecindarioCentroComercialService VecindarioCentroComercialService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda un centro_comercial dentro de una vecindario con la informacion que recibe el/la
	 * URL. Se devuelve el centro_comercial que se guarda en el vecindario.
	 *
	 * @param vecindarioId Identificador del vecindario que se esta actualizando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param centrocomercialId      Identificador del centro_comercial que se desea guardar. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link CentroComercialDTO} - El centro_comercial guardado en el vecindario.
	 */
	@PostMapping(value = "/{vecindarioId}/centroscomerciales/{centrocomercialId}")
	@ResponseStatus(code = HttpStatus.OK)
	public CentroComercialDTO addCentroComercial(@PathVariable("vecindarioId") Long vecindarioId, @PathVariable("centrocomercialId") Long centroComercialId)
			throws EntityNotFoundException {
		CentroComercialEntity centroComercialEntity = VecindarioCentroComercialService.addCentroComercial(centroComercialId, vecindarioId);
		return modelMapper.map(centroComercialEntity, CentroComercialDTO.class);
	}

	/**
	 * Busca y devuelve todos los centro_comercials que existen en la vecindario.
	 *
	 * @param vecindarioId Identificador de la vecindario que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @return JSONArray {@link CentroComercialDetailDTO} - Los centro_comerciales encontrados en el
	 *         vecindario. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{vecindarioId}/centroscomerciales")
	@ResponseStatus(code = HttpStatus.OK)
	public List<CentroComercialDetailDTO> getCentrosComerciales(@PathVariable("vecindarioId") Long vecindarioId) throws EntityNotFoundException {
		List<CentroComercialEntity> centroComercialList = VecindarioCentroComercialService.getCentrosComerciales(vecindarioId);
		return modelMapper.map(centroComercialList, new TypeToken<List<CentroComercialDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca el centro_comercial con el id asociado dentro de la vecindario con id asociado.
	 *
	 * @param vecindarioId Identificador de la vecindario que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @param centrocomercialId      Identificador del centro_comercial que se esta buscando. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link CentroComercialDetailDTO} - El centro_comercial buscado
	 */
	@GetMapping(value = "/{vecindarioId}/centroscomerciales/{centrocomercialId}")
	@ResponseStatus(code = HttpStatus.OK)
	public CentroComercialDetailDTO getCentroComercial(@PathVariable("vecindarioId") Long vecindarioId, @PathVariable("centrocomercialId") Long centrocomercialId)
			throws EntityNotFoundException, IllegalOperationException {
		CentroComercialEntity centroComercialEntity = VecindarioCentroComercialService.getCentroComercial(vecindarioId, centrocomercialId);
		return modelMapper.map(centroComercialEntity, CentroComercialDetailDTO.class);
	}

	/**
	 * Remplaza las instancias de CentroComercial asociadas a una instancia de vecindario
	 *
	 * @param vecindarioId Identificador del vecindario que se esta remplazando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param centroscomerciales       JSONArray {@link BookDTO} El arreglo de centro_comercials nuevo para
	 *                    la vecindario.
	 * @return JSON {@link CentroComercialDTO} - El arreglo de centro_comercials guardado en la
	 *         vecindario.
	 */
	@PutMapping(value = "/{vecindarioId}/centroscomerciales")
	@ResponseStatus(code = HttpStatus.OK)
	public List<CentroComercialDTO> replaceCentrosComerciales(@PathVariable("vecindarioId") Long vecindarioId,
			@RequestBody List<CentroComercialDTO> centroscomerciales) throws EntityNotFoundException {
		List<CentroComercialEntity> centroscomercialesList = modelMapper.map(centroscomerciales, new TypeToken<List<CentroComercialEntity>>() {
		}.getType());
		List<CentroComercialEntity> result = VecindarioCentroComercialService.replaceCentrosComerciales(vecindarioId, centroscomercialesList);
		return modelMapper.map(result, new TypeToken<List<CentroComercialDTO>>() {
		}.getType());
	}
}
