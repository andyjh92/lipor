/**
 * Fehlerklasse für allg. Fehler im Zusammenhang mit einer Handaktion
 */
package cardstuffExceptions;

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
		super("Allg. Fehler im Zusammenhang Handaktion.");
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
