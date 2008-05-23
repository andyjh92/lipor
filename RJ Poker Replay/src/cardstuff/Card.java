/**
 * Copyright (C) 2008  Ralf Joswig
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package cardstuff;

/**
 * @author Ralf Joswig
 * 
 * Klasse zur Darstellung und Handhabung einer Spielkarte
 * 
 */
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import rjPokerReplay.ApplicationWorkbenchAdvisor;
import rjPokerReplay.Constants;
import rjPokerReplay.views.ViewTable;
import cardstuffExceptions.CardIllegalSuitException;
import cardstuffExceptions.CardIllegalValueException;


public class Card {

	/***************************************************************************
	 * Konstanten
	 **************************************************************************/
	/*
	 * Konstante für die Farbe Karo
	 */
	public static final char DIAMOND = 'D';
	/**
	 * Konstante für die Farbe Pik
	 */
	public static final char SPADE = 'S';
	/**
	 * Konstante für die Farbe Kreuz
	 */
	public static final char CLUB = 'C';
	/**
	 * Konstante für die Farbe Herz
	 */
	public static final char HEART = 'H';
	/**
	 * Konstante für eine Rückseite
	 */
	public static final char BACK = 'B';
	/**
	 * Konstante für die Karte 2
	 */
	public static final int TWO = 2;
	/**
	 * Konstante für die Karte 3
	 */
	public static final int THREE = 3;
	/**
	 * Konstante für die Karte 4
	 */
	public static final int FOUR = 4;
	/**
	 * Konstante für die Karte 5
	 */
	public static final int FIVE = 5;
	/**
	 * Konstante für die Karte 6
	 */
	public static final int SIX = 6;
	/**
	 * Konstante für die Karte 7
	 */
	public static final int SEVEN = 7;
	/**
	 * Konstante für die Karte 8
	 */
	public static final int EIGHT = 8;
	/**
	 * Konstante für die Karte 9
	 */
	public static final int NINE = 9;
	/**
	 * Konstante für die Karte 10
	 */
	public static final int TEN = 10;
	/**
	 * Konstante für die Karte Bube (Jack)
	 */
	public static final int JACK = 11;
	/**
	 * Konstante für die Karte Dame (Queen)
	 */
	public static final int QUEEN = 12;
	/**
	 * Konstante für die Karte König (King)
	 */
	public static final int KING = 13;
	/**
	 * Konstante für die Karte Ass
	 */
	public static final int ACE = 14;

	/***************************************************************************
	 * Instanz Eigenschaften
	 **************************************************************************/
	/**
	 * Farbe der Karte
	 */
	private char suit;
	/**
	 * Wert der Karte
	 */
	private int value;

	/***************************************************************************
	 * Konstruktoren
	 **************************************************************************/
	/**
	 * Konstruktor
	 * 
	 * @param value
	 *            Wet der Karte
	 * @param suit
	 *            Farbe der Karte
	 * @throws CardIllegalValueException
	 * @throws CardIllegalSuitException
	 */
	public Card(int value, char suit) throws CardIllegalValueException,
			CardIllegalSuitException {
		// Wert prüfen
		checkValue(value);

		// Farbe prüfen
		checkSuit(suit);

		// Alles im grünen Bereich, dann speichern
		this.value = value;
		this.suit = suit;

	}

	/***************************************************************************
	 * Getter
	 **************************************************************************/
	/**
	 * Rückgabe der Eigenschaft <tt>suit</tt> Farbe
	 * 
	 * @return Die Farbe der Karte.
	 */
	public char getSuit() {
		return suit;
	}

	/**
	 * Rückgabe der Eigenschaft <tt>value</tt> Wert
	 * 
	 * @return Der Wert der Karte
	 */
	public int getValue() {
		return value;
	}

	/***************************************************************************
	 * Methoden
	 **************************************************************************/
	/**
	 * Prüft ob ein Kartenwert erlaubt ist
	 * 
	 * @param value
	 *            Der zu prüfende Kartenwert
	 */
	public static void checkValue(int value) throws CardIllegalValueException {
		if (value < TWO || value > ACE) {
			throw new CardIllegalValueException(String.valueOf(value)
					+ " ist kein erlaubter Kartenwert!");
		}
	}

