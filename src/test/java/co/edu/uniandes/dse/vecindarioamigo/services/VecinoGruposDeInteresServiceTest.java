package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.vecindarioamigo.entities.GruposDeInteresEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VecinoGrupoDeInteresService.class)

public class VecinoGruposDeInteresServiceTest {

    @Autowired
    private VecinoGrupoDeInteresService vecinoGrupoDeInteresService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VecinoEntity> vecinosList = new ArrayList<>();
    private VecindarioEntity vecindario;
    private List<GruposDeInteresEntity> gruposDeInteresList = new ArrayList<>();

    @BeforeEach

    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VecinoEntity");
        entityManager.getEntityManager().createQuery("delete from GruposDeInteresEntity");
    }

    private void insertData() {

        vecindario = factory.manufacturePojo(VecindarioEntity.class);
		entityManager.persist(vecindario);

    for (int i = 0; i < 3; i++) {
            GruposDeInteresEntity grupoEntity = factory.manufacturePojo(GruposDeInteresEntity.class);
            entityManager.persist(grupoEntity);
            gruposDeInteresList.add(grupoEntity);
    }

    for (int i = 0; i < 3; i++) {
            VecinoEntity VecinoEntity = factory.manufacturePojo(VecinoEntity.class);
            VecinoEntity.setVecindario(vecindario);
            VecinoEntity.setGruposDeInteres(gruposDeInteresList);
            entityManager.persist(VecinoEntity);
            vecinosList.add(VecinoEntity);
    }


    GruposDeInteresEntity gruposEntity = factory.manufacturePojo(GruposDeInteresEntity.class);
    entityManager.persist(gruposEntity);
    gruposEntity.getVecinos().add(vecinosList.get(0));
    vecinosList.get(0).getGruposDeInteres().add(gruposEntity);
    }

    @Test
  
    void testAddGrupoDeInteres() throws EntityNotFoundException, IllegalOperationException {

        VecinoEntity newVecino = factory.manufacturePojo(VecinoEntity.class);
        entityManager.persist(newVecino);
        
        GruposDeInteresEntity grupo = factory.manufacturePojo(GruposDeInteresEntity.class);
        entityManager.persist(grupo);
        
        vecinoGrupoDeInteresService.addGrupoDeInteres(  newVecino.getId(),grupo.getId());
        
        GruposDeInteresEntity grupoAct = vecinoGrupoDeInteresService.getGrupoDeInteres(newVecino.getId(), grupo.getId());
        assertEquals(grupo.getId(), grupoAct.getId());
        assertEquals(grupo.getNombre(), grupoAct.getNombre());
        assertEquals(grupo.getDescripcion(), grupoAct.getDescripcion());
        

    }

    @Test
    void testGetGruposDeInteres() throws EntityNotFoundException {

        VecinoEntity vecino = vecinosList.get(0);

        List<GruposDeInteresEntity> entity = vecino.getGruposDeInteres();

        List<GruposDeInteresEntity> resultEntity = vecinoGrupoDeInteresService.getGruposDeInteres(vecino.getId());

        assertNotNull(resultEntity);

        assertEquals(entity.size(), resultEntity.size());

        for (int i = 0; i < resultEntity.size(); i++) {

            GruposDeInteresEntity prueba1 = entity.get(i);
            GruposDeInteresEntity prueba2 = resultEntity.get(i);

            assertEquals(prueba1.getId(), prueba2.getId());
            assertEquals(prueba1.getNombre(), prueba2.getNombre());
            assertEquals(prueba1.getDescripcion(), prueba2.getDescripcion());
        }

    }

    @Test
    void testAddInvalidGrupoDeInteres() {

        assertThrows(EntityNotFoundException.class, () -> {

            VecinoEntity veci = vecinosList.get(0);

            vecinoGrupoDeInteresService.addGrupoDeInteres( 0L, veci.getId());

        });
    }



    @Test
    void testGetGrupoDeInteres() throws IllegalOperationException, EntityNotFoundException {

        VecinoEntity veci = vecinosList.get(0);

        GruposDeInteresEntity grupoDeInteresPrueba = gruposDeInteresList.get(0);

        veci.setGruposDeInteres(gruposDeInteresList);

        GruposDeInteresEntity grupoDeInteresEntity = vecinoGrupoDeInteresService
                .getGrupoDeInteres(veci.getId(), grupoDeInteresPrueba.getId());

        assertNotNull(grupoDeInteresEntity);

        assertEquals(grupoDeInteresPrueba.getId(), grupoDeInteresEntity.getId());
        assertEquals(grupoDeInteresPrueba.getNombre(), grupoDeInteresEntity.getNombre());
        assertEquals(grupoDeInteresPrueba.getDescripcion(), grupoDeInteresEntity.getDescripcion());
       

    }

    @Test
    void testGetInvalidGrupoDeInteres() {

        assertThrows(EntityNotFoundException.class, () -> {

            VecinoEntity veci = vecinosList.get(0);

            veci.setGruposDeInteres(gruposDeInteresList);

            vecinoGrupoDeInteresService.getGrupoDeInteres(veci.getId(), 0L);

        });

    }

    @Test
    void testReplaceNullGrupoDeInteres() {

        assertThrows(IllegalOperationException.class, () -> {

            VecinoEntity veci = vecinosList.get(0);

            veci.setGruposDeInteres(gruposDeInteresList);

            vecinoGrupoDeInteresService.replaceGrupoDeInteres(veci.getId(), null);

        });

    }

    @Test
    void testReplaceInvalidGrupoDeInteres() {

        assertThrows(EntityNotFoundException.class, () -> {

            VecinoEntity veci = vecinosList.get(0);

            List<GruposDeInteresEntity> grupoDeInteressVacias = new ArrayList<>();

            GruposDeInteresEntity grupoDeInteres0L = gruposDeInteresList.get(0);

            grupoDeInteres0L.setId(0L);

            grupoDeInteressVacias.add(grupoDeInteres0L);

            veci.setGruposDeInteres(gruposDeInteresList);

            vecinoGrupoDeInteresService.replaceGrupoDeInteres(veci.getId(), grupoDeInteressVacias);

        });

    }

    @Test
    void testRemoveGrupoDeInteres() throws EntityNotFoundException, IllegalOperationException {

        VecinoEntity veci = vecinosList.get(0);

        veci.setGruposDeInteres(gruposDeInteresList);

        GruposDeInteresEntity grupo = gruposDeInteresList.get(1);

        vecinoGrupoDeInteresService.removeGrupoDeInteres(veci.getId(), grupo.getId());

        for (int i = 0; i < gruposDeInteresList.size(); i++) {

            assertNotEquals(veci.getGruposDeInteres().get(i).getId(), grupo.getId());

        }

    }

    @Test
    void testRemoveInvalidGrupoDeInteres() {

        assertThrows(EntityNotFoundException.class, () -> {

            VecinoEntity veci = vecinosList.get(0);

            veci.setGruposDeInteres(gruposDeInteresList);

            vecinoGrupoDeInteresService.removeGrupoDeInteres(veci.getId(), 0L);

        });

    }

    @Test
    void testRemoveInvalidVecinoGrupoDeInteres() {

        assertThrows(EntityNotFoundException.class, () -> {

            VecinoEntity veci = vecinosList.get(0);

            veci.setGruposDeInteres(gruposDeInteresList);

            GruposDeInteresEntity grupo = gruposDeInteresList.get(1);

            vecinoGrupoDeInteresService.removeGrupoDeInteres(0L, grupo.getId());

        });

    }

    @Test
    void testAddGrupoDeInteresNull() {

        VecinoEntity  veci = vecinosList.get(0);
        Long veciId= veci.getId();

        assertThrows(IllegalOperationException.class,
                () -> vecinoGrupoDeInteresService.addGrupoDeInteres(null,  veciId));

    }

    @Test

    void testAddGrupoDeInteresInvalidVecino() {
        assertThrows(EntityNotFoundException.class, () -> 
        vecinoGrupoDeInteresService.addGrupoDeInteres(0L, gruposDeInteresList.get(0).getId()));
    }

  

    @Test

    void testGetGruposDeInteresInvalidVecino() {

        assertThrows(EntityNotFoundException.class,
                () -> vecinoGrupoDeInteresService.getGruposDeInteres(0L));

    }

    @Test
    void testReplaceGruposDeInteres() throws EntityNotFoundException, IllegalOperationException {
        VecinoEntity veci = vecinosList.get(0);
        veci.setGruposDeInteres(gruposDeInteresList);

        List<GruposDeInteresEntity> groups = new ArrayList<>();
        groups.add(gruposDeInteresList.get(0));
        groups.add(gruposDeInteresList.get(1));
        groups.add(gruposDeInteresList.get(2));
        groups.add(gruposDeInteresList.get(3));

        List<GruposDeInteresEntity>  newGroups = vecinoGrupoDeInteresService.replaceGrupoDeInteres(veci.getId(), groups);

        assertNotNull(newGroups);
        assertEquals(newGroups.size(), groups.size());
        assertTrue(veci.getGruposDeInteres().containsAll(groups));
    }

    @Test
    void testReplaceGruposDeInteresWithNullList()
            throws EntityNotFoundException, IllegalOperationException {
        VecinoEntity veci = vecinosList.get(0);

        assertThrows(IllegalOperationException.class,
                () -> vecinoGrupoDeInteresService.replaceGrupoDeInteres(veci.getId(), null));
    }

    @Test
    void testReplaceGruposDeInteresWithInvalidVecino() {
        
        List<GruposDeInteresEntity> newGropus = new ArrayList<>();
        newGropus.add(gruposDeInteresList.get(0));

        assertThrows(EntityNotFoundException.class,
                () -> vecinoGrupoDeInteresService.replaceGrupoDeInteres(0L, newGropus));
    }

    @Test
    void testReplaceGruposDeInteresWithInvalidGrupoDeInteres() {
        assertThrows(EntityNotFoundException.class, () -> {
            VecinoEntity veci = vecinosList.get(0);
            veci.setId(4L);
            List<GruposDeInteresEntity> newGropus = new ArrayList<>();

            newGropus.add(factory.manufacturePojo(GruposDeInteresEntity.class));
            newGropus.add(factory.manufacturePojo(GruposDeInteresEntity.class));

            vecinoGrupoDeInteresService.replaceGrupoDeInteres(veci.getId(), newGropus);
        });
    }

    @Test
    void testGetGrupoDeInteresWithInvalidVecino() {
    
        Long grupoDeInteresId = gruposDeInteresList.get(0).getId();

        assertThrows(EntityNotFoundException.class,
                () -> vecinoGrupoDeInteresService.getGrupoDeInteres(0L, grupoDeInteresId));
    }

 

    @Test
    void testAddGrupoDeInteresNonExistentGrupoDeInteres() {
        assertThrows(EntityNotFoundException.class, () -> {
            VecinoEntity veci = vecinosList.get(0);
            vecinoGrupoDeInteresService.addGrupoDeInteres(0L,veci.getId());
        });
    }

    @Test
    void testAddGrupoDeInteresNonExistentVecino() {
        assertThrows(EntityNotFoundException.class, () -> {
            GruposDeInteresEntity grupo = gruposDeInteresList.get(0);
            vecinoGrupoDeInteresService.addGrupoDeInteres(grupo.getId(),0L);
        });
    }

    

}
