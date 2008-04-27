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

import rjPokerReplay.ErrorHandler;

import cardstuffExceptions.ActionException;
import cardstuffExceptions.ActionIllegalActionException;
import cardstuffExceptions.ActionIllegalValueException;
import cardstuffExceptions.CardException;
import cardstuffExceptions.CardIllegalSuitException;
import cardstuffExceptions.CardIllegalValueException;
import cardstuffExceptions.HandException;
import cardstuffExceptions.HandIllegalBigblindException;
import cardstuffExceptions.HandIllegalButtonPositionException;
import cardstuffExceptions.HandIllegalCardCountException;
import cardstuffExceptions.HandIllegalGametypeException;
import cardstuffExceptions.HandIllegalPokerroomException;
import cardstuffExceptions.HandIllegalPotsizeException;
import cardstuffExceptions.HandIllegalRakesizeException;
import cardstuffExceptions.HandIllegalSeatCountException;
import cardstuffExceptions.HandIllegalSeatException;
import cardstuffExceptions.HandIllegalSmallblindException;
import cardstuffExceptions.HandIllegalTabletypeException;
import cardstuffExceptions.HandMissingNameException;
import cardstuffExceptions.HandPositionNotEmptyException;
import cardstuffExceptions.HandhistoryException;
import cardstuffExceptions.HandhistoryIllegalFormatException;
import cardstuffExceptions.PlayerDoubleCardException;
import cardstuffExceptions.PlayerException;
import cardstuffExceptions.PlayerIllegalChipsException;
import cardstuffExceptions.PlayerIllegalStateException;
import cardstuffExceptions.PlayerMissingException;
import cardstuffExceptions.PlayerMissingNameException;
import cardstuffExceptions.PlayerToMuchPocketCards;


public class HandhistoryPokerstars extends Handhistory {

	/**
	 * Importiert eine Pokerstars Handhistorie aus einer Datei
	 * 
	 * @param path Der Pfad zur Datei
	 */
	public ArrayList<Hand> importHistory(String path) throws HandhistoryException {
		// Variablen für das Einlesen von Datei
		BufferedReader inputFile;
		
		// Eingelesener String
		String input = "";
		
		// Speicher für die eingelesenen Hände
		ArrayList<Hand> hands = new ArrayList<Hand>();
		
		// aktuell eingelesene Hand
		Hand hand = null;
		
		// Teil der Datei soll überlesen werden
		boolean skip = false;
		
		try {
			// Datei öffen
			inputFile = new BufferedReader( new FileReader(path) );
			
//String[] ssIDs = TimeZone.getAvailableIDs();
//for( int i=0; i<ssIDs.length; i++ )  System.out.println( ssIDs[i] );			
			// Eingabedatei Zeile fuer Zeile lesen
	        while ( (input = inputFile.readLine()) != null) {
	        	// leere Zeilen überlesen
	        	if(input.trim().equals("")) {
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
	        	} else if (input.startsWith("*** HOLE CARDS ***")) { //$NON-NLS-1$
	        		// Nix zu tun, nur Texthinweis
	        	} else if (input.startsWith("*** SUMMARY ***")) { //$NON-NLS-1$
	        		// Nix zu tun, nur Texthinweis
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
	        		System.out.println(input);
	        	}
        	}
		}
		catch (IOException e) {
			throw new HandhistoryIllegalFormatException("IOException");
		} catch (HandException e) {
			throw new HandhistoryIllegalFormatException("HandException");
		} catch (NumberFormatException e) {
			throw new HandhistoryIllegalFormatException("NumberFormatException"); 
		} catch (ParseException e) {
			throw new HandhistoryIllegalFormatException("ParseException");
		} catch (PlayerException e) {
			throw new HandhistoryIllegalFormatException("PlayerException"); 
		} catch (ActionException e) {
			throw new HandhistoryIllegalFormatException("ActionException");
		} catch (HandhistoryException e) {
			throw new HandhistoryIllegalFormatException("HandhistoryException");
		} catch (CardException e) {
			throw new HandhistoryIllegalFormatException("CardException");
		} 
		return hands;
	}

