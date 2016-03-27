package local.ss;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;



public class Reader {

    public static void main(String[] args) {
        System.out.println("Hello");

        List<String> result =
            new Reader(new StringBufferInputStream("\tasd\tbde")).splitNextLine();
        for (String entry : result) {
            System.out.println(entry);
        }

    }

    public Reader(InputStream is) {
        lineScanner = new Scanner(is);
    }

    public List<String> splitNextLine() {

        if (!lineScanner.hasNext()) {
            return Collections.<String>emptyList();
        }

        String line = lineScanner.nextLine();
        return Arrays.<String>asList(line.split("\t", 10));
    }

    //private Pattern columnDelimiter = Pattern.compile("\t");
    private Scanner lineScanner;
}
