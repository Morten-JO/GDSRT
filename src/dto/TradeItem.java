package dto;

public class TradeItem {

	private String itemId;
	private int quantity;
	
	public TradeItem(String itemId, int quantity) {
		this.itemId = itemId;
		this.quantity = quantity;
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
}
