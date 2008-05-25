package rjPokerReplay.preferences;

import language.Messages;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import rjPokerReplay.Activator;

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
		addField(
				new IntegerFieldEditor(PreferenceConstants.P_GENERAL_TIME_DELAY, Messages.PreferenceGeneral_1, getFieldEditorParent()));
		
	}

}
