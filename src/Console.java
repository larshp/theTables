public class Console {

	private static String text = "";

	public static String getText() {
		return text;
	}

	public static void addLine(String s) {
		text = text + "\n" + s;
	}

	public static void addChar(char c) {
		text = text + c;
	}

	public static void clear() {
		text = "";
	}
	
	public static void addNewLine() {
		text = text + "\n";
	}

	public static void parseCommand() {
		String[] lines = text.split("\n");
		if (lines[lines.length - 1].startsWith("add")) {
			Commands.add(lines[lines.length - 1]);
		} else if (lines[lines.length - 1].startsWith("clear")) {
			Commands.clear();
		} else if (lines[lines.length - 1].startsWith("help")) {
			Commands.help();
		} else if (lines[lines.length - 1].startsWith("savepng")) {
			Commands.savepng();
		} else if (lines[lines.length - 1].startsWith("savepdf")) {
			Commands.savepdf();
		} else if (lines[lines.length - 1].startsWith("decfont")) {
			Commands.decFont();
		} else if (lines[lines.length - 1].startsWith("incfont")) {
			Commands.incFont();
		} else if (lines[lines.length - 1].startsWith("jco")) {
			Commands.jCO(lines[lines.length - 1]);
		} else if (lines[lines.length - 1].startsWith("setsize")) {
			Commands.setsize(lines[lines.length - 1]);	
		} else if (lines[lines.length - 1].startsWith("getsize")) {
			Commands.getsize();			
		} else if (lines[lines.length - 1].startsWith("save")) {
			Commands.save();
		} else if (lines[lines.length - 1].startsWith("load")) {
			Commands.load();			
		} else if (lines[lines.length - 1].startsWith("quit")
				|| lines[lines.length - 1].startsWith("exit")) {
			Commands.quit();
		} else {
			Console.addLine("Unknown command");
		}
		Console.addNewLine();
	}

	public static void removeLastCharacter() {
		if(text.length() > 0 && text.substring(text.length()-1).compareTo("\n")!=0) {
			text = text.substring(0, text.length()-1);
		}
	}
}
