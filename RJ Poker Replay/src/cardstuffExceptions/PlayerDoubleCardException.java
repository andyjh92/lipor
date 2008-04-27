/**
 * Fehlerklasse wenn einem Spieler zwei gleiche Karten zugewiesen werden
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */
public class PlayerDoubleCardException extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 4819098832027705887L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerDoubleCardException() {
		super("Der Spieler hat diese Karte bereits.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerDoubleCardException(String text) {
		super(text);
	}
}
