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

import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(Zona_VerdeService.class)

public class Zona_verdeServiceTest {

    @Autowired
    private Zona_VerdeService zona_VerdeService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<Zona_VerdeEntity> listaZona_Verde = new ArrayList<>();
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
        entityManager.getEntityManager().createQuery("delete from Zona_VerdeEntity");
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
            Zona_VerdeEntity zona_VerdeEntity = factory.manufacturePojo(Zona_VerdeEntity.class);
            zona_VerdeEntity.setVecindario(listaVecindario.get(0));
            entityManager.persist(zona_VerdeEntity);
            listaZona_Verde.add(zona_VerdeEntity);
        }

    }

    /**
     * Prueba para crear un Zona_Verde
     */
    @Test
    void testCreateZona_Verde() throws EntityNotFoundException, IllegalOperationException {
        Zona_VerdeEntity newEntity = factory.manufacturePojo(Zona_VerdeEntity.class);
        newEntity.setVecindario(vecindarioEntity);
        newEntity.setNombre("Parque el Contry");

        Zona_VerdeEntity result = zona_VerdeService.createZona_Verde(newEntity);

        assertNotNull(result);
        Zona_VerdeEntity entity = entityManager.find(Zona_VerdeEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getCalificacion(), entity.getCalificacion());
        assertEquals(newEntity.getUbicacion(), entity.getUbicacion());
        assertEquals(newEntity.getDescripcion(), entity.getDescripcion());

    }

    @Test

    void testCreateZona_VerdeConNombreExistente() {
        assertThrows(IllegalOperationException.class, () -> {
            Zona_VerdeEntity newEntity = factory.manufacturePojo(Zona_VerdeEntity.class);
            newEntity.setVecindario(listaVecindario.get(0));
            newEntity.setNombre(listaZona_Verde.get(0).getNombre());
            zona_VerdeService.createZona_Verde(newEntity);
        });
    }

    @Test
    void testCreateZona_VerdeConVecindarioNulo() {
        assertThrows(IllegalOperationException.class, () -> {
            Zona_VerdeEntity newEntity = factory.manufacturePojo(Zona_VerdeEntity.class);
            newEntity.setNombre("XCOLI");
            newEntity.setVecindario(null);
            zona_VerdeService.createZona_Verde(newEntity);
        });
    }

    @Test
    void testDaleteZona_Verde() throws EntityNotFoundException, IllegalOperationException {
        Zona_VerdeEntity entity = listaZona_Verde.get(1);
        zona_VerdeService.deleteZona_Verde(entity.getId());
        Zona_VerdeEntity deleted = entityManager.find(Zona_VerdeEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteZona_VerdeInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            zona_VerdeService.deleteZona_Verde(0L);
        });
    }

    @Test
    void testGetZona_Verdes() {
        List<Zona_VerdeEntity> list = zona_VerdeService.getZona_Verdes();
        assertEquals(listaZona_Verde.size(), list.size());
        for (Zona_VerdeEntity entity : list) {
            boolean found = false;
            for (Zona_VerdeEntity storedEntity : listaZona_Verde) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetZona_Verde() throws EntityNotFoundException {
        Zona_VerdeEntity entity = listaZona_Verde.get(0);
        Zona_VerdeEntity resultEntity = zona_VerdeService.getZona_Verde(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
        assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
        assertEquals(entity.getUbicacion(), resultEntity.getUbicacion());
    }

    @Test
    void testUpdateZona_Verde() throws EntityNotFoundException, IllegalOperationException {
        Zona_VerdeEntity entity = listaZona_Verde.get(0);
        Zona_VerdeEntity pojoEntity = factory.manufacturePojo(Zona_VerdeEntity.class);
        pojoEntity.setId(entity.getId());
        zona_VerdeService.updateZona_Verde(entity.getId(), pojoEntity);

        Zona_VerdeEntity resultEntity = entityManager.find(Zona_VerdeEntity.class, entity.getId());
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getCalificacion(), resultEntity.getCalificacion());
        assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
        assertEquals(entity.getUbicacion(), resultEntity.getUbicacion());
    }
}
