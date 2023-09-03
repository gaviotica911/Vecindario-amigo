package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;

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
    CentroComercialRepository CentroComercialRepository;

    @Transactional
    public CentroComercialEntity createCentroComercial(CentroComercialEntity centroComercial)
            throws EntityNotFoundException, IllegalOperationException {

        if (!CentroComercialEntity.findByName(centroComercial.getNombre()).isEmpty()) {
            throw new IllegalOperationException("Centro comercial name already exists");
        }

        return CentroComercialRepository.save(centroComercial);
    }

    @Transactional
    public List<CentroComercialEntity> getCentrosComerciales() {

        return CentroComercialRepository.findAll();
    }

    @Transactional
    public CentroComercialEntity getCentroComercial(Long centroComercialID) throws EntityNotFoundException {

        Optional<CentroComercialEntity> centroComercial = CentroComercialRepository.findById(centroComercialID);
        if (centroComercial.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.centroComercial_NOT_FOUND);
        return centroComercial.get();
    }

    @Transactional
    public CentroComercialEntity updateCentroComercial(Long centroComercialID, CentroComercialEntity centroComercial)
            throws EntityNotFoundException, IllegalOperationException {
        Optional<CentroComercialEntity> centroComercial = CentroComercialRepository.findById(centroComercialID);
        if (centroComercial.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.centroComercial_NOT_FOUND);

        centroComercial.setId(centroComercialID);
        return CentroComercialRepository.save(centroComercial);
    }

    @Transactional
    public void deleteCentroComercial(Long centroComercialID)
            throws EntityNotFoundException, IllegalOperationException {
        Optional<CentroComercialEntity> centroComercial = CentroComercialRepository.findById(centroComercialID);
        if (CentroComercialRepository.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.centroComercial_NOT_FOUND);
        CentroComercialRepository.deleteById(centroComercialID);
    }
}
