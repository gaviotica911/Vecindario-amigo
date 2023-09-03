package co.edu.uniandes.dse.vecindarioamigo.exceptions;

public final class ErrorMessage {
	public static final String VECINDARIO_NOT_FOUND = "The neighborhood with the given id was not found";
    public static final String SHOPPING_MALL_NOT_FOUND = "The shopping mall with the given id was not found";
    public static final String NEIGHBORHOOD_NOT_FOUND = "The neighborhood with the given id was not found";
    public static final String BUSINESS_NOT_FOUND = "The business with the given id was not found";
    public static final String GREEN_ZONE_NOT_FOUND = "The green zone with the given id was not found";

	private ErrorMessage() {
		throw new IllegalStateException("Utility class");
	}
}