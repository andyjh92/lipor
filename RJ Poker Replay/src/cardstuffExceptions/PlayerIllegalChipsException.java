/**
 * Fehlerklasse für fehlerhafte Chipsbeträge
 */

package cardstuffExceptions;

public class PlayerIllegalChipsException extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 2719905990115641517L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerIllegalChipsException() {
		super("Nicht erlaubter Chipsbetrag.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerIllegalChipsException(String text) {
		super(text);
	}
}
