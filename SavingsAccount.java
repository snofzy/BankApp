package bankApp;

import java.math.BigDecimal;

/**
 * Gör ett uttag från kontot. Om inga tidigare fria uttag har gjorts, debiteras ingen ränta.
 * Vid framtida uttag tillkommer ränta på det uttagna beloppet.
 * 
 * @param amount - Beloppet som ska tas ut.
 * @return true om uttaget lyckades, annars false.
 */

final public class SavingsAccount extends Account {
	static private BigDecimal savingsInterestRate = new BigDecimal("1.2"); // Innehåller aktuell ränta för sparkonto
	static private BigDecimal withdrawInterest = new BigDecimal("2"); // Innehåller aktuell ränta för uttag
	private Boolean freeWithdrawUsed;
	
	
	
	//KUNSTROKTUR
	public SavingsAccount() {
		super("Sparkonto");
		this.freeWithdrawUsed = false;
	}

	/**
	 * Gör ett uttag från kontot. Om inga tidigare fria uttag har gjorts, debiteras ingen ränta.
	 * Vid framtida uttag tillkommer ränta på det uttagna beloppet.
	 * 
	 * @param amount - Beloppet som ska tas ut.
	 * @return true om uttaget lyckades, annars false.
	 */
	@Override
	public boolean withdraw(BigDecimal amount) {
		if (!this.freeWithdrawUsed) {
			return withdrawWithoutInterest(amount);
		}
		else {
			return withdrawWithInterest(amount);
		}					
	}
	
	/**
     * Privat hjälpmetod "withdrawWithInterest" används för att göra ett uttag från kontot med ränta.
     * 
     * @param BigDecimal amount - Beloppet som ska tas ut från kontot.
     * @return true om uttaget lyckas (beloppet är större än noll och inte överstiger saldo), annars false.
     */
	private boolean withdrawWithInterest(BigDecimal amount) {
		// if statement som kontrollerar att önskat värde att ta ut är större än noll men mindre eller lika med än saldo på konto
		BigDecimal divideResult = withdrawInterest.divide(new BigDecimal("100"));
		BigDecimal subtractAmount = amount.add(divideResult.multiply(amount));
		if (subtractAmount.compareTo(BigDecimal.ZERO) > 0 && subtractAmount.compareTo(balance) <= 0) { 
            this.balance = balance.subtract(subtractAmount);
            String transaction = newTimeStamp() + " -" + formatCurrency(subtractAmount) + " Saldo: " + formatCurrency(balance);
			transactions.add(transaction);
            return true;
		}
		return false;
	}

	/**
     * Privat hjälpmetod "withdrawWithoutInterest" används för att göra ett uttag från kontot utan ränta.
     * 
     * @param BigDecimal amount - Beloppet som ska tas ut från kontot.
     * @return true om uttaget lyckas (beloppet är större än noll och inte överstiger saldo), annars false.
     */
	private boolean withdrawWithoutInterest(BigDecimal amount) {
		// if statement som kontrollerar att önskat värde att ta ut är större än noll men mindre eller lika med än saldo på konto
		if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(balance) <= 0) { 
            this.balance = balance.subtract(amount); //inbyggda .subtract används för att säkerställa korrekta värden i enlighet med BigDecimal
            this.freeWithdrawUsed = true;
            String transaction = newTimeStamp() + " -" + formatCurrency(amount) + " Saldo: " + formatCurrency(balance);
			transactions.add(transaction);
            return true;
		}
		return false;
	}

    /**
     * Metoden "calculateInterest" används för att beräkna räntan som förtjänas på saldot.
     * 
     * @return Räntan som en BigDecimal.
     */
	@Override
	public BigDecimal calculateInterest() {
		return this.balance.multiply(savingsInterestRate.divide(new BigDecimal(100)));
	}

	
    /**
     * Metoden "getInterestRate" används för att hämta den aktuella räntesatsen för alla konton.
     * 
     * @return Aktuell räntesatsen som en BigDecimal.
     */
	@Override
	public BigDecimal getInterestRate() {
		
		return savingsInterestRate;
		
	}
}
