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

import language.Messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import rjPokerReplay.Activator;
import rjPokerReplay.ApplicationWorkbenchAdvisor;
import rjPokerReplay.preferences.PreferenceConstants;
import rjPokerReplay.util.ErrorHandler;
import util.AbstractListener;
import cardstuff.Action;
import cardstuff.Hand;
import cardstuff.Player;
import cardstuff.RatedHand;
import cardstuff.Table;
import cardstuffExceptions.ActionIllegalActionException;
import cardstuffExceptions.PlayerException;

public class ViewHandinfo extends ViewPart implements AbstractListener {
	
	public static final String ID = "RJPoker Replay.ViewHandinfo"; //$NON-NLS-1$
	
	private Text text;
	
	public ViewHandinfo() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
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

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if (ApplicationWorkbenchAdvisor.getHands() != null && ApplicationWorkbenchAdvisor.getHands().size() != 0) {
			updateView();
		}
	}

	/* (non-Javadoc)
	 * @see util.AbstractListener#inform(java.lang.Object)
	 */
	public void inform(Object source) {
		if (ApplicationWorkbenchAdvisor.getHands() != null && ApplicationWorkbenchAdvisor.getHands().size() != 0) {
			updateView();
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
	
	private void updateView() {
		Table table = ApplicationWorkbenchAdvisor.getTable();
		// Pruefen ob der Tisch Daten enthaelt
		if (table != null && table.getPokerroom() != 0) {
        	// ja, dann Tisch anzeigen
			
        	try {
        		IWorkbenchPage activPage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
            	ViewTable tableView = null;
            	if (activPage != null) {
            		tableView = (ViewTable)(activPage.findView(ViewTable.ID));
            	}
            	if (tableView != null) {
            		int pokerroom = -1;
            		if (table != null) {
            			pokerroom = table.getPokerroom();
            		}
            		String prefID = null;
            		if (pokerroom != -1) {
            			switch (pokerroom) {
            			case Hand.POKERSTARS:
            				prefID = PreferenceConstants.P_POKERSTARS_PLAYER;
            				break;
            			}
            		}
            		String playerName = ""; //$NON-NLS-1$
            		if (prefID != null) {
            			playerName = Activator.getDefault().getPreferenceStore().getString(prefID);
            		}
            		Hand hand = ApplicationWorkbenchAdvisor.getHands().get(ApplicationWorkbenchAdvisor.getActiveHand());
            		Action action = null;
            		int handStep = ApplicationWorkbenchAdvisor.getHandStep() - 1;
            		if (hand != null && handStep != -1) {
            			action = hand.getAction(handStep);
            		}
            		if (handStep == -1) {
            			text.setText(""); //$NON-NLS-1$
            		}
            		int actionNumber = -1;
            		if (action != null) {
            			actionNumber = action.getAction();
            		}
            		if (actionNumber != -1 && (actionNumber == Action.DEAL ||
            							       actionNumber == Action.FLOP ||
            							       actionNumber == Action.RIVER ||
            							       actionNumber == Action.TURN)) {
            			// alten Inhalt loeschen
            			text.setText(""); //$NON-NLS-1$
            			
            			// Ermitteln was der Spieler haellt
            			Player player = tableView.getTable().getPlayerByName(playerName);
            			text.append(playerName + Messages.ViewHandinfo_0+ new RatedHand(player, tableView.getActualBoard(), 0).toString());
            		}
            	}
			} catch (PlayerException e) {
				ErrorHandler.handleError(e, Messages.ViewHandinfo_1, false);
			} catch (ActionIllegalActionException e) {
				ErrorHandler.handleError(e, Messages.ViewHandinfo_1, false);
			}
        	
		}
	}

}
