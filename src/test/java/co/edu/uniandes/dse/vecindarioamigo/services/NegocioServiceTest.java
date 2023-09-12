package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(NegocioService.class)

public class NegocioServiceTest {

    @Autowired
    private NegocioService negocioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<NegocioEntity> listaNegocio = new ArrayList<>();
    private List<VecindarioEntity> listaVecindario = new ArrayList<>();
    private VecindarioEntity vecindarioEntity;

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from NegocioEntity");
        entityManager.getEntityManager().createQuery("delete from VecindarioEntity");

    }

    private void insertData() {

        vecindarioEntity = factory.manufacturePojo(VecindarioEntity.class);
        entityManager.persist(vecindarioEntity);

        for (int i = 0; i < 3; i++) {
            VecindarioEntity vecindarioEntity = factory.manufacturePojo(VecindarioEntity.class);
            entityManager.persist(vecindarioEntity);
            listaVecindario.add(vecindarioEntity);
        }
        for (int i = 0; i < 3; i++) {
            NegocioEntity negocioEntity = factory.manufacturePojo(NegocioEntity.class);
            negocioEntity.setVecindario(listaVecindario.get(0));
            entityManager.persist(negocioEntity);
            listaNegocio.add(negocioEntity);
        }

    }

    /**
     * Prueba para crear un Negocio
     */
    @Test
    void testCreateNegocio() throws EntityNotFoundException, IllegalOperationException {
        NegocioEntity newEntity = factory.manufacturePojo(NegocioEntity.class);
        newEntity.setVecindario(vecindarioEntity);
        newEntity.setNombre("ANDRES CARNE DE RES");

        NegocioEntity result = negocioService.createNegocio(newEntity);

        assertNotNull(result);
        NegocioEntity entity = entityManager.find(NegocioEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getCalificacion(), entity.getCalificacion());
        assertEquals(newEntity.getNumeroDeTelefonico(), entity.getNumeroDeTelefonico());
        assertEquals(newEntity.getDescripcion(), entity.getDescripcion());

    }

    @Test

    void testCreateNegocioConNombreExistente() {
        assertThrows(IllegalOperationException.class, () -> {
            NegocioEntity newEntity = factory.manufacturePojo(NegocioEntity.class);
            newEntity.setVecindario(listaVecindario.get(0));
            newEntity.setNombre(listaNegocio.get(0).getNombre());
            negocioService.createNegocio(newEntity);
        });
    }

    @Test
    void testDaleteNegocio() throws EntityNotFoundException, IllegalOperationException {
        NegocioEntity entity = listaNegocio.get(1);
        negocioService.deleteNegocio(entity.getId());
        NegocioEntity deleted = entityManager.find(NegocioEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteNegocioInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            negocioService.deleteNegocio(0L);
        });
    }

    @Test
    void testGetNegocios() {
        List<NegocioEntity> list = negocioService.getNegocios();
        assertEquals(listaNegocio.size(), list.size());
        for (NegocioEntity entity : list) {
            boolean found = false;
            for (NegocioEntity storedEntity : listaNegocio) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetNegocio() throws EntityNotFoundException {
        NegocioEntity entity = listaNegocio.get(0);
        NegocioEntity resultEntity = negocioService.getNegocio(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
        assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
        assertEquals(entity.getNumeroDeTelefonico(), resultEntity.getNumeroDeTelefonico());
    }

    @Test
    void testUpdateNegocio() throws EntityNotFoundException, IllegalOperationException {
        NegocioEntity entity = listaNegocio.get(0);
        NegocioEntity pojoEntity = factory.manufacturePojo(NegocioEntity.class);
        pojoEntity.setId(entity.getId());
        negocioService.updateNegocio(entity.getId(), pojoEntity);

        NegocioEntity resultEntity = entityManager.find(NegocioEntity.class, entity.getId());
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
        assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
        assertEquals(entity.getNumeroDeTelefonico(), resultEntity.getNumeroDeTelefonico());
    }
}
