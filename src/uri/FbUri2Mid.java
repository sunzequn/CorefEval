package uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;

import com.sun.javafx.binding.StringFormatter;

import crawler.HttpMethod;
import crawler.PullText;
import database.DBOperation;

public class FbUri2Mid  extends PullText{
	
	private static final String FB_MID_SPARQL_PREFIX = "http://freebase.ailao.eu:3030/freebase/query?query=";
	private static final String FB_MID_SPARQL_SUFFIX = "&output=text&stylesheet=";
	private static final int TIME_OUT = 3000;
	
	public static void main(String[] args) throws Exception {
		String uri = "http://rdf.freebase.com/ns/en.barack_obama";
		parseMid(uri);
//		getSparql(uri);
	}
	//select ?s where {?s <http://rdf.freebase.com/key/en> "semantic_web"} 
	private static String getSparql(String uri){
		if (!uri.contains("ns/en")) {
			return null;
		}
		uri = uri.replace("/ns/", "/key/");
		String[] params = uri.split("en");
		String suffix = params[1];
		System.out.println(suffix);
		suffix = suffix.substring(1, suffix.length());
		System.out.println(suffix);
		String prefix = params[0] + "en";
		return FB_MID_SPARQL_PREFIX +  URLEncoder.encode("select ?s where {?s <" + prefix + "> \"" + suffix + "\"}") + FB_MID_SPARQL_SUFFIX;
	}
	
	public static String parseMid(String uri) throws Exception{
		String string = DBOperation.getMid(uri);
		if (string != null) {
			return string;
		}
		String sparql = getSparql(uri);
		if (sparql == null) {
			return null;
		}
		System.out.println(sparql);
		URL queryURL = new URL(sparql);
        URLConnection connAPI = queryURL.openConnection();
        connAPI.setConnectTimeout(TIME_OUT);
        connAPI.connect();
        String res = getMid(connAPI.getInputStream());
        System.out.println(res);
		return res;
	}
	
	private static String getMid(InputStream is) {   
		   BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
		        String line = null;   
		        try {   
		            while ((line = reader.readLine()) != null) {   
		            	if (line.contains("http://")) {
							line = StringUtils.removeEnd(line, "> |");
							line = StringUtils.removeStart(line, "| <");
							return line;
						}  
		            }   
		        } catch (IOException e) {   
		            e.printStackTrace();   
		        } finally {   
		            try {   
		                is.close();   
		            } catch (IOException e) {   
		                e.printStackTrace();   
		            }   
		        }   
		        return null;
		    }   
}
