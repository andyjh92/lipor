/**
 * Fehlerklasse wenn einer Hand mehr Spieler als Plätze zugewiesen werden
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */


public class HandToMuchPlayersException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 6498280908258765470L;

	/**
	 * Standartkonstuktor
	 */
	public HandToMuchPlayersException() {
		super("Es sind nicht genug Plätze für die Spieler vorhanden.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandToMuchPlayersException(String text) {
		super(text);
	}
	
}
