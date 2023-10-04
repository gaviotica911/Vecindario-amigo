package co.edu.uniandes.dse.vecindarioamigo.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.vecindarioamigo.dto.CentroComercialDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.CentroComercialDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.ComentarioDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.ComentarioDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.CentroComercialComentarioService;
import co.edu.uniandes.dse.vecindarioamigo.services.VecindarioCentroComercialService;

@RestController
@RequestMapping("/centrosComerciales")
public class CentroComercialComentarioController {
    @Autowired
    private CentroComercialComentarioService centroComercialComentarioService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{centroComercialId}/comentarios/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ComentarioDTO addComentario(@PathVariable("centroComercialId") Long centroComercialId,
            @PathVariable("comentarioId") Long comentarioId)
            throws EntityNotFoundException {
        ComentarioEntity comentarioEntity = centroComercialComentarioService.addComentario(comentarioId,
                centroComercialId);
        return modelMapper.map(comentarioEntity, ComentarioDTO.class);
    }

    @GetMapping(value = "/{centroComercialId}/comentarios")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ComentarioDetailDTO> getComentarios(@PathVariable("centroComercialId") Long centroComercialId)
            throws EntityNotFoundException {
        List<ComentarioEntity> comentarioList = centroComercialComentarioService.getComentarios(centroComercialId);

        return modelMapper.map(comentarioList, new TypeToken<List<CentroComercialDetailDTO>>() {
        }.getType());
    }

    @GetMapping(value = "/{centroComercialId}/comentarios/{comentarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ComentarioDetailDTO getComentario(@PathVariable("centroComercialId") Long centroComercialId,
            @PathVariable("comentarioId") Long comentarioId)
            throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity comentarioEntity = centroComercialComentarioService.getComentario(comentarioId,
                centroComercialId);
        return modelMapper.map(comentarioEntity, ComentarioDetailDTO.class);
    }

}
