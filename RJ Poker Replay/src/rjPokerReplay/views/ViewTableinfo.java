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

package rjPokerReplay.views;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import rjPokerReplay.ApplicationWorkbenchAdvisor;

import cardstuff.Action;
import cardstuff.Hand;
import cardstuff.Table;
import cardstuffExceptions.ActionIllegalActionException;

public class ViewTableinfo extends ViewPart {
	
	public static final String ID = "RJPoker Replay.ViewTableinfo";
	
	private Text text;

	public ViewTableinfo() {
	}

	@Override
	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);
	}

	@Override
	public void setFocus() {
		showInfo();
	}
	
	public void showInfo() {
		Table table = ApplicationWorkbenchAdvisor.getTable();
		// Prüfen ob der Tisch Daten enthält
		if (table != null && table.getPokerroom() != 0) {
        	// ja, dann Tisch anzeigen
			
			// alten Inhalt löschen
			text.setText("");
			
        	// Tischinformation wie Name, Spielnummer und Datum
        	text.append("Pokerroom: " + table.getPokerroomText() + " ");
        	text.append(table.getGame() + " ");
        	text.append(table.getGameTypeText() + " ");
        	text.append(table.getTabletypeText() + Text.DELIMITER);
        	
        	text.append("Blinds: " + table.getSmallblind() + "/" + table.getBigblind()+ " ");
        	text.append(table.getCountSeats() + "-max" + Text.DELIMITER);

        	text.append("Table: " + table.getTitel() + " ");
        	SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy - HH:mm:ss" );
        	text.append(df.format(table.getDate().getTime()) + " ");
        	text.append("Rake: " + String.valueOf(table.getRake()) + Text.DELIMITER);
        	
        	// letzte Aktion
			try {
				Hand hand = ApplicationWorkbenchAdvisor.getHands().get(ApplicationWorkbenchAdvisor.getActiveHand());
				Action action = hand.getAction(ApplicationWorkbenchAdvisor.getHandStep() - 1);
				text.append(action.toString());
			} catch (ActionIllegalActionException e) {
				// Nix, es gibt halt eben keine letze Aktion
			}
		}
	}

}
