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

package io.gitlab.lipor.wizard;


import java.util.ArrayList;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;

import io.gitlab.lipor.Activator;
import io.gitlab.lipor.ApplicationWorkbenchAdvisor;
import io.gitlab.lipor.cardstuff.Hand;
import io.gitlab.lipor.cardstuff.HandhistoryPokerstars;
import io.gitlab.lipor.cardstuff.Table;
import io.gitlab.lipor.cardstuff.win2day.HandhistoryWin2Day;
import io.gitlab.lipor.cardstuffExceptions.HandhistoryException;
import io.gitlab.lipor.preferences.PreferenceConstants;
import io.gitlab.lipor.util.ErrorHandler;
import io.gitlab.lipor.views.ViewTable;


public class Win2day extends Wizard implements IImportWizard {

	/**
	 * Konstruktor
	 */
	public Win2day() {
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
		ArrayList<Hand> handhis;
		try {
			handhis = new HandhistoryWin2Day().importHistory(file);
			ApplicationWorkbenchAdvisor.setHands(handhis);
		} catch (HandhistoryException e) {
			ErrorHandler.handleError(e, Messages.Pokerstars_0, false);
			return false;
		}
		
		// Info mit Anzahl der importierten Haende ausgeben
		int anzahl = ApplicationWorkbenchAdvisor.getHands().size();
		MessageBox msg = new MessageBox(getShell());
		msg.setMessage(String.valueOf(anzahl) + Messages.Pokerstars_1);
		msg.setText(Messages.Pokerstars_2);
		msg.open();
		
		// pruefen ob in den Benutzereinstellungen ein anderer Spielername hinterlegt ist als der ermittelte
		String nikname = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_WIN2DAY_PLAYER);
		String handNikname = handhis.get(0).getNikname();
		if (!nikname.equals(handNikname)) {
			// Beide Namen weichen voneinander ab, nachfragen
			InputDialog dialog = new InputDialog(null, Messages.Pokerstars_3, Messages.Pokerstars_4, handNikname, null);
			dialog.open();
			if (dialog.getReturnCode() == Window.OK) {
				// der neue Name soll uebernommen werden
				Activator.getDefault().getPreferenceStore().setValue(PreferenceConstants.P_WIN2DAY_PLAYER, handNikname);
			}
		}
		
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
