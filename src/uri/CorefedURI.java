/**
 * 
 */
package uri;

/**
 * @author "Cunxin Jia"
 *
 */
public class CorefedURI {
	private int cid;
	private String URI;
	private int isCorefed;
	/**
	 * @param cid
	 * @param uRI
	 * @param source
	 * @param isCorefed
	 */
	public CorefedURI(int cid, String URI, int isCorefed) {
		this.cid = cid;
		this.URI = URI;
		this.isCorefed = isCorefed;
	}
	/**
	 * @return the cid
	 */
	public int getCid() {
		return cid;
	}
	/**
	 * @param cid the cid to set
	 */
	public void setCid(int cid) {
		this.cid = cid;
	}
	/**
	 * @return the uRI
	 */
	public String getURI() {
		return URI;
	}
	/**
	 * @param uRI the uRI to set
	 */
	public void setURI(String uRI) {
		URI = uRI;
	}
	/**
	 * @return the isCorefed
	 */
	public int getIsCorefed() {
		return isCorefed;
	}
	/**
	 * @param isCorefed the isCorefed to set
	 */
	public void setIsCorefed(int isCorefed) {
		this.isCorefed = isCorefed;
	}
}
