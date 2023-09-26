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

import co.edu.uniandes.dse.vecindarioamigo.dto.*;
import co.edu.uniandes.dse.vecindarioamigo.entities.GruposDeInteresEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.GrupoDeInteresService;

@RestController
@RequestMapping("/gruposDeInteres")
public class GrupoDeInteresController {
    @Autowired
	private GrupoDeInteresService authorService;

    
	@Autowired
	private ModelMapper modelMapper;
	
    @GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<GrupoDeInteresDetailDTO> findAll() {
		List<GruposDeInteresEntity> authors = authorService.getGruposDeInteres();
		return modelMapper.map(authors, new TypeToken<List<GrupoDeInteresDetailDTO>>() {
		}.getType());
	}


    @GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public GrupoDeInteresDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
		GruposDeInteresEntity authorEntity = authorService.getGrupoDeInteres(id);
		return modelMapper.map(authorEntity, GrupoDeInteresDetailDTO.class);
	}

	/**
	 * Crea un nuevo autor con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param authorDTO {@link GrupoDeInteresDTO} - EL autor que se desea guardar.
	 * @return JSON {@link GrupoDeInteresDTO} - El autor guardado con el atributo id
	 *         autogenerado.
	 * @throws IllegalOperationException 
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public GrupoDeInteresDTO create(@RequestBody GrupoDeInteresDTO authorDTO) throws IllegalOperationException {
		GruposDeInteresEntity authorEntity = authorService.createGruposDeInteres(modelMapper.map(authorDTO, GruposDeInteresEntity.class));
		return modelMapper.map(authorEntity, GrupoDeInteresDTO.class);
	}

	/**
	 * Actualiza el autor con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param id     Identificador del autor que se desea actualizar. Este debe ser
	 *               una cadena de dígitos.
	 * @param author {@link GrupoDeInteresDTO} El autor que se desea guardar.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public GrupoDeInteresDTO update(@PathVariable("id") Long id, @RequestBody GrupoDeInteresDTO authorDTO)
			throws EntityNotFoundException {
		GruposDeInteresEntity authorEntity = authorService.updateGruposDeInteres(id, modelMapper.map(authorDTO, GruposDeInteresEntity.class));
		return modelMapper.map(authorEntity, GrupoDeInteresDTO.class);
	}

	/**
	 * Borra el autor con el id asociado recibido en la URL.
	 *
	 * @param id Identificador del autor que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
		authorService.deleteGruposDeInteres(id);
	}

}
