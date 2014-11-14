package Øvinger;

import java.util.Date;
import java.util.Scanner;

/*
 * Må helt ærlig si jeg synes denne oppgaven ble veldig klumsete formulert
 * Du sier ikke eksplisitt noe om hvordan man skal manipulere de
 * definerte kontoene, du sier bare vi skal definere en sjekk for en id
 * Oppgaven blir tvetydig, det er uhyre viktig at du er presis i formulering av oppgaven
 * Jeg løser oppgaven på min måte, så får vi se om det var slik som du mente her
 * 
 * Ang. siste innlevering, de overflødigheten kom av at jeg var på en tråd, også så jeg
 * en annen løsning i farta, det ble *veldig* fort skrevet og jeg glemte gjøre om de tingene som ble
 * meningsløse eller dårlig strukturert. Jeg ble noe flau da jeg så over senere!
 */

public class Account {
	
	private int id;
	private double balance;
	private double annualInterestRate;
	private Date dateCreated;

	public Account() {
		id = 0;
		balance = 0.D;
		annualInterestRate = 0.D;
		dateCreated = new Date();
	}

	public Account(int id, double balance) {
		this.id = id;
		this.balance = balance;
		this.annualInterestRate = 0.D;
		dateCreated = new Date();
	}
	
	public void withdraw(double amount) {
		if (amount > balance) {
			println("Illegal withdrawal amount.");
			return;
		}
		balance -= amount;
	}

	public void deposit(double amount) {
		balance += amount;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public int getId() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	public double getAnnualInterestRate() {
		return annualInterestRate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setAnnualInterestRate(double annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
	}

	public static void println(String str) {
		System.out.println(str);
	}
	
	public static void println(int val) {
		System.out.println(val);
	}
	
	public static void println(double val) {
		System.out.println(val);
	}
	
	public static void print(String str) {
		System.out.print(str);
	}
	
	public static class AccountManager {
		public static final int MAX_ACCS = Integer.MAX_VALUE;
		public static final int CAPACITY = 10;
		private static int size = 0;
		private static final float LOAD_FACTOR = 0.75F;
		private static Account[] accs = new Account[CAPACITY];
		private static boolean initialized = false;
		
		public static void init() {
			if(initialized) return;
			
			add(0, 100);
			add(1, 50);
			add(2, 350);
			add(3, 729);
			add(4, 999);
			add(5, 29);
			add(6, 196);
			add(7, 242);
			add(8, 430);
			add(9, 327);
			
			initialized = true;
		}
		
		public static void add(int id, double balance) {
			Account[] tmp = new Account[CAPACITY];
			System.out.println("accs[0] is: " + accs[0]);
			for(int i = 0; i < size; ++i) {
				tmp[i] = accs[i];
			}
			accs = new Account[CAPACITY + (int)(CAPACITY * LOAD_FACTOR)];
			System.out.println("Tmp[0] is: " + tmp[0]);
			for(int i = 0; i < size; ++i) {
				accs[i] = tmp[i];
			}
			accs[size++] = new Account(id, balance);
		}
		
		public static void add(Account acc) {
			add(acc.getId(), acc.getBalance());
		}
		
		/*
		 * Å bruke arrays for kontoer er ikke prosess-gunstig 
		 * Worst case O(n) for lineært søk 
		 * Skulle vi vært litt mer seriøse hadde vi hatt et hashmap
		 * eller annet assosiativt map
		 */
		public static boolean idExists(int id) {
			for (int i = 0; i < size; ++i) {
				if (accs[i].id == id) {
					return true;
				}
			}
			return false;
		}
		
		//returns -1 if account with that id does not exist
		public static int getArrayIndex(int id) {
			for (int i = 0; i < size; ++i) {
				if (accs[i].id == id) {
					return i;
				}
			}
			return -1;
		}
		
		//returns null if index would cause array out of bounds error
		public static void deposit(int index, double amount) {
			if(index < 0 || index >= size) return;
			accs[index].deposit(amount);
		}
		
		//returns null if index would cause array out of bounds error
		public static void withdraw(int index, double amount) {
			if(index < 0 || index >= size) return;
			accs[index].withdraw(amount);
		}
		
		//returns null if index would cause array out of bounds error
		public static Account get(int index) {
			if(index < 0 || index >= size) return null;
			return accs[index];
		}
		
		//throws runtime exception if index would cause array out of bounds error
		public static double getBalance(int index) {
			if(index < 0 || index >= size) throw new RuntimeException("in getBalance(int index): index out of bounds");
			return accs[index].getBalance();
		}
		
		//returns -1 if index would cause array out of bounds error
		public static int getId(int index) {
			if(index < 0 || index >= size) return -1;
			return accs[index].getId();
		}
		
		public static void printAccountIds() {
			for(int i = 0; i < size; ++i) {
				println("Account " + i + " id: " + accs[i].getId());
			}
		}

		public static void printMenu() {
			println("1 Balance");
			println("2 Withdraw");
			println("3 Deposit");
			println("4 Exit");
		}
	}
	

	public static void main(String[] args) {
		
		final int INPUT_BALANCE = 1;
		final int INPUT_WITHDRAW = 2;
		final int INPUT_DEPOSIT = 3;
		final int INPUT_EXIT = 4;
		
		AccountManager.init();
		int input = 0;
		Scanner scanner = new Scanner(System.in);
		
		print("Enter a valid account id: ");
		while(!AccountManager.idExists(input = scanner.nextInt()));
		
		int accIndex = input;
		while (true) {
			AccountManager.printMenu();
			input = scanner.nextInt();
			if(input == INPUT_EXIT) {
				println(AccountManager.getBalance(accIndex));
				break;
			}
			else if(input == INPUT_BALANCE) {
				println(AccountManager.get(accIndex).getBalance());
			}
			else if(input == INPUT_WITHDRAW) {
				print("Enter an amount to withdraw: ");
				int withdrawalSum = scanner.nextInt();
				AccountManager.withdraw(accIndex, withdrawalSum);
			}
			else if(input == INPUT_DEPOSIT) {
				print("Enter an amount to deposit: ");
				int depositSum = scanner.nextInt();
				AccountManager.deposit(accIndex, depositSum);
			}
			
			println(AccountManager.getBalance(accIndex));
		}
	}
}
