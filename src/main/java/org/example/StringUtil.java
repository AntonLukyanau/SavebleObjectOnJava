package org.example;

public class StringUtil {

    public int indentsBeforeText(String line) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                count++;
            } else {
                break;
            }
        }
        return count / Config.INDENT;
    }

    public String trimLastSymbol(String str) {
        return str.substring(0, str.length() - 1);
    }

    public String addIndents(String str, int indentCount) {
        return " ".repeat(indentCount * Config.INDENT) + str;
    }

}
