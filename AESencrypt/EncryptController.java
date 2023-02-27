// AEStest: test AES encryption
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class EncryptController {
    // convert a string to hex
    public static String toHex(String arg) {
        return String.format("%016x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }

    public static void main(String[] args) {
        int Nk = 4; // 4, 6, or 8
        int Nb = 4; // always 4
        // for 128-bit key, use 16, 16, and 4 below
        // for 192-bit key, use 16, 24 and 6 below
        // for 256-bit key, use 16, 32 and 8 below

        // from plaintext to input
        try {
            // Read plaintext from file
            File inputFile = new File("plaintext.txt");
            FileInputStream inputStream = new FileInputStream(inputFile);
            StringBuilder plaintextBuilder = new StringBuilder();
            byte[] buffer = new byte[16];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                plaintextBuilder.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
            }
            inputStream.close();
            String plaintext = plaintextBuilder.toString();
        
            // Pad the last block if necessary
            int plaintextLength = plaintext.length();
            int paddingLength = 16 - (plaintextLength % 16);
            if (paddingLength < 16) {
                for (int i = 0; i < paddingLength; i++) {
                    plaintext += (char) 04;
                }
            }
        
            // Convert plaintext to series of hex strings
            StringBuilder hexBuilder = new StringBuilder();
            for (int i = 0; i < plaintext.length(); i++) {
                hexBuilder.append(String.format("%02x", (int) plaintext.charAt(i)));
            }
            String hexStrings = hexBuilder.toString();
        
            // Split hex strings into strings with 32 characters
            int n = 32;
            String[] hexStringArray = new String[(hexStrings.length() + n - 1) / n];
            int index = 0;
            for (int i = 0; i < hexStrings.length(); i += n) {
                hexStringArray[index++] = hexStrings.substring(i, Math.min(i + n, hexStrings.length()));
            }
        
            // Write hex strings to file
            File outputFile = new File("input.txt");
            FileWriter writer = new FileWriter(outputFile);
            for (String hexString : hexStringArray) {
                writer.write(hexString);
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // read key
        GetBytes getKey = new GetBytes("key.txt", Nk * 4);
        byte[] key = getKey.getBytes();
        // read input.txt, 16 bytes at a time
        GetBytes getInput = new GetBytes("input.txt", Nb * 4);
        byte[] in = getInput.getBytes();
        int blockNumber = 1; // index of block, start from 1
        // overwrite ciphertext.txt
        try {
            FileWriter writerObj = new FileWriter("ciphertext.txt", false);
            writerObj.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        try(FileWriter fw = new FileWriter("ciphertext.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter output = new PrintWriter(bw)) {
            // encrypt each 16-byte block
            // stop when the last block is read
            while ((in[0] != -64) &  (in[1] != -64) & (in[2] != -64) & (in[3] != -64)) {
                System.out.println("-------------------------Block number " + blockNumber++ + "--------------------------");
                AESencrypt aes = new AESencrypt(key, Nk);
                Print.printArray("Plaintext:\t ", in);
                Print.printArray("Key:\t\t ", key);
                byte[] out = new byte[16];
                aes.Cipher(in, out);
                Print.printArray("Ciphertext:\t ", out);
                // write into ciphertext.txt
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
    }
}
