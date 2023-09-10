package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.CentroComercialRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;

@SpringBootTest
@Transactional
public class NegocioCentroComercialServiceTest {

    @Autowired
    private NegocioCentroComercialService servicio;

    @MockBean
    private NegocioRepository negocioRepository;

    @MockBean
    private CentroComercialRepository centroComercialRepository;

    @Test
    public void testReplaceCentroComercial() throws EntityNotFoundException {
        NegocioEntity negocio = new NegocioEntity();
        CentroComercialEntity centroComercial = new CentroComercialEntity();

        when(negocioRepository.findById(any(Long.class))).thenReturn(Optional.of(negocio));
        when(centroComercialRepository.findById(any(Long.class))).thenReturn(Optional.of(centroComercial));

        NegocioEntity result = servicio.replaceCentroComercial(1L, 1L);
        assertEquals(centroComercial, result.getCentroComercial());
    }

    @Test
    public void testReplaceCentroComercial_NegocioNotFound() {
        when(negocioRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> {
            servicio.replaceCentroComercial(1L, 1L);
        });
    }

    @Test
    public void testReplaceCentroComercial_CentroComercialNotFound() {
        when(negocioRepository.findById(any(Long.class))).thenReturn(Optional.of(new NegocioEntity()));
        when(centroComercialRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> {
            servicio.replaceCentroComercial(1L, 1L);
        });
    }

    @Test
    public void testRemoveCentroComercial() throws EntityNotFoundException {
        NegocioEntity negocio = new NegocioEntity();
        negocio.setCentroComercial(new CentroComercialEntity());

        when(negocioRepository.findById(any(Long.class))).thenReturn(Optional.of(negocio));

        servicio.removeCentroComercial(1L);
        assertNull(negocio.getCentroComercial());
    }

    @Test
    public void testRemoveCentroComercial_NegocioNotFound() {
        when(negocioRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> {
            servicio.removeCentroComercial(1L);
        });
    }
}
