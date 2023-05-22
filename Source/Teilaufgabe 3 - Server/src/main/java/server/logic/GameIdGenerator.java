package server.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.data.GameId;

public class GameIdGenerator {
	
	final int MAX_LENGTH = 5;
	
	private List<Character> alphabet = new ArrayList<>();
	
	public GameIdGenerator() {
		this.generateAlphabet();
	}
	
	private void generateAlphabet() {
		for(char c = 'a'; c <= 'z'; c++) 
		    alphabet.add(c);
		for(char c = 'A'; c <= 'Z'; c++) 
		    alphabet.add(c);
	}
	
	public GameId generateRandomID() {
		char[] gameId = new char[MAX_LENGTH];
		Random random = new Random();
		for(int index = 0; index < MAX_LENGTH; index++) 
			gameId[index] = alphabet.get(random.nextInt(alphabet.size()));
		
		return new GameId(gameId.toString());
	}
}
