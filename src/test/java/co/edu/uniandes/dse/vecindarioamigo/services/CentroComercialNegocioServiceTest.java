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
@Import({ CentroComercialService.class, CentroComercialNegocioService.class })
public class CentroComercialNegocioServiceTest {
    @Autowired
    private CentroComercialNegocioService centroComercialNegocioService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private CentroComercialEntity centroComercial = new CentroComercialEntity();

    private List<CentroComercialEntity> centroComercialList = new ArrayList<>();
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
        for (int i = 0; i < 3; i++) {
            NegocioEntity business = factory.manufacturePojo(NegocioEntity.class);
            entityManager.persist(business);
            negocioList.add(business);
        }

        for (int i = 0; i < 3; i++) {
            CentroComercialEntity entity = factory.manufacturePojo(CentroComercialEntity.class);
            entityManager.persist(entity);
            centroComercialList.add(entity);
            if (i == 0) {
                negocioList.get(i).setCentroComercial(entity);
                entity.getLista_negocios().add(negocioList.get(i));
            }
        }
    }

    @Test
    void testaddNegocio() throws EntityNotFoundException {
        CentroComercialEntity entity = centroComercialList.get(0);
        NegocioEntity negocioEntity = negocioList.get(0);
        NegocioEntity response = centroComercialNegocioService.addNegocio(negocioEntity.getId(), entity.getId());

        assertNotNull(response);
        assertEquals(negocioEntity.getId(), response.getId());
        assertEquals(negocioEntity.getNombre(), response.getNombre());
        assertEquals(negocioEntity.getDescripcion(),
                response.getDescripcion());
        assertEquals(negocioEntity.getNumeroDeTelefonico(),
                response.getNumeroDeTelefonico());
    }

    @Test
    void testAddNegocioInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            NegocioEntity negocioEntity = negocioList.get(0);
            centroComercialNegocioService.addNegocio(negocioEntity.getId(), 0L);
        });
    }

    @Test
    void testAddInvalidNegocio() {
        assertThrows(EntityNotFoundException.class, () -> {
            CentroComercialEntity entity = centroComercialList.get(0);
            centroComercialNegocioService.addNegocio(0L, entity.getId());
        });
    }

    @Test
    void testGetNegocios() throws EntityNotFoundException {
        List<NegocioEntity> list = centroComercialNegocioService.getNegocios(centroComercialList.get(0).getId());
        assertEquals(1, list.size());
    }

    @Test
    void testGetNegociosInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            centroComercialNegocioService.getNegocios(0L);
        });
    }

    @Test
    void testgetNegocio() throws EntityNotFoundException, IllegalOperationException {
        CentroComercialEntity entity = centroComercialList.get(0);
        NegocioEntity negocioEntity = negocioList.get(0);
        NegocioEntity response = centroComercialNegocioService.getNegocio(entity.getId(), negocioEntity.getId());

        assertEquals(negocioEntity.getId(), response.getId());
        assertEquals(negocioEntity.getNombre(), response.getNombre());
        assertEquals(negocioEntity.getDescripcion(), response.getDescripcion());
        assertEquals(negocioEntity.getNumeroDeTelefonico(), response.getNumeroDeTelefonico());
        assertEquals(negocioEntity.getCalificacion(), response.getCalificacion());
    }

    @Test
    void testgetNegocioInvalidCentroComercial() {
        assertThrows(EntityNotFoundException.class, () -> {
            NegocioEntity negocioEntity = negocioList.get(0);
            centroComercialNegocioService.getNegocio(0L, negocioEntity.getId());
        });
    }

    @Test
    void testGetInvalidNegocio() {
        assertThrows(EntityNotFoundException.class, () -> {
            CentroComercialEntity entity = centroComercialList.get(0);
            centroComercialNegocioService.getNegocio(entity.getId(), 0L);
        });
    }

    @Test
    public void getNegocioNoAsociadoTest() {
        assertThrows(IllegalOperationException.class, () -> {
            CentroComercialEntity entity = centroComercialList.get(0);
            NegocioEntity negocioEntity = negocioList.get(1);
            centroComercialNegocioService.getNegocio(entity.getId(), negocioEntity.getId());
        });
    }

    @Test
    void testReplaceNegocios() throws EntityNotFoundException {
        CentroComercialEntity entity = centroComercialList.get(0);
        List<NegocioEntity> list = negocioList.subList(1, 3);
        centroComercialNegocioService.replaceNegocios(entity.getId(), list);

        for (NegocioEntity negocio : list) {
            NegocioEntity b = entityManager.find(NegocioEntity.class, negocio.getId());
            assertTrue(b.getCentroComercial().equals(entity));
        }
    }

    @Test
    void testReplaceInvalidNegocios() {
        assertThrows(EntityNotFoundException.class, () -> {
            CentroComercialEntity entity = centroComercialList.get(0);

            List<NegocioEntity> negocios = new ArrayList<>();
            NegocioEntity newNegocio = factory.manufacturePojo(NegocioEntity.class);
            newNegocio.setId(0L);
            negocios.add(newNegocio);

            centroComercialNegocioService.replaceNegocios(entity.getId(), negocios);
        });
    }

    @Test
    void testReplaceNegociosInvalidCentroComercial() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            List<NegocioEntity> list = negocioList.subList(1, 3);
            centroComercialNegocioService.replaceNegocios(0L, list);
        });
    }

}
