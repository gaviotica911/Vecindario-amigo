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

import co.edu.uniandes.dse.vecindarioamigo.dto.VecinoDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.VecinoDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.VecinoService;

@RestController
@RequestMapping("/vecinos")
public class VecinoController {
    @Autowired
    private VecinoService vecinoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VecinoDetailDTO> findAll() {
        List<VecinoEntity> vecinos = vecinoService.getVecinos();
        return modelMapper.map(vecinos, new TypeToken<List<VecinoDetailDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VecinoDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        VecinoEntity vecinoEntity = vecinoService.getVecino(id);
        return modelMapper.map(vecinoEntity, VecinoDetailDTO.class);
    }

    @PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public VecinoDTO create(@RequestBody VecinoDTO authorDTO) throws IllegalOperationException, EntityNotFoundException {
		
        
        VecinoEntity authorEntity = vecinoService.createVecino(modelMapper.map(authorDTO, VecinoEntity.class));
		return modelMapper.map(authorEntity, VecinoDTO.class);
	}

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VecinoDTO update(@PathVariable("id") Long id, @RequestBody VecinoDTO vecinoDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VecinoEntity vecinoEntity = vecinoService.updateVecino(id, modelMapper.map(vecinoDTO, VecinoEntity.class));
        return modelMapper.map(vecinoEntity, VecinoDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
        vecinoService.deleteVecino(id);
    }

}
