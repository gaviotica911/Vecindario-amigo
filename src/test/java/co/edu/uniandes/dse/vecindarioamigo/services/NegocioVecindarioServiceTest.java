package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;

public class NegocioVecindarioServiceTest {

    @InjectMocks
    private NegocioVecindarioService service;

    @Mock
    private NegocioRepository negocioRepository;

    @Mock
    private VecindarioRepository vecindarioRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReplaceVecindario() throws EntityNotFoundException {
        NegocioEntity negocio = new NegocioEntity();
        negocio.setId(1L);

        VecindarioEntity vecindario = new VecindarioEntity();
        vecindario.setId(2L);

        when(negocioRepository.findById(1L)).thenReturn(Optional.of(negocio));
        when(vecindarioRepository.findById(2L)).thenReturn(Optional.of(vecindario));

        NegocioEntity result = service.replaceVecindario(1L, 2L);

        assertEquals(vecindario, result.getVecindario());
        verify(negocioRepository).findById(1L);
        verify(vecindarioRepository).findById(2L);
    }

    @Test
    public void testRemoveVecindario() throws EntityNotFoundException {
        NegocioEntity negocio = new NegocioEntity();
        negocio.setId(1L);

        VecindarioEntity vecindario = new VecindarioEntity();
        vecindario.setId(2L);
        negocio.setVecindario(vecindario);

        when(negocioRepository.findById(1L)).thenReturn(Optional.of(negocio));
        when(vecindarioRepository.findById(2L)).thenReturn(Optional.of(vecindario));

        service.removeVecindario(1L);

        assertNull(negocio.getVecindario());
        verify(negocioRepository).findById(1L);
        verify(vecindarioRepository).findById(2L);
    }

    @Test
    public void testReplaceVecindarioNotFound() {
        when(negocioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.replaceVecindario(1L, 2L));
        verify(negocioRepository).findById(1L);
    }

    @Test
    public void testRemoveVecindarioNotFound() {
        when(negocioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.removeVecindario(1L));
        verify(negocioRepository).findById(1L);
    }
}
