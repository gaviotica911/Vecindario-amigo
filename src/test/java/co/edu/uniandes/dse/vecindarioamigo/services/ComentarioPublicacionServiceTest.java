package co.edu.uniandes.dse.vecindarioamigo.services;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.entities.*;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.*;
import co.edu.uniandes.dse.vecindarioamigo.services.ComentarioPublicacionService;
import co.edu.uniandes.dse.vecindarioamigo.services.*;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
/**
 * Pruebas de logica de la relacion Book - Editorial
 *
 * @author ISIS2603
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ ComentarioService.class, ComentarioPublicacionService.class })

class ComentarioPublicacionServiceTest {
    @Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ComentarioPublicacionService comentarioPublicacionService;

	@Autowired
	private ComentarioService comentarioService;

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
			ComentarioEntity books = factory.manufacturePojo(ComentarioEntity.class);
			entityManager.persist(books);
			comentarioList.add(books);
		}
		for (int i = 0; i < 3; i++) {
			PublicacionEntity entity = factory.manufacturePojo(PublicacionEntity.class);
			entityManager.persist(entity);
			publicacionList.add(entity);
			if (i == 0) {
				comentarioList.get(i).setPublicacion(entity);
			}
		}
	}
/**
	 * Prueba para remplazar las instancias de Books asociadas a una instancia de
	 * Editorial.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceCentroComercial() throws EntityNotFoundException {
		ComentarioEntity entity = comentarioList.get(0);
		comentarioPublicacionService.replacepublicacion(entity.getId(), publicacionList.get(1).getId());
		entity = comentarioService.getComentario(entity.getId());
		assertEquals(entity.getPublicacion(), publicacionList.get(1));
	}
	
/**
	 * Prueba para remplazar las instancias de Books asociadas a una instancia de
	 * Editorial con un libro que no existe
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceEditorialInvalidBook() {
		assertThrows(EntityNotFoundException.class, ()->{
			comentarioPublicacionService.replacepublicacion(0L, publicacionList.get(1).getId());
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de Books asociadas a una instancia de
	 * Editorial que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidEditorial() {
		assertThrows(EntityNotFoundException.class, ()->{
			ComentarioEntity entity = comentarioList.get(0);
			comentarioPublicacionService.replacepublicacion(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para desasociar un Book existente de un Editorial existente
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	void testRemoveEditorial() throws EntityNotFoundException {
		comentarioPublicacionService.removepublicacion(comentarioList.get(0).getId());
		ComentarioEntity response = comentarioService.getComentario(comentarioList.get(0).getId());
		assertNull(response.getCentroComercial());
	}
	
	/**
	 * Prueba para desasociar un Book que no existe de un Editorial
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.bookstore.exceptions.BusinessLogicException
	 */
	@Test
	void testRemoveEditorialInvalidBook() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			comentarioPublicacionService.removepublicacion(0L);
		});
	}
}
