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
 *
 */
package cardstuff;

/**
 * @author Ralf Joswig
 *
 */
public class Pot {
	
	// Anzahl an Spielern die in dieser Runde bereits bezahl haben
	private int playerPayed = 0;
	
	// Zähler für die Pots (Main- und Sidepot
	private int potNumber = 0;
	
	// Zähler für die Pots in der aktuellen Setzrunde
	private int roundPotNumber = 0;
	
	// Der Main- und die Sidepots
	private double pot[] = new double[10];
	
	// die in der laufenden Runde gezahlten Einsätze
	// auch da kann es bereits einen oder mehr Sidepost geben
	private double roundPot[] = new double[10];
	
	// ist ein Spieler in der aktuellen Runde All-In
	private boolean onePlayerIsAllIn = false;
	
	// Der Einsatz mit dem ein Spieler All-In ging
	private double allInBet = 0.0;

	/**
	 * Konstruktor
	 */
	public Pot() {
		// zur Sicherheit alle Felder auf Anfang stellen
		clear();
	}
	
	/**
	 * Überträgt am Ende einer Setzrunde die Zwischenpots in die Hauptpots
	 */
	public void roundsEnd() {
		for (int i = 0; i <= roundPotNumber; i++) {
			pot[potNumber] = pot[potNumber] + roundPot[i];
			roundPot[i] = 0.0; 
		}
		roundPotNumber = 0;
	}
	
	/**
	 * Setzt alle Felder des Pot auf den Grundwert.
	 * Wird benötigt wenn ein Pot für eine neue Setzrunde verwendet wird
	 */
	public void clear() {
		// alle Felder initialisieren
		playerPayed = 0;
		potNumber = 0;
		roundPotNumber = 0;
		onePlayerIsAllIn = false;
		allInBet = 0.0;
		for (int i = 0; i < 10; i++) {
			pot[i] = 0.0;
			roundPot[i] = 0.0;
		}
		
	}
	
	/**
	 * Nimmt die Zahlung eines Spielers entgegen.
	 * Dabei muss beachtet werden ob dieser oder ein anderer
	 * Spieler All-In ist und ggf. der Betrag oder die bereits gezahlten
	 * in Main- und Sidepots aufgeteilt werden.
	 * 
	 * @param player Der Spieler der Zahl
	 * @param amount Der Betrag der geazahlt werden soll
	 * @return Den tatsächlichl gezahlten Betrag
	 */
	public double pay(Player player, double amount) {
		// Betrag von Spieler einfordern
		double payed = player.pay(amount);
		
		// hat der Spieler den vollen Betrag bezahl
		if (payed < amount) {
			// nein, dann ist er All-In
			onePlayerIsAllIn = true;
			
			// dann den aktuellen Pot in einen Main- und eine Sidepot aufteilen
			roundPot[roundPotNumber + 1] = roundPot[roundPotNumber];
			roundPot[roundPotNumber] = payed * playerPayed + payed;
			roundPot[roundPotNumber + 1] = roundPot[roundPotNumber + 1] - payed * playerPayed;
			roundPotNumber++;
			
			// vermerken das der Spieler was gezahlt hat
			playerPayed++;
			
			// für alle späteren Setzer in dieser Runde merken wieviel gezahlt wurde
			allInBet = payed;
			
			
		} else {
			// Spieler hat den vollen Betrag bezahlt.
			// Kann zwar sein das er All-In ist, aber das interessiert uns
			// hier nicht.
			// Ist aber bereits ein Spieler in dieser Runde All-In?
			if (onePlayerIsAllIn) {
				// ja, dann nur einen Teil in den ersten Pot und der Rest in den Sidepot
				roundPot[roundPotNumber - 1] = roundPot[roundPotNumber - 1] + allInBet;
				roundPot[roundPotNumber] = roundPot[roundPotNumber] + payed - allInBet;
			} else {
				// nein, dann vollen Betrag in den aktuellen Pot
				roundPot[roundPotNumber] = roundPot[roundPotNumber] + payed;
			}
			
			// merken das ein weiterer Spieler gezahlt hat
			playerPayed++;
		}
		
		// den gezahlten Betrag zurück melden
		return payed;
	}
	
	/**
	 * Gibt den Gesamtwert aller Pots (Main- und Side) zurück
	 * 
	 * @return Der Gesamtwert des Pots
	 */
	public double getPotSize() {
		// Summe aller Pots
		double potSize = 0.0;
		
		// die Pots der fertigen Runden summieren
		for (int i = 0; i <= potNumber; i++) {
			potSize = potSize + pot[i];
		}
		
		// die Pots der aktuellen Runde summieren
		for (int i = 0; i <= roundPotNumber; i++) {
			potSize = potSize + roundPot[i];
		}
		
		// und die Summe zurück gegeben
		return potSize;
	}
	
	/**
	 * Zahlt einen Betrag aus dem Pot aus
	 * 
	 * @param payment Der auszuzahlende Betrag
	 * @return Der ausgezahlte Betrag
	 * @throws IllegalArgumentException
	 */
	public double payout(double payment) throws IllegalArgumentException{
		if (pot[potNumber] >= payment) {
			pot[potNumber] = pot[potNumber] - payment;
			if (pot[potNumber] == 0 && potNumber > 0) {
				potNumber--;
			}
		} else {
			throw new IllegalArgumentException("Payment biger than potsize");
		}
		
		return payment;
	}
}
