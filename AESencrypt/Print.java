// Print: print arrays of bytes
public class Print {
    private static final int Nb = 4;
    private static String[] dig = {"0","1","2","3","4","5","6","7",
                                   "8","9","a","b","c","d","e","f"};

    // hex: print a byte as two hex digits
    public static String hex(byte a) {
        return dig[(a & 0xff) >> 4] + dig[a & 0x0f];
    }

    public static void printArray(String name, byte[] a) {
        System.out.print(name + " ");
        for (int i = 0; i < a.length; i++)
            System.out.print(hex(a[i]) + " ");
        System.out.println();
    }

    public static void printArray(String name, byte[][] s) {
        System.out.print(name + "\t  ");
        for (int c = 0; c < Nb; c++)
            for (int r = 0; r < 4; r++)
                System.out.print(hex(s[r][c]) + " ");
        System.out.println();
    }
}