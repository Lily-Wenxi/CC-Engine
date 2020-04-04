import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Lexeme {
	static long total_keywords;
	static TST<Integer> st_unique_keywords;
	//static BinarySearchTree<String>  st_unique_keywords;

	static List<String[]> ls_all_keyword;
	//HashSet<String> hs; 
	
	private Lexeme(){
		total_keywords = 0;
		st_unique_keywords = new TST<Integer>();
		//st_unique_keywords = new BinarySearchTree<String>();
		ls_all_keyword = new ArrayList<>();	
		//hs = new HashSet<>();

	}
	
	private void rate_keyword_in_all_files()
	{
		In in = new In("./Index/IndexOnWord.csv");
		int count_unique_keywords = 0;
        while (in.hasNextLine())
        {
        	//keywords, position, filename.
        	String s = in.readLine();
            String[] s1 = s.split(",");
            ls_all_keyword.add(s1);
            //check if the keyword is existing in the tree
            if(st_unique_keywords.get(s1[0])!= null)
            	count_unique_keywords = st_unique_keywords.get(s1[0])+1;
            else count_unique_keywords = 1;
            //StdOut.println("s1[0]::"+s1[0]+"++>count_unique_keywords::"+count_unique_keywords);
            st_unique_keywords.put(s1[0],count_unique_keywords); //to count the number of a single keywords in all the file.
          total_keywords++;//to count how many keywords in all the files
         
        }   
	}		 int count= 0;

			
	public void rate_keyword_in_one_files()
	{
		//final int count= 0;
		st_unique_keywords.keys().forEach(a->{
			int count_keywords_in_file = 0;
			TST<Integer> st = new TST<Integer>();//keyword, filename, count
			for( int i=0; i<ls_all_keyword.size(); i++)
			{ // StdOut.println("+++++++++++++++++++++++++++++++++++++");
			   //StdOut.println("ls_all_keyword.get(i)[0]::"+ls_all_keyword.get(i)[0]+">>>>>>>"+a);

				//StdOut.println("ls_all_keyword.get(i)[0]"+	ls_all_keyword.get(i)[0]+"========>"+(ls_all_keyword.get(i)[0]==a));
				if(ls_all_keyword.get(i)[0].matches(a))//get keyword, check if eaqual
				{ StdOut.println("///////////////////////////////");
					if (st.get(ls_all_keyword.get(i)[2])!= null)//count
					count_keywords_in_file = st.get(ls_all_keyword.get(i)[2])+1;
				     else count_keywords_in_file = 1;
				StdOut.println("count_keywords_in_file"+count_keywords_in_file);
				    st.put(ls_all_keyword.get(i)[2], count_keywords_in_file);}//filename, count
				}
			StdOut.println("I'm here");
			st.keys().forEach(b->{
				try {
				BufferedWriter out = new BufferedWriter( new FileWriter("./Index/Lexeme.csv", true));
				String idx1 = a; 
				String idx2= b; 
				int idx3 = st.get(b); 
				int idx4 = st_unique_keywords.get(a); 
				out.append(idx1);				
				out.append(",");
				out.append(idx2);
				out.append(",");
				out.append(idx3+",");
				out.append((double)idx4/(double)total_keywords+"\n" );				
				out.flush();
				out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					count ++;});
			StdOut.println(count);
		});
		
	}

	public static void main(String[] arg) {	
		Lexeme lx = new Lexeme();	
		lx.rate_keyword_in_all_files();
		lx.rate_keyword_in_one_files();
		
		StdOut.println("well done");

	}
}