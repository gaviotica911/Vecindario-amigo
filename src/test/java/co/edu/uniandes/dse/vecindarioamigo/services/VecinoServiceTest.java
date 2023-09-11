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

import co.edu.uniandes.dse.vecindarioamigo.entities.GruposDeInteresEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecinoEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(VecinoService.class)


public class VecinoServiceTest {
    @Autowired
    private VecinoService vecinoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VecinoEntity> listaVecinos = new ArrayList<>();
    private List<VecindarioEntity> listaVecindario = new ArrayList<>();

    @BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from VecinoEntity");
		entityManager.getEntityManager().createQuery("delete from VecindarioEntity");
		entityManager.getEntityManager().createQuery("delete from GruposDeInteresEntity");
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
        //vecindarioEntity = factory.manufacturePojo(VecindarioEntity.class);
		//entityManager.persist(vecindarioEntity);

		for (int i = 0; i < 3; i++) {
			VecindarioEntity vecindarioEntity = factory.manufacturePojo(VecindarioEntity.class);
			entityManager.persist(vecindarioEntity);
			listaVecindario.add(vecindarioEntity);
		}
        for (int i = 0; i < 3; i++) {
            VecinoEntity vecinoEntity = factory.manufacturePojo(VecinoEntity.class);
            vecinoEntity.setVecindario(listaVecindario.get(0));
            entityManager.persist(vecinoEntity);
            listaVecinos.add(vecinoEntity);
    }

