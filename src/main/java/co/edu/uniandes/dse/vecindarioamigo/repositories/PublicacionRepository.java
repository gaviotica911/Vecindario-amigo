package co.edu.uniandes.dse.vecindarioamigo.repositories;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;


@Repository
public interface PublicacionRepository extends JpaRepository<PublicacionEntity, Long> {
}



