package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.edu.uniandes.dse.vecindarioamigo.dto.NegocioDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.services.OfertaNegocioService;

public class OfertaNegocioController {
    

	@Autowired
	private OfertaNegocioService ofertaNegocioService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Remplaza la instancia de Editorial asociada a un Book.
	 *
	 * @param ofertaId    Identificador del libro que se esta actualizando. Este debe
	 *                  ser una cadena de dígitos.
	 * @param negocios La editorial que se será del libro.
	 * @return JSON {@link BookDetailDTO} - El arreglo de libros guardado en la
	 *         editorial.
	 */
	@PutMapping(value = "/{ofertaId}/negocios")
	@ResponseStatus(code = HttpStatus.OK)
	public NegocioDTO replaceEditorial(@PathVariable("ofertaId") Long ofertaId, @RequestBody NegocioDTO negocioDTO)
			throws EntityNotFoundException {
		NegocioEntity negocioEntity = ofertaNegocioService.replaceNegocio(ofertaId, negocioDTO.getId());
		return modelMapper.map(negocioEntity, NegocioDTO.class);
	}
}
