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

package rjPokerReplay.wizard;

import language.Messages;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;

import rjPokerReplay.Activator;
import rjPokerReplay.ApplicationWorkbenchAdvisor;
import rjPokerReplay.util.ErrorHandler;
import rjPokerReplay.views.ViewTable;
import cardstuff.HandhistoryPokerstars;
import cardstuff.Table;
import cardstuffExceptions.HandhistoryException;

public class Pokerstars extends Wizard implements IImportWizard {

	/**
	 * Konstruktor
	 */
	public Pokerstars() {
	}

	/**
	 * Arbeiten die beim Betaetigen der Finish-Taste ausgefuehrt werden
	 */
	@Override
	public boolean performFinish() {
		// gewaehlten Dateiname ermitteln
		String file = ""; //$NON-NLS-1$
		SelectFile t = (SelectFile)getPage("Select File");//$NON-NLS-1$
		if (t != null) {
			file = t.getPath();
		}
		
		// wenn kein Dateiname erfasst gehts zurueck
		if (t == null || "".equals(file)) { //$NON-NLS-1$
			return false;
		}
		
		// Datei importieren
		try {
			ApplicationWorkbenchAdvisor.setHands(new HandhistoryPokerstars().importHistory(file));
		} catch (HandhistoryException e) {
			ErrorHandler.handleError(e, Messages.Pokerstars_1, false);
			return false;
		}
		
		// Info mit Anzahl der importierten Haende ausgeben
		int anzahl = ApplicationWorkbenchAdvisor.getHands().size();
		MessageBox msg = new MessageBox(getShell());
		msg.setMessage(String.valueOf(anzahl) + Messages.Pokerstars_2);
		msg.setText(Messages.Pokerstars_3);
		msg.open();
		
		// Table ueber Aenderungen informieren
		if (ApplicationWorkbenchAdvisor.getHands() != null) {
        	ApplicationWorkbenchAdvisor.setActiveHand(0);
        	ApplicationWorkbenchAdvisor.setHandStep(0);
        	ApplicationWorkbenchAdvisor.setTable((Table)ApplicationWorkbenchAdvisor.getHands().get(0));
        	IWorkbenchPage activPage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        	ViewTable tableView = null;
        	if (activPage != null) {
        		tableView = (ViewTable)(activPage.findView(ViewTable.ID));
        	}
        	if (tableView != null) {
        		tableView.resetTable();
        		tableView.drawTable();
        	}
        }
		
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	/**
	 * Fuegt weitere Seiten zum Wizard hinzu
	 */
	public void addPages() {
		// Seite mit Dialog zur Auswahl der Datei
		addPage(new SelectFile("Select File"));//$NON-NLS-1$
	}
}
