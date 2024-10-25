package bankApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.logging.Logger;

/**
 * Grafiskt användargränssnitt för bankapplikationen.
 * Tillåter användare att interagera med bankens funktioner genom knappar och fönster.
 * Använder sig av BankLogic-klassen för att hantera affärslogik.
 * 
 *  @author Klara Svensson
 */
public class BankGUI extends JFrame implements Serializable{
	BankLogic bankLogic = new BankLogic();
	ActionListener listener = new ClickListener();
	private static final int FRAME_WIDTH = 650;
	private static final int FRAME_HEIGHT = 530;
	private static final int TEXT_WIDTH = 20;
	private static final int ROWS = 3;
	private static final int COLS = 1;
	private String inputPNo;
	private String[] customerArray;
	private List<String> thisCustomer;
	URL imageURL = getClass().getResource("/bankApp_files/BankenLogo.png");
	ImageIcon logoImage = new ImageIcon(imageURL);
	URL logoURL = getClass().getResource("/bankApp_files/BankenLogoSimple.png");
	ImageIcon logoImageSimpleOriginalSize = new ImageIcon(logoURL);
	ImageIcon logoImageSimple = new ImageIcon(logoImageSimpleOriginalSize.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	JLabel logoShow = new JLabel(logoImage);
	JLabel logoSimpleShow = new JLabel(logoImageSimple);
	JTextField amountTextField;

	JButton logInButton = new JButton("Logga in");
	JButton createUserButton = new JButton("Skapa användare");
	
	JComboBox comboBox;
	JComboBox endAccountComboBox;
	// Variabler för skapa kund
	JButton newCustomerButton;
	JTextField nameTextField;
	JTextField lastnameTextField;
	JTextField PNumberTextfield;
	JTextField password;

	//menyalternativ
	JMenu mainMenu;
	JMenu transactionsMenu;
	JMenuItem home;
	JMenuItem showAccounts;
	JMenuItem withdraw;
	JMenuItem deposit;
	JMenuItem showMyProfile;
	JMenuItem logOff;
	
	// Används i flera metoder
	JTextField pNoField;
	
	//Frames
	JFrame mainFrame;
	JFrame createUserFrame;
	JFrame homePageFrame;
	JFrame showAccountsFrame;
	JFrame createNewAccountFrame;
	JFrame withdrawFrame;
	JFrame depositFrame;
	JFrame transactionsFrame;
	JFrame showMyProfileFrame;
	JFrame updateUserFrame;
	JFrame endAccountFrame;
	JFrame adminLogOnFrame;
	JFrame adminFrame;
	JFrame allCustomerFrame;
	 
	
	//Logger
	private static final Logger logger = LoggerUtil.getLogger();

	/**
     * Konstruerar ett BankGUI-objekt, ställer in initial storlek och skapar startsidan.
     */
	public BankGUI() {
		mainFrame = this;
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		createStartPage();
	}
	/**
     * Huvudmetoden för att starta applikationen.
     * Den skapar en instans av BankGUI-klassen och ställer in den initiala ramen.
     * 
     */
	public static void main(String[] args) {
		startApplication();
	}
	
	/**
     * Ställer in den initiala ramen och startar applikationen.
     */
	private static void startApplication() {
		JFrame frame = new BankGUI();
		frame.setTitle("Banken");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setVisible(true);
	}
	
	/**
     * Skapar startsidan med inloggnings- och skapa användaralternativ.
     */
	private void createStartPage() {
		ActionListener listener = new ClickListener();
		pNoField = new JTextField(TEXT_WIDTH);
		JPanel panelTop = new JPanel();
		JPanel panelMiddle = new JPanel();
		JPanel panelBottom = new JPanel();
		JPanel extraPanel = new JPanel();
		JButton adminLogonButton = new JButton("Admin log-in");
		JTextArea startPageInfo = new JTextArea(ROWS, COLS);
		startPageInfo.setEditable(false);	
		
		JLabel pNoLabel = new JLabel("Personnummer(YYMMDDNNNN): ");
		panelTop.add(logoShow);
		panelMiddle.add(startPageInfo);
		panelMiddle.add(pNoLabel);
		panelMiddle.add(pNoField);
		panelBottom.add(logInButton);
		panelBottom.add(createUserButton);
		extraPanel.add(adminLogonButton);
		startPageInfo.setText("Välkommen till Banken, vänligen logga in med ditt personnummer nedan. \nOm du inte är kund hos oss kan du skapa en användare genom att trycka på \"Skapa användare\".");
		add(panelTop);
		add(panelMiddle);
		add(panelBottom);
		add(new JPanel());
		add(extraPanel);
		//Lägg till Action Listeners
		logInButton.addActionListener(listener);
		createUserButton.addActionListener(listener);
		adminLogonButton.addActionListener(listener);
	}
	
	/**
     * Visar startsidan.
     */
	private void showStartPage() {
		mainFrame.setVisible(true);
	}
	
	/**
     * Skapar den personliga startsidan för en inloggad kund.
     * @param currentCustomer Information om den aktuella kunden.
     */
	private void createPersonalHomePage(List<String> currentCustomer) {
        thisCustomer = currentCustomer;
        homePageFrame = new JFrame();
        homePageFrame.setLayout(new GridBagLayout());
        homePageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add menu
        JMenuBar menuBar = new JMenuBar();
        menuBar = addmenu(menuBar);
        homePageFrame.setJMenuBar(menuBar);

        // Information about customer
        customerArray = splitWithAccount(thisCustomer);
        String[] customerName = splitNameString(customerArray[0]);
        JTextField customer = new JTextField("Hej " + customerName[1] + " " + customerName[2] + "!\nVälkommen till din startsida.");
        inputPNo = customerName[0];
        customer.setEditable(false);

        // Logo
        JLabel logoLabel = new JLabel(logoImageSimple);

        // Account information
        JLabel accountLabel = new JLabel("Nedan är dina tillgängliga konton");

        // Panel for account data
        JPanel accountDataPanel = new JPanel();
        accountDataPanel.setLayout(new GridLayout(customerArray.length, 4, 10, 5)); 

        // Add headings to the panel
        addHeadingToPanel(accountDataPanel, "Kontonummer");
        addHeadingToPanel(accountDataPanel, "Saldo");
        addHeadingToPanel(accountDataPanel, "Kontotyp");
        addHeadingToPanel(accountDataPanel, "Ränta");

        // Add account data to the panel
        for (int i = 1; i < customerArray.length; i++) {
            String[] accountData = customerArray[i].split("\\s+"); // Dela vid whitespace
            for (String data : accountData) {
                JLabel label = new JLabel(data);
                label.setPreferredSize(new Dimension(90, 20)); // Size för labels
                accountDataPanel.add(label);
            }
        }

        // Panels
        JPanel logoPanel = new JPanel();
        JPanel infoPanel = new JPanel();
        JPanel accountPanel = new JPanel();

        // Set layout for each panel
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        accountPanel.setLayout(new BorderLayout());

        // Add components to panels
        infoPanel.add(customer);
        logoPanel.add(logoLabel);
        accountPanel.add(accountLabel, BorderLayout.NORTH);
        accountPanel.add(accountDataPanel, BorderLayout.CENTER);

        // Set constraints for GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        homePageFrame.add(logoPanel, gbc);

        gbc.gridy = 1;
        homePageFrame.add(infoPanel, gbc);

        gbc.gridy = 2;
        homePageFrame.add(accountPanel, gbc);

        homePageFrame.setSize(550, 340); // Ökad bredd pga headings and preferred sizes
        homePageFrame.setVisible(true);
    }

	/**
     * Lägger till en rubrik i den givna panelen med ett angivet etikett.
     * @param panel Panelen till vilken rubriken läggs till.
     * @param heading Texten för rubriken.
     */
    private void addHeadingToPanel(JPanel panel, String heading) {
        JLabel label = new JLabel(heading);
        label.setPreferredSize(new Dimension(80, 25)); // Set preferred size for each heading
        panel.add(label);
    }
    
    /**
     * Delar upp namnsträngen i en array av namndelar.
     * @param nameString Hela namnsträngen som ska delas upp.
     * @return En array som innehåller förnamn, mellannamn och efternamn.
     */
	private String[] splitNameString(String nameString) {
		String[] nameParts = nameString.split(" ");
		return new String[] { nameParts[0], nameParts[1], nameParts[2] };
	}
	
	/**
     * Delar upp strängen med kundinformation i en array av konto-relaterad information.
     * @param currentCustomer Kundinformationen som ska delas upp.
     * @return En array som innehåller kund- och kontouppgifter.
     */
	private String[] splitWithAccount(List<String> currentCustomer) {
		List<String> result = new ArrayList<>();

        for (String element : currentCustomer) {
            String[] parts = element.split(", ");
            for (String part : parts) {
                result.add(part);
            }
        }
        return result.toArray(new String[0]);
	}
	
	/**
     * Lägger till en menyrad med menyfunktioner för olika bankoperationer.
     * @param menuBar Menyraden till vilken menyer och funktioner läggs till.
     * @return Den uppdaterade menyraden.
     */
	private JMenuBar addmenu(JMenuBar menuBar) {
		mainMenu = new JMenu("Huvudmeny");
		transactionsMenu = new JMenu("Transaktioner");
		//menyalternativ
		home = new JMenuItem ("Hem");
		showAccounts = new JMenuItem("Visa alla konton");
		withdraw = new JMenuItem("Uttag");
		deposit = new JMenuItem("Insättning");
		showMyProfile = new JMenuItem("Visa min profil");
		logOff = new JMenuItem("Logga ut");
		
		home.addActionListener(listener);
		showAccounts.addActionListener(listener);
		withdraw.addActionListener(listener);
		deposit.addActionListener(listener);
		showMyProfile.addActionListener(listener);
		logOff.addActionListener(listener);
		
		menuBar.add(mainMenu);
		
		// Lägg till menyalternav
		mainMenu.add(home);
		mainMenu.add(showAccounts);
		mainMenu.add(transactionsMenu);
		transactionsMenu.add(withdraw);
		transactionsMenu.add(deposit);
		mainMenu.add(showMyProfile);
		mainMenu.add(logOff);
		return menuBar;
	}
	
	/**
     * ActionListener-implementering för att hantera knapptryckningar och andra åtgärder.
     */
	public class ClickListener implements ActionListener {
		/**
         * Hanterar olika åtgärder baserat på händelsekällan.
         * @param e ActionEvent som representerar användarens åtgärd.
         */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == logInButton) {
				logOn();
			}
			else if(e.getSource() == createUserButton) {
				createUserInputFrame();
			}
			else if(e.getSource() == newCustomerButton) {
				createNewUser();
			}
			else if(e.getSource() == showAccounts) {
				createShowAccoutsFrame();
			}
			else if(e.getSource() == home) {
				disposeOpenFrames();
				createPersonalHomePage(bankLogic.getCustomer(inputPNo));
				//try(createPersonalHomePage(bankLogic.getCustomer(inputPNo))){
					
				//}
				//catch(NullPointerException){
					
				//}
			}
			else if(e.getSource()== withdraw) {
				createWithdrawFrame();
			}
			else if(e.getSource()== deposit) {
				createDepositFrame();
			}
			else if(e.getSource() == comboBox) {
				showAccountTransactions();
			}
			else if(e.getSource()== showMyProfile) {
				
				createMyProfileFrame();
			}
			else if(e.getSource()== logOff) {
				disposeOpenFrames();
				showStartPage();
			}
			else if (e.getSource() instanceof JButton) {
                JButton button = (JButton) e.getSource();

                // Kontrollera om knappens text är "Öppna konto"
                if ("Öppna konto".equals(button.getText())) {
                    createNewAccountFrame();
                }
                else if ("Skapa sparkonto".equals(button.getText())) {
                	createNewSavingsAccount();
                }
                else if("Skapa kreditkonto".equals(button.getText())) {
                	createNewCreditAccount();
                }
                else if("Genomför uttag".equals(button.getText())) {
                	doWithdraw();
                }
                else if("Genomför insättning".equals(button.getText())) {
                	doDeposit();
                }
                else if("Visa transaktioner".equals(button.getText())) {
                	showTransactions();
                }
                else if("Uppdatera namn".equals(button.getText())) {
                	createUpdateName();
                }
                else if("Avsluta användare".equals(button.getText())) {
                	endUser();
                }
                else if("Uppdatera användare".equals(button.getText())) {
                	updateUser();
                }
                else if("Avsluta konto".equals(button.getText())) {
                	createEndAccountFrame();
                }
                else if("Avsluta".equals(button.getText())) {
                	endAccount();
                }
                else if("Admin log-in".equals(button.getText())) {
                	createAdminLogOn();
                }
                else if("Logga in som admin".equals(button.getText())) {
                	if (password.getText().equals("99")) {
                		disposeOpenFrames();
                    	adminLogOnFrame.dispose();
            			createAdminFrame();
            		}
                	else{
                		JOptionPane.showMessageDialog(null, "Fel lösenord", "Error", JOptionPane.INFORMATION_MESSAGE);
                	}
                }
                else if("Visa alla kunder i banken".equals(button.getText())) {
                	
                	createShowAllCustomersFrame();
                }
                else if("Stäng".equals(button.getText())) {
                	allCustomerFrame.dispose();
                }
                else if("Spara transaktioner".equals(button.getText())) {
                	saveAllTransactions();
                }
                else if("Spara Banken".equals(button.getText())) {
                	try (ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream("src\\bankApp_files\\bankfil.dat"))) {
                        // Spara nextAccountNr separat
                        outFile.writeInt(Account.getNextAccountNr());

                        // Spara övriga BankLogic-objekt
                        outFile.writeObject(bankLogic);

                        System.out.println("BankLogic instance saved to file.");
                    } catch (IOException ex) {
                        System.out.println("Error saving BankLogic instance to file: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                else if ("Ladda in Bank".equals(button.getText())) {
                	try (ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("src\\\\bankApp_files\\\\bankfil.dat"))) {
                	    // Ladda nextAccountNr separat
                	    int nextAccountNr = inFile.readInt();
                	    Account.setNextAccountNr(nextAccountNr);
                	    
                	    // Ladda övriga BankLogic-objekt
                	    bankLogic = (BankLogic) inFile.readObject();

                	    System.out.println("BankLogic instance loaded from file.");
                	} catch (IOException | ClassNotFoundException ex) {
                	    System.out.println("Error loading BankLogic instance from file: " + ex.getMessage());
                	    ex.printStackTrace();
                	}
                }

            }
		}
	}
	
	
	private void saveAllTransactions() {  
		
	    ArrayList<String> transactions;
	    
	    String account = (String) comboBox.getSelectedItem();
	    String accountNumber = account.substring(0, 4);
	    String fileName = accountNumber + "_transactions";
	    // Hitta index efter kontonr
	    int spaceIndex = account.indexOf(' ', 5); //Antar att det är "space" efter kontonr

	    // Ta fram saldo
	    String balanceString = account.substring(4, spaceIndex);
	    transactions = bankLogic.getTransactions(inputPNo, Integer.parseInt(accountNumber));

	    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
	        // Lägg till dagens datum i filen
	        LocalDate currentDate = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        out.println("Date: " + currentDate.format(formatter) + " Kontonummer: " + accountNumber + "Saldo: " + balanceString);

	        for (String transaction : transactions) {
	            out.println(transaction); 
	        }
	        JOptionPane.showMessageDialog(null, "Transaktionerna är sparade med filnamn: " + fileName, "Success", JOptionPane.INFORMATION_MESSAGE);
	    } 
	    catch (IOException e) {
	        System.out.println("Det gick inte att skriva till filen");
	        JOptionPane.showMessageDialog(null, "Det gick inte att skriva till filen", "IOException", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
	/**
	 * Skapar och visar admininloggningsfönstret.
	 */
	private void createAdminLogOn() {
		adminLogOnFrame = new JFrame();
		adminLogOnFrame.setLayout(new BoxLayout(adminLogOnFrame.getContentPane(), BoxLayout.Y_AXIS));
		adminLogOnFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Knappar + actionListeners
		JLabel passwordLabel = new JLabel("Ange adminlösenord: ");
		password = new JTextField();
		password.setPreferredSize(new Dimension(100, password.getPreferredSize().height));
		
		
		JButton logOn = new JButton("Logga in som admin");
		logOn.addActionListener(listener);
		
		JPanel panel = new JPanel();
		JPanel buttonPanel = new JPanel();
		panel.add(passwordLabel);
		panel.add(password);
		buttonPanel.add(logOn);
		
		
		
		adminLogOnFrame.add(panel);
		adminLogOnFrame.add(buttonPanel);
		adminLogOnFrame.setSize(350, 150);
		adminLogOnFrame.setVisible(true);
	}
	/**
	 * Skapar och visar fönstret med information om alla kunder.
	 */
	private void createShowAllCustomersFrame() {
		List<String> allCustomerInfo = new ArrayList<>();
		allCustomerInfo = bankLogic.getAllCustomers();
		System.out.println(allCustomerInfo);
		allCustomerFrame = new JFrame();
		
		// Skapa komponenter
        JLabel titleLabel = new JLabel("Kundinformation");
        JTextArea customerInfoTextArea = new JTextArea(10, 30);
        customerInfoTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(customerInfoTextArea);
        JButton closeButton = new JButton("Stäng");
        closeButton.addActionListener(listener);
        JPanel panel = new JPanel();
        
        // Ställ in layout manager
        panel.setLayout(new BorderLayout());
        
        // Lägg till komponenter till fönstret
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        // Ställ in text för JTextArea
        StringBuilder infoText = new StringBuilder();
        for (String info : allCustomerInfo) {
            infoText.append(info).append("\n");
        }
        customerInfoTextArea.setText(infoText.toString());

        //Lägg till panel
        allCustomerFrame.add(panel);
        
        //lägg till padding
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Ställ in fönsteregenskaper
        allCustomerFrame.setTitle("Bankkundinformation");
        allCustomerFrame.setSize(400, 300);
        allCustomerFrame.setLocationRelativeTo(null);
        allCustomerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        allCustomerFrame.setVisible(true);
				
	}
	/**
	 * Skapar och visar adminfönstret med olika funktioner.
	 */
	private void createAdminFrame() {
		adminFrame = new JFrame();
		adminFrame.setLayout(new BoxLayout(adminFrame.getContentPane(), BoxLayout.Y_AXIS));
		adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Add menu
		JMenuBar menuBar = new JMenuBar();
		mainMenu = new JMenu("Huvudmeny");
		logOff = new JMenuItem("Logga ut");
		logOff.addActionListener(listener);
		menuBar.add(mainMenu);
		mainMenu.add(logOff);
		adminFrame.setJMenuBar(menuBar);
		
		JLabel welcomeMessage = new JLabel("Admin startsida" );

		//Logo
		JLabel logoLabel = new JLabel(logoImageSimple);
		
		
		JButton saveBankButton = new JButton("Spara Banken");
		JButton loadBankButton = new JButton("Ladda in Bank");
		JButton showAllCustomers = new JButton("Visa alla kunder i banken");
		showAllCustomers.addActionListener(listener);
		saveBankButton.addActionListener(listener);
		loadBankButton.addActionListener(listener);
		//Panels
		JPanel logoPanel = new JPanel();
		JPanel infoPanel = new JPanel();
		
		logoPanel.add(logoLabel);
		logoPanel.add(welcomeMessage);
		infoPanel.add(saveBankButton);
		infoPanel.add(loadBankButton);
		infoPanel.add(showAllCustomers);
		
		
		adminFrame.add(logoPanel);
		adminFrame.add(infoPanel);
		
		adminFrame.setSize(450, 340);
		adminFrame.setVisible(true);
		
	}
	
	/**
	 * Avslutar ett konto och visar resultaten för användaren.
	 */
	private void endAccount() {
		String selectedOption = (String)endAccountComboBox.getSelectedItem();
		String firstFourChars = selectedOption.substring(0, 4);
		int kontoNr = Integer.parseInt(firstFourChars);
		
		String endedAccount = bankLogic.closeAccount(inputPNo, kontoNr);
		
		
		if (endedAccount == null) {
			JOptionPane.showMessageDialog(null, "Konto kunde inte tas bort", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			String[] splitAccount = endedAccount.split(" ");
			JOptionPane.showMessageDialog(null, "Ditt " + splitAccount[2] + " med kontonummer " + splitAccount[0] + " avslutades. Slutränta: " + splitAccount[3] + " Slutsaldo: " + splitAccount[1], "Success", JOptionPane.INFORMATION_MESSAGE);
			
		}
		
		
		endAccountFrame.dispose();
		showAccountsFrame.dispose();
		createShowAccoutsFrame();
	}
	
	
	/**
	 * Skapar fönstret för att avsluta ett konto.
	 */
	private void createEndAccountFrame() {
		endAccountFrame = new JFrame();
		endAccountFrame.setLayout(new BoxLayout(endAccountFrame.getContentPane(), BoxLayout.Y_AXIS));
		endAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		customerArray = splitWithAccount(bankLogic.getCustomer(inputPNo));
		String[] options = null;
		//Hämta konton tillgängliga
		if (customerArray.length >= 2) {
			int i = customerArray.length; // Gå igenom alla konton
			int index = 1; // starta på index 1 för att hoppa över namnet
			ArrayList<String> tempArrayList = new ArrayList<String>();
			while (i > index) {
				String temp = customerArray[index];
				tempArrayList.add(temp);
				index++;
			}
			options = Arrays.copyOf(tempArrayList.toArray(), tempArrayList.size(), String[].class);
		}
		
		//Skapa panel innehåll
		endAccountComboBox = new JComboBox(options);
	    JLabel labelTransaction = new JLabel("Avsluta konto");
	    JLabel labelChooseAccount = new JLabel("Välj konto du önskar avsluta:");
	    JButton endAccountButton = new JButton("Avsluta");
	    endAccountButton.addActionListener(listener);
	    // Skapa topppanel
	    JPanel headingPanel = new JPanel();
	    headingPanel.add(logoSimpleShow);
	    headingPanel.add(labelTransaction);
	    
	    //Skapa panel med info
	    JPanel endAccountPanel = new JPanel();
	    endAccountPanel.add(labelTransaction);
	    endAccountPanel.add(labelChooseAccount);
	    endAccountPanel.add(endAccountComboBox);
	    endAccountPanel.add(endAccountButton);
	    endAccountFrame.add(headingPanel);
	    endAccountFrame.add(endAccountPanel);
	    endAccountFrame.setSize(250, 300);
		//Padding
	    endAccountPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	    endAccountFrame.setVisible(true);
		
	}
	
	/**
	 * Uppdaterar användarinformation och visar resultatet för användaren.
	 */
	private void updateUser() {
		Boolean returnIs = false;
		String nameString = nameTextField.getText();
		String lastnameString = lastnameTextField.getText();
		System.out.println(nameString);
		System.out.println(lastnameString);
		returnIs = bankLogic.changeCustomerName(nameString, lastnameString, inputPNo);
		if (returnIs) {
			JOptionPane.showMessageDialog(null, "Namnet är uppdaterat", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (!returnIs) {
			JOptionPane.showMessageDialog(null, "Uppdatering av namn misslyckades", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		updateUserFrame.dispose();
		showMyProfileFrame.dispose();
		createMyProfileFrame();
	}
	
	/**
	 * Avslutar en användare och visar resultatet för användaren.
	 */
	private void endUser() {
		List<String> endedUser = new ArrayList<>();
		endedUser = bankLogic.deleteCustomer(inputPNo);
		if (endedUser == null) {
			JOptionPane.showMessageDialog(null, "Kunden kunde inte tas bort", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
						
			 StringBuilder message = new StringBuilder("Följande kund togs bort:\n");
	        message.append("- ").append(endedUser.get(0)).append("\n\n");
	        message.append("Tillhörande konton:\n");
	        for (int i = 1; i < endedUser.size(); i++) {
	            message.append("- ").append(endedUser.get(i)).append("\n");
	        }
	        JOptionPane.showMessageDialog(null, message.toString(), "Sucess", JOptionPane.INFORMATION_MESSAGE);
		}
		
		disposeOpenFrames();
		
		showStartPage();
		
	}
	
	/**
	 * Avslutar öppna fönster för att undvika minnesläckor.
	 */
	private void disposeOpenFrames() {
		try {
			if (showMyProfileFrame != null) {
		        showMyProfileFrame.dispose();
		    }
		    if (mainFrame != null) {
		        mainFrame.dispose();
		    }
		    if (showAccountsFrame != null) {
		        showAccountsFrame.dispose();
		    }
		    if (updateUserFrame != null) {
		    	updateUserFrame.dispose();
		    }
		    if (transactionsFrame != null) {
		    	transactionsFrame.dispose();
		    }
		    if (withdrawFrame != null) {
		    	withdrawFrame.dispose();
		    }
		    if (depositFrame != null) {
		    	depositFrame.dispose();
		    }
		    if (createNewAccountFrame != null) {
		    	createNewAccountFrame.dispose();
		    }
		    if (showAccountsFrame != null) {
		    	showAccountsFrame.dispose();
		    }
		    if (createUserFrame != null) {
		    	createUserFrame.dispose();
		    }
		    if (endAccountFrame != null) {
		    	endAccountFrame.dispose();
		    }
		    if (homePageFrame != null) {
		    	homePageFrame.dispose();
		    }
		    if (homePageFrame != null) {
		    	adminFrame.dispose();
		    }
		} catch (NullPointerException e) {
		    // in case the frames are not yet open
		}
		
	}
	

/**
 * Skapar fönstret för att uppdatera användarnamn.
 */
	private void createUpdateName() {
		updateUserFrame = new JFrame();
		updateUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Namn textfield
		nameTextField = new JTextField(TEXT_WIDTH);
		nameTextField.setBorder(BorderFactory.createTitledBorder("Nytt namn: (lämna blankt om du vill behålla förnamn)"));
		nameTextField.setPreferredSize(new Dimension(100, nameTextField.getPreferredSize().height));
		
		//Efternamn textfield
		lastnameTextField = new JTextField(TEXT_WIDTH);
		lastnameTextField.setBorder(BorderFactory.createTitledBorder("Nytt efternamn: (lämna blankt om du vill behålla efternamn)"));
		lastnameTextField.setPreferredSize(new Dimension(100, lastnameTextField.getPreferredSize().height));
		
		JButton updateCustomerButton = new JButton("Uppdatera användare");
		updateCustomerButton.addActionListener(listener);
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.add(nameTextField);
		panel.add(lastnameTextField);
		panel.add(updateCustomerButton);
		updateUserFrame.add(panel);
		updateUserFrame.setSize(450, 250);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		try {
		    mainFrame.dispose();
		    showAccountsFrame.dispose();
		    homePageFrame.dispose();
		    transactionsFrame.dispose();
		} catch (NullPointerException e) {
		    // in case the frames are not yet open
		}
		updateUserFrame.setVisible(true);
		
	}
	
	/**
	 * Skapar användarprofilfönstret och visar användarinformation.
	 */
	private void createMyProfileFrame() {
		showMyProfileFrame = new JFrame();
		showMyProfileFrame.setLayout(new BoxLayout(showMyProfileFrame.getContentPane(), BoxLayout.Y_AXIS));
		showMyProfileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Add menu
		JMenuBar menuBar = new JMenuBar();
		menuBar = addmenu(menuBar);
		showMyProfileFrame.setJMenuBar(menuBar);
		
		//Knappar + actionListeners
		JTextField customerFirstnameInfo;
		JTextField customerLastnameInfo;
		JLabel customerInfo = new JLabel("KundInformation: ");
		JButton updateNameButton = new JButton("Uppdatera namn");
		updateNameButton.addActionListener(listener);
		JButton endAccount = new JButton("Avsluta användare");
		endAccount.addActionListener(listener);
		
		customerArray = splitWithAccount(bankLogic.getCustomer(inputPNo));
		
		for (String customer : customerArray) {
		    System.out.println(customer);
		}
		
		String[] customerName = splitNameString(customerArray[0]);
		customerFirstnameInfo = new JTextField(customerName[1]);
		customerFirstnameInfo.setEditable(false);
		customerFirstnameInfo.setBorder(BorderFactory.createTitledBorder("Förnamn: "));
		customerFirstnameInfo.setPreferredSize(new Dimension(100, nameTextField.getPreferredSize().height));
		customerLastnameInfo = new JTextField(customerName[2]);
		customerLastnameInfo.setEditable(false);
		customerLastnameInfo.setBorder(BorderFactory.createTitledBorder("Efternamn: "));
		customerLastnameInfo.setPreferredSize(new Dimension(100, nameTextField.getPreferredSize().height));
		
		JPanel panel = new JPanel(new GridLayout(4, 1));
		panel.add(customerInfo);
		panel.add(customerFirstnameInfo);
		panel.add(customerLastnameInfo);
		
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.add(updateNameButton);
		buttonPanel.add(endAccount);
		
		
		showMyProfileFrame.add(panel);
		showMyProfileFrame.add(buttonPanel);
		
		showMyProfileFrame.setSize(450, 340);
		
		disposeOpenFrames();
		
		showMyProfileFrame.setVisible(true);
	}
	
	/**
	 * Visar kontotransaktioner för det valda kontot.
	 */
	private void showAccountTransactions() {
		String selectedOption = (String)comboBox.getSelectedItem();
		String firstFourChars = selectedOption.substring(0, 4);
		
		int kontoNr = Integer.parseInt(firstFourChars);
		
		JLabel transactionsLabel = new JLabel("Transaktioner för konto " + kontoNr);
		
		ArrayList<String> transactions = bankLogic.getTransactions(inputPNo, kontoNr);

	    if (transactions.isEmpty()) {
	        JLabel transactionMissing = new JLabel("No transactions have been made on this account");
	        transactionsFrame.add(transactionMissing);
	    } else {
	        transactionsFrame.add(transactionsLabel);
	        JList<String> transactionList = new JList<>(transactions.toArray(new String[0]));
	        JScrollPane transactionsScroll = new JScrollPane(transactionList);
	        transactionsFrame.add(transactionsScroll);
	    }
	    
	    JPanel buttonPanel = new JPanel();
	    JButton saveTransactions = new JButton("Spara transaktioner");
	    saveTransactions.addActionListener(listener);
	    buttonPanel.add(saveTransactions);
	    transactionsFrame.add(buttonPanel);
	    transactionsFrame.revalidate();
	    transactionsFrame.repaint();
	    transactionsFrame.setVisible(true);
	}
	
	/**
	 * Visar alla transaktioner för de olika kontona.
	 */
	private void showTransactions() {
		transactionsFrame = new JFrame();
		transactionsFrame.setLayout(new BoxLayout(transactionsFrame.getContentPane(), BoxLayout.Y_AXIS));
		transactionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Add menu
		JMenuBar menuBar = new JMenuBar();
		menuBar = addmenu(menuBar);
		transactionsFrame.setJMenuBar(menuBar);
		
		customerArray = splitWithAccount(bankLogic.getCustomer(inputPNo));
		String[] options = null;
		//Hämta konton tillgängliga
		if (customerArray.length >= 2) {
			int i = customerArray.length; // Gå igenom alla konton
			int index = 1; // starta på index 1 för att hoppa över namnet
			ArrayList<String> tempArrayList = new ArrayList<String>();
			while (i > index) {
				String temp = customerArray[index];
				tempArrayList.add(temp);
				index++;
			}
			options = Arrays.copyOf(tempArrayList.toArray(), tempArrayList.size(), String[].class);
		}
		
		//Skapa panel innehåll
	    comboBox = new JComboBox(options);
	    JLabel labelTransaction = new JLabel("Transaktioner");
	    JLabel labelChooseAccount = new JLabel("Välj konto du önskar visa transaktioner från:");
		comboBox.addActionListener(listener);
	    
	    // Skapa topppanel
	    JPanel headingPanel = new JPanel();
	    headingPanel.add(logoSimpleShow);
	    headingPanel.add(labelTransaction);
	    
	    //Skapa panel med info
	    JPanel transactionPanel = new JPanel();
	    transactionPanel.add(labelTransaction);
	    transactionPanel.add(labelChooseAccount);
	    transactionPanel.add(comboBox);
	    
	    
	    transactionsFrame.add(headingPanel);
	    transactionsFrame.add(transactionPanel);
	    transactionsFrame.setSize(550, 350);
		//Padding
	    transactionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	    transactionsFrame.setVisible(true);
		 
	}
	
	/**
	 * Genomför ett uttag och visar resultatet för användaren.
	 */
	private void doWithdraw() {
	    boolean returnIs = false;
	    
	    
	    // Assuming inputPNo is declared and initialized somewhere in your code
	    String selectedOption = (String) comboBox.getSelectedItem();
	    String firstFourChars = selectedOption.substring(0, 4);
	    int kontoNr = Integer.parseInt(firstFourChars);

	    try {
	        // Use Integer.parseInt to convert the amountTextField text to an integer
	    	int amount = Integer.parseInt(amountTextField.getText());

	        // Assuming bankLogic.withdraw returns a boolean indicating the success of the withdrawal
	        returnIs = bankLogic.withdraw(inputPNo, kontoNr, amount);

	        if (!returnIs) {
	            JOptionPane.showMessageDialog(null, "Kunde inte genomföra uttag", "Error", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(null, "Uttag genomförd", "Success", JOptionPane.INFORMATION_MESSAGE);
	            disposeOpenFrames();
	            createPersonalHomePage(bankLogic.getCustomer(inputPNo));
	        }
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(null, "Felaktigt belopp", "Error", JOptionPane.INFORMATION_MESSAGE);
	    }
	}

	
	/**
	 * Genomför en insättning och visar resultatet för användaren.
	 */
	private void doDeposit() {
		boolean returnIs = false;	
		String selectedOption = (String) comboBox.getSelectedItem();
		String firstFourChars = selectedOption.substring(0, 4);
		int kontoNr = Integer.parseInt(firstFourChars);
		
		try {
			int amount = Integer.parseInt(amountTextField.getText());
			returnIs = bankLogic.deposit(inputPNo, kontoNr, amount);
			if (!returnIs) {
				JOptionPane.showMessageDialog(null, "Kunde inte genomföra insättning", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(null, "Insättning är genomförd", "Success", JOptionPane.INFORMATION_MESSAGE);
				disposeOpenFrames();
				createPersonalHomePage(bankLogic.getCustomer(inputPNo));
			}
		}
		catch (NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Felaktigt belopp", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * Skapar fönstret för att genomföra ett uttag.
	 */
	private void createWithdrawFrame() {
		withdrawFrame = new JFrame();
		withdrawFrame.setLayout(new BoxLayout(withdrawFrame.getContentPane(), BoxLayout.Y_AXIS));
		withdrawFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		customerArray = splitWithAccount(bankLogic.getCustomer(inputPNo));
		
		String[] options = null;
		//Hämta konton tillgängliga
		if (customerArray.length >= 2) {
			int i = customerArray.length; // Gå igenom alla konton
			int index = 1; // starta på index 1 för att hoppa över namnet
			ArrayList<String> tempArrayList = new ArrayList<String>();
			while (i > index) {
				String temp = customerArray[index];
				tempArrayList.add(temp);
				index++;
			}
			options = Arrays.copyOf(tempArrayList.toArray(), tempArrayList.size(), String[].class);
		}
		
		//Skapa panel innehåll
	    comboBox = new JComboBox(options);
	    JLabel labelWithdraw = new JLabel("Uttag");
	    JLabel labelChooseAccount = new JLabel("Välj konto du önskar ta ut från:");
	    JLabel labelWithdrawAmount = new JLabel("Skriv in önskat uttagsbelopp:");
	    amountTextField = new JTextField(TEXT_WIDTH);
	    JButton withdrawButton = new JButton("Genomför uttag");
	    withdrawButton.addActionListener(listener);
		
	    
	    // Skapa topppanel
	    JPanel headingPanel = new JPanel();
	    headingPanel.add(logoSimpleShow);
	    headingPanel.add(labelWithdraw);
	    
	    //Skapa panel med info
	    JPanel withdrawPanel = new JPanel();
	    withdrawPanel.add(labelWithdraw);
	    withdrawPanel.add(labelChooseAccount);
	    withdrawPanel.add(comboBox);
	    withdrawPanel.add(labelWithdrawAmount);
	    withdrawPanel.add(amountTextField);
	    withdrawPanel.add(withdrawButton);
	    
	    
	    withdrawFrame.add(headingPanel);
	    withdrawFrame.add(withdrawPanel);
	    withdrawFrame.setSize(250, 390);
		//Padding
	    withdrawPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		withdrawFrame.setVisible(true);
		
		
	}
	
	/**
	 * Skapar fönstret för att genomföra en insättning.
	 */
	private void createDepositFrame() {
		depositFrame = new JFrame();
		depositFrame.setLayout(new BoxLayout(depositFrame.getContentPane(), BoxLayout.Y_AXIS));
		depositFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		customerArray = splitWithAccount(bankLogic.getCustomer(inputPNo));
		String[] options = null;
		//Hämta konton tillgängliga
		if (customerArray.length >= 2) {
			int i = customerArray.length; // Gå igenom alla konton
			int index = 1; // starta på index 1 för att hoppa över namnet
			ArrayList<String> tempArrayList = new ArrayList<String>();
			while (i > index) {
				String temp = customerArray[index];
				tempArrayList.add(temp);
				index++;
			}
			options = Arrays.copyOf(tempArrayList.toArray(), tempArrayList.size(), String[].class);
		}
		
		//Skapa panel innehåll
	    comboBox = new JComboBox(options);
	    JLabel labelDeposit = new JLabel("Insättning");
	    JLabel labelChooseAccount = new JLabel("Välj konto du önskar sätta in pengar på:");
	    JLabel labelDepositAmount = new JLabel("Skriv in önskat insättningsbelopp:");
	    amountTextField = new JTextField(TEXT_WIDTH);
	    JButton depositButton = new JButton("Genomför insättning");
	    depositButton.addActionListener(listener);
		
	    
	    // Skapa topppanel
	    JPanel headingPanel = new JPanel();
	    headingPanel.add(logoSimpleShow);
	    headingPanel.add(labelDeposit);
	    
	    //Skapa panel med info
	    JPanel depositPanel = new JPanel();
	    depositPanel.add(labelDeposit);
	    depositPanel.add(labelChooseAccount);
	    depositPanel.add(comboBox);
	    depositPanel.add(labelDepositAmount);
	    depositPanel.add(amountTextField);
	    depositPanel.add(depositButton);
	    
	    
	    depositFrame.add(headingPanel);
	    depositFrame.add(depositPanel);
	    depositFrame.setSize(250, 390);
		//Padding
	    depositPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	    depositFrame.setVisible(true);
		
		
	}
	
	/**
	 * Skapar ett nytt kreditkonto för användaren.
	 */
	private void createNewCreditAccount() {
		int kontoNr;
		kontoNr = bankLogic.createCreditAccount(inputPNo);
		if (kontoNr == -1) {
			JOptionPane.showMessageDialog(null, "Skapande av konto misslyckades", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else  {
			JOptionPane.showMessageDialog(null, "Konto med kontonummer " + kontoNr + " är skapat." , "Successfully created", JOptionPane.INFORMATION_MESSAGE);
		}
		disposeOpenFrames();
		createShowAccoutsFrame();
		
	}
	
	/**
	 * Skapar ett nytt sparkonto för användaren.
	 */
	private void createNewSavingsAccount() {
		int kontoNr;
		kontoNr = bankLogic.createSavingsAccount(inputPNo);
		if (kontoNr == -1) {
			JOptionPane.showMessageDialog(null, "Skapande av konto misslyckades", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else  {
			JOptionPane.showMessageDialog(null, "Konto med kontonummer " + kontoNr + " är skapat." , "Successfully created", JOptionPane.INFORMATION_MESSAGE);
		}
		disposeOpenFrames();
		createShowAccoutsFrame();
		
	}
	
	/**
	 * Skapar fönstret för att välja kontotyp vid öppning av nytt konto.
	 */
	private void createNewAccountFrame() {
		createNewAccountFrame = new JFrame();
		createNewAccountFrame.setLayout(new BoxLayout(createNewAccountFrame.getContentPane(), BoxLayout.Y_AXIS));
		createNewAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//SKapa label + buttons + actionlisteners
		JLabel openNewAccountHeading = new JLabel("Vilken typ av konto önskar du öppna?");
		JButton createSavingsAccountButton = new JButton("Skapa sparkonto");
		createSavingsAccountButton.addActionListener(listener);
		JButton createCreditAccountButton = new JButton("Skapa kreditkonto");
		createCreditAccountButton.addActionListener(listener);
		
		//Skapa paneler och lägga till element
		JPanel headPanel = new JPanel();
		headPanel.add(openNewAccountHeading);
		JPanel panel = new JPanel();
		panel.add(createSavingsAccountButton);
		panel.add(createCreditAccountButton);
		
		//Lägg till panel i frame
		createNewAccountFrame.add(headPanel);
		createNewAccountFrame.add(panel);
		createNewAccountFrame.setSize(350, 250);
		//Padding
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		createNewAccountFrame.setVisible(true);
		
	}
	
	/**
	 * Skapar fönstret för att visa användarens olika konton.
	 */
	private void createShowAccoutsFrame() {
        showAccountsFrame = new JFrame();
        showAccountsFrame.setLayout((new BoxLayout(showAccountsFrame.getContentPane(), BoxLayout.Y_AXIS))); // 4 rows, 1 column
        showAccountsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add menu
        JMenuBar menuBar = new JMenuBar();
        menuBar = addmenu(menuBar);
        showAccountsFrame.setJMenuBar(menuBar);

        // Knappar + actionListeners
        JButton addAccount = new JButton("Öppna konto");
        JButton removeAccount = new JButton("Avsluta konto");
        JButton showTransactionsButton = new JButton("Visa transaktioner");
        
        addAccount.addActionListener(listener);
        removeAccount.addActionListener(listener);
        showTransactionsButton.addActionListener(listener);

        JLabel accountsHeading = new JLabel("Konton");

        // Create Panels
        JPanel buttonPanel = new JPanel();
        //buttonPanel.setLayout(new GridLayout(1, 3)); // 1 row, 3 columns
        buttonPanel.add(addAccount);
        buttonPanel.add(removeAccount);
        buttonPanel.add(showTransactionsButton);

        JPanel headingPanel = new JPanel();
        headingPanel.add(accountsHeading);

        JPanel accountHeadingPanel = new JPanel();

        // Add components to panels
        showAccountsFrame.add(headingPanel);
        customerArray = splitWithAccount(bankLogic.getCustomer(inputPNo));
        if (customerArray.length >= 2) {
            showAccountsFrame.add(accountHeadingPanel);

            // Table model for account data
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Kontonummer");
            tableModel.addColumn("Saldo");
            tableModel.addColumn("Kontotyp");
            tableModel.addColumn("Ränta");

            // Fill the table model with data
            for (int i = 1; i < customerArray.length; i++) {
                String[] accountData = customerArray[i].split("\\s+");
                tableModel.addRow(accountData);
            }

            // Create the table
            JTable accountTable = new JTable(tableModel);
            JScrollPane tableScrollPane = new JScrollPane(accountTable);
            showAccountsFrame.add(tableScrollPane);
        }

        showAccountsFrame.add(buttonPanel);

        showAccountsFrame.setSize(550, 340);
        showAccountsFrame.setVisible(true);
        homePageFrame.dispose();
    }
	
	/**
	 * Loggar in användaren och öppnar användarens startsida.
	 */
	private void logOn() {
		List<String> currentCustomer = null;
			currentCustomer = bankLogic.getCustomer(pNoField.getText());
		
		if (currentCustomer == null) {
			logger.warning("Misslyckat inloggningsförsök för användare med personnummer: " + pNoField.getText());
			JOptionPane.showMessageDialog(null, "Kunde inte hitta kund, försök igen", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			logger.info("Användare med personnummer " + pNoField.getText() + " loggade in.");
	        disposeOpenFrames();
			createPersonalHomePage(currentCustomer);
		}
	}
	

/**
 * Skapar fönstret för att mata in användaruppgifter vid skapande av ny användare.
 */
	private void createUserInputFrame() {

		createUserFrame = new JFrame();
		createUserFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		nameTextField = new JTextField(TEXT_WIDTH);
		nameTextField.setBorder(BorderFactory.createTitledBorder("Namn: "));
		nameTextField.setPreferredSize(new Dimension(100, nameTextField.getPreferredSize().height));
		lastnameTextField = new JTextField(TEXT_WIDTH);
		lastnameTextField.setBorder(BorderFactory.createTitledBorder("Efternamn: "));
		PNumberTextfield = new JTextField(TEXT_WIDTH);
		PNumberTextfield.setBorder(BorderFactory.createTitledBorder("Personnummer (YYMMDDNNNN): "));
		newCustomerButton = new JButton("Skapa användare");
		JPanel panel = new JPanel(new GridLayout(4, 1));
		panel.add(nameTextField);
		panel.add(lastnameTextField);
		panel.add(PNumberTextfield);
		panel.add(newCustomerButton);
		createUserFrame.add(panel);
		createUserFrame.setSize(350, 240);
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		createUserFrame.setVisible(true);
		
		newCustomerButton.addActionListener(listener);
		
	}
	
	/**
	 * Skapar en ny användare baserat på inmatade uppgifter.
	 */
	private void createNewUser() {
		Boolean returnIs;
		returnIs = bankLogic.createCustomer(nameTextField.getText(), lastnameTextField.getText(), PNumberTextfield.getText());
		if (returnIs) {
			JOptionPane.showMessageDialog(null, "Användaren är skapad", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (!returnIs) {
			JOptionPane.showMessageDialog(null, "Skapande av användare misslyckades", "Fail", JOptionPane.INFORMATION_MESSAGE);
		}
		createUserFrame.dispose();
	}
}