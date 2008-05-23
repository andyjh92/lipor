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

import java.io.IOException;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rjPokerReplay.Activator;
import rjPokerReplay.ApplicationWorkbenchAdvisor;
import rjPokerReplay.preferences.PreferenceConstants;
import rjPokerReplay.util.ErrorHandler;
import rjPokerReplay.util.FileUtil;
import cardstuff.Handhistory;

public class SelectFile extends WizardPage {

	// Textfeld mit Dateipfad
	private Text tPfad;
	
	/**
	 * Konstruktor
	 * 
	 * @param pageName Name (Überschrift) der Wizardseiten
	 */
	protected SelectFile(String pageName) {
		super(pageName);
        setTitle("Import Handhistory from Pokerstars");
        setDescription("Select Handhistory from Pokerstars to import");
        setPageComplete(false);
	}

	/**
	 * Seitenelemente hinzufügen
	 */
	public void createControl(Composite parent) {
		// Container für die Elemente
		Composite container = new Composite(parent, SWT.NULL);
		
		// Das Layout festlegen
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        container.setLayout(layout);
        
        // Label für den Dateipfad
        Label lPfad = new Label(container, SWT.NULL);
        lPfad.setText("File:");
        
        // Ein Textfeld für den Pfad
        tPfad = new Text(container, SWT.LEFT);
        GridData gridData = new GridData();
		gridData.widthHint = 400;
		tPfad.setLayoutData(gridData);
		
		// Listener für Änderungne am Text
		tPfad.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// prüfen ob Eingabe vollständig und plausibel
				isPageComplete();
				getContainer().updateButtons();
			}
		});
		
		// ein Button um den Pfad zu wählen
		Button bWaehlen = new Button(container, SWT.ICON);
		try {
			// dem Button ein Bild hinzufügen
			bWaehlen.setImage(ApplicationWorkbenchAdvisor.getImageStore().get(rjPokerReplay.Constants.IMG_OPEN_FOLDER));
		} catch (Exception e) {
			// wenn das mit dem Bild nicht ging, einen Text wählen
			bWaehlen.setText("Brows");
		}
		// Und eine Listener für die Wahltaste
		bWaehlen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectPfadDialog();
				getContainer().updateButtons();
			}
		});
        // Versuchen wir ein Bild die Wählentaste zu laden
        setControl(container);
	}

	/**
	 * Dialog zur Dateiauswahl anzeigen
	 */
	protected void selectPfadDialog() {
		// Den Diaog öffnen
		FileDialog dia = new  FileDialog(getShell(), SWT.OPEN);
		
		// Pfad für vorgeinstelltes Verzeichnis setzen
		dia.setFilterPath(Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_POKERSTARS_PATH));
		
		// Dialog anzeigen
		String pfad = dia.open();
		
		// Wenn ein Pfad eingegeben wurde ... 
		if (pfad != null) {
			// Den Pfad in das Textfeld übertragen
			tPfad.setText(pfad);
		}
		
	}
	
	/**
	 * Prüft ob alle Eingaben im Dialog gemacht wurden und sinnvoll sind
	 */
	public boolean isPageComplete() {
		boolean ret = false;
		
		// wurde ein Dateinameerfasst
		if (FileUtil.checkFileExists(tPfad.getText())) {
			// ja, dann Fehlermeldung löschen und Rückgabewert auf vollständig setzen
			setErrorMessage(null);
			ret = true;
		} else {
			// nein, Fehlermeldung ausgeben
			setErrorMessage("File does not exist");
		}
		
		// Prüfen ob der Daeityp eine Pokerstars-Handhistorie-Datei ist
		try {
			if (ret && Handhistory.getKind(tPfad.getText()) != Handhistory.POKERSTARS) {
				// nein, Fehlertext setzen und Rückgabewert umsetzen 
				setErrorMessage("Wrong filetype, is not a Pokerstar handhistory.");
				ret = false;
			} else {
				// Typ stimmt, dann Fehlermeldung löschen und Rückgabewert umsetzen
				setErrorMessage(null);
				ret = true;
			}
		} catch (IOException e) {
			ErrorHandler.handleError(e, "Error checking filtetype", false);
		}
		
		return ret;		
	}
	
	/**
	 * Gibt den erfassten Dateipfad zurück
	 * @return Der Dateipfad
	 */
	public String getPath() {
		return tPfad.getText();
	}
}
