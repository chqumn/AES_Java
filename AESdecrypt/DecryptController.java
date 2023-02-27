import java.io.*;

// AESinvTest: test AES decryption
public class DecryptController {
    public static String unHex(String arg) {        

        String str = "";
        for(int i=0;i<arg.length();i+=2)
        {
            String s = arg.substring(i, (i + 2));
            int decimal = Integer.parseInt(s, 16);
            str = str + (char) decimal;
        }       
        return str;
    }

    public static void main(String[] args) {
        int Nk = 4; // 4, 6, or 8 words
        int Nb = 4; // always 4 words
        // for 128-bit key, use 16, 16, and 4 below
        // for 192-bit key, use 16, 24 and 6 below
        // for 256-bit key, use 16, 32 and 8 below

        GetBytes getKey = new GetBytes("key.txt", Nk * 4);
        byte[] key = getKey.getBytes();
        // read ciphertext, 32 hex digits at a time
        GetBytes getInput = new GetBytes("ciphertext.txt", Nb * 4);
        byte[] in = getInput.getBytes();

        int blockNumber = 1; // index of block, start from 1
        // overwrite ciphertext.txt
        try {
            FileWriter writerObj = new FileWriter("output.txt", false);
            writerObj.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try(FileWriter fw = new FileWriter("output.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter output = new PrintWriter(bw)) {
            // encrypt each 16-byte block
            // stop when the last block is read
            while ((in[0] != -64) && (in[1] != -64) && (in[2] != -64) && (in[3] != -64)) {
                System.out.println("-------------------------Block number " + blockNumber++ + "--------------------------");
                AESdecrypt aesDec = new AESdecrypt(key, Nk);
                Print.printArray("Ciphertext:\t ", in);
                Print.printArray("Key:\t\t ", key);
                byte[] out = new byte[16];
                aesDec.InvCipher(in, out);
                Print.printArray("Plaintext:\t ", out);
                // write to output.txt
                for (int c = 0; c < Nb; c++)
                    for (int r = 0; r < 4; r++)
                        output.print(Print.hex(out[c * Nb + r]));
                output.println();
                // read next 16 bytes
                in = getInput.getBytes();
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File input = new File("output.txt");
            FileInputStream myReader = new FileInputStream(input);
            byte[] buffer = new byte[32];
            try {
                FileWriter myWriter = new FileWriter("plaintext.txt");
                try {
                    int rc = myReader.read(buffer);
                    while (rc != -1) {
                        // convert to string
                        String bufString = unHex(new String(buffer));
                        // remove null characters and end-of-text character
                        String fixString = bufString.replace("\0", "").replace("\u0004", "").replace("\u0003", "").replace("\u0001", "");
                        // write to plaintext.txt
                        myWriter.write(fixString);
                        buffer = new byte[32];
                        rc = myReader.read(new byte[2]);
                        rc = myReader.read(buffer);
                    }
                    myReader.close();
                    myWriter.close();
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
    }
}