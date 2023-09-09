
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.vecindarioamigo.entities.*;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.*;
import co.edu.uniandes.dse.vecindarioamigo.services.*;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de la relacion GrupoDeInteres - Books
 *
 * @grupodeinteres ISIS2603
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ GrupoDeInteresVecinoService.class, GrupoDeInteresService.class, VecinoService.class })
public class GrupoDeInteresVecinoServiceTest {
    @Autowired
	private GrupoDeInteresVecinoService grupoDeInteresVecinoService;
    
    @Autowired
	private GrupoDeInteresService grupoDeInteresService;

	@Autowired
	private VecinoService vecinoService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private GruposDeInteresEntity grupoDeInteres = new GruposDeInteresEntity();/*grupoDeInteres*/
	private VecindarioEntity vecindario = new VecindarioEntity(); /*vecindario*/
	private List<VecinoEntity> vecinoList = new ArrayList<>(); /*vecinoList*/

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
		entityManager.getEntityManager().createQuery("delete from GruposDeInteresEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from VecinoEntity").executeUpdate();
	}
/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		vecindario = factory.manufacturePojo(VecindarioEntity.class);
		entityManager.persist(vecindario);

		grupoDeInteres = factory.manufacturePojo(GruposDeInteresEntity.class);
		entityManager.persist(grupoDeInteres);

		for (int i = 0; i < 3; i++) {
			VecinoEntity entity = factory.manufacturePojo(VecinoEntity.class);
			entity.setVecindario(vecindario);
			entity.getGruposDeInteres().add(grupoDeInteres);
			entityManager.persist(entity);
			vecinoList.add(entity);
			grupoDeInteres.getVecinos().add(entity);
		}
	}
	/**
	 * Prueba para asociar un libro a un grupodeinteres.
	 *
	 */
	@Test
	void testAddVecino() throws EntityNotFoundException, IllegalOperationException {
		VecinoEntity newVecino = factory.manufacturePojo(VecinoEntity.class);
		newVecino.setVecindario(vecindario);
		vecinoService.createVecino(newVecino);

		VecinoEntity vecinoEntity = grupoDeInteresVecinoService.addVecino(grupoDeInteres.getId(), newVecino.getId());
		assertNotNull(vecinoEntity);

		assertEquals(vecinoEntity.getId(), newVecino.getId());
		assertEquals(vecinoEntity.getNombre(), newVecino.getNombre());
		assertEquals(vecinoEntity.getDescripcion(), newVecino.getDescripcion());
		assertEquals(vecinoEntity.getEdad(), newVecino.getEdad());

		VecinoEntity lastVecino = grupoDeInteresVecinoService.getVecino(grupoDeInteres.getId(), newVecino.getId());

		assertEquals(lastVecino.getId(), newVecino.getId());
		assertEquals(lastVecino.getNombre(), newVecino.getNombre());
		assertEquals(lastVecino.getDescripcion(), newVecino.getDescripcion());
	    assertEquals(vecinoEntity.getEdad(), newVecino.getEdad());
	}

