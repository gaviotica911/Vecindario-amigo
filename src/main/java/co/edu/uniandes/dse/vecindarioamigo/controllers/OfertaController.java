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

import co.edu.uniandes.dse.vecindarioamigo.dto.OfertaDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.OfertaEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.OfertaService;

@RestController
@RequestMapping("/ofertas")
public class OfertaController {

	@Autowired
	private OfertaService OfertaService;

	@Autowired
	private ModelMapper modelMapper;

	 /**
     * Busca la oferta con el id asociado recibido en la URL y lo devuelve.
     *
     * @param ofertaId Identificador de la oferta que se esta buscando.
     * Este debe ser una cadena de dígitos.
     * @return JSON {@link ofertaDetailDTO} - La oferta buscada
     */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public OfertaDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		OfertaEntity ofertaEntity = OfertaService.getOferta(id);
		return modelMapper.map(ofertaEntity, OfertaDTO.class);
	}

	/**
     * Busca y devuelve todas las ofertas que existen en la aplicacion.
     *
     * @return JSONArray {@link ofertaDetailDTO} - Las ofertas
     * encontrados en la aplicación. Si no hay ninguno retorna una lista vacía.
     */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<OfertaDTO> findAll() {
		List<OfertaEntity> ofertas = OfertaService.getOfertas();
		return modelMapper.map(ofertas, new TypeToken<List<OfertaDTO>>() {
		}.getType());
	}

	/**
     * Crea una nueva oferta con la informacion que se recibe en el cuerpo de
     * la petición y se regresa un objeto identico con un id auto-generado por
     * la base de datos.
     *
     * @param oferta {@link ofertaDTO} - El oferta que se desea
     * guardar.
     * @return JSON {@link ofertaDTO} - El oferta guardado con el atributo
     * id autogenerado.
     */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public OfertaDTO create(@RequestBody OfertaDTO ofertaDTO) throws IllegalOperationException {
		OfertaEntity ofertaEntity = OfertaService
				.createOffer(modelMapper.map(ofertaDTO, OfertaEntity.class));
		return modelMapper.map(ofertaEntity, OfertaDTO.class);
	}

	 /**
     * Actualiza la oferta con el id recibido en la URL con la informacion
     * que se recibe en el cuerpo de la petición.
     *
     * @param ofertaId Identificador del oferta que se desea
     * actualizar. Este debe ser una cadena de dígitos.
     * @param oferta {@link ofertaDTO} El oferta que se desea
     * guardar.
     * @return JSON {@link ofertaDTO} - El oferta guardada.
     */
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public OfertaDTO update(@PathVariable("id") Long id, @RequestBody OfertaDTO ofertaDTO)
			throws EntityNotFoundException {
		OfertaEntity ofertaEntity = OfertaService.updateOferta(id,
				modelMapper.map(ofertaDTO, OfertaEntity.class));
		return modelMapper.map(ofertaEntity, OfertaDTO.class);
	}

	/**
     * Borra la oferta con el id asociado recibido en la URL.
     *
     * @param id Identificador de la oferta que se desea borrar.
     * Este debe ser una cadena de dígitos.
     */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		OfertaService.deleteOferta(id);
	}
}
