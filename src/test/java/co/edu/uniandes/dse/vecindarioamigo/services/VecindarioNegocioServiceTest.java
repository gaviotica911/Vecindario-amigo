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

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ VecindarioService.class, VecindarioNegocioService.class })
public class VecindarioNegocioServiceTest {
    
    

	@Autowired
	private VecindarioNegocioService vecindarioNegocioService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<VecindarioEntity> vecindarioList = new ArrayList<>();
	private List<NegocioEntity> negocioList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from NegocioEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from VecindarioEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			NegocioEntity business = factory.manufacturePojo(NegocioEntity.class);
			entityManager.persist(business);
			negocioList.add(business);
		}

		for (int i = 0; i < 3; i++) {
			VecindarioEntity entity = factory.manufacturePojo(VecindarioEntity.class);
			entityManager.persist(entity);
			vecindarioList.add(entity);
			if (i == 0) {
				negocioList.get(i).setVecindario(entity);
				entity.getNegocios().add(negocioList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar un Negocio existente a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testaddNegocio() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		NegocioEntity negocioEntity = negocioList.get(1);
		NegocioEntity response = vecindarioNegocioService.addNegocio(negocioEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(negocioEntity.getId(), response.getId());
	}
	
	/**
	 * Prueba para asociar un Negocio que no existe a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidCentroComercial() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			vecindarioNegocioService.addNegocio(0L, entity.getId());
		});
	}
	
	/**
	 * Prueba para asociar un Negocio a un Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testaddNegocioInvalidVecindario() {
		assertThrows(EntityNotFoundException.class, ()->{
			NegocioEntity NegocioEntity = negocioList.get(1);
			vecindarioNegocioService.addNegocio(NegocioEntity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una colección de instancias de Negocio asociadas a una
	 * instancia Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testgetNegocios() throws EntityNotFoundException {
		List<NegocioEntity> list = vecindarioNegocioService.getNegocios(vecindarioList.get(0).getId());
		assertEquals(1, list.size());
	}
	
	/**
	 * Prueba para obtener una colección de instancias de Negocio asociadas a una
	 * instancia Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testgetNegociosInvalidVecindario() {
		assertThrows(EntityNotFoundException.class,()->{
			vecindarioNegocioService.getNegocios(0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de Negocio asociada a una instancia Vecindario.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.centroComercialstore.exceptions.BusinessLogicException
	 */
	@Test
	void testgetNegocio() throws EntityNotFoundException, IllegalOperationException {
		VecindarioEntity entity = vecindarioList.get(0);
		NegocioEntity NegocioEntity = negocioList.get(0);
		NegocioEntity response = vecindarioNegocioService.getNegocio(entity.getId(), NegocioEntity.getId());

		assertEquals(NegocioEntity.getId(), response.getId());
		assertEquals(NegocioEntity.getNombre(), response.getNombre());
        assertEquals(NegocioEntity.getDescripcion(), response.getDescripcion());
		assertEquals(NegocioEntity.getNumeroDeTelefonico(), response.getNumeroDeTelefonico());
		assertEquals(NegocioEntity.getCalificacion(), response.getCalificacion());
	}
	
	/**
	 * Prueba para obtener una instancia de Negocio asociada a una instancia Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 *
	 */
	@Test
	void testgetNegocioInvalidVecindario()  {
		assertThrows(EntityNotFoundException.class, ()->{
			NegocioEntity NegocioEntity = negocioList.get(0);
			vecindarioNegocioService.getNegocio(0L, NegocioEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener una instancia de Negocio que no existe asociada a una instancia Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetInvalidNegocio()  {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			vecindarioNegocioService.getNegocio(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de Negocio asociada a una instancia Vecindario
	 * que no le pertenece.
	 *
	 * @throws co.edu.uniandes.dse.vecindarioamigo.exceptions
	 */
	@Test
	public void getNegocioNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			VecindarioEntity entity = vecindarioList.get(0);
			NegocioEntity NegocioEntity = negocioList.get(1);
			vecindarioNegocioService.getNegocio(entity.getId(), NegocioEntity.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Negocio asociadas a una instancia de
	 * Vecindario.
	 */
	@Test
	void testreplaceNegocios() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		List<NegocioEntity> list = negocioList.subList(1, 3);
		vecindarioNegocioService.replaceNegocios(entity.getId(), list);

		for (NegocioEntity negocio : list) {
			NegocioEntity b = entityManager.find(NegocioEntity.class, negocio.getId());
			assertTrue(b.getVecindario().equals(entity));
		}
	}
	
	/**
	 * Prueba para remplazar las instancias de Negocio que no existen asociadas a una instancia de
	 * Vecindario.
	 */
	@Test
	void testReplaceInvalidCentrosComerciales() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			
			List<NegocioEntity> negocios = new ArrayList<>();
			NegocioEntity newNegocio = factory.manufacturePojo(NegocioEntity.class);
			newNegocio.setId(0L);
			negocios.add(newNegocio);
			
			vecindarioNegocioService.replaceNegocios(entity.getId(), negocios);
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de Negocio asociadas a una instancia de
	 * Vecindario que no existe.
	 */
	@Test
	void testreplaceNegociosInvalidVecindario() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			List<NegocioEntity> list = negocioList.subList(1, 3);
			vecindarioNegocioService.replaceNegocios(0L, list);
		});
	}
}
