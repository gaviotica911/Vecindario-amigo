package co.edu.uniandes.dse.vecindarioamigo.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.vecindarioamigo.entities.GruposDeInteresEntity;


@Repository
public interface GrupoDeInteresRepository extends JpaRepository<GruposDeInteresEntity, Long> {
   

}