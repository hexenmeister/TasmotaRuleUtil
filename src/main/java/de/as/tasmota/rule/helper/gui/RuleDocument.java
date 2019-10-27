package de.as.tasmota.rule.helper.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import de.as.tasmota.rule.helper.gui.RuleEditorPanel.UndoHandler;

public class RuleDocument extends DefaultStyledDocument {

    private Map<String, SimpleAttributeSet> keywordSets = new HashMap<String, SimpleAttributeSet>();

    private SimpleAttributeSet bold = new SimpleAttributeSet();
    private SimpleAttributeSet string = new SimpleAttributeSet();
    private SimpleAttributeSet normal = new SimpleAttributeSet();
    private SimpleAttributeSet number = new SimpleAttributeSet();
    private SimpleAttributeSet comments = new SimpleAttributeSet();

    private int currentPos = 0;

    public static int STRING_MODE = 10;
    public static int TEXT_MODE = 11;
    public static int NUMBER_MODE = 12;
    public static int COMMENT_MODE = 13;
    public static int LINE_COMMENT_MODE = 14;
    private int mode = TEXT_MODE;

    public RuleDocument() {
	initStyles();
	initKeywords();
    }

    private void initStyles() {
	StyleConstants.setBold(bold, true);
	StyleConstants.setForeground(string, Color.blue);
	StyleConstants.setForeground(number, new Color(63, 95, 191));
	StyleConstants.setForeground(comments, new Color(0x097D60));
	StyleConstants.setItalic(comments, true);
    }

    private static class KeywordStyle {
	Color color;
	boolean bold = false;
	boolean italic = false;

	public KeywordStyle(Color color, boolean bold, boolean italic) {
	    this.color = color;
	    this.bold = bold;
	    this.italic = italic;
	}
    }

    public void setKeywords(Map<String, KeywordStyle> mapKeywords) {
	if (mapKeywords != null) {
	    for (Map.Entry<String, KeywordStyle> entry : mapKeywords.entrySet()) {
		SimpleAttributeSet temp = new SimpleAttributeSet();
		StyleConstants.setForeground(temp, entry.getValue().color);
		StyleConstants.setBold(temp, entry.getValue().bold);
		StyleConstants.setItalic(temp, entry.getValue().italic);
		this.keywordSets.put(entry.getKey(), temp);
	    }
	}
    }

    private void initKeywords() {
	Map<String, KeywordStyle> keywords = new HashMap<String, KeywordStyle>();

	Color defColor = new Color(127, 0, 85);
	Color cmdsColor = new Color(127, 0, 85);
	Color varColor = new Color(63, 95, 191);
	Color memColor = new Color(0xBF7600);

	keywords.put("on", new KeywordStyle(defColor, true, false));
	keywords.put("do", new KeywordStyle(defColor, true, false));
	keywords.put("endon", new KeywordStyle(defColor, true, false));
	keywords.put("if", new KeywordStyle(defColor, true, false));
	keywords.put("else", new KeywordStyle(defColor, true, false));
	keywords.put("elseif", new KeywordStyle(defColor, true, false));
	keywords.put("endif", new KeywordStyle(defColor, true, false));
	keywords.put("backlog", new KeywordStyle(cmdsColor, false, false));
	keywords.put("var1", new KeywordStyle(varColor, false, false));
	keywords.put("var2", new KeywordStyle(varColor, false, false));
	keywords.put("var3", new KeywordStyle(varColor, false, false));
	keywords.put("var4", new KeywordStyle(varColor, false, false));
	keywords.put("var5", new KeywordStyle(varColor, false, false));
	keywords.put("value", new KeywordStyle(varColor, false, false));
	keywords.put("mem1", new KeywordStyle(memColor, false, false));
	keywords.put("mem2", new KeywordStyle(memColor, false, false));
	keywords.put("mem3", new KeywordStyle(memColor, false, false));
	keywords.put("mem4", new KeywordStyle(memColor, false, false));
	keywords.put("mem5", new KeywordStyle(memColor, false, false));
	keywords.put("or", new KeywordStyle(defColor, false, false));
	keywords.put("and", new KeywordStyle(defColor, false, false));

	this.setKeywords(keywords);
    }

