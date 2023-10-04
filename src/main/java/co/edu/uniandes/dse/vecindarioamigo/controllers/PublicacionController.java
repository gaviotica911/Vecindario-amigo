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

import co.edu.uniandes.dse.vecindarioamigo.dto.PublicacionDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.PublicacionDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.PublicacionService;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {
    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PublicacionDetailDTO> findAll() {
        List<PublicacionEntity> publicaciones = publicacionService.getPublicaciones();
        return modelMapper.map(publicaciones, new TypeToken<List<PublicacionDetailDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PublicacionDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        PublicacionEntity publicacionEntity = publicacionService.getPublicacion(id);
        return modelMapper.map(publicacionEntity, PublicacionDetailDTO.class);
    }

    
    @PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PublicacionDTO create(@RequestBody PublicacionDTO bookDTO) throws IllegalOperationException, EntityNotFoundException {
		PublicacionEntity bookEntity = publicacionService.createPublicacion(modelMapper.map(bookDTO, PublicacionEntity.class));
		return modelMapper.map(bookEntity, PublicacionDTO.class);
	}


    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PublicacionDTO update(@PathVariable("id") Long id, @RequestBody PublicacionDTO publicacionDTO)
            throws EntityNotFoundException, IllegalOperationException {
        PublicacionEntity publicacionEntity = publicacionService.updatePublicacion(id, modelMapper.map(publicacionDTO, PublicacionEntity.class));
        return modelMapper.map(publicacionEntity, PublicacionDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
        publicacionService.deletePublicacion(id);
    }

}
