
package co.edu.uniandes.dse.vecindarioamigo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import co.edu.uniandes.dse.vecindarioamigo.entities.OfertaEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;

import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.OfertaRepository;

@Service
public class NegocioOfertaService {

	@Autowired
	private NegocioRepository negocioRepository;

	@Autowired
	private OfertaRepository ofertaRepository;

	// agrega una oferta a un negocio
	@Transactional
	public OfertaEntity addOferta(Long OfertaId, Long negocioId) throws EntityNotFoundException {

		Optional<OfertaEntity> OfertaEntity = ofertaRepository.findById(OfertaId);
		if (OfertaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFERTA_NOT_FOUND);

		Optional<NegocioEntity> NegocioEntity = negocioRepository.findById(negocioId);
		if (NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);

		OfertaEntity.get().setNegocio(NegocioEntity.get());
		return OfertaEntity.get();
	}

	// retorna todas las ofertas que tiene un negocio
	@Transactional
	public List<OfertaEntity> getOfertas(Long negocioId) throws EntityNotFoundException {
		Optional<NegocioEntity> negocioEntity = negocioRepository.findById(negocioId);
		if (negocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);

		return negocioEntity.get().getOfertas();
	}

	@Transactional
	public void removeOferta(Long OfertaId, Long negocioId) throws EntityNotFoundException {
		Optional<OfertaEntity> OfertaEntity = ofertaRepository.findById(OfertaId);
		if (OfertaEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.OFERTA_NOT_FOUND);

		Optional<NegocioEntity> NegocioEntity = negocioRepository.findById(negocioId);
		if (NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);

		if (OfertaEntity.get().getNegocio().equals(NegocioEntity.get())) {
			OfertaEntity.get().setNegocio(null);
		} else {
			throw new EntityNotFoundException(ErrorMessage.OFERTA_NOT_ASSOCIATED_WITH_NEGOCIO);
		}

	}
}