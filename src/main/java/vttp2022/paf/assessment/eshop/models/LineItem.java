package vttp2022.paf.assessment.eshop.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// DO NOT CHANGE THIS CLASS
public class LineItem {

	private String item;
	private Integer quantity;

	public String getItem() { return this.item; }
	public void setItem(String item) { this.item = item; }

	public Integer getQuantity() { return this.quantity; }
	public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public static LineItem fromJson(JsonObject jo) {
		LineItem li = new LineItem();
		li.setItem(jo.getString("item"));
		li.setQuantity(jo.getInt("quantity"));
        return li;
    }
	@Override
	public String toString() {
		return "LineItem [item=" + item + ", quantity=" + quantity + "]";
	}

	public JsonObject toJson (){
		return Json.createObjectBuilder()
						.add("item", this.getItem())
						.add("quantity", this.getQuantity())
						.build();
	}

}
