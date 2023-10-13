package co.edu.uniandes.dse.vecindarioamigo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;


import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.Zona_VerdeEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;


import co.edu.uniandes.dse.vecindarioamigo.repositories.Zona_VerdeRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.ComentarioRepository;

@Service
public class Zona_VerdeComentarioService {
    //dependency injections of the two entities of the relation
    @Autowired
	private ComentarioRepository comentarioRepository;

    @Autowired
	private Zona_VerdeRepository zona_VerdeRepository;

    
    //agregar comentario a un Zona_Verde
    @Transactional
	public ComentarioEntity addComentario(Long comentarioId, Long Zona_VerdeId) throws EntityNotFoundException {
		
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if(comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
		
		Optional<Zona_VerdeEntity> zona_VerdeEntity = zona_VerdeRepository.findById(Zona_VerdeId);
		if(zona_VerdeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ZONA_VERDE_NOT_FOUND);
		
		comentarioEntity.get().setZonaVerde(zona_VerdeEntity.get());
		return comentarioEntity.get();
	}


    //retorna todas los comentarios que tiene un Zona_Verde
	@Transactional
	public List<ComentarioEntity> getComentarios(Long Zona_VerdeId) throws EntityNotFoundException {
		Optional<Zona_VerdeEntity> zona_VerdeEntity = zona_VerdeRepository.findById(Zona_VerdeId);
		if(zona_VerdeEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.ZONA_VERDE_NOT_FOUND);
		
		return zona_VerdeEntity.get().getReviews();
	}

}
