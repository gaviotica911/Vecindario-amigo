package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.dto.VecindarioDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.VecinoDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.VecinoVecindarioService;

@RestController
@RequestMapping("/vecinos")

public class VecinoVecindarioController {
    @Autowired
	private VecinoVecindarioService vecinoVecindarioService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Remplaza la instancia de Vecindario asociada a un Vecino.
	 *
	 * @param vecinoId    Identificador del vecino que se esta actualizando. Este debe
	 *                  ser una cadena de dígitos.
	 * @param vecindario  vecindario que se será del vecino.
	 * @return JSON {@link VecinoDetailDTO} - El arreglo de vecinos guardado en la
	 *         vecindario.
	 */
	@PutMapping(value = "/{vecinoId}/vecindarios")
	@ResponseStatus(code = HttpStatus.OK)
	public VecinoDetailDTO replaceVecindario(@PathVariable("vecinoId") Long vecinoId, @RequestBody VecindarioDTO vecindarioDTO)
			throws EntityNotFoundException {
		VecinoEntity vecinoEntity = vecinoVecindarioService.replaceVecindario(vecinoId, vecindarioDTO.getId());
		return modelMapper.map(vecinoEntity, VecinoDetailDTO.class);
	}
    
}
