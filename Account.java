package bankApp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/*
/**
 * Klassen representerar ett bankkonto med grundläggande funktionalitet.
 * Den hanterar kontosaldo, transaktioner, samt ränta för specifika kontotyper.
 * Den är bas för olika specifika kontotyper såsom sparkonton och kreditkonton.
 * 
 *  @author Klara Svensson
 */ 

public abstract class Account implements Serializable{
	static private int nextAccountNr = 1001; // Används för att hålla reda på nästa tillgängliga kontonummer
	protected int accountNr; // Innehåller kontonummer
	protected String accountType; // Innehåller kontotyp
	protected BigDecimal balance; // innehåller saldo
	protected ArrayList<String> transactions;
	
	/**
	 * Konstruktorn skapar ett konto med ett unikt kontonummer och angiven kontotyp.
	 *
	 * @param accountType - Typ av kontot (t.ex. sparkonto, kreditkonto).
	 */

	public Account (String kontoTyp) {
		this.accountNr = nextAccountNr ++;
		this.accountType = kontoTyp; 
		this.balance = new BigDecimal("0");
		this.transactions = new ArrayList<>();
	}
	// Metod för att hämta nästa tillgängliga kontonummer
    public static int getNextAccountNr() {
        return nextAccountNr;
    }

    // Metod för att sätta nästa tillgängliga kontonummer
    public static void setNextAccountNr(int nextAccountNr) {
        Account.nextAccountNr = nextAccountNr;
    }
    
	public abstract boolean withdraw(BigDecimal amount);
    	
	public boolean deposit(BigDecimal amount) {
		
		if (amount.compareTo(BigDecimal.ZERO) > 0) {
				
			this.balance = balance.add(amount);
			String transaction = newTimeStamp() + " " + formatCurrency(amount) + " Saldo: " + formatCurrency(balance);
			transactions.add(transaction);
			return true;
		}
		else {
			return false;
		}
	}
		
	protected String newTimeStamp() {
		Date date = new Date();  // Create a new Date object
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = sdf.format(date);
		return strDate;
	}

    /**
     * Metoden "getAccountNr" används för att hämta kontonumret för detta konto.
     * 
     * @return Kontonumret som en heltalsvärde.
     */
	public int getAccountNr() {
		return this.accountNr;
	}
	
	/**
     * Metoden "calculateInterest" är en abstrakt metod som beräknar ränta
     * 
     */
	public abstract BigDecimal calculateInterest();

    /**
     * Metoden "getBalance" används för att hämta saldot på kontot.
     * 
     * @return Saldot som en BigDecimal.
     */
	public BigDecimal getBalance() {
		return this.balance;
	}
	
    /**
     * Metoden "getAccountType" används för att hämta kontotypen för detta konto.
     * 
     * @return Kontotypen som en sträng.
     */
	public String getAccountType() {
		return this.accountType;
	}
	
	/**
     * Metoden "getInterestRate" är en abstrakt metod som hämtar räntenivån
     * 
     */
	public abstract BigDecimal getInterestRate();
	
	/**
     * Metoden "getAccountTransactions" är en abstrakt metod som hämtar transactioner till 
     * kontonummret
     * 
     * @param accountId - Kontonumret på kontot vars transaktioner ska hämtas
     * 
     * @return copyTransactions - returnerar en kopia av transaktionslistan
     * 
     */
	public ArrayList<String> getAccountTransactions(int accountId){
		ArrayList<String> copyTransactions = new ArrayList<>(this.transactions);
		return copyTransactions;
	}
	
	/**
   * En protected metod för att formatera kontoinformation utefter instruktioner
   *
   * @param balance - saldot som ska formateras
   *          
   * @return en string med formaterad saldo
   */	
	protected String formatCurrency(BigDecimal balance) {
		String balanceStr = NumberFormat.getCurrencyInstance(new Locale("sv","SE")).format(balance);
		return balanceStr;
	}

}
