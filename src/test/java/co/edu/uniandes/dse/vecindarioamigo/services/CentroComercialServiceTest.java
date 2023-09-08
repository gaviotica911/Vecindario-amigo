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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.GruposDeInteresEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(CentroComercialService.class)

public class CentroComercialServiceTest {
    @Autowired
    private CentroComercialService centroComercialService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<CentroComercialEntity> listaCentrosComerciales = new ArrayList<>();
    private List<VecindarioEntity> listaVecindario = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from CentroComercialEntity");
        entityManager.getEntityManager().createQuery("delete from VecindarioEntity");

    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VecindarioEntity vecindarioEntity = factory.manufacturePojo(VecindarioEntity.class);
            entityManager.persist(vecindarioEntity);
            listaVecindario.add(vecindarioEntity);
        }

        for (int i = 0; i < 3; i++) {
            CentroComercialEntity centroComercialEntity = factory.manufacturePojo(CentroComercialEntity.class);
            centroComercialEntity.setVecindario(listaVecindario.get(0));
            entityManager.persist(centroComercialEntity);
            listaCentrosComerciales.add(centroComercialEntity);
        }

    }

    @Test
    void testCreateCentroComercial() throws EntityNotFoundException, IllegalOperationException {
        CentroComercialEntity newEntity = factory.manufacturePojo(CentroComercialEntity.class);
        newEntity.setVecindario(listaVecindario.get(0));
        newEntity.setNombre("Unicentro");
        newEntity.setCalificacion(5);
        CentroComercialEntity result = centroComercialService.createCentroComercial(newEntity);
        assertNotNull(result);
        CentroComercialEntity entity = entityManager.find(CentroComercialEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
        assertEquals(newEntity.getUbicacion(), entity.getUbicacion());
        assertEquals(newEntity.getCalificacion(), entity.getCalificacion());
    }

    @Test
    void testCreateCentroComercialWithNoValidNombre() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity newEntity = factory.manufacturePojo(CentroComercialEntity.class);
            newEntity.setVecindario(listaVecindario.get(0));
            newEntity.setNombre("");
            centroComercialService.createCentroComercial(newEntity);
        });
    }

    @Test
    void testCreateCentroComercialWithNoValidNombre2() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity newEntity = factory.manufacturePojo(CentroComercialEntity.class);
            newEntity.setVecindario(listaVecindario.get(0));
            newEntity.setNombre(null);
            centroComercialService.createCentroComercial(newEntity);
        });
    }

    @Test

    void testCreateCentroComercialConNombreInexistente() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity newEntity = factory.manufacturePojo(CentroComercialEntity.class);
            newEntity.setVecindario(listaVecindario.get(0));
            newEntity.setNombre(listaCentrosComerciales.get(0).getNombre());
            centroComercialService.createCentroComercial(newEntity);
        });
    }

    @Test
    void testCreateCentroComercialConVecindarioInvalido() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity newEntity = factory.manufacturePojo(CentroComercialEntity.class);
            newEntity.setNombre("Unicentro");
            VecindarioEntity vecindarioEntity = new VecindarioEntity();
            vecindarioEntity.setId(0L);
            newEntity.setVecindario(vecindarioEntity);
            centroComercialService.createCentroComercial(newEntity);
        });
    }

    @Test
    void testCreateCentroComercialConVecindarioNulo() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity newEntity = factory.manufacturePojo(CentroComercialEntity.class);
            newEntity.setNombre("Bellavista");
            newEntity.setVecindario(null);
            centroComercialService.createCentroComercial(newEntity);
        });
    }

    @Test
    void testGetCentrosComerciales() {
        List<CentroComercialEntity> list = centroComercialService.getCentrosComerciales();
        assertEquals(listaCentrosComerciales.size(), list.size());
        for (CentroComercialEntity entity : list) {
            boolean found = false;
            for (CentroComercialEntity storedEntity : listaCentrosComerciales) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetCentroComercial() throws EntityNotFoundException {
        CentroComercialEntity entity = listaCentrosComerciales.get(0);
        CentroComercialEntity resultEntity = centroComercialService.getCentroComercial(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getUbicacion(), resultEntity.getUbicacion());
        assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
        assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
    }

    @Test
    void testGetInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialService.getCentroComercial(0L);
        });
    }

    @Test
    void testUpdateCentroComercial() throws EntityNotFoundException, IllegalOperationException {
        CentroComercialEntity entity = listaCentrosComerciales.get(0);
        CentroComercialEntity pojoEntity = factory.manufacturePojo(CentroComercialEntity.class);
        pojoEntity.setId(entity.getId());
        centroComercialService.updateCentroComercial(entity.getId(), pojoEntity);

        CentroComercialEntity resultEntity = entityManager.find(CentroComercialEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resultEntity.getId());
        assertEquals(pojoEntity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getUbicacion(), resultEntity.getUbicacion());
        assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
        assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
    }

    @Test
    void testUpdateCentroComercialInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            CentroComercialEntity pojoEntity = factory.manufacturePojo(CentroComercialEntity.class);
            pojoEntity.setId(0L);
            centroComercialService.updateCentroComercial(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateCentroComercialConNombreInvalido() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity entity = listaCentrosComerciales.get(0);
            CentroComercialEntity pojoEntity = factory.manufacturePojo(CentroComercialEntity.class);
            pojoEntity.setNombre("");
            pojoEntity.setId(entity.getId());
            centroComercialService.updateCentroComercial(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateCentroComercialConNombreInvalido2() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity entity = listaCentrosComerciales.get(0);
            CentroComercialEntity pojoEntity = factory.manufacturePojo(CentroComercialEntity.class);
            pojoEntity.setNombre(null);
            pojoEntity.setId(entity.getId());
            centroComercialService.updateCentroComercial(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteCentroComercial() throws EntityNotFoundException, IllegalOperationException {
        CentroComercialEntity entity = listaCentrosComerciales.get(1);
        centroComercialService.deleteCentroComercial(entity.getId());
        CentroComercialEntity deleted = entityManager.find(CentroComercialEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteCentroComercialInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialService.deleteCentroComercial(0L);
        });
    }

}
