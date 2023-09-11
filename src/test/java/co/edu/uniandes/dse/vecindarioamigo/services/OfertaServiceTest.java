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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.OfertaEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Oferta
 *
 * @author ISIS2603
 */
@DataJpaTest
@Transactional
@Import(OfertaService.class)
public class OfertaServiceTest {
    
    
    @Autowired
	private OfertaService ofertaService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<OfertaEntity> ofertaList = new ArrayList<>();
	
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
		entityManager.getEntityManager().createQuery("delete from NegocioEntity").executeUpdate();
		
        //eliminate data from the entity to test
		entityManager.getEntityManager().createQuery("delete from OfertaEntity");
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {

		for (int i = 0; i < 3; i++) {
			OfertaEntity OfertaEntity = factory.manufacturePojo(OfertaEntity.class);
			entityManager.persist(OfertaEntity);
			ofertaList.add(OfertaEntity);
		}

	}

    /**
	 * Prueba para crear un Oferta.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
	@Test
	void testcreateOffer() throws EntityNotFoundException, IllegalOperationException {
		OfertaEntity newEntity = factory.manufacturePojo(OfertaEntity.class);
		OfertaEntity result = ofertaService.createOffer(newEntity);
		assertNotNull(result);

		OfertaEntity entity = entityManager.find(OfertaEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
	}

    /**
	 * Prueba para consultar la lista de Ofertas.
	 */
	@Test
	void testGetOfertas() {
		List<OfertaEntity> list = ofertaService.getOfertas();
		assertEquals(ofertaList.size(), list.size());
		for (OfertaEntity entity : list) {
			boolean found = false;
			for (OfertaEntity storedEntity : ofertaList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

    /**
	 * Prueba para consultar un Oferta.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetOferta() throws EntityNotFoundException {
		OfertaEntity entity = ofertaList.get(0);
		OfertaEntity resultEntity = ofertaService.getOferta(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
	}

    /**
	 * Prueba para consultar un Oferta que no existe.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetOfertaInvalid() {
		assertThrows(EntityNotFoundException.class, () -> {
			ofertaService.getOferta(0L);
		});
	}

    /**
	 * Prueba para actualizar un Oferta.
	 */
	@Test
	void testUpdateOferta() throws EntityNotFoundException {
		//get the first instance of the list to be replicated
		OfertaEntity entity = ofertaList.get(0);

		//create a instance with random values with pojo librarie
		OfertaEntity pojoEntity = factory.manufacturePojo(OfertaEntity.class);

		//set the id of the instance to be replaced to the new instance created by pojo
		pojoEntity.setId(entity.getId());

		//update the old instance for the new instance created with pojo
		ofertaService.updateOferta(entity.getId(), pojoEntity);

		//get with entity manager the new entity instance created by pojo
		OfertaEntity resp = entityManager.find(OfertaEntity.class, entity.getId());

		//check values of pojo instance and the new instance saved in the db, in order to check if it updated well
		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getDescripcion(), resp.getDescripcion());
	}
	
	/**
	 * Prueba para actualizar un Oferta que no existe.
	 */
	@Test
	void testUpdateOfertaInvalid() {
		assertThrows(EntityNotFoundException.class, ()->{
			//create an invalid instance with random values with pojo librarie
			OfertaEntity pojoEntity = factory.manufacturePojo(OfertaEntity.class);

			//set the id of the instance to be replaced to the new invalid instance created by pojo
			pojoEntity.setId(0L);

			//update the instance with the invalid instance
			ofertaService.updateOferta(0L, pojoEntity);
		});
	}

	/**
	 * Prueba para eliminar un Oferta.
	 */
	@Test
	void testDeleteOferta() throws EntityNotFoundException, IllegalOperationException {
		//get an entity to be eliminated
		OfertaEntity entity = ofertaList.get(1);

		//eliminate the entity collected
		ofertaService.deleteOferta(entity.getId());

		//search the entity eliminated
		OfertaEntity deleted = entityManager.find(OfertaEntity.class, entity.getId());

		//check if it is eliminated
		assertNull(deleted);
	}
	
	/**
	 * Prueba para eliminar un Oferta que no existe.
	 */
	@Test
	void testDeleteOfertaInvalid(){
		assertThrows(EntityNotFoundException.class, () -> {
			ofertaService.deleteOferta(0L);
		});
	}

    	/**
	 * Prueba para eliminar un Vecindario con centros comerciales asociados.
	 */
	@Test
	void testDeleteOfertaWithNegocios() {
		assertThrows(IllegalOperationException.class, () -> {
			
			OfertaEntity ofertaEntity = ofertaList.get(0);
			NegocioEntity negocioEntity = factory.manufacturePojo(NegocioEntity.class);
			entityManager.persist(negocioEntity);
			ofertaEntity.setNegocio(negocioEntity);
			ofertaService.deleteOferta(ofertaEntity.getId());
		});
	}
}
