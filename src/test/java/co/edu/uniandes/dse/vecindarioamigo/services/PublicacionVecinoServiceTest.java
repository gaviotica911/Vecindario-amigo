package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ PublicacionService.class, PublicacionVecinoService.class })

public class PublicacionVecinoServiceTest {
    @Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PublicacionVecinoService publicacionVecinoService;

	@Autowired
	private PublicacionService publicacionService;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<PublicacionEntity> listaPosts= new ArrayList<>();
    private List<VecinoEntity> listaVecinos = new ArrayList<>();

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
		entityManager.getEntityManager().createQuery("delete from PublicacioEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from VecinoEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			PublicacionEntity posts = factory.manufacturePojo(PublicacionEntity.class);
			entityManager.persist(posts);
			listaPosts.add(posts);
		}
		for (int i = 0; i < 3; i++) {
			VecinoEntity entity = factory.manufacturePojo(VecinoEntity.class);
			entityManager.persist(entity);
			listaVecinos.add(entity);
			if (i == 0) {
				listaPosts.get(i).setVecino(entity);
			}
		}
	}

	/**
	 * Prueba para remplazar las instancias de Publicacions asociadas a una instancia de
	 * Vecino.
	 * 
	 * @throws EntityNotFoundException
	 */
    @Test
	void testReplaceVecino() throws EntityNotFoundException {
		PublicacionEntity entity = listaPosts.get(0);
		publicacionVecinoService.replaceVecino(entity.getId(), listaVecinos.get(1).getId());
		entity = publicacionService.getPublicacion(entity.getId());
		assertEquals(entity.getVecino(), listaVecinos.get(1));
	}
	
	/**
	 * Prueba para remplazar las instancias de Publicacions asociadas a una instancia de
	 * Vecino con un libro que no existe
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceVecinoInvalidPublicacion() {
		assertThrows(EntityNotFoundException.class, ()->{
			publicacionVecinoService.replaceVecino(0L, listaVecinos.get(1).getId());
		});
	}
	
	/**
	 * Prueba para remplazar las instancias de Publicacions asociadas a una instancia de
	 * Vecino que no existe.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	void testReplaceInvalidVecino() {
		assertThrows(EntityNotFoundException.class, ()->{
			PublicacionEntity entity = listaPosts.get(0);
			publicacionVecinoService.replaceVecino(entity.getId(), 0L);
		});
	}

	/**
	 * Prueba para desasociar un Publicacion existente de un Vecino existente
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.Publicacionstore.exceptions.BusinessLogicException
	 */
	@Test
	void testRemoveVecino() throws EntityNotFoundException {
		publicacionVecinoService.removeVecino(listaPosts.get(0).getId());
		PublicacionEntity response = publicacionService.getPublicacion(listaPosts.get(0).getId());
		assertNull(response.getVecino());
	}
	
	/**
	 * Prueba para desasociar un Publicacion que no existe de un Vecino
	 * 
	 * @throws EntityNotFoundException
	 *
	 * @throws co.edu.uniandes.csw.Publicacionstore.exceptions.BusinessLogicException
	 */
	@Test
	void testRemoveVecinoInvalidPublicacion() throws EntityNotFoundException {
		assertThrows(EntityNotFoundException.class, ()->{
			publicacionVecinoService.removeVecino(0L);
		});
	}





    
}
