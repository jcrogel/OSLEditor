package osleditor.editors;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class OSLScanner extends RuleBasedScanner {
	private WordRule keywordrule;
	

	public OSLScanner(ColorManager manager) {

		keywordrule =  new WordRule(new OSLWordDetector());
		
		
		IToken data_type_token = new Token(new TextAttribute(manager.getColor(IOSLColorConstants.DATA_TYPE)));
		IToken OSLComment = new Token(new TextAttribute(manager.getColor(IOSLColorConstants.OSL_COMMENT)));
		IToken OSLString = new Token(new TextAttribute(manager.getColor(IOSLColorConstants.STRING)));
		IToken OSLShaderType = new Token(new TextAttribute(manager.getColor(IOSLColorConstants.SHADERTYPE)));
		IToken OSLKeyWord = new Token(new TextAttribute(manager.getColor(IOSLColorConstants.KEYWORD)));
		IToken OSLProc_Inst = new Token(new TextAttribute(manager.getColor(IOSLColorConstants.PROC_INSTR)));
		
		
		// To be removed from here, goes in config file
		for (int i=0;i<OSLConstants.shader_declarations.length;i++) 
			keywordrule.addWord(OSLConstants.shader_declarations[i], OSLShaderType);
		for (int i=0;i<OSLConstants.type_declarations.length;i++) 
			keywordrule.addWord(OSLConstants.type_declarations[i], data_type_token);
		for (int i=0;i<OSLConstants.keyword_declarations.length;i++) 
			keywordrule.addWord(OSLConstants.keyword_declarations[i], OSLKeyWord);
		for (int i=0;i<OSLConstants.struct_declarations.length;i++) 
			keywordrule.addWord(OSLConstants.struct_declarations[i], OSLKeyWord);
		for (int i=0;i<OSLConstants.preprocessor_declarations.length;i++) 
			keywordrule.addWord(OSLConstants.preprocessor_declarations[i], OSLProc_Inst);		
		
		
		IRule[] rules = new IRule[6];
		rules[0] = new WhitespaceRule(new OSLWhitespaceDetector());
		rules[1] = keywordrule;
		rules[2] = (new EndOfLineRule("//",OSLComment));
		rules[3] = new SingleLineRule("\"", "\"", OSLString);
		rules[4] = new SingleLineRule("\'", "\'", OSLString);
		rules[5] = new MultiLineRule("/*", "*/", OSLComment);
		
		setRules(rules);
	}
	
    /*protected void readOSLEditorWordConfig(){
    	
    	
    }*/
}
