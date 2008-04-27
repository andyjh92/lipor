/**
 * Fehlerklasse wenn einem Spieler zuviele Pocketkarten zugeteilt werden
 */
package cardstuffExceptions;

public class PlayerToMuchPocketCards extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -1367144567742224479L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerToMuchPocketCards() {
		super("Spieler hat bereits zwei Karten");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerToMuchPocketCards(String text) {
		super(text);
	}
}
