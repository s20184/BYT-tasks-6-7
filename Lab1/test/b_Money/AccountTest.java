package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));
		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() {
		/* Tests if the addTimedPayment() method properly adds a timed payment,
		 * and removeTimedPayment() removes it.
		 */
		Money testMoney_1 = new Money(10000, SEK);
		Money testMoney_2 = new Money(1000, DKK);

		testAccount.addTimedPayment("timedPayment1", 2, 2, testMoney_1, SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("timedPayment1"));
		testAccount.removeTimedPayment("timedPayment1");
		assertFalse(testAccount.timedPaymentExists("timedPayment1"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		/* Tests if the tick() method correctly changes the balance
		 * of both Accounts involved in a TimedPayment. 
		 */
		Money testMoney_1 = new Money(10000, SEK);
		Money testMoney_2 = testMoney_1.add(testMoney_1);
		Money testMoney_3 = new Money(10000000, SEK).sub(testMoney_2);
		Money testMoney_4 = new Money(1000000, SEK).add(testMoney_2);
		
		testAccount.addTimedPayment("timedPayment1", 1, 1, testMoney_1, SweBank, "Alice");
		testAccount.tick();
		testAccount.tick();
		assertEquals(testAccount.getBalance().toString(), testMoney_3.toString());
		assertEquals(SweBank.getBalance("Alice"), testMoney_4.getAmount());
	}

	@Test
	public void testAddWithdraw() {
		/* Tests if the deposit() and withdraw() methods correctly
		 * affect the amount of Money in an Account.
		 */
		Money testMoney_1 = new Money(10000, SEK);
		Money testMoney_2 = new Money(30000, SEK);
		Money testMoney_3 = new Money(10000000, SEK).add(testMoney_1).sub(testMoney_2);
		
		testAccount.deposit(testMoney_1);
		testAccount.withdraw(testMoney_2);
		assertEquals(testAccount.getBalance().toString(), testMoney_3.toString());
		
	}
	
	@Test
	public void testGetBalance() {
		/* Tests if the getBalance() method correctly returns the Money field of the account. */
		Money testMoney_1 = new Money(10000000, SEK);
		assertEquals(testAccount.getBalance().toString(), testMoney_1.toString());
	}
}
