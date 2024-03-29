package co.edu.uniandes.dse.vecindarioamigo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;

@Repository
public interface  Zona_VerdeRepository extends JpaRepository<Zona_VerdeEntity, Long> {
    List<Zona_VerdeEntity> findByNombre(String nombre); 
}
