package dto;

public class Problem {

	public enum ProblemResult {
		HANDLED, IGNORED, DENIED
	}
	
	
	private int problemId;
	private ProblemResult problemResult;
	
}
