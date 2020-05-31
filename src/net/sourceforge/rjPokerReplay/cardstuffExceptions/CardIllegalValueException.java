/**
 * Oberklasse der Karten-Fehler
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */
public class CardIllegalValueException extends CardException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -4486423206399476996L;

	/**
	 * Konsturktor ohne Parameter
	 */
	public CardIllegalValueException() {
		super(Messages.CardIllegalValueException_0);
	}

	/**
	 * Konsturkor mit einem Fehlertext
	 * @param text Der Fehlertext
	 */
	public CardIllegalValueException(String text) {
		super(text);
	}

}
