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

import io.gitlab.lipor.Activator;
import io.gitlab.lipor.ApplicationWorkbenchAdvisor;
import io.gitlab.lipor.cardstuff.Handhistory;
import io.gitlab.lipor.preferences.PreferenceConstants;
import io.gitlab.lipor.util.ErrorHandler;
import io.gitlab.lipor.util.FileUtil;


public class SelectFile extends WizardPage {

	// Textfeld mit Dateipfad
	private Text tPfad;
	
	/**
	 * Konstruktor
	 * 
	 * @param pageName Name (Ueberschrift) der Wizardseiten
	 */
	protected SelectFile(String pageName) {
		super(pageName);
        setTitle(Messages.SelectFile_0);
        setDescription(Messages.SelectFile_1);
        setPageComplete(false);
	}

	/**
	 * Seitenelemente hinzufuegen
	 */
	public void createControl(Composite parent) {
		// Container fuer die Elemente
		Composite container = new Composite(parent, SWT.NULL);
		
		// Das Layout festlegen
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        container.setLayout(layout);
        
        // Label fuer den Dateipfad
        Label lPfad = new Label(container, SWT.NULL);
        lPfad.setText(Messages.SelectFile_2);
        
        // Ein Textfeld fuer den Pfad
        tPfad = new Text(container, SWT.LEFT);
        GridData gridData = new GridData();
		gridData.widthHint = 400;
		tPfad.setLayoutData(gridData);
		
		// Listener fuer Aenderungne am Text
		tPfad.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// pruefen ob Eingabe vollstaendig und plausibel
				isPageComplete();
				getContainer().updateButtons();
			}
		});
		
		// ein Button um den Pfad zu waehlen
		Button bWaehlen = new Button(container, SWT.ICON);
		try {
			// dem Button ein Bild hinzufuegen
			bWaehlen.setImage(ApplicationWorkbenchAdvisor.getImageStore().get(io.gitlab.lipor.Constants.IMG_OPEN_FOLDER));
		} catch (Exception e) {
			// wenn das mit dem Bild nicht ging, einen Text waehlen
			bWaehlen.setText(Messages.SelectFile_3);
		}
		// Und eine Listener fuer die Wahltaste
		bWaehlen.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectPfadDialog();
				getContainer().updateButtons();
			}
		});
        // Versuchen wir ein Bild die Waehlentaste zu laden
        setControl(container);
	}

	/**
	 * Dialog zur Dateiauswahl anzeigen
	 */
	protected void selectPfadDialog() {
		// Den Diaog oeffnen
		FileDialog dia = new  FileDialog(getShell(), SWT.OPEN);
		
		// Pfad fuer vorgeinstelltes Verzeichnis setzen
		dia.setFilterPath(Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_WIN2DAY_PATH));
		
		// Dialog anzeigen
		String pfad = dia.open();
		
		// Wenn ein Pfad eingegeben wurde ... 
		if (pfad != null) {
			// Den Pfad in das Textfeld uebertragen
			tPfad.setText(pfad);
		}
		
	}
	
	/**
	 * Prueft ob alle Eingaben im Dialog gemacht wurden und sinnvoll sind
	 */
	public boolean isPageComplete() {
		boolean ret = false;
		
		// wurde ein Dateinameerfasst
		if (FileUtil.checkFileExists(tPfad.getText())) {
			// ja, dann Fehlermeldung loeschen und Rueckgabewert auf vollstaendig setzen
			setErrorMessage(null);
			ret = true;
		} else {
			// nein, Fehlermeldung ausgeben
			setErrorMessage(Messages.SelectFile_4);
		}
		
		// Pruefen ob der Daeityp eine Pokerstars-Handhistorie-Datei ist
		try {
			if (ret && Handhistory.getKind(tPfad.getText()) != Handhistory.WIN2DAY) {
				// nein, Fehlertext setzen und Rueckgabewert umsetzen 
				setErrorMessage(Messages.SelectFile_5);
				ret = false;
			} else {
				// Typ stimmt, dann Fehlermeldung loeschen und Rueckgabewert umsetzen
				setErrorMessage(null);
				ret = true;
			}
		} catch (IOException e) {
			ErrorHandler.handleError(e, Messages.SelectFile_6, false);
		}
		
		return ret;		
	}
	
	/**
	 * Gibt den erfassten Dateipfad zurueck
	 * @return Der Dateipfad
	 */
	public String getPath() {
		return tPfad.getText();
	}
}
