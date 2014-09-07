package osleditor.editors;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.contentoutline.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.text.TextSelection;

public class OSLOutlinePage extends ContentOutlinePage {
	protected OSLEditor editor;
	protected Object fInput;
	protected IDocumentProvider fDocumentProvider;
	
	protected class OSLCOPMouseListener extends MouseAdapter{
		public void mouseDoubleClick(MouseEvent e){
			Tree sourcectrl = (Tree) e.getSource();
			//System.out.println(sourcectrl.getSelection();
			TreeItem selitem = sourcectrl.getSelection()[0];
			OSLContentProvider.Segment caughtsgement =  (OSLContentProvider.Segment) selitem.getData();
			ISelectionProvider selprovider = caughtsgement.getEditor().getSelectionProvider();
			TextSelection selection = new TextSelection(caughtsgement.getDocument(),caughtsgement.position.offset,caughtsgement.position.length);
			selprovider.setSelection(selection);
		}
	}
	
	public OSLOutlinePage(IDocumentProvider provider,OSLEditor editor) {
		super();
		this.fDocumentProvider = provider;
		this.editor = editor;
	}
	
	public void createControl(Composite parent) {
		//System.out.println("parent:"+parent.getClass().hashCode());
		super.createControl(parent);
		this.getControl().addMouseListener(new OSLCOPMouseListener());

		TreeViewer viewer= getTreeViewer();
		OSLContentProvider contentprovider = new OSLContentProvider();
		contentprovider.setEditor(this.editor);
		//if (fDocumentProvider!= null)
		contentprovider.setDocumentProvider(fDocumentProvider);
		viewer.setContentProvider(contentprovider);
		viewer.setLabelProvider(new LabelProvider());
		viewer.addSelectionChangedListener(this);
		
		if (fInput != null){
			viewer.setInput(fInput);
			((OSLContentProvider)viewer.getContentProvider()).setInput(fInput);
		}
			
	}
	
	public void setInput(Object input) {
		fInput = input;
		update();            
	}
	
	public void update(){
		TreeViewer viewer = getTreeViewer();
		if (viewer != null){
			Control control = viewer.getControl();
			if (control != null && !control.isDisposed()) {
				control.setRedraw(false);
				viewer.setInput(fInput);
				((OSLContentProvider)viewer.getContentProvider()).setInput(fInput);
				viewer.expandAll();
				control.setRedraw(true);
			}
		}
	}

}
