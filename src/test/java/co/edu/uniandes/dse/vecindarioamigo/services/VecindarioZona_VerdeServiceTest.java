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

import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ VecindarioService.class, VecindarioZona_VerdeService.class })
public class VecindarioZona_VerdeServiceTest {
    
    
	@Autowired
	private VecindarioZona_VerdeService VecindarioZona_VerdeService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<VecindarioEntity> vecindarioList = new ArrayList<>();
	private List<Zona_VerdeEntity> zonaVerdeList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from Zona_VerdeEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from VecindarioEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			Zona_VerdeEntity greenZone = factory.manufacturePojo(Zona_VerdeEntity.class);
			entityManager.persist(greenZone);
			zonaVerdeList.add(greenZone);
		}

		for (int i = 0; i < 3; i++) {
			VecindarioEntity entity = factory.manufacturePojo(VecindarioEntity.class);
			entityManager.persist(entity);
			vecindarioList.add(entity);
			if (i == 0) {
				zonaVerdeList.get(i).setVecindario(entity);
				entity.getZonasVerdes().add(zonaVerdeList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar un ZonaVerde existente a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testaddZona_Verde() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		Zona_VerdeEntity Zona_VerdeEntity = zonaVerdeList.get(1);
		Zona_VerdeEntity response = VecindarioZona_VerdeService.addZona_Verde(Zona_VerdeEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(Zona_VerdeEntity.getId(), response.getId());
	}
	
	/**
	 * Prueba para asociar un ZonaVerde que no existe a un Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidZonaVerde() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			VecindarioZona_VerdeService.addZona_Verde(0L, entity.getId());
		});
	}
	
	/**
	 * Prueba para asociar un ZonaVerde a un Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testaddZona_VerdeInvalidVecindario() {
		assertThrows(EntityNotFoundException.class, ()->{
			Zona_VerdeEntity Zona_VerdeEntity = zonaVerdeList.get(1);
			VecindarioZona_VerdeService.addZona_Verde(Zona_VerdeEntity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una colección de instancias de ZonaVerde asociadas a una
	 * instancia Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testgetZonas_Verdes() throws EntityNotFoundException {
		List<Zona_VerdeEntity> list = VecindarioZona_VerdeService.getZonas_Verdes(vecindarioList.get(0).getId());
		assertEquals(1, list.size());
	}
	
	/**
	 * Prueba para obtener una colección de instancias de ZonaVerde asociadas a una
	 * instancia Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testgetZonas_VerdesInvalidVecindario() {
		assertThrows(EntityNotFoundException.class,()->{
			VecindarioZona_VerdeService.getZonas_Verdes(0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de ZonaVerde asociada a una instancia Vecindario.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.centroComercialstore.exceptions.BusinessLogicException
	 */
	@Test
	void testgetZona_Verde() throws EntityNotFoundException, IllegalOperationException {
		VecindarioEntity entity = vecindarioList.get(0);
		Zona_VerdeEntity Zona_VerdeEntity = zonaVerdeList.get(0);
		Zona_VerdeEntity response = VecindarioZona_VerdeService.getZona_Verde(entity.getId(), Zona_VerdeEntity.getId());

		assertEquals(Zona_VerdeEntity.getId(), response.getId());
		assertEquals(Zona_VerdeEntity.getNombre(), response.getNombre());
        assertEquals(Zona_VerdeEntity.getDescripcion(), response.getDescripcion());
		assertEquals(Zona_VerdeEntity.getPreferencias(), response.getPreferencias());
		assertEquals(Zona_VerdeEntity.getCalificacion(), response.getCalificacion());
        assertEquals(Zona_VerdeEntity.getUbicacion(), response.getUbicacion());
	}
	
	/**
	 * Prueba para obtener una instancia de ZonaVerde asociada a una instancia Vecindario que no existe.
	 * 
	 * @throws EntityNotFoundException
	 *
	 */
	@Test
	void testgetZona_VerdeInvalidVecindario()  {
		assertThrows(EntityNotFoundException.class, ()->{
			Zona_VerdeEntity Zona_VerdeEntity = zonaVerdeList.get(0);
			VecindarioZona_VerdeService.getZona_Verde(0L, Zona_VerdeEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener una instancia de ZonaVerde que no existe asociada a una instancia Vecindario.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetInvalidZonaVerde()  {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			VecindarioZona_VerdeService.getZona_Verde(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de ZonaVerde asociada a una instancia Vecindario
	 * que no le pertenece.
	 *
	 * @throws co.edu.uniandes.dse.vecindarioamigo.exceptions
	 */
	@Test
	public void getZona_VerdeNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			VecindarioEntity entity = vecindarioList.get(0);
			Zona_VerdeEntity Zona_VerdeEntity = zonaVerdeList.get(1);
			VecindarioZona_VerdeService.getZona_Verde(entity.getId(), Zona_VerdeEntity.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de ZonaVerde asociadas a una instancia de
	 * Vecindario.
	 */
	@Test
	void testreplaceZonas_Verdes() throws EntityNotFoundException {
		VecindarioEntity entity = vecindarioList.get(0);
		List<Zona_VerdeEntity> list = zonaVerdeList.subList(1, 3);
		VecindarioZona_VerdeService.replaceZonas_Verdes(entity.getId(), list);

		for (Zona_VerdeEntity ZonaVerde : list) {
			Zona_VerdeEntity b = entityManager.find(Zona_VerdeEntity.class, ZonaVerde.getId());
			assertTrue(b.getVecindario().equals(entity));
		}
	}
	
	/**
	 * Prueba para remplazar las instancias de ZonaVerde que no existen asociadas a una instancia de
	 * Vecindario.
	 */
	@Test
	void testReplaceInvalidZonas_verdes() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecindarioEntity entity = vecindarioList.get(0);
			
			List<Zona_VerdeEntity> zonas_verdes = new ArrayList<>();
			Zona_VerdeEntity newZonaVerde = factory.manufacturePojo(Zona_VerdeEntity.class);
			newZonaVerde.setId(0L);
			zonas_verdes.add(newZonaVerde);
			
			VecindarioZona_VerdeService.replaceZonas_Verdes(entity.getId(), zonas_verdes);
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de ZonaVerde asociadas a una instancia de
	 * Vecindario que no existe.
	 */
	@Test
	void testreplaceZonas_VerdesInvalidVecindario() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			List<Zona_VerdeEntity> list = zonaVerdeList.subList(1, 3);
			VecindarioZona_VerdeService.replaceZonas_Verdes(0L, list);
		});
	}
    
}
