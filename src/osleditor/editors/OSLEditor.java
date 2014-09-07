package osleditor.editors;

import java.util.HashMap;
import java.util.ArrayList;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.jface.text.source.projection.*;
import org.eclipse.jface.text.source.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.IEditorInput;

public class OSLEditor extends TextEditor {

	private ProjectionSupport projectionSupport;
	private ProjectionAnnotationModel annotationModel;
	private Annotation[] oldAnnotations;
	
	private ColorManager colorManager;
	private OSLOutlinePage fOutlinePage;

	public OSLEditor() {
		super();
		
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new OSLConfiguration(colorManager,this));
	}
	
	public void dispose() {
		colorManager.dispose();
		if (fOutlinePage != null)
			fOutlinePage.setInput(null);
		super.dispose();
	}

	public void doRevertToSaved() {
		super.doRevertToSaved();
		if (fOutlinePage != null)
			fOutlinePage.update();
	}
	
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		if (fOutlinePage != null)
			fOutlinePage.update();
	}

	// Text folding
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles)
	{
	   ISourceViewer viewer = new ProjectionViewer(parent, ruler,
					getOverviewRuler(), isOverviewRulerVisible(), styles);
	
	   // ensure decoration support has been created and configured.
	   getSourceViewerDecorationSupport(viewer);
	   return viewer;
	}
	
	public void doSaveAs() {
		super .doSaveAs();
		if (fOutlinePage != null)
			fOutlinePage.update();
	}
	
	public void doSetInput(IEditorInput input) throws CoreException {
		super .doSetInput(input);
		if (fOutlinePage != null)
			fOutlinePage.setInput(input);
		
		
	}

    public void createPartControl(Composite parent)
    {
		
        super.createPartControl(parent);
        
        ProjectionViewer viewer =(ProjectionViewer)getSourceViewer();
        
        projectionSupport = new ProjectionSupport(viewer,getAnnotationAccess(),getSharedColors());
		projectionSupport.install();
		
		//turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);
		annotationModel = viewer.getProjectionAnnotationModel();
		
    }
	
	public void updateFoldingStructure(ArrayList positions)
	{
		
		Annotation[] annotations = new Annotation[positions.size()];
		
		//this will hold the new annotations along
		//with their corresponding positions
		HashMap newAnnotations = new HashMap();
		
		for(int i =0;i<positions.size();i++){
			ProjectionAnnotation annotation = new ProjectionAnnotation();
			newAnnotations.put(annotation,positions.get(i));
			annotations[i]=annotation;
		}
		
		annotationModel.modifyAnnotations(oldAnnotations,newAnnotations,null);
		oldAnnotations=annotations;
	}
	
	public Object getAdapter(Class required) {
		
		if (IContentOutlinePage.class.equals(required)) {
			if (fOutlinePage == null) 
				fOutlinePage= new OSLOutlinePage(getDocumentProvider(),this);
			if (getEditorInput() != null){
				fOutlinePage.setInput(getEditorInput());
			}
			return fOutlinePage;
		}
		
		return super.getAdapter(required);
	}
	
}
