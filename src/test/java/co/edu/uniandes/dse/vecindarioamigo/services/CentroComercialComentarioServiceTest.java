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

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(CentroComercialComentarioService.class)
public class CentroComercialComentarioServiceTest {
    @Autowired
    private CentroComercialComentarioService centroComercialComentarioService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CentroComercialEntity centroComercial = new CentroComercialEntity();
    private VecindarioEntity vecindario = new VecindarioEntity();
    private List<ComentarioEntity> comentarioList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ComentarioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CentroComercialEntity").executeUpdate();
    }

    private void insertData() {
        vecindario = factory.manufacturePojo(VecindarioEntity.class);
        entityManager.persist(vecindario);

        centroComercial = factory.manufacturePojo(CentroComercialEntity.class);
        entityManager.persist(centroComercial);

        for (int i = 0; i < 3; i++) {
            ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
            entity.setCentroComercial(centroComercial);
            entityManager.persist(entity);
            comentarioList.add(entity);
            centroComercial.getComentarios().add(entity);
        }
    }

    @Test
    void testAddComentario() throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity newComentario = factory.manufacturePojo(ComentarioEntity.class);
        newComentario.setCentroComercial(centroComercial);

        comentarioService.createComentarios(newComentario);

        ComentarioEntity comentarioEntity = centroComercialComentarioService
                .addComentario(comentarioList.get(0).getId(), centroComercial.getId());
        assertNotNull(comentarioEntity);

        assertEquals(comentarioEntity.getId(), newComentario.getId());
        assertEquals(comentarioEntity.getNombre(), newComentario.getNombre());
        assertEquals(comentarioEntity.getDescripcion(), newComentario.getDescripcion());

        ComentarioEntity ultimoComentario = centroComercialComentarioService.getComentario(centroComercial.getId(),
                newComentario.getId());

        assertEquals(ultimoComentario.getId(), newComentario.getId());
        assertEquals(ultimoComentario.getNombre(), newComentario.getNombre());
        assertEquals(ultimoComentario.getDescripcion(), newComentario.getDescripcion());
    }

    @Test
    void testAddComentarioInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity newComentario = factory.manufacturePojo(ComentarioEntity.class);
            newComentario.setCentroComercial(centroComercial);
            comentarioService.createComentarios(newComentario);
            centroComercialComentarioService.addComentario(0L, newComentario.getId());
        });
    }

    @Test
    void testAddInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialComentarioService.addComentario(0L, centroComercial.getId());
        });
    }

    @Test
    void testGetComentarios() throws EntityNotFoundException {
        List<ComentarioEntity> comentaroiEntities = centroComercialComentarioService
                .getComentarios(centroComercial.getId());

        assertEquals(comentarioList.size(), comentaroiEntities.size());

        for (int i = 0; i < comentarioList.size(); i++) {
            assertTrue(comentaroiEntities.contains(comentarioList.get(0)));
        }
    }

    @Test
    void testGetComentariosInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialComentarioService.getComentarios(0L);
        });
    }

    @Test
    void testGetComentario() throws EntityNotFoundException, IllegalOperationException {
        ComentarioEntity comentarioEntity = comentarioList.get(0);
        ComentarioEntity comentario = centroComercialComentarioService.getComentario(centroComercial.getId(),
                comentarioEntity.getId());
        assertNotNull(comentario);

        assertEquals(comentarioEntity.getId(), comentario.getId());
        assertEquals(comentarioEntity.getNombre(), comentario.getNombre());
        assertEquals(comentarioEntity.getDescripcion(), comentario.getDescripcion());

    }

    @Test
    void testGetComentarioInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity comentarioEntity = comentarioList.get(0);
            centroComercialComentarioService.getComentario(0L, comentarioEntity.getId());
        });
    }

    @Test
    void testGetInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialComentarioService.getComentario(centroComercial.getId(), 0L);
        });
    }

    @Test
    void testGetComentarioNotAssociatedCentroComercial() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity centroComercialEntity = factory.manufacturePojo(CentroComercialEntity.class);
            entityManager.persist(centroComercialEntity);

            ComentarioEntity comentarioEntity = factory.manufacturePojo(ComentarioEntity.class);
            comentarioEntity.setCentroComercial(centroComercialEntity);
            entityManager.persist(comentarioEntity);

            centroComercialComentarioService.getComentario(centroComercialEntity.getId(), comentarioEntity.getId());
        });
    }

    @Test
    void testReplaceComentarios() throws EntityNotFoundException, IllegalOperationException {
        List<ComentarioEntity> nuevaLista = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
            entity.setCentroComercial(centroComercial);
            entityManager.persist(entity);
            nuevaLista.add(entity);
        }

        for (int i = 0; i < nuevaLista.size(); i++) {
            centroComercialComentarioService.addComentario(nuevaLista.get(i).getId(), centroComercial.getId());
        }

        List<ComentarioEntity> comentarioEntities = entityManager
                .find(CentroComercialEntity.class, centroComercial.getId()).getComentarios();
        for (ComentarioEntity item : nuevaLista) {
            assertTrue(comentarioEntities.contains(item));
        }
    }

    @Test
    void testReplaceComentariosInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<ComentarioEntity> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
                entity.setCentroComercial(centroComercial);
                comentarioService.createComentarios(entity);
                nuevaLista.add(entity);
            }
            for (int i = 0; i < nuevaLista.size(); i++) {
                centroComercialComentarioService.addComentario(nuevaLista.get(i).getId(), 0L);
            }
        });
    }

    @Test
    void testReplaceInvalidComentarios() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<ComentarioEntity> nuevaLista = new ArrayList<>();
            ComentarioEntity entity = factory.manufacturePojo(ComentarioEntity.class);
            entity.setCentroComercial(centroComercial);
            entity.setId(0L);
            nuevaLista.add(entity);
            for (int i = 0; i < nuevaLista.size(); i++) {
                centroComercialComentarioService.addComentario(nuevaLista.get(i).getId(), centroComercial.getId());
            }
        });
    }

    @Test
    void testRemoveComentario() throws EntityNotFoundException {
        for (ComentarioEntity comentario : comentarioList) {
            centroComercialComentarioService.removeComentario(centroComercial.getId(), comentario.getId());
        }
        assertTrue(centroComercialComentarioService.getComentarios(centroComercial.getId()).isEmpty());
    }

    @Test
    void testRemoveComentarioInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            for (ComentarioEntity comentario : comentarioList) {
                centroComercialComentarioService.removeComentario(0L, comentario.getId());
            }
        });
    }

    @Test
    void testRemoveInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialComentarioService.removeComentario(centroComercial.getId(), 0L);
        });
    }

}
