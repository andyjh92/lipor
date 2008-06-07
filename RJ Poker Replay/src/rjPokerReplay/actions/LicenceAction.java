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
package rjPokerReplay.actions;

import language.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

import rjPokerReplay.util.LicenceDialog;

public class LicenceAction extends Action {
	
	private final String actionId = "LicenceAction";  //$NON-NLS-1$
	private IWorkbenchWindow window;

	private LicenceDialog licenceDialog;
	/**
	 * Konstruktor, die noetigen Daten speichern
	 * 
	 * @param window Das uebergeordnete Fenster
	 */
	public LicenceAction(IWorkbenchWindow window) {
		super(Messages.LicenceAction_0);
		if (window == null) {
			throw new IllegalArgumentException();
		}
		this.window = window;
	}
	
	/**
	 * Gibt die ID des View zurueck
	 */
	public String getId() {
        return actionId;
    }
	
	/**
	 * Zeigt den Dialog mit der Lizenz
	 */
	public void run() {
		if (licenceDialog == null) {
			licenceDialog = new LicenceDialog(window.getShell());
		}
		licenceDialog.open();
	}

}
