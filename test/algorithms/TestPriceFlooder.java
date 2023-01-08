package algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_retrievers.IItemDataRetriever;
import extensions.TestItemDataRetriever;

public class TestPriceFlooder {
	
	private IItemDataRetriever dr;
	
	@BeforeEach
	void setup() {
		dr = new TestItemDataRetriever();
	}
	
	@Test
	void testPriceFlooding() throws Exception {
		
		Map<String, Float> map = new HashMap<>();
		map.put("itemOne", 1.0f);
		map.put("itemTwo", 2.0f);
		map.put("itemThree", 3.0f);
		map.put("itemFour", 4.0f);
		map.put("itemFive", 5.0f);
		map.put("itemSix", 6.0f);
		map.put("itemSeven", 7.0f);
		

		PriceFlooder.importPrices(map, dr);
		assertTrue(dr.itemExists("itemOne"));
		assertTrue(dr.itemExists("itemTwo"));
		assertTrue(dr.itemExists("itemThree"));
		assertTrue(dr.itemExists("itemFour"));
		assertTrue(dr.itemExists("itemFive"));
		assertTrue(dr.itemExists("itemSix"));
		assertTrue(dr.itemExists("itemSeven"));
	}
	
	@Test
	void testPriceFloodingError() throws Exception {
		Map<String, Float> map = new HashMap<>();
		map.put(null, null);

		PriceFlooder.importPrices(map, dr);
		assertFalse(dr.itemExists(null));
	}
	
}