	/**
	 * Gibt den Wert der Karte als Text aus
	 */
	public String toString() {
		String ret = "";

		// internen Wert der Karte umwandeln
		switch (value) {
		case 2:
			ret = "2"; //$NON-NLS-1$
			break;
		case 3:
			ret = "3"; //$NON-NLS-1$
			break;
		case 4:
			ret = "4"; //$NON-NLS-1$
			break;
		case 5:
			ret = "5"; //$NON-NLS-1$
			break;
		case 6:
			ret = "6"; //$NON-NLS-1$
			break;
		case 7:
			ret = "7"; //$NON-NLS-1$
			break;
		case 8:
			ret = "8"; //$NON-NLS-1$
			break;
		case 9:
			ret = "9"; //$NON-NLS-1$
			break;
		case 10:
			ret = "T"; //$NON-NLS-1$
			break;
		case 11:
			ret = "J"; //$NON-NLS-1$
			break;
		case 12:
			ret = "Q"; //$NON-NLS-1$
			break;
		case 13:
			ret = "K"; //$NON-NLS-1$
			break;
		case 14:
			ret = "A"; //$NON-NLS-1$
			break;
		default:
			ret = "unbekannter Wert!";
			break;
		}

		// Farbe der Karte hinzufügen
		switch (suit) {
		case 'D':
			ret = ret + "d"; //$NON-NLS-1$
			break;
		case 'H':
			ret = ret + "h"; //$NON-NLS-1$
			break;
		case 'C':
			ret = ret + "c"; //$NON-NLS-1$
			break;
		case 'S':
			ret = ret + "s"; //$NON-NLS-1$
			break;
		default:
			ret = ret + " unbekannte Farbe!";
			break;
		}

		return ret;
	}

	/**
	 * Prüft ob die Kartenfarbe erlaubt ist
	 * 
	 * @param suit
	 *            Die zu prüfende Farbe
	 * @throws CardIllegalSuitException
	 */
	public static void checkSuit(char suit) throws CardIllegalSuitException {
		if (suit != CLUB && suit != SPADE && suit != DIAMOND && suit != HEART && suit != BACK) {
			throw new CardIllegalSuitException(suit
					+ " ist keine definierter Kartenfarbe!");
		}
	}

