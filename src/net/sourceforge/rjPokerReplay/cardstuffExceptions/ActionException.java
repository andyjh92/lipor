/**
 * Fehlerklasse fuer allg. Fehler im Zusammenhang mit einer Handaktion
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class ActionException extends Exception{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -2198505568643968406L;	

	/**
	 * Standartkonstuktor
	 */
	public ActionException() {
		super(Messages.ActionException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public ActionException(String text) {
		super(text);
	}
}
