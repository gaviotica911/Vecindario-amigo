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

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.OfertaEntity;

/**
 * Pruebas de logica de OfertaNegocio
 *
 * @author ISIS2603
 */
@DataJpaTest
@Transactional
@Import(OfertaNegocioService.class)
public class OfertaNegocioServiceTest {
    

	private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private OfertaNegocioService OfertaNegocioService;

	@Autowired
	private TestEntityManager entityManager;

	private List<NegocioEntity> negocioList = new ArrayList<>();
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
		entityManager.getEntityManager().createQuery("delete from OfertaEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from NegocioEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			OfertaEntity offer = factory.manufacturePojo(OfertaEntity.class);
			entityManager.persist(offer);
			ofertaList.add(offer);
		}
		for (int i = 0; i < 3; i++) {
			NegocioEntity entity = factory.manufacturePojo(NegocioEntity.class);
			entityManager.persist(entity);
			negocioList.add(entity);
			if (i == 0) {
				ofertaList.get(i).setNegocio(entity);
			}
		}
	}

	/**
	 * Prueba para asociar un Oferta existente a un Negocio.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddNegocio() throws EntityNotFoundException {
		NegocioEntity entity = negocioList.get(0);
		OfertaEntity OfertaEntity = ofertaList.get(1);
		NegocioEntity response = OfertaNegocioService.addNegocio(entity.getId(), OfertaEntity.getId());

		assertNotNull(response);
		assertEquals(entity.getId(), response.getId());
	}

	/**
	 * Prueba para asociar un Oferta existente a un Negocio que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidNegocio() {
		assertThrows(EntityNotFoundException.class, () -> {
			OfertaEntity OfertaEntity = ofertaList.get(1);
			OfertaNegocioService.addNegocio(0L, OfertaEntity.getId());
		});
	}

	/**
	 * Prueba para asociar un Oferta que no existe a un Negocio.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddNegocioInvalidOferta() {
		assertThrows(EntityNotFoundException.class, () -> {
			NegocioEntity entity = negocioList.get(0);
			OfertaNegocioService.addNegocio(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para consultar un Negocio.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetNegocio() throws EntityNotFoundException {
		OfertaEntity entity = ofertaList.get(0);
		NegocioEntity resultEntity = OfertaNegocioService.getNegocio(entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getNegocio().getId(), resultEntity.getId());
	}

	/**
	 * Prueba para consultar un Negocio de una oferta que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetNegocioInvalidOferta() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			OfertaNegocioService.getNegocio(0L);
		});
	}

	/**
	 * Prueba para consultar un Negocio que no tiene oferta.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetNegocioNotOferta() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			OfertaEntity Oferta = ofertaList.get(1);
			OfertaNegocioService.getNegocio(Oferta.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Ofertas asociadas a una instancia de
	 * Negocio.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceNegocio() throws EntityNotFoundException {
		NegocioEntity entity = negocioList.get(0);
		OfertaNegocioService.replaceNegocio(ofertaList.get(1).getId(), entity.getId());

		OfertaEntity Oferta = entityManager.find(OfertaEntity.class, ofertaList.get(1).getId());
		assertTrue(Oferta.getNegocio().equals(entity));
	}

	/**
	 * Prueba para remplazar las instancias de Ofertas asociadas a una instancia de
	 * un Negocio que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidNegocio() {
		assertThrows(EntityNotFoundException.class, () -> {
			OfertaNegocioService.replaceNegocio(ofertaList.get(1).getId(), 0L);
		});
	}

	/**
	 * Prueba para remplazar las instancias de Ofertas que no existen asociadas a una
	 * instancia de Negocio.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceNegocioInvalidOferta() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, () -> {
			NegocioEntity entity = negocioList.get(0);
			OfertaNegocioService.replaceNegocio(0L, entity.getId());

		});
	}

	/**
	 * Prueba para desasociar un Oferta existente de un Negocio existente.
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemoveOferta() throws EntityNotFoundException {
		OfertaNegocioService.removeNegocio(ofertaList.get(0).getId());
		OfertaEntity Oferta = entityManager.find(OfertaEntity.class, ofertaList.get(0).getId());
		assertNull(Oferta.getNegocio());
	}
	
	/**
	 * Prueba para desasociar un Oferta que no existe de un Negocio existente.
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	public void testRemoveInvalidOferta(){
		assertThrows(EntityNotFoundException.class, ()->{
			OfertaNegocioService.removeNegocio(0L);
		});
	}

	/**
	 * Prueba para desasociar un Oferta existente de un Negocio existente.
	 */
	@Test
	void testRemoveNegocio() {
		assertThrows(EntityNotFoundException.class, () -> {
			OfertaNegocioService.removeNegocio(ofertaList.get(1).getId());
		});
	}
}
