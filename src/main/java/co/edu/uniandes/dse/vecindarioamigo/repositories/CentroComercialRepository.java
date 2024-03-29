package co.edu.uniandes.dse.vecindarioamigo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.vecindarioamigo.entities.*;

@Repository
public interface CentroComercialRepository extends JpaRepository<CentroComercialEntity, Long> {
    List<CentroComercialEntity> findByNombre(String nombre);
}