	/**
	 * Gibt die der Karte entsprechende Grafik zurück
	 * 
	 * @return Die zur Karte gehörende Gafrik
	 * @throws CardIllegalValueException
	 * @throws CardIllegalSuitException
	 */
	public Image getImage() throws CardIllegalValueException,
			CardIllegalSuitException {
		String ret = "";

		// String mit dem Dateinamen erstellen
		// Der Wert
//		switch (value) {
//		case 2:
//			ret = "2_"; //$NON-NLS-1$
//			break;
//		case 3:
//			ret = "3_"; //$NON-NLS-1$
//			break;
//		case 4:
//			ret = "4_"; //$NON-NLS-1$
//			break;
//		case 5:
//			ret = "5_"; //$NON-NLS-1$
//			break;
//		case 6:
//			ret = "6_"; //$NON-NLS-1$
//			break;
//		case 7:
//			ret = "7_"; //$NON-NLS-1$
//			break;
//		case 8:
//			ret = "8_"; //$NON-NLS-1$
//			break;
//		case 9:
//			ret = "9_"; //$NON-NLS-1$
//			break;
//		case 10:
//			ret = "10_"; //$NON-NLS-1$
//			break;
//		case 11:
//			ret = "11_"; //$NON-NLS-1$
//			break;
//		case 12:
//			ret = "12_"; //$NON-NLS-1$
//			break;
//		case 13:
//			ret = "13_"; //$NON-NLS-1$
//			break;
//		case 14:
//			ret = "1_"; //$NON-NLS-1$
//			break;
//		default:
//			throw new CardIllegalValueException();
//		}
//
//		// Farbe der Karte hinzufügen
//		switch (suit) {
//		case 'D':
//			ret = ret + "1.png"; //$NON-NLS-1$
//			break;
//		case 'H':
//			ret = ret + "4.png"; //$NON-NLS-1$
//			break;
//		case 'C':
//			ret = ret + "2.png"; //$NON-NLS-1$
//			break;
//		case 'S':
//			ret = ret + "3.png"; //$NON-NLS-1$
//			break;
//		case 'B':
//			ret = "back_red_casino.gif"; //$NON-NLS-1$
//			break;
//		default:
//			throw new CardIllegalSuitException();
//		}
//
//		// und Dateiendung und Pfad einfügen
//		ret = "images/" + ret; //$NON-NLS-1$
		
		// passende Konstante zum Kartenwert ermitteln
		if (suit == 'B') {
			ret = Constants.IMG_CARD_BACK;
		} else if (suit == 'D' && value == 2) {
			ret = Constants.IMG_CARD_DIAMOND_TWO; 
		} else if (suit == 'D' && value == 3) {
			ret = Constants.IMG_CARD_DIAMOND_THREE; 
		} else if (suit == 'D' && value == 4) {
			ret = Constants.IMG_CARD_DIAMOND_FOUR; 
		} else if (suit == 'D' && value == 5) {
			ret = Constants.IMG_CARD_DIAMOND_FIVE; 
		} else if (suit == 'D' && value == 6) {
			ret = Constants.IMG_CARD_DIAMOND_SIX; 
		} else if (suit == 'D' && value == 7) {
			ret = Constants.IMG_CARD_DIAMOND_SEVEN; 
		} else if (suit == 'D' && value == 8) {
			ret = Constants.IMG_CARD_DIAMOND_EIGHT; 
		} else if (suit == 'D' && value == 9) {
			ret = Constants.IMG_CARD_DIAMOND_NINE; 
		} else if (suit == 'D' && value == 10) {
			ret = Constants.IMG_CARD_DIAMOND_TEN; 
		} else if (suit == 'D' && value == 11) {
			ret = Constants.IMG_CARD_DIAMOND_JACK; 
		} else if (suit == 'D' && value == 12) {
			ret = Constants.IMG_CARD_DIAMOND_QUEEN; 
		} else if (suit == 'D' && value == 13) {
			ret = Constants.IMG_CARD_DIAMOND_KING; 
		} else if (suit == 'D' && value == 14) {
			ret = Constants.IMG_CARD_DIAMOND_ASS; 
		} else if (suit == 'H' && value == 2) {
			ret = Constants.IMG_CARD_HART_TWO; 
		} else if (suit == 'H' && value == 3) {
			ret = Constants.IMG_CARD_HART_THREE; 
		} else if (suit == 'H' && value == 4) {
			ret = Constants.IMG_CARD_HART_FOUR; 
		} else if (suit == 'H' && value == 5) {
			ret = Constants.IMG_CARD_HART_FIVE; 
		} else if (suit == 'H' && value == 6) {
			ret = Constants.IMG_CARD_HART_SIX; 
		} else if (suit == 'H' && value == 7) {
			ret = Constants.IMG_CARD_HART_SEVEN; 
		} else if (suit == 'H' && value == 8) {
			ret = Constants.IMG_CARD_HART_EIGHT; 
		} else if (suit == 'H' && value == 9) {
			ret = Constants.IMG_CARD_HART_NINE; 
		} else if (suit == 'H' && value == 10) {
			ret = Constants.IMG_CARD_HART_TEN; 
		} else if (suit == 'H' && value == 11) {
			ret = Constants.IMG_CARD_HART_JACK; 
		} else if (suit == 'H' && value == 12) {
			ret = Constants.IMG_CARD_HART_QUEEN; 
		} else if (suit == 'H' && value == 13) {
			ret = Constants.IMG_CARD_HART_KING; 
		} else if (suit == 'H' && value == 14) {
			ret = Constants.IMG_CARD_HART_ASS; 
		} else if (suit == 'S' && value == 2) {
			ret = Constants.IMG_CARD_SPADE_TWO; 
		} else if (suit == 'S' && value == 3) {
			ret = Constants.IMG_CARD_SPADE_THREE; 
		} else if (suit == 'S' && value == 4) {
			ret = Constants.IMG_CARD_SPADE_FOUR; 
		} else if (suit == 'S' && value == 5) {
			ret = Constants.IMG_CARD_SPADE_FIVE; 
		} else if (suit == 'S' && value == 6) {
			ret = Constants.IMG_CARD_SPADE_SIX; 
		} else if (suit == 'S' && value == 7) {
			ret = Constants.IMG_CARD_SPADE_SEVEN; 
		} else if (suit == 'S' && value == 8) {
			ret = Constants.IMG_CARD_SPADE_EIGHT; 
		} else if (suit == 'S' && value == 9) {
			ret = Constants.IMG_CARD_SPADE_NINE; 
		} else if (suit == 'S' && value == 10) {
			ret = Constants.IMG_CARD_SPADE_TEN; 
		} else if (suit == 'S' && value == 11) {
			ret = Constants.IMG_CARD_SPADE_JACK; 
		} else if (suit == 'S' && value == 12) {
			ret = Constants.IMG_CARD_SPADE_QUEEN; 
		} else if (suit == 'S' && value == 13) {
			ret = Constants.IMG_CARD_SPADE_KING; 
		} else if (suit == 'S' && value == 14) {
			ret = Constants.IMG_CARD_SPADE_ASS; 
		} else if (suit == 'C' && value == 2) {
			ret = Constants.IMG_CARD_CLUB_TWO; 
		} else if (suit == 'C' && value == 3) {
			ret = Constants.IMG_CARD_CLUB_THREE; 
		} else if (suit == 'C' && value == 4) {
			ret = Constants.IMG_CARD_CLUB_FOUR; 
		} else if (suit == 'C' && value == 5) {
			ret = Constants.IMG_CARD_CLUB_FIVE; 
		} else if (suit == 'C' && value == 6) {
			ret = Constants.IMG_CARD_CLUB_SIX; 
		} else if (suit == 'C' && value == 7) {
			ret = Constants.IMG_CARD_CLUB_SEVEN; 
		} else if (suit == 'C' && value == 8) {
			ret = Constants.IMG_CARD_CLUB_EIGHT; 
		} else if (suit == 'C' && value == 9) {
			ret = Constants.IMG_CARD_CLUB_NINE; 
		} else if (suit == 'C' && value == 10) {
			ret = Constants.IMG_CARD_CLUB_TEN; 
		} else if (suit == 'C' && value == 11) {
			ret = Constants.IMG_CARD_CLUB_JACK; 
		} else if (suit == 'C' && value == 12) {
			ret = Constants.IMG_CARD_CLUB_QUEEN; 
		} else if (suit == 'C' && value == 13) {
			ret = Constants.IMG_CARD_CLUB_KING; 
		} else if (suit == 'C' && value == 14) {
			ret = Constants.IMG_CARD_CLUB_ASS; 
		} else {
			throw new CardIllegalValueException();
		}
		
//		ImageData imgData = new ImageData(View.class.getClassLoader().getResourceAsStream(ret));
		ImageData imgData = ApplicationWorkbenchAdvisor.getImageStore().get(ret).getImageData();
		Image image = new Image(Display.getCurrent(), imgData);
		return image;
	}
	
