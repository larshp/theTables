import java.util.LinkedList;

public class Table {
	LinkedList<Field> fields = new LinkedList<Field>();
	
	private String name = null;
	private String description = null;

	public Table(String name) {
		setName(name);
	}
	
	public LinkedList<Field> getFields() {
		return fields;
	}
	
	public void addField(Field f) {
		fields.add(f);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void rearrangeFields() {
		int len = fields.size();
		int i = 0;
		// bubblesort
		while(i<len-1) {
			Field current = fields.get(i);
			Field next = fields.get(i+1);
			if(current.getPosition() > next.getPosition()) {
				// System.out.println("Switch");
				fields.remove(i+1);
				fields.add(i, next);
				i = 0;
				continue;
			}
			
			i++;
		}
	}
}
