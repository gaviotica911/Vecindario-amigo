package co.edu.uniandes.dse.vecindarioamigo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;


import co.edu.uniandes.dse.vecindarioamigo.entities.CentroComercialEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;


import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.CentroComercialRepository;





@Service
public class NegocioCentroComercialService {

    @Autowired
	private CentroComercialRepository centrocomercialRepository;

    @Autowired
	private NegocioRepository negocioRepository;



    //remplaza el centro comercial de un negocio
    @Transactional
	public NegocioEntity replaceCentroComercial(Long NegocioId, Long CentroComercialId) throws EntityNotFoundException {
		
		Optional<NegocioEntity> NegocioEntity = negocioRepository.findById(NegocioId);
		if (NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);

		Optional<CentroComercialEntity> CentroComercialEntity = centrocomercialRepository.findById(CentroComercialId);
		if (CentroComercialEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CentroComercial_NOT_FOUND);

		NegocioEntity.get().setCentroComercial(CentroComercialEntity.get());

		return NegocioEntity.get();


    //borra un negocio de un Centro Comerial
    @Transactional
        public void removeCentroComercial(Long NegocioId) throws EntityNotFoundException {
            Optional<NegocioEntity> NegocioEntity = negocioRepository.findById(NegocioId);
            if (NegocioEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);
    
            Optional<CentroComercialEntity> CentroComercialEntity = centrocomercialRepository.findById(NegocioEntity.get().getCentroComercial().getId());
            CentroComercialEntity.ifPresent(CentroComercial -> CentroComercial.getNegocios().remove(NegocioEntity.get()));
    
            NegocioEntity.get().setCentroComercial(null);
            log.info("Termina proceso de borrar la CentroComercial del libro con id = {0}", NegocioId);
        }
}
