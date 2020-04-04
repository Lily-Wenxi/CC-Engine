
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class WebCrawler<Hashset> {

	private HashSet<String> links;	
	private List<List<String>> articles;
     List<String> idx_ls;
     String url;


	


	public WebCrawler( )throws IOException {
		//this.url = url;
		links = new HashSet<>();
        idx_ls = new ArrayList<String>();

	}	


	public void Gather(String url) throws IOException, ParseException {
		if ((!links.contains(url))&&links.size()<1000)
			
		try{links.add(url);
	    if (links.size()%20==0)
	    	FilterUrl();	

		    StdOut.println("links.size::"+links.size());
		    StdOut.println("url to connect Jsoup::"+ url);

		    Document doc = Jsoup.connect(url).timeout(5000).get();
			Elements linksOnPage = doc.select("a[href] ");
				for (Element page : linksOnPage) 
				{
					
					if (page.text().toLowerCase().contains("coronavirus"))
					{//StdOut.println(page.attr("abs:href"));
						Gather(page.attr("abs:href"));							
					 }																
			     }} catch (IOException | ParseException |IllegalArgumentException e ){
				System.err.println(e.getMessage());}
		}
	

	
	public void FilterUrl() throws IOException, ParseException { 
		articles = new ArrayList<>();
		getURLIndex();
   		links.forEach(a -> {
			try {
				List<String> pages = new ArrayList<String>();

				Document doc = Jsoup.connect(a).get();
				String md5; 
        		md5 = MD5(doc.text());         		
        		if(doc.hasText() && (!isMD5Exist(md5)))
        		 
				if(isNewVersion(a,md5))
					{ BufferedWriter writeURLIndex = new BufferedWriter(new FileWriter("./Index/indexURL.csv", true));
					writeURLIndex.append(VersionUpdate(a));
					writeURLIndex.append(",");
					writeURLIndex.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getPublishDate(a)));
					writeURLIndex.append(",");
					writeURLIndex.append(a);
					writeURLIndex.append(","+doc.text().length());
					writeURLIndex.append(",");
					writeURLIndex.append(md5+"\n");
					writeURLIndex.flush();
					writeURLIndex.close();
					StdOut.println("version update:" + VersionUpdate(a));
					pages.add(a);
					pages.add(md5);
					pages.add(doc.text());
					articles.add(pages);
					}
				   else 
					{ BufferedWriter writeURLIndex = new BufferedWriter(new FileWriter("./Index/indexURL.csv", true));
					writeURLIndex.append("version1.0");
					writeURLIndex.append(",");
					writeURLIndex.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getPublishDate(a)));
					writeURLIndex.append(",");
					writeURLIndex.append(a);
					writeURLIndex.append(","+doc.text().length());
					writeURLIndex.append(",");
					writeURLIndex.append(md5+"\n");
					writeURLIndex.flush();					
					writeURLIndex.close();
					pages.add(a);
					pages.add(md5);
					pages.add(doc.text());
					articles.add(pages);
					StdOut.println("version 1.0");
					}					
				} catch (ParseException | IOException | NoSuchAlgorithmException e) { e.printStackTrace();}
				             
			                
			});
   	 SavePage();
		

	}
						 int countpage = 0; 

	public void  SavePage() {
		articles.forEach(a -> {			
		
				try {
					savePagetoDisk(a.get(1),a.get(2),a.get(0));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		countpage++;
		StdOut.println(countpage + " pages have saved.");
		});	
		
	}
	public void savePagetoDisk(String save_md5, String save_text, String save_url) throws IOException
{
   		
		try {
			
		 BufferedWriter savePage = new BufferedWriter( new FileWriter("./Coronavirus/"+save_md5+".txt"));
		 savePage.write(save_url+"\n");
		 savePage.append(save_text);
		 savePage.flush();
		 savePage.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	

	public String VersionUpdate(String url)
	{		String new_version;	
		int i = idx_ls.indexOf(url);
		if(i == -1)
		new_version	= "version1.0";
		else
		new_version = idx_ls.get(i-2).substring(0,7)+(Integer.parseInt(idx_ls.get(i-2).substring(7,8))+1)+".0";
		return new_version; 
	}
		
	public void getURLIndex() throws IOException, ParseException {
		File indexURL = new File("./Index/indexURL.csv");
		if (indexURL.exists())
		{In in = new In ("./Index/indexURL.csv");
        String[] idx = in.readAllLines();
        String[] idx_split;
        for (String s : idx)
        {	
        	idx_split = s.split(",");
        idx_ls.add(idx_split[0]);
        idx_ls.add(idx_split[1]);
        idx_ls.add(idx_split[2]);
        idx_ls.add(idx_split[3]);
        idx_ls.add(idx_split[4]);
        }  }
		else 
			{idx_ls.add(null);
        	idx_ls.add(null);
        	idx_ls.add(null);
        	idx_ls.add(null);
        	idx_ls.add(null);
			}

	}
	
	public boolean isMD5Exist(String md5)
	{
		boolean isMD5Exist = false;
		if(idx_ls.isEmpty())
			isMD5Exist = false;
		else
			if(idx_ls.contains(md5))
				isMD5Exist = true;
		return isMD5Exist;
	}
	public Date getPublishDate (String url) throws IOException, ParseException
	{

		String p2 = "20\\d{2}(-|/)((0[1-9])|(1[0-2]))(-|/)((0[1-9])|([1-2][0-9])|"
				+ "(3[0-1]))(T|\\s)(([0-1][0-9])|(2[0-3])):([0-5][0-9]):([0-5][0-9])";
		Pattern p = Pattern.compile(p2, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Document doc = Jsoup.connect(url).get();
		List<Date> lstdate = new ArrayList<Date>();
		Date publishedDate = new Date();
				for (Element pq:doc.getElementsByTag("time"))
					{Matcher m = p.matcher(pq.attr("datetime"));
				while (m.find()){
					String dd = m.group(0);
		       		publishedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.ENGLISH).parse(dd);
		            lstdate.add(publishedDate);
				}
				
				}if (lstdate.isEmpty())
					publishedDate = new Date();
				else if(lstdate.size() == 1)
					publishedDate = lstdate.get(0);
				else if(lstdate.size() > 1)
				{
					Collections.sort(lstdate);
					publishedDate=lstdate.get(lstdate.size()-1);
				}
				
				

		return publishedDate;
	}
	
	public Date maxDate() throws IOException, ParseException {
		Date max_date = null ;
         if (idx_ls.size() == 0	)
        	 max_date = null;
         else {
 			Date[] dates = new Date[idx_ls.size()/5];
		 for (int i =0; i<(idx_ls.size()/5) ; i++)
			
		
			 {
				String sate = idx_ls.get(i*5+1);	
				dates[i]=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.ENGLISH).parse(sate);
			 }
			
		   
		  Sort.quicksort(dates);
		   max_date = dates[(idx_ls.size()/5)-1];
         }
			
		    return max_date;
	}
	
	public boolean isDateCurrent(String url) throws IOException, ParseException
	{
		boolean isCurrent = true;
		Date max_Date = maxDate();
		if(max_Date != null)			
		if(getPublishDate(url).compareTo(maxDate())<=0)
			isCurrent = false;
		return isCurrent;
		
		
	}
	
	public boolean isNewVersion(String url, String md5)
	{
		boolean isNewVersion = true;
		int i = idx_ls.indexOf(url);
		if(i==-1)
			isNewVersion = false;
		else
		if(idx_ls.get(i+2)!=null && idx_ls.get(i+2).equals(md5))
			isNewVersion = false;
		else isNewVersion = true;
		return isNewVersion;			
	}
	
	
	public String MD5(String context) throws NoSuchAlgorithmException{

			    MessageDigest md = MessageDigest.getInstance("MD5");
			    md.update(context.getBytes());
			    byte[] md5File = md.digest();
			    String myHash = DatatypeConverter
			      .printHexBinary(md5File).toUpperCase();
			         
			    //assertThat(myHash.equals(hash)).isTrue();
			    return(myHash);
			}
	public String MD5Url(String url) throws NoSuchAlgorithmException, IOException{

	    Document doc = Jsoup.connect(url).get();
		MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(doc.text().getBytes());
	    byte[] md5File = md.digest();	    
	    String myHash = DatatypeConverter
	      .printHexBinary(md5File).toUpperCase();
	    return(myHash);
	}

	public static void main(String args[]) throws IOException, ParseException {

		String URL = "https://news.yahoo.com/coronavirus/";
		//String pattern1 = "(\\S)*(news.yahoo.com)[-A-Za-z0-9/]+[-A-Za-z0-9/_]";
			WebCrawler	crawl = new WebCrawler();
			crawl.Gather(URL);

	}
	
}
