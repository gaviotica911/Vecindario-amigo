package co.edu.uniandes.dse.vecindarioamigo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import javax.transaction.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;

import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;




@Service
public class NegocioVecindarioService {

    @Autowired
    private VecindarioRepository vecindarioRepository;

    @Autowired
    private NegocioRepository negocioRepository;

    // remplaza el vecindario de un negocio
    @Transactional
    public NegocioEntity replaceVecindario(Long NegocioId, Long vecindarioId) throws EntityNotFoundException {

        Optional<NegocioEntity> NegocioEntity = negocioRepository.findById(NegocioId);
        if (NegocioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);

        Optional<VecindarioEntity> VecindarioEntity = vecindarioRepository.findById(vecindarioId);
        if (VecindarioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.NEIGHBORHOOD_NOT_FOUND);

        NegocioEntity.get().setVecindario(VecindarioEntity.get());

        return NegocioEntity.get();

    }

    // borra un negocio de un vecindario
    @Transactional
    public void removeVecindario(Long NegocioId) throws EntityNotFoundException {
        Optional<NegocioEntity> NegocioEntity = negocioRepository.findById(NegocioId);
        if (NegocioEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);

        Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(NegocioEntity.get().getVecindario().getId());
        vecindarioEntity.ifPresent(Vecindario -> Vecindario.getNegocios().remove(NegocioEntity.get()));

        NegocioEntity.get().setVecindario(null);
        
    }

}