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
import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.CentroComercialService;

@RestController
@RequestMapping("/centrosComerciales")
public class CentroComercialController {
    @Autowired
    private CentroComercialService centroComercialService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<CentroComercialDetailDTO> findAll() {
        List<CentroComercialEntity> centrosComerciales = centroComercialService.getCentrosComerciales();
        return modelMapper.map(centrosComerciales, new TypeToken<List<CentroComercialDetailDTO>>() {
        }.getType());

    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CentroComercialDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        CentroComercialEntity centroComercial = centroComercialService.getCentroComercial(id);
        return modelMapper.map(centroComercial, CentroComercialDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CentroComercialDTO create(@RequestBody CentroComercialDTO centroComercialDTO)
            throws IllegalOperationException, EntityNotFoundException {
        CentroComercialEntity centroComercialEntity = centroComercialService
                .createCentroComercial(modelMapper.map(centroComercialDTO, CentroComercialEntity.class));
        return modelMapper.map(centroComercialEntity, CentroComercialDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CentroComercialDTO update(@PathVariable("id") Long id, @RequestBody CentroComercialDTO centroComercialDTO)
            throws EntityNotFoundException, IllegalOperationException {
        CentroComercialEntity centroComercialEntity = centroComercialService.updateCentroComercial(id,
                modelMapper.map(centroComercialDTO, CentroComercialEntity.class));
        return modelMapper.map(centroComercialEntity, CentroComercialDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
        centroComercialService.deleteCentroComercial(id);
        ;
    }

}
