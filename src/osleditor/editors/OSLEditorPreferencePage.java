package osleditor.editors;

import org.eclipse.core.internal.content.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import java.util.Hashtable;

public class OSLEditorPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private Hashtable keywordList;


	protected void createFieldEditors() {
		//addField();
	}


	public void init(IWorkbench workbench) {		
	}
	
	public OSLEditorPreferencePage(){
		super(GRID);
		setPreferenceStore(osleditor.Activator.getDefault().getPreferenceStore());
		setDescription("OSL Editor Preferences");
	}
	

}
