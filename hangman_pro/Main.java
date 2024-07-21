package hangman_pro;


import java.util.Scanner;


public class Main {
	public static void main(String[] args) {
		Game game = new Game();
		
		while(true) {
			cls();
			Scanner scan = new Scanner(System.in);
			showMenu();
			System.out.print(">>> ");
			String choice = scan.nextLine().strip();
			
			if(choice.equals("1")) {
				cls();
				gameFlow(game);
			}
			else if(choice.equals("2")) {
				cls();
				settingsFlow(game);
			} 
			else {
				break;
			}
		}
	}
	
	
	public static void showMenu() {
		System.out.println("[1] - Play game");
		System.out.println("[2] - Settings");
		System.out.println("[3] - Exit");
	}
	
	
	public static void showSettings() {
		cls();
		System.out.println("[1] - Show statistic");
		System.out.println("[2] - Clear statistic");
		System.out.println("[3] - New value of max wrong answers");
		System.out.println("[4] - Show all words");
		System.out.println("[5] - Add new word in list");
		System.out.println("[6] - Back");
	}
	
	public static void settingsFlow(Game game) {
		while(true) {
			showSettings();
			System.out.print("\n>>> ");
			Scanner scan = new Scanner(System.in);
			
			String choice = scan.nextLine().strip();
			if(choice.equals("1")) {
				game.showStatistic();
			}
			else if(choice.equals("2")) {
				game.clearStatistic();
			}
			else if(choice.equals("3")) {
				game.newMaxWrongs();
			}
			else if(choice.equals("4")) {
				game.showAllWords();
			}
			else if(choice.equals("5")) {
				game.addNewWord();
			}
			else {
				break;
			}
		}
	}
	
	public static void gameFlow(Game game) {
		game.setRandomWord();
		while(true) {
			game.showHangman();
			game.showUngessedWord();
			game.getLetter();
			
			if(game.checkWord()) {
				System.out.println("\nYou WON!");
				break;
			}
			
			if(game.tooMuchWrongAnswes()) {
				System.out.println("\nYou lose ((((");
				break;
			}
		}
	}
	
	public static void cls() {
		System.out.print("\033[H\033[2J");
	}
}
