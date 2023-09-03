package co.edu.uniandes.dse.vecindarioamigo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;


@Repository
public interface VecinoRepository extends JpaRepository<VecinoEntity, Long> {
       
}


