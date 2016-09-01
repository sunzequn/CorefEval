package uri;

import java.net.URLDecoder;
import java.net.URLEncoder;

import com.sun.org.apache.xerces.internal.util.URI;

import utils.DES;

public class CurrentURI {


	private int cid;
	private int bid;
	private String currentURI;
	private String currentFbMid = null;
	private String baseURI;
	private String baseMid = null;
	private String currentDESURI;
	private String baseDESURI;
	private int position;
	private int sumURI;
	private int isCorefed;
	private int sumMarked;
	private boolean hasPrev;
	private boolean hasNext;
	
	public CurrentURI() {
	
	}
	public CurrentURI(int cid, int bid, String currentURI, String baseURI,
			int position, int sumURI, int isCorefed, boolean hasPrev,
			boolean hasNext) throws Exception {
		super();
		this.cid = cid;
		this.bid = bid;
		this.currentURI =  URLDecoder.decode(currentURI);
		this.baseURI = URLDecoder.decode(baseURI);
		this.position = position;
		this.sumURI = sumURI;
		this.isCorefed = isCorefed;
		this.hasPrev = hasPrev;
		this.hasNext = hasNext;
		this.currentDESURI = DES.getDES(currentURI);
		this.baseDESURI = DES.getDES(baseURI);
	}
	
	public int getSumMarked() {
		return sumMarked;
	}
	public void setSumMarked(int sumMarked) {
		this.sumMarked = sumMarked;
	}
	public String getCurrentDESURI() {
		return currentDESURI;
	}
	public void setCurrentDESURI(String currentDESURI) {
		this.currentDESURI = currentDESURI;
	}
	public String getBaseDESURI() {
		return baseDESURI;
	}
	public void setBaseDESURI(String baseDESURI) {
		this.baseDESURI = baseDESURI;
	}
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public String getCurrentURI() {
		return currentURI;
	}

	public void setCurrentURI(String currentURI) {
		this.currentURI = currentURI;
	}

	public String getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getSumURI() {
		return sumURI;
	}

	public void setSumURI(int sumURI) {
		this.sumURI = sumURI;
	}

	public int getIsCorefed() {
		return isCorefed;
	}

	public void setIsCorefed(int isCorefed) {
		this.isCorefed = isCorefed;
	}

	public boolean isHasPrev() {
		return hasPrev;
	}

	public void setHasPrev(boolean hasPrev) {
		this.hasPrev = hasPrev;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}
	
	public String getCurrentFbMid() {
		return currentFbMid;
	}
	
	public void setCurrentFbMid(String currentFbMid) {
		this.currentFbMid = currentFbMid;
	}
	
	public String getBaseMid() {
		return baseMid;
	}
	public void setBaseMid(String baseMid) {
		this.baseMid = baseMid;
	}
	@Override
	public String toString() {
		return "CurrentURI [cid=" + cid + ", bid=" + bid + ", currentURI=" + currentURI + ", currentFbMid="
				+ currentFbMid + ", baseURI=" + baseURI + ", baseMid=" + baseMid + ", currentDESURI=" + currentDESURI
				+ ", baseDESURI=" + baseDESURI + ", position=" + position + ", sumURI=" + sumURI + ", isCorefed="
				+ isCorefed + ", sumMarked=" + sumMarked + ", hasPrev=" + hasPrev + ", hasNext=" + hasNext + "]";
	}

}
