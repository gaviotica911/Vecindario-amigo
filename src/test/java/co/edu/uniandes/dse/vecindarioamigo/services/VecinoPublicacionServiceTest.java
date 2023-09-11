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

import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import({ VecinoService.class, VecinoPublicacionService.class })

public class VecinoPublicacionServiceTest {
    
	@Autowired
	private VecinoPublicacionService vecinoPublicacionService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

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
			PublicacionEntity posts = factory.manufacturePojo(PublicacionEntity.class);
			entityManager.persist(posts);
			publicacionList.add(posts);
		}

		for (int i = 0; i < 3; i++) {
			VecinoEntity entity = factory.manufacturePojo(VecinoEntity.class);
			entityManager.persist(entity);
			vecinoList.add(entity);
			if (i == 0) {
				publicacionList.get(i).setVecino(entity);
				entity.getPublicaciones().add(publicacionList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar un publicacion existente a un vecino.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddPublicacion() throws EntityNotFoundException {
		VecinoEntity entity = vecinoList.get(0);
		PublicacionEntity publicacionEntity = publicacionList.get(1);
		PublicacionEntity response = vecinoPublicacionService.addPublicacion(publicacionEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(publicacionEntity.getId(), response.getId());
	}
	
	/**
	 * Prueba para asociar un publicacion que no existe a un vecino.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidPost() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecinoEntity entity = vecinoList.get(0);
			vecinoPublicacionService.addPublicacion(0L, entity.getId());
		});
	}
	
	/**
	 * Prueba para asociar un publicacion a un vecino que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testaddPublicacionInvalidVecino() {
		assertThrows(EntityNotFoundException.class, ()->{
			PublicacionEntity publicacionEntity = publicacionList.get(1);
			vecinoPublicacionService.addPublicacion(publicacionEntity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una colección de instancias de publicacion asociadas a una
	 * instancia vecino.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testGetPublicacions() throws EntityNotFoundException {
		List<PublicacionEntity> list = vecinoPublicacionService.getPubliaciones(vecinoList.get(0).getId());
		assertEquals(1, list.size());
	}
	
	/**
	 * Prueba para obtener una colección de instancias de publicacion asociadas a una
	 * instancia vecino que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetPublicacionsInvalidVecino() {
		assertThrows(EntityNotFoundException.class,()->{
			vecinoPublicacionService.getPubliaciones(0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de publicacion asociada a una instancia vecino.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.centroComercialstore.exceptions.postsLogicException
	 */
	@Test
	void testGetPublicacion() throws EntityNotFoundException, IllegalOperationException {
		VecinoEntity entity = vecinoList.get(0);
		PublicacionEntity publicacionEntity = publicacionList.get(0);
		PublicacionEntity response = vecinoPublicacionService.getPublicacion(entity.getId(), publicacionEntity.getId());

		assertEquals(publicacionEntity.getId(), response.getId());
		assertEquals(publicacionEntity.getCompartidos(), response.getCompartidos());
        assertEquals(publicacionEntity.getContenido(), response.getContenido());
		assertEquals(publicacionEntity.getFecha(), response.getFecha());
		assertEquals(publicacionEntity.getFoto(), response.getFoto());
        assertEquals(publicacionEntity.getVideo(), response.getVideo());
        assertEquals(publicacionEntity.getLikes(), response.getLikes());
        

	}
	
	/**
	 * Prueba para obtener una instancia de publicacion asociada a una instancia vecino que no existe.
	 * 
	 * @throws EntityNotFoundException
	 *
	 */
	@Test
	void testGetPublicacionInvalidVecino()  {
		assertThrows(EntityNotFoundException.class, ()->{
			PublicacionEntity publicacionEntity = publicacionList.get(0);
			vecinoPublicacionService.getPublicacion(0L, publicacionEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener una instancia de publicacion que no existe asociada a una instancia vecino.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetInvalidpublicacion()  {
		assertThrows(EntityNotFoundException.class, ()->{
			VecinoEntity entity = vecinoList.get(0);
			vecinoPublicacionService.getPublicacion(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de publicacion asociada a una instancia vecino
	 * que no le pertenece.
	 *
	 * @throws co.edu.uniandes.dse.vecinoamigo.exceptions
	 */
	@Test
	public void getPublicacionNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			VecinoEntity entity = vecinoList.get(0);
			PublicacionEntity publicacionEntity = publicacionList.get(1);
			vecinoPublicacionService.getPublicacion(entity.getId(), publicacionEntity.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de publicacion asociadas a una instancia de
	 * vecino.
	 */
	@Test
	void testReplacePublicacions() throws EntityNotFoundException {
		VecinoEntity entity = vecinoList.get(0);
		List<PublicacionEntity> list = publicacionList.subList(1, 3);
		vecinoPublicacionService.replacePublicaciones(entity.getId(), list);

		for (PublicacionEntity publicacion : list) {
			PublicacionEntity b = entityManager.find(PublicacionEntity.class, publicacion.getId());
			assertTrue(b.getVecino().equals(entity));
		}
	}
	
	/**
	 * Prueba para remplazar las instancias de publicacion que no existen asociadas a una instancia de
	 * vecino.
	 */
	@Test
	void testReplaceInvalidCentrosComerciales() {
		assertThrows(EntityNotFoundException.class, ()->{
			VecinoEntity entity = vecinoList.get(0);
			
			List<PublicacionEntity> publicacions = new ArrayList<>();
			PublicacionEntity newpublicacion = factory.manufacturePojo(PublicacionEntity.class);
			newpublicacion.setId(0L);
			publicacions.add(newpublicacion);
			
			vecinoPublicacionService.replacePublicaciones(entity.getId(), publicacions);
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de publicacion asociadas a una instancia de
	 * vecino que no existe.
	 */
	@Test
	void testreplacepublicacionsInvalidvecino() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			List<PublicacionEntity> list = publicacionList.subList(1, 3);
			vecinoPublicacionService.replacePublicaciones(0L, list);
		});
	}

    
}
