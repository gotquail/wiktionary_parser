import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class WiktionaryParser {

    private final static String TARGET_FILE = "wiktionary_simple.tsv";
    private final static int INDEX_DEFINITION = 3;

    public static void main(String[] args) {

        readWiktionary(TARGET_FILE);
    }

    public static void readWiktionary(String fn) {
        BufferedReader inputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader(fn));

            String line;
            while ((line = inputStream.readLine()) != null) {
                String[] entry = line.split("\t");
                for(String item : entry) {
                    System.out.println(item);
                }
                if (!parseDefinition(entry[INDEX_DEFINITION])) {
                    return;
                }
            }
        } catch (IOException e) {
            ;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {

            }
        }
    }

    public static boolean parseDefinition(String def) {

        String[] words = def.split(" ");
        for (String w : words) {
            System.out.println("\t" + w);

            String validChar = "[\\w\\-\\.,;]";
            String optionalPunctuation = "[\\.,;]?";
            
            String plainWordPattern = "\\w+" + optionalPunctuation;
            String linkPattern = "\\[\\[(\\w+)\\]\\](\\w*?)" + optionalPunctuation;
            String linkCompositePattern = "\\[\\[(\\w+)\\|(\\w+)\\]\\](\\w*?)" + 
                    optionalPunctuation;

            // try to match vs all known markups.
            if (w.matches(plainWordPattern)) {
                // Normal words.
            }
            else if (w.matches("#")) {
                // TODO: Trim this thing? Does every line have one?
            } 
            else if (w.matches(linkPattern)) {
                // TODO: handle links. Replace [[word]] with link to word.
                // Don't forget to handle [[word]]s case.
            }
            else if (w.matches(linkCompositePattern)) { // [[word to link to|appearance]]
                System.out.println(w.replaceAll(linkCompositePattern, 
                    "$1 is link for $2$3"));
            }
            else {
                System.out.println("Failed on: " + w);
                return false;
            }
            
        }

        return true;
    }
}
