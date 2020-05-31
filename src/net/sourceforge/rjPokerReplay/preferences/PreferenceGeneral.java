package net.sourceforge.rjPokerReplay.preferences;

import net.sourceforge.rjPokerReplay.Activator;
import net.sourceforge.rjPokerReplay.ApplicationWorkbenchAdvisor;
import net.sourceforge.rjPokerReplay.views.ViewTable;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferencePage;


  public class PreferenceGeneral extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public PreferenceGeneral() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.PreferenceGeneral_0);
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		// Feld für die Zeit beim Autoplay
		addField(
				new IntegerFieldEditor(PreferenceConstants.P_GENERAL_TIME_DELAY, Messages.PreferenceGeneral_1, getFieldEditorParent()));
		
		// Feld für das Bild für den Tisch
		addField(
				new FileFieldEditor(PreferenceConstants.P_GENERAl_TABLE_IMAGE, Messages.PreferenceGeneral_2, getFieldEditorParent()));
		
		// Feld für das Verzeichnis in dem die Bilder mit den Karten liegen
		addField(
				new DirectoryFieldEditor(PreferenceConstants.P_GENERAl_CARD_DIR, Messages.PreferenceGeneral_3, getFieldEditorParent()));
	}
	
	/**
	 * Aktionen wenn die OK-Taste gedrueckt wird
	 */
	@Override
	public boolean performOk() {
		super.performOk();
		
		// Die Bilder neu laden
		ApplicationWorkbenchAdvisor.initImageStore();
		
		// Tisch neu zeichnen
		IWorkbenchPage activPage = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
    	ViewTable tableView = null;
    	if (activPage != null) {
    		tableView = (ViewTable)(activPage.findView(ViewTable.ID));
    	}
    	if (tableView != null) {
    		tableView.drawTable();
    	}
		return true;
	}

}
