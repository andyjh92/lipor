/**
 * Fehlerklasse für nicht definierte Kartenfarbe
 */
package cardstuffExceptions;


/**
 * @author Ralf Joswig
 */
public class CardIllegalSuitException extends CardException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -3521618558426232241L;

	/**
	 * Standartkonstuktor
	 */
	public CardIllegalSuitException() {
		super("Unbekannte Kartenfarbe!");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public CardIllegalSuitException(String text) {
		super(text);
	}

}
