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
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PublicacionService.class)


public class PublicacionServiceTest {
    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<PublicacionEntity> listaPublicaciones= new ArrayList<>();
    private List<VecinoEntity> listaVecinos = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}
    /**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from PublicacionEntity");
		entityManager.getEntityManager().createQuery("delete from PublicacionEntity");
		entityManager.getEntityManager().createQuery("delete from ComentarioEntity");
	}

    /*
     *
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */

	private void insertData() {
        //vecinoEntity = factory.manufacturePojo(vecinoEntity.class);
		//entityManager.persist(vecinoEntity);

		for (int i = 0; i < 3; i++) {
			VecinoEntity vecinoEntity = factory.manufacturePojo(VecinoEntity.class);
			entityManager.persist(vecinoEntity);
			listaVecinos.add(vecinoEntity);
		}
        for (int i = 0; i < 3; i++) {
            PublicacionEntity PublicacionEntity = factory.manufacturePojo(PublicacionEntity.class);
            PublicacionEntity.setVecino(listaVecinos.get(0));
            entityManager.persist(PublicacionEntity);
            listaPublicaciones.add(PublicacionEntity);
    }

        ComentarioEntity commentEntity = factory.manufacturePojo(ComentarioEntity.class);
        entityManager.persist(commentEntity);
        commentEntity.setPublicacion(listaPublicaciones.get(0));
        listaPublicaciones.get(0).getComentarios().add(commentEntity);
	}

    @Test
    void testCreatePost() throws EntityNotFoundException, IllegalOperationException {
        PublicacionEntity newEntity = factory.manufacturePojo(PublicacionEntity.class);
        newEntity.setVecino(listaVecinos.get(0));
        newEntity.setContenido("hola amigos de yutu");
        PublicacionEntity result = publicacionService.createPublicacion(newEntity);
        assertNotNull(result);
        PublicacionEntity entity = entityManager.find(PublicacionEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getComentarios(), entity.getComentarios());
       
    }

    @Test
    void testCreatePostWithNoValidContent() {
        assertThrows(IllegalOperationException.class, () -> {
                PublicacionEntity newEntity = factory.manufacturePojo(PublicacionEntity.class);
                newEntity.setVecino(listaVecinos.get(0));
                newEntity.setContenido("");
                publicacionService.createPublicacion(newEntity);
        });
    }

   

    @Test
    void testCreatePostWithInvalidVecino() {
            assertThrows(IllegalOperationException.class, () -> {
                    PublicacionEntity newEntity = factory.manufacturePojo(PublicacionEntity.class);
                    newEntity.setContenido("solomillos");
                    VecinoEntity vecinoEntity = new VecinoEntity();
                    vecinoEntity.setId(0L);
                    newEntity.setVecino(vecinoEntity);
                    publicacionService.createPublicacion(newEntity);
            });
    }
    @Test
    void testCreatePostWithNullVecino() {
        assertThrows(IllegalOperationException.class, () -> {
                PublicacionEntity newEntity = factory.manufacturePojo(PublicacionEntity.class);
                newEntity.setContenido("holaaaa");
                newEntity.setVecino(null);
                publicacionService.createPublicacion(newEntity);
        });
    }
    @Test
    void testGetPosts() {
        List<PublicacionEntity> list = publicacionService.getPublicaciones();
        assertEquals(listaPublicaciones.size(), list.size());
        for (PublicacionEntity entity : list) {
                boolean found = false;
                for (PublicacionEntity storedEntity : listaPublicaciones) {
                        if (entity.getId().equals(storedEntity.getId())) {
                                found = true;
                        }
                }
                assertTrue(found);
        }
    }
    @Test
    void testGetpost() throws EntityNotFoundException {
        PublicacionEntity entity = listaPublicaciones.get(0);
        PublicacionEntity resultEntity = publicacionService.getPublicacion(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getCompartidos(), resultEntity.getCompartidos());
        assertEquals(entity.getContenido(), resultEntity.getContenido());
        assertEquals(entity.getFecha(), resultEntity.getFecha());
        assertEquals(entity.getFoto(), resultEntity.getFoto());
        assertEquals(entity.getVideo(), resultEntity.getVideo());
        assertEquals(entity.getLikes(), resultEntity.getLikes());
        assertEquals(entity.getCompartidos(), resultEntity.getCompartidos());
    }
    @Test
    void testGetInvalidPost() {
        assertThrows(EntityNotFoundException.class,()->{
                publicacionService.getPublicacion(0L);
        });
    }

    @Test
    void testUpdatePublicacion() throws EntityNotFoundException, IllegalOperationException {
        PublicacionEntity entity = listaPublicaciones.get(0);
        PublicacionEntity pojoEntity = factory.manufacturePojo(PublicacionEntity.class);
        pojoEntity.setId(entity.getId());
        publicacionService.updatePublicacion(entity.getId(), pojoEntity);

        PublicacionEntity resp = entityManager.find(PublicacionEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getCompartidos(), resp.getCompartidos());
        assertEquals(pojoEntity.getContenido(), resp.getContenido());
        assertEquals(pojoEntity.getFecha(), resp.getFecha());
        assertEquals(pojoEntity.getFoto(), resp.getFoto());
        assertEquals(pojoEntity.getVideo(), resp.getVideo());
        assertEquals(pojoEntity.getLikes(), resp.getLikes());
        assertEquals(pojoEntity.getCompartidos(), resp.getCompartidos());
    }   
    
@Test
    void testUpdatePostWithNoValidContenido() {
        assertThrows(IllegalOperationException.class, () -> {
                PublicacionEntity entity = listaPublicaciones.get(0);
                PublicacionEntity pojoEntity = factory.manufacturePojo(PublicacionEntity.class);
                pojoEntity.setContenido("");
                pojoEntity.setId(entity.getId());
                publicacionService.updatePublicacion(entity.getId(), pojoEntity);
        });
    }

@Test
    void testDeletePublicacion() throws EntityNotFoundException, IllegalOperationException {
        PublicacionEntity entity = listaPublicaciones.get(1);
        publicacionService.deletePublicacion(entity.getId());
        PublicacionEntity deleted = entityManager.find(PublicacionEntity.class, entity.getId());
        assertNull(deleted);
    }
@Test
    void testDeleteInvalidPublicacion() {
        assertThrows(EntityNotFoundException.class, ()->{
                publicacionService.deletePublicacion(0L);
        });
    }


    
}