    private void insertKeyword(String str, int pos, SimpleAttributeSet as) {
	try {
	    // remove the old word and formatting
//	    this.remove(pos - str.length(), str.length());
//	    /*
//	     * replace it with the same word, but new formatting we MUST call the super
//	     * class insertString method here, otherwise we would end up in an infinite
//	     * loop!!
//	     */
//	    super.insertString(pos - str.length(), str, as);
	    super.setCharacterAttributes(pos-str.length(), str.length(), as, false);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void insertTextString(String str, int pos) {
	try {
	    // remove the old word and formatting
//	    this.remove(pos, str.length());
//	    super.insertString(pos, str, string);
	    super.setCharacterAttributes(pos-str.length(), str.length(), string, true);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void insertNumberString(String str, int pos) {
	try {
	    // remove the old word and formatting
//	    this.remove(pos, str.length());
//	    super.insertString(pos, str, number);
	    super.setCharacterAttributes(pos-str.length(), str.length(), number, true);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void insertCommentString(String str, int pos) {
	try {
	    // remove the old word and formatting
//	    this.remove(pos, str.length());
//	    super.insertString(pos, str, comments);
	    super.setCharacterAttributes(pos-str.length(), str.length(), comments, true);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void checkForString() {
	int offs = this.currentPos;
	Element element = this.getParagraphElement(offs);
	String elementText = "";
	try {
	    // this gets our chuck of current text for the element we're on
	    elementText = this.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	int strLen = elementText.length();
	if (strLen == 0) {
	    return;
	}
	int i = 0;

	if (element.getStartOffset() > 0) {
	    // translates backward if neccessary
	    offs = offs - element.getStartOffset();
	}
	int quoteCount = 0;
	if ((offs >= 0) && (offs <= strLen - 1)) {
	    i = offs;
	    while (i > 0) {
		// the while loop walks back until we hit a delimiter

		char charAt = elementText.charAt(i);
		if ((charAt == '"')) {
		    quoteCount++;
		}
		i--;
	    }
	    int rem = quoteCount % 2;
	    // System.out.println(rem);
	    mode = (rem == 0) ? TEXT_MODE : STRING_MODE;
	}
    }

    private void checkForKeyword() {
	if (mode != TEXT_MODE) {
	    return;
	}
	int offs = this.currentPos;
	Element element = this.getParagraphElement(offs);
	String elementText = "";
	try {
	    // this gets our chuck of current text for the element we're on
	    elementText = this.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
	} catch (Exception ex) {
	    // whoops!
	    System.out.println("no text");
	}
	int strLen = elementText.length();
	if (strLen == 0) {
	    return;
	}
	int i = 0;

	if (element.getStartOffset() > 0) {
	    // translates backward if neccessary
	    offs = offs - element.getStartOffset();
	}
	if ((offs >= 0) && (offs <= strLen - 1)) {
	    i = offs;
	    while (i > 0) {
		// the while loop walks back until we hit a delimiter
		i--;
		char charAt = elementText.charAt(i);
		if ((i == 0) | " (){}%".indexOf(charAt) >= 0) {
		    // if i == 0 then we're at the begininng
		    if (i != 0) {
			i++;
		    }
		    String word = elementText.substring(i, offs);// skip the period

		    String s = word.trim().toLowerCase();
		    // this is what actually checks for a matching keyword
		    if (keywordSets.containsKey(s)) {
			insertKeyword(word, currentPos, keywordSets.get(s));
		    }
		    break;
		}
	    }
	}
    }

    private void checkForNumber() {
	int offs = this.currentPos;
	Element element = this.getParagraphElement(offs);
	String elementText = "";
	try {
	    // this gets our chuck of current text for the element we're on
	    elementText = this.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
	} catch (Exception ex) {
	    // whoops!
	    System.out.println("no text");
	}
	int strLen = elementText.length();
	if (strLen == 0) {
	    return;
	}
	int i = 0;

	if (element.getStartOffset() > 0) {
	    // translates backward if neccessary
	    offs = offs - element.getStartOffset();
	}
	mode = TEXT_MODE;
	if ((offs >= 0) && (offs <= strLen - 1)) {
	    i = offs;
	    while (i > 0) {
		// the while loop walks back until we hit a delimiter
		char charAt = elementText.charAt(i);
		if ((i == 0) | " (){}<>=!|".indexOf(charAt) >= 0) {
		    // if i == 0 then we're at the begininng
		    if (i != 0) {
			i++;
		    }
		    mode = NUMBER_MODE;
		    break;
//		} else if (!(charAt >= '0' & charAt <= '9' | charAt == '.' | charAt == '+' | charAt == '-' | charAt == '/' | charAt == '*' | charAt == '%' | charAt == '=')) {
		} else if (!("0123456789.+-/*%=".indexOf(charAt) >= 0)) {
		    mode = TEXT_MODE;
		    break;
		}
		i--;
	    }
	}
    }

    private void checkForComment() {
	int offs = this.currentPos;
	Element element = this.getParagraphElement(offs);
	String elementText = "";
	try {
	    // this gets our chuck of current text for the element we're on
	    elementText = this.getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
	} catch (Exception ex) {
	    // whoops!
	    System.out.println("no text");
	}
	int strLen = elementText.length();
	if (strLen == 0) {
	    return;
	}
	int i = 0;

	if (element.getStartOffset() > 0) {
	    // translates backward if neccessary
	    offs = offs - element.getStartOffset();
	}
	if ((offs >= 1) && (offs <= strLen - 1)) {
	    i = offs;
	    char commentStartChar1 = elementText.charAt(i - 1);
	    char commentStartChar2 = elementText.charAt(i);
	    if (commentStartChar1 == '/' && commentStartChar2 == '*') {
		mode = COMMENT_MODE;
		this.insertCommentString("/*", currentPos - 1);
	    } else if (commentStartChar1 == '/' && commentStartChar2 == '/') {
		mode = LINE_COMMENT_MODE;
		this.insertCommentString("//", currentPos - 1);
	    } else if (commentStartChar1 == '*' && commentStartChar2 == '/') {
		mode = TEXT_MODE;
		this.insertCommentString("*/", currentPos - 1);
	    }
	}
    }

    private void processChar(char strChar) {
	char[] chrstr = new char[1];
	chrstr[0] = strChar;
	String str = new String(chrstr);

	if (mode != COMMENT_MODE && mode != LINE_COMMENT_MODE) {
	    mode = TEXT_MODE;
	}

	switch (strChar) {
	case ('{'):
	case ('}'):
	case (' '):
	case ('\n'):
	case ('('):
	case (')'):
	case (';'):
	case ('.'):
	case ('<'):
	case ('>'):
	case ('='):
	case ('!'):
	case ('|'):
	case ('%'): {
	    checkForKeyword();
	    if ((mode == STRING_MODE || mode == LINE_COMMENT_MODE) && strChar == '\n') {
		mode = TEXT_MODE;
	    }
	}
	    break;

	case ('"'): {
	    insertTextString(str, currentPos);
	    this.checkForString();
	}
	    break;

	case ('*'):
	case ('/'): {
	    checkForComment();
	}
	    break;

	case ('0'):
	case ('1'):
	case ('2'):
	case ('3'):
	case ('4'):
	case ('5'):
	case ('6'):
	case ('7'):
	case ('8'):
	case ('9'): {
	    if (mode != LINE_COMMENT_MODE) {
		checkForNumber();
	    }
	}
	    break;

	}
	if (mode == TEXT_MODE) {
	    this.checkForString();
	}
	if (mode == STRING_MODE) {
	    insertTextString(str, this.currentPos);
	} else if (mode == NUMBER_MODE) {
	    insertNumberString(str, this.currentPos);
	} else if (mode == COMMENT_MODE) {
	    insertCommentString(str, this.currentPos);
	} else if (mode == LINE_COMMENT_MODE) {
	    insertCommentString(str, this.currentPos);
	}
    }

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
	super.insertString(offs, str, normal);

	if(undoHandler!=null) {
	    undoHandler.setEnabled(false);
	}
	
	int strLen = str.length();
	int endpos = offs + strLen;
	int strpos;
	for (int i = offs; i < endpos; i++) {
	    currentPos = i;
	    strpos = i - offs;
	    processChar(str.charAt(strpos));
	}
	currentPos = offs;
	
	if(undoHandler!=null) {
	    undoHandler.setEnabled(true);
	}
    }
    
    protected UndoHandler undoHandler = null;
}