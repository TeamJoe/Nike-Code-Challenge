package market.items;

/**
 * An Item which connects a Product with a Count. This is usually used to determine how many products are required to make a deal work.
 */
public class DealItem {
	private Product fProduct;
	private int fCount;
	
	/**
	 * An item attached to a count
	 * 
	 * @param product The item
	 * @param count The count
	 */
	public DealItem(Product product, int count) {
		fProduct = product;
		fCount = count;
	}
	
	/**
	 * Gets the item
	 */
	public Product getProduct() {
		return fProduct;
	}
	
	/**
	 * Gets the count
	 */
	public int getCount() {
		return fCount;
	}
}
