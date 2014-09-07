package osleditor.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class OSLWordDetector implements IWordDetector {

	public boolean isWordPart(char c)  {
		char[] special_chars = {',',';','.','{','}','(',')'};
		char[] operators = {'+','-','*','/','%','<','>','='};
		
		for (int i=0;i<special_chars.length;i++){
			if (c==special_chars[i]){
				return false;
			}			
		}
		
		for (int i=0;i<operators.length;i++){
			if (c==operators[i]){
				return false;
				
			}			
		}
		
		
		return !Character.isWhitespace(c);
		
		//return true;
	}
	
	public boolean isWordStart(char c)  {
		if (Character.isDigit(c)){
			return false;
		}
		char[] special_chars = {',',';','.','{','}','(',')'};
		char[] operators = {'+','-','*','/','%','<','>','='};
		
		for (int i=0;i<special_chars.length;i++){
			if (c==special_chars[i]){
				return false;
				
			}			
		}
		
		for (int i=0;i<operators.length;i++){
			if (c==operators[i]){
				return false;
				
			}			
		}
		return !Character.isWhitespace(c);

		//return true;
	}
}
