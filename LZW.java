import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JOptionPane;



public class LZW {
	public static void compress(String str, HashMap<String, Integer> dictionary, BufferedWriter output) throws IOException {
		String current = "";

		for (int i = 0; i < str.length(); ++i) {
			if (!dictionary.containsKey(current + str.charAt(i))) {
				output.write(dictionary.get(current));
				dictionary.put(current + str.charAt(i), dictionary.size());
				current = "";
			}

			current += str.charAt(i);
		}

		output.write(dictionary.get(current));
	}

	public static void decompress(String dictionary[], PrintWriter output, BufferedReader input) throws IOException {

		int currentCode, previousCode = 0, currentSize = 127;
		while ((currentCode = input.read()) != -1) {
			String str = dictionary[currentCode];
			if (str == null)
				str = dictionary[currentSize] = dictionary[previousCode] + dictionary[previousCode].charAt(0);

			dictionary[currentSize++] = dictionary[previousCode] + dictionary[currentCode].charAt(0);

			output.write(str);

			previousCode = currentCode;
		}
	}

	public static void main(String[] args) throws IOException {
		int choice = Integer.parseInt(JOptionPane.showInputDialog("Select your choice:\n1-Compress\n2-Decompress"));

		if (choice == 1) {
			HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
			for (int i = 0; i < 128; ++i)
				dictionary.put("" + (char) (i), i);

			Scanner input = new Scanner(new File("input.txt"));

			BufferedWriter output = new BufferedWriter(new FileWriter("compressed.lzw", false));

			while (input.hasNext())
				compress(input.nextLine(), dictionary, output);

			input.close();
			output.flush();
			output.close();

		}
		if (choice == 2) {
			final int MAXSIZE = 10000;
			String dictionary[] = new String[MAXSIZE];

			for (int i = 0; i < 128; ++i)
				dictionary[i] = "" + (char) i;

			BufferedReader input = new BufferedReader(new FileReader("compressed.lzw"));

			PrintWriter output = new PrintWriter(new File("uncompressed.txt"));
			decompress(dictionary, output, input);

			input.close();
			output.flush();
			output.close();
		}
	}
}
