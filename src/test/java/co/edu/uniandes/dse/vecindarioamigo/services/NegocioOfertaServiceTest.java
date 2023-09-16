package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import co.edu.uniandes.dse.vecindarioamigo.entities.OfertaEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.OfertaRepository;

public class NegocioOfertaServiceTest {

    @InjectMocks
    private NegocioOfertaService service;

    @Mock
    private NegocioRepository negocioRepository;

    @Mock
    private OfertaRepository ofertaRepository;

    private NegocioEntity negocio;
    private OfertaEntity oferta;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        negocio = new NegocioEntity();
        negocio.setId(1L);

        oferta = new OfertaEntity();
        oferta.setId(2L);

        when(negocioRepository.findById(1L)).thenReturn(Optional.of(negocio));
        when(ofertaRepository.findById(2L)).thenReturn(Optional.of(oferta));
    }

    @Test
    public void testAddOferta() throws EntityNotFoundException {
        OfertaEntity result = service.addOferta(2L, 1L);
        assertEquals(negocio, result.getNegocio());
    }

    @Test
    public void testGetOfertas() throws EntityNotFoundException {
        negocio.setOfertas(Arrays.asList(oferta));
        when(negocioRepository.findById(1L)).thenReturn(Optional.of(negocio));

        List<OfertaEntity> ofertas = service.getOfertas(1L);
        assertTrue(ofertas.contains(oferta));
    }

    @Test
    public void testAddOferta_EntityNotFoundException() {
        when(ofertaRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.addOferta(3L, 1L);
        });
    }

    @Test
    public void testGetOfertas_EntityNotFoundException() {
        when(negocioRepository.findById(4L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.getOfertas(4L);
        });
    }
}
