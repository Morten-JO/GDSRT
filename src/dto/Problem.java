package dto;

public class Problem {

	public enum ProblemResult {
		HANDLED, IGNORED, DENIED
	}
	private int problemId;
	private ProblemResult problemResult;
	
	public int getProblemId() {
		return problemId;
	}
	public void setProblemId(int problemId) {
		this.problemId = problemId;
	}
	public ProblemResult getProblemResult() {
		return problemResult;
	}
	public void setProblemResult(ProblemResult problemResult) {
		this.problemResult = problemResult;
	}
}
