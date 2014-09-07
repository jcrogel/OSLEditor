package osleditor.editors;

import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.jface.text.*;
import java.util.LinkedList;
import java.util.Iterator;

import java.util.List;
import java.util.LinkedList;
import org.eclipse.jface.text.IDocument;

public class OSLContentProvider extends TreeNodeContentProvider {
	protected IDocumentProvider fDocumentProvider;
	protected LinkedList fContent = new LinkedList();
	protected final static String SEGMENTS = "__osl_segments"; //$NON-NLS-1$
	protected IPositionUpdater fPositionUpdater = new DefaultPositionUpdater(SEGMENTS);
	protected Object fInput;
	protected OSLEditor editor;
	
	protected static class Segment {
        public String name;
        public Position position;
        public OSLEditor editor;
        public IDocument document;

        public Segment(String name, Position position,OSLEditor editor,IDocument doc) {
            this.name = name;
            this.position = position;
            this.editor = editor;
            this.document = doc;
            
        }

        public OSLEditor getEditor(){
        	return this.editor;
        }
        
        public IDocument getDocument(){
        	return this.document;
        }
        
        public String toString() {
            return name;
        }
    }
	
	public void dispose() {
		if (fContent != null) {
			fContent.clear();
			fContent = null;
		}
	}

	public void setEditor(OSLEditor editor){
		this.editor = editor;
	}
	
	public void setDocumentProvider(IDocumentProvider docpro){
		this.fDocumentProvider = docpro;
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		//System.out.println(newInput.getClass().getName());
		if (oldInput!=null){
			IDocument document = fDocumentProvider.getDocument(oldInput);
			
			if (document != null) {
				try {
					document.removePositionCategory(SEGMENTS);
				} catch (BadPositionCategoryException x) {
				}
				document.removePositionUpdater(fPositionUpdater);
			}
		}
		
		fContent.clear();
		
		if (newInput != null) {
			if (fDocumentProvider!=null){
				if (fDocumentProvider!=null){
					IDocument document = fDocumentProvider.getDocument(newInput);
					if (document != null) {
						document.addPositionCategory(SEGMENTS);
						document.addPositionUpdater(fPositionUpdater);
						parse(document);
					}
				}
			}
		}
	}

	protected void parse(IDocument document) {
	    OSLWordDetector worddetector = new OSLWordDetector();
	    LinkedList<String> statementwords = new LinkedList<String>();
	    
	    try{
		    int offset = 0;
		    int lineno = 1;
		    String prevword = "";
		    for(int i=0;i<document.getLength();i++){
		    	if(worddetector.isWordStart(document.getChar(i))){
		    		while(worddetector.isWordPart(document.getChar(i))){
		    			i++;
		    		}
		    		prevword = document.get(offset, (i-offset)).trim();
		    		statementwords.add(prevword);
		    		i--;
		    	}else{
		    		if(document.getChar(i)==';' || document.getChar(i)=='{'){
		    			String statement=isStatement(statementwords);
		    			if (statement!=null){
			    			int length =  (i-offset);
			    			lineno = document.getLineOfOffset(i)+1;
			    			Position p = new Position(offset, length);
			    			document.addPosition(SEGMENTS, p);
			    			fContent.add(new Segment(statement,p,this.editor,document));
		    			}
		    			statementwords = new LinkedList<String>();
		    			
		    		}else if(document.getChar(i)=='/'){
		    			i++;
		    			if(document.getChar(i)=='/'){
		    				while(document.getChar(i)!='\n'){
		    					i++;
		    				}
		    			}else if(document.getChar(i)=='*'){
		    				i++;
		    				while(document.getChar(i)!='*' && document.getChar(i+1)!='/' ){
		    					i++;
		    				}
		    				i++;
		    			}
		    		}
		    		
		    	}
		    	offset = i;
		    }
	    }catch(Exception e){
	    	
	    }
	}
	
	public String isStatement(LinkedList<String> statements){
		String found=isShaderDeclaration(statements);
		if(found!=null){
			return "Shader:"+found;
		}
		
		found=isStructDeclaration(statements);
		if(found!=null){
			return "Struct:"+found;
		}
		
		found=isFunctionDeclaration(statements);
		if(found!=null){
			return "Function:	"+found;
		}
		return null;
	}
	
	public String isShaderDeclaration(LinkedList<String> statements){
		Iterator iter = statements.iterator();
		String sd = (String)iter.next();
		
		String[] shader_declarations=OSLConstants.shader_declarations;
		
		
		for (int i=0;i<shader_declarations.length;i++){
			if (sd.equals(shader_declarations[i])){
				if (iter.hasNext()){
					return (String) iter.next();
				}
			}
		}
		
		return null;
	}
	
	public String isStructDeclaration(LinkedList<String> statements){
		Iterator iter = statements.iterator();
		String sd = (String)iter.next();
		
		String[] struct_declarations=OSLConstants.struct_declarations;	
		
		for (int i=0;i<struct_declarations.length;i++){
			if (sd.equals(struct_declarations[i])){
				if (iter.hasNext()){
					return (String) iter.next();
				}
			}
		}
		
		return null;
	}

	public String isFunctionDeclaration(LinkedList<String> statements){
		return null;
	}
	// Added extra
	
	public void setInput(Object input) {
		fInput = input;            
	}
	
	public Object[] getChildren(Object element) {
        if (element == fInput)
            return fContent.toArray();
        return new Object[0];
    }
	
	public Object getParent(Object element) {
		if (element instanceof  Segment)
			return fInput;
		return null;
	}
	
	public boolean hasChildren(Object element) {
		return element == fInput;
	}
	
	public Object[] getElements(Object element) {
		return fContent.toArray();
	}
	
	public boolean isDeleted(Object element) {
		return false;
	}
	
}
