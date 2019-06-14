package market;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import market.items.Deal;
import market.items.DealItem;
import market.items.Product;

public class FredMeyers implements SuperMarket {
	private Map<String, Product> fProducts;
	private List<Deal> fDeals;
	
	public FredMeyers() {
		fProducts = new HashMap<String, Product>();
		fDeals = new LinkedList<Deal>();
	}
	
	/**
	 * Adds a new product to the grocery store
	 * 
	 * @param product The product to add
	 * @return True if the product did not previous exist and now does
	 * @throws IllegalArgumentException Thrown if product ID is invalid or ID conflicts with another product
	 */
	public boolean addProduct(Product product) throws IllegalArgumentException {
		String id = product.getID();
		if (id == null || id.length() < 1) {
			throw new IllegalArgumentException("Product ID must be a string with length.");
		}
		
		// Check if no product ID conflicts exist
		for(Product conflictProduct : fProducts.values()) {
			if (conflictProduct.getID().startsWith(id) || id.startsWith(conflictProduct.getID())) {
				throw new IllegalArgumentException("Product \"" + id + "\" conflicts with current product \"" + conflictProduct.getID()  + "\".");
			}
		}
		
		return fProducts.put(id, product) != null;
	}
	
	/**
	 * Removes a product from the grocery store
	 * 
	 * @param id The id of the product to remove
	 * @return True if the product used to exist and now does not
	 */
	public boolean removeProduct(String id) {
		return fProducts.remove(id) != null;
	}
	
	/**
	 * Adds a deal to the grocery store
	 * 
	 * @param deal The deal to add
	 * @return True if the deal was added
	 */
	public boolean addDeal(Deal deal) {
		return fDeals.add(deal);
	}
	
	/**
	 * Removes a deal to the grocery store
	 * 
	 * @param deal The deal to remove
	 * @return True if the deal was removed
	 */
	public boolean removeDeal(Deal deal) {
		return fDeals.remove(deal);
	}
	
	/**
	 * Gets the number of items being checked out
	 * 
	 * @param items The item count
	 * @param products The product that could exist
	 * @return A count of all products in the check out
	 * @throws IllegalArgumentException Thrown if product ID does not match a store product
	 */
	private Map<Product, Integer> getItemCounts(String items, Iterable<Product> products) throws IllegalArgumentException {
		Map <Product, Integer> itemCounts = new HashMap<Product, Integer>();
		for (int i = 0; i < items.length();) {
			boolean foundProduct = false;
			for(Product product : products) {
				String id = product.getID();
				if (i + id.length() < items.length() + 1 && items.substring(i, i + id.length()).equals(id)) {
					Integer count = itemCounts.get(product);
					if (count == null) {
						count = 0;
					}
					itemCounts.put(product, ++count);
					i += id.length();
					foundProduct = true;
					break;
				}
			}
			if (!foundProduct) {
				throw new IllegalArgumentException("No product found for \"" + items.substring(i) + "\".");
			}
		}
		return itemCounts;
	}
	
	/**
	 * Gets the maximum number of times a deal can be applied
	 * 
	 * @param deal The deal to be applied
	 * @param itemCount The items in checkout
	 * @return The maximum number of times the deal can be applied
	 */
	private int getMaxDealCount(Deal deal, Map<Product, Integer> itemCount) {
		int dealCount = Integer.MAX_VALUE;
		for(DealItem item : deal.getItemsRequired()) {
			int maxCount = itemCount.get(item.getProduct()) / item.getCount();
			if (maxCount < dealCount) {
				dealCount = maxCount;
			}
		}
		return dealCount;
	}
	
	/**
	 * Applies a deal to the item count <br>
	 * <b>Note:</b> Items will be subtracted from the item count
	 * 
	 * @param deal The deal to apply
	 * @param itemCount The count of all the items
	 * @return The cost of the deal
	 */
	private int applyDeal(Deal deal, Map<Product, Integer> itemCount) {
		int count = getMaxDealCount(deal, itemCount);
		
		// Remove deal items from count
		for(DealItem item : deal.getItemsRequired()) {
			int value = itemCount.get(item.getProduct()) - (count * item.getCount());
			itemCount.put(item.getProduct(), value);
		}
		
		return deal.getCost(count);
	}
	
	/**
	 * Gets the final cost of everything at the checkout
	 * 
	 * @param itemCount The count of all the items
	 * @param deals The deals which could be applied
	 * @return The cost of all the items
	 */
	private int getCost(Map<Product, Integer> itemCount, List<Deal> deals) {
		int totalCount = 0;
		
		// Apply Deals
		for (Deal deal : deals) {
			totalCount += applyDeal(deal, itemCount);
		}
		
		// Add remaining base count
		for (Entry<Product, Integer> item : itemCount.entrySet()) {
			totalCount += item.getKey().getCost(item.getValue());
		}
		return totalCount;
	}

	@Override
	public int checkout(String items) throws IllegalArgumentException {
		Map <Product, Integer> itemCounts = getItemCounts(items, fProducts.values());
		return getCost(itemCounts, fDeals);
	}
}
