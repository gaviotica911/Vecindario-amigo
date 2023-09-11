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

import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import({ PublicacionService.class, PublicacionVecinoService.class })

public class PublicacionVecinoServiceTest {
    
	private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private PublicacionVecinoService publicacionVecinoService;

	@Autowired
	private TestEntityManager entityManager;

	private List<VecinoEntity> vecinoList = new ArrayList<>();
	private List<PublicacionEntity> publicacionList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from PublicacionEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from VecinoEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			PublicacionEntity offer = factory.manufacturePojo(PublicacionEntity.class);
			entityManager.persist(offer);
			publicacionList.add(offer);
		}
		for (int i = 0; i < 3; i++) {
			VecinoEntity entity = factory.manufacturePojo(VecinoEntity.class);
			entityManager.persist(entity);
			vecinoList.add(entity);
			if (i == 0) {
				publicacionList.get(i).setVecino(entity);
			}
		}
	}

	/**
	 * Prueba para asociar un Publicacion existente a un Vecino.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddVecino() throws EntityNotFoundException {
		VecinoEntity entity = vecinoList.get(0);
		PublicacionEntity PublicacionEntity = publicacionList.get(1);
		VecinoEntity response = publicacionVecinoService.addVecino(entity.getId(), PublicacionEntity.getId());

		assertNotNull(response);
		assertEquals(entity.getId(), response.getId());
	}

	/**
	 * Prueba para asociar un Publicacion existente a un Vecino que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidVecino() {
		assertThrows(EntityNotFoundException.class, () -> {
			PublicacionEntity PublicacionEntity = publicacionList.get(1);
			publicacionVecinoService.addVecino(0L, PublicacionEntity.getId());
		});
	}

	/**
	 * Prueba para asociar un Publicacion que no existe a un Vecino.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddVecinoInvalidPublicacion() {
		assertThrows(EntityNotFoundException.class, () -> {
			VecinoEntity entity = vecinoList.get(0);
			publicacionVecinoService.addVecino(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar un Vecino.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetVecino() throws EntityNotFoundException {
		PublicacionEntity entity = publicacionList.get(0);
		VecinoEntity resultEntity = publicacionVecinoService.getVecino(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getVecino().getId(), resultEntity.getId());
	}

	/**
	 * Prueba para consultar un Vecino de una Publicacion que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetVecinoInvalidPublicacion() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			publicacionVecinoService.getVecino(0L);
		});
	}

	/**
	 * Prueba para consultar un Vecino que no tiene Publicacion.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetVecinoNotPublicacion() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			PublicacionEntity Publicacion = publicacionList.get(1);
			publicacionVecinoService.getVecino(Publicacion.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Publicacions asociadas a una instancia de
	 * Vecino.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceVecino() throws EntityNotFoundException {
		VecinoEntity entity = vecinoList.get(0);
		publicacionVecinoService.replaceVecino(publicacionList.get(1).getId(), entity.getId());

		PublicacionEntity Publicacion = entityManager.find(PublicacionEntity.class, publicacionList.get(1).getId());
		assertTrue(Publicacion.getVecino().equals(entity));
	}

	/**
	 * Prueba para remplazar las instancias de Publicacions asociadas a una instancia de
	 * un Vecino que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidVecino() {
		assertThrows(EntityNotFoundException.class, () -> {
			publicacionVecinoService.replaceVecino(publicacionList.get(1).getId(), 0L);
		});
	}

	/**
	 * Prueba para remplazar las instancias de Publicacions que no existen asociadas a una
	 * instancia de Vecino.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceVecinoInvalidPublicacion() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			VecinoEntity entity = vecinoList.get(0);
			publicacionVecinoService.replaceVecino(0L, entity.getId());

		});
	}

	/**
	 * Prueba para desasociar una Publicacion existente de un Vecino existente.
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemovePublicacion() throws EntityNotFoundException {
		
		publicacionVecinoService.removeVecino(publicacionList.get(0).getId());
		PublicacionEntity publicacion = entityManager.find(PublicacionEntity.class, publicacionList.get(0).getId());
		assertNull(publicacion.getVecino());
	}
	
	/**
	 * Prueba para desasociar un Publicacion que no existe de un Vecino existente.
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemoveInvalidPublicacion(){
		assertThrows(EntityNotFoundException.class, ()->{
			publicacionVecinoService.removeVecino(0L);
		});
	}

	/**
	 * Prueba para desasociar una Publicacion existente de un Vecino existente.
	 */
	@Test
	void testRemoveVecino() {
		assertThrows(EntityNotFoundException.class, () -> {
			publicacionVecinoService.removeVecino(publicacionList.get(1).getId());
		});
	}



    
}
