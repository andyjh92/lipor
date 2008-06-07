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

import language.Messages;
import rjPokerReplay.util.ErrorHandler;
import cardstuffExceptions.CardException;
import cardstuffExceptions.PlayerMissingException;

public class RatedHand {
	
	/*
	 * Konstanten fuer den Wert einer Hand
	 */
	public static final int HIGH_CARD = 1;
	public static final int PAIR = 2;
	public static final int TWO_PAIR = 3;
	public static final int THREE_OF_A_KIND = 4;
	public static final int STRAIGHT = 5;
	public static final int FLUSH = 6;
	public static final int FULL_HOUSE = 7;
	public static final int FOUR_OF_A_KIND = 8;
	public static final int STRAIGHT_FLUSH = 9;
	public static final int ROYAL_FLUSH = 10;

	// zwei Array zur Bewertung der Karten
	private int suits[] = new int[4];
	private int value[] = new int[14];
	
	// Wert der Hand
	private int handValue = 0;
	
	// Anzahl der Karten auf der Hand und im Board
	private int cardCount = 0;
	
	// Der Kicker
	private Card kicker;
	
	// Die beste aus Poket- und Boardkarten moegliche Hand
	private Card besteHand[] = new Card[5];
	
	// Merker fuer alle Karten
	private Card alleKarten[] = new Card[7];
	
	/**
	 * Konstruktor
	 * 
	 * @param player Der Spieler dessen Hand bewertet wird
	 * @param board Die Boardkarten
	 * @param pot Der aktuelle Pot
	 * @throws PlayerMissingException
	 * @throws IllegalArgumentException
	 */
	public RatedHand(Player player, Card[] board, double pot) throws PlayerMissingException, IllegalArgumentException {
		// Pruefen ob ein Spieler uebergeben wurde
		if (player == null) {
			throw new PlayerMissingException(Messages.RatedHand_0);
		}
		
		// pruefen ob Boardkarten uebergeben wurden
		if (board == null) {
			throw new IllegalArgumentException(Messages.RatedHand_1);
		}
		
		// Poketkarten zur Bewertung hinzufügen
		Card karten[] = player.getPocketCards();
		for (int i = 0; i < karten.length; i++) {
			int wert = -1;
			int farbe = -1;
			if (karten[i] != null) {
				// Farbe und Wert ermitteln
				farbe = karten[i].getSuitValue();
				wert = karten[i].getValueValue();
				
				// und Karte an Merker anhaengen
				alleKarten[cardCount] = karten[i];
			}
			if (wert > -1 && farbe > -1) {
				suits[farbe]++;
				value[wert + 1]++;
				
				// Anzahl der Karten hochzaehlen
				cardCount++;
				
				// Wenn die Karte ein Ass zusaetzlich als 1 vermerken, fuer Strassen
				if (wert == 12) {
					value[0]++;
				}
			}
		}
		
		// Boardkarten zur Bewertung hinzufügen
		for (int i = 0; i < board.length; i++) {
			int wert = -1;
			int farbe = -1;
			if (board[i] != null) {
				// Farbe und Wert ermitteln
				farbe = board[i].getSuitValue();
				wert = board[i].getValueValue();
				
				// und Karte an Merker anhaengen
				alleKarten[cardCount] = board[i];
			}
			if (wert > -1 && farbe > -1) {
				value[wert + 1]++;
				suits[farbe]++;
				
				// Anzahl der Karten hochzaehlen
				cardCount++;
				
				// Wenn die Karte ein Ass zusaetzlich als 1 vermerken, fuer Strassen
				if (wert == 12) {
					value[0]++;
				}
			}
		}
		
		// Ermitteln was der Spieler bereits haelt wenn er min. 2 Karten hat
		if (cardCount > 1) {
			ermittelWasAufHand();
		}
		
		// Ermittelt die entscheidene Karte
		if (handValue > 0) {
			ermittelEntscheideKarte();
		}
	}

