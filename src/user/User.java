/**
 * 
 */
package user;

/**
 * @author "Cunxin Jia"
 *
 */
public class User {
	private int uid;
	private String email;
	/**
	 * @param uid
	 * @param email
	 */
	public User(int uid, String email) {
		super();
		this.uid = uid;
		this.email = email;
	}
	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "User [uid=" + uid + ", email=" + email + "]";
	}
}
