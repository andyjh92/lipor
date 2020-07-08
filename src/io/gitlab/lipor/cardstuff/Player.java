/**
 *  * Copyright (C) 2008  Ralf Joswig
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 *
 */
package io.gitlab.lipor.cardstuff;

import io.gitlab.lipor.cardstuffExceptions.PlayerDoubleCardException;
import io.gitlab.lipor.cardstuffExceptions.PlayerException;
import io.gitlab.lipor.cardstuffExceptions.PlayerIllegalChipsException;
import io.gitlab.lipor.cardstuffExceptions.PlayerIllegalStateException;
import io.gitlab.lipor.cardstuffExceptions.PlayerMissingNameException;
import io.gitlab.lipor.cardstuffExceptions.PlayerToMuchPocketCards;
import io.gitlab.lipor.util.ErrorHandler;

/**
 * @author Ralf Joswig
 *
 * Klasse zur Darstellung und Handhabung eines Pokerspielers
 * 
 */
public class Player {

	/***************************************************************************
	 * Konstanten
	 **************************************************************************/
	// Spieler hat gefoldeet
	public static final int FOLDED = 1;
	
	// Spieler ist aktiv
	public static final int AKTIV = 2;
	
	// Spieler sezt aus
	public static final int SITTINGOUT = 3;
	
	/***************************************************************************
	 * Instanz Eigenschaften
	 **************************************************************************/
	// Pocket-Karten des Spielers
	private Card pocketCards[] = new Card[2];
	
	// Geld des Spielers
	private double chips = 0.0;
	
	// Name des Spielers
	private String name = ""; //$NON-NLS-1$
	
	// Status
	private int state = AKTIV;

	/***************************************************************************
	 * Konstruktoren 
	 **************************************************************************/
	/**
	 * Standartkonstruktor
	 * 
	 * @param name Name des Spielers
	 * @throws PlayerMissingNameException
	 */
	public Player(String name) throws PlayerMissingNameException {
		// pruefen ob der Name gefuellt ist
		if (name == null || name.equals("")) { //$NON-NLS-1$
			throw new PlayerMissingNameException();
		}
		
		// Felder belegen, alle bis auf den Namen mit Defaultwerten
		this.name = name;
	}
	
	/**
	 * Spieler mit Namen und Chips
	 * 
	 * @param name Name des Spielers
	 * @param chips Anzahl Chips des Spielers
	 * @throws PlayerMissingNameException 
	 * @throws PlayerIllegalChipsException 
	 */
	public Player(String name, double chips) throws PlayerMissingNameException, PlayerIllegalChipsException {
		// pruefen ob der Name gefuellt ist
		if (name == null || name.equals("")) { //$NON-NLS-1$
			throw new PlayerMissingNameException();
		}
		
		// pruefen ob der Chipsberag erlaubt, wenn nein gehtes mit einer Exception direkt raus
		checkMoneyValue(chips);
		
		// Felder belegen, handCards und pocketCars mit Defaultwerten
		this.name = name;
		this.chips = chips;
	}
	
	/**
	 * Vollstaendiger Konstruktor
	 * 
	 * @param name Name des Spielers
	 * @param chips Anzahl Chips des Spielers
	 * @throws PlayerMissingNameException 
	 * @throws PlayerIllegalChipsException 
	 * @throws PlayerIllegalStateException 
	 */
	public Player(String name, double chips, int state) throws PlayerMissingNameException, PlayerIllegalChipsException, PlayerIllegalStateException {
		// pruefen ob der Name gefuellt ist
		if (name == null || name.equals("")) { //$NON-NLS-1$
			throw new PlayerMissingNameException();
		}
		
		// pruefen ob der Chipsberag erlaubt, wenn nein geht es mit einer Exception direkt raus
		checkMoneyValue(chips);
		
		// Felder belegen, handCards und pocketCars mit Defaultwerten
		this.name = name;
		this.chips = chips;
		setState(state);
	}
	/***************************************************************************
	 * Getter
	 **************************************************************************/
	/**
	 * Gibt die Pocketcards zurueck
	 * 
	 * @return Die Pocketcards des Spielers
	 */
	public Card[] getPocketCards() {
		return pocketCards;
	}

	/**
	 * Gibt die Anzahl an Chips zurueck
	 * 
	 * @return Die Anzahl an Chips die der Spieler hat
	 */
	public double getChips() {
		return chips;
	}

	/**
	 * Gibt den Namen zurueck
	 * 
	 * @return Der Name des Spielers
	 */
	public String getName() {
		return name;
	}
	

	/**
	 * Gibt den aktuellen status des Spielers zurueck
	 * 
	 * @return Der Status des Spielers
	 */
	public int getState() {
		return state;
	}
	
	/***************************************************************************
	 * Setter
     ***************************************************************************/
	/**
	 * Setzt den Satus des Spielers
	 * Die Moeglichen Sati sind als Konstanten definiert
	 * 
	 * @param state the state to set
	 * @throws PlayerIllegalStateException 
	 */
	public void setState(int state) throws PlayerIllegalStateException {
		// ist es ein bekannter Status
		if (state != FOLDED &&
			  state != SITTINGOUT &&
			  state != AKTIV) {
			// nein, dann Fehlermeldung
			throw new PlayerIllegalStateException();
		}
		
		// Status merken
		this.state = state;
	}
	
