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
package io.gitlab.lipor.cardstuff;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import io.gitlab.lipor.ApplicationWorkbenchAdvisor;
import io.gitlab.lipor.Constants;
import io.gitlab.lipor.cardstuffExceptions.CardIllegalSuitException;
import io.gitlab.lipor.cardstuffExceptions.CardIllegalValueException;
import io.gitlab.lipor.views.ViewTable;



public class Card {

	/***************************************************************************
	 * Konstanten
	 **************************************************************************/
	
	/*
	 * Konstante fuer die Farbe Karo
	 */
	public static final char DIAMOND = 'D';
	/**
	 * Konstante fuer die Farbe Pik
	 */
	public static final char SPADE = 'S';
	/**
	 * Konstante fuer die Farbe Kreuz
	 */
	public static final char CLUB = 'C';
	/**
	 * Konstante fuer die Farbe Herz
	 */
	public static final char HEART = 'H';
	/**
	 * Konstante fuer eine Rueckseite
	 */
	public static final char BACK = 'B';
	/**
	 * Konstante fuer die Karte 2
	 */
	public static final int TWO = 2;
	/**
	 * Konstante fuer die Karte 3
	 */
	public static final int THREE = 3;
	/**
	 * Konstante fuer die Karte 4
	 */
	public static final int FOUR = 4;
	/**
	 * Konstante fuer die Karte 5
	 */
	public static final int FIVE = 5;
	/**
	 * Konstante fuer die Karte 6
	 */
	public static final int SIX = 6;
	/**
	 * Konstante fuer die Karte 7
	 */
	public static final int SEVEN = 7;
	/**
	 * Konstante fuer die Karte 8
	 */
	public static final int EIGHT = 8;
	/**
	 * Konstante fuer die Karte 9
	 */
	public static final int NINE = 9;
	/**
	 * Konstante fuer die Karte 10
	 */
	public static final int TEN = 10;
	/**
	 * Konstante fuer die Karte Bube (Jack)
	 */
	public static final int JACK = 11;
	/**
	 * Konstante fuer die Karte Dame (Queen)
	 */
	public static final int QUEEN = 12;
	/**
	 * Konstante fuer die Karte König (King)
	 */
	public static final int KING = 13;
	/**
	 * Konstante fuer die Karte Ass
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
		// Wert pruefen
		checkValue(value);

		// Farbe pruefen
		checkSuit(suit);

		// Alles im gruenen Bereich, dann speichern
		this.value = value;
		this.suit = suit;

	}

	/***************************************************************************
	 * Getter
	 **************************************************************************/
	/**
	 * Rueckgabe der Eigenschaft <tt>suit</tt> Farbe
	 * 
	 * @return Die Farbe der Karte.
	 */
	public char getSuit() {
		return suit;
	}

	/**
	 * Rueckgabe der Eigenschaft <tt>value</tt> Wert
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
	 * Prueft ob ein Kartenwert erlaubt ist
	 * 
	 * @param value
	 *            Der zu pruefende Kartenwert
	 */
	public static void checkValue(int value) throws CardIllegalValueException {
		if (value < TWO || value > ACE) {
			throw new CardIllegalValueException(String.valueOf(value)
					+ Messages.Card_0);
		}
	}

	/**
	 * Gibt den Wert der Karte als Text aus
	 */
	public String toString() {
		String ret = ""; //$NON-NLS-1$

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
			ret = Messages.Card_1;
			break;
		}

		// Farbe der Karte hinzufuegen
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
			ret = ret + Messages.Card_2;
			break;
		}

		return ret;
	}

	/**
	 * Prueft ob die Kartenfarbe erlaubt ist
	 * 
	 * @param suit
	 *            Die zu pruefende Farbe
	 * @throws CardIllegalSuitException
	 */
	public static void checkSuit(char suit) throws CardIllegalSuitException {
		if (suit != CLUB && suit != SPADE && suit != DIAMOND && suit != HEART && suit != BACK) {
			throw new CardIllegalSuitException(suit
					+ Messages.Card_3);
		}
	}

	/**
	 * Gibt die der Karte entsprechende Grafik zurueck
	 * 
	 * @return Die zur Karte gehörende Gafrik
	 * @throws CardIllegalValueException
	 * @throws CardIllegalSuitException
	 */
	public Image getImage() throws CardIllegalValueException,
			CardIllegalSuitException {
		String ret = ""; //$NON-NLS-1$
		
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
	 * Prueft ob zwei Karten gleich sind, also gleiche Farbe und Wert haben
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
		
		if (name1.equals("2")) { //$NON-NLS-1$
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

	/**
	 * Gibt ein Bild mit einer Kartenrueckseite zurueck
	 * 
	 * @return Die Kartenrueckseite
	 */
	public static Image getImageBack() {
		ImageData imgData = new ImageData(ViewTable.class.getClassLoader().getResourceAsStream("images/back_red_casino.gif")); //$NON-NLS-1$
		Image image = new Image(Display.getCurrent(), imgData);
		return image;
	}
	
	/**
	 * Gibt einen der Kartenfarbe entsprechenden Wert zurueck.
	 * Dabei gilt: Kara (Diamond) = 0
	 *             Herz (Heart) = 1
	 *             Kreuz (Cross) = 2
	 *             Pik (Spade) = 3
	 *             
	 * @return Der der Farbe entsprechende Wert
	 */
	public int getSuitValue() {
		int ret;
		
		// Wert zur Farbe ermitteln
		switch (suit) {
		case DIAMOND:
			ret = 0;
			break;
		case HEART:
			ret = 1;
			break;
		case CLUB:
			ret = 2;
			break;
		case SPADE:
			ret = 3;
			break;
		default:
			ret = -1;
			break;
		}
		
		// und zurueckgeben
		return ret;
	}
	
	/**
	 * Gibt den Wert der Karte einem von 0 bis 12 entsprechende Wert zurueck
	 * Dabei gilt: 2 = 0
	 *             3 = 1
	 *             4 = 2 usw. bis
	 *             J = 9
	 *             Q = 10
	 *             K = 11
	 *             A = 12
	 *              
	 * @return Einen int-Wert entsprechende des Kartenwertes
	 */
	public int getValueValue() {
		int ret =0 ;
		
		// Dem internen Wert einen externen zurordnen
		switch (value) {
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			ret = value - 2;
			break;
		case 11:
			ret = 9;
			break;
		case 12:
			ret = 10;
			break;
		case 13:
			ret = 11;
			break;
		case 14:
			ret = 12;
			break;
		default:
			ret = -1;
			break;
		}
		
		// und diesen zurueckmelden
		return ret;
	}
}
