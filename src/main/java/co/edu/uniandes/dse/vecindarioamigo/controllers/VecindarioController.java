package co.edu.uniandes.dse.vecindarioamigo.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;
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

import co.edu.uniandes.dse.vecindarioamigo.dto.VecindarioDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.VecindarioDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.VecindarioService;

/**
 * Clase que implementa el recurso "vecindarios".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/vecindarios")
public class VecindarioController {
    

	@Autowired
	private VecindarioService vecindarioService;

	@Autowired
	private ModelMapper modelMapper;

	 /**
     * Busca el vecindario con el id asociado recibido en la URL y lo devuelve.
     *
     * @param vecindarioId Identificador de la vecindario que se esta buscando.
     * Este debe ser una cadena de dígitos.
     * @return JSON {@link VecindarioDetailDTO} - El vecindario buscado
     */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public VecindarioDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		VecindarioEntity VecindarioEntity = vecindarioService.getVecindario(id);
		return modelMapper.map(VecindarioEntity, VecindarioDetailDTO.class);
	}

	/**
     * Busca y devuelve todos los vecindarios que existen en la aplicacion.
     *
     * @return JSONArray {@link VecindarioDetailDTO} - Las vecindarios
     * encontrados en la aplicación. Si no hay ninguno retorna una lista vacía.
     */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public Page<VecindarioDetailDTO> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
											@RequestParam(value = "size", defaultValue = "10") int size) {
		PageRequest pageable = PageRequest.of(page, size);
		Page<VecindarioEntity> vecindariosPage = vecindarioService.getVecindarios(pageable);
		return vecindariosPage.map(vecindario -> modelMapper.map(vecindario, VecindarioDetailDTO.class));
	}

	/**
     * Crea una nueva vecindario con la informacion que se recibe en el cuerpo de
     * la petición y se regresa un objeto identico con un id auto-generado por
     * la base de datos.
     *
     * @param vecindario {@link VecindarioDTO} - El vecindario que se desea
     * guardar.
     * @return JSON {@link VecindarioDTO} - El vecindario guardado con el atributo
     * id autogenerado.
     */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public VecindarioDTO create(@RequestBody VecindarioDTO VecindarioDTO) throws IllegalOperationException {
		VecindarioEntity VecindarioEntity = vecindarioService
				.createVecindario(modelMapper.map(VecindarioDTO, VecindarioEntity.class));
		return modelMapper.map(VecindarioEntity, VecindarioDTO.class);
	}

	 /**
     * Actualiza el vecindario con el id recibido en la URL con la informacion
     * que se recibe en el cuerpo de la petición.
     *
     * @param vecindarioId Identificador del vecindario que se desea
     * actualizar. Este debe ser una cadena de dígitos.
     * @param vecindario {@link VecindarioDTO} El vecindario que se desea
     * guardar.
     * @return JSON {@link VecindarioDTO} - El vecindario guardada.
     */
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public VecindarioDTO update(@PathVariable("id") Long id, @RequestBody VecindarioDTO VecindarioDTO)
			throws EntityNotFoundException {
		VecindarioEntity VecindarioEntity = vecindarioService.updateVecindario(id,
				modelMapper.map(VecindarioDTO, VecindarioEntity.class));
		return modelMapper.map(VecindarioEntity, VecindarioDTO.class);
	}

	/**
     * Borra la vecindario con el id asociado recibido en la URL.
     *
     * @param id Identificador de la vecindario que se desea borrar.
     * Este debe ser una cadena de dígitos.
     */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		vecindarioService.deleteVecindario(id);
	}
}
