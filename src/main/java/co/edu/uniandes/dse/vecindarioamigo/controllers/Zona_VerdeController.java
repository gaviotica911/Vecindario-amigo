package co.edu.uniandes.dse.vecindarioamigo.controllers;

import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.vecindarioamigo.dto.Zona_VerdeDTO;
import co.edu.uniandes.dse.vecindarioamigo.dto.Zona_VerdeDetailsDTO;
import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.services.Zona_VerdeService;

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
@RequestMapping("/zonas_verdes")
public class Zona_VerdeController {

    @Autowired
    private Zona_VerdeService zona_verdeService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve todos los zona_verdes que existen en la aplicacion.
     *
     * @return JSONArray Los zona_verdes encontrados en la
     *         aplicación. Si no hay ninguno retorna una lista vacía.
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Zona_VerdeDetailsDTO> findAll() {
        List<Zona_VerdeEntity> zona_verdes = zona_verdeService.getZona_Verdes();
        return modelMapper.map(zona_verdes, new TypeToken<List<Zona_VerdeDetailsDTO>>() {
        }.getType());

    }

    /**
     * Busca el zona_verde con el id asociado recibido en la URL y lo devuelve.
     *
     * @param zona_verdeId Identificador del zona_verde.
     * @return JSON La zona_verde buscado
     */
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Zona_VerdeDetailsDTO findOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        Zona_VerdeEntity zona_verde = zona_verdeService.getZona_Verde(id);
        return modelMapper.map(zona_verde, Zona_VerdeDetailsDTO.class);
    }

    /**
     * Crea un nuevo zona_verde con la informacion que se recibe en el cuerpo de la
     * petición y se regresa un objeto identico con un id auto-generado por la base
     * de datos.
     *
     * @param zona_verde - EL zona_verde que se desea guardar.
     * @return JSON - El zona_verde guardado con el atributo id
     *         autogenerado.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Zona_VerdeDTO create(@RequestBody Zona_VerdeDTO zona_verdeDto)
            throws IllegalOperationException, EntityNotFoundException {
        Zona_VerdeEntity zona_verdeEntity = zona_verdeService
                .createZona_Verde(modelMapper.map(zona_verdeDto, Zona_VerdeEntity.class));
        return modelMapper.map(zona_verdeEntity, Zona_VerdeDTO.class);
    }

    /**
     * Actualiza el zona_verde con el id recibido en la URL con la información que
     * se
     * recibe en el cuerpo de la petición.
     *
     * @param zona_verdeId Identificador del zona_verde que se desea actualizar.
     *                     Este debe
     *                     ser
     *                     una cadena de dígitos.
     * @param zona_verde   El zona_verde que se desea guardar.
     * @return JSON - El zona_verde guardada.
     */
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Zona_VerdeDTO update(@PathVariable("id") Long id, @RequestBody Zona_VerdeDTO zona_verdeDTO)
            throws EntityNotFoundException, IllegalOperationException {
        Zona_VerdeEntity zona_verdeEntity = zona_verdeService.updateZona_Verde(id,
                modelMapper.map(zona_verdeDTO, Zona_VerdeEntity.class));
        return modelMapper.map(zona_verdeEntity, Zona_VerdeDTO.class);
    }

    /**
     * Borra el zona_verde con el id asociado recibido en la URL.
     *
     * @param zona_verdeId Identificador del zona_verde que se desea borrar. Este
     *                     debe ser
     *                     una
     *                     cadena de dígitos.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) throws EntityNotFoundException, IllegalOperationException {
        zona_verdeService.deleteZona_Verde(id);
        ;
    }
}