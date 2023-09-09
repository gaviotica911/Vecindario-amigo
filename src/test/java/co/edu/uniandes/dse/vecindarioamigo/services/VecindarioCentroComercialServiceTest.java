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

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ VecindarioService.class, VecindarioCentroComercialService.class })
public class VecindarioCentroComercialServiceTest {
    

	@Autowired
	private VecindarioCentroComercialService vecindarioCentroComercialService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<VecindarioEntity> vecindarioList = new ArrayList<>();
	private List<CentroComercialEntity> centroComercialList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from CentroComercialEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from VecindarioEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			CentroComercialEntity mall = factory.manufacturePojo(CentroComercialEntity.class);
			entityManager.persist(mall);
			centroComercialList.add(mall);
		}

		for (int i = 0; i < 3; i++) {
			VecindarioEntity entity = factory.manufacturePojo(VecindarioEntity.class);
			entityManager.persist(entity);
			vecindarioList.add(entity);
			if (i == 0) {
				centroComercialList.get(i).setVecindario(entity);
				entity.getCentrosComerciales().add(centroComercialList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar un centroComercial existente a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddCentroComercial() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		CentroComercialEntity centroComercialEntity = centroComercialList.get(1);
		CentroComercialEntity response = vecindarioCentroComercialService.addCentroComercial(centroComercialEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(centroComercialEntity.getId(), response.getId());
	}
	
	/**
	 * Prueba para asociar un centroComercial que no existe a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidCentroComercial() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			vecindarioCentroComercialService.addCentroComercial(0L, entity.getId());
		});
	}
	
	/**
	 * Prueba para asociar un centroComercial a un Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddCentroComercialInvalidVecindario() {
		assertThrows(EntityNotFoundException.class, ()->{
			CentroComercialEntity centroComercialEntity = centroComercialList.get(1);
			vecindarioCentroComercialService.addCentroComercial(centroComercialEntity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una colección de instancias de CentrosComerciales asociadas a una
	 * instancia Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testGetCentrosComerciales() throws EntityNotFoundException {
		List<CentroComercialEntity> list = vecindarioCentroComercialService.getCentrosComerciales(vecindarioList.get(0).getId());
		assertEquals(1, list.size());
	}
	
	/**
	 * Prueba para obtener una colección de instancias de CentrosComerciales asociadas a una
	 * instancia Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetCentrosComercialesInvalidVecindario() {
		assertThrows(EntityNotFoundException.class,()->{
			vecindarioCentroComercialService.getCentrosComerciales(0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de CentroComercial asociada a una instancia Vecindario.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.centroComercialstore.exceptions.BusinessLogicException
	 */
	@Test
	void testGetcentroComercial() throws EntityNotFoundException, IllegalOperationException {
		VecindarioEntity entity = vecindarioList.get(0);
		CentroComercialEntity centroComercialEntity = centroComercialList.get(0);
		CentroComercialEntity response = vecindarioCentroComercialService.getCentroComercial(entity.getId(), centroComercialEntity.getId());

		assertEquals(centroComercialEntity.getId(), response.getId());
		assertEquals(centroComercialEntity.getNombre(), response.getNombre());
        assertEquals(centroComercialEntity.getDescripcion(), response.getDescripcion());
		assertEquals(centroComercialEntity.getUbicacion(), response.getUbicacion());
		assertEquals(centroComercialEntity.getCalificacion(), response.getCalificacion());
	}
	
	/**
	 * Prueba para obtener una instancia de CentroComercial asociada a una instancia Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 *
	 */
	@Test
	void testGetCentroComercialInvalidVecindario()  {
		assertThrows(EntityNotFoundException.class, ()->{
			CentroComercialEntity centroComercialEntity = centroComercialList.get(0);
			vecindarioCentroComercialService.getCentroComercial(0L, centroComercialEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener una instancia de CentroComercial que no existe asociada a una instancia Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetInvalidCentroComercial()  {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			vecindarioCentroComercialService.getCentroComercial(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de CentroComercial asociada a una instancia Vecindario
	 * que no le pertenece.
	 *
	 * @throws co.edu.uniandes.dse.vecindarioamigo.exceptions
	 */
	@Test
	public void getCentroComercialNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			VecindarioEntity entity = vecindarioList.get(0);
			CentroComercialEntity centroComercialEntity = centroComercialList.get(1);
			vecindarioCentroComercialService.getCentroComercial(entity.getId(), centroComercialEntity.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de CentroComercial asociadas a una instancia de
	 * Vecindario.
	 */
	@Test
	void testReplaceCentrosComerciales() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		List<CentroComercialEntity> list = centroComercialList.subList(1, 3);
		vecindarioCentroComercialService.replaceCentrosComerciales(entity.getId(), list);

		for (CentroComercialEntity centroComercial : list) {
			CentroComercialEntity b = entityManager.find(CentroComercialEntity.class, centroComercial.getId());
			assertTrue(b.getVecindario().equals(entity));
		}
	}
	
	/**
	 * Prueba para remplazar las instancias de CentroComercial que no existen asociadas a una instancia de
	 * Vecindario.
	 */
	@Test
	void testReplaceInvalidCentrosComerciales() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			
			List<CentroComercialEntity> centroComercials = new ArrayList<>();
			CentroComercialEntity newcentroComercial = factory.manufacturePojo(CentroComercialEntity.class);
			newcentroComercial.setId(0L);
			centroComercials.add(newcentroComercial);
			
			vecindarioCentroComercialService.replaceCentrosComerciales(entity.getId(), centroComercials);
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de CentroComercial asociadas a una instancia de
	 * Vecindario que no existe.
	 */
	@Test
	void testReplaceCentrosComercialesInvalidVecindario() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			List<CentroComercialEntity> list = centroComercialList.subList(1, 3);
			vecindarioCentroComercialService.replaceCentrosComerciales(0L, list);
		});
	}
}
