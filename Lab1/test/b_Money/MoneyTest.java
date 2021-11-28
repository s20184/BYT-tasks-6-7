package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		/* Tests if the getAmount() method returns proper integer values for the amount of each Money. */
		assertEquals((Integer) 10000, SEK100.getAmount());
		assertEquals((Integer) 1000, EUR10.getAmount());
		assertEquals((Integer) 20000, SEK200.getAmount());
		assertEquals((Integer) 2000, EUR20.getAmount());
		assertEquals((Integer) 0, SEK0.getAmount());
		assertEquals((Integer) 0, EUR0.getAmount());
		assertEquals((Integer) (-10000), SEKn100.getAmount());
	}

	@Test
	public void testGetCurrency() {
		/* Tests if the getCurrency() method returns a proper Currency object with correct field values. */
		Currency test_1 = SEK100.getCurrency();
		assertEquals(test_1.getClass(), SEK.getClass());
		assertEquals(test_1.getName(), SEK.getName());
		assertEquals(test_1.getRate(), SEK.getRate());
		
		Currency test_2 = EUR10.getCurrency();
		assertEquals(test_2.getClass(), EUR.getClass());
		assertEquals(test_2.getName(), EUR.getName());
		assertEquals(test_2.getRate(), EUR.getRate());
	}

	@Test
	public void testToString() {
		/* Tests if the toString() method returns a proper String with both the amount and currency name of the provided Money. */
		assertEquals("100.0 SEK", SEK100.toString());
		assertEquals("0.0 SEK", SEK0.toString());
		assertEquals("200.0 SEK", SEK200.toString());
		assertEquals("-100.0 SEK", SEKn100.toString());
		assertEquals("0.0 EUR", EUR0.toString());
		assertEquals("10.0 EUR", EUR10.toString());
		assertEquals("20.0 EUR", EUR20.toString());
	}

	@Test
	public void testGlobalValue() {
		/* Tests if each Money's amount is correctly converted to its universal value. 
		 * This is assuming, however, that the conversion also works correctly in Currency 
		 * (I'm not sure if this is the correct approach).
		 */
		assertEquals(SEK.universalValue(10000), SEK100.universalValue());
		assertEquals(SEK.universalValue(0), SEK0.universalValue());
		assertEquals(SEK.universalValue(20000), SEK200.universalValue());
		assertEquals(SEK.universalValue(-10000), SEKn100.universalValue());
		assertEquals(EUR.universalValue(0), EUR0.universalValue());
		assertEquals(EUR.universalValue(2000), EUR20.universalValue());
		assertEquals(EUR.universalValue(1000), EUR10.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		/* Tests if Money amounts are compared to each other correctly. */
		assertTrue("0 SEK is equal to 0 EUR!", SEK0.equals(EUR0));
		assertFalse("10 EUR is not equal to 20 EUR!", EUR10.equals(EUR20));
		assertTrue("10 EUR is equal to 100 SEK!", EUR10.equals(SEK100));
	}

	@Test
	public void testAdd() {
		/* Tests if Money amounts are added correctly, also in cases where currency exchange has to occur. */
		Money test_1 = EUR20.add(EUR20);
		assertTrue("EUR20 added to EUR20 should amount to EUR40!", test_1.getAmount().equals(4000));
		Money test_2 = SEK100.add(SEK200);
		assertTrue("SEK200 added to SEK100 should amount to SEK300!", test_2.getAmount().equals(30000));
		Money test_3 = EUR10.add(SEK100);
		assertTrue("SEK100 added to EUR10 should amount to EUR20!", test_3.getAmount().equals(EUR20.getAmount()));
	}

	@Test
	public void testSub() {
		/* Tests if Money amounts are subtracted correctly, also in cases where currency exchange has to occur. */
		Money test_1 = EUR20.sub(EUR10);
		assertEquals("EUR10 subtracted from EUR20 should amount to EUR10!", test_1.getAmount(), EUR10.getAmount());
		Money test_2 = SEK200.sub(SEK100);
		assertEquals("SEK100 subtracted from SEK200 should amount to SEK100!", test_2.getAmount(), SEK100.getAmount());
		Money test_3 = EUR10.sub(SEK100);
		assertEquals("SEK100 subtracted from EUR10 should amount to 0!", test_3.getAmount(), (Integer) 0);
		assertEquals("SEK100 subtracted from EUR10 should be equal to EUR0!", test_3.getAmount(), EUR0.getAmount());
	}

	@Test
	public void testIsZero() {
		/* Tests if the isZero() method correctly returns true if Money amount is equal to 0.0, and false otherwise. */
		assertTrue(SEK0.isZero());
		assertTrue(EUR0.isZero());
		assertFalse(SEK100.isZero());
		assertFalse(SEKn100.isZero());
		assertFalse(SEK200.isZero());
		assertFalse(EUR10.isZero());
		assertFalse(EUR20.isZero());
	}

	@Test
	public void testNegate() {
		/* Tests if the negate() method correctly negates the amount of money in a newly returned Money instance. */
		assertEquals("SEK100 negated should be equal to SEKn100!", SEK100.negate().getAmount(), SEKn100.getAmount());
		assertEquals("SEKn100 negated should be equal to SEK100!", SEKn100.negate().getAmount(), SEK100.getAmount());
	}

	@Test
	public void testCompareTo() {
		/* Tests if the negate() compareTo correctly returns:
		 * 0 if the two Monies are equal,
		 * A positive integer if this Money is more valuable than other Money
		 * A negative integer if this Money is less valuable than other Money. */
		assertEquals("EUR10 and SEK100 are equal!", EUR10.compareTo(SEK100), 0);
		assertTrue("EUR20 is more valuable than SEK100!", EUR20.compareTo(SEK100)>0);
		assertFalse("SEK100 is less valuable than EUR20!", SEK100.compareTo(EUR20)>0);
	}
}
