package Bridge;

//import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
//import java.util.Scanner;







import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Player {
	boolean isAI;
	String name;
	Game game;
	VBox playerBox;
	private int playerNumber;
	ArrayList<Card> hand = new ArrayList<Card>();
	static Card trickWinningCard;
	private int playerScore = 0;
	private int selectedIndex = -1;
	private Button currentCardButton;
	private Player playerPartner;
	
	Player(String name, Game game, boolean isAI){
		game.addPlayer(this);
		this.playerNumber = game.getNumberOfPlayers();
		this.name = name;
		this.game = game;
		this.isAI = isAI;
		if(playerNumber > 2)
			setPlayerPartner(game.playerWithNumber(playerNumber-2));
	}
	public Player getPlayerPartner() {
		return playerPartner;
	}
	public void setPlayerPartner(Player player) {
		playerPartner = player;
		player.playerPartner = this;
	}
	public int getPlayerNumber() {
		return playerNumber;
	}
	public Button getCurrentCardButton() {
		return currentCardButton;
	}
	public void setCurrentCardButton(Button cardButton) {
		currentCardButton = cardButton;
	}
 	public void addCardToHand(Card c){
		this.hand.add(c);
		if(hand.size() > 1)sort();
	}
	public void sort(){
		for(int i = 0; i < hand.size()-1; i++)
			for(int j = 1; j < hand.size(); j++)
				if(hand.get(j-1).value > hand.get(j).value)
					Collections.swap(hand, j-1, j);
		for(int i = 0; i < hand.size()-1; i++)
			for(int j = 1; j < hand.size(); j++)
				if(hand.get(j-1).suit.ordinal() > hand.get(j).suit.ordinal())
					Collections.swap(hand, j-1, j);
	}
	public Card playCardAtIndex(int index){
		Card c = hand.get(index);
			setCurrentCardButton(((Button)((HBox)getPlayerBox().getChildren().get(1)).getChildren().get(index)));
			
			//File imageFile = new File("resources/png/cropped/" + ((Button)getCurrentCardButton()).getId() + ".png");
    		Image image = new Image(getClass().getResource("/resources/.png/cropped/" + ((Button)getCurrentCardButton()).getId() + ".png").toExternalForm(),225, 313, false, false);
    		//Image image = new Image((imageFile.toURI().toString()),225, 313, false, false);



    		((ImageView)getCurrentCardButton().getGraphic()).setImage(image);
    		//getCurrentCardButton().setGraphic(imageView);

		Platform.runLater( () -> { 
			try{
				//player.getPlayerBox().getChildren().remove(cardButton);
				sendToTrickBox(getCurrentCardButton());
				getPlayerBox().getChildren().remove(getCurrentCardButton());
			}catch(Exception e) {
				System.err.println(e.getMessage());
			}
		});
		

		hand.remove(index);
		return c;
	}
    public void sendToTrickBox(Button cardButton) {
    	//cardButton.setStyle("-fx-scale-x:1.2; -fx-scale-y:1.2;");
		cardButton.setMaxSize(75,104);


    	if(game.getTrickBox().getChildren().size() >= 4)
    		game.getTrickBox().getChildren().clear();
    	game.getTrickBox().getChildren().add(cardButton);

    }

	public boolean checksOut() {
		int selectedCardIndex = this.selectedIndex;
		boolean failedToFindSuit = true;
		if(game.orderedList.size() == 0)game.arrangeNextTurnOrder();
		if(selectedIndex == -1)
			return false;
		
		if(game.orderedList.get(0) == this)
			return true;

		if(!game.isFirstCardOfTrick()) {
			System.out.println(Arrays.toString(game.getTrickNow()));
			for(int i = 0; i < hand.size(); i++)
				if(hand.get(i).suit == game.getTrick().get(0).suit)
					failedToFindSuit = false;
			if(!failedToFindSuit && hand.get(selectedCardIndex).suit != game.getTrick().get(0).suit) {
				System.out.println("You didn't follow suit!");
				return false;
			}else{
				return true;
			}
		}
		
		return false;
	}
	public Card getCardFromUser(Card playedCard) {
		
		int selectedCardIndex = this.selectedIndex;
		/*
		boolean failedToFindSuit = true;
		if(!game.isFirstCardOfTrick())
			System.out.println(Arrays.toString(game.getTrickNow()));
		System.out.println("Choose a card to play\nHand:" + hand);
		

		if(!game.isFirstCardOfTrick()) {
			for(int i = 0; i < hand.size(); i++)
				if(hand.get(i).suit == playedCard.suit)
					failedToFindSuit = false;
			if(!failedToFindSuit && hand.get(selectedCardIndex).suit != playedCard.suit) {
				System.out.println("You didn't follow suit!");
			}
		}
		
		//Scanner input = new Scanner(System.in);
		
		
		do {
			selectedCardIndex = input.nextInt();
			if(selectedCardIndex >= hand.size())System.out.println("You don't have that many cards!");

			if(!game.isFirstCardOfTrick() && selectedCardIndex < hand.size()) {
				for(int i = 0; i < hand.size(); i++)
					if(hand.get(i).suit == playedCard.suit)
						failedToFindSuit = false;
				while((!failedToFindSuit && hand.get(selectedCardIndex).suit != playedCard.suit)) {
					System.out.println("You didn't follow suit!");
					selectedCardIndex = input.nextInt();
				}
			}
		}
		while(selectedCardIndex >= hand.size());
		*/
		if(game.isFirstCardOfTrick())
			trickWinningCard = hand.get(selectedCardIndex);
		
		if(trickWinningCard != null && (hand.get(selectedCardIndex).suit == trickWinningCard.suit) && hand.get(selectedCardIndex).value >= trickWinningCard.value) {
			game.setTrickWinner(this);
			trickWinningCard = hand.get(selectedCardIndex);
		}

		//input.close();
		return playCardAtIndex(selectedCardIndex);
	}
	public Card bestChoiceCard(Card playedCard,Card lastCard){
		Card highestCard = new Card(0,Card.Suit.CLUBS);
		
		Random random = new Random();
		boolean failedToFindSuit = true;
		int index = -1;
		if(playedCard != null)
		for(int i = 0; i < hand.size(); i++)
			if(hand.get(i).suit == playedCard.suit){
				failedToFindSuit = false;
				if(hand.get(i).value > highestCard.value){
					highestCard = hand.get(i);
					index = i;
				}
			}
		if(index != -1)highestCard = playCardAtIndex(index);

		if(game.isFirstCardOfTrick() || failedToFindSuit){
			highestCard = playCardAtIndex(random.nextInt(hand.size()));
		}
		if(game.isFirstCardOfTrick())
			trickWinningCard = highestCard;
		
		if(highestCard != null && trickWinningCard != null && (highestCard.suit == trickWinningCard.suit) && highestCard.value >= trickWinningCard.value) {
			game.setTrickWinner(this);
			trickWinningCard = highestCard;
		}
		
		return highestCard;
	}
	public void increaseScore() {
		playerScore++;
	}
	public int getScore() {
		return playerScore;
	}
	public void setBox(VBox box) {
		this.playerBox = box;
	}
	public VBox getPlayerBox() {
		return playerBox;
	}
	public int getSelectedIndex() {
		return selectedIndex;
	}
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	public String toString() {
	        return "Player "+ playerNumber + (isAI ? " (bot)" : "") + " : " + name + "\nHand: " + ((!hand.isEmpty()) ? (hand.size() + " - " + hand.toString()) : "Empty\tScore:" + getScore());
	 }
}
