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

package net.sourceforge.rjPokerReplay.cardstuff;

/**
 * @author ralf
 *
 * Klasse um eine Handhistorie von Pokerstars einzulesen und zu schreiben
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;


import net.sourceforge.rjPokerReplay.cardstuffExceptions.ActionException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.ActionIllegalActionException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.ActionIllegalValueException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.CardException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.CardIllegalSuitException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.CardIllegalValueException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalBigblindException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalButtonPositionException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalCardCountException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalGametypeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalPokerroomException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalPotsizeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalRakesizeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalSeatCountException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalSeatException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalSmallblindException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalTabletypeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandMissingNameException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandPositionNotEmptyException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandhistoryException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandhistoryIllegalFormatException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerDoubleCardException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerIllegalChipsException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerIllegalStateException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerMissingException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerMissingNameException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerToMuchPocketCards;
import net.sourceforge.rjPokerReplay.util.ErrorHandler;

public class HandhistoryPokerstars extends Handhistory {
	
	/**
	 * Importiert eine Pokerstars Handhistorie aus einer Datei
	 * 
	 * @param path Der Pfad zur Datei
	 */
	public ArrayList<Hand> importHistory(String path) throws HandhistoryException {
		// Variablen fuer das Einlesen von Datei
		BufferedReader inputFile;
		
		// Eingelesener String
		String input = ""; //$NON-NLS-1$
		
		// Speicher fuer die eingelesenen Haende
		ArrayList<Hand> hands = new ArrayList<Hand>();
		
		// aktuell eingelesene Hand
		Hand hand = null;
		
		// Teil der Datei soll ueberlesen werden
		boolean skip = false;
		
		try {
			// Datei oeffen
			inputFile = new BufferedReader( new FileReader(path) );
			
			// Eingabedatei Zeile fuer Zeile lesen
	        while ( (input = inputFile.readLine()) != null) {
	        	// leere Zeilen ueberlesen
	        	if(input.trim().equals("")) { //$NON-NLS-1$
	        		continue;
	        	}
	        	
	        	// Verarbeitung der einzelnen Zeilen anhand von bestimmten Merkmalen
	        	if (input.startsWith("PokerStars")) { //$NON-NLS-1$
	        		hand = convertHeader(input);
	        		skip = false;
	        	} else if (input.startsWith("Table")) { //$NON-NLS-1$
	        		convertTableinfo(input, hand);
	        	} else if (input.startsWith("Seat") && !skip) { //$NON-NLS-1$
	        		convertSeat(input, hand);
	        	} else if (input.startsWith("Seat")) { //$NON-NLS-1$
	        		// Nix zu tun, nur Texthinweis zum Schowdown
	        	} else if (input.startsWith("***")) { //$NON-NLS-1$
	        		convertSektionInfo(input, hand);
	        	} else if (input.startsWith("Board")) { //$NON-NLS-1$
	        		// Nix zu tun, nur Texthinweis
	        	} else if (input.startsWith("Total pot ")) { //$NON-NLS-1$
	        		convertTotal(input, hand);
	        		hands.add(hand);
	        		skip = true;
	        	} else if (input.startsWith("Dealt")) { //$NON-NLS-1$
	        		convertDealt(input, hand);
	        	} else if (!skip) {
	        		convertAction(input, hand);
	        	} else {
	        		System.out.println(Messages.HandhistoryPokerstars_0 + input);
	        	}      	

	        	// Zeile an Liste der original Zeilen anfuegen
	        	hand.addLineToFileLines(input);
        	}
		}
		catch (IOException e) {
			throw new HandhistoryIllegalFormatException("IOException"); //$NON-NLS-1$
		} catch (HandException e) {
			throw new HandhistoryIllegalFormatException("HandException"); //$NON-NLS-1$
		} catch (NumberFormatException e) {
			throw new HandhistoryIllegalFormatException("NumberFormatException");  //$NON-NLS-1$
		} catch (ParseException e) {
			throw new HandhistoryIllegalFormatException("ParseException"); //$NON-NLS-1$
		} catch (PlayerException e) {
			throw new HandhistoryIllegalFormatException("PlayerException");  //$NON-NLS-1$
		} catch (ActionException e) {
			throw new HandhistoryIllegalFormatException("ActionException"); //$NON-NLS-1$
		} catch (HandhistoryException e) {
			throw new HandhistoryIllegalFormatException("HandhistoryException");  //$NON-NLS-1$
		} catch (CardException e) {
			throw new HandhistoryIllegalFormatException("CardException"); //$NON-NLS-1$
		} 
		return hands;
	}

	/**
	 * Konvertiert die Zeile mit der abschliessenden Uebersicht ein
	 * 
	 * @param input Die Eingabezeile
	 * @param hand Die aktuell eingelesene Hand
	 * @throws NumberFormatException
	 * @throws HandIllegalPotsizeException
	 * @throws HandIllegalRakesizeException
	 */
	private void convertTotal(String input, Hand hand) throws NumberFormatException, HandIllegalPotsizeException, HandIllegalRakesizeException {
		// Potgroesse und Rake uebernehmen
		
		// Einleitung ueberlesen
		StringTokenizer tokenizer = new StringTokenizer(input);
		String text = tokenizer.nextToken();
		text = tokenizer.nextToken();
		
		// Potgroesse
		text = tokenizer.nextToken();
		if (text.startsWith("$")) { //$NON-NLS-1$
			text = text.substring(1, text.length()); 
		}
		hand.setPotExtern(Double.valueOf(text));
		
		// Rake
		do {
			text = tokenizer.nextToken();
		} while (!Messages.HandhistoryPokerstars_1.equals(text));
		
		text = tokenizer.nextToken();
		
		if (text.startsWith("$")) { //$NON-NLS-1$
			text = text.substring(1, text.length());
		}
		if (text.endsWith(".")) { //$NON-NLS-1$
			text = text.substring(0, text.length() - 1);
		}
		hand.setRake(Double.valueOf(text));
	}

	/**
	 * Konvertiert die Zeile in der die Handkarten verteilt werden
	 * 
	 * @param input Die Eingabezeile
	 * @param hand Die aktuelle Hand
	 * @throws PlayerMissingNameException
	 * @throws ActionIllegalActionException
	 * @throws PlayerMissingException
	 * @throws ActionIllegalValueException
	 * @throws CardIllegalValueException
	 * @throws CardIllegalSuitException
	 */
	private void convertDealt(String input, Hand hand) throws PlayerMissingNameException, ActionIllegalActionException, PlayerMissingException, ActionIllegalValueException, CardIllegalValueException, CardIllegalSuitException {
		// Pocket-Karten werden verteilt
		StringTokenizer tokenizer = new StringTokenizer(input);
		String text = tokenizer.nextToken();
		text = tokenizer.nextToken();
			
		// wer bekommt die Karten
		text = tokenizer.nextToken();
		Player player = hand.getPlayerByName(text);
		
		// Spielernamen als vermutlichen Nikname merken
		hand.setNikname(text);
		
		try {
			// erste Karte
			text = tokenizer.nextToken();
			text = text.substring(1, text.length());
			hand.addAction(new Action(Action.DEAL, player, Card.getCardByString(text)));
			
			 // zweite Karte
			text = tokenizer.nextToken();
			text = text.substring(0, text.length() - 1);
			hand.addAction(new Action(Action.DEAL, player, Card.getCardByString(text)));	
		} catch (ActionException e) {
			ErrorHandler.handleError(e, Messages.HandhistoryPokerstars_2, false);
		}
	}

	/**
	 * Fuegt einer Hand eine Aktion hinzu
	 * 
	 * @param input Die Zeile mit der Aktion
	 * @param hand Die Hand fuer die Aktion
	 * @throws PlayerMissingNameException
	 * @throws HandhistoryIllegalFormatException
	 * @throws ActionIllegalActionException
	 * @throws PlayerMissingException
	 * @throws ActionIllegalValueException
	 * @throws CardException 
	 * @throws HandIllegalCardCountException 
	 * @throws PlayerDoubleCardException 
	 * @throws PlayerToMuchPocketCards 
	 * @throws PlayerIllegalStateException 
	 */
	private void convertAction(String input, Hand hand) throws PlayerMissingNameException, HandhistoryIllegalFormatException, ActionIllegalActionException, PlayerMissingException, ActionIllegalValueException, HandIllegalCardCountException, CardException, PlayerToMuchPocketCards, PlayerDoubleCardException, PlayerIllegalStateException {
		StringTokenizer tokenizer = new StringTokenizer(input);

		// Spieler ermitteln
		String text = tokenizer.nextToken();
		String name = "";  //$NON-NLS-1$
		while (!text.equals("calls") && //$NON-NLS-1$
				!text.equals("folds") && //$NON-NLS-1$
				!text.equals("raises") && //$NON-NLS-1$
				!text.equals("posts") && //$NON-NLS-1$
				!text.equals("bets") && //$NON-NLS-1$
				!text.equals("checks") && //$NON-NLS-1$
				!text.equals("is") && //$NON-NLS-1$
				!text.equals("collected") && //$NON-NLS-1$
				!text.equals("doesn't") && //$NON-NLS-1$
				!text.equals("shows") && //$NON-NLS-1$
				!text.equals("mucks") && //$NON-NLS-1$
				!text.equals("joins") && //$NON-NLS-1$
				!text.equals("said,") && //$NON-NLS-1$
				!text.equals("has") && //$NON-NLS-1$
				!text.equals("leaves") && //$NON-NLS-1$
				!text.equals("sits") && //$NON-NLS-1$
				!text.equals("was") && //$NON-NLS-1$
				!text.equals("Uncalled") && //$NON-NLS-1$
				tokenizer.hasMoreTokens()) {
			name = name + " " + text; //$NON-NLS-1$
			text = tokenizer.nextToken();
		}
		if ("Uncalled".equals(text) && //$NON-NLS-1$
				tokenizer.hasMoreTokens()) {
			do {
				text = tokenizer.nextToken();
			} while (!"to".equals(text)); //$NON-NLS-1$
			name = tokenizer.nextToken();
			while (tokenizer.hasMoreTokens()) {
				name = name+ " " + tokenizer.nextToken(); //$NON-NLS-1$
			}
			tokenizer = new StringTokenizer(input);
			text = tokenizer.nextToken();
		}
		name = name.trim();
		
		if ("is".equals(text)) { //$NON-NLS-1$
			text = tokenizer.nextToken();
			if (!"disconnected".equals(text) &&  //$NON-NLS-1$
					!"connected".equals(text) && //$NON-NLS-1$
					!"capped".equals(text)) { //$NON-NLS-1$
				text = text + tokenizer.nextToken();
			}
		}

		if (name.endsWith(":")) { //$NON-NLS-1$
			name = name.substring(0, name.length() - 1);
		}
		Player player = null;
		if (name != null && !"".equals(name)) { //$NON-NLS-1$
			player = hand.getPlayerByName(name);
		}
		
		// macht was
		int action = 0;
		if ("calls".equals(text)) { //$NON-NLS-1$
			action = Action.CALL;
		} else if ("folds".equals(text)) { //$NON-NLS-1$
			action = Action.FOLD;
		} else if ("raises".equals(text)) { //$NON-NLS-1$
			action = Action.RAISE;
			// den Wert um wieviel erhoeht wird ueberlesen
			text = tokenizer.nextToken();
			// das Wort "to" ueberlesen
			text = tokenizer.nextToken();
		} else if ("posts".equals(text)) { //$NON-NLS-1$
			// Art des Blinds ermitteln
			String blindtype = tokenizer.nextToken();
			// nachtes Wort ist entweder "blind" oder "&" oder "ante" 
			text = tokenizer.nextToken();
			if ("small".equals(blindtype) &&  //$NON-NLS-1$
					"&".equals(text)) { //$NON-NLS-1$
				action = Action.SMALL_AND_BIGBLIND;
				text = tokenizer.nextToken();
				text = tokenizer.nextToken();
			} else  if ("small".equals(blindtype)) { //$NON-NLS-1$
				action = Action.SMALLBLIND;
			} else if ("big".equals(blindtype)) { //$NON-NLS-1$
				action = Action.BIGBLIND;
			} else if ("the".equals(blindtype) && //$NON-NLS-1$ 
					"ante".equals(text)) { //$NON-NLS-1$
				action = Action.ANTE;
			} else {
				throw new HandhistoryIllegalFormatException();
			}
	    } else if ("bets".equals(text)) { //$NON-NLS-1$
			action = Action.BET;
		} else if ("collected".equals(text)) { //$NON-NLS-1$
			action = Action.COLLECT;
		} else if ("doesn't".equals(text)) { //$NON-NLS-1$
			// Nix zu tun, einer tut einfach was nicht
		} else if ("checks".equals(text)) { //$NON-NLS-1$
			action = Action.CHECK;
		} else if ("shows".equals(text)) { //$NON-NLS-1$
			// Aktion auf Null jetzen, da direkt hinzugefuegt
			action = 0;
			String karten = ""; //$NON-NLS-1$
			int pos = input.length() - 1;
			while (pos >= 0 && !"]".equals(input.substring(pos, pos + 1))) { //$NON-NLS-1$
				pos--;
			}
			pos--;
			while (pos >= 0 && !"[".equals(input.substring(pos, pos + 1))) { //$NON-NLS-1$
				karten = input.substring(pos, pos + 1) + karten;
				pos--;
			}
			
			// Karte(n) ermitteln und als Aktion hinzufuegne
			StringTokenizer tokenizer2 = new StringTokenizer(karten);
			while (tokenizer2.hasMoreTokens()) {
				String karte = tokenizer2.nextToken();	
				try {
					hand.addAction(new Action(Action.SHOW, player, Card.getCardByString(karte)));
				} catch (ActionException e) {
					ErrorHandler.handleError(e, Messages.HandhistoryPokerstars_3, false);
				}
			}
		} else if ("sittingout".equals(text) || //$NON-NLS-1$ 
				"sits".equals(text)) { //$NON-NLS-1$
			if (player != null) {
				// nur wenn der Spieler bereits am Tisch sitzt, muss wir ihn aussetzen lassen
				hand.getPlayerByName(name).setState(Player.SITTINGOUT);
			}
		} else if ("mucks".equals(text)) { //$NON-NLS-1$
			action = Action.MUCK;
		} else if ("has".equals(text)) { //$NON-NLS-1$
			text = text + tokenizer.nextToken();
			if ("hasreturned".equals(text) && !(player == null)) { //$NON-NLS-1$
				hand.getPlayerByName(name).setState(Player.AKTIV);
			}
		} else if ("said,".equals(text)) {  //$NON-NLS-1$
			action = Action.SAY;
		} else if ("joins".equals(text)) {  //$NON-NLS-1$
			// Spieler setzt sich an den Tisch, wird erst mit der naechsten Hand interessant
		} else if (input.endsWith("will be allowed to play after the button")) {  //$NON-NLS-1$
			// Neuer Spieler darf erst nach dem Button spielen, also nix fuer diese Hand
		} else if (input.endsWith("has timed out")) {  //$NON-NLS-1$
			// Zeitueberschreitung fuer Spieler, danach kommt Meldung ueber Fold, deshalb ist die hier ohne Bedeutung
		} else if (input.endsWith("has timed out while being disconnected")) {  //$NON-NLS-1$
			// Zeitueberschreitung fuer Spieler, danach kommt Meldung ueber Fold, deshalb ist die hier ohne Bedeutung
		} else if (input.endsWith("is connected ")) {  //$NON-NLS-1$
			// Spieler hat die Verbindung wieder aufgenommen, reine Info-Meldung
		} else if (input.endsWith("is disconnected ")) {  //$NON-NLS-1$
			// Spieler hat die Verbindung verlohren, reine Info-Meldung
		} else if (input.endsWith("leaves the table")) {  //$NON-NLS-1$
			// Spieler verlaesst den Tisch
		} else if (input.endsWith("was removed from the table for failing to post")) {  //$NON-NLS-1$
			// Spieler wurde vom Tisch entfernt, da er nicht gezahlt hat
		} else if (input.endsWith("is capped")) {  //$NON-NLS-1$
			// Spieler hat beim Limit Holdem die max. Anzahl an Erhoehungen erreicht 
		} else if ("Uncalled".equals(text)) { //$NON-NLS-1$
			// Spieler bekommt einen nicht gecallten Einsatz zurueck
			action = Action.UNCALLED_BET;
			text = tokenizer.nextToken();
		} else {
			System.out.println(Messages.HandhistoryPokerstars_4 + input);
		}
		
		// bei einigen Aktionen brauchen wir den Wert
		Object value = null;
		if (action == Action.BET ||
			  action == Action.CALL ||
			  action == Action.RAISE ||
			  action == Action.SMALLBLIND ||
			  action == Action.BIGBLIND ||
			  action == Action.COLLECT ||
			  action == Action.SMALL_AND_BIGBLIND ||
			  action == Action.ANTE ||
			  action == Action.UNCALLED_BET) {
			text = tokenizer.nextToken();
			if (text.startsWith("$")) { //$NON-NLS-1$
				text = text.substring(1, text.length());
			}
			if (text.startsWith("(")) {  //$NON-NLS-1$
				text = text.substring(1, text.length());
				text = text.substring(0, text.length() - 1);
			}
			value = Double.valueOf(text);
		} else if (action == Action.SAY) {
			value = input.substring(name.length() + 8, input.length()- 1).trim();

		}
		
		// wenn die Aktion gleich Says und kein Spieler gefunden, dann hat wohl ein Zusauer was gesagt.
		// Dann merken wir uns die Aktion nicht.
		if (action == Action.SAY && player == null) {
			action = 0;
		}
		
		// und Aktion uebernehmen
		if (action != 0) {
			try {
				hand.addAction(new Action(action, player, value));
			} catch (ActionException e) {
				ErrorHandler.handleError(e, Messages.HandhistoryPokerstars_5, false);
			}
		}
		
		// wenn der Spieler den Ante bezahlt hat, den zum Tisch
		// zusaetzlich setzen
		if (action == Action.ANTE) {
			hand.setAnte(((Double)value).doubleValue());
		}
	}

	/**
	 * Konvertiert die Zeile mit den Boardkarten
	 * 
	 * @param input Die Eingabezeile
	 * @param hand Die Hand zu der das Board gehoert
	 * @throws HandIllegalCardCountException
	 * @throws CardException
	 */
	private void addBoard(String input, Hand hand) throws HandIllegalCardCountException, CardException {
		String karten = ""; //$NON-NLS-1$
		int pos = input.length() - 2;
		while (pos >= 0 && !"[".equals(input.substring(pos, pos + 1))) { //$NON-NLS-1$
			karten = input.substring(pos, pos + 1) + karten;
			pos--;
		}
		
		// Karte(n) ermitteln und zum Board hinzufuegen
		StringTokenizer tokenizer = new StringTokenizer(karten);
		while (tokenizer.hasMoreTokens()) {
			String text = tokenizer.nextToken();	
			Card card = Card.getCardByString(text);
			hand.addBoardCard(card);
		}
	}

	/**
	 * Liest die Spieler mit Sitzposition und Stack ein
	 * 
	 * @param input Die Eingabezeile
	 * @param hand Die Hand die erweitert werden soll
	 * @throws NumberFormatException
	 * @throws PlayerMissingException
	 * @throws HandPositionNotEmptyException
	 * @throws HandIllegalSeatException
	 * @throws PlayerMissingNameException
	 * @throws PlayerIllegalChipsException
	 * @throws PlayerIllegalStateException 
	 */
	private void convertSeat(String input, Hand hand) throws NumberFormatException, PlayerMissingException, HandPositionNotEmptyException, HandIllegalSeatException, PlayerMissingNameException, PlayerIllegalChipsException, PlayerIllegalStateException {
		// Spieler mit Sitzposition und Stack
		StringTokenizer tokenizer = new StringTokenizer(input);
		String text = tokenizer.nextToken();

		text = tokenizer.nextToken();
		int seat = Integer.valueOf(text.substring(0, text.length() - 1));
		
		// Name des Spielers
		String name = ""; //$NON-NLS-1$
		text = tokenizer.nextToken();
		while (!text.startsWith("(")) { //$NON-NLS-1$
			name = name + " " + text; //$NON-NLS-1$
			text = tokenizer.nextToken();
		}
		name = name.substring(1, name.length());
		
		// und der Chipstack
		String stack = text;
		while ("(".equals(stack.substring(0, 1)) || //$NON-NLS-1$
				"$".equals(stack.substring(0, 1))) { //$NON-NLS-1$
			stack = stack.substring(1, stack.length());
		}
		
		// pruefen ob Spieler aussetzt
		if (tokenizer.hasMoreTokens()) {
			text = tokenizer.nextToken();
		}
		if (tokenizer.hasMoreTokens()) {
			text = tokenizer.nextToken();
		}
		if (tokenizer.hasMoreTokens()) {
			text = tokenizer.nextToken();
		}
		int state = Player.AKTIV;
		if ("is".equals(text)) { //$NON-NLS-1$
			text = text + tokenizer.nextToken();
			text = text + tokenizer.nextToken();
			if ("issittingout".equals(text)) { //$NON-NLS-1$
				state = Player.SITTINGOUT;
			} else {
				System.out.println(Messages.HandhistoryPokerstars_6 + input); 
			}
		}
		
		// Spieler zur Hand hinzufuegen
		hand.addPlayer(new Player(name, Double.valueOf(stack), state), seat);
	}

	/**
	 * Importiert die Zeile mit den Tischinformationen
	 * 
	 * @param input Die eingelesene Zeile
	 * @param hand Die Hand die erweitert werden soll
	 * @throws NumberFormatException
	 * @throws HandIllegalSeatCountException
	 * @throws HandIllegalButtonPositionException
	 * @throws HandIllegalTabletypeException 
	 */
	private void convertTableinfo(String input, Hand hand) throws NumberFormatException, HandIllegalSeatCountException, HandIllegalButtonPositionException, HandIllegalTabletypeException {
		// Tischname
		StringTokenizer tokenizer = new StringTokenizer(input);
		String text = tokenizer.nextToken();
		text = tokenizer.nextToken();
		
		// wenn der Tischname aus mehr als einem Wort besteht
		while (!text.endsWith("'")) { //$NON-NLS-1$
			text = text + " " + tokenizer.nextToken(); //$NON-NLS-1$
		}
		
		// Anfuehrungszeichen entfernen
		if (text.startsWith("'")) { //$NON-NLS-1$
			text = text.substring(1, text.length() - 1);
		}
		hand.setTitel(text);
		
		// Anzahl Sitzplaetze
		text = tokenizer.nextToken();
		// Trenner zwischen nach Anzahl
		int stop = text.indexOf('-'); //$NON-NLS-1$
		text = text.substring(0, stop);
		hand.setCountSeats(Integer.valueOf(text));


		// Wenn ein Spielgeldtisch, dann kommt das jetzt
		text = tokenizer.nextToken();
		if ("(Play".equals(text)) { //$NON-NLS-1$
			text = tokenizer.nextToken();
			hand.setTabletype(Hand.PLAYMONEY);
			text = tokenizer.nextToken();

		}
		
		// Position des Bigblinds
		text = tokenizer.nextToken();
		text = text.substring(1, text.length());
		hand.setButtonPos(Integer.valueOf(text));
	}

	/**
	 * Wandelt die die erste Zeile in die passenden Hand-Daten um
	 * 
	 * @param input Die Kopfzeile 
	 * @return Eine neue Hand
	 * @throws HandIllegalPokerroomException
	 * @throws HandMissingNameException
	 * @throws HandIllegalGametypeException
	 * @throws NumberFormatException
	 * @throws HandIllegalBigblindException
	 * @throws HandIllegalSmallblindException
	 * @throws ParseException
	 * @throws HandhistoryIllegalFormatException
	 * @throws HandIllegalTabletypeException 
	 */
	private Hand convertHeader(String input) throws HandIllegalPokerroomException, HandMissingNameException, HandIllegalGametypeException, NumberFormatException, HandIllegalBigblindException, HandIllegalSmallblindException, ParseException, HandhistoryIllegalFormatException, HandIllegalTabletypeException {
		// Kopfzeile
		StringTokenizer tokenizer = new StringTokenizer(input);
		String text = tokenizer.nextToken();
		Hand hand = new Hand();
		
		// Pokerraum
		if ("PokerStars".equals(text)) { //$NON-NLS-1$
			hand.setPokerroom(Hand.POKERSTARS);
		} else {
			// Bei einer Pokerstarshandhistory sollte eigendlich nur Pokerstars drinstehen 
			throw new HandIllegalPokerroomException();
		}
		
		// Spielnummer
		if (!"Game".equals(tokenizer.nextToken())) { //$NON-NLS-1$
			throw new HandhistoryIllegalFormatException();
		}
		text = tokenizer.nextToken();
		if (text.endsWith(":")) { //$NON-NLS-1$
			text = text.substring(0, text.length() - 1);
		}
		hand.setGame(text);
		
		// Bei einem Turnier kommt jetzt der Hinweis
		text = tokenizer.nextToken();
		if ("Tournament".equals(text)) { //$NON-NLS-1$
			// ja es ist ein Turnier
			hand.setTabletype(Hand.TOURNAMENT);
		
			// Tuniernummer und -typ ueberlesen
			text = tokenizer.nextToken();
			text = tokenizer.nextToken();
			text = tokenizer.nextToken();
		}
		
		// Spieltyp aus Eingabe lesen
		String type = ""; //$NON-NLS-1$
		while (!text.startsWith("(") && //$NON-NLS-1$
				!"-".equals(text)) { //$NON-NLS-1$
			type = type + text;
			text = tokenizer.nextToken();
		}
		
		// und geben die bekannten Texte pruefen
		if ("Hold'emNoLimit".equals(type)) { //$NON-NLS-1$
			hand.setGametype(Hand.NO_LIMIT);
		} else if ("Hold'emPotLimit".equals(type)) { //$NON-NLS-1$
			hand.setGametype(Hand.POT_LIMIT);
		} else if ("Hold'emLimit".equals(type)) { //$NON-NLS-1$
			hand.setGametype(Hand.LIMIT);
		} else {
			// unbekannter Typ
			throw new HandIllegalGametypeException();
		}
	
		// Blinds
		if ("-".equals(text)) { //$NON-NLS-1$
			text = tokenizer.nextToken();
			text = tokenizer.nextToken();
			text = tokenizer.nextToken();
		}
		// Klammern entfernen
		text = text.substring(1, text.length() - 1);
		
		// Wenn Echtgeld, dann steht ein $-Zeichen vor dem Wert
		if (text.startsWith("$")) { //$NON-NLS-1$
			text = text.substring(1, text.length());
			
			// und Tischtyp setzen
			hand.setTabletype(Hand.REALMONEY);
		}
		
		// Trenner zwischen Small- und Bigblind
		int stop = text.indexOf('/'); //$NON-NLS-1$
		String small = text.substring(0, stop);
		
		// Trenner entfernen
		text = text.substring(small.length() + 1);
		
		// Auch beim Bigblind kann ein $-Zeichen vor dem Wert stehen
		if (text.startsWith("$")) { //$NON-NLS-1$
			text = text.substring(1, text.length());
		}
		String big = text;
		
		// Blinds uebernehmen
		hand.setBigblind(Double.valueOf(big));	
		hand.setSmallblind(Double.valueOf(small));

		// Datum und Uhrzeit
		text = tokenizer.nextToken();
		text = tokenizer.nextToken();
		text = text + tokenizer.nextToken();
		text = text + tokenizer.nextToken();
		
		// Festlegung des Formats:
		SimpleDateFormat df = new SimpleDateFormat( "yyyy/MM/dd-HH:mm:ss" ); //$NON-NLS-1$
		Date date = new Date();
		
		// Zeitzone
		String tz = tokenizer.nextToken();
		tz = tz.substring(1, tz.length() - 1);
		
		if ("ET".equals(tz)) { //$NON-NLS-1$
			df.setTimeZone(TimeZone.getTimeZone("Etc/GMT+6") ); //$NON-NLS-1$
		}
		
		// Datum gem. der Zeitzone zuweisen
		date = df.parse(text);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		hand.setDate(cal);
		
		return hand;
	}

	/**
	 * Wandelt die Zeile um die zwischen den Spielabschnitten steht
	 * 
	 * @param input Die Eingabezeile
	 * @param hand Die aktuelle Hand
	 */
	private void convertSektionInfo(String input, Hand hand) {
		// um welchen Spielabschnitt handelt es sich?
		StringTokenizer tokenizer = new StringTokenizer(input);
		String text = tokenizer.nextToken();
		text = tokenizer.nextToken();
		
		int action = 0;
		try {
			if ("FLOP".equals(text)) { //$NON-NLS-1$
				action = Action.FLOP;
				addBoard(input, hand);
			} else if ("TURN".equals(text)) { //$NON-NLS-1$
				action = Action.TURN;
				addBoard(input, hand);
			} else if ("RIVER".equals(text)) { //$NON-NLS-1$
				action = Action.RIVER;
				addBoard(input, hand);
			} else if ("SHOW".equals(text)) { //$NON-NLS-1$
				action = Action.SHOWDOWN;
			} else if ("HOLE".equals(text)) { //$NON-NLS-1$
				// Hat fuer uns keine Bedeutung, einfach ueberlesen
			} else if ("SUMMARY".equals(text)) { //$NON-NLS-1$
				// Hat fuer uns keine Bedeutung, einfach ueberlesen
			} else {
				System.out.println(Messages.HandhistoryPokerstars_7 + input);
			}
		} catch (HandIllegalCardCountException e) {
			ErrorHandler.handleError(e, Messages.HandhistoryPokerstars_8, false);
		} catch (CardException e) {
			ErrorHandler.handleError(e, Messages.HandhistoryPokerstars_9, false);
		}
		
		if (action != 0) {
			try {
				hand.addAction(new Action(action, null, null));
			} catch (ActionException e) {
				ErrorHandler.handleError(e, Messages.HandhistoryPokerstars_10, false);
			}
		}
	}
}