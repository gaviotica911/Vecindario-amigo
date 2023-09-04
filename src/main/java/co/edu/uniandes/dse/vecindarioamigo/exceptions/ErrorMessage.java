package co.edu.uniandes.dse.vecindarioamigo.exceptions;

public final class ErrorMessage {
	public static final String VECINDARIO_NOT_FOUND = "The neighborhood with the given id was not found";
    public static final String PUBLICACION_NOT_FOUND = "The post with the given id was not found";
    public static final String VECINO_NOT_FOUND = "The neighbor with the given id was not found";
    public static final String SHOPPING_MALL_NOT_FOUND = "The shopping mall with the given id was not found";
    public static final String NEIGHBORHOOD_NOT_FOUND = "The neighborhood with the given id was not found";
    public static final String BUSINESS_NOT_FOUND = "The business with the given id was not found";
    public static final String GREEN_ZONE_NOT_FOUND = "The green zone with the given id was not found";
    public static final String NEIGHBOR_NOT_FOUND = "The neighbor with the given id was not found";
    public static final String OFFER_NOT_FOUND = "The OFFER with the given id was not found";
    public static final String Negocio_NOT_FOUND = "The negocio is not found";
    public static final String Zona_Verde_NOT_FOUND = "The zona verde is not found";
    public static final String OFERTA_NOT_FOUND = null;
    public static final String COMENTARIO_NOT_FOUND = "The Comentario is not found";
    public static final String CentroComercial_NOT_FOUND = null;
    public static final String GRUPO_DE_INTERES_NOT_FOUND = "The Grupo de interes is not found";
    public static final String PUBLICACION_NOT_FOUND = "The Publication is not found";



	private ErrorMessage() {
		throw new IllegalStateException("Utility class");
	}
}