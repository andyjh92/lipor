/**
 * Fehlerklasse fuer nicht definierte Kartenfarbe
 */
package io.gitlab.lipor.cardstuffExceptions;

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
		super(Messages.CardIllegalSuitException_0);
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