	/***************************************************************************
	 * Methoden
	 **************************************************************************/
	/**
	 * Ermittelt ob eine Spieler pleite ist
	 * 
	 * @return true wenn der Spieleer pleite ist
	 */
	public boolean isbusted() {
		if (chips < 0.01){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Zieht einen zu brigenden Betrag vom Konto des Spielers ab.
	 * Wenn der vorhandene Betrag geringer als der zu zahlende ist, wird max.
	 * der vorhandene gezahlt.
	 * 
	 * @param payment Der zu zahlende Betrag
	 * @return Vieviel konnte bezahlt werden
	 */
	public double pay(double payment) {
		// Der gezahlte Betrag
		double paid = 0;
		
		// pruefen ob der Betrag erlaubt ist
		try {
			checkMoneyValue(payment);
		} catch (PlayerIllegalChipsException e) {
			// Kein erlaubter Betrag, dann setzen wir den auf Null
			payment = 0;
		}
		
		// Hat der Spieler noch genug um den vollen Betrag zu zahlen?
		if (chips > payment) {
			// Ja
			paid = payment;
		} else {
			// Nein, dann zahlt der den restlichen Besitz
			paid = chips;
		}
		
		// den gezahlten Betrag vom Konto abziehen
		chips = chips - paid;
		
		// den gezahlten Betrag zurueckmelden
		return paid;
	}
	
	/**
	 * Einen Betrag auf das Konto des Spielers einzahlen
	 * 
	 * @param deposite Der Einzahlungsbetrag
	 * @return Der Kontostand nach der Einzahlung
	 * @throws PlayerIllegalChipsException 
	 */
	public double deposit(double deposite) throws PlayerIllegalChipsException {
		
		// pruefen ob der Betrag im erlauten Bereich liegt
		checkMoneyValue(deposite);
		
		// Wenn die Einzahlung i.O. ist, zum Kontostand addieren
		chips = chips + deposite;
		
		// und den neuen Kontostand zurueck melden
		return chips;
	}
	
	/**
	 * Prueft ob ein Betrag erlaubt ist
	 * 
	 * @param value Der zupruefende Betrag
	 * @throws PlayerIllegalChipsException
	 */
	private void checkMoneyValue(double value) throws PlayerIllegalChipsException {
		// pruefen ob der Betrag kleiner Null ist
		if (value < 0) {
			throw new PlayerIllegalChipsException(Messages.Player_0);
		}
		
		// pruefen ob der Betrag ein Vielfaches von 0.01 ist
		int tmpValue = (int)(value * 100);
		if ((value - (double)tmpValue / 100.0) != 0) {
//			throw new PlayerIllegalChipsException("Die Menge an Chips muss ein Vielfaches von 0,01 sein.");
		}
	}
	
	/**
	 * Fuegt dem Spieler eine Pocketkarte hinzu
	 * 
	 * @param card
	 * @throws PlayerToMuchPocketCards 
	 * @throws PlayerDoubleCardException 
	 */
	public void addPocketCard(Card card) throws PlayerToMuchPocketCards, PlayerDoubleCardException {
		// pruefen ob noch Platz fuer eine Pocketkarte ist
		if (pocketCards.length > 2) {
			throw new PlayerToMuchPocketCards();
		}
		
		for (int i = 0; i< pocketCards.length; i++) {
			if (pocketCards[i] == null) {
				pocketCards[i] = card;
				break;
			}
		}
	}
	
	/**
	 * Gibt eine String zum Spieler zurueck
	 * 
	 * @return Ein String mit dem Namen und dem Stack
	 */
	public String toString() {
		String ret = ""; //$NON-NLS-1$
		
		// der Name
		ret = name;
		
		// den Stack hinzufuegen
		ret = ret + Messages.Player_1 + String.valueOf(chips) + Messages.Player_2;
		
		return ret;
	}
	
	/**
	 * Prueft ob ein zweiter Spieler gleich diesem ist. Dabei reicht es wenn die Namen
	 * gleich sind.
	 * 
	 * @param player Der zu vergleichende Spieler
	 * @return True wenn beide gleich sind
	 */
	public boolean equal(Player player) {
		if (player.getName() == this.name) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Spieler folded, legt also die Pocketcards ab
	 */
	public void fold() {
		// Karten ablegen
		pocketCards = null;
		
		// Status aendern wenn der Spieler nicht aussetzt
		if (state != SITTINGOUT) {
			state = FOLDED;
		}
	}
	
	/**
	 * Kopiert den Spieler auf einen neuen
	 * 
	 * @return Die Kopie des Spielers
	 */
	public Player copy() {
		// einen neuen Spieler fuer die Kopie
		Player ret = null;
		
		try {
			// Spieler anlegen und Werte uebertragen
			ret = new Player(name, chips, state);
			ret.addPocketCard(pocketCards[0]);
			ret.addPocketCard(pocketCards[1]);
		} catch (PlayerException e) {
			ErrorHandler.handleError(e, Messages.Player_3, false);
		}
		
		return ret;
	}
	
	/**
	 * Tauscht die aktuellen Pocketkarten gegen zwei neue.
	 * Darf eigendlich nur verwendet werden um anonyme Karten gegen die tatsaechliche zu tauschen
	 * 
	 * @param card1 Die erste neue Karte
	 * @param card2 Die zwiete neue Karte
	 * @throws PlayerToMuchPocketCards
	 * @throws PlayerDoubleCardException
	 */
	public void replacePoketCards(Card card1, Card card2) throws PlayerToMuchPocketCards, PlayerDoubleCardException {
		// alte Karten entfernen
		pocketCards[0] = null;
		pocketCards[1] = null;
		
		// neue Karten hinzufuegen
		addPocketCard(card1);
		addPocketCard(card2);
	}
}
