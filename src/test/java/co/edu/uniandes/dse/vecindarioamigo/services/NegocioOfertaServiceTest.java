package co.edu.uniandes.dse.vecindarioamigo.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.edu.uniandes.dse.vecindarioamigo.entities.OfertaEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;

import java.util.List;

@SpringBootTest
public class NegocioOfertaServiceTest {

    @Autowired
    private NegocioOfertaService service;

    private NegocioEntity negocio;
    private OfertaEntity oferta;

    @Test
    public void testAddOferta() throws EntityNotFoundException {
        OfertaEntity result = service.addOferta(oferta.getId(), negocio.getId());
        assertEquals(negocio, result.getNegocio());
    }

    @Test
    public void testGetOfertas() throws EntityNotFoundException {
        List<OfertaEntity> ofertas = service.getOfertas(negocio.getId());
        assertTrue(ofertas.contains(oferta));
    }

}
