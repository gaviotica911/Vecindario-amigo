package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ VecindarioService.class, VecindarioVecinoService.class })
public class VecindarioVecinoServiceTest {
    
	@Autowired
	private VecindarioVecinoService vecindarioVecinoService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<VecindarioEntity> vecindarioList = new ArrayList<>();
	private List<VecinoEntity> vecinoList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from VecindarioEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			VecinoEntity neighbor = factory.manufacturePojo(VecinoEntity.class);
			entityManager.persist(neighbor);
			vecinoList.add(neighbor);
		}

		for (int i = 0; i < 3; i++) {
			VecindarioEntity entity = factory.manufacturePojo(VecindarioEntity.class);
			entityManager.persist(entity);
			vecindarioList.add(entity);
			if (i == 0) {
				vecinoList.get(i).setVecindario(entity);
				entity.getVecinos().add(vecinoList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar un Vecino existente a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testaddVecino() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		VecinoEntity VecinoEntity = vecinoList.get(1);
		VecinoEntity response = vecindarioVecinoService.addVecino(VecinoEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(VecinoEntity.getId(), response.getId());
	}
	
	/**
	 * Prueba para asociar un Vecino que no existe a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidVecino() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			vecindarioVecinoService.addVecino(0L, entity.getId());
		});
	}
	
	/**
	 * Prueba para asociar un Vecino a un Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testaddVecinoInvalidVecindario() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecinoEntity VecinoEntity = vecinoList.get(1);
			vecindarioVecinoService.addVecino(VecinoEntity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una colección de instancias de Vecino asociadas a una
	 * instancia Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testgetVecinos() throws EntityNotFoundException {
		List<VecinoEntity> list = vecindarioVecinoService.getVecinos(vecindarioList.get(0).getId());
		assertEquals(1, list.size());
	}
	
	/**
	 * Prueba para obtener una colección de instancias de Vecino asociadas a una
	 * instancia Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testgetVecinosInvalidVecindario() {
		assertThrows(EntityNotFoundException.class,()->{
			vecindarioVecinoService.getVecinos(0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de Vecino asociada a una instancia Vecindario.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.centroComercialstore.exceptions.BusinessLogicException
	 */
	@Test
	void testgetVecino() throws EntityNotFoundException, IllegalOperationException {
		VecindarioEntity entity = vecindarioList.get(0);
		VecinoEntity VecinoEntity = vecinoList.get(0);
		VecinoEntity response = vecindarioVecinoService.getVecino(entity.getId(), VecinoEntity.getId());

		assertEquals(VecinoEntity.getId(), response.getId());
		assertEquals(VecinoEntity.getNombre(), response.getNombre());
        assertEquals(VecinoEntity.getDescripcion(), response.getDescripcion());
		assertEquals(VecinoEntity.getPorfile_pic(), response.getPorfile_pic());
		assertEquals(VecinoEntity.getEdad(), response.getEdad());
	}
	
	/**
	 * Prueba para obtener una instancia de Vecino asociada a una instancia Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 *
	 */
	@Test
	void testgetVecinoInvalidVecindario()  {
		assertThrows(EntityNotFoundException.class, ()->{
			VecinoEntity VecinoEntity = vecinoList.get(0);
			vecindarioVecinoService.getVecino(0L, VecinoEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener una instancia de Vecino que no existe asociada a una instancia Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetInvalidVecino()  {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			vecindarioVecinoService.getVecino(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de Vecino asociada a una instancia Vecindario
	 * que no le pertenece.
	 *
	 * @throws co.edu.uniandes.dse.vecindarioamigo.exceptions
	 */
	@Test
	public void getVecinoNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			VecindarioEntity entity = vecindarioList.get(0);
			VecinoEntity VecinoEntity = vecinoList.get(1);
			vecindarioVecinoService.getVecino(entity.getId(), VecinoEntity.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Vecino asociadas a una instancia de
	 * Vecindario.
	 */
	@Test
	void testreplaceVecinos() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		List<VecinoEntity> list = vecinoList.subList(1, 3);
		vecindarioVecinoService.replaceVecinos(entity.getId(), list);

		for (VecinoEntity Vecino : list) {
			VecinoEntity b = entityManager.find(VecinoEntity.class, Vecino.getId());
			assertTrue(b.getVecindario().equals(entity));
		}
	}
	
	/**
	 * Prueba para remplazar las instancias de Vecino que no existen asociadas a una instancia de
	 * Vecindario.
	 */
	@Test
	void testReplaceInvalidCentrosComerciales() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			
			List<VecinoEntity> Vecinos = new ArrayList<>();
			VecinoEntity newVecino = factory.manufacturePojo(VecinoEntity.class);
			newVecino.setId(0L);
			Vecinos.add(newVecino);
			
			vecindarioVecinoService.replaceVecinos(entity.getId(), Vecinos);
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de Vecino asociadas a una instancia de
	 * Vecindario que no existe.
	 */
	@Test
	void testreplaceVecinosInvalidVecindario() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			List<VecinoEntity> list = vecinoList.subList(1, 3);
			vecindarioVecinoService.replaceVecinos(0L, list);
		});
	}
}
