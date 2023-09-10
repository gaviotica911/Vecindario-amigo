package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.uniandes.dse.vecindarioamigo.entities.VecindarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecindarioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.Zona_VerdeRepository;

public class Zona_VerdeVecindarioServiceTest {

    @InjectMocks
    private Zona_VerdeVecindarioService service;

    @Mock
    private VecindarioRepository vecindarioRepository;

    @Mock
    private Zona_VerdeRepository zona_VerdeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRemoveVecindario() throws EntityNotFoundException {
        Zona_VerdeEntity zona = new Zona_VerdeEntity();
        zona.setId(1L);

        VecindarioEntity vecindario = new VecindarioEntity();
        vecindario.setId(2L);
        zona.setVecindario(vecindario);

        when(zona_VerdeRepository.findById(1L)).thenReturn(Optional.of(zona));
        when(vecindarioRepository.findById(2L)).thenReturn(Optional.of(vecindario));

        service.removeVecindario(1L);

        assertNull(zona.getVecindario());
    }

    @Test
    public void testGetVecindario() throws EntityNotFoundException {
        Zona_VerdeEntity zona = new Zona_VerdeEntity();
        zona.setId(1L);

        VecindarioEntity vecindario = new VecindarioEntity();
        vecindario.setId(2L);
        zona.setVecindario(vecindario);

        when(zona_VerdeRepository.findById(1L)).thenReturn(Optional.of(zona));

        VecindarioEntity result = service.getVecindario(1L);

        assertEquals(vecindario, result);
    }
}
