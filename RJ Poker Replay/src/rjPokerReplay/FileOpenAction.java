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
package rjPokerReplay;

import java.io.IOException;

import javax.swing.JFileChooser;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

import cardstuff.Handhistory;
import cardstuff.HandhistoryPokerstars;
import cardstuff.Table;
import cardstuffExceptions.HandhistoryException;

/**
 * @author Ralf Joswig
 *
 */
public class FileOpenAction extends Action {
	
	private final String actionId = "fileOpenAction";
	private IWorkbenchWindow window;
	
	public FileOpenAction(IWorkbenchWindow window) {
//		super(window, "ID");
		if (window == null) {
			throw new IllegalArgumentException();
		}
		this.window = window;
	}

	public String getId() {
        return actionId;
    }
	
	public String getText() {
		return "Open";
	}
	
	public void run() {
//		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Titel", "Bitte Datei öffen");
		// Dialogobjekt für den öffnen Dialog erzeugen
		JFileChooser fc = new JFileChooser();
		
        // festlegen das nur Dateien ausgewaehlt werden koennen
		// JFileChooser.FILES_ONLY            = nur Dateien (0)
		// JFileChooser.DIRECTORIES_ONLY      = nur Verzeichnise (1)
		// JFileChooser.FILES_AND_DIRECTORIES = Dateien und Verzeichnise (2)
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Dateifilter vorbelegen
//        ExampleFileFilter filter = new ExampleFileFilter();
//        filter.addExtension("ovl");
//        filter.addExtension("gpx");
//        filter.addExtension("pth");
//        filter.setDescription("Tour-Dateien");
//        fc.setFileFilter(filter);
        
        // Dialoag anzeigen und Rueckgabe merken
        int returnVal = fc.showOpenDialog(fc);//  showOpenDialog(this);
        
        // Wenn eine Datei gewaehlt wurde
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	try {
				switch (Handhistory.getKind(fc.getSelectedFile().getAbsolutePath())) {
				case Handhistory.POKERSTARS:
					ApplicationWorkbenchAdvisor.setHands(new HandhistoryPokerstars().importHistory(fc.getSelectedFile().getAbsolutePath()));
					break;
				default:
				}
			} catch (IOException e) {
				ErrorHandler.handleError(e, "Fehler beim Einlesen der Datei", false);
			} catch (HandhistoryException e) {
				ErrorHandler.handleError(e, "Fehler beim Einlesen der Datei", false);
			}
        }
        
        if (ApplicationWorkbenchAdvisor.getHands() != null) {
        	ApplicationWorkbenchAdvisor.setActiveHand(0);
        	ApplicationWorkbenchAdvisor.setHandStep(0);
        	ApplicationWorkbenchAdvisor.setTable((Table)ApplicationWorkbenchAdvisor.getHands().get(0));
        	((View)(window.getActivePage().findView(View.ID))).resetTable();
        	((View)(window.getActivePage().findView(View.ID))).drawTable();
        }
	}
}
