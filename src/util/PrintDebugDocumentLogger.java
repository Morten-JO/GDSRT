package util;

public class PrintDebugDocumentLogger extends DebugDocumentLogger {

	public PrintDebugDocumentLogger() {
	}

	@Override
	public void writeLineToFile(String line) {
		System.out.println(line);
	}

}
