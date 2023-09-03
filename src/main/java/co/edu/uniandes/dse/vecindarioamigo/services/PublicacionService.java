package co.edu.uniandes.dse.vecindarioamigo.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.vecindarioamigo.entities.PublicacionEntity;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.vecindarioamigo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.vecindarioamigo.repositories.VecinoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PublicacionService {
    @Autowired
    VecinoRepository vecinoRepository;

    @Transactional
    public PublicacionEntity createPost(PublicacionEntity publicacionEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de una publicación");
                
        if (publicacionEntity.getVecino() == null)
                throw new IllegalOperationException("Vecino is not valid");
                
/*
        Optional<VecinoEntity> vecinoEntity = vecinoRepository.findById(publicacionEntity.getVecino().getId());
        if (vecinoEntity.isEmpty())
                throw new IllegalOperationException("Vecino is not valid");

        if (!validateISBN(bookEntity.getIsbn()))
                throw new IllegalOperationException("ISBN is not valid");

        if (!bookRepository.findByIsbn(bookEntity.getIsbn()).isEmpty())
                throw new IllegalOperationException("ISBN already exists");

        bookEntity.setEditorial(editorialEntity.get());
        log.info("Termina proceso de creación del libro");
        */
        return publicacionEntity; //PublicacionEntity.save(publicacionEntity); 
         

}

   

}
