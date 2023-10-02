package co.edu.uniandes.dse.vecindarioamigo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.CentroComercialRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;

/**
 *
 * @author ISIS2603
 */
@Slf4j
@Service
public class CentroComercialNegocioService {
    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private CentroComercialRepository centroComercialRepository;

    @Transactional
    public NegocioEntity addNegocio(Long negocioID, Long centroComercialID) throws EntityNotFoundException {
        log.info("Start the process of adding a business to the shopping centre with id = {0}", centroComercialID);

        Optional<NegocioEntity> negocioEntity = negocioRepository.findById(negocioID);
        if (negocioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BUSINESS_NOT_FOUND);

        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        negocioEntity.get().setCentroComercial(centroComercialEntity.get());
        log.info("Finish process of adding a business to the shopping centre with id = {0}", centroComercialID);
        return negocioEntity.get();
    }

    @Transactional
    public List<NegocioEntity> getNegocios(Long centroComercialID) throws EntityNotFoundException {
        log.info("Start the process of consulting the businesses associated with the shopping centre with id = {0}",
                centroComercialID);
        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        return centroComercialEntity.get().getLista_negocios();
    }

    @Transactional
    public NegocioEntity getNegocio(Long centroComercialID, Long negocioID)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Start the process of consulting the business with id = {0} from the shopping centre with id = "
                + centroComercialID, negocioID);

        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        Optional<NegocioEntity> negocioEntity = negocioRepository.findById(negocioID);
        if (negocioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BUSINESS_NOT_FOUND);

        log.info("The process of consulting the business with id ends = {0} from the shopping centre with id = "
                + centroComercialID, negocioID);

        if (!centroComercialEntity.get().getLista_negocios().contains(negocioEntity.get()))
            throw new IllegalOperationException("The business is not associated to the shopping centre");

        return negocioEntity.get();
    }

    @Transactional
    public List<NegocioEntity> replaceNegocios(Long centroComercialID, List<NegocioEntity> negocios)
            throws EntityNotFoundException {
        log.info("Start process of updating the shopping centre with id = {0}", centroComercialID);
        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialID);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        for (NegocioEntity negocio : negocios) {
            Optional<NegocioEntity> n = negocioRepository.findById(negocio.getId());
            if (n.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.BUSINESS_NOT_FOUND);

            n.get().setCentroComercial(centroComercialEntity.get());
        }
        return negocios;
    }

    @Transactional
    public void removeNegocio(Long centroComercialId, Long negocioId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un negocio del centro comercial con id = {0}", centroComercialId);
        Optional<CentroComercialEntity> centroComercialEntity = centroComercialRepository.findById(centroComercialId);
        if (centroComercialEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.SHOPPING_MALL_NOT_FOUND);

        Optional<NegocioEntity> negocioEntity = negocioRepository.findById(negocioId);
        if (negocioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BUSINESS_NOT_FOUND);

        centroComercialEntity.get().getLista_negocios().remove(negocioEntity.get());
        log.info("Finaliza proceso de borrar un negocio del centro comercial con id = {0}", centroComercialId);
    }

}
