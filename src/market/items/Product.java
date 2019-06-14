package market.items;

/**
 * A product is a specific item in the store
 */
public class Product {
	private final String fId;
	private int fCost;
	
	/**
	 * A product at the grocery store
	 * 
	 * @param id The id of the product
	 * @param cost The base cost of the product
	 */
	public Product(String id, int cost) {
		fId = id;
		fCost = cost;
	}
	
	/**
	 * Gets the Product ID
	 */
	public String getID() {
		return fId;
	}
	
	/**
	 * Gets the cost of the product for a specific count
	 * 
	 * @param count The count to get
	 */
	public int getCost(int count) {
		return fCost * count;
	}
	
	@Override
	public int hashCode() {
		return fId.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Product) {
			return fId.equals(((Product) obj).fId);
		}
		
		return super.equals(obj);
	}
}
