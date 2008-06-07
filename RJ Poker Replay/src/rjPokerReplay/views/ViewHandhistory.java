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

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import rjPokerReplay.Activator;
import rjPokerReplay.ApplicationWorkbenchAdvisor;
import util.AbstractListener;

import cardstuff.Hand;

public class ViewHandhistory extends ViewPart implements AbstractListener {
	
	public static final String ID = "RJPoker Replay.Handhistory"; //$NON-NLS-1$
	
	// Ein Textfeld fuer den origianal Handverlauf
	private Text text;
	
	// der aktuelle Pokerraum
	private int pokerroom = -1;
	
	// das aktuelle Spiel
	private String game = "";  //$NON-NLS-1$

	public ViewHandhistory() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);
		
		// hinterlegen das der View informiert wird wenn sich was am Tisch tut
		IWorkbenchPage activPage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
    	ViewTable tableView = null;
    	if (activPage != null) {
    		tableView = (ViewTable)(activPage.findView(ViewTable.ID));
    	}
    	if (tableView != null) {
    		tableView.addListener(ID, this);
    	}
	}

	@Override
	public void setFocus() {
		if (ApplicationWorkbenchAdvisor.getHands() != null && ApplicationWorkbenchAdvisor.getHands().size() != 0) {
			showHand(ApplicationWorkbenchAdvisor.getHands().get(ApplicationWorkbenchAdvisor.getActiveHand()));
		}
	}
	
	/**
	 * Zeigt den Handverlauf der aktuellen Hand an
	 * 
	 * @param hand Die aktuelle Hand
	 */
	public void showHand(Hand hand) {
		// ein Upate wird nur benoetig wenn sich die Hand geaendert hat
		if (!game.equals(hand.getGame()) ||
				pokerroom != hand.getPokerroom()) {
			
			// aktuelle Werte merken
			game = hand.getGame();
			pokerroom = hand.getPokerroom();
			
			// den Handverlauf anzeigen
			text.setText(""); //$NON-NLS-1$
			if (hand != null) {
				Iterator<String> iter = hand.getFileLines().iterator();
				while (iter.hasNext()) {
					text.append(iter.next() + Text.DELIMITER);
				}
			}
			text.setSelection(0,0);
			text.showSelection();
		}
	}

	/**
	 * Wie von aussen aufgerufen wenn sich was am Tisch geaendert hat. Zeigt dann die aktuelle Hand an
	 */
	public void inform(Object source) {
		if (ApplicationWorkbenchAdvisor.getHands() != null && ApplicationWorkbenchAdvisor.getHands().size() != 0) {
			showHand(ApplicationWorkbenchAdvisor.getHands().get(ApplicationWorkbenchAdvisor.getActiveHand()));
		}
	}
	
	/**
	 * Wird durchlaufen wenn der View nicht mehr angezeigt wird
	 */
	public void dispose() {
		super.dispose();
		// der View soll nicht mehr ueber Aenderungen am Tisch informiert werden
		IWorkbenchPage activPage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
    	ViewTable tableView = null;
    	if (activPage != null) {
    		tableView = (ViewTable)(activPage.findView(ViewTable.ID));
    	}
    	if (tableView != null) {
    		tableView.removeListener(ID);
    	}
	}

}

