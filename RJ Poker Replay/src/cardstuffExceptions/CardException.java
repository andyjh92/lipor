/**
 * Fehlerklasse für llg. Kartenfehler
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */
public class CardException extends Exception {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 6414904391515276799L;

	/**
	 * Standartkonstuktor
	 */
	public CardException() {
		super("Allg. Fehler bei Kartenbearbeitung");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public CardException(String text) {
		super(text);
	}
}
