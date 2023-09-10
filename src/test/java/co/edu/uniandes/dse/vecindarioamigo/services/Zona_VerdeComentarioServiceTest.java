package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.ComentarioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.Zona_VerdeRepository;

public class Zona_VerdeComentarioServiceTest {

    @InjectMocks
    private Zona_VerdeComentarioService service;

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private Zona_VerdeRepository zona_VerdeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddComentario() throws EntityNotFoundException {
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setId(1L);

        Zona_VerdeEntity zona = new Zona_VerdeEntity();
        zona.setId(2L);

        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(comentario));
        when(zona_VerdeRepository.findById(2L)).thenReturn(Optional.of(zona));

        ComentarioEntity result = service.addComentario(1L, 2L);

        assertEquals(zona, result.getZonaVerde());
    }

}
