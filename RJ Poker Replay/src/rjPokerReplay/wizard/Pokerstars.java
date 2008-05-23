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
import rjPokerReplay.views.ViewTableinfo;
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
	 * Arbeiten die beim Betätigen der Finish-Taste ausgeführt werden
	 */
	@Override
	public boolean performFinish() {
		// gewählten Dateiname ermitteln
		String file = "";
		SelectFile t = (SelectFile)getPage("Select File");
		if (t != null) {
			file = t.getPath();	
		}
		
		// Datei importieren
		try {
			ApplicationWorkbenchAdvisor.setHands(new HandhistoryPokerstars().importHistory(file));
		} catch (HandhistoryException e) {
			ErrorHandler.handleError(e, "Error during import handhistory", false);
			return false;
		}
		
		// Info mit Anzahl der importierten Hände ausgeben
		int anzahl = ApplicationWorkbenchAdvisor.getHands().size();
		MessageBox msg = new MessageBox(getShell());
		msg.setMessage(String.valueOf(anzahl) + " hand(s) imported");
		msg.setText("Import was successful");
		msg.open();
		
		// Table über Änderungen informieren
		if (ApplicationWorkbenchAdvisor.getHands() != null) {
        	ApplicationWorkbenchAdvisor.setActiveHand(0);
        	ApplicationWorkbenchAdvisor.setHandStep(0);
        	ApplicationWorkbenchAdvisor.setTable((Table)ApplicationWorkbenchAdvisor.getHands().get(0));
        	IWorkbenchPage activPage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        	ViewTable tableView = null;
        	ViewTableinfo infoView = null;
        	if (activPage != null) {
        		tableView = (ViewTable)(activPage.findView(ViewTable.ID));
        		infoView = (ViewTableinfo)(activPage.findView(ViewTableinfo.ID)); 
        	}
        	if (tableView != null) {
        		tableView.resetTable();
        		tableView.drawTable();
        		tableView.updateHandView();
        	}
        	if (infoView != null) {
        		infoView.showInfo();
        	}
        }
		
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	/**
	 * Fügt weitere Seiten zum Wizard hinzu
	 */
	public void addPages() {
		// Seite mit Dialog zur Auswahl der Datei
		addPage(new SelectFile("Select File"));
	}
}