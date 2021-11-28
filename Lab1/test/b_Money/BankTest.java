package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		/* Tests if the getName() method returns the correct String value. */
		assertEquals(SweBank.getName(), "SweBank");
		assertEquals(Nordea.getName(), "Nordea");
		assertEquals(DanskeBank.getName(), "DanskeBank");
	}

	@Test
	public void testGetCurrency() {
		/* Tests if the getCurrency() method returns the correct Currency. */
		assertEquals(SweBank.getCurrency(), SEK);
		assertEquals(Nordea.getCurrency(), SEK);
		assertEquals(DanskeBank.getCurrency(), DKK);
	}

	@Test(expected=AccountExistsException.class)
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException {
		/* Tests if the openAccount() method correctly creates an Account in the Bank, 
		 * or throws an Exception if the Account with the same ID already exists within the Bank.
		 */
		assertNotNull(SweBank.getBalance("Bob"));
		assertNotNull(Nordea.getBalance("Bob"));
		assertNotNull(SweBank.getBalance("Ulrika"));
		assertNotNull(DanskeBank.getBalance("Gertrud"));
		SweBank.openAccount("Ulrika"); //should throw an exception
		SweBank.openAccount("Bob"); //should throw an exception
		Nordea.openAccount("Bob"); //should throw an exception
		DanskeBank.openAccount("Gertrud"); //should throw an exception
		/* Test failed. Bugs fixed:
		 * Method amended to actually create an Account in the HashTable. 
		 */
	}

	@Test(expected=AccountDoesNotExistException.class)
	public void testDeposit() throws AccountDoesNotExistException {
		/* Tests if the deposit() method correctly deposits the specified Money into the Account,
		 * and throws AccountDoesNotExistException if the Account does not exist.
		 */
		Money testMoney_1 = new Money(10000, SEK);
		Money testMoney_2 = new Money(1000, DKK);
		Money testMoney_3 = testMoney_1.add(testMoney_2);

		SweBank.deposit("Bob", testMoney_1);
		assertEquals("The amount of Money in Bob's account should be equal to SEK100", 
				SweBank.getBalance("Bob"), testMoney_1.getAmount());
		SweBank.deposit("Bob", testMoney_2);
		assertEquals("The amount of Money in Bob's account should be equal to SEK100+DKK10", 
				SweBank.getBalance("Bob"), testMoney_3.getAmount());
		SweBank.deposit("Gertrud", testMoney_1); //should throw an exception
		/* Test failed. Bugs fixed:
		 * AccountDoesNotExistException being thrown when the account actually exists.
		 */
	}

	@Test(expected=AccountDoesNotExistException.class)
	public void testWithdraw() throws AccountDoesNotExistException {
		/* Tests if the withdraw() method correctly withdraws the specified Money from the Account,
		 * and throws AccountDoesNotExistException if the Account does not exist.
		 */
		Money testMoney_1 = new Money(1000, DKK);
		Money testMoney_2 = new Money(10000, SEK);

		DanskeBank.withdraw("Gertrud", testMoney_1);
		assertEquals("The amount of Money in Gertrud's account should be equal to -DKK10",
				DanskeBank.getBalance("Gertrud"), testMoney_1.negate().getAmount());
		SweBank.withdraw("Bob", testMoney_2);
		assertEquals("The amount of Money in Bob's account should be equal to -SEK100",
				DanskeBank.getBalance("Bob"), testMoney_2.negate().getAmount());
		SweBank.withdraw("Gertrud", testMoney_1); //should throw an exception
		/* Test failed. Bugs fixed:
		 * Money being deposited instead of being withdrawn. Found out through checking the balance.
		 */

	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testGetBalance() throws AccountDoesNotExistException {
		/* Tests if the getBalance() method returns the Money amount assigned to this Account,
		 * and throws an exception if the account does not exist.
		 */
		Money testMoney_1 = new Money(10000, SEK);
		Money testMoney_2 = new Money(1000, DKK);

		SweBank.deposit("Bob", testMoney_1);
		SweBank.deposit("Bob", testMoney_2);
		SweBank.withdraw("Bob", testMoney_2);
		
		assertEquals(SweBank.getBalance("Bob"), testMoney_1.getAmount());
		assertEquals(SweBank.getBalance("Ulrika"), (Integer) 0);
		SweBank.getBalance("Gertrud"); //should throw an exception
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testTransfer() throws AccountDoesNotExistException {
		/* Tests if the transfer() method correctly 
		 * withdraws the amount from one Account, deposits it into another Account,
		 * and if one of the Accounts doesn't exist - throws an Exception.
		 * Also checks the method for transfers occurring within the same Bank.
		 */
		Money testMoney_1 = new Money(10000, SEK);
		Money testMoney_2 = new Money(1000, DKK);
		Money testMoney_3 = new Money(DKK.valueInThisCurrency(10000, SEK), DKK);
		
		Nordea.transfer("Bob", DanskeBank, "Gertrud", testMoney_1);
		assertEquals("Getrud's balance should now be equal to SEK100", 
				DanskeBank.getBalance("Gertrud"), testMoney_3.getAmount());
		assertEquals("Bob's balance should now be equal to -SEK100", 
				Nordea.getBalance("Bob"), testMoney_1.negate().getAmount());
		
		SweBank.transfer("Bob", "Ulrika", testMoney_1);
		assertEquals("Ulrika's balance should now be equal to SEK100", 
				DanskeBank.getBalance("Ulrika"), testMoney_1.getAmount());
		assertEquals("Bob's balance should now be equal to -SEK100", 
				SweBank.getBalance("Bob"), testMoney_1.negate().getAmount());
		
		SweBank.transfer("Gertrud", SweBank, "Bob", testMoney_2); //should throw an exception
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		/* Tests if the addTimedPayment() method properly adds a timed payment,
		 * and removeTimedPayment() removes it.
		 * DOES NOT if both methods throw an Exception if one of the Accounts doesn't exist,
		 * as this doesn't exist in JavaDoc specification. */
		Money testMoney_1 = new Money(10000, SEK);

		SweBank.addTimedPayment("Bob", "timedPayment1", 2, 2, testMoney_1, DanskeBank, "Gertrud");
		SweBank.removeTimedPayment("Bob", "timedPayment1");
	}
}
