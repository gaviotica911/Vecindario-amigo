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

import co.edu.uniandes.dse.vecindarioamigo.dto.PublicacionDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.VecinoDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.PublicacionVecinoService;


/**
 * Clase que implementa el recurso "publicaciones/{id}/vecino".
 *

 * @version 1.0
 */
@RestController
@RequestMapping("/publicaciones")

public class PublicacionVecinoController {
    
	@Autowired
	private PublicacionVecinoService publicacionVecinoService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Remplaza la instancia de Vecino asociada a un Publicacion.
	 *
	 * @param publicacionId    Identificador del post que se esta actualizando. Este debe
	 *                  ser una cadena de dígitos.
	 * @param vecino el vecino que se será del post.
	 * @return JSON {@link PublicacionDetailDTO} - El arreglo de posts guardado en la
	 *         vecino.
	 */
	@PutMapping(value = "/{publicacionId}/vecinos")
	@ResponseStatus(code = HttpStatus.OK)
	public PublicacionDetailDTO replaceVecino(@PathVariable("publicacionId") Long publicacionId, @RequestBody VecinoDTO vecinoDTO)
			throws EntityNotFoundException {
		PublicacionEntity publicacionEntity = publicacionVecinoService.replaceVecino(publicacionId, vecinoDTO.getId());
		return modelMapper.map(publicacionEntity, PublicacionDetailDTO.class);
	}
	
    
}
