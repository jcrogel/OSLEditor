package osleditor.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;

public class OSLConfiguration extends SourceViewerConfiguration {
	private OSLDoubleClickStrategy doubleClickStrategy;
	private OSLScanner scanner;
	private ColorManager colorManager;
	private OSLEditor editor;
	
	OSLReconcilingStrategy strategy = new OSLReconcilingStrategy(); 

	public OSLConfiguration(ColorManager colorManager, OSLEditor editor) {
		this.colorManager = colorManager;
		this.editor = editor;
	}
	
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			};
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new OSLDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected OSLScanner getOSLScanner() {
		if (scanner == null) {
			scanner = new OSLScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IOSLColorConstants.DEFAULT))));
		}
		
		return scanner;
	}
	

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getOSLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		return reconciler;
	}
	
    public IReconciler getReconciler(ISourceViewer sourceViewer){
        OSLReconcilingStrategy strategy = new OSLReconcilingStrategy();
        strategy.setEditor(editor);
        MonoReconciler reconciler = new MonoReconciler(strategy,false);
        
        return reconciler;
    }

}