package dto;

public class Percentage {

	private int percentage = 0;
	
	public void setPercentage(int value) {
		if(value <= 100 && value >= 0) {
			percentage = value;
		}
	}
	
	public int getPercentage() {
		return percentage;
	}
	
}
