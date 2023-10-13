package co.edu.uniandes.dse.vecindarioamigo.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ComentarioDetailDTO {
    private List<PublicacionDTO> publicaciones = new ArrayList<>();

}
