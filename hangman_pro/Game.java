package hangman_pro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Game {
	private ArrayList<String> words_list = new ArrayList<>();
	
	private final String PATH = "*your path*\\hangman_data.txt";
	private final String STATISTIC_PATH = "*your path*\\hangman_statistic.txt";
	
	private String current_word = null;
	private String unguessed_word = new String();
	
	private int max_wrong_answers = 5;
	private int wrong_answers = 0;
	
	private int[] stats = new int[4];
	
	
	public Game() {
		updateWordsList();
		updateData();
	}
	
	
	// Обновляет список слов для загадывания 
	private void updateWordsList() {
		String path = this.PATH;
		try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			while((line = reader.readLine()) != null) {
				words_list.add(line);
			}
		} catch (IOException ex) {
			System.out.println("error! File not found");
		}
	}
	
	
	// Добавляет новое слово в список слов для загадывания
	public void addNewWord() {
		cls();
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter word: ");
		String word = scan.nextLine().strip();
		
		if(wordIsCorrect(word)) {
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(this.PATH, true))) {
				writer.write(word+"\n");
			} catch (IOException ex) {
				System.out.println("error! File not found");
			} finally {
				words_list.clear();
				updateWordsList();
				System.out.println("File was added successfully!\n");
			}
		} else {
			System.out.println("error! Your word is not corret!");
		}
	}
	
	
	// Проверяет является ли слово корректным
	private boolean wordIsCorrect(String word) {
		if(word.length() > 0) {
			return true;
		} 
		return false;
	}
	
	
	// Устанавливает случайное слово
	public void setRandomWord() {
		this.unguessed_word = new String();
		Random random = new Random();
		int r_index = random.nextInt(this.words_list.size());
		this.current_word = words_list.get(r_index);
		for(int i = 0; i < current_word.length(); i++) {
			this.unguessed_word += "_";
		}
		this.wrong_answers = 0;
		
		updateParameter(0, stats[0]++);
	}
	
	
	// Выводит состояние смертника в консоль
	public void showHangman() {
		System.out.println("---------------");
		System.out.println("|             |");
		
		if(this.wrong_answers >= 1) System.out.println("|           (_)");
		else System.out.println("|             ()");
		
		if(this.wrong_answers >= 2) System.out.println("|          /| |\\");
		else System.out.println("|");
		
		if(this.wrong_answers >= 3) System.out.println("|         / |-| \\");
		else System.out.println("|");
		
		if(this.wrong_answers >= 4) System.out.println("|           | |");
		else System.out.println("|");
		
		if(this.wrong_answers >= 5) System.out.println("|           | |");
		else System.out.println("|");
		
		System.out.println("|");
		System.out.println("|");
	}
	
	

// ======================= Обработка пользовательского ввода (буква) ============

	// Получает букву от пользователя
	public void getLetter() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter letter: ");
		String letter = scan.nextLine();
		checkLetter(letter);
		
		updateParameter(2, stats[2]++);
	}
	
	
	// Проверяет входит ли буква, введенная пользователм в загаданное слово
	private void checkLetter(String str_letter) {
		if(str_letter.length() == 1) {
			char letter = str_letter.charAt(0);
			boolean isTrueLetter = false;
			
			char[] char_current_word = this.current_word.toCharArray();
			char[] char_unguessed_word = this.unguessed_word.toCharArray();
			
			for(int i = 0; i < char_current_word.length; i++) {
				if(char_current_word[i] == letter) {
					char_unguessed_word[i] = letter;
					isTrueLetter = true;
				}
			}
			
			if(!isTrueLetter) {
				System.out.println("\nYou guessed wrong :(");
				updateParameter(3, stats[3]++);
				this.wrong_answers++;
			} else {
				System.out.println("\nYou guessed :)");
			}
			
			this.unguessed_word = String.valueOf(char_unguessed_word);
		}
	}
	
	
	// Выводит сколько букв отгадал пользователь
	public void showUngessedWord() {
		char[] char_u_w = this.unguessed_word.toCharArray();
		for(char el : char_u_w) {
			System.out.print(el+" ");
		}
		System.out.println();
	} 
	
	
	// Проверяет кол-во неправильных ответов
	public boolean tooMuchWrongAnswes() {
		if(this.wrong_answers == this.max_wrong_answers) {
			return true;
		}
		return false;
	}
	
	
	// Проверяет на совпадение слова
	public boolean checkWord() {
		if(this.current_word.equals(this.unguessed_word)) {
			updateParameter(1, stats[1]++);
			return true;
		}
		return false;
	}

// =============================================================
	
	
	
// =============== Работа со статистикой ========================
	
	// Считывает файл со статистикой и сохраняет ее значения в список
	private void updateData() {
		try(BufferedReader reader = new BufferedReader(new FileReader(STATISTIC_PATH))) {
			String line;
			int i = 0;
			while((line = reader.readLine()) != null) {
				stats[i++] = Integer.parseInt(line);
			}
		} catch(IOException ex) {
			System.out.println("error ! File not found");
		}
	}
	
	
	// Обновляет значение параметра в файле
	private void updateParameter(int ind, int new_val) {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(STATISTIC_PATH))) {
			String stroke = new String();
			
			for(int i = 0; i < 4; ++i) {
				if(i == ind) stroke += new_val + "\n";
				else stroke += stats[i] + "\n";
			}
			writer.write(stroke);
		} catch(IOException ex) {
			System.out.println("error ! File not found");
		}
	}
		
	
	// Показывает статистику
	public void showStatistic() {
		cls();
		System.out.println("Statistic:");
		System.out.println("Games: " + stats[0]);
		System.out.println("Completed words: " + stats[1]);
		System.out.println("All steps: " + stats[2]);
		System.out.println("Wrong steps: " + stats[3] + "\n");
	}
	
	
	// Очистка статистики
	public void clearStatistic() {
		cls();
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(STATISTIC_PATH, false))) {
			writer.write("0\n0\n0\n0");
		} catch(IOException ex) {
			System.out.println("error! File not found");
		}
		
		updateData();		
		System.out.println("Statistic was cleared\n");
	}
	
// ==============================================================
	
	
	
	// Установление нового лимита максимального кол-ва ошибок за игру
	public void newMaxWrongs() {
		cls();
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter new value: ");
		try {
			this.max_wrong_answers = scan.nextInt();
		} catch(Exception ex) {
			System.out.println("error! You should enter a NUMBER!");
		} finally {
			cls();
			System.out.println("New value was added!\n");
		}
	}
	
	
	// Выводи все существующие слова в списке слов
	public void showAllWords() {
		cls();
		int i = 0;
		for(String el : this.words_list) {
			i++;
			if(i % 4 == 0) System.out.println();
			System.out.print(el+" ");
		}
	}
	
	
	// Очищает косноль
	private void cls() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
}