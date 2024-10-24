package bankApp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Klassen innehåller logiken för att hantera bankkunder och deras bankkonton.
 * Den innehåller metoder för att skapa kunder, hantera kundinformation och bankkonton, samt göra insättningar och uttag.
 * Klassen har också metoder för att hämta information om kunder och deras konton.
 * 
 *  @author Klara Svensson
 */ 
public class BankLogic implements Serializable{
	private ArrayList<Customer> customers;
	
	
    /**
     * Konstruktorn för BankLogic-klassen skapar en ny instans av BankLogic med en tom lista över kunder.
     */
	public BankLogic() {
		customers = new ArrayList<Customer>();
	}
    
    public void clearCustomers() {
        customers.clear();
    }

	/**
     * Metoden "getAllCustomers" används för att hämta information om alla bankens kunder.
     * 
     * @return En lista av strängar som representerar kundinformationen.
     */
	public List<String> getAllCustomers(){
		List<String> allCustomerInfo = new ArrayList<>(); // Den temporära listan
		for (Customer customer : customers) {// Gå igenom customers och ta varje objekt och gör en string av, spara denna i allCustomerInfo
			String customerInfo = customer.getCustomerInformation();
			allCustomerInfo.add(customerInfo);
		}
		return allCustomerInfo;
	}
	
	
    /**
     * Metoden "createCustomer" används för att skapa en ny kund och lägga till denne i bankens kundlista.
     * 
     * @param name - Kundens förnamn.
     * @param surname - Kundens efternamn.
     * @param pNo - Kundens personnummer.
     * @return true om kunden skapas, annars false (om personnumret redan finns).
     */
	public boolean createCustomer(String name, String surname, String pNo){
		
		if (checkCustomer(pNo) == null) {
			
			Customer newCustomer = new Customer(name, surname, pNo); // Skapar en ny instans i Customer klasen via konstruktor.
			customers.add(newCustomer); //Nya kunden som skapas läggs till i banklogiclistan med kunder
			
			return true;
		}
		return false;
	}
	
	
    /**
     * Metoden "getCustomer" används för att hämta information om en specifik kund och dess bankkonton.
     * 
     * @param pNo - Personnumret för den kund som ska hämtas.
     * @return En lista av strängar som representerar kundens information och bankkonton.
     */	
	public List<String> getCustomer(String pNo){
		List<String> customerAccountInfo = new ArrayList<>();
		Customer foundCustomer = null;
		
		foundCustomer = checkCustomer(pNo);
		
		if (foundCustomer == null) { // returnerar null om kunden inte hittats
			return null; 
		}
		
		String customerInfo = foundCustomer.getCustomerInformation(); // Här hämtar vi personnummer och namn och lägger till i Listan
		customerAccountInfo.add(customerInfo);
		
		List<Account> customerAccounts = foundCustomer.getAccounts();
		for (Account account : customerAccounts) {
			String accountInfo = account.getAccountNr() + " " + formatCurrency(account.getBalance()) + " " + account.getAccountType() + " " + formatInterest(account.getInterestRate());
			customerAccountInfo.add(accountInfo);
		}
		
		
		return customerAccountInfo;
	}
	
	
	/**
   * En privat metod för att formatera kontoinformation utefter instruktioner
   *
   * @param interest - Räntan som ska formateras
   *          
   * @return en string med formaterad ränta
   */	
	private String formatInterest(BigDecimal interest) {
		NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv","SE"));
		percentFormat.setMaximumFractionDigits(1); // Anger att vi vill ha max 1 decimal
		String percentStr = percentFormat.format(interest.divide(new BigDecimal(100)));
		return percentStr;
	}
	
	
	/**
   * En privat metod för att formatera kontoinformation utefter instruktioner
   *
   * @param balance - saldot som ska formateras
   *          
   * @return en string med formaterad saldo
   */	
	private String formatCurrency(BigDecimal balance) {
		String balanceStr = NumberFormat.getCurrencyInstance(new Locale("sv","SE")).format(balance);
		return balanceStr;
	}
	

    /**
     * Metoden "changeCustomerName" används för att ändra kundens förnamn och/eller efternamn.
     * 
     * @param name - Det nya förnamnet.
     * @param surname - Det nya efternamnet.
     * @param pNo - Personnumret för den kund vars namn ska ändras.
     * @return true om namnändring lyckades, annars false.
     */
	public boolean changeCustomerName(String name, String surname, String pNo) {
		Customer foundCustomer = null;
		
		// kontrollerar personnr mot befintliga kunder och returnerar false om personnr redan finns
		for (Customer customer : customers) { 
			if (customer.getPersonNr().equals(pNo)) { // om personnummer hittas gå in och hämta konto info
				foundCustomer = customer;
				break;
			}
		}
		if (foundCustomer == null) { // returnerar null om kunden inte hittats
			return false; 
		}
		if (name != "") { //Om förnamn lämnas tomt byt endast efternamn
			String tempSurname = foundCustomer.getLName();
			foundCustomer.changeName(name, tempSurname);
		}
		else if (surname != "") { //Om efternamn lämnas tomt byt endast förnamn
			String tempFirstname = foundCustomer.getFName();
			foundCustomer.changeName(tempFirstname, surname);
		}
		else if (name == "" && surname == "") { // Om båda är tomma returnera false(inga namnbyten gjorda för inga önskades)
			return false;
		}
		else {
			foundCustomer.changeName(name, surname); // om båda är ifyllda byt både för- och efternamn
		}
		return true;
	}

	
	/**
	 * Skapar ett nytt sparkonto för en befintlig kund.
	 * 
	 * @param pNo - Personnummer för kunden som ska få ett sparkonto.
	 * @return Kontonumret för det nya sparkontot, eller -1 om kunden inte hittades.
	 */

