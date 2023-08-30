package co.edu.uniandes.dse.vecindarioamigo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;

@Repository
public interface  NegocioRepository extends JpaRepository<NegocioEntity, Long> {
        
}