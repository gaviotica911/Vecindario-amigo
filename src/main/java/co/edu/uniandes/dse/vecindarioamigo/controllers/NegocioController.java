package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.dto.NegocioDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.NegocioDetailDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.NegocioService;

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

@RestController
@RequestMapping("/negocios")
public class NegocioController {

    @Autowired
    private NegocioService negocioService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve todos los negocios que existen en la aplicacion.
     *
     * @return JSONArray Los negocios encontrados en la
     *         aplicación. Si no hay ninguno retorna una lista vacía.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<NegocioDetailDTO> findAll() {
        List<NegocioEntity> negocios = negocioService.getNegocios();
        return modelMapper.map(negocios, new TypeToken<List<NegocioDetailDTO>>() {
        }.getType());

    }

    /**
     * Busca el negocio con el id asociado recibido en la URL y lo devuelve.
     *
     * @param negocioId Identificador del negocio.
     * @return JSON El libro buscado
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public NegocioDetailDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        NegocioEntity negocio = negocioService.getNegocio(id);
        return modelMapper.map(negocio, NegocioDetailDTO.class);
    }

    /**
     * Crea un nuevo negocio con la informacion que se recibe en el cuerpo de la
     * petición y se regresa un objeto identico con un id auto-generado por la base
     * de datos.
     *
     * @param negocio - EL negocio que se desea guardar.
     * @return JSON - El negocio guardado con el atributo id
     *         autogenerado.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public NegocioDTO create(@RequestBody NegocioDTO negocioDto)
            throws IllegalOperationException, EntityNotFoundException {
        NegocioEntity negocioEntity = negocioService.createNegocio(modelMapper.map(negocioDto, NegocioEntity.class));
        return modelMapper.map(negocioEntity, NegocioDTO.class);
    }

    /**
     * Actualiza el negocio con el id recibido en la URL con la información que se
     * recibe en el cuerpo de la petición.
     *
     * @param negocioId Identificador del negocio que se desea actualizar. Este debe
     *                  ser
     *                  una cadena de dígitos.
     * @param negocio   El negocio que se desea guardar.
     * @return JSON - El negocio guardada.
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public NegocioDTO update(@PathVariable("id") Long id, @RequestBody NegocioDTO negocioDTO)
            throws EntityNotFoundException, IllegalOperationException {
        NegocioEntity negocioEntity = negocioService.updateNegocio(id,
                modelMapper.map(negocioDTO, NegocioEntity.class));
        return modelMapper.map(negocioEntity, NegocioDTO.class);
    }

    /**
     * Borra el negocio con el id asociado recibido en la URL.
     *
     * @param negocioId Identificador del negocio que se desea borrar. Este debe ser
     *                  una
     *                  cadena de dígitos.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
        negocioService.deleteNegocio(id);
        ;
    }
}
