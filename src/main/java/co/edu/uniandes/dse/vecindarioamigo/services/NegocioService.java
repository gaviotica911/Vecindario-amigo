package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NegocioService {
	@Autowired
	NegocioRepository negocioRepository;
	
	
	@Transactional
	public NegocioEntity createNegocio (NegocioEntity negocioEntity) throws EntityNotFoundException, IllegalOperationException {
	                
		if (!NegocioRepository.findByName(negocioEntity.getNombre()).isEmpty()) {
			throw new IllegalOperationException("Negocio name already exists");
		}

	        return NegocioRepository.save(negocioEntity);
	}
	
	
	@Transactional
	public List<NegocioEntity> getNegocios() {

		return NegocioRepository.findAll();
	}
	
	
	@Transactional
	public NegocioEntity getNegocio(Long NegocioId) throws EntityNotFoundException {
		
		Optional<NegocioEntity> NegocioEntity = NegocioRepository.findById(NegocioId);
		if (NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);
		return NegocioEntity.get();
	}
		
	
	@Transactional
	public NegocioEntity updateNegocio(Long NegocioId, NegocioEntity Negocio)
			throws EntityNotFoundException, IllegalOperationException {
		Optional<NegocioEntity> negocioEntity = NegocioRepository.findById(NegocioId);
		if (NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);


		Negocio.setId(NegocioId);
		return NegocioRepository.save(Negocio);
	}
	
	@Transactional
	public void deleteNegocio(Long NegocioId) throws EntityNotFoundException, IllegalOperationException {
		Optional<NegocioEntity> negocioEntity = NegocioRepository.findById(NegocioId);
		if (NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);
		NegocioRepository.deleteById(NegocioId);
	}
}











