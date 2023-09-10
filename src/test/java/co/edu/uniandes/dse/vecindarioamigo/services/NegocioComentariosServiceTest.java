package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;

import co.edu.uniandes.dse.vecindarioamigo.repositories.ComentarioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;

public class NegocioComentariosServiceTest {

    @InjectMocks
    private NegocioComentariosService service;

    @Mock
    private NegocioRepository negocioRepository;

    @Mock
    private ComentarioRepository comentarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddComentario() throws EntityNotFoundException {
        ComentarioEntity comentario = new ComentarioEntity();
        comentario.setId(1L);

        NegocioEntity negocio = new NegocioEntity();
        negocio.setId(2L);

        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(comentario));
        when(negocioRepository.findById(2L)).thenReturn(Optional.of(negocio));

        ComentarioEntity result = service.addComentario(1L, 2L);

        assertEquals(negocio, result.getNegocio());
        verify(comentarioRepository).findById(1L);
        verify(negocioRepository).findById(2L);
    }

    @Test
    public void testGetComentarios() throws EntityNotFoundException {
        NegocioEntity negocio = new NegocioEntity();
        negocio.setId(1L);

        ComentarioEntity comentario1 = new ComentarioEntity();
        comentario1.setId(2L);

        ComentarioEntity comentario2 = new ComentarioEntity();
        comentario2.setId(3L);

        List<ComentarioEntity> comentarios = new ArrayList<>(Arrays.asList(comentario1, comentario2));
        negocio.setComentarios(comentarios);

        when(negocioRepository.findById(1L)).thenReturn(Optional.of(negocio));

        List<ComentarioEntity> result = service.getComentarios(1L);

        assertEquals(comentarios, result);
        verify(negocioRepository).findById(1L);
    }

    @Test
    public void testAddComentarioNotFound() {
        when(comentarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.addComentario(1L, 2L));
        verify(comentarioRepository).findById(1L);
    }

    @Test
    public void testGetComentariosNotFound() {
        when(negocioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getComentarios(1L));
        verify(negocioRepository).findById(1L);
    }
}
