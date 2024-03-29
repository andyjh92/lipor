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

package io.gitlab.lipor.views;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import io.gitlab.lipor.Activator;
import io.gitlab.lipor.ApplicationWorkbenchAdvisor;
import io.gitlab.lipor.cardstuff.Action;
import io.gitlab.lipor.cardstuff.Hand;
import io.gitlab.lipor.cardstuff.Table;
import io.gitlab.lipor.cardstuffExceptions.ActionIllegalActionException;
import io.gitlab.lipor.util.AbstractListener;



public class ViewTableinfo extends ViewPart implements AbstractListener {
	
	public static final String ID = "RJPoker Replay.ViewTableinfo"; //$NON-NLS-1$
	
	private Text text;

	public ViewTableinfo() {
	}

	@Override
	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);
		
		// hinterlegen das der View informiert wird wenn sich was am Tisch tut
    	Activator.getDefault().addListener(ID, this);
	}

	@Override
	public void setFocus() {
		showInfo();
	}
	
	public void showInfo() {
		Table table = ApplicationWorkbenchAdvisor.getTable();
		// Pruefen ob der Tisch Daten enthaelt
		if (table != null && table.getPokerroom() != 0) {
        	// ja, dann Tisch anzeigen
			
			// alten Inhalt loeschen
			text.setText(""); //$NON-NLS-1$
			
        	// Tischinformation wie Name, Spielnummer und Datum
        	text.append(Messages.ViewTableinfo_0 +
        			table.getPokerroomText() + " "); //$NON-NLS-1$
        	text.append(table.getGame() + " "); //$NON-NLS-1$
        	text.append(table.getGameTypeText() + " "); //$NON-NLS-1$
        	text.append(table.getTabletypeText() + Text.DELIMITER);
        	
        	text.append(Messages.ViewTableinfo_1 + table.getSmallblind() +
        			"/" + //$NON-NLS-1$ 
        			table.getBigblind() + " " +  //$NON-NLS-1$
        			"Ante: " + table.getAnte() + " "); //$NON-NLS-1$ //$NON-NLS-2$
        	text.append(table.getCountSeats() + "-max" + Text.DELIMITER); //$NON-NLS-1$

        	text.append(Messages.ViewTableinfo_2 + table.getTitel() +
        			" "); //$NON-NLS-1$
        	SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy - HH:mm:ss" ); //$NON-NLS-1$
        	text.append(df.format(table.getDate().getTime()) + " "); //$NON-NLS-1$
        	text.append(Messages.ViewTableinfo_3 + String.valueOf(table.getRake()) + Text.DELIMITER);
        	
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
	
	/**
	 * Wie von aussen aufgerufen wenn sich was am Tisch geaendert hat. Zeigt dann die aktuelle Hand an
	 */
	public void inform(Object source) {
		showInfo();
	}
	
	/**
	 * Wird durchlaufen wenn der View nicht mehr angezeigt wird
	 */
	public void dispose() {
		super.dispose();
		// der View soll nicht mehr ueber Aenderungen am Tisch informiert werden
   		Activator.getDefault().removeListener(ID);
	}
}
