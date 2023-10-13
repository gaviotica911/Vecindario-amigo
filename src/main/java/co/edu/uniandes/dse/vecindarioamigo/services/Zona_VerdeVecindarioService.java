package co.edu.uniandes.dse.vecindarioamigo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import javax.transaction.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;

import co.edu.uniandes.dse.vecindarioamigo.repositories.Zona_VerdeRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;




@Service
public class Zona_VerdeVecindarioService {

    @Autowired
    private VecindarioRepository vecindarioRepository;

    @Autowired
    private Zona_VerdeRepository zona_VerdeRepository;
    

    // borra un Zona_Verde de un vecindario
    @Transactional
    public void removeVecindario(Long Zona_VerdeId) throws EntityNotFoundException {
        Optional<Zona_VerdeEntity> Zona_VerdeEntity = zona_VerdeRepository.findById(Zona_VerdeId);
        if (Zona_VerdeEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.ZONA_VERDE_NOT_FOUND);

        Optional<VecindarioEntity> vecindarioEntity = vecindarioRepository.findById(Zona_VerdeEntity.get().getVecindario().getId());
        vecindarioEntity.ifPresent(Vecindario -> Vecindario.getZonasVerdes().remove(Zona_VerdeEntity.get()));

        Zona_VerdeEntity.get().setVecindario(null);
        
    }


    // Mira a que vencidario le pertenece la zonaverde
    public VecindarioEntity getVecindario(Long Zona_VerdeId) throws EntityNotFoundException {
        Optional<Zona_VerdeEntity> Zona_VerdeEntity = zona_VerdeRepository.findById(Zona_VerdeId);
        if (Zona_VerdeEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.ZONA_VERDE_NOT_FOUND);

        vecindarioRepository.findById(Zona_VerdeEntity.get().getVecindario().getId());
    
        return  Zona_VerdeEntity.get().getVecindario();
    }

}