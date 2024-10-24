package bankApp;

import java.math.BigDecimal;


final public class CreditAccount extends Account {
	static private BigDecimal deptInterest = new BigDecimal("7"); // Innehåller aktuell ränta för sparkonto
	static private BigDecimal interest = new BigDecimal("0.5"); // Innehåller aktuell ränta för uttag
	private BigDecimal creditLimit;
	
	
	/**
	 * Konstruktorn skapar ett kreditkonto med specifik räntesats.
	 * Konton har en fördefinierad kreditgräns.
	 */

	public CreditAccount() {
		super("Kreditkonto");
		creditLimit = new BigDecimal("5000");

	}

	/**
     * Metoden "withdraw" används för att göra ett uttag från kontot.
     * 
     * @param BigDecimal amount - Beloppet som ska tas ut från kontot.
     * @return true om uttaget lyckas (beloppet är större än noll och inte överstiger saldo), annars false.
     */
	@Override
	public boolean withdraw(BigDecimal amount) {
		if (this.balance.compareTo(BigDecimal.ZERO) >= 0) { // Om kontobalancen är positiv eller 0
            if(amount.compareTo(this.balance.add(creditLimit)) <= 0) { // Om uttagsbeloppet inte överskrider balance + kreditgräns
            	this.balance = this.balance.subtract(amount);
            	String transaction = newTimeStamp() + " -" + formatCurrency(amount) + " Saldo: " + formatCurrency(balance);
    			transactions.add(transaction);
            	return true;
            }
            else { // om beloppet är större än tillgångar + kreditgräns
            	return false;
            }

        } else { // om kontobalansen är negativ
            if ((balance.subtract(amount)).compareTo(creditLimit.negate()) >= 0) {// om det finns täckning för uttagsbeloppet
            	this.balance = this.balance.subtract(amount);
            	String transaction = newTimeStamp() + " -" + formatCurrency(amount) + " Saldo: " + formatCurrency(balance);
    			transactions.add(transaction);
            	return true;
            }
            else { //om det inte finns täckning för uttagsbeloppet
            	return false;
            }
        }
	}

    /**
     * Metoden "calculateInterest" används för att beräkna räntan som förtjänas på saldot.
     * 
     * @return Räntan som en BigDecimal.
     */
	@Override
	public BigDecimal calculateInterest() {
		// If balance is 0 or positive - calculateInterest() else calculateDeptInterest()
		if (this.balance.compareTo(new BigDecimal("0")) >= 0) {
			return this.balance.multiply(interest.divide(new BigDecimal(100)));
		} else {
			return this.balance.multiply(deptInterest.divide(new BigDecimal(100)));
		}

	}
	
	/**
     * Metoden "getInterestRate" används för att hämta den aktuella räntesatsen för alla konton.
     * 
     * @return Aktuell räntesatsen som en BigDecimal.
     */
	@Override
	public BigDecimal getInterestRate() {
		if(balance.compareTo(BigDecimal.ZERO) >= 0) {
			return interest;
		}
		else {
		return deptInterest;
		}
	}
}
