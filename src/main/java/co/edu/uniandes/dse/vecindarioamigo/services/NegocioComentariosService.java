package co.edu.uniandes.dse.vecindarioamigo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;


import co.edu.uniandes.dse.vecindarioamigo.entities.ComentarioEntity;
import co.edu.uniandes.dse.vecindarioamigo.entities.NegocioEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.ErrorMessage;


import co.edu.uniandes.dse.vecindarioamigo.repositories.NegocioRepository;
import co.edu.uniandes.dse.vecindarioamigo.repositories.ComentarioRepository;

@Service
public class NegocioComentariosService {
    //dependency injections of the two entities of the relation
    @Autowired
	private ComentarioRepository comentarioRepository;

    @Autowired
	private NegocioRepository negocioRepository;

    
    //agregar comentario a un negocio
    @Transactional
	public ComentarioEntity addComentario(Long comentarioId, Long negocioId) throws EntityNotFoundException {
		
		Optional<ComentarioEntity> comentarioEntity = comentarioRepository.findById(comentarioId);
		if(comentarioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.COMENTARIO_NOT_FOUND);
		
		Optional<NegocioEntity> NegocioEntity = negocioRepository.findById(negocioId);
		if(NegocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);
		
		comentarioEntity.get().setNegocio(NegocioEntity.get());
		return comentarioEntity.get();
	}


    //retorna todas los comentarios que tiene un negocio
	@Transactional
	public List<ComentarioEntity> getComentarios(Long negocioId) throws EntityNotFoundException {
		Optional<NegocioEntity> negocioEntity = negocioRepository.findById(negocioId);
		if(negocioEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.Negocio_NOT_FOUND);
		
		return negocioEntity.get().getComentarios();
	}

}
