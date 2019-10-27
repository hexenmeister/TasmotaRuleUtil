package de.as.tasmota.rule.helper;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RulePrettyPrinter {

    private Map<String, String> cmdsStart = new HashMap<>();
    private Map<String, String> cmdsEnd = new HashMap<>();

    private static String reComparison = "(=|==|>|<|!=|>=|<=|\\|)";

    public abstract static class LexCommandPart {

        //        private int basicIndent = 0;
        private int indentDelta = 0;
        private String prefix = "";
        //        private String suffix = "";
        //        private List<LexCommandPart> children;

        //        protected LexCommandPart() {
        //
        //        }

        //        protected LexCommandPart(int basicIndent, int indentDelta) {
        protected LexCommandPart(int indentDelta) {
            //            this.basicIndent = basicIndent;
            this.indentDelta = indentDelta;
        }

        protected int getIndentDelta() {
            return this.indentDelta;
        }

        //        protected void setIndentDelta(int delta) {
        //            this.indentDelta = delta;
        //        }

        //        protected int getBasicIndent() {
        //            return this.basicIndent;
        //        }

        //        protected int getCurrentDelta() {
        //            return this.getBasicIndent() + this.getIndentDelta();
        //        }

        protected String getPrefix() {
            return this.prefix;
        }

        protected void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        //        protected String getSuffix() {
        //            return this.suffix;
        //        }
        //
        //        protected void setSuffix(String suffix) {
        //            this.suffix = suffix;
        //        }

        //        protected List<LexCommandPart> getChildren() {
        //            return this.children;
        //        }

        //        public abstract boolean accept(List<LexCommandPart> cmds, StringBuffer text);

        protected static LexCommandPart create(Class<? extends LexCommandPart> clazz, String text) {
            try {
                //                clazz.getConstructors()
                Constructor<? extends LexCommandPart> constructor = clazz.getConstructor(String.class);
                return constructor.newInstance(text);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new RuntimeException("Auto-generated catch block", e);
            }
        }

        public final void print(int indent, PrintStream ps) {
            //this.print(basicIndent, ps);
            //            for (int i = 0, n = this.getBasicIndent(); i < n; i++) {
            if (indent > 0) {
                for (int i = 0; i < indent; i++) {
                    ps.print("  ");
                }
            }
            ps.print(this.getPrefix());
            //            this.getChildren().forEach(x -> x.print(ps));
            //            ps.print(this.getSuffix());
        }

        //        protected void print(int indent, PrintStream ps) {
        //            ps.print(this.getPrefix());
        //            this.getChildren().forEach(LexCommandPart::print);
        //            ps.print(this.getSuffix());
        //        }

        protected static int searchInlineComment(String str, int pos) {
            if (pos >= str.length()) {
                return str.length();
            }
            Pattern p = Pattern.compile("(?m)//.*$", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(str);
            //            if (m.find(pos + 1)) {
            if (m.find(pos)) {
                pos = m.end();
            }
            return pos;
        }

        protected static int searchEndOfCurrentLine(String str, int pos) {
            Pattern p = Pattern.compile("(?m).*$", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(str);
            //                if (m.find(pos + 1)) {
            if (m.find(pos)) {
                pos = m.end();
            }
            return pos;
        }

        protected static int searchSpaceOrSemicolon(String str, int pos) {
            Pattern p = Pattern.compile("(?m)[\\s+|;|$]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(str + "\r\n");
            //            if (m.find(pos + 1)) {
            if (m.find(pos)) {
                pos = m.end();
            }
            return pos;
        }

        protected static String searchNextWord(String str, int pos) {
            Pattern p = Pattern.compile("\\s*(\\S+)\\s*", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(str);
            //            if (m.find(pos + 1)) {
            if (m.find(pos)) {
                return m.group(0);
                //                pos = m.end();
            }
            //            return pos;
            return null;
        }

        protected static boolean parseCommand(Class<? extends LexCommandPart> clazz, String start, String end, List<LexCommandPart> cmds, StringBuffer text) {
            String str = text.toString().toUpperCase();
            int pos = -1;
            if ((pos = str.indexOf(start)) >= 0) {
                if (str.substring(0, pos).trim().length() == 0) {
                    if (end != null) {
                        pos = str.indexOf(end, pos + 1);
                    } else {
                        pos = searchSpaceOrSemicolon(str, pos);
                    }
                    if (pos > 0) {
                        if (end != null) {
                            pos += end.length();
                        }
                        pos = searchInlineComment(str, pos);
                        String cmd = text.substring(0, pos);
                        text.delete(0, pos);
                        cmds.add(create(clazz, cmd.trim()));
                        return true;
                    }
                }
            }
            return false;
        }
    }
    protected static boolean parseCommand_TEST(Class<? extends LexCommandPart> clazz, String start, String end, List<LexCommandPart> cmds, StringBuffer text) {
	
	return false;
    }

    static class CmdOn extends LexCommandPart {

        public CmdOn(String text) {
            super(1);
            this.setPrefix(text);
        }

        public static boolean accept(List<LexCommandPart> cmds, StringBuffer text) {
            return parseCommand(CmdOn.class, "ON ", "DO", cmds, text);
        }

    }

    static class CmdEndon extends LexCommandPart {

        public CmdEndon(String text) {
            super(-1);
            this.setPrefix(text);
        }

        public static boolean accept(List<LexCommandPart> cmds, StringBuffer text) {
            return parseCommand(CmdEndon.class, "ENDON", null, cmds, text);
        }

    }

    static class CmdVar extends LexCommandPart {

        public CmdVar(String text) {
            super(0);
            this.setPrefix(text);
        }

        public static boolean accept(List<LexCommandPart> cmds, StringBuffer text) {
            return parseCommand(CmdVar.class, "VAR", null, cmds, text);
        }

    }

    static class CmdBacklog extends LexCommandPart {

        public CmdBacklog(String text) {
            super(0);
            this.setPrefix(text);
        }

        public static boolean accept(List<LexCommandPart> cmds, StringBuffer text) {
            return parseCommand(CmdBacklog.class, "BACKLOG", null, cmds, text);
        }

    }

    static class SimpleLine extends LexCommandPart {

        public SimpleLine(String text) {
            super(0);
            this.setPrefix(text);
        }

        public static boolean accept(List<LexCommandPart> cmds, StringBuffer text) {
            String str = text.toString().toUpperCase();
            int pos = searchEndOfCurrentLine(str, 0);
            String cmd = text.substring(0, pos);
            text.delete(0, pos);
            cmds.add(create(SimpleLine.class, cmd.trim()));
            return true;
        }

    }

    static class Remaining extends LexCommandPart {

        public Remaining(String text) {
            super(0);
            this.setPrefix(text);
        }

        public static boolean accept(List<LexCommandPart> cmds, StringBuffer text) {
            String cmd = text.toString();
            text.delete(0, text.length());
            cmds.add(create(Remaining.class, cmd.trim()));
            return true;
        }

    }

    static class Dummy extends LexCommandPart {

        public Dummy(String text) {
            super(-99999);
            this.setPrefix(text);
        }
    }

    static class Parser {
        public static List<LexCommandPart> parse(String in) {
            StringBuffer sb = new StringBuffer(in);
            List<LexCommandPart> ret = new ArrayList<>();

            while (sb.length() > 0) {
                boolean found = CmdOn.accept(ret, sb) ||
                        CmdEndon.accept(ret, sb) ||
                        CmdBacklog.accept(ret, sb) ||
                        CmdVar.accept(ret, sb);

                if (!found) {
                    String unknownCmd = LexCommandPart.searchNextWord(sb.toString(), 0);
                    SimpleLine.accept(ret, sb);
                    ret.add(new Dummy(">>>>> Unknown command: " + unknownCmd));
                    Remaining.accept(ret, sb);
                }
            }
            return ret;
        }
    }

    public RulePrettyPrinter() {
        cmdsStart.put("on", "on\\s\\w(#\\w)?(.*)\\sdo");
        cmdsEnd.put("on", "");
    }

    public static void main(String[] a) {

        //        Pattern p = Pattern.compile("(?m)[\\s+|;|$]", Pattern.CASE_INSENSITIVE);
        //        Matcher m;
        //        String str = "test\r\n";
        //        m = p.matcher(str);
        //        if (m.find()) {
        //            int end = m.end();
        ////            System.out.print(m.group(0));
        //            System.out.print(end);
        //            System.out.println(": " + str.substring(0, end));
        //        } else {
        //            System.out.println("not found");
        //        }

        String in = "on System#Boot do backlog VAR1 0; VAR1 0 endon on Button1#state>0 if(VAR1<10) power on elseif(VAR1<20) power off endif endon";
        //        String in = "on System#Boot do backlog on test#X do var endon  endon ";
        RulePrettyPrinter.getInstance().format(in);
    }

    public void format(String in) {
        List<LexCommandPart> cmds = Parser.parse(in);
        int indent = 0;
        for (LexCommandPart cmd : cmds) {
            int indentDelta = cmd.getIndentDelta();
            if (indentDelta != -99999) {
                if (indentDelta < 0) {
                    indent += indentDelta;
                }
                cmd.print(indent, System.out);
                if (indentDelta > 0) {
                    indent += indentDelta;
                }
            } else {
                cmd.print(0, System.out);
            }
            System.out.println();
        }
    }

    static RulePrettyPrinter inst = new RulePrettyPrinter();

    public static RulePrettyPrinter getInstance() {
        return inst;
    }
}
