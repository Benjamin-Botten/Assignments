package Øvinger;

import java.util.Scanner;

public class FlightReservation {

	public static final int MAX_ROWSEATS = 9;
	public final int SEATS, ROWS;

	private char seats[];
	private SeatHashMap shm;
	public static final int[][] seatRegister = { { 1, 'A' }, { 2, 'B' }, { 3, 'C' }, { 4, 'D' }, { 5, 'E' }, { 6, 'F' }, { 7, 'G' }, { 8, 'H' }, { 9, 'I' } };
	
	/* Dette nedenfor gir bedre leselighet og skjønnere kode generelt, og hashmappet støtter også direkte bruk av SeatData typen som argument
	 * for inserts og gets, men jeg synes den versjonen jeg har gått for er mer morsom programmatisk, derfor ble det den.
	 * kan selvfølgelig kommenteres ut/inn og bruke shm.insert(seatRegister[i]); i init() istedet
	 */
	
//	public static final SeatData[] seatRegister = { new SeatData('A', 1), new SeatData('B', 2), new SeatData('C', 3),
//													 new SeatData('D', 4), new SeatData('E', 5), new SeatData('F', 6),
//													 new SeatData('G', 7), new SeatData('H', 8), new SeatData('I', 9),
//	};
	
	public static final char ID_RESERVED = 'X';
	public static final int INDEX_VALUE = 0;
	public static final int INDEX_CHAR = 1;

	public Scanner scanner;
	private boolean running;
	private int nReserved;

	public FlightReservation(int nSeats, int nRows) {
		SEATS = (nSeats > MAX_ROWSEATS || nSeats < 0) ? MAX_ROWSEATS : nSeats;
		ROWS = nRows <= 0 ? 1 : nRows;
		seats = new char[SEATS * ROWS];
		shm = new SeatHashMap();
		scanner = new Scanner(System.in);
		running = false;
		nReserved = 0;
	}

	public void init() {
		for (int i = 0; i < SEATS; ++i) {
			shm.insert((char) seatRegister[i][INDEX_CHAR], seatRegister[i][INDEX_VALUE]);
		}
		for (int y = 0; y < ROWS; ++y) {
			for (int x = 0; x < SEATS; ++x) {
				seats[x + y * SEATS] = (char) seatRegister[x][INDEX_CHAR];
			}
		}
		running = true;
	}

	public float percentReserved() {
		return ((float) nReserved / (SEATS * ROWS)) * 100.f;
	}

	public boolean isReserved(char seat, int row) {
		int val = shm.retrieve(seat);
		if (seats[(val - 1) + row * SEATS] == ID_RESERVED)
			return true;
		return false;
	}

	public boolean isRowFilled(int row) {
		for (int i = 0; i < SEATS; ++i) {
			if (seats[i + row * SEATS] != ID_RESERVED) {
				return false;
			}
		}
		return true;
	}

	public void reserveSeat(char seat, int row) {
		if (!isReserved(seat, row)) {
			int val = shm.retrieve(seat);
			seats[(val - 1) + row * SEATS] = ID_RESERVED;
			nReserved++;
		}
	}

	public void handleInput() {
		char seat = 0;
		int row = 0;
		boolean done = false;
		do {

			row = scanner.nextInt();
			while (row > ROWS || row <= 0) {
				if (row == -1) {
					running = false;
					return;
				}
				print("Please choose a row that exists: ");
				row = scanner.nextInt();
			}
			
			seat = scanner.next().charAt(0);
			while (seat < seatRegister[0][INDEX_CHAR] || seat > seatRegister[SEATS - 1][INDEX_CHAR]) {
				print("Please choose a seat that exists: ");
				seat = scanner.next().charAt(0);
			}
			
			if(!(done = !isReserved((char)seat, row - 1))) {
				println("That seat is reserved, please try a different one.");
			} else {
				reserveSeat((char)seat, row - 1);
			}
			
		} while(!done);
		
		println("Seat reserved successfully! " + percentReserved() + "% reserved.");
		render();
	}

	public void render() {
		String space = " ";
		int maxDigits = 0;
		
		for(int i = ROWS; i >= 1; i /= 10) {
			maxDigits++;
		}
		
		for (int y = 0; y < ROWS; ++y) {
			String spaces = space;
			int digits = 0;
			print(y + 1);
			
			for(int i = y + 1; i >= 1; i /= 10) {
				digits++;
			}
			
			for(int i = 0; i < maxDigits - digits; i++) {
				spaces += space;
			}
			
			for (int x = 0; x < SEATS; ++x) {
				if(x == 0)
					print(spaces + seats[x + y * SEATS]);
				else 
					print(space + seats[x + y * SEATS]);
			}
			println();
		}
		println();
	}

	public void run() {
		init();

		println("Would you like to reserve a seat on the plane? (If not, enter -1 as row value to terminate, otherwise follow the instructions)");
		while (running) {
			print("Enter a row number (1-" + ROWS + ") " + "and a seat (A-" + (char) seatRegister[SEATS - 1][INDEX_CHAR] + "): ");
			handleInput();
		}
		println("Terminating flight reservation application...");
	}

	public static void print(String str) {
		System.out.print(str);
	}

	public static void print(int n) {
		System.out.print(n);
	}

	public static void println(String str) {
		System.out.println(str);
	}

	public static void println() {
		System.out.println("");
	}

	/*
	 * Auxiliary classes, skrevet for moro skyld
	 */
	static class SeatData {
		public final char seatKey;
		public final int seatValue;

		public SeatData(char key, int value) {
			seatKey = key;
			seatValue = value;
		}
	}

	static class SeatHashMap {
		private SeatData table[];
		private static final int MAX_ITEMS = 1 << 4; // 2^4 = 16, bevar verdien som en power of two

		public SeatHashMap() {
			table = new SeatData[MAX_ITEMS];
			for (int i = 0; i < table.length; ++i) {
				table[i] = null;
			}
		}

		public void insert(char key, int value) {
			if(invalidKey(key, "SeatHashMap does not map keys less than zero")) {
				return;
			}
			int h = hash(key);
			while (table[h] != null && table[h].seatKey != key) {
				h = hash((char) (h + 1));
			}
			table[h] = new SeatData(key, value);
		}

		public void insert(SeatData sd) {
			insert(sd.seatKey, sd.seatValue);
		}

		public int retrieve(char key) {
			int result = -1;
			int h = hash(key);
			while (table[h] != null && table[h].seatKey != key) {
				h = hash((char) (key + 1));
			}
			if (table[h] != null) {
				result = table[h].seatValue;
			}
			return result;
		}

		public int retrieve(SeatData sd) {
			return retrieve(sd.seatKey);
		}
		
		//En ekstremt simpel hash funksjon
		public static int hash(char key) {
			return (key & (MAX_ITEMS - 1));
		}
		
		//Returnerer true hvis erroren er valid
		private boolean invalidKey(char key, String errmsg) {
			if(key < 0) {
				println(errmsg);
				return true;
			}
			return false;
		}
	}

	public static void main(String[] args) {
		FlightReservation fr = new FlightReservation(6, 1000);
		fr.run();
	}
}
