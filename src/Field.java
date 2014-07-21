
public class Field {
	private String name;
	private int position;
	private String domain;
	private String description;
	private boolean key;
	
	public Field(String name, int position, String domain, String description) {
		setName(name);
		setPosition(position);
		setDomain(domain);
		setDescription(description);
	}
	
	public Field(String name, int position, String domain, String description, boolean key) {
		setName(name);
		setPosition(position);
		setDomain(domain);
		setDescription(description);
		setKey(key);
	}
	
	public void setKey(boolean key) {
		this.key = key;
	}
	
	public boolean getKey() {
		return key;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription()  {
		return this.description;
	}
	
	public String getDomain() {
		return this.domain;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getFieldName() {
		return this.name;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getPosition() {
		return this.position;
	}
}
