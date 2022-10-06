package dto;

public class Percentage {

	private int percentage = 0;
	
	public Percentage(int value) {
		setPercentage(value);
	}
	
	public void setPercentage(int value) {
		if(value <= 100 && value >= 0) {
			percentage = value;
		}
	}
	
	public int getPercentage() {
		return percentage;
	}
	
}
