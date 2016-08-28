package uri;

public class BaseURI {

	private int bid;
	private String baseURI;
	private int position;
	private int sum;
	private int cid;
	
	public BaseURI(int bid, String baseURI, int position, int sum, int cid) {
		super();
		this.bid = bid;
		this.baseURI = baseURI;
		this.position = position;
		this.sum = sum;
		this.cid = cid;
	}
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
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
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
}
