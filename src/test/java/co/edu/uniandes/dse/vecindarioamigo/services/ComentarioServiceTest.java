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
@Import(ComentarioService.class)
class ComentarioServiceTest {

    @Autowired
	private ComentarioService comentarioService;

    @Autowired
	private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ComentarioEntity> comentatioList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
@BeforeEach
void setUp() {
	clearData();
	insertData();
	}


    private void clearData() {
		entityManager.getEntityManager().createQuery("delete from Zona_VerdeEntity");
		entityManager.getEntityManager().createQuery("delete from ComentarioEntity");
        entityManager.getEntityManager().createQuery("delete from CentroComercialEntity");
    	entityManager.getEntityManager().createQuery("delete from NegocioEntity");
		entityManager.getEntityManager().createQuery("delete from PublicacionEntity");


	}


	private void insertData() {

		for (int i = 0; i < 3; i++) {
			ComentarioEntity comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
			entityManager.persist(comentarioEntity);
			comentatioList.add(comentarioEntity);
		}
    }
	/**
	 * Prueba para crear un Editorial.
	 *
	 * @throws EntityNotFoundException, IllegalOperationException
	 */
    @Test
	void testCreateCometario() throws EntityNotFoundException, IllegalOperationException {
		ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
		ComentarioEntity result = comentarioService.createComentarios(newEntity);
		assertNotNull(result);

		ComentarioEntity entity = entityManager.find(ComentarioEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
		assertEquals(newEntity.getNombre(), entity.getNombre());
	}
/**
	 * Prueba para crear un Editorial con el mismo nombre de un Editorial que ya
	 * existe.
	 *
	 * @throws IllegalOperationException
	 */
	@Test
	void testCreateComentarioWithSameName() {
		assertThrows(IllegalOperationException.class, () -> {
			ComentarioEntity newEntity = factory.manufacturePojo(ComentarioEntity.class);
			newEntity.setNombre(comentatioList.get(0).getNombre());
			comentarioService.createComentarios(newEntity);
		});
    }
    /**
	 * Prueba p consaraultar un Editorial.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetComentarios() {
		List<ComentarioEntity> list = comentarioService.getComentarios();
		assertEquals(comentatioList.size(), list.size());
		for (ComentarioEntity entity : list) {
			boolean found = false;
			for (ComentarioEntity storedEntity :comentatioList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

    /**
	 * Prueba p consaraultar un Editorial.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetComentario() throws EntityNotFoundException {
		ComentarioEntity entity = comentatioList.get(0);
		ComentarioEntity resultEntity = comentarioService.getComentario(entity.getId());
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
	void testGetCoomentarioInvalid() {
		assertThrows(EntityNotFoundException.class, ()->{
			comentarioService.getComentario(0L);
		});
	} 

    @Test
	void testUpdateComentario() throws EntityNotFoundException {
		ComentarioEntity entity = comentatioList.get(0);
		ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
		pojoEntity.setId(entity.getId());
		comentarioService.updateComentarios(entity.getId(), pojoEntity);
		ComentarioEntity resp = entityManager.find(ComentarioEntity.class, entity.getId());
		assertEquals(pojoEntity.getId(), resp.getId());
		assertEquals(pojoEntity.getNombre(), resp.getNombre());
	}
/**
	 * Prueba para actualizar un comnetario que no existe.
	 */
	@Test
	void testUpdateEditorialInvalid() {
		assertThrows(EntityNotFoundException.class, ()->{
			ComentarioEntity pojoEntity = factory.manufacturePojo(ComentarioEntity.class);
			pojoEntity.setId(0L);
			comentarioService.updateComentarios(0L, pojoEntity);
		});
	}
	/**
	 * Prueba para eliminar un Editorial.
	 */
	@Test
	void testDeleteComentario() throws EntityNotFoundException, IllegalOperationException {
		ComentarioEntity entity = comentatioList.get(1);
		comentarioService.deleteComentarios(entity.getId());
		ComentarioEntity deleted = entityManager.find(ComentarioEntity.class, entity.getId());
		assertNull(deleted);
	}
	/**
	 * Prueba para eliminar una Editorial que no existe.
	 */
	@Test
	void testDeleteComentarioinvalid(){
		assertThrows(EntityNotFoundException.class, ()->{
			comentarioService.deleteComentarios(0L);
		});
	}
	
}