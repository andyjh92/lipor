/**
 * Fehlerklasse für einen nicht erlaubte Sitzplätzen
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalSeatException extends HandException{
	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -3885682499216305525L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalSeatException() {
		super("Der Sitzplaz ist nicht vorhanden oder besetzt.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalSeatException(String text) {
		super(text);
	}
}
