package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.CentroComercialRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class CentroComercialVecindarioServiceTest {
    @InjectMocks
    private CentroComercialVecindarioService centroComercialVecindarioService;

    @Mock
    private CentroComercialRepository centroComercialRepository;

    @Mock
    private VecindarioRepository vecindarioRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRemoveVecindario() throws EntityNotFoundException {
        CentroComercialEntity centroComercial = new CentroComercialEntity();
        centroComercial.setId(1L);

        VecindarioEntity vecindario = new VecindarioEntity();
        vecindario.setId(2L);

        when(centroComercialRepository.findById(1L)).thenReturn(Optional.of(centroComercial));
        when(vecindarioRepository.findById(2L)).thenReturn(Optional.of(vecindario));

        CentroComercialEntity result = centroComercialVecindarioService.replaceVecindario(1L, 2L);

        assertEquals(vecindario, result.getVecindario());
        verify(centroComercialRepository).findById(1L);
        verify(vecindarioRepository).findById(2L);
    }

    @Test
    public void testReplaceVecindario() throws EntityNotFoundException {
        CentroComercialEntity centroComercial = new CentroComercialEntity();
        centroComercial.setId(1L);

        VecindarioEntity vecindario = new VecindarioEntity();
        vecindario.setId(2L);

        when(centroComercialRepository.findById(1L)).thenReturn(Optional.of(centroComercial));
        when(vecindarioRepository.findById(2L)).thenReturn(Optional.of(vecindario));

        CentroComercialEntity result = centroComercialVecindarioService.replaceVecindario(1L, 2L);

        assertEquals(vecindario, result.getVecindario());
        verify(centroComercialRepository).findById(1L);
        verify(vecindarioRepository).findById(2L);
    }

    @Test
    public void testRemoveVecindarioNotFound() {
        when(centroComercialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> centroComercialVecindarioService.removeVecindario(1L));
        verify(centroComercialRepository).findById(1L);
    }

    @Test
    public void testReplaceVecindarioNotFound() {
        when(centroComercialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> centroComercialVecindarioService.replaceVecindario(1L, 2L));
        verify(centroComercialRepository).findById(1L);
    }

}