        GruposDeInteresEntity grupoEntity = factory.manufacturePojo(GruposDeInteresEntity.class);
        entityManager.persist(grupoEntity);
        grupoEntity.getVecinos().add(listaVecinos.get(0));
        listaVecinos.get(0).getGruposDeInteres().add(grupoEntity);
	}


    @Test
    void testCreateVecino() throws EntityNotFoundException, IllegalOperationException {
        VecinoEntity newEntity = factory.manufacturePojo(VecinoEntity.class);
        newEntity.setVecindario(listaVecindario.get(0));
        newEntity.setNombre("Gabriela Garcia");
        VecinoEntity result = vecinoService.createVecino(newEntity);
        assertNotNull(result);
        VecinoEntity entity = entityManager.find(VecinoEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getEdad(), entity.getEdad());
        assertEquals(newEntity.getDescripcion(), entity.getDescripcion());
        assertEquals(newEntity.getPorfile_pic(), entity.getPorfile_pic());
        assertEquals(newEntity.getGruposDeInteres(), entity.getGruposDeInteres());
    }

    @Test
    
    void testCreateVecinoWithNoValidName() {
        assertThrows(IllegalOperationException.class, () -> {
                VecinoEntity newEntity = factory.manufacturePojo(VecinoEntity.class);
                newEntity.setVecindario(listaVecindario.get(0));
                newEntity.setNombre("");
                vecinoService.createVecino(newEntity);
        });
    }

    @Test
    void testCreateVecinoWithNoValidName2() {
        assertThrows(IllegalOperationException.class, () -> {
                VecinoEntity newEntity = factory.manufacturePojo(VecinoEntity.class);
                newEntity.setVecindario(listaVecindario.get(0));
                newEntity.setNombre(null);
                vecinoService.createVecino(newEntity);
        }); 
    }

    @Test
    
    void testCreateVecinoConNombreExistente() {
        assertThrows(IllegalOperationException.class, () -> {
                VecinoEntity newEntity = factory.manufacturePojo(VecinoEntity.class);
                newEntity.setVecindario(listaVecindario.get(0));
                newEntity.setNombre(listaVecinos.get(0).getNombre());
                vecinoService.createVecino(newEntity);
        });
    }

    @Test
    void testCreateVecinoConVecindarioInvalido() {
        assertThrows(IllegalOperationException.class, () -> {
                VecinoEntity newEntity = factory.manufacturePojo(VecinoEntity.class);
                newEntity.setNombre("Francois De Garcia");
                VecindarioEntity vecindarioEntity = new VecindarioEntity();
                vecindarioEntity.setId(0L);
                newEntity.setVecindario(vecindarioEntity);
                vecinoService.createVecino(newEntity);
        });
    }   
    @Test
    void testCreateVecinoConVecindarioNulo() {
        assertThrows(IllegalOperationException.class, () -> {
                VecinoEntity newEntity = factory.manufacturePojo(VecinoEntity.class);
                newEntity.setNombre("Tambor Garcia");
                newEntity.setVecindario(null);
                vecinoService.createVecino(newEntity);
        });
    }

    @Test
    void testGetVecinos(){
         List<VecinoEntity> list = vecinoService.getVecinos();
        assertEquals(listaVecinos.size(), list.size());
        for (VecinoEntity entity : list) {
                boolean found = false;
                for (VecinoEntity storedEntity : listaVecinos) {
                        if (entity.getId().equals(storedEntity.getId())) {
                                found = true;
                        }
                }
                assertTrue(found);
        }    
    }
    @Test
    void testGetVecino() throws EntityNotFoundException{
        VecinoEntity entity = listaVecinos.get(0);
        VecinoEntity resultEntity = vecinoService.getVecino(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNombre(), resultEntity.getNombre());
        assertEquals(entity.getEdad(), resultEntity.getEdad());
        assertEquals(entity.getDescripcion(), resultEntity.getDescripcion());
        assertEquals(entity.getPorfile_pic(), resultEntity.getPorfile_pic());
    }

    @Test
    void testGetInvalidVecino() {
        assertThrows(EntityNotFoundException.class,()->{
                vecinoService.getVecino(0L);
        });
    }

    @Test
    void testUpdateVecino() throws EntityNotFoundException, IllegalOperationException {
        VecinoEntity entity = listaVecinos.get(0);
        VecinoEntity pojoEntity = factory.manufacturePojo(VecinoEntity.class);
        pojoEntity.setId(entity.getId());
        vecinoService.updateVecino(entity.getId(), pojoEntity);

        VecinoEntity resp = entityManager.find(VecinoEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getNombre(), resp.getNombre());
        assertEquals(pojoEntity.getEdad(), resp.getEdad());
        assertEquals(pojoEntity.getDescripcion(), resp.getDescripcion());
        assertEquals(pojoEntity.getPorfile_pic(), resp.getPorfile_pic());
    }

    @Test
    void testUpdateVecinoInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
                VecinoEntity pojoEntity = factory.manufacturePojo(VecinoEntity.class);
                pojoEntity.setId(0L);
                vecinoService.updateVecino(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateVecinoCOnNombreInvalido() {
        assertThrows(IllegalOperationException.class, () -> {
                VecinoEntity entity = listaVecinos.get(0);
                VecinoEntity pojoEntity = factory.manufacturePojo(VecinoEntity.class);
                pojoEntity.setNombre("");
                pojoEntity.setId(entity.getId());
                vecinoService.updateVecino(entity.getId(), pojoEntity);
        });
    }

    @Test
	void testUpdateVecinoConNombreInvalido2() {
		assertThrows(EntityNotFoundException.class, ()->{
			//create an invalid instance with random values with pojo librarie
			VecinoEntity pojoEntity = factory.manufacturePojo(VecinoEntity.class);

			//set the id of the instance to be replaced to the new invalid instance created by pojo
			pojoEntity.setId(0L);

			//update the instance with the invalid instance
			vecinoService.updateVecino(0L, pojoEntity);
		});
	}

    @Test
    void testDaleteVecino() throws EntityNotFoundException, IllegalOperationException {
        VecinoEntity entity = listaVecinos.get(1);
        vecinoService.deleteVecino(entity.getId());
        VecinoEntity deleted = entityManager.find(VecinoEntity.class, entity.getId());
        assertNull(deleted);
    }
    @Test
    void testDeleteVecinoInvalido() {
        assertThrows(EntityNotFoundException.class, ()->{
                vecinoService.deleteVecino(0L);
        });
    }




    
}
