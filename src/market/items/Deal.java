package market.items;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A deal is for when a specific set of products are purchased and the cost is the final price of the deal
 */
public class Deal {
	private final Map<Product, DealItem> fItems;
	private int fCost;
	
	/**
	 * The deal constructor
	 * 
	 * @param cost The cost of the deal
	 * @param items A Collection of all the items with their counts required in order to apply the deal
	 */
	public Deal(int cost, DealItem... items) {
		fItems = new HashMap<Product, DealItem>();
		fCost = cost;
		
		for (DealItem item : items) {
			if (fItems.put(item.getProduct(), item) != null) {
				throw new IllegalArgumentException("Product \"" + item.getProduct().getID() + "\" is present more than once.");
			}
		}
	}
	
	/**
	 * Gets all the items with their counts required in order to apply the deal
	 * 
	 * @return A Collection of all the items with their counts required in order to apply the deal
	 */
	public Collection<DealItem> getItemsRequired() {
		return fItems.values();
	}
	
	/**
	 * Gets the cost of the item
	 * 
	 * @param count The cost for the specific count for items
	 * @return The cost
	 */
	public int getCost(int count) {
		return fCost * count;
	}
}
