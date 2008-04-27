/**
 * Fehlerklasse für den unbekannten Aufbau einer Handhistorie
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandhistoryIllegalFormatException extends HandhistoryException{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 556097401026712481L;

	/**
	 * Standartkonstuktor
	 */
	public HandhistoryIllegalFormatException() {
		super("Handhistorie hat einen unbekannten Aufbau.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandhistoryIllegalFormatException(String text) {
		super(text);
	}
}