	/**
	 * Ermittelt welches Blatt sich aus Poket- und Boardkarten bilden laesst
	 */
	private void ermittelWasAufHand() {
		// Farbe von Flush
		char suit = ' ';
		int flashSuit = -1;
		
		// hoechste Karte einer Strasse
		int straightEnd = -1;
		
		// Karten des Vierlings
		int vierlingKarte = -1;
		
		// Karten des Drillings
		int drillingKarte = -1;
		
		// hoechste Karte die ein Paar bildet
		int highPair = -1;
		
		// niedrigere Karte einens Twopair
		int lowPair = -1;
		
		// auf Flush pruefen
		boolean flush = false;
		for (int i = 0; i < 4; i++) {
			if (suits[i] >= 5) {
				flush = true;
				flashSuit = i;
			}
		}
		
		// Wenn Flush, dann Zahl in Farbencode wandeln
		switch (flashSuit) {
		case 0:
			suit = Card.DIAMOND; 
			break;
		case 1:
			suit = Card.HEART; 
			break;
		case 2:
			suit = Card.CLUB; 
			break;
		case 3:
			suit = Card.SPADE; 
			break;
		}
		
		// auf Straße pruefen
		boolean  straight = false;
		int zaehler = 0;
		for (int i = 0; i < 14; i++) {
			if (value[i] != 0) {
				zaehler++;
				straightEnd = i + 1;
			} else if (zaehler >= 5) {
				break;
			} else {
				zaehler = 0;
				straightEnd = -1;
			}
		}
		if (zaehler >= 5) {
			straight = true;
		}
		
		// Straightflus ??
		boolean straightflush = false;
		if (straight && flush) {
			straightflush = true;
		}
		
		// Royalflush ??
		boolean royalflush = false;
		if (straightflush) {
			zaehler = 0;
			for (int i = 13; i > 8; i--) {
				if (value[i] != 0) {
					zaehler++;
				}
			}
			if (zaehler == 5) {
				royalflush = true;
			}
		}
		
		// Poker, Set, Twopair, Pair ??
		boolean poker = false;
		boolean set = false;
		boolean twopair = false;
		boolean pair = false;
		zaehler = 0;
		for (int i = 13; i > 0; i--) {
			switch (value[i]) {
			case 4:
				poker = true;
				vierlingKarte = i + 1;
				break;
			case 3:
				set = true;
				drillingKarte = i + 1;
				break;
			case 2:
				pair = true;
				zaehler++;
				if (zaehler == 1) {
					lowPair = i + 1;
				} else if (zaehler == 2) {
					highPair = lowPair;
					lowPair = i + 1;
				}
				break;
			default:
				break;
			}
		}
		if (pair && zaehler == 2) {
			twopair = true;
		}
		if (pair && zaehler == 1) {
			highPair = lowPair;
			lowPair = -1;
		}
		
		// Fullhouse
		boolean fullhouse = false;
		if (set && pair) {
			fullhouse = true;
		}
		
		// gefundenen Wert merken
		if (royalflush) {
			handValue = ROYAL_FLUSH;
		} else if (straightflush) {
			handValue = STRAIGHT_FLUSH;
		} else if (poker) {
			handValue = FOUR_OF_A_KIND;
		} else if (fullhouse) {
			handValue = FULL_HOUSE;
		} else if (flush) {
			handValue = FLUSH;
		} else if (straight) {
			handValue = STRAIGHT;
		} else if (set) {
			handValue = THREE_OF_A_KIND;
		} else if (twopair) {
			handValue = TWO_PAIR;
		} else if (pair) {
			handValue = PAIR;
		} else {
			handValue = HIGH_CARD;
		}
		
		// anhand des Handwertes die beste Hand zusammenbauen
		switch (handValue) {
		case ROYAL_FLUSH:
			// ist recht einfach, alle Karten von 10 bis As zur Flush-Farbe
			try {
				besteHand[0] = new Card(Card.TEN, suit);
				besteHand[1] = new Card(Card.JACK, suit);
				besteHand[2] = new Card(Card.QUEEN, suit);
				besteHand[3] = new Card(Card.KING, suit);
				besteHand[4] = new Card(Card.ACE, suit);
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_2, false);
			}
			break;
		case STRAIGHT_FLUSH:
			// auch ganz einfach, ab hoechste Karte der Strasse - 4 von der Flush-Farbe
			try {
				besteHand[0] = new Card(straightEnd - 4, suit);
				besteHand[1] = new Card(straightEnd - 3, suit);
				besteHand[2] = new Card(straightEnd - 2, suit);
				besteHand[3] = new Card(straightEnd - 1, suit);
				besteHand[4] = new Card(straightEnd, suit);
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_3, false);
			}
		break;
		case FOUR_OF_A_KIND:
			// auch ganz einfach, Farbe ist egal da alle und wir wissen welche Karte
			try {
				besteHand[0] = new Card(vierlingKarte, Card.DIAMOND);
				besteHand[1] = new Card(vierlingKarte, Card.HEART);
				besteHand[2] = new Card(vierlingKarte, Card.CLUB);
				besteHand[3] = new Card(vierlingKarte, Card.SPADE);
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_4, false);
			}
			break;
		case FULL_HOUSE:
			// schon etwas schwieriger, erst die Karten zum Paar und dann zum Drilling finden
			try {
				Card zwilling[] = new Card[4];
				Card drilling[] = new Card[4];
				zwilling = findCardsByValue(highPair);
				drilling = findCardsByValue(drillingKarte);
				besteHand[0] = new Card(zwilling[0].getValue(), zwilling[0].getSuit());
				besteHand[1] = new Card(zwilling[1].getValue(), zwilling[1].getSuit());
				besteHand[2] = new Card(drilling[0].getValue(), drilling[0].getSuit());
				besteHand[3] = new Card(drilling[1].getValue(), drilling[1].getSuit());
				besteHand[4] = new Card(drilling[2].getValue(), drilling[2].getSuit());
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_5, false);
			}
			break;
		case FLUSH:
			// schon etwas schwieriger, die Karten sollen auch in der Reihenfolge kommen
			try {
				Card karten[] = new Card[7];
				karten = findCardsBySuit(suit);
				besteHand[0] = new Card(karten[4].getValue(), karten[4].getSuit());
				besteHand[1] = new Card(karten[3].getValue(), karten[3].getSuit());
				besteHand[2] = new Card(karten[2].getValue(), karten[2].getSuit());
				besteHand[3] = new Card(karten[1].getValue(), karten[1].getSuit());
				besteHand[4] = new Card(karten[0].getValue(), karten[0].getSuit());
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_6, false);
			}
			break;
		case STRAIGHT:
			// schon etwas schwieriger, die Karten sollen auch in der Reihenfolge kommen
			try {
				// Karten sortieren
				Card karten[] = new Card[7];
				karten = findStraight(straightEnd);
				for (int i = 0; i < 5; i++) {
					besteHand[i] = new Card(karten[i].getValue(), karten[i].getSuit());
				}
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_7, false);
			}
			break;
		case THREE_OF_A_KIND:
			// wieder ganz einfach
			try {
				Card karten[] = new Card[3];
				karten = findCardsByValue(drillingKarte);
				for (int i = 0; i < 3; i++) {
					besteHand[i] = new Card(karten[i].getValue(), karten[i].getSuit());
				}
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_8, false);
			}
			break;
		case TWO_PAIR:
			// geht auch, erst das niedriegere Paar, dann das hoehere
			try {
				Card karten[] = new Card[4];
				karten = findCardsByValue(lowPair);
				for (int i = 0; i < 2; i++) {
					besteHand[i] = new Card(karten[i].getValue(), karten[i].getSuit());
				}
				karten = findCardsByValue(highPair);
				for (int i = 0; i < 2; i++) {
					besteHand[i + 2] = new Card(karten[i].getValue(), karten[i].getSuit());
				}
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_9, false);
			}
			break;
		case PAIR:
			// geht auch, einfach Karten mit dem passenden Wert suchen
			try {
				Card karten[] = new Card[2];
				karten = findCardsByValue(highPair);
				for (int i = 0; i < 2; i++) {
					besteHand[i] = new Card(karten[i].getValue(), karten[i].getSuit());
				}
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_10, false);
			}
			break;
		case HIGH_CARD:
			// ganz einfach, absteigend sortieren und dann dier erste nehmen
			try {
				sortCardsByValueDescending(alleKarten);
				besteHand[0] = new Card(alleKarten[0].getValue(), alleKarten[0].getSuit());
			} catch (CardException e) {
				ErrorHandler.handleError(e, Messages.RatedHand_11, false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Sucht die passenden Karten anhand der Farbe und gibt sie absteigend
	 * sortiert zurueck
	 * 
	 * @param flashSuit Die gesuchte Farbe
	 * @return Die gefundenen Karten
	 */
	private Card[] findCardsBySuit(char flashSuit) {
		Card ret[] = new Card[7];
		int counter = 0;
		
		// zur Farbe passende Karten finden
		for (int i = 0; i < 7; i++) {
			if (alleKarten[i] != null && alleKarten[i].getSuit() == flashSuit) {
				ret[counter] = alleKarten[i];
				counter++;
			}
		}
		
		// gefundenen Karten absteigend sortieren
		sortCardsByValueDescending(ret);
		
		return ret;
	}

	/**
	 * Sortiert die uebergebenen Karten absteigend nach ihrem Wert
	 * 
	 * @param cards Die zu sortierenden Karten
	 */
	public void sortCardsByValueDescending (Card[] cards) {
		boolean fertig = true;
		Card zw;
		do {
			boolean getauscht = false;
			for (int i = 0; i < 6; i++) {
				if (cards[i] != null && cards[i + 1] != null && cards[i].getValue() < cards[i + 1].getValue()) {
					getauscht = true;
					zw = cards[i];
					cards[i] = cards[i + 1];
					cards[i + 1] = zw;
				}
			}
			if (getauscht) {
				fertig = false;
				getauscht = false;
			} else {
				fertig = true;
			}
		} while (!fertig);
	}

	/**
	 * Sortiert die Karten aufsteigend
	 * 
	 * @param cards Die zu sortierenden Karten
	 */
	public void sortCardsByValueAscending (Card[] cards) {
		// erste Sortierung, absteigend
		sortCardsByValueDescending(cards);
		
		// und jetzt Sortierung drehen
		// letzte Karten finden
		int cardCount = 0;
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] != null) {
				cardCount = i;
			}
		}
		
		// jetzt erste mit letzer Karte tauschen, dann zweite mit vorletzer usw.
		int end = cardCount / 2;
		for (int i = 0; i <= end; i++) {
			Card zw = cards[i];
			cards[i] = cards[cardCount];
			cards[i] = zw;
			cardCount--;
		}
	}
	
	/**
	 * Sucht aus der Liste alle Karten die mit einem bestimmten Wert heraus
	 * 
	 * @param cardValue Der zu suchende Kartenwert
	 * @return Ein Array der gefundenen Karten
	 */
	private Card[] findCardsByValue(int cardValue) {
		Card ret[] = new Card[4];
		int counter = 0;
		for (int i = 0; i < 7; i++) {
			if (alleKarten[i] != null && alleKarten[i].getValue() == cardValue) {
				ret[counter] = alleKarten[i];
				counter++;
			}
		}
		return ret;
	}

	/**
	 * Einen dem Wert der Hand entsprechenden Int-Wert zurueck.
	 * Die Handwerte sind als Konstanten definiert.
	 * 
	 * @return Der Wert der Hand
	 */
	public int getHandValue() {
		return handValue;
	}
	
	/**
	 * Gibt den Wert der Hand als Text aus
	 */
	public String toString(){
		String ret = ""; //$NON-NLS-1$
		
		switch (handValue) {
		case 0:
			ret = Messages.RatedHand_12;
			break;
		case HIGH_CARD:
			ret = Messages.RatedHand_13;
			break;
		case PAIR:
			ret = Messages.RatedHand_14;
			break;
		case TWO_PAIR:
			ret = Messages.RatedHand_15;
			break;
		case THREE_OF_A_KIND:
			ret = Messages.RatedHand_16;
			break;
		case STRAIGHT:
			ret = Messages.RatedHand_17;
			break;
		case FLUSH:
			ret = Messages.RatedHand_18;
			break;
		case FULL_HOUSE:
			ret = Messages.RatedHand_19;
			break;
		case FOUR_OF_A_KIND:
			ret = Messages.RatedHand_20;
			break;
		case STRAIGHT_FLUSH:
			ret = Messages.RatedHand_21;
			break;
		case ROYAL_FLUSH:
			ret = Messages.RatedHand_22;
			break;
		}
		
		if (handValue > 0) {
			Card karten[] = getBestPosibleHand();
			for (int i = 0; i < 5; i++) {
				if (karten[i] != null) {
					ret = ret + " " + karten[i].toString(); //$NON-NLS-1$
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Gibt den Kicker zurueck
	 * 
	 * @return Der Kicker
	 */
	public Card getKicker() {
		return kicker;
	}
	
	/**
	 * Gibt die beste moegliche Kombination aus Poket- und Boardkarten zurueck
	 * 
	 * @return Die beste moegliche Hand
	 */
	public Card[] getBestPosibleHand() {
		return besteHand;
	}
	
	/**
	 * Findet die Strasse und gibt diese zurueck
	 * 
	 * @return Die gefundene Strasse
	 */
	private Card[] findStraight(int end) {
		Card ret[] = new Card[5];
		
		ret[0] = findCardsByValue(end - 4)[0];
		ret[1] = findCardsByValue(end - 3)[0];
		ret[2] = findCardsByValue(end - 2)[0];
		ret[3] = findCardsByValue(end - 1)[0];
		ret[4] = findCardsByValue(end)[0];

		return ret;
	}
	
	/**
	 * Ermittelt die hoechste Poketkarte im Handwert
	 */
	private void ermittelEntscheideKarte() {
		// TODO Auto-generated method stub
		
	}
}
