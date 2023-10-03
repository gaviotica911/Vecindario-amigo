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

import co.edu.uniandes.dse.vecindarioamigo.dto.Zona_VerdeDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.Zona_VerdeDetailsDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.VecindarioZona_VerdeService;

public class VecindarioZona_VerdeController {
    

	@Autowired
	private VecindarioZona_VerdeService vecindarioZona_VerdeService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Guarda una Zona_Verde dentro de una vecindario con la informacion que recibe el/la
	 * URL. Se devuelve la Zona_Verde que se guarda en el vecindario.
	 *
	 * @param vecindarioId Identificador del vecindario que se esta actualizando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param zonaVerdeId      Identificador de la Zona_Verde que se desea guardar. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link Zona_VerdeDTO} - El Zona_Verde guardado en el vecindario.
	 */
	@PostMapping(value = "/{vecindarioId}/zonasverdes/{zonaVerdeId}")
	@ResponseStatus(code = HttpStatus.OK)
	public Zona_VerdeDTO addZona_Verde(@PathVariable("vecindarioId") Long vecindarioId, @PathVariable("zonaVerdeId") Long zonaVerdeId)
			throws EntityNotFoundException {
		Zona_VerdeEntity Zona_VerdeEntity = vecindarioZona_VerdeService.addZona_Verde(zonaVerdeId, vecindarioId);
		return modelMapper.map(Zona_VerdeEntity, Zona_VerdeDTO.class);
	}

	/**
	 * Busca y devuelve todos las Zona_Verdes que existen en el vecindario.
	 *
	 * @param vecindarioId Identificador de la vecindario que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @return JSONArray {@link Zona_VerdeDetailDTO} - Las Zona_Verdes encontrados en el
	 *         vecindario. Si no hay ninguno retorna una lista vacía.
	 */
	@GetMapping(value = "/{vecindarioId}/zonasverdes")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Zona_VerdeDetailsDTO> getZona_Verdes(@PathVariable("vecindarioId") Long vecindarioId) throws EntityNotFoundException {
		List<Zona_VerdeEntity> Zona_VerdeList = vecindarioZona_VerdeService.getZonas_Verdes(vecindarioId);
		return modelMapper.map(Zona_VerdeList, new TypeToken<List<Zona_VerdeDetailsDTO>>() {
		}.getType());
	}

	/**
	 * Busca la Zona_Verde con el id asociado dentro del vecindario con id asociado.
	 *
	 * @param vecindarioId Identificador del vecindario que se esta buscando. Este
	 *                    debe ser una cadena de dígitos.
	 * @param zonaVerdeId      Identificador del Zona_Verde que se esta buscando. Este debe
	 *                    ser una cadena de dígitos.
	 * @return JSON {@link Zona_VerdeDetailDTO} - El Zona_Verde buscado
	 */
	@GetMapping(value = "/{vecindarioId}/zonasverdes/{zonaVerdeId}")
	@ResponseStatus(code = HttpStatus.OK)
	public Zona_VerdeDetailsDTO getZona_Verde(@PathVariable("vecindarioId") Long vecindarioId, @PathVariable("zonaVerdeId") Long zonaVerdeId)
			throws EntityNotFoundException, IllegalOperationException {
		Zona_VerdeEntity Zona_VerdeEntity = vecindarioZona_VerdeService.getZona_Verde(vecindarioId, zonaVerdeId);
		return modelMapper.map(Zona_VerdeEntity, Zona_VerdeDetailsDTO.class);
	}

	/**
	 * Remplaza las instancias de Zona_Verde asociadas a una instancia de vecindario
	 *
	 * @param vecindarioId Identificador del vecindario que se esta remplazando.
	 *                    Este debe ser una cadena de dígitos.
	 * @param Zona_Verdes       JSONArray {@link BookDTO} El arreglo de Zona_Verdes nuevo para
	 *                    el vecindario.
	 * @return JSON {@link Zona_VerdeDTO} - El arreglo de Zona_Verdes guardado en el
	 *         vecindario.
	 */
	@PutMapping(value = "/{vecindarioId}/zonasverdes")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Zona_VerdeDTO> replaceZona_Verdes(@PathVariable("vecindarioId") Long vecindarioId,
			@RequestBody List<Zona_VerdeDTO> Zona_Verdes) throws EntityNotFoundException {
		List<Zona_VerdeEntity> Zona_VerdesList = modelMapper.map(Zona_Verdes, new TypeToken<List<Zona_VerdeEntity>>() {
		}.getType());
		List<Zona_VerdeEntity> result = vecindarioZona_VerdeService.replaceZonas_Verdes(vecindarioId, Zona_VerdesList);
		return modelMapper.map(result, new TypeToken<List<Zona_VerdeDTO>>() {
		}.getType());
	}
}
