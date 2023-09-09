package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
// import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
// import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Editorials
 *
 * @author ISIS2603
 */
@DataJpaTest
@Transactional
@Import(VecindarioService.class)
public class VecindarioServiceTest {
    
    @Autowired
	private VecindarioService vecindarioService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<VecindarioEntity> vecindarioList = new ArrayList<>();
	
	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

    /**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
        //eliminate data from relations
		entityManager.getEntityManager().createQuery("delete from CentroComercialEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from NegocioEntity").executeUpdate();

        //eliminate data from the entity to test
		entityManager.getEntityManager().createQuery("delete from VecindarioEntity");
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {

		for (int i = 0; i < 3; i++) {
			VecindarioEntity vecindarioEntity = factory.manufacturePojo(VecindarioEntity.class);
			entityManager.persist(vecindarioEntity);
			vecindarioList.add(vecindarioEntity);
		}

	}

    /**
	 * Prueba para crear un Vecindario.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testCreateVecindario() throws EntityNotFoundException, IllegalOperationException {
		VecindarioEntity newEntity = factory.manufacturePojo(VecindarioEntity.class);
		VecindarioEntity result = vecindarioService.createVecindario(newEntity);
		assertNotNull(result);

		VecindarioEntity entity = entityManager.find(VecindarioEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
	}

    /**
	 * Prueba para crear un Vecindario con el mismo nombre de un Vecindario que ya
	 * existe.
	 *
	 * @throws IllegalOperationException
	 */
	@Test
	void testCreateVecindarioWithSameNombre() {
		assertThrows(IllegalOperationException.class, () -> {
			VecindarioEntity newEntity = factory.manufacturePojo(VecindarioEntity.class);
			newEntity.setNombre(vecindarioList.get(0).getNombre()); 
			vecindarioService.createVecindario(newEntity);
		});
	}

    /**
	 * Prueba para consultar la lista de Vecindarios.
	 */
	@Test
	void testGetVecindarios() {
		List<VecindarioEntity> list = vecindarioService.getVecindarios();
		assertEquals(vecindarioList.size(), list.size());
		for (VecindarioEntity entity : list) {
			boolean found = false;
			for (VecindarioEntity storedEntity : vecindarioList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

    /**
	 * Prueba para consultar un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetVecindario() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		VecindarioEntity resultEntity = vecindarioService.getVecindario(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getNombre(), resultEntity.getNombre());
	}

    /**
	 * Prueba para consultar un vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetVecindarioInvalid() {
		assertThrows(EntityNotFoundException.class, () -> {
			vecindarioService.getVecindario(0L);
		});
	}

    /**
	 * Prueba para actualizar un Vecindario.
	 */
	@Test
	void testUpdateVecindario() throws EntityNotFoundException {
		//get the first instance of the list to be replicated
		VecindarioEntity entity = vecindarioList.get(0);

		//create a instance with random values with pojo librarie
		VecindarioEntity pojoEntity = factory.manufacturePojo(VecindarioEntity.class);

		//set the id of the instance to be replaced to the new instance created by pojo
		pojoEntity.setId(entity.getId());

		//update the old instance for the new instance created with pojo
		vecindarioService.updateVecindario(entity.getId(), pojoEntity);

		//get with entity manager the new entity instance created by mojo
		VecindarioEntity resp = entityManager.find(VecindarioEntity.class, entity.getId());

		//check values of pojo instance and the new instance saved in the db, in order to check if it updated well
		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getNombre(), resp.getNombre());
	}
	
	/**
	 * Prueba para actualizar un Vecindario que no existe.
	 */
	@Test
	void testUpdateVecindarioInvalid() {
		assertThrows(EntityNotFoundException.class, ()->{
			//create an invalid instance with random values with pojo librarie
			VecindarioEntity pojoEntity = factory.manufacturePojo(VecindarioEntity.class);

			//set the id of the instance to be replaced to the new invalid instance created by pojo
			pojoEntity.setId(0L);

			//update the instance with the invalid instance
			vecindarioService.updateVecindario(0L, pojoEntity);
		});
	}

	/**
	 * Prueba para eliminar un Vecindario.
	 */
	@Test
	void testDeleteVecindario() throws EntityNotFoundException, IllegalOperationException {
		//get an entity to be eliminated
		VecindarioEntity entity = vecindarioList.get(1);

		//eliminate the entity collected
		vecindarioService.deleteVecindario(entity.getId());

		//search the entity eliminated
		VecindarioEntity deleted = entityManager.find(VecindarioEntity.class, entity.getId());

		//check if it is eliminated
		assertNull(deleted);
	}
	
	/**
	 * Prueba para eliminar un Vecindario que no existe.
	 */
	@Test
	void testDeleteVecindarioInvalid(){
		assertThrows(EntityNotFoundException.class, () -> {
			vecindarioService.deleteVecindario(0L);
		});
	}


	/**
	 * Prueba para eliminar un Vecindario con centros comerciales asociados.
	 */
	@Test
	void testDeleteVecindarioWithCentrosComerciales() {
		assertThrows(IllegalOperationException.class, () -> {
			
			VecindarioEntity VecindarioEntity = vecindarioList.get(0);
			CentroComercialEntity centroComercialEntity = factory.manufacturePojo(CentroComercialEntity.class);
			entityManager.persist(centroComercialEntity);
			VecindarioEntity.getCentrosComerciales().add(centroComercialEntity);
			vecindarioService.deleteVecindario(VecindarioEntity.getId());
		});
	}

	/**
	 * Prueba para eliminar un Vecindario con negocios asociados.
	 */
	@Test
	void testDeleteVecindarioWithNegocio() {
		assertThrows(IllegalOperationException.class, () -> {
			
			VecindarioEntity VecindarioEntity = vecindarioList.get(0);
			NegocioEntity negocioEntity = factory.manufacturePojo(NegocioEntity.class);
			entityManager.persist(negocioEntity);
			VecindarioEntity.getNegocios().add(negocioEntity);
			vecindarioService.deleteVecindario(VecindarioEntity.getId());
		});
	}
}
