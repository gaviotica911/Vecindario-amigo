package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.*;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CentroComercialService {
    @Autowired
    CentroComercialRepository centroComercialRepository;

    @Transactional
    public CentroComercialEntity createCentroComercial(CentroComercialEntity centroComercialEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("The shopping centre creation process begins");
        if (!centroComercialRepository.findByNombre(centroComercialEntity.getNombre()).isEmpty()) {
            throw new IllegalOperationException("Centro comercial name already exists");
        }
        if (centroComercialEntity.getNombre().equals("")) {
            throw new IllegalOperationException("Centro comercial name cannot be empty");
        }
        if (centroComercialEntity.getVecindario() == null) {
            throw new IllegalOperationException("Neighbourhood cannot be null");
        }
        log.info("End of shopping centre creation process");
        return centroComercialRepository.save(centroComercialEntity);
    }

    @Transactional
    public List<CentroComercialEntity> getCentrosComerciales() {

        return centroComercialRepository.findAll();
    }

    @Transactional
    public CentroComercialEntity getCentroComercial(Long centroComercialID) throws EntityNotFoundException {

        Optional<CentroComercialEntity> centroComercial = centroComercialRepository.findById(centroComercialID);
        if (centroComercial.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);
        return centroComercial.get();
    }

    @Transactional
    public CentroComercialEntity updateCentroComercial(Long centroComercialID, CentroComercialEntity centroComercial)
            throws EntityNotFoundException, IllegalOperationException {
        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);
        centroComercial.setId(centroComercialID);
        return centroComercialRepository.save(centroComercial);
    }

    @Transactional
    public void deleteCentroComercial(Long centroComercialID)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Start process of deleting the shopping centre with id = {0}", centroComercialID);
        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        List<ComentarioEntity> comentarios = centroComercialEntity.get().getComentarios();
        if (!comentarios.isEmpty()) {
            throw new IllegalOperationException(
                    "Unable to delete 'centro comercial' because it has associated comments");
        }

        List<NegocioEntity> negocios = centroComercialEntity.get().getLista_negocios();
        if (!negocios.isEmpty()) {
            throw new IllegalOperationException(
                    "Unable to delete 'centro comercial' because it has associated businesses");
        }
        log.info("End process of checking the neighborhood constraints with id = {0}", centroComercialID);

        centroComercialRepository.deleteById(centroComercialID);
        log.info("End process of deleting the neighborhood with id = {0}", centroComercialID);
    }
}
