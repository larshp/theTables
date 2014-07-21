import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;


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
			function = Main.destination.getRepository()
					.getFunction("RFC_READ_TABLE");
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
				function.execute(Main.destination);
			} catch (JCoException e) {
				e.printStackTrace();
			}

			JCoTable fields = function.getTableParameterList().getTable("FIELDS");
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
				while(iterator.hasNextField())
				{
				    JCoField field = iterator.nextField();
//				    System.out.println("FieldValue: "+field.getString() + " "+field.getName());
				
				    if(field.getName().compareTo("FIELDNAME") == 0 && field.getString().compareTo("FIELDNAME") == 0) {
				    	foffset = iterator.nextField().getInt();
				    	flength = iterator.nextField().getInt();
				    }
				    
				    if(field.getName().compareTo("FIELDNAME") == 0 && field.getString().compareTo("POSITION") == 0) {
				    	poffset = iterator.nextField().getInt();
				    	plength = iterator.nextField().getInt();
				    }		
				    
				    if(field.getName().compareTo("FIELDNAME") == 0 && field.getString().compareTo("KEYFLAG") == 0) {
				    	koffset = iterator.nextField().getInt();
				    	klength = iterator.nextField().getInt();
				    }
				    
				    if(field.getName().compareTo("FIELDNAME") == 0 && field.getString().compareTo("ROLLNAME") == 0) {
				    	roffset = iterator.nextField().getInt();
				    	rlength = iterator.nextField().getInt();
				    }
				}
			} while (fields.nextRow());

			do {
				JCoFieldIterator iterator = data.getFieldIterator();
				while(iterator.hasNextField())
				{
				    JCoField field = iterator.nextField();
				    	    
				String fieldname = field.getString()
						.substring(
								foffset,
								Math.min(foffset + flength, field.getString()
										.length()));
				fieldname = fieldname.trim();
				String position = field.getString()
						.substring(
								poffset,
								Math.min(poffset + plength, field.getString()
										.length()));
				String keyflag = field.getString()
						.substring(
								koffset,
								Math.min(koffset + klength, field.getString()
										.length()));
				boolean key = false;
				if (keyflag.compareTo("X") == 0) {
					key = true;
				}
				String rollname = field.getString()
						.substring(
								roffset,
								Math.min(roffset + rlength, field.getString()
										.length()));
				rollname = rollname.trim();
//	 System.out.println("Field: " + fieldname + " " + position);
				if (fieldname.charAt(0) != '.') {
					this.addField(new Field(fieldname, Integer
							.parseInt(position), rollname,
							getElementDescription(rollname), key));
				}
				}
			} while (data.nextRow());

//			JCO.releaseClient(client);
		} else {
			System.out.println("function not found");
		}

		rearrangeFields();
	}

	// /////////////////////////////////////////////////////////

	public String getElementDescription(String element) {
		/*IFunctionTemplate ftemplate = Main.repository
				.getFunctionTemplate("RFC_READ_TABLE");

		if (ftemplate != null) {
			JCO.Function function = ftemplate.getFunction();

			JCO.Client client = JCO.getClient(Main.SID);

			JCO.ParameterList input = function.getImportParameterList();
			input.setValue("DD04T", "QUERY_TABLE");
			ParameterList pl = function.getTableParameterList();
			JCO.Table options = pl.getTable("OPTIONS");
			options.firstRow();
			options.insertRow(0);
			options.setRow(0);
			options.setValue("ROLLNAME = '" + element
					+ "' AND DDLANGUAGE = 'E'", "TEXT");
			options.lastRow();
			pl.setValue(options, "OPTIONS");
			function.setTableParameterList(pl);

			client.execute(function);

			JCO.Table fields = function.getTableParameterList().getTable(
					"FIELDS");
			JCO.Table data = function.getTableParameterList().getTable("DATA");

			int offset = 0;
			int length = 0;
			do {
				JCO.Field f = fields.getField("FIELDNAME");
				// System.out.println(f.getString());
				// if (f.getString().compareTo("DDTEXT") == 0) {
				if (f.getString().compareTo("SCRTEXT_M") == 0) {
					// System.out.println(fields.getField("OFFSET").getString());
					// System.out.println(fields.getField("LENGTH").getString());
					offset = fields.getField("OFFSET").getInt();
					length = fields.getField("LENGTH").getInt();
					// System.out.println(offset + " " + length);
				}
			} while (fields.nextRow());

			// only one row should have been returned
			JCO.Field field = data.getField("WA");
			// System.out.println((offset + length) + " "
			// + field.getString().length() + " " + offset + " "
			// + Math.min(offset + length, field.getString().length()));
			// System.out.println(field.getString());

			String description = "";
			try {
				description = field.getString().substring(offset,
						Math.min(offset + length, field.getString().length()));
				description = description.trim();
			} catch (StringIndexOutOfBoundsException e) {
				// TODO: use another text instead
			}

			JCO.releaseClient(client);

			return description;
		} else {
			System.out.println("function not found");
		}
*/
		return "";
		
	}

	// /////////////////////////////////////////////////////////

	public void setDescription() throws NotFoundException {
		super.setDescription("todo description");
/*		IFunctionTemplate ftemplate = Main.repository
				.getFunctionTemplate("RFC_READ_TABLE");

		if (ftemplate != null) {
			JCO.Function function = ftemplate.getFunction();

			JCO.Client client = JCO.getClient(Main.SID);

			JCO.ParameterList input = function.getImportParameterList();
			input.setValue("DD02T", "QUERY_TABLE");
			ParameterList pl = function.getTableParameterList();
			JCO.Table options = pl.getTable("OPTIONS");
			options.firstRow();
			options.insertRow(0);
			options.setRow(0);
			options.setValue("TABNAME = '" + name + "' AND DDLANGUAGE ='E'",
					"TEXT");
			options.lastRow();
			pl.setValue(options, "OPTIONS");
			function.setTableParameterList(pl);

			client.execute(function);

			JCO.Table fields = function.getTableParameterList().getTable(
					"FIELDS");
			JCO.Table data = function.getTableParameterList().getTable("DATA");

			if (data.getNumRows() == 0) {
				throw new NotFoundException();
			}

			int offset = 0;
			int length = 0;
			do {
				JCO.Field f = fields.getField("FIELDNAME");
				if (f.getString().compareTo("DDTEXT") == 0) {
					offset = fields.getField("OFFSET").getInt();
					length = fields.getField("LENGTH").getInt();
				}
			} while (fields.nextRow());

			// only one row should have been returned
			data.nextRow();
			// TODO: we dont need the loop below
			for (JCO.FieldIterator e = data.fields(); e.hasMoreElements();) {
				JCO.Field field = e.nextField();
				String desc = field.getString().substring(offset,
						Math.min(offset + length, field.getString().length()));
				// System.out.println("Description: " + desc);
				super.setDescription(desc);
			}

			JCO.releaseClient(client);
		} else {
			System.out.println("function not found");
		}
		*/
	}
}
