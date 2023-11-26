package co.edu.uniandes.dse.vecindarioamigo.services;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.vecindarioamigo.entities.*;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.*;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
/**
 * Pruebas de logica de Editorials
 *
 * @author ISIS2603
 */

@DataJpaTest
@Transactional
@Import(GrupoDeInteresService.class)
public class GrupoDeInteresServiceTest {
    
	@Autowired
	private GrupoDeInteresService grupoDeInteresService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<GruposDeInteresEntity> grupoDeInteresList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from VecinoEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from GruposDeInteresEntity").executeUpdate();
	}
	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			GruposDeInteresEntity authorEntity = factory.manufacturePojo(GruposDeInteresEntity.class);
			entityManager.persist(authorEntity);
			grupoDeInteresList.add(authorEntity);
		}
	}
	/**
	 * Prueba para crear un Author.
	 * @throws IllegalOperationException 
	 */
    
@Test
	void testCreateGrupoDeInteres() throws IllegalOperationException {
		GruposDeInteresEntity newEntity = factory.manufacturePojo(GruposDeInteresEntity.class);
		GruposDeInteresEntity result = grupoDeInteresService.createGruposDeInteres(newEntity);
		assertNotNull(result);
		GruposDeInteresEntity entity = entityManager.find(GruposDeInteresEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
		assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
	}


	/**
	 * Prueba para crear un Editorial con el mismo nombre de un Editorial que ya
	 * existe.
	 *
	 * @throws IllegalOperationException
	 */
	@Test
	void testCreateEditorialWithSameName() {
		assertThrows(IllegalOperationException.class, () -> {
			GruposDeInteresEntity newEntity = factory.manufacturePojo(GruposDeInteresEntity.class);
			newEntity.setNombre(grupoDeInteresList.get(0).getNombre());
			grupoDeInteresService.createGruposDeInteres(newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Editorials.
	//  */
	// @Test
	// void testGetEditorials() {
	// 	List<GruposDeInteresEntity> list = grupoDeInteresService.getGruposDeInteres();
	// 	assertEquals(grupoDeInteresList.size(), list.size());
	// 	for (GruposDeInteresEntity entity : list) {
	// 		boolean found = false;
	// 		for (GruposDeInteresEntity storedEntity : grupoDeInteresList) {
	// 			if (entity.getId().equals(storedEntity.getId())) {
	// 				found = true;
	// 			}
	// 		}
	// 		assertTrue(found);
	// 	}
	// }

	/**
	 * Prueba para consultar un Editorial.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetEditorial() throws EntityNotFoundException {
		GruposDeInteresEntity entity = grupoDeInteresList.get(0);
		GruposDeInteresEntity resultEntity = grupoDeInteresService.getGrupoDeInteres(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
		assertEquals(entity.getNombre(), resultEntity.getNombre());
	}
	
	/**
	 * Prueba para consultar un Editorial que no existe.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetEditorialInvalid() {
		assertThrows(EntityNotFoundException.class, ()->{
			grupoDeInteresService.getGrupoDeInteres(0L);
		});
	}

	/**
	 * Prueba para actualizar una Editorial.
	 */
	@Test
	void testUpdateEditorial() throws EntityNotFoundException {
		GruposDeInteresEntity entity = grupoDeInteresList.get(0);
		GruposDeInteresEntity pojoEntity = factory.manufacturePojo(GruposDeInteresEntity.class);
		pojoEntity.setId(entity.getId());
		grupoDeInteresService.updateGruposDeInteres(entity.getId(), pojoEntity);
		GruposDeInteresEntity resp = entityManager.find(GruposDeInteresEntity.class, entity.getId());
		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getNombre(), resp.getNombre());
	}
	
	/**
	 * Prueba para actualizar una Editorial que no existe.
	 */
	@Test
	void testUpdateEditorialInvalid() {
		assertThrows(EntityNotFoundException.class, ()->{
			GruposDeInteresEntity pojoEntity = factory.manufacturePojo(GruposDeInteresEntity.class);
			pojoEntity.setId(0L);
			grupoDeInteresService.updateGruposDeInteres(0L, pojoEntity);
		});
	}

	/**
	 * Prueba para eliminar un Editorial.
	 */
	@Test
	void testDeleteEditorial() throws EntityNotFoundException, IllegalOperationException {
		GruposDeInteresEntity entity = grupoDeInteresList.get(1);
		grupoDeInteresService.deleteGruposDeInteres(entity.getId());
		GruposDeInteresEntity deleted = entityManager.find(GruposDeInteresEntity.class, entity.getId());
		assertNull(deleted);
	}
	
	/**
	 * Prueba para eliminar una Editorial que no existe.
	 */
	@Test
	void testDeleteEditorialInvalid(){
		assertThrows(EntityNotFoundException.class, ()->{
			grupoDeInteresService.deleteGruposDeInteres(0L);
		});
	}

}
