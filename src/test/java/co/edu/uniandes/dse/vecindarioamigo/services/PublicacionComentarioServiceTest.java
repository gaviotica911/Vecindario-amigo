package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ PublicacionService.class, PublicacionComentarioService.class })

public class PublicacionComentarioServiceTest {
    @Autowired
	private PublicacionComentarioService publicacionComentarioService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<PublicacionEntity> publicacionList = new ArrayList<>();
	private List<ComentarioEntity> comentarioList = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from PublicacionEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			ComentarioEntity neighbor = factory.manufacturePojo(ComentarioEntity.class);
			entityManager.persist(neighbor);
			comentarioList.add(neighbor);
		}

		for (int i = 0; i < 3; i++) {
			PublicacionEntity entity = factory.manufacturePojo(PublicacionEntity.class);
			entityManager.persist(entity);
			publicacionList.add(entity);
			if (i == 0) {
				comentarioList.get(i).setPublicacion(entity);
				entity.getComentarios().add(comentarioList.get(i));
			}
		}
	}

	/**
	 * Prueba para asociar un Comentario existente a un Publicacion.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddComentario() throws EntityNotFoundException {
		PublicacionEntity entity = publicacionList.get(0);
		ComentarioEntity ComentarioEntity = comentarioList.get(1);
		ComentarioEntity response = publicacionComentarioService.addComentario(ComentarioEntity.getId(), entity.getId());

		assertNotNull(response);
		assertEquals(ComentarioEntity.getId(), response.getId());
	}
	
	/**
	 * Prueba para asociar un Comentario que no existe a un Publicacion.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddInvalidComentario() {
		assertThrows(EntityNotFoundException.class, ()->{
			PublicacionEntity entity = publicacionList.get(0);
			publicacionComentarioService.addComentario(0L, entity.getId());
		});
	}
	
	/**
	 * Prueba para asociar un Comentario a un Publicacion que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testAddComentarioInvalidPublicacion() {
		assertThrows(EntityNotFoundException.class, ()->{
			ComentarioEntity comentarioEntity = comentarioList.get(1);
			publicacionComentarioService.addComentario(comentarioEntity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una colección de instancias de Comentario asociadas a una
	 * instancia Publicacion.
	 * 
	 * @throws EntityNotFoundException
	 */

	@Test
	void testGetComentarios() throws EntityNotFoundException {
		List<ComentarioEntity> list = publicacionComentarioService.getComentaros(publicacionList.get(0).getId());
		assertEquals(1, list.size());
	}
	
	/**
	 * Prueba para obtener una colección de instancias de Comentario asociadas a una
	 * instancia Publicacion que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testGetComentariosInvalidPublicacion() {
		assertThrows(EntityNotFoundException.class,()->{
			publicacionComentarioService.getComentaros(0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de Comentario asociada a una instancia Publicacion.
	 * 
	 * @throws IllegalOperationException
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.centroComercialstore.exceptions.BusinessLogicException
	 */
	@Test
	void testgetComentario() throws EntityNotFoundException, IllegalOperationException {
		PublicacionEntity entity = publicacionList.get(0);
		ComentarioEntity comentarioEntity = comentarioList.get(0);
		ComentarioEntity response = publicacionComentarioService.getComentario(entity.getId(), comentarioEntity.getId());

		assertEquals(comentarioEntity.getId(), response.getId());
		assertEquals(comentarioEntity.getNombre(), response.getNombre());
        assertEquals(comentarioEntity.getFecha(), response.getFecha());
		assertEquals(comentarioEntity.getDescripcion(), response.getDescripcion());
		
	}
	
	/**
	 * Prueba para obtener una instancia de Comentario asociada a una instancia Publicacion que no existe.
	 * 
	 * @throws EntityNotFoundException
	 *
	 */
	@Test
	void testgetComentarioInvalidPublicacion()  {
		assertThrows(EntityNotFoundException.class, ()->{
			ComentarioEntity ComentarioEntity = comentarioList.get(0);
			publicacionComentarioService.getComentario(0L, ComentarioEntity.getId());
		});
	}
	
	/**
	 * Prueba para obtener una instancia de Comentario que no existe asociada a una instancia Publicacion.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	void testGetInvalidComentario()  {
		assertThrows(EntityNotFoundException.class, ()->{
			PublicacionEntity entity = publicacionList.get(0);
			publicacionComentarioService.getComentario(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para obtener una instancia de Comentario asociada a una instancia Publicacion
	 * que no le pertenece.
	 *
	 * @throws co.edu.uniandes.dse.Publicacionamigo.exceptions
	 */
	@Test
	public void getComentarioNoAsociadoTest() {
		assertThrows(IllegalOperationException.class, () -> {
			PublicacionEntity entity = publicacionList.get(0);
			ComentarioEntity ComentarioEntity = comentarioList.get(1);
			publicacionComentarioService.getComentario(entity.getId(), ComentarioEntity.getId());
		});
	}

	/**
	 * Prueba para remplazar las instancias de Comentario asociadas a una instancia de
	 * Publicacion.
	 */
	@Test
	void testreplaceComentarios() throws EntityNotFoundException {
		PublicacionEntity entity = publicacionList.get(0);
		List<ComentarioEntity> list = comentarioList.subList(1, 3);
		publicacionComentarioService.replaceComentarios(entity.getId(), list);

		for (ComentarioEntity Comentario : list) {
			ComentarioEntity b = entityManager.find(ComentarioEntity.class, Comentario.getId());
			assertTrue(b.getPublicacion().equals(entity));
		}
	}
	
	/**
	 * Prueba para remplazar las instancias de Comentario que no existen asociadas a una instancia de
	 * Publicacion.
	 */
	@Test
	void testReplaceInvalidCentrosComerciales() {
		assertThrows(EntityNotFoundException.class, ()->{
			PublicacionEntity entity = publicacionList.get(0);
			
			List<ComentarioEntity> Comentarios = new ArrayList<>();
			ComentarioEntity newComentario = factory.manufacturePojo(ComentarioEntity.class);
			newComentario.setId(0L);
			Comentarios.add(newComentario);
			
			publicacionComentarioService.replaceComentarios(entity.getId(), Comentarios);
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de Comentario asociadas a una instancia de
	 * Publicacion que no existe.
	 */
	@Test
	void testreplaceComentariosInvalidPublicacion() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			List<ComentarioEntity> list = comentarioList.subList(1, 3);
			publicacionComentarioService.replaceComentarios(0L, list);
		});
	}
    
}
