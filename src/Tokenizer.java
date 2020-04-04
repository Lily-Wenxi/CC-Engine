import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tokenizer {
	int wordFrequency;
	private  Tokenizer(){
		wordFrequency = 0;
	}

	public void tokenizer(File folder) throws IOException {
		TST<Integer> stTST = new TST<Integer>();
		In in;
		BufferedWriter ou2;
		String key = null;
		BinarySearchTree<String> BT = new BinarySearchTree<String>();
        
		for (File file : folder.listFiles()) {
			in = new In(file);
			StdOut.println(file);
			String url = in.readLine();
			key = in.readAll().replaceAll("[,.+()\"/-\\@#$%^&*_+=<>;:?‘'’-]+"," ");
			key = key.replace("!"," ");		
			String[] str = key.split(" ");
			ou2 = new BufferedWriter(new FileWriter("./Index/indexOnWord.csv", true));

			for (int i = 0; i < str.length; i++) {
				{

					if (str[i].length() > 1 && (str[i].matches("[a-zA-Z]+")) && isKeyword(str[i]))

					{ stTST.put(str[i], i);
					  BT.insert(str[i]);
					
					ou2.append(str[i].toLowerCase() + "," + i + "," + file.getName() + "," + file.getPath() + "," + url
							+ "\n");
					StdOut.println(str[i].toLowerCase() + "," + i + "," + file.getName() + "," + file.getPath() + ","
							+ url + "\n");
						}
					
				}
				
				}
			ou2.flush();
			ou2.close();
			
		}
	}

	public static boolean isKeyword(String singleWord) {
		In in = new In("./Index/Filter.csv");
		String[] word = in.readAllStrings();
		Boolean isKeyword = true;
		for (String s : word)
			if (s.equals(singleWord.toLowerCase()))
				isKeyword = false;
		in.close();
		return isKeyword;
		
	}

	final static File file = new File ("./Coronavirus"); 
	public static void main(String[] args) throws IOException {
		Tokenizer tk = new Tokenizer();
		tk.tokenizer(file);
		
		
	}

}