	public int createSavingsAccount(String pNo) {
		Customer foundCustomer = null;
		
		foundCustomer = checkCustomer(pNo);
		if (foundCustomer == null) { // returnerar -1 om kunden inte hittats
			return -1; 
		}
		
		Account newAccount = new SavingsAccount();
		foundCustomer.addAccount(newAccount);
		return newAccount.getAccountNr();
	}

	
	/**
     * Metoden "createSavingsAccount" används för att skapa ett sparkonto åt en kund.
     * 
     * @param pNo - Personnumret för kunden som ska få sparkontot.
     * @return Kontonumret för det skapade sparkontot, eller -1 om kunden inte hittades.
     */
	public int createCreditAccount(String pNo) {
		Customer foundCustomer = null;
		
		foundCustomer = checkCustomer(pNo);
		if (foundCustomer == null) { // returnerar -1 om kunden inte hittats
			return -1; 
		}
		
		Account newAccount = new CreditAccount();
		foundCustomer.addAccount(newAccount);
		return newAccount.getAccountNr();
		
	}


	/**
	 * Kontrollera om en kund med angivet personnummer redan finns i kundlistan.
	 * 
	 * @param pNo - Kundens personnummer.
	 * @return Kunden om den hittades, annars null.
	 */
    private Customer checkCustomer(String pNo) {
    	// kontrollerar personnr mot befintliga kunder och returnerar false om personnr redan finns
		for (Customer customer : customers) { 
			if (customer.getPersonNr().equals(pNo)) { // om personnummer hittas
				return customer;
			}
		}
		return null;
	}
    
    
    /**
     * Metoden "getAccount" används för att hämta information om ett specifikt bankkonto för en kund.
     * 
     * @param pNo - Personnumret för kunden som äger bankkontot.
     * @param accountId - Kontonumret för det bankkonto som ska hämtas.
     * @return En sträng som representerar bankkontoinformationen, eller null om kunden eller kontot inte hittas.
     */	
	public String getAccount(String pNo, int accountId) {
		Customer foundCustomer = null;
		foundCustomer = checkCustomer(pNo);
		
		if (foundCustomer == null) { // returnerar null om kunden inte hittats
			return null; 
		}
		
		List<Account> foundCustomerAccounts = foundCustomer.getAccounts();
		for (Account account : foundCustomerAccounts) {
			if(account.getAccountNr() == accountId) {
				String accountInfo = account.getAccountNr() + " " + formatCurrency(account.getBalance()) + " " + account.getAccountType() + " " + formatInterest(account.getInterestRate());
				return accountInfo;
			}
		}
		
		return null; // returnerar null om önskat konto inte hittas bland kundens konton
	}
	
	
    /**
     * Metoden "deposit" används för att göra en insättning på ett bankkonto.
     * 
     * @param pNo - Personnumret för kunden som äger bankkontot.
     * @param accountId - Kontonumret för det bankkonto som insättningen ska göras på.
     * @param amount - Beloppet som ska sättas in.
     * @return true om insättningen lyckades, annars false om kunden eller kontot inte hittas.
     */
	public boolean deposit(String pNo, int accountId, int amount) {
		Customer foundCustomer = null;
		
		foundCustomer = checkCustomer(pNo);
		
		
		// returnerar null om kunden inte hittats
		if (foundCustomer == null) { 
			System.out.println("Fel här 1");
			return false; 
		}
		
		
		List<Account> foundCustomerAccounts = foundCustomer.getAccounts();
		for (Account account : foundCustomerAccounts) {
			if(account.getAccountNr() == accountId) {
				Boolean returnIs;
				returnIs = account.deposit(new BigDecimal(amount));  // Gör insättningen
				return returnIs;
			}
		}
		
		return false; // returnerar false om önskat konto inte hittas bland kundens konton
	}
	
	
    /**
     * Metoden "withdraw" används för att göra ett uttag från ett bankkonto.
     * 
     * @param pNo - Personnumret för kunden som äger bankkontot.
     * @param accountId - Kontonumret för det bankkonto som uttaget ska göras från.
     * @param amount - Beloppet som ska tas ut.
     * @return true om uttaget lyckades, annars false om kunden eller kontot inte hittas eller om uttaget är ogiltigt.
     */
	public boolean withdraw(String pNo, int accountId, int amount) {
		Customer foundCustomer = null;
		
		foundCustomer = checkCustomer(pNo);
				
	
		// returnerar null om kunden inte hittats
		if (foundCustomer == null) { 
			System.out.println("Fel här 1");
			return false; 
		}
		
		List<Account> foundCustomerAccounts = foundCustomer.getAccounts();
		for (Account account : foundCustomerAccounts) {
			if(account.getAccountNr() == accountId) {
				Boolean returnIs;
				returnIs = account.withdraw(new BigDecimal(amount)); // Gör uttaget
				return returnIs;
			}
		}
		
		// returnerar false om önskat konto inte hittas bland kundens konton
		return false; 
	}
	
	
    /**
     * Metoden "closeAccount" används för att stänga ett bankkonto och returnera information om kontots status.
     * 
     * @param pNo - Personnumret för kunden som äger bankkontot.
     * @param accountId - Kontonumret för det bankkonto som ska stängas.
     * @return En sträng som representerar information om det stängda kontot, eller null om kunden eller kontot inte hittas.
     */
	public String closeAccount(String pNo, int accountId) {
		Customer foundCustomer = null;
		
		// kontrollerar personnr mot befintliga kunder och returnerar false om personnr redan finns
		for (Customer customer : customers) { 
			if (customer.getPersonNr().equals(pNo)) { // om personnummer hittas gå in och hämta konto info
				foundCustomer = customer;
				break;
			}
		}
	
		if (foundCustomer == null) { // returnerar null om kunden inte hittats
			return null; 
		}
		
		List<Account> foundCustomerAccounts = foundCustomer.getAccounts();
		for (Account account : foundCustomerAccounts) {
			if(account.getAccountNr() == accountId) {
				String closedAccountInfo = account.getAccountNr() + " " + formatCurrency(account.getBalance()) + " " + account.getAccountType() + " " + formatCurrency(account.calculateInterest());
				foundCustomer.removeAccount(account); // Tar bort kontot från kunden
				return closedAccountInfo;
			}
		}
		
		return null; // returnerar null om önskat konto inte hittas bland kundens konton
	}
	
	
    /**
     * Metoden "deleteCustomer" används för att ta bort en kund och alla dess bankkonton från banken.
     * 
     * @param pNo - Personnumret för den kund som ska tas bort.
     * @return En lista av strängar som representerar information om de borttagna kontona och kunden, eller null om kunden inte hittas.
     */
	public List<String> deleteCustomer(String pNo){
		Customer foundCustomer = null;
		
		// kontrollerar personnr mot befintliga kunder och returnerar false om personnr redan finns
		for (Customer customer : customers) { 
			
			if (customer.getPersonNr().equals(pNo)) { // om personnummer hittas gå in och hämta konto info
				foundCustomer = customer;
				break;
			}
		}
	
		if (foundCustomer == null) { // returnerar null om kunden inte hittats
			return null; 
		}
		
		List<Account> foundCustomerAccounts = foundCustomer.getAccounts();
		List<String> returnClosedCustomer = new ArrayList<>();
		
		String closedAccountInfo = foundCustomer.getCustomerInformation();
		returnClosedCustomer.add(closedAccountInfo);
		
		for (Account account : foundCustomerAccounts) {
			closedAccountInfo = account.getAccountNr() + " " + formatCurrency(account.getBalance()) + " " + account.getAccountType() + " " + formatCurrency(account.calculateInterest());
			returnClosedCustomer.add(closedAccountInfo);
		}
		
		customers.remove(foundCustomer); // Tar bort kund och dess konton från banken
		return returnClosedCustomer; // Returnerar information om de borttagna kontona och kunden
	}
	

    /**
     * Metoden "getTransactions" används för att hämta en lista med transaktioner kopplat till ett kundkonto
     * 
     * @param accountId - Kontonummer vars transaktioner ska hämtas
     * @param pNo - Personnumret för den kund som står på personummer
     * @return En lista med alla transaktionerna kopplade till kontot eller null om transaktionerna inte finns.
     */
	public ArrayList<String> getTransactions(String pNo, int accountId){	
		Customer foundCustomer = null;

		
		foundCustomer = checkCustomer(pNo);
		if (foundCustomer == null) {
			return null;
		}
		
		List<Account> theAccounts = foundCustomer.getAccounts();
		
		for (Account account : theAccounts) {
			if (accountId == account.getAccountNr()) {
				return account.getAccountTransactions(accountId);
			}
		}
		return null;
		
	}
	
	public Customer customerObject(String pNo) {

	    String thePNo;
	    Customer customerObject = null;

	    for (int i = 0; i < customers.size(); i++) {
	    	thePNo = customers.get(i).getPersonNr();
	      {
	        if (thePNo.equals(pNo)) {
	        	customerObject = customers.get(i);
	        }
	      }
	    }
	    return customerObject;
	}
}
