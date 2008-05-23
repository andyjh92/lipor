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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import rjPokerReplay.ApplicationWorkbenchAdvisor;
import rjPokerReplay.Constants;
import rjPokerReplay.util.ErrorHandler;
import rjPokerReplay.views.ViewTable;

/**
 * @author Ralf Joswig
 *
 */
public class ShowViewAction extends Action {
	
	private final String actionId = "ShowViewAction";
	private IWorkbenchWindow window;
	private String text = "";
	private boolean show = false;
	private String id = "";
	
	/**
	 * Konstruktor, die nötigen Daten speichern
	 * 
	 * @param window Das übergeordnete Fenster
	 * @param text Text für den Reiter
	 * @param id Die Id des verwendeten Views
	 */
	public ShowViewAction(IWorkbenchWindow window, String text, String id) {
		if (window == null) {
			throw new IllegalArgumentException();
		}
		this.window = window;
		this.text = text;
		this.id = id;
	}

	/**
	 * Gibt die ID des View zurück
	 */
	public String getId() {
        return actionId;
    }
	
	/**
	 * Gibt den Text mit der Bezeichnung des Views zurück
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Setzt den Text mit der Bezeichnung des Views
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Die Aktion die ausgeführt wird wenn der Menüpunkt ausgewählt wird.
	 * Hier das Umschalten zwischen Anzeigen und Verbergen des Views
	 */
	public void run() {
		if (show) {
			// View wird bereits angezeigt, dann verbergen
			show = false;
		} else {
			// View ist nicht sichtbar, dann anzeigen
			show = true;
		}
		// Update der Anzeige
		show(show);
	}
	
	/**
	 * Die Routine die das eigendliche Anzeigen oder Verbergen des Views übernimmt.
	 * 
	 * @param show True wenn der View angezeigt weden soll, sonst false
	 */
	public void show(boolean show) {
		// den Wert ob angezeigt wird oder nicht merken, wichtig für externe Aufrufe
		this.show = show;
		
		if (show) {
			// View soll anzgezeigt werden.
			// Vor den Menüpunkt einen Haken setzen
			setImageDescriptor(ImageDescriptor.createFromImage((ApplicationWorkbenchAdvisor.getImageStore().get(Constants.IMG_CHECKED))));
			
			// Wenn Views bereits angelegt wurden, den gewünschten View anzeigen
			try {
				window.getPages()[0].showView(id);
			} catch (PartInitException e) {
				ErrorHandler.handleError(e, "View kann nicht angezeigt werden.", false);
			} catch (ArrayIndexOutOfBoundsException e) {
				// Es wurden noch keine Views angelegt, also kann hier noch nichts geschehen
			}
			
			// Tisch neu zeichnen, da jetzt weniger Platz für die Anzeige
			if (window.getActivePage() != null) {
				((ViewTable)(window.getActivePage().findView(ViewTable.ID))).drawTable();
			}
				
		} else {
			// Den Haken vor dem Menüpunkt entfernen
			setImageDescriptor(null);
			
			// Wenn Views bereits angelegt wurden, den passenden finden und verbergen
			try {
				window.getPages()[0].hideView(window.getPages()[0].findView(id));
				
				if (window.getActivePage().findView(id) != null) {
					// Tisch neu zeichnen, da jetzt weniger Platz für die Anzeige
					((ViewTable)(window.getActivePage().findView(ViewTable.ID))).drawTable();
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// Es wurden noch keine Views angelegt, also kann hier noch nichts geschehen
			}
		}
	}
}
