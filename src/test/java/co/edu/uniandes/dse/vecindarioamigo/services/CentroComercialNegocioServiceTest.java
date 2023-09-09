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

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(CentroComercialNegocioService.class)
public class CentroComercialNegocioServiceTest {
    @Autowired
    private CentroComercialNegocioService centroComercialNegocioService;

    @Autowired
    private NegocioService negocioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CentroComercialEntity centroComercial = new CentroComercialEntity();
    private VecindarioEntity vecindario = new VecindarioEntity();
    private List<NegocioEntity> negocioList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from NegocioEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from CentroComercialEntity").executeUpdate();
    }

    private void insertData() {
        vecindario = factory.manufacturePojo(VecindarioEntity.class);
        entityManager.persist(vecindario);

        centroComercial = factory.manufacturePojo(CentroComercialEntity.class);
        entityManager.persist(centroComercial);

        for (int i = 0; i < 3; i++) {
            NegocioEntity entity = factory.manufacturePojo(NegocioEntity.class);
            entity.setCentroComercial(centroComercial);
            entityManager.persist(entity);
            negocioList.add(entity);
            centroComercial.getLista_negocios().add(entity);
        }
    }

    @Test
    void testAddNegocio() throws EntityNotFoundException, IllegalOperationException {
        NegocioEntity newNegocio = factory.manufacturePojo(NegocioEntity.class);
        newNegocio.setCentroComercial(centroComercial);

        negocioService.createNegocio(newNegocio);

        NegocioEntity negocioEntity = centroComercialNegocioService
                .addNegocio(negocioList.get(0).getId(), centroComercial.getId());
        assertNotNull(negocioEntity);

        assertEquals(negocioEntity.getId(), newNegocio.getId());
        assertEquals(negocioEntity.getNombre(), newNegocio.getNombre());
        assertEquals(negocioEntity.getDescripcion(), newNegocio.getDescripcion());
        assertEquals(negocioEntity.getNumeroDeTelefonico(), newNegocio.getNumeroDeTelefonico());

        NegocioEntity ultimoNegocio = centroComercialNegocioService.getNegocio(centroComercial.getId(),
                newNegocio.getId());

        assertEquals(negocioEntity.getId(), newNegocio.getId());
        assertEquals(negocioEntity.getNombre(), newNegocio.getNombre());
        assertEquals(negocioEntity.getDescripcion(), newNegocio.getDescripcion());
        assertEquals(negocioEntity.getNumeroDeTelefonico(), newNegocio.getNumeroDeTelefonico());
    }

    @Test
    void testAddNegocioInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            NegocioEntity newNegocio = factory.manufacturePojo(NegocioEntity.class);
            newNegocio.setCentroComercial(centroComercial);
            negocioService.createNegocio(newNegocio);
            centroComercialNegocioService.addNegocio(0L, newNegocio.getId());
        });
    }

    @Test
    void testAddInvalidNegocio() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialNegocioService.addNegocio(0L, centroComercial.getId());
        });
    }

    @Test
    void testGetNegocios() throws EntityNotFoundException {
        List<NegocioEntity> negocioEntities = centroComercialNegocioService
                .getNegocios(centroComercial.getId());

        assertEquals(negocioList.size(), negocioEntities.size());

        for (int i = 0; i < negocioList.size(); i++) {
            assertTrue(negocioEntities.contains(negocioList.get(0)));
        }
    }

    @Test
    void testGetNegociosInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialNegocioService.getNegocios(0L);
        });
    }

    @Test
    void testGetNegocio() throws EntityNotFoundException, IllegalOperationException {
        NegocioEntity negocioEntity = negocioList.get(0);
        NegocioEntity negocio = centroComercialNegocioService.getNegocio(centroComercial.getId(),
                negocioEntity.getId());
        assertNotNull(negocio);

        assertEquals(negocioEntity.getId(), negocio.getId());
        assertEquals(negocioEntity.getNombre(), negocio.getNombre());
        assertEquals(negocioEntity.getDescripcion(), negocio.getDescripcion());
        assertEquals(negocioEntity.getNumeroDeTelefonico(), negocio.getNumeroDeTelefonico());

    }

    @Test
    void testGetNegocioInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            NegocioEntity negocioEntity = negocioList.get(0);
            centroComercialNegocioService.getNegocio(0L, negocioEntity.getId());
        });
    }

    @Test
    void testGetInvalidNegocio() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialNegocioService.getNegocio(centroComercial.getId(), 0L);
        });
    }

    @Test
    void testGetNegocioNotAssociatedCentroComercial() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity centroComercialEntity = factory.manufacturePojo(CentroComercialEntity.class);
            entityManager.persist(centroComercialEntity);

            NegocioEntity negocioEntity = factory.manufacturePojo(NegocioEntity.class);
            negocioEntity.setCentroComercial(centroComercialEntity);
            entityManager.persist(negocioEntity);

            centroComercialNegocioService.getNegocio(centroComercialEntity.getId(), negocioEntity.getId());
        });
    }

    @Test
    void testReplaceNegocios() throws EntityNotFoundException, IllegalOperationException {
        List<NegocioEntity> nuevaLista = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            NegocioEntity entity = factory.manufacturePojo(NegocioEntity.class);
            entity.setCentroComercial(centroComercial);
            entityManager.persist(entity);
            nuevaLista.add(entity);
        }

        for (int i = 0; i < nuevaLista.size(); i++) {
            centroComercialNegocioService.addNegocio(nuevaLista.get(i).getId(), centroComercial.getId());
        }

        List<NegocioEntity> negocioEntities = entityManager
                .find(CentroComercialEntity.class, centroComercial.getId()).getLista_negocios();
        for (NegocioEntity item : nuevaLista) {
            assertTrue(negocioEntities.contains(item));
        }
    }

    @Test
    void testReplaceNegociosInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<NegocioEntity> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                NegocioEntity entity = factory.manufacturePojo(NegocioEntity.class);
                entity.setCentroComercial(centroComercial);
                negocioService.createNegocio(entity);
                nuevaLista.add(entity);
            }
            for (int i = 0; i < nuevaLista.size(); i++) {
                centroComercialNegocioService.addNegocio(nuevaLista.get(i).getId(), 0L);
            }
        });
    }

    @Test
    void testReplaceInvalidNegocios() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<NegocioEntity> nuevaLista = new ArrayList<>();
            NegocioEntity entity = factory.manufacturePojo(NegocioEntity.class);
            entity.setCentroComercial(centroComercial);
            entity.setId(0L);
            nuevaLista.add(entity);
            for (int i = 0; i < nuevaLista.size(); i++) {
                centroComercialNegocioService.addNegocio(nuevaLista.get(i).getId(), centroComercial.getId());
            }
        });
    }

    @Test
    void testRemoveNegocio() throws EntityNotFoundException {
        for (NegocioEntity negocio : negocioList) {
            centroComercialNegocioService.removeNegocio(centroComercial.getId(), negocio.getId());
        }
        assertTrue(centroComercialNegocioService.getNegocios(centroComercial.getId()).isEmpty());
    }

    @Test
    void testRemoveNegocioInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            for (NegocioEntity negocio : negocioList) {
                centroComercialNegocioService.removeNegocio(0L, negocio.getId());
            }
        });
    }

    @Test
    void testRemoveInvalidNegocio() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialNegocioService.removeNegocio(centroComercial.getId(), 0L);
        });
    }

}
