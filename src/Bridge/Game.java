package Bridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.layout.HBox;

public class Game {
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private ArrayList<Card> trick = new ArrayList<Card>();
	ArrayList<Player> orderedList = new ArrayList<Player>();
	ArrayList<Map<String,Object>> trickStore = new ArrayList<Map<String,Object>>();
	private Player trickWinner;
	private boolean trickIsOver = false;
	private int resumePoint;
	private HBox trickBox;
	
	Game(){
		
	}
	public void addPlayer(Player player){
		this.playerList.add(player);
	}
	public int getResumePoint() {
		return resumePoint;
	}
	public void setResumePoint(int index) {
		resumePoint = index;
	}
	public int getNumberOfPlayers(){
		return playerList.size();
	}
	public Player playerWithNumber(int number){
		return playerList.get(number-1);
	}
	public boolean isFirstCardOfTrick(){
		return (trick.size() <= 0);
	}
	public Player getTrickWinner() {
		return trickWinner;
	}
	public void setTrickWinner(Player trickWinner) {
		this.trickWinner = trickWinner;
	}
	public void setTrickBox(HBox trickBox) {
		this.trickBox = trickBox;
	}
	public HBox getTrickBox() {
		return trickBox;
	}
	public ArrayList<Card> getTrick() {
		return trick;
	}
	public String[] getTrickNow() {
		String[] trickInCurrentState = new String[trick.size()];
		for(int i = 0; i < trick.size(); i++)
			trickInCurrentState[i] = trick.get(i).toString();
		return trickInCurrentState;
	}
	public void playTurn() {
			if(orderedList.isEmpty())arrangeNextTurnOrder();
			
			for(int i = getResumePoint(); i < getNumberOfPlayers(); i++) 
				if(orderedList.get(i).isAI)
					if(isFirstCardOfTrick())
						trick.add(orderedList.get(i).bestChoiceCard(null,null));
					else
						trick.add(orderedList.get(i).bestChoiceCard(trick.get(0),trick.get(trick.size()-1)));
				else{
					if(!orderedList.get(i).checksOut()){
						setResumePoint(i);
						break;
					}
		    		trick.add(orderedList.get(i).getCardFromUser((!isFirstCardOfTrick()) ? trick.get(0) : null));

					setResumePoint(0);
				}
//				if(orderedList.get(i).checksOut())
//					trick.add(orderedList.get(i).getCardFromUser((!isFirstCardOfTrick()) ? trick.get(0) : null));
		if(trick.size() >= getNumberOfPlayers()){
			System.out.println("Trick: " + trick.toString() + "\nWinner: " + getTrickWinner().name);

			setTrickIsOver(true);
			arrangeNextTurnOrder();

			storeTrickInfo();
		}
	}
	public void storeTrickInfo() {
		getTrickWinner().increaseScore();
		
		Map<String,Object> trickInfo = new HashMap<String,Object>();
		trickInfo.put("Winner", getTrickWinner());
		trickInfo.put("Winning Card", Player.trickWinningCard);
		trickInfo.put("Trick", new ArrayList<Card>(trick));
		trickStore.add(trickInfo);
		//all done clear current trick
		trick.clear();
		setResumePoint(0);
		//System.out.println("\n\n\n"+ trickInfo.toString() + "\n\n\n");
		
		

	}
	public void arrangeNextTurnOrder(){
		orderedList.clear();
		int startingPlayerNumber = ((getTrickWinner() != null))	? getTrickWinner().getPlayerNumber() : 4;


		while(orderedList.size() < getNumberOfPlayers()) {
			if(startingPlayerNumber <= 0)
				orderedList.add(playerWithNumber(getNumberOfPlayers() + startingPlayerNumber));
			else {
				orderedList.add(playerWithNumber(startingPlayerNumber));
			}
			startingPlayerNumber--;
		}
		
		
		System.out.println("\n\n" + orderedList.toString() + "\n\n");
	}
	public boolean isTrickOver() {
		return this.trickIsOver;
	}
	public void setTrickIsOver(boolean trickIsOver) {
		this.trickIsOver = trickIsOver;
	}
	public Player winner(int i) {
		return (i <= 1) ? playerWithNumber(i) : max(playerWithNumber(i), winner(--i));
	}
	public Player max(Player a, Player b) {
		return (a.getScore()+a.getPlayerPartner().getScore() > b.getScore()+b.getPlayerPartner().getScore()) ? a : b;
	}
	
}
