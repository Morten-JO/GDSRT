package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DebugDocumentLogger {

	private PrintWriter out;
	private FileWriter writer;
	private BufferedWriter bufferedWriter;
	private ArrayList<String> storedLines;
	private boolean messageStored = false;

	private File file;

	public DebugDocumentLogger() {

	}

	public DebugDocumentLogger(File file) throws IOException {
		this.file = file;
		if(!file.exists()) {
			file.createNewFile();
		}
		writer = new FileWriter(file, true);
		bufferedWriter = new BufferedWriter(writer);
		out = new PrintWriter(bufferedWriter);
		storedLines = new ArrayList<>();
	}

	public void writeLineToFile(String line) {
		if (out != null) {
			try {
				out.println(line);
				messageStored = true;
				save();
			} catch (NullPointerException e) {
				e.printStackTrace();
				storedLines.add(line);
			}
		} else {
			storedLines.add(line);
		}
	}

	public void save() {
		if (messageStored) {
			PrintWriter old = out;
			messageStored = false;
			int storedLinesSize = storedLines.size();
			for (int i = 0; i < storedLinesSize; i++) {
				old.println(storedLines.get(0));
				storedLines.remove(0);
			}
			old.flush();
			try {
				exit();
			} catch (IOException e) {
				e.printStackTrace();
			}
			out = null;
			try {
				writer = new FileWriter(file, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			bufferedWriter = new BufferedWriter(writer);
			out = new PrintWriter(bufferedWriter);

		}
	}

	public void exit() throws IOException {
		out.close();
		bufferedWriter.close();
		writer.close();
	}
}