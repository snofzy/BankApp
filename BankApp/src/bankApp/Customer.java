package bankApp;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
/**
 * Klassen representerar en bankkund.
 * Varje kund har ett unikt personnummer samt en lista över bankkonton som tillhör kunden.
 * 
 * 
 *  @author Klara Svensson
 */ 
public class Customer implements Serializable{
	private String fName; // Förnamn på kunden
	private String lName; // Efternamn på kunden
	private final String personNr; // final/konstant för att personnummer inte ska gå att ändra
	private List<Account> accounts; // En lista som innehåller kundens bankkonton
	
	/**
	 * Skapar en ny kund med förnamn, efternamn och personnummer.
	 * 
	 * @param customerFName - Kundens förnamn.
	 * @param customerLName - Kundens efternamn.
	 * @param personNr - Kundens personnummer.
	 */

	public Customer(String customerFName, String customerLName, String customerPersonNr ) {
		this.fName = customerFName;
		this.lName = customerLName;
		this.personNr = customerPersonNr;
		this.accounts = new ArrayList<>();
	}
	
	
    /**
     * Metoden "ChangeName" används för att ändra förnamn och/eller efternamn på kunden.
     * 
     * @param newFName - Det nya förnamnet.
     * @param newLName - Det nya efternamnet.
     */
	public void changeName(String newFName, String newLName) {
		this.fName = newFName;
		this.lName = newLName;
	}
	
	
    /**
     * Metoden "GetCustomerInformation" används för att hämta kundens information i form av 
     * personnummer, förnamn och efternamn.
     * 
     * @return En sträng innehållande kundinformationen.
     */
	public String getCustomerInformation() {
		return this.personNr + " " + this.fName + " " + this.lName;
	}
	
	
    /**
     * Metoden "getPersonNr" används för att hämta kundens personnummer.
     * 
     * @return Personnumret som en sträng.
     */
    public String getPersonNr() {
        return this.personNr;
    }

    
    /**
     * Metoden "addAccount" används för att lägga till ett bankkonto till kundens lista över konton.
     * 
     * @param account - Kontot som ska läggas till i kundens portfölj av bankkonton.
     */
	public void addAccount(Account account) {
		accounts.add(account);
	}
	
	
    /**
     * Metoden "removeAccount" används för att ta bort ett bankkonto från kundens lista över konton.
     * 
     * @param account - Kontot som ska tas bort från kundens portfölj av bankkonton.
     */
	public void removeAccount(Account account) {
		accounts.remove(account);
	}
	

    /**
     * Metoden "getAccounts" hämtar en lista över konton som tillgör kunden. 
     * 
     * @return En lista med kundens bankkonton.
     */
	public List<Account> getAccounts() {
        return List.copyOf(accounts);
    }
	
	
    /**
     * Metoden "getLName" används för att hämta kundens efternamn.
     * 
     * @return Efternamnet som en sträng.
     */
	public String getLName() {
		return this.lName;
	}
	
	
    /**
     * Metoden "getFName" används för att hämta kundens förnamn.
     * 
     * @return Förnamnet som en sträng.
     */
	public String getFName() {
		return this.fName;
	}
}
