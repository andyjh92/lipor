/**
 * Fehlerklasse für eine nicht definierte Handaktion
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class ActionIllegalActionException extends ActionException{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 76669686147151142L;

	/**
	 * Standartkonstuktor
	 */
	public ActionIllegalActionException() {
		super("Handaktion ist unbekannt.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public ActionIllegalActionException(String text) {
		super(text);
	}
}
