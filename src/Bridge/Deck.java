package Bridge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Bridge.Card.Suit;

public class Deck {
	Card[] deckArray = new Card[52];
	Deck() {
		//sorted deck //(i=0; i < deckArray-0; )--> ace=1
		for(int i = 1; i <= deckArray.length/4; i++)
			for(int j = 0; j < Suit.values().length; j++){
				deckArray[i+(j*deckArray.length/4)-1] = new Card(i+1,Suit.values()[j]);
			}
	}
	public void shuffleDeck() {
		Card tmp;
		int randomInt;
		Random random = new Random();
		for (int i = deckArray.length-1; i > 0; i--){
			randomInt = (random.nextInt(i));
			tmp = deckArray[randomInt];
			deckArray[randomInt] = deckArray[i];
			deckArray[i] = tmp;

		}
	}
	public void dealToGame(Game game){
		int cardsPerPlayer = deckArray.length/game.getNumberOfPlayers();
		ArrayList<Card> deckAsList = new ArrayList<Card>(Arrays.asList(deckArray));
		for(int i = 0; i < cardsPerPlayer; i++)
			for(int j = game.getNumberOfPlayers()-1; j >= 0; j--){
					game.playerWithNumber(j+1).addCardToHand(deckAsList.get(0));
					deckAsList.remove(0);
			}
	}
   
}
