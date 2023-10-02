package co.edu.uniandes.dse.vecindarioamigo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import javax.transaction.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.CentroComercialRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service

public class CentroComercialVecindarioService {
    @Autowired
    private VecindarioRepository vecindarioRepository;

    @Autowired
    private CentroComercialRepository centroComercialRepository;

    @Transactional
    public CentroComercialEntity replaceVecindario(Long centroComercialID, Long vecindarioID)
            throws EntityNotFoundException {

        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(vecindarioID);
        if (vecindarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);

        centroComercialEntity.get().setVecindario(vecindarioEntity.get());

        return centroComercialEntity.get();

    }

    @Transactional
    public void removeVecindario(Long centroComercialID) throws EntityNotFoundException {
        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository
                .findById(centroComercialEntity.get().getVecindario().getId());
        vecindarioEntity
                .ifPresent(vecindario -> vecindario.getCentrosComerciales().remove(centroComercialEntity.get()));

        centroComercialEntity.get().setVecindario(null);
        log.info("Termina proceso de borrar el centro comercial={0} de su vecindario", centroComercialID);
    }

}
