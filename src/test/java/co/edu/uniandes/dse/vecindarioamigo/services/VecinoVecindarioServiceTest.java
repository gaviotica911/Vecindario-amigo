package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VecinoVecindarioService.class)

public class VecinoVecindarioServiceTest {
    
	private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private VecinoVecindarioService vecinoVecindarioService;

	@Autowired
	private TestEntityManager entityManager;

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
			VecinoEntity offer = factory.manufacturePojo(VecinoEntity.class);
			entityManager.persist(offer);
			vecinoList.add(offer);
		}
		for (int i = 0; i < 3; i++) {
			VecindarioEntity entity = factory.manufacturePojo(VecindarioEntity.class);
			entityManager.persist(entity);
			vecindarioList.add(entity);
			if (i == 0) {
				vecinoList.get(i).setVecindario(entity);
			}
		}
	}

	/**
	 * Prueba para asociar un Vecino existente a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddVecindario() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		VecinoEntity vecinoEntity = vecinoList.get(1);
		VecindarioEntity response = vecinoVecindarioService.addVecindario(entity.getId(), vecinoEntity.getId());

		assertNotNull(response);
		assertEquals(entity.getId(), response.getId());
	}

	/**
	 * Prueba para asociar un Vecino existente a un Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidVecindario() {
		assertThrows(EntityNotFoundException.class, () -> {
			VecinoEntity VecinoEntity = vecinoList.get(1);
			vecinoVecindarioService.addVecindario(0L, VecinoEntity.getId());
		});
	}

	/**
	 * Prueba para asociar un Vecino que no existe a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddVecindarioInvalidVecino() {
		assertThrows(EntityNotFoundException.class, () -> {
			VecindarioEntity entity = vecindarioList.get(0);
			vecinoVecindarioService.addVecindario(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetVecindario() throws EntityNotFoundException {
		VecinoEntity entity = vecinoList.get(0);
		VecindarioEntity resultEntity = vecinoVecindarioService.getVecindario(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getVecindario().getId(), resultEntity.getId());
	}

	/**
	 * Prueba para consultar el Vecindario de una Vecino que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetVecindarioInvalidVecino() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			vecinoVecindarioService.getVecindario(0L);
		});
	}

	/**
	 * Prueba para consultar un Vecindario que no tiene Vecino.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetVecindarioNotVecino() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			VecinoEntity vecino = vecinoList.get(1);
			vecinoVecindarioService.getVecindario(vecino.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Vecinos asociadas a una instancia de
	 * Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceVecindario() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		vecinoVecindarioService.replaceVecindario(vecinoList.get(1).getId(), entity.getId());

		VecinoEntity vecino = entityManager.find(VecinoEntity.class, vecinoList.get(1).getId());
		assertTrue(vecino.getVecindario().equals(entity));
	}

	/**
	 * Prueba para remplazar las instancias de Vecinos asociadas a una instancia de
	 * un Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidVecindario() {
		assertThrows(EntityNotFoundException.class, () -> {
			vecinoVecindarioService.replaceVecindario(vecinoList.get(1).getId(), 0L);
		});
	}

	/**
	 * Prueba para remplazar las instancias de Vecinos que no existen asociadas a una
	 * instancia de Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceVecindarioInvalidVecino() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			VecindarioEntity entity = vecindarioList.get(0);
			vecinoVecindarioService.replaceVecindario(0L, entity.getId());

		});
	}

	/**
	 * Prueba para desasociar un Vecino existente de un Vecindario existente.
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemoveVecino() throws EntityNotFoundException {
		vecinoVecindarioService.removeVecindario(vecinoList.get(0).getId());
		VecinoEntity Vecino = entityManager.find(VecinoEntity.class, vecinoList.get(0).getId());
		assertNull(Vecino.getVecindario());
	}
	
	/**
	 * Prueba para desasociar un Vecino que no existe de un Vecindario existente.
	 * 
	 * @throws EntityNotFoundException
	 *
	 * 
	 */
	@Test
	public void testRemoveInvalidVecino(){
		assertThrows(EntityNotFoundException.class, ()->{
			vecinoVecindarioService.removeVecindario(0L);
		});
	}

	/**
	 * Prueba para desasociar un Vecino existente de un Vecindario existente.
	 */
	@Test
	void testRemoveVecindario() {
		assertThrows(EntityNotFoundException.class, () -> {
			vecinoVecindarioService.removeVecindario(vecinoList.get(1).getId());
		});
	}
    
}
