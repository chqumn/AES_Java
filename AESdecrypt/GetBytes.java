// GetBytes: fetch array of bytes, represented in hex
import java.io.*;

public class GetBytes {
    private String fileName; // input filename
    private int arraySize; // number of bytes to read
    private Reader in;

    // GetBytes: constructor, opens input file
    public GetBytes(String file, int n) {
        fileName = file;
        arraySize = n;
        try {
            in = new FileReader(fileName);
        } catch (IOException e) {
            System.out.println("Exception opening " + fileName);
        }
    }

    // getNextChar: fetches next char
    private char getNextChar() {
        char ch = ' '; // = ' ' to keep compiler happy
        try {
            ch = (char)in.read();
        } catch (IOException e) {
            System.out.println("Exception reading character");
        }
    return ch;
    }

    // val: return int value of hex digit
    private int val(char ch) {
        if (ch >= '0' && ch <= '9') return ch - '0';
        if (ch >= 'a' && ch <= 'f') return ch - 'a' + 10;
        if (ch >= 'A' && ch <= 'F') return ch - 'A' + 10;
        return -1000000;
    }

    // getBytes: fetch array of bytes in hex
    public byte[] getBytes() {
        byte[] ret = new byte[arraySize];
        for (int i = 0; i < arraySize; i++) {
            char ch1 = getNextChar();
            char ch2 = getNextChar();
            ret[i] = (byte)(val(ch1)*16 + val(ch2));
        }
        char unwanted = getNextChar();
        unwanted = getNextChar();
        return ret;
    }
}