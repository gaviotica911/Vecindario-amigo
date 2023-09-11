package co.edu.uniandes.dse.vecindarioamigo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.Zona_VerdeRepository;

@Service

public class Zona_VerdeService {
	@Autowired
	Zona_VerdeRepository Zona_VerdeRepository;

	// creo zonan verde
	@Transactional
	public Zona_VerdeEntity createZona_Verde(Zona_VerdeEntity zona_VerdeEntity)
			throws EntityNotFoundException, IllegalOperationException {

		if (!Zona_VerdeRepository.findByNombre(zona_VerdeEntity.getNombre()).isEmpty()) {
			throw new IllegalOperationException("Zona_Verde name already exists");
		}
		if (zona_VerdeEntity.getVecindario() == null) {
			throw new IllegalOperationException("Zona_Verde name is null");
		}
		return Zona_VerdeRepository.save(zona_VerdeEntity);
	}

	// retorna una lista con todas las zona verdes
	@Transactional
	public List<Zona_VerdeEntity> getZona_Verdes() {
		return Zona_VerdeRepository.findAll();
	}

	// retorno una zona verde dado un id
	@Transactional
	public Zona_VerdeEntity getZona_Verde(Long Zona_VerdeId) throws EntityNotFoundException {

		Optional<Zona_VerdeEntity> Zona_VerdeEntity = Zona_VerdeRepository.findById(Zona_VerdeId);
		if (Zona_VerdeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Zona_Verde_NOT_FOUND);
		return Zona_VerdeEntity.get();
	}

	@Transactional
	public Zona_VerdeEntity updateZona_Verde(Long Zona_VerdeId, Zona_VerdeEntity Zona_Verde)
			throws EntityNotFoundException, IllegalOperationException {
		Optional<Zona_VerdeEntity> Zona_VerdeEntity = Zona_VerdeRepository.findById(Zona_VerdeId);
		if (Zona_VerdeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Zona_Verde_NOT_FOUND);

		Zona_Verde.setId(Zona_VerdeId);
		return Zona_VerdeRepository.save(Zona_Verde);
	}

	@Transactional
	public void deleteZona_Verde(Long Zona_VerdeId) throws EntityNotFoundException, IllegalOperationException {
		Optional<Zona_VerdeEntity> Zona_VerdeEntity = Zona_VerdeRepository.findById(Zona_VerdeId);
		if (Zona_VerdeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Zona_Verde_NOT_FOUND);
		Zona_VerdeRepository.deleteById(Zona_VerdeId);
	}

}
