import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Searching {
	List<String> ls;
	TST<Double> t1;
	TST<Double> t2;
	BinarySearchTree<String> tt;

	public Searching()
	{
		ls = new ArrayList<>();
		t1= new TST<>();
		t2 = new TST<>();
		tt = new BinarySearchTree<>();
	}
	public static boolean isKeyword(String singleWord) {
		In in = new In("./Index/Filter.csv");
		String[] word = in.readAllStrings();
		Boolean isKeyword = true;
		for (String s : word)
			if (s.equals(singleWord.toLowerCase()))
				isKeyword = false;
		return isKeyword;
	}
	
	public void ReadKeywords(String key)
	{
		 String word = key.replace("!", " ");
			StdOut.println(word);

		      String[] words = word.split(" ");

	
		for(int i = 0; i < words.length; i++) 
		{          
			if (words[i].length()>1 && isKeyword(words[i])&& words[i].matches("[A-Za-z]+"))
			
					ls.add(words[i].toLowerCase());
			        
				}
	}
	
	public void readFiles()
	{
		In in = new In("./Index/Lexeme.csv");	
        
        while(in.hasNextLine())
        {   
        	String[] s = in.readLine().split(",");
        	String s1 = s[0];
        	String s2 = s[1];
        	File file = new File("./Coronavirus/"+s2); 
        	if (file.exists())
        	{//StdOut.println(file);
        	In readfile = new In(file);
        	String header = readfile.readLine();
        	if (!header.contains("?") && (t1.get(s1+","+ header)==null))
        	{//put url without "?" and different URL into t1
        		double s3 = Integer.valueOf(s[2])*Double.valueOf(s[3]);        	
            	t1.put(s1+","+ header, s3);
        	}
        	readfile.close();
        	
        	}
        }
	}
	
	public void mappingURL()
	{
		

		for(String keyword: ls)
		{

			t1.keys().forEach(k->{
				String k1=k.split(",")[0];//keyword
				String k2=k.split(",")[1];//url
				Double k3=t1.get(k);//word frequency
				if (k1.matches(keyword))
				{
					if(t2.get(k2)!=null)
					t2.put(k2,t2.get(k2)+k3);//calculate frequency of input keywords
					else t2.put(k2,k3);
				}					
			});			
		}
	}
	
	
	public void ranking()
	{
		t2.keys().forEach(ss->{
			tt.insert(t2.get(ss)+","+ss);
		});
		}
	
	
	int count = 0;	
	public void display ()
	{
		
		
	for(int i =0; i<10; i++)
	{       
		String max = tt.findMax();
	    String url = max.split(",")[1];
        tt.remove(max);
			Document doc;
			try {
				doc = Jsoup.connect(url).get();
			
			//In display = new In(dp);
			String title = doc.title();
			String text = doc.text();			
			StdOut.println(title);
			StdOut.println(url);
			if (text.length()<100)
				StdOut.println(text.substring(text.lastIndexOf(">")));

				//StdOut.println(text.substring(title.length()));
			else if(text.length()>100 && text.length()<200)
			{
				StdOut.println(text.substring(title.length(),title.length()+100));
				StdOut.println(text.substring(title.length()+101));
			}
			else
				{
				StdOut.println(text.substring(title.length(),title.length()+100));
				StdOut.println(text.substring(title.length()+101,title.length()+200));
				}

			StdOut.println();
			StdOut.println();

			count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			//}
				}
	}
	}
	

	public static void main(String args[]) throws IOException
	{		
		Searching sh = new Searching();
	    sh.readFiles();
		StdOut.println("well done readFiles");

        TST<String> t = new TST<>( );
        StdOut.println("pls type something you want to search: \n");		
		String key = StdIn.readLine().replaceAll("[,.+()\"/-\\@#$%^&*_+=<>;:?‘'’-]+", " ");
		BinarySearchTree<String> t3 = new BinarySearchTree<>();
		sh.ReadKeywords(key);
		StdOut.println("well done reading keywords");


			sh.mappingURL();
			StdOut.println("well done mapping URL");
			sh.ranking();
			StdOut.println("well done ranking");
			sh.display();
			StdOut.println("well done display");

				}
		}


