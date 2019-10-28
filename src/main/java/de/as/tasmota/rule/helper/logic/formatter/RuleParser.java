package de.as.tasmota.rule.helper.logic.formatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RuleParser {

    // private static String reComparison = "(=|==|>|<|!=|>=|<=|\\|)";

    public static abstract class Part {
	protected List<Part> children;

	protected Part() {
	    this.children = new ArrayList<>();
	}

	public List<Part> getChildren() {
	    return this.children;
	}

	protected void addChild(Part part) {
	    this.children.add(part);
	}

	protected void skipSpaces(List<String> in) {
	    while (!in.isEmpty() && in.get(0).trim().isEmpty()) {
		in.remove(0);
	    }
	}

	public abstract boolean accept(List<String> in);

	@Override
	public String toString() {
	    return this.write();
	}

	public abstract String write();

	public final String writeFormated() {
	    return writeFormatedIntern(0);
	}

	protected abstract String writeFormatedIntern(int indent);

	protected String writeChildren() {
	    return this.writeChildren(" ");
	}

	protected String writeChildren(String dlm) {
	    return this.children.stream().map(Part::write).filter(Objects::nonNull).collect(Collectors.joining(dlm));
	}

	protected String writeChildrenFormated(int indent) {
	    return this.writeChildrenFormated(indent, "");
	}

	protected String writeChildrenFormated(int indent, String dlm) {
	    return this.children.stream().map(x -> x.writeFormatedIntern(indent)).collect(Collectors.joining(dlm));
	}

	protected String getIndentString(int indent) {
	    StringBuilder sb = new StringBuilder();
	    if (indent > 0) {
		for (int i = 0; i < indent; i++) {
		    sb.append("    ");
		}
	    }
	    return sb.toString();
	}

	protected String searchForLineComment(List<String> in) {
	    skipSpaces(in);
	    if (!in.isEmpty() && in.get(0).trim().startsWith("//")) {
		in.remove(0);
		StringBuilder sb = new StringBuilder();
		while (!in.isEmpty() && (!in.get(0).contains("\r") && !in.get(0).contains("\n"))) {
		    sb.append(in.remove(0));
		}
		// if (in.get(0).trim().isEmpty()) {
		if (!in.isEmpty()) {
		    in.remove(0);
		}
		// }
		return sb.toString();
	    }
	    return null;
	}
    }

    public static class RuleScript extends Part {

	List<String> unparsed = null;

	public List<String> getUnparsed() {
	    return this.unparsed;
	}

	public String getUnparsedText() {
	    return this.getUnparsed().stream().collect(Collectors.joining());
	}

	@Override
	public boolean accept(List<String> in) {
	    boolean found = false;
	    skipSpaces(in);
	    while (!in.isEmpty()) {
		found = false;
		Part p;
		if ((p = new RuleBlock()).accept(in)) {
		    this.addChild(p);
		    found = true;
		} else if ((p = new CmdOnDo()).accept(in)) {
		    this.addChild(p);
		    found = true;
		} else if ((p = new CommentLine()).accept(in)) {
		    this.addChild(p);
		    found = true;
		}
		if (!found) {
		    if (!in.isEmpty()) {
			// unexpected word
			String part = in.get(0);
			System.out.println("unexpected command: " + part);// TODO
		    }
		    break;
		} else {
		    skipSpaces(in);
		}
	    }
	    skipSpaces(in);
	    this.unparsed = in;
	    return found;
	}

	@Override
	public String write() {
	    return this.writeChildren();
	}

	@Override
	protected String writeFormatedIntern(int indent) {
	    return this.writeChildrenFormated(indent);
	}
    }

    public static class RuleBlock extends Part {
	private String blockName = "";
	private String comment = "";

	private boolean newBlock(List<String> in) {
	    return !in.isEmpty() && in.get(0).equals("[");
	}

	public boolean accept(List<String> in) {
	    boolean found = false;
	    skipSpaces(in);
	    if (in.isEmpty()) {
		return false;
	    }
	    if (newBlock(in)) {
		StringBuilder sb = new StringBuilder();
		in.remove(0);
		while (!in.isEmpty() && !in.get(0).trim().equals("]")) {
		    sb.append(in.remove(0));
		}
		in.remove(0);
		this.blockName = sb.toString();
	    } else {
		return false;
	    }
	    skipSpaces(in); // TODO: Ändern: nur Leerzeichen, nicht Zeilenumbrüche!
	    this.comment = searchForLineComment(in);
	    while (!in.isEmpty() && !newBlock(in)) {
		found = false;
		Part p;
		if ((p = new CmdOnDo()).accept(in)) {
		    this.addChild(p);
		    found = true;
		} else if ((p = new CommentLine()).accept(in)) {
		    this.addChild(p);
		    found = true;
		}
		if (!found) {
		    break;
		} else {
		    skipSpaces(in);
		}
	    }
	    return true; // return found;
	}

	@Override
	public String write() {
	    return write(true);
	}

	public String write(boolean withName) {
	    return (withName ? "\r\n[" + this.blockName + "]\r\n" : "") + this.writeChildren();
	}

	public String writeFormatedIntern(int indent) {
	    return writeFormatedIntern(true, indent);
	}

	public String writeFormatedIntern(boolean withName, int indent) {
	    String comm = this.comment != null ? " //" + this.comment : "";
	    return (withName ? "[" + this.blockName + "]" : "") + comm + "\r\n" + this.writeChildrenFormated(indent);
	}

	public String getName() {
	    return this.blockName;
	}
    }

    static class CommentLine extends Part {
	private String comment = "";

	public boolean accept(List<String> in) {
	    skipSpaces(in);
	    if (in.isEmpty()) {
		return false;
	    }
	    if (in.get(0).trim().equals("//")) {
		StringBuilder sb = new StringBuilder();
		in.remove(0);
		while (!in.isEmpty() && !in.get(0).equals("\r") && !in.get(0).equals("\n")) {
		    sb.append(in.remove(0));
		}
		if (!in.isEmpty()) {
		    in.remove(0);
		}
		this.comment = sb.toString();
		return true;
	    }
	    return false;
	}

	public String write() {
	    return null;// ""; //" //" + this.comment + "\r\n";
	}

	public String writeFormatedIntern(int indent) {
	    String space = getIndentString(indent);
	    return space + "//" + this.comment + "\r\n";
	}
    }

    static class CmdOnDo extends Part {

	private String trigger = "";
	private String error = "";
	private String commentDo = null;
	private String commentEnd = null;
	private boolean endBreak = false;
	private boolean foundDo = false;
	private boolean foundEnd = false;

	public boolean accept(List<String> in) {
	    boolean found = false;
	    skipSpaces(in);
	    if (in.isEmpty()) {
		return false;
	    }

	    if (in.get(0).equalsIgnoreCase("on")) {
		StringBuilder sb = new StringBuilder();
		in.remove(0);
		// search 'do', get trigger
		while (!in.isEmpty() && !in.get(0).equalsIgnoreCase("do")) {
		    sb.append(in.remove(0));
		}
		if (!in.isEmpty()) {
		    found = in.get(0).equalsIgnoreCase("do");
		    foundDo = found;
		    if (found) {
			in.remove(0);
		    }
		}
		this.trigger = sb.toString().trim();
		if (!found) {
		    this.error = "on without do";
		    return true;
		} else {
		    this.commentDo = searchForLineComment(in);
		    skipSpaces(in);
		}

		// search 'endon'
		while (!in.isEmpty() && !in.get(0).equalsIgnoreCase("endon") && !in.get(0).equalsIgnoreCase("break")) {
		    found = false;
		    Part p;
		    if ((p = new CmdIf()).accept(in)) {
			this.addChild(p);
			found = true;
		    } else if ((p = new CommentLine()).accept(in)) {
			this.addChild(p);
			found = true;
		    } else if ((p = new CmdBacklog()).accept(in)) {
			this.addChild(p);
			found = true;
		    } else if ((p = new CmdGeneric()).accept(in)) {
			this.addChild(p);
			found = true;
		    }
		    if (!found) {
			break;
		    } else {
			skipSpaces(in);
		    }
		}
		found = false;
		if (!in.isEmpty()) {
		    found = in.get(0).equalsIgnoreCase("endon");
		    if (!found) {
			found = in.get(0).equalsIgnoreCase("break");
			this.endBreak = true;
		    }
		    foundEnd = found;
		    if (found) {
			in.remove(0);
		    }
		}
		if (!found) {
		    this.error = "on without endon";
		    return true;
		} else {
		    this.commentEnd = searchForLineComment(in);
		    skipSpaces(in);
		}
		return true;
	    }

	    return false;
	}

	public String write() {
	    return "on " + this.trigger + (this.foundDo ? " do " : "") + this.writeChildren()
		    + (this.foundEnd ? (this.endBreak ? " break" : " endon") : "");
	}

	public String writeFormatedIntern(int indent) {
	    String space = getIndentString(indent);
	    String commDo = this.commentDo != null ? " //" + this.commentDo : "";
	    String commEn = this.commentEnd != null ? " //" + this.commentEnd : "";
	    return space + "on " + this.trigger + (this.foundDo ? (" do" + commDo) : "") + "\r\n"
		    + this.writeChildrenFormated(indent + 1)
		    // + "\r\n"
		    + (this.foundEnd ? (space + (this.endBreak ? "break" : "endon") + commEn + "\r\n\r\n") : "");
	}
    }

    static class CmdElseIf extends CmdIf {
	protected CmdElseIf() {
	    super("elseif", true, true);
	}
    }

    static class CmdElse extends CmdIf {
	protected CmdElse() {
	    super("else", false, true);
	}
    }

    static class CmdIf extends Part {
	private String condition = "";
	private String error = "";
	private String commentIf = null;
	private String commentEnd = null;
	private String start = null;
	boolean hasCondition = false;
	boolean secuence = false; // Folge Part (elseif, else)

	protected CmdIf() {
	    this("if", true, false);
	}

	protected CmdIf(String start, boolean hasCondition, boolean secuence) {
	    super();
	    this.start = start;
	    this.hasCondition = hasCondition;
	    this.secuence = secuence;
	}

	public boolean accept(List<String> in) {
	    boolean found = false;
	    skipSpaces(in);
	    if (in.isEmpty()) {
		return false;
	    }

	    if (in.get(0).equalsIgnoreCase(this.start)) {
		StringBuilder sb = new StringBuilder();
		in.remove(0);
		skipSpaces(in);

		if (hasCondition) {
		    // search last ')', get condition
		    int pcnt = 0;
		    if (!in.isEmpty() && !in.get(0).equals("(")) {
			this.error = "found no condition in " + this.start;
			return true;
		    }
		    while (!in.isEmpty()) {
			if (in.get(0).equals("(")) {
			    pcnt++;
			}
			if (in.get(0).equals(")")) {
			    pcnt--;
			}
			sb.append(in.remove(0));
			if (pcnt == 0) {
			    break;
			}
		    }
		    this.condition = sb.toString().trim();
		}
		this.commentIf = searchForLineComment(in);

		skipSpaces(in);

		// search 'endif, else, elseif'
		while (!in.isEmpty() && !in.get(0).equalsIgnoreCase("endif")) {
		    found = false;
		    Part p;
		    if ((p = new CmdIf()).accept(in)) {
			this.addChild(p);
			found = true;
		    } else if ((p = new CmdElseIf()).accept(in)) {
			this.addChild(p);
			found = true;
		    } else if ((p = new CmdElse()).accept(in)) {
			this.addChild(p);
			found = true;
		    } else if ((p = new CommentLine()).accept(in)) {
			this.addChild(p);
			found = true;
		    } else if ((p = new CmdBacklog()).accept(in)) {
			this.addChild(p);
			found = true;
		    } else if ((p = new CmdGeneric()).accept(in)) {
			this.addChild(p);
			found = true;
		    }
		    if (!found) {
			break;
		    } else {
			skipSpaces(in);
		    }
		}
		if (!this.secuence && !in.isEmpty()) {
		    found = in.get(0).equalsIgnoreCase("endif");
		    in.remove(0);
		}
		this.commentEnd = searchForLineComment(in);
		if (!found) {
		    this.error = this.start + " without endif";
		    return true;
		} else {
		    skipSpaces(in);
		}
		return true;
	    }

	    return false;
	}

	public String write() {
	    return this.start + (this.hasCondition ? " " + this.condition + " " : " ") + this.writeChildren()
		    + (this.secuence ? "" : " endif");
	}

	public String writeFormatedIntern(int indent) {
	    String space = getIndentString(this.secuence ? (indent - 1) : (indent));
	    String commIf = this.commentIf != null ? " //" + this.commentIf : "";
	    String commEn = this.commentEnd != null ? " //" + this.commentEnd : "";
	    return space + this.start + (this.hasCondition ? " " + this.condition + " " : " ") + commIf + "\r\n"
		    + this.writeChildrenFormated(this.secuence ? indent : (indent + 1)/* , "\r\n" */)
		    // + "\r\n"
		    + (this.secuence ? space + "endif\r\n" + commEn : "") /* + "\r\n" */;
	}

	// public String write() {
	// return " TODO: IF/ELSE";
	// }
	//
	// public String writeFormatedIntern(int indent) {
	// String space = getIndentString(indent);
	// return space + " TODO: IF/ELSE";
	// }
    }

    static class CmdBacklog extends Part {
	private String comment = null;

	public boolean accept(List<String> in) {
	    boolean found;
	    skipSpaces(in);
	    if (in.isEmpty()) {
		return false;
	    }
	    if (in.get(0).equalsIgnoreCase("backlog")) {
		in.remove(0);
		this.comment = searchForLineComment(in);
		while (!in.isEmpty() && !in.get(0).equalsIgnoreCase("endon")) {
		    found = false;
		    Part p;
		    if ((p = new CmdIf()).accept(in) || (p = new CmdGeneric()).accept(in)) {
			this.addChild(p);
			found = true;
		    }
		    if (!found) {
			break;
		    } else {
			skipSpaces(in);
		    }
		}
		return true;
	    }
	    return false;
	}

	public String write() {
	    return "backlog " + this.writeChildren();
	}

	public String writeFormatedIntern(int indent) {
	    String space = getIndentString(indent);
	    String comm = this.comment != null ? "//" + this.comment : "";
	    return space + "backlog " + comm + "\r\n" + this.writeChildrenFormated(indent + 1/* , "\r\n" */);
	}

    }

    static class CmdGeneric extends Part {
	private String body = "";
	protected String comment = null;
	boolean semicolon = false;

	public boolean accept(List<String> in) {
	    skipSpaces(in);
	    if (in.isEmpty()) {
		return false;
	    }
	    StringBuilder sb = new StringBuilder();
	    sb.append(in.remove(0));
	    while (!in.isEmpty() && !in.get(0).trim().equals(";") && !in.get(0).toLowerCase().startsWith("end")
		    && !in.get(0).toLowerCase().startsWith("else") && !in.get(0).trim().startsWith("//")) {
		sb.append(in.remove(0));
	    }
	    if (!in.isEmpty() && in.get(0).trim().equals(";")) {
		this.semicolon = true;
		in.remove(0);
	    }
	    this.comment = searchForLineComment(in);
	    this.body = sb.toString().trim();
	    return true;
	}

	public String write() {
	    return this.body + (this.semicolon ? ";" : "");
	}

	public String writeFormatedIntern(int indent) {
	    String space = getIndentString(indent);
	    return space + this.body + (this.semicolon ? ";" : "") + (this.comment != null ? " //" + this.comment : "")
		    + "\r\n";
	}
    }

    protected static RuleScript parse(List<String> codeParts) {
	RuleScript script = new RuleScript();
	script.accept(codeParts);
	return script;
    }

    public static RuleScript parse(String code) {
	String[] codeArray = code.split(
		"((?<=;)|(?=;)|(?<=%)|(?=%)|(?<=\\s)|(?=\\s)|(?<=//)|(?=//)|(?<=\\()|(?=\\()|(?<=\\))|(?=\\))|(\\b))");
	List<String> codeParts = new LinkedList<>(Arrays.asList(codeArray));
	return parse(codeParts);
    }

    public static void main(String[] a) {
	// String in = "on \r\n System#Boot do backlog VAR1 0; VAR1 0 endon on
	// Button1#state>0 do if(VAR1<10) power on elseif(VAR1<20) power off endif
	// endon";
	// String in = "on System#Boot do var 1 endon ";
	// String in = "on System#Boot do if(var1>0) var1 1; var2 2 else var1 2 endif
	// endon ";
	// String in = "on System#Boot do // comment DO \r\n// Comment line\r\n
	// \r\nbacklog test X; test2; test3 %value% endon // Comment END \r\non test#X
	// do var1 1 endon ";
	String in = "on System#Boot do backlog power off; VAR1 0; VAR2 0; VAR3 99 endon on BH1750#Illuminance do VAR3 %value% endon on Switch1#State do VAR2 %value% endon on Switch1#State>0 do if ((VAR1<11 AND VAR3<10) OR (VAR1>0 AND VAR1<21)) DIMMER 95; VAR1 12; RuleTimer1 60 elseif (VAR1<21) RuleTimer1 60 endif endon on event#setdim do if (%time%>1310 OR %time%<360) DIMMER 40 else DIMMER 80 endif endon on Switch1#State do publish dev/tasmota/fl/motion %value% endon";
	// String in = "[rule1 abc]// CMD RBlock1\r\non System#Boot do // comment DO
	// \r\n // comment line \r\n \r\nbacklog // comment backlog\r\n mul1 %VAR1%; //
	// CCC\r\n test X; // comment 1 \r\ntest2; // comment 2\r\n test3 %value% //
	// comment 3\r\n endon // Comment END \r\n[rule2] // RBlock2\r\non test#X!=1 do
	// var1 1 // comment21\r\n endon";

	// Teilen nach Wordgrenzen und ; mit Erhalt von ; als Extra-Token
	// String[] strparts =
	// in.split("((?<=;)|(?=;)|(?<=%)|(?=%)|(?<=\\s)|(?=\\s)|(?<=//)|(?=//)|(?<=\\()|(?=\\()|(?<=\\))|(?=\\))|(\\b))");
	// List<String> parts = new LinkedList<>(Arrays.asList(strparts));
	// System.out.println(in);
	// System.out.println(parts);
	// Part script = new RuleScript();
	// script.accept(parts);
	RuleScript script = parse(in);
	System.out.println();
	System.out.println(script.write());
	System.out.println();
	System.out.println(script.writeFormated());
	if (!script.getUnparsed().isEmpty()) {
	    System.err.println("List ist not empty: " + script.getUnparsed());
	}

    }

}
