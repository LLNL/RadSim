package gov.llnl.rtk.mcnp;

import java.util.ArrayList;
import java.util.List;

public class MCNP_Card {

    private final static int MAX_LINE_LENGTH = 80;

    private List<String> lines = new ArrayList<>();

    public MCNP_Card() {

    }

    public MCNP_Card(String line) {
        lines.add(line);
    }

    private String currentLine(){
        if (lines.size() == 0) {
            lines.add("");
        }
        return lines.get(lines.size() - 1);
    }

    private void concatCurrentLine(String string) {
        lines.set(lines.size() - 1, currentLine() + string);
    }

    private void setEndToCurrentLine(String end) {
        String format = "%-" + (MAX_LINE_LENGTH - 1) + "s";
        lines.set(lines.size() - 1, String.format(format, currentLine()) + end);
    }

    public void addEntry(Object entry) {
        String concatenatedLine = currentLine();
        if (concatenatedLine.length() != 0) concatenatedLine += " ";
        concatenatedLine += entry;

        if (concatenatedLine.length() >= MAX_LINE_LENGTH) {
            setEndToCurrentLine("&");
            lines.add("");
        }

        if (currentLine().length() != 0) concatCurrentLine(" " + entry);
        else concatCurrentLine(entry.toString());
    }

    public void addComment(String comment) {
        setEndToCurrentLine("$ " + comment);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line).append("\n");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static MCNP_Card centeredTextCard(String beginning, String contents, String end){
        int totalSpaces = MAX_LINE_LENGTH - beginning.length() - contents.length() - end.length();
        int leftSpaces = totalSpaces / 2;
        int rightSpaces = leftSpaces;
        if (totalSpaces % 2 != 0) rightSpaces += 1;
        String leftFormat = "%-" + leftSpaces + "s";
        String rightFormat = "%-" + rightSpaces + "s";
        return new MCNP_Card(beginning + String.format(leftFormat, "") + contents + String.format(rightFormat, "") + end);
    }

    public static MCNP_Card centeredTextCard(String contents) {
        return MCNP_Card.centeredTextCard("", contents, "");
    }

    public static MCNP_Card fullLineCommentCard() {
        return new MCNP_Card("C " + "*".repeat(MAX_LINE_LENGTH- 2 ));
    }

    public static void main(String ... args) {
        MCNP_Card card = new MCNP_Card();
        card.addEntry("m1");
        card.addEntry(567);
        card.addEntry(9.00010000);
        card.addComment("Test");
        System.out.println(card);
    }



}
