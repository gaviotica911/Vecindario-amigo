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

import java.util.Optional;

@Service
public class NegocioService {

	@Autowired
	private NegocioRepository negocioRepository;

	// crea un negocio
	@Transactional
	public NegocioEntity createNegocio(NegocioEntity negocioEntity)
			throws EntityNotFoundException, IllegalOperationException {

		if (!negocioRepository.findByNombre(negocioEntity.getNombre()).isEmpty()) {
			throw new IllegalOperationException("Negocio name already exists");
		}
		return negocioRepository.save(negocioEntity);
	}

	// retorna una lista de todos los negocios
	@Transactional
	public List<NegocioEntity> getNegocios() {

		return negocioRepository.findAll();
	}

	// retorno un negocio dado un Id
	@Transactional
	public NegocioEntity getNegocio(Long NegocioId) throws EntityNotFoundException {
		Optional<NegocioEntity> NegocioEntity = negocioRepository.findById(NegocioId);
		if (NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);
		return NegocioEntity.get();

	}

	// update de un negocio dado un id
	@Transactional
	public NegocioEntity updateNegocio(Long NegocioId, NegocioEntity Negocio)
			throws EntityNotFoundException, IllegalOperationException {
		Optional<NegocioEntity> negocioEntity = negocioRepository.findById(NegocioId);
		if (negocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);
		Negocio.setId(NegocioId);
		return negocioRepository.save(Negocio);
	}

	// borra un negocio
	@Transactional
	public void deleteNegocio(Long NegocioId) throws EntityNotFoundException, IllegalOperationException {
		Optional<NegocioEntity> negocioEntity = negocioRepository.findById(NegocioId);
		if (negocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);
		negocioRepository.deleteById(NegocioId);
	}
}