	/**
	 * Konvertiert die Zeile mit der abschließenden Übersicht ein
	 * 
	 * @param input Die Eingabezeile
	 * @param hand Die aktuell eingelesene Hand
	 * @throws NumberFormatException
	 * @throws HandIllegalPotsizeException
	 * @throws HandIllegalRakesizeException
	 */
	private void convertTotal(String input, Hand hand) throws NumberFormatException, HandIllegalPotsizeException, HandIllegalRakesizeException {
		// Potgröße und Rake übernehmen
		
		// Einleitung überlesen
		StringTokenizer tokenizer = new StringTokenizer(input);
		String text = tokenizer.nextToken();
		text = tokenizer.nextToken();
		
		// Potgröße
		text = tokenizer.nextToken();
		if (text.startsWith("$")) { //$NON-NLS-1$
			text = text.substring(1, text.length()); 
		}
		hand.setPot(Double.valueOf(text));
		
		// Rake
		text = tokenizer.nextToken();
		text = tokenizer.nextToken();
		text = tokenizer.nextToken();
		if (text.startsWith("$")) { //$NON-NLS-1$
			text = text.substring(1, text.length());
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
			ErrorHandler.handleError(e, "Fehler beim Hinzugügen der Poketkarten", false);
		}
	}

	/**
	 * Fügt einer Hand eine Aktion hinzu
	 * 
	 * @param input Die Zeile mit der Aktion
	 * @param hand Die Hand für die Aktion
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
		String name = tokenizer.nextToken();
		while (!name.endsWith(":") && !"***".equals(name) && tokenizer.hasMoreTokens()) { //$NON-NLS-1$
			name = name + " " + tokenizer.nextToken();
		}
		if (!tokenizer.hasMoreElements()) {
			tokenizer = new StringTokenizer(input);
			name = tokenizer.nextToken();	
		}

		if (name.endsWith(":")) { //$NON-NLS-1$
			name = name.substring(0, name.length() - 1);
		}
		Player player = hand.getPlayerByName(name);
		
		// macht was
		String text = ""; //$NON-NLS-1$
		if (tokenizer.hasMoreTokens()) {
			text = tokenizer.nextToken();
		}
		int action = 0;
		if ("calls".equals(text)) { //$NON-NLS-1$
			action = Action.CALL;
		} else if ("folds".equals(text)) { //$NON-NLS-1$
			action = Action.FOLD;
		} else if ("raises".equals(text)) { //$NON-NLS-1$
			action = Action.RAISE;
		} else if ("posts".equals(text)) { //$NON-NLS-1$
			// Art des Blinds ermitteln
			String blindtype = tokenizer.nextToken();
			// nachtes Wort ist entweder "blind" oder "&" 
			text = tokenizer.nextToken();
			if ("small".equals(blindtype) && "&".equals(text)) { //$NON-NLS-1$
				action = Action.SMALL_AND_BIGBLIND;
				text = tokenizer.nextToken();
				text = tokenizer.nextToken();
			}
			else  if ("small".equals(blindtype)) { //$NON-NLS-1$
				action = Action.SMALLBLIND;
			} else if ("big".equals(blindtype)) { //$NON-NLS-1$
				action = Action.BIGBLIND;
			} else {
				throw new HandhistoryIllegalFormatException();
			}
	    } else if ("bets".equals(text)) { //$NON-NLS-1$
			action = Action.BET;
		} else if ("collected".equals(text)) { //$NON-NLS-1$
			action = Action.COLLECT;
		} else if ("FLOP".equals(text)) { //$NON-NLS-1$
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
		} else if ("doesn't".equals(text)) { //$NON-NLS-1$
			// Nix zu tun, einer tut einfach was nicht
		} else if ("checks".equals(text)) { //$NON-NLS-1$
			action = Action.CHECK;
		} else if ("shows".equals(text)) { //$NON-NLS-1$
			// Aktion auf Null jetzen, da direkt hinzugefügt
			action = 0;
			String karten = "";
			int pos = input.length() - 1;
			while (pos >= 0 && !"]".equals(input.substring(pos, pos + 1))) { //$NON-NLS-1$
				pos--;
			}
			pos--;
			while (pos >= 0 && !"[".equals(input.substring(pos, pos + 1))) { //$NON-NLS-1$
				karten = input.substring(pos, pos + 1) + karten;
				pos--;
			}
			
			// Karte(n) ermitteln und als Aktion hinzufügne
			StringTokenizer tokenizer2 = new StringTokenizer(karten);
			while (tokenizer2.hasMoreTokens()) {
				String karte = tokenizer2.nextToken();	
				try {
					hand.addAction(new Action(Action.SHOW, player, Card.getCardByString(karte)));
				} catch (ActionException e) {
					ErrorHandler.handleError(e, "Fehler beim Hinzufügen der gezeigten Karten", false);
				}
			}
		} else if ("is".equals(text)){ //$NON-NLS-1$
			if (tokenizer.hasMoreTokens()) {
				text = text + tokenizer.nextToken();
			} else {
				System.out.println("Unbekannt 2: " + input);
			}
			if (tokenizer.hasMoreTokens()) {
				text = text + tokenizer.nextToken();
			} else {
				System.out.println("Unbekannt 2: " + input);
			}
			if ("issittingout".equals(text)) { //$NON-NLS-1$
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
		} else {
			System.out.println("UNBEKANNT: " + input);
		}
		
		// bei einigen Aktionen brauchen wir den Wert
		Object value = null;
		if (action == Action.BET ||
			  action == Action.CALL ||
			  action == Action.RAISE ||
			  action == Action.SMALLBLIND ||
			  action == Action.BIGBLIND ||
			  action == Action.COLLECT ||
			  action == Action.SMALL_AND_BIGBLIND) {
			text = tokenizer.nextToken();
			if (text.startsWith("$")) { //$NON-NLS-1$
				text = text.substring(1, text.length());
			}
			value = Double.valueOf(text);
		} else if (action == Action.SAY) {
			value = tokenizer.nextToken();
		}
		
		// und Aktion übernehmen
		if (action != 0) {
			try {
				hand.addAction(new Action(action, player, value));
			} catch (ActionException e) {
				ErrorHandler.handleError(e, "Fehler beim Kovertieren einer Aktion", false);
			}
		}
	}

	/**
	 * Konvertiert die Zeile mit den Boardkarten
	 * 
	 * @param input Die Eingabezeile
	 * @param hand Die Hand zu der das Board gehört
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
		
		// Karte(n) ermitteln und zum Board hinzufugen
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
		String name = "";
		text = tokenizer.nextToken();
		while (!text.startsWith("(")) { //$NON-NLS-1$
			name = name + " " + text;
			text = tokenizer.nextToken();
		}
		name = name.substring(1, name.length());
		
		// und der Chipstack
		String stack = text;
		while ("(".equals(stack.substring(0, 1)) || "$".equals(stack.substring(0, 1))) { //$NON-NLS-1$
			stack = stack.substring(1, stack.length());
		}
		
		// prüfen ob Spieler aussetzt
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
				System.out.println("Unbekannt 3: " + input); 
			}
		}
		
		// Spieler zur Hand hinzufügen
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
		
		// Anführungszeichen entfernen
		if (text.startsWith("'")) { //$NON-NLS-1$
			text = text.substring(1, text.length() - 1);
		}
		hand.setTitel(text);
		
		// Anzahl Sitzplätze
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
		
			// Tuniernummer und -typ überlesen
			text = tokenizer.nextToken();
			text = tokenizer.nextToken();
			text = tokenizer.nextToken();
			
		}
		
		// Spieltyp aus Eingabe lesen
		String type = ""; //$NON-NLS-1$
		while (!text.startsWith("(") && !"-".equals(text)) { //$NON-NLS-1$
			type = type + text;
			text = tokenizer.nextToken();
		}
		
		// und geben die bekannten Texte prüfen
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
		
		// Blinds übernehmen
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

}
