package test;

import market.FredMeyers;
import market.items.Deal;
import market.items.DealItem;
import market.items.Product;
import junit.framework.TestCase;

public class FredMeyersTests extends TestCase {
	public void test_CheckOutNoProductsNoItems() {
		FredMeyers store = new FredMeyers();
		assertEquals(0, store.checkout(""));
	}
	
	public void test_CheckOutNoProducts() {
		FredMeyers store = new FredMeyers();
		try {
			store.checkout("A");
			fail("No exception thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("Failed on wrong argument: " + ex.getMessage(), true, ex.getMessage().contains("\"A\""));
		}
	}
	
	public void test_CannotAddProductWithNullID() {
		FredMeyers store = new FredMeyers();
		try {
			store.addProduct(new Product(null, 0));
			fail("No exception thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("Failed with wrong exception: " + ex.getMessage(), true, ex.getMessage().contains("Product ID"));
		}
	}
	
	public void test_CannotAddProductWithEmptyID() {
		FredMeyers store = new FredMeyers();
		try {
			store.addProduct(new Product("", 0));
			fail("No exception thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("Failed with wrong exception: " + ex.getMessage(), true, ex.getMessage().contains("Product ID"));
		}
	}
	
	public void test_CheckOutOneProduct() {
		FredMeyers store = new FredMeyers();
		store.addProduct(new Product("A", 30));
		assertEquals(30, store.checkout("A"));
	}
	
	public void test_CheckOutOneProductMultipleTimes() {
		FredMeyers store = new FredMeyers();
		store.addProduct(new Product("A", 30));
		assertEquals(90, store.checkout("AAA"));
	}
	
	public void test_CannotAddProductTwice() {
		FredMeyers store = new FredMeyers();
		try {
			store.addProduct(new Product("A", 0));
			store.addProduct(new Product("A", 0));
			fail("No exception thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("Failed with wrong exception: " + ex.getMessage(), true, ex.getMessage().contains("Product \"A\"") && ex.getMessage().contains("product \"A\""));
		}
	}
	
	public void test_CheckOutOneProductWithInvalidProduct() {
		FredMeyers store = new FredMeyers();
		store.addProduct(new Product("A", 30));
		try {
			store.checkout("AAABA");
			fail("No exception thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("Failed on wrong argument: " + ex.getMessage(), true, ex.getMessage().contains("\"BA\""));
		}
	}
	
	public void test_CheckOutMultiProductsOneTimeEach() {
		FredMeyers store = new FredMeyers();
		store.addProduct(new Product("A", 30));
		store.addProduct(new Product("B", 50));
		store.addProduct(new Product("C", 20));
		assertEquals(100, store.checkout("ABC"));
	}
	
	public void test_CheckOutMultiProductsMultiTimesEach() {
		FredMeyers store = new FredMeyers();
		store.addProduct(new Product("A", 30));
		store.addProduct(new Product("B", 50));
		store.addProduct(new Product("C", 20));
		assertEquals(360, store.checkout("ABBACBBAB"));
	}
	
	public void test_CheckOutMultiProductsWithInvalidProduct() {
		FredMeyers store = new FredMeyers();
		store.addProduct(new Product("A", 20));
		store.addProduct(new Product("B", 50));
		store.addProduct(new Product("C", 30));
		try {
			store.checkout("ABBACBDBAB");
			fail("No exception thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("Failed on wrong argument: " + ex.getMessage(), true, ex.getMessage().contains("\"DBAB\""));
		}
	}
	
	public void test_CannotAddProductWithConflictingNames_ShorterNameFirst() {
		FredMeyers store = new FredMeyers();
		try {
			store.addProduct(new Product("A", 0));
			store.addProduct(new Product("BA", 0));
			store.addProduct(new Product("AB", 0));
			
			fail("No exception thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("Failed with wrong exception: " + ex.getMessage(), true, ex.getMessage().contains("Product \"AB\"") && ex.getMessage().contains("product \"A\""));
		}
	}
	
	public void test_CannotAddProductWithConflictingNames_ShorterNameLast() {
		FredMeyers store = new FredMeyers();
		try {
			store.addProduct(new Product("AB", 0));
			store.addProduct(new Product("BA", 0));
			store.addProduct(new Product("A", 0));
			
			fail("No exception thrown");
		} catch (IllegalArgumentException ex) {
			assertEquals("Failed with wrong exception: " + ex.getMessage(), true, ex.getMessage().contains("Product \"A\"") && ex.getMessage().contains("product \"AB\""));
		}
	}
	
	public void test_CheckOutMultiProductsMultiTimesEach_LongProductNames() {
		FredMeyers store = new FredMeyers();
		store.addProduct(new Product("A", 30));
		store.addProduct(new Product("BC", 50));
		store.addProduct(new Product("CD", 20));
		assertEquals(130, store.checkout("ABCCDA"));
	}
	
	public void test_CheckOutWithDeal() {
		FredMeyers store = new FredMeyers();
		store.addProduct(new Product("A", 20));
		store.addProduct(new Product("B", 50));
		store.addProduct(new Product("C", 30));
		store.addDeal(new Deal(150, new DealItem(new Product("B", 50), 5)));
		assertEquals(240, store.checkout("ABBACBBAB"));
	}
	
	public void test_CheckOutWithMultiItemDeal() {
		FredMeyers store = new FredMeyers();
		store.addProduct(new Product("A", 20));
		store.addProduct(new Product("B", 50));
		store.addProduct(new Product("C", 30));
		store.addDeal(new Deal(150, new DealItem(new Product("B", 50), 4), new DealItem(new Product("A", 20), 2)));
		assertEquals(250, store.checkout("ABBACBBAB"));
	}
}
