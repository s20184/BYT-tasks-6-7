package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates. */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		NOK = new Currency("NOK", 0.11);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		/* Tests if the getter returns proper values for each currency. */
		assertEquals("SEK", SEK.getName());
		assertEquals("DKK", DKK.getName());
		assertEquals("NOK", NOK.getName());
		assertEquals("EUR", EUR.getName());
	}
	
	@Test
	public void testGetRate() {
		/* Tests if the getter for rate returns correct values ascribed to each currency. */
		assertEquals((Double) 0.15, SEK.getRate(), 0.000001);
		assertEquals((Double) 0.20, DKK.getRate(), 0.000001);
		assertEquals((Double) 1.5, EUR.getRate(), 0.000001);
	}
	
	@Test
	public void testSetRate() {
		/* Tests if the setter correctly assigns new value to rates of currencies. */
		Double rate = 0.5;
		DKK.setRate(rate);
		assertTrue(rate == DKK.getRate());
	}
	
	@Test
	public void testGlobalValue() {
		/* Tests if universal value is correctly calculated from the currencies from setup. */
		assertEquals("The global value of 100 SEK should be 100*0.15=15", (Integer) 15, SEK.universalValue(100));
		assertEquals("The global value of 100 DKK should be 100*0.20=20", (Integer) 20, DKK.universalValue(100));
		assertEquals("The global value of 100 NOK should be 100*0.11=11", (Integer) 11, NOK.universalValue(100));
		assertEquals("The global value of 100 EUR should be 100*1.5=150", (Integer) 150, EUR.universalValue(100));
	}
	
	@Test
	public void testValueInThisCurrency() {
		/* Tests if currencies are correctly exchanged to each other. */
		assertEquals("The value of 100 EUR converted to DKK should be 100*(1.5/0.2)=750", 
				(Integer) 750, DKK.valueInThisCurrency(100, EUR));
		assertEquals("The amount 100 SEK converted to NOK should be 100*(0.15/0.11)=136", 
				(Integer) 136, NOK.valueInThisCurrency(100, SEK));
	}

}
