/**
 * 
 */
package importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import database.DBConnection;
import database.DBOperation;

/**
 * @author "Cunxin Jia"
 *
 */
public class SingleSourceImporter {
	private int sid;
	private int bid;
	private List<String> corefedURIs;
	private String filePath;
	

	public SingleSourceImporter(String source, String filePath) {
		this.filePath = filePath;
		setSid(source);
		corefedURIs = new ArrayList<String>();
	}
	
	public void setSid(String sourceName) {
		try {
			sid = DBOperation.getSourceId(sourceName);
			DBConnection.shutdown();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	public int getSid() {
		return sid;
	}
	
	public void insertCorefedURIs() {
		try {
			String baseURI = corefedURIs.get(0);
			bid = DBOperation.insertBaseURI(baseURI);
			for(int i = 1; i < corefedURIs.size(); i++) {
				int position = i + 1;	//Position starts with 1
				String corefedURI = corefedURIs.get(i);
//				System.out.println(corefedURI);
				int cid = DBOperation.insertCorefedURI(corefedURI, bid);
				DBOperation.insertURISource(cid, sid, position);
			}
			DBOperation.updateSumURI(bid);
			DBConnection.shutdown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readURIsFromFile() {
		try {
			Scanner input = new Scanner(new File(filePath), "utf-8");
			while(input.hasNext()) {
				String line = input.nextLine().trim();
				if(line != null && !line.equals("")) {
					corefedURIs.add(URLEncoder.encode(line));
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void importFromFile() {
		readURIsFromFile();
		insertCorefedURIs();
	}
	
	public void truncateAll() {
		
	}
	
	public static void main(String[] args) throws SQLException {
		DBOperation.reset();
		for(int i = 0; i < 51; i++) {
			SingleSourceImporter importer = new SingleSourceImporter("SelfTrainer", "uri/SelfTrainer/" + i);
			importer.importFromFile();
			System.out.println(i);
		}
		for(int i = 0; i < 51; i++) {
			SingleSourceImporter importer = new SingleSourceImporter("SelfTrainer FPC", "uri/SelfTrainerFPC/" + i);
			importer.importFromFile();
			System.out.println(i);
		}
		for(int i = 0; i < 51; i++) {
			SingleSourceImporter importer = new SingleSourceImporter("SelfTrainer CC", "uri/SelfTrainer_CC/" + i);
			importer.importFromFile();
			System.out.println(i);
		}
		for(int i = 0; i < 51; i++) {
			SingleSourceImporter importer = new SingleSourceImporter("SelfTrainer FPC CC", "uri/SelfTrainerFPC_CC/" + i);
			importer.importFromFile();
			System.out.println(i);
		}
	}
}
