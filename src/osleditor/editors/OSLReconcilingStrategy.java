package osleditor.editors;

import java.util.ArrayList;
import java.util.Stack;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.widgets.Display;

public class OSLReconcilingStrategy implements IReconcilingStrategy,
		IReconcilingStrategyExtension {

	private OSLEditor editor;
	private IDocument fDocument;
	
	protected int fOffset;
	protected int fRangeEnd;
	protected final ArrayList fPositions = new ArrayList();
	protected int cNextPos = 0;
    protected int cNewLines = 0;
	
	public void setProgressMonitor(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	
	public void initialReconcile() {
		fOffset = 0;
        fRangeEnd = fDocument.getLength();
        calculatePositions();
	}

	
	public void setDocument(IDocument document) {
		this.fDocument = document;
	}

	
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		initialReconcile();
	}

	
	public void reconcile(IRegion partition) {
		initialReconcile();
	}
	
	public void setEditor(OSLEditor editor) {
        this.editor = editor;
	}
	
	public OSLEditor getEditor(){
        return editor;
	}
	
	protected void calculatePositions() {
		
        fPositions.clear();
        cNextPos = fOffset;

        try {
                recursiveTokens(0);
        } catch (BadLocationException e) {
                e.printStackTrace();
        }
        // Collections.sort(fPositions, new RangeTokenComparator());

        Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                        editor.updateFoldingStructure(fPositions);
                }

        });
	}

	protected int recursiveTokens(int depth) throws BadLocationException {
		
		int newLines = 0;
		int startOffset = 0;
		Stack curlylevelsentry = new Stack();
		while (cNextPos < fRangeEnd) {
            while (cNextPos < fRangeEnd) {
                char ch = fDocument.getChar(cNextPos++);                    
                switch (ch) {
                case '{':             
                	curlylevelsentry.push(new Integer(cNextPos));
                	break;
                case '}':
                	startOffset = ((Integer)curlylevelsentry.pop()).intValue();
                	emitPosition(startOffset, cNextPos - startOffset);
                	break;
                	//emitPosition(startOffset, cNextPos - startOffset);
                	//emitPosition(startOffset, cNextPos - startOffset);
	            case '\n':
	            case '\r':
	                    break;
	            default:
	                    break;
                }
            }
		}
		return newLines;
	}
	
	protected void emitPosition(int startOffset, int length) {
        fPositions.add(new Position(startOffset, length));
	}
	
}