/**
	 * Prueba para asociar un libro a un grupoDeInteres que no existe.
	 *
	 */

     @Test
     void testAddBookInvalidAuthor() {
         assertThrows(EntityNotFoundException.class, () -> {
             VecinoEntity newBook = factory.manufacturePojo(VecinoEntity.class);
             newBook.setVecindario(vecindario);
             vecinoService.createVecino(newBook);
             grupoDeInteresVecinoService.addVecino(0L, newBook.getId());
         });
     }
 
     /**
      * Prueba para asociar un libro que no existe a un grupoDeInteres.
      *
      */
     @Test
     void testAddInvalidBook() {
         assertThrows(EntityNotFoundException.class, () -> {
             grupoDeInteresVecinoService.addVecino(grupoDeInteres.getId(), 0L);
         });
     }
 
     /**
      * Prueba para consultar la lista de libros de un autor.
      */
     @Test
     void testGetBooks() throws EntityNotFoundException {
         List<VecinoEntity> vecinoEntities = grupoDeInteresVecinoService.getVecinos(grupoDeInteres.getId());
 
         assertEquals(vecinoList.size(), vecinoEntities.size());
 
         for (int i = 0; i < vecinoList.size(); i++) {
             assertTrue(vecinoEntities.contains(vecinoList.get(0)));
         }
     }
 
     /**
      * Prueba para consultar la lista de libros de un autor que no existe.
      */
     @Test
     void testGetBooksInvalidAuthor() {
         assertThrows(EntityNotFoundException.class, () -> {
             grupoDeInteresVecinoService.getVecinos(0L);
         });
     }
 
     /**
      * Prueba para consultar un libro de un autor.
      *
      * @throws throws EntityNotFoundException, IllegalOperationException
      */
     @Test
     void testGetBook() throws EntityNotFoundException, IllegalOperationException {
         VecinoEntity vecinoEntity = vecinoList.get(0);
         VecinoEntity vecino = grupoDeInteresVecinoService.getVecino(grupoDeInteres.getId(), vecinoEntity.getId());
         assertNotNull(vecino);
 
         assertEquals(vecinoEntity.getId(), vecino.getId());
		assertEquals(vecinoEntity.getNombre(), vecino.getNombre());
		assertEquals(vecinoEntity.getDescripcion(), vecino.getDescripcion());
	    assertEquals(vecinoEntity.getEdad(), vecino.getEdad());
	
     }
 
     /**
      * Prueba para consultar un libro de un autor que no existe.
      *
      * @throws throws EntityNotFoundException, IllegalOperationException
      */
     @Test
     void testGetBookInvalidAuthor() {
         assertThrows(EntityNotFoundException.class, () -> {
             VecinoEntity vecinoEntity = vecinoList.get(0);
             grupoDeInteresVecinoService.getVecino(0L, vecinoEntity.getId());
         });
     }
 
     /**
      * Prueba para consultar un libro que no existe de un autor.
      *
      * @throws throws EntityNotFoundException, IllegalOperationException
      */
     @Test
     void testGetInvalidBook() {
         assertThrows(EntityNotFoundException.class, () -> {
             grupoDeInteresVecinoService.getVecino(grupoDeInteres.getId(), 0L);
         });
     }
 
     /**
      * Prueba para consultar un libro que no está asociado a un autor.
      *
      * @throws throws EntityNotFoundException, IllegalOperationException
      */
     @Test
     void testGetBookNotAssociatedAuthor() {
         assertThrows(IllegalOperationException.class, () -> {
             GruposDeInteresEntity grupoDeInteresEntity = factory.manufacturePojo(GruposDeInteresEntity.class);
             entityManager.persist(grupoDeInteresEntity);
 
             VecinoEntity vecinoEntity = factory.manufacturePojo(VecinoEntity.class);
             vecinoEntity.setVecindario(vecindario);
             entityManager.persist(vecinoEntity);
 
             grupoDeInteresVecinoService.getVecino(grupoDeInteresEntity.getId(), vecinoEntity.getId());
         });
     }
 
     /**
      * Prueba para actualizar los libros de un autor.
      *
      * @throws EntityNotFoundException, IllegalOperationException
      */
     @Test
     void testReplaceBooks() throws EntityNotFoundException, IllegalOperationException {
         List<VecinoEntity> nuevaLista = new ArrayList<>();
         
         for (int i = 0; i < 3; i++) {
             VecinoEntity entity = factory.manufacturePojo(VecinoEntity.class);
             entity.setVecindario(vecindario);
             entityManager.persist(entity);
             nuevaLista.add(entity);
         }
         
         grupoDeInteresVecinoService.addVecinos(grupoDeInteres.getId(), nuevaLista);
         
         List<VecinoEntity> vecinoEntities = entityManager.find(GruposDeInteresEntity.class, grupoDeInteres.getId()).getVecinos();
         for (VecinoEntity item : nuevaLista) {
             assertTrue(vecinoEntities.contains(item));
         }
     }
     
     /**
      * Prueba para actualizar los libros de un autor que no existe.
      *
      * @throws EntityNotFoundException, IllegalOperationException
      */
     @Test
     void testReplaceBooksInvalidAuthor() {
         assertThrows(EntityNotFoundException.class, () -> {
             List<VecinoEntity> nuevaLista = new ArrayList<>();
             for (int i = 0; i < 3; i++) {
                 VecinoEntity entity = factory.manufacturePojo(VecinoEntity.class);
                 entity.setVecindario(vecindario);
                 vecinoService.createVecino(entity);
                 nuevaLista.add(entity);
             }
             grupoDeInteresVecinoService.addVecinos(0L, nuevaLista);
         });
     }
 
     /**
      * Prueba para actualizar los libros que no existen de un autor.
      *
      * @throws EntityNotFoundException, IllegalOperationException
      */
     @Test
     void testReplaceInvalidBooks() {
         assertThrows(EntityNotFoundException.class, () -> {
             List<VecinoEntity> nuevaLista = new ArrayList<>();
             VecinoEntity entity = factory.manufacturePojo(VecinoEntity.class);
             entity.setVecindario(vecindario);
             entity.setId(0L);
             nuevaLista.add(entity);
             grupoDeInteresVecinoService.addVecinos(grupoDeInteres.getId(), nuevaLista);
         });
     }
 
     /**
      * Prueba desasociar un libro con un autor.
      *
      */
     @Test
     void testRemoveBook() throws EntityNotFoundException {
         for (VecinoEntity vecino : vecinoList) {
             grupoDeInteresVecinoService.removeVecino(grupoDeInteres.getId(), vecino.getId());
         }
         assertTrue(grupoDeInteresVecinoService.getVecinos(grupoDeInteres.getId()).isEmpty());
     }
 
     /**
      * Prueba desasociar un libro con un autor que no existe.
      *
      */
     @Test
     void testRemoveBookInvalidAuthor() {
         assertThrows(EntityNotFoundException.class, () -> {
             for (VecinoEntity vecino : vecinoList) {
                 grupoDeInteresVecinoService.removeVecino(0L, vecino.getId());
             }
         });
     }
 
     /**
      * Prueba desasociar un libro que no existe con un autor.
      *
      */
     @Test
     void testRemoveInvalidBook() {
         assertThrows(EntityNotFoundException.class, () -> {
             grupoDeInteresVecinoService.removeVecino(grupoDeInteres.getId(), 0L);
         });
     }







}