	/**
	 * Prüft ob zwei Karten gleich sind, also gleiche Farbe und Wert haben
	 * 
	 * @param card Die zu vegleichende Karte
	 * @return true wenn beide Karten gleich sind
	 */
	public boolean equal(Card card) {
		if (card.getSuit() == this.suit && card.getValue() == this.value) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Card getCardByString(String name) throws CardIllegalValueException, CardIllegalSuitException {
		String name1 = name.substring(0, name.length() - 1);
		char name2 = name.substring(name1.length(), name.length()).charAt(0);
		int tmpValue = 0;
		char tmpSuit = ' ';
		
		switch (name2) {
		case 'c':
			tmpSuit = CLUB;
			break;
		case 'h':
			tmpSuit = HEART;
			break;
		case 's':
			tmpSuit = SPADE;
			break;
		case 'd':
			tmpSuit = DIAMOND;
			break;
		}
		
		if (name1.equals("2")) {
			tmpValue = TWO;
		} else if (name1.equals("3")) { //$NON-NLS-1$
			tmpValue = THREE;
		} else if (name1.equals("4")) { //$NON-NLS-1$
			tmpValue = FOUR;
		} else if (name1.equals("5")) { //$NON-NLS-1$
			tmpValue = FIVE;
		} else if (name1.equals("6")) { //$NON-NLS-1$
			tmpValue = SIX;
		} else if (name1.equals("7")) { //$NON-NLS-1$
			tmpValue = SEVEN;
		} else if (name1.equals("8")) { //$NON-NLS-1$
			tmpValue = EIGHT;
		} else if (name1.equals("9")) { //$NON-NLS-1$
			tmpValue = NINE;
		} else if (name1.equals("T")) { //$NON-NLS-1$
			tmpValue = TEN;
		} else if (name1.equals("J")) { //$NON-NLS-1$
			tmpValue = JACK;
		} else if (name1.equals("Q")) { //$NON-NLS-1$
			tmpValue = QUEEN;
		} else if (name1.equals("K")) { //$NON-NLS-1$
			tmpValue = KING;
		} else if (name1.equals("A")) { //$NON-NLS-1$
			tmpValue = ACE;
		} 
		
		return new Card(tmpValue, tmpSuit);
	}
	
	public static Image getImageBack() {
		ImageData imgData = new ImageData(ViewTable.class.getClassLoader().getResourceAsStream("images/back_red_casino.gif"));
		Image image = new Image(Display.getCurrent(), imgData);
		return image;
	}

}
