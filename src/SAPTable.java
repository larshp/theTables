import java.io.IOException;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

// todo, refactoring needed

public class SAPTable extends Table {
	String name;

	public SAPTable(String name) throws NotFoundException {
		super(name);
		this.name = name;

		setDescription();

		addFields();
	}

	// /////////////////////////////////////////////////////////

	public void addFields() {
		JCoFunction function = null;

		try {
			function = Main.getDest().getRepository().getFunction(
					"RFC_READ_TABLE");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JCoException e) {
			e.printStackTrace();
		}

		if (function != null) {
			JCoParameterList input = function.getImportParameterList();
			input.setValue("QUERY_TABLE", "DD03L");
			JCoParameterList pl = function.getTableParameterList();
			JCoTable options = pl.getTable("OPTIONS");
			options.firstRow();
			options.insertRow(0);
			options.setRow(0);
			options.setValue("TEXT", "TABNAME = '" + name + "'");
			options.lastRow();

			try {
				function.execute(Main.getDest());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JCoException e) {
				e.printStackTrace();
			}

			JCoTable fields = function.getTableParameterList().getTable(
					"FIELDS");
			JCoTable data = function.getTableParameterList().getTable("DATA");

			int foffset = 0;
			int flength = 0;
			int poffset = 0;
			int plength = 0;
			int koffset = 0;
			int klength = 0;
			int roffset = 0;
			int rlength = 0;

			do {
				JCoFieldIterator iterator = fields.getFieldIterator();
				while (iterator.hasNextField()) {
					JCoField field = iterator.nextField();
					// System.out.println("FieldValue: "+field.getString() +
					// " "+field.getName());

					if (field.getName().compareTo("FIELDNAME") == 0
							&& field.getString().compareTo("FIELDNAME") == 0) {
						foffset = iterator.nextField().getInt();
						flength = iterator.nextField().getInt();
					}

					if (field.getName().compareTo("FIELDNAME") == 0
							&& field.getString().compareTo("POSITION") == 0) {
						poffset = iterator.nextField().getInt();
						plength = iterator.nextField().getInt();
					}

					if (field.getName().compareTo("FIELDNAME") == 0
							&& field.getString().compareTo("KEYFLAG") == 0) {
						koffset = iterator.nextField().getInt();
						klength = iterator.nextField().getInt();
					}

					if (field.getName().compareTo("FIELDNAME") == 0
							&& field.getString().compareTo("ROLLNAME") == 0) {
						roffset = iterator.nextField().getInt();
						rlength = iterator.nextField().getInt();
					}
				}
			} while (fields.nextRow());

			do {
				JCoFieldIterator iterator = data.getFieldIterator();
				while (iterator.hasNextField()) {
					JCoField field = iterator.nextField();

					String fieldname = field.getString().substring(
							foffset,
							Math.min(foffset + flength, field.getString()
									.length()));
					fieldname = fieldname.trim();
					String position = field.getString().substring(
							poffset,
							Math.min(poffset + plength, field.getString()
									.length()));
					String keyflag = field.getString().substring(
							koffset,
							Math.min(koffset + klength, field.getString()
									.length()));
					boolean key = false;
					if (keyflag.compareTo("X") == 0) {
						key = true;
					}
					String rollname = field.getString().substring(
							roffset,
							Math.min(roffset + rlength, field.getString()
									.length()));
					rollname = rollname.trim();
					// System.out.println("Field: " + fieldname + " " +
					// position);
					if (fieldname.charAt(0) != '.') {
						this.addField(new Field(fieldname, Integer
								.parseInt(position), rollname,
								getElementDescription(rollname), key));
					}
				}
			} while (data.nextRow());

			// JCO.releaseClient(client);
		} else {
			System.out.println("function not found");
		}

		rearrangeFields();
	}

	// /////////////////////////////////////////////////////////

	public String getElementDescription(String element) {

		JCoFunction function = null;
		String description = "";

		try {
			function = Main.getDest().getRepository().getFunction(
					"RFC_READ_TABLE");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JCoException e) {
			e.printStackTrace();
		}

		if (function != null) {
			JCoParameterList input = function.getImportParameterList();
			input.setValue("QUERY_TABLE", "DD04T");
			JCoParameterList pl = function.getTableParameterList();
			JCoTable options = pl.getTable("OPTIONS");
			options.firstRow();
			options.insertRow(0);
			options.setRow(0);
			options.setValue("TEXT", "ROLLNAME = '" + element
					+ "' AND DDLANGUAGE = 'E'");
			options.lastRow();

			try {
				function.execute(Main.getDest());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JCoException e) {
				e.printStackTrace();
			}

			JCoTable fields = function.getTableParameterList().getTable(
					"FIELDS");
			JCoTable data = function.getTableParameterList().getTable("DATA");

			int offset = 0;
			int length = 0;
			do {
				JCoFieldIterator iterator = fields.getFieldIterator();
				while (iterator.hasNextField()) {
					JCoField field = iterator.nextField();

					if (field.getName().compareTo("FIELDNAME") == 0
							&& field.getString().compareTo("SCRTEXT_M") == 0) {
						offset = iterator.nextField().getInt();
						length = iterator.nextField().getInt();
					}
				}
			} while (fields.nextRow());

			do {
				JCoFieldIterator iterator = data.getFieldIterator();
				while (iterator.hasNextField()) {
					JCoField field = iterator.nextField();
					try {
						description = field.getString().substring(
								offset,
								Math.min(offset + length, field.getString()
										.length()));
						description = description.trim();
					} catch (StringIndexOutOfBoundsException e) {
						description = "";
					}
				}
			} while (data.nextRow());

		} else {
			System.out.println("function not found");
		}
		return description;
	}

	// /////////////////////////////////////////////////////////

	public void setDescription() throws NotFoundException {

		JCoFunction function = null;

		try {
			function = Main.getDest().getRepository().getFunction(
					"RFC_READ_TABLE");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JCoException e) {
			e.printStackTrace();
		}

		if (function != null) {
			JCoParameterList input = function.getImportParameterList();
			input.setValue("QUERY_TABLE", "DD02T");
			JCoParameterList pl = function.getTableParameterList();
			JCoTable options = pl.getTable("OPTIONS");
			options.firstRow();
			options.insertRow(0);
			options.setRow(0);
			options.setValue("TEXT", "TABNAME = '" + name
					+ "' AND DDLANGUAGE ='E'");
			options.lastRow();

			try {
				function.execute(Main.getDest());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JCoException e) {
				e.printStackTrace();
			}

			JCoTable fields = function.getTableParameterList().getTable(
					"FIELDS");
			JCoTable data = function.getTableParameterList().getTable("DATA");

			if (data.getNumRows() == 0) {
				throw new NotFoundException();
			}

			int offset = 0;
			int length = 0;
			do {
				JCoFieldIterator iterator = fields.getFieldIterator();
				while (iterator.hasNextField()) {
					JCoField field = iterator.nextField();

					if (field.getName().compareTo("FIELDNAME") == 0
							&& field.getString().compareTo("DDTEXT") == 0) {
						offset = iterator.nextField().getInt();
						length = iterator.nextField().getInt();
					}
				}
			} while (fields.nextRow());

			String desc = "";
			do {
				JCoFieldIterator iterator = data.getFieldIterator();
				while (iterator.hasNextField()) {
					JCoField field = iterator.nextField();

					desc = field.getString().substring(
							offset,
							Math.min(offset + length, field.getString()
									.length()));
					desc = desc.trim();
				}
			} while (data.nextRow());
			super.setDescription(desc);

		} else {
			System.out.println("function not found");
		}
	}
}
