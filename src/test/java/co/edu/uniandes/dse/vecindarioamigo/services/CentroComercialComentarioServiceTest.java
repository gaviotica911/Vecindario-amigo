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
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({ CentroComercialService.class, CentroComercialComentarioService.class })
public class CentroComercialComentarioServiceTest {
    @Autowired
    private CentroComercialComentarioService centroComercialComentarioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CentroComercialEntity> centroComercialList = new ArrayList<>();
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
        for (int i = 0; i < 3; i++) {
            ComentarioEntity comment = factory.manufacturePojo(ComentarioEntity.class);
            entityManager.persist(comment);
            comentarioList.add(comment);
        }

        for (int i = 0; i < 3; i++) {
            CentroComercialEntity entity = factory.manufacturePojo(CentroComercialEntity.class);
            entityManager.persist(entity);
            centroComercialList.add(entity);
            if (i == 0) {
                comentarioList.get(i).setCentroComercial(entity);
                entity.getComentarios().add(comentarioList.get(i));
            }
        }
    }

    @Test
    void testAddComentario() throws EntityNotFoundException, IllegalOperationException {
        CentroComercialEntity entity = centroComercialList.get(0);
        ComentarioEntity comentarioEntity = comentarioList.get(0);
        ComentarioEntity response = centroComercialComentarioService.addComentario(comentarioEntity.getId(),
                entity.getId());

        assertNotNull(response);

        assertEquals(comentarioEntity.getId(), response.getId());
        assertEquals(comentarioEntity.getNombre(), response.getNombre());
        assertEquals(comentarioEntity.getDescripcion(), response.getDescripcion());
    }

    @Test
    void testAddInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            CentroComercialEntity entity = centroComercialList.get(0);
            centroComercialComentarioService.addComentario(0L, entity.getId());
        });
    }

    @Test
    void testGetComentarios() throws EntityNotFoundException {
        List<ComentarioEntity> list = centroComercialComentarioService
                .getComentarios(centroComercialList.get(0).getId());
        assertEquals(1, list.size());
    }

    @Test
    void testGetComentariosInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialComentarioService.getComentarios(0L);
        });
    }

    @Test
    void testgetComentario() throws EntityNotFoundException, IllegalOperationException {
        CentroComercialEntity entity = centroComercialList.get(0);
        ComentarioEntity comentarioEntity = comentarioList.get(0);
        ComentarioEntity response = centroComercialComentarioService.getComentario(entity.getId(),
                comentarioEntity.getId());

        assertEquals(comentarioEntity.getId(), response.getId());
        assertEquals(comentarioEntity.getNombre(), response.getNombre());
        assertEquals(comentarioEntity.getDescripcion(), response.getDescripcion());
    }

    @Test
    void testgetComentarioInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            ComentarioEntity comentarioEntity = comentarioList.get(0);
            centroComercialComentarioService.getComentario(0L, comentarioEntity.getId());
        });
    }

    @Test
    void testGetInvalidComentario() {
        assertThrows(EntityNotFoundException.class, () -> {
            CentroComercialEntity entity = centroComercialList.get(0);
            centroComercialComentarioService.getComentario(entity.getId(), 0L);
        });
    }

    @Test
    public void getComentarioNoAsociadoTest() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity entity = centroComercialList.get(0);
            ComentarioEntity comentarioEntity = comentarioList.get(1);
            centroComercialComentarioService.getComentario(entity.getId(), comentarioEntity.getId());
        });
    }

    @Test
    void testReplaceComentarios() throws EntityNotFoundException {
        CentroComercialEntity entity = centroComercialList.get(0);
        List<ComentarioEntity> list = comentarioList.subList(1, 3);
        centroComercialComentarioService.replaceComentarios(entity.getId(), list);

        for (ComentarioEntity comentario : list) {
            ComentarioEntity c = entityManager.find(ComentarioEntity.class, comentario.getId());
            assertTrue(c.getCentroComercial().equals(entity));
        }
    }

    @Test
    void testReplaceInvalidComentarios() {
        assertThrows(EntityNotFoundException.class, () -> {
            CentroComercialEntity entity = centroComercialList.get(0);

            List<ComentarioEntity> comentarios = new ArrayList<>();
            ComentarioEntity newComentario = factory.manufacturePojo(ComentarioEntity.class);
            newComentario.setId(0L);
            comentarios.add(newComentario);

            centroComercialComentarioService.replaceComentarios(entity.getId(), comentarios);
        });
    }

    @Test
    void testReplaceComentariosInvalidCentroComercial() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            List<ComentarioEntity> list = comentarioList.subList(1, 3);
            centroComercialComentarioService.replaceComentarios(0L, list);
        });
    }

}
