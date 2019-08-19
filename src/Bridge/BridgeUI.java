package Bridge;


//import java.io.File;




import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.util.ArrayList;
//import java.util.List;
import java.util.Optional;
import java.util.Random;
//import java.util.Scanner;







import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javafx.scene.input.MouseEvent;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class BridgeUI extends Application {
	
	TextField userTextField;
	HBox startBox;
	HBox handBox;
	VBox centerBox;
	Stage startStage;
	
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
		if(System.getProperty("os.name").equals("Mac OS X"))
    	try{
            //System.getProperties().list(System.out);

        	com.apple.eawt.Application.getApplication().setDockIconBadge("Bridge");
        	com.apple.eawt.Application.getApplication().setDockIconImage(ImageIO.read(getClass().getResource( "/resources/appIcon.png" )));
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    	}
    	startStage = primaryStage;
        primaryStage.setTitle("Bridge");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
    	Text errorText = new Text("Please enter a valid name.");
    	errorText.setFont(Font.font("Tahoma", FontWeight.THIN, 14));
    	errorText.setFill(Color.RED);
        grid.add(errorText, 1, 2);
        errorText.setVisible(false);

        Button btn = new Button();
        btn.setText("Go");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                if(userTextField.getText().equals("")) {
                    errorText.setVisible(true);
                }else{
                    errorText.setVisible(false);
                    //StackPane root = new StackPane();
                    SSBorderPane borderPane = new SSBorderPane();
                    borderPane.setMinSize(940,400);
                    //borderPane.setMaxSize(550,480);
                    //primaryStage.setMinSize(borderPane.getMinWidth(),borderPane.getMinHeight());
                    primaryStage.setMinHeight(primaryStage.getHeight());
                   // primaryStage.setMaxWidth(borderPane.getMaxWidth());

                    createGame(userTextField.getText(),borderPane);
                    Scene mainScene = new Scene(borderPane);
//                    primaryStage.setMaxHeight(mainScene.getHeight());

                    primaryStage.minWidthProperty().bind(mainScene.heightProperty().multiply(2.35));
                    primaryStage.maxWidthProperty().bind(mainScene.heightProperty().multiply(2.35));
                   // primaryStage.minHeightProperty().bind(mainScene.heightProperty());

                    //mainScene.getStylesheets().add("file://"+ (new File("resources/style.css").getAbsolutePath()));
                    mainScene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());


                    primaryStage.setScene(mainScene);
                    primaryStage.centerOnScreen();
                    //primaryStage.setMaximized(true);
                }
            }
        });
        
        StackPane root = new StackPane();
        //root.getChildren().add(btn);
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0);

        Label userName = new Label("Player Name:");
        grid.add(userName, 0, 1);

        userTextField = new TextField();
        userTextField.setPromptText(System.getProperty("user.name"));

        userTextField.setOnAction(e ->{
        	btn.fire();
        });
        grid.add(userTextField, 1, 1);

        
        grid.add(btn, 2, 1);

        root.getChildren().add(grid);
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.show();
    }
    private void createGame(String playerName, SSBorderPane root) {
    	Game game = new Game();
		Deck deck = new Deck();
        centerBox = new VBox(5);
        handBox = new HBox(5);
        startBox = new HBox(12);
        
        centerBox.setPadding(new Insets(30,30,30,30));

        handBox.setDisable(true);
        
		Button dealBtn = new Button();
		dealBtn.setText("Deal");
		dealBtn.setDisable(true);
		dealBtn.setId("centerButtons");


        Button shuffleBtn = new Button();
        shuffleBtn.setText("Shuffle");
    	shuffleBtn.setId("centerButtons");
        shuffleBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
        		//System.out.println("New Deck:\n" + Arrays.toString(deck.deckArray));
        		deck.shuffleDeck();
        		//System.out.println("Shuffled Deck:\n" + Arrays.toString(deck.deckArray));
        		if(dealBtn.disabledProperty() != null)dealBtn.setDisable(false);
        		

            }
        });
        dealBtn.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent event) {
            	root.setDisable(true);
        		deck.dealToGame(game);
        		startBox.getChildren().clear();
     			//root.getChildren().remove(root.getChildren().get(root.getChildren().size()-1));
        		Runnable dealTask = () -> {

        			for(int j = 0; j < game.getNumberOfPlayers();j++) {
        				final int i = j;
     	       			Platform.runLater(()-> {
     	       				try{
     	       					Thread.sleep(100);
     	       				}catch(Exception e) {
     	       					e.printStackTrace();
     	       				}
     	       				addCardsForPlayer(game.playerWithNumber(i+1));

     	       			});
        			}
        		};
        		new Thread(dealTask).start();
            	root.setDisable(false);


            }
        });        
        centerBox.setAlignment(Pos.CENTER);
        startBox.setAlignment(Pos.CENTER);
        handBox.setAlignment(Pos.CENTER);
        //grid.setHgap(10);
        //grid.setVgap(10);
        //startBox.setPadding(new Insets(0, 0, 0, 0));
        
        startBox.getChildren().addAll(shuffleBtn,dealBtn);
        centerBox.getChildren().addAll(startBox,handBox);
        game.setTrickBox(handBox);

 		
		new Player(playerName , game,false);
		try {
			for(int i = 0; i < 3; i++){
			new Player(chooseName(),game,true);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//double centerWidth = playerGrid(game.playerWithNumber(1)).getWidth();
		//centerWidth -= playerGrid(game.playerWithNumber(1)).getHeight()*2;
		//centerBox.setMinWidth(centerWidth);
        //centerBox.maxWidthProperty().bind(playerGrid(game.playerWithNumber(1)).heightProperty());
        //centerBox.minWidthProperty().bind(playerGrid(game.playerWithNumber(1)).heightProperty());

        root.set(centerBox, 0);
        //centerBox.setStyle("-fx-border-width:1;" + "-fx-border-color:blue;");

		for(int i = game.getNumberOfPlayers(); i > 0; i--){
	        root.set(playerGrid(game.playerWithNumber(i)), i);
		}
	    //centerBox.toBack();

    }
    private VBox playerGrid(Player player) {
    	boolean isSidePos = positionForPlayer(player.getPlayerNumber()) == Pos.CENTER_LEFT || positionForPlayer(player.getPlayerNumber()) == Pos.CENTER_RIGHT;
    	int multiplier = (isSidePos && positionForPlayer(player.getPlayerNumber()) == Pos.CENTER_LEFT) ? 1 : -1;

    	VBox playerBox = new VBox(10);
    	HBox handBox = new HBox(-45);
    	handBox.setAlignment(Pos.CENTER);
    	playerBox.setAlignment(Pos.BOTTOM_CENTER);
    	//playerBox.setStyle("-fx-border-width:1;" + "-fx-border-color:red;");
    	

    	//playerBox.setAlignment(positionForPlayer(player.getPlayerNumber()));
    	playerBox.setPadding(new Insets(5, 5, 5, 5));
        if(isSidePos){
        	playerBox.setRotate(multiplier*90);
        	//playerBox.setMaxSize(playerBox.getPrefHeight(), playerBox.getWidth());
        	//playerBox.setMinSize(0,0);

        	//playerBox.setPrefSize(playerBox.getPrefHeight(), playerBox.getHeight());
        	//playerBox.setMaxHeight(playerBox.getHeight());
        	//playerBox.setMaxHeight(playerBox.getHeight());
        	
        	//playerBox.translateYProperty().bind(centerBox.layoutYProperty());
        	
        	playerBox.maxWidthProperty().bind(centerBox.heightProperty());
        	playerBox.minWidthProperty().bind(centerBox.heightProperty());
        	//playerBox.translateYProperty().bind(centerBox.heightProperty());
        	
        	
        	//playerBox.prefHeightProperty().bind(playerBox.widthProperty());
        	//playerBox.minHeightProperty().bind(centerBox.widthProperty());
        	//playerBox.maxHeightProperty().bind(centerBox.widthProperty());
        	//playerBox.prefWidthProperty().bind(playerBox.heightProperty());

        }
    	Text playerName = new Text(player.name + ((player.isAI) ? " (bot)" : "") + " - Score: " + player.getScore());
    	playerName.setFont(Font.font("Tahoma", FontWeight.LIGHT, 14));
    	playerName.setId("playerLabel");

        playerBox.getChildren().add(playerName);
        playerBox.getChildren().add(handBox);
        player.setBox(playerBox);
	    centerBox.minWidthProperty().bind(centerBox.heightProperty());


    	return playerBox;
    }
    private Pos positionForPlayer(int playerNumber) {
    	switch(playerNumber){
    	case 1:
    		return Pos.BOTTOM_CENTER;
    	case 2:
    		return Pos.CENTER_RIGHT;
    	case 3:
    		return Pos.TOP_CENTER;
    	case 4:
    		return Pos.CENTER_LEFT;
    	default:
    		return Pos.CENTER;
    	}
    }
    private void addCardsForPlayer(Player player) {
    	Runnable dealTask = () -> {
    		player.getPlayerBox().setDisable(true);
    	for(int j = 0; j < player.hand.size(); j++) {
    		try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			final int i = j;
    		Platform.runLater(() -> {
    		Button cardButton = new Button();

    		//cardButton.setText(player.hand.get(i).toShortString());
    		((HBox)player.getPlayerBox().getChildren().get(1)).getChildren().add(cardButton);

    		cardButton.setPadding(new Insets(0,0,0,0));
            //File imageFile = new File((!player.isAI) ? ("resources/png/cropped/" + player.hand.get(i).toShortString() + ".png") : "resources/back.png");
            
    		Image image = new Image(getClass().getResourceAsStream((!player.isAI) ? ("/resources/.png/cropped/" + player.hand.get(i).toShortString() + ".png") : "/resources/back.png"),225, 313, false, false);
    		//Image image = new Image((imageFile.toURI().toString()),225, 313, false, false);

    		cardButton.setDisable(player.isAI);
    		cardButton.setOpacity(1);
    		ImageView imageView = new ImageView(image);

    		cardButton.setMaxSize(67,95);
    		imageView.fitHeightProperty().bind(cardButton.maxHeightProperty());
    	    imageView.fitWidthProperty().bind(cardButton.maxWidthProperty()); 
            //imageView.setClip(cardButton);

            cardButton.setGraphic(imageView);
            cardButton.setId(player.hand.get(i).toShortString());
            if(!player.isAI) {
                player.getPlayerBox().toFront();
                changeBackgroundOnHoverUsingEvents(cardButton);
            	cardButton.setOnAction(new EventHandler<ActionEvent>() {
        		
            		@Override
            		public void handle(ActionEvent event) {
        	    		playCard(player,cardButton);
        	    		updateHandIndices(player);

            		}
            	});    
            }
    	});

    	}
		//player.getPlayerBox().setDisable(false);
		Platform.runLater(() -> {
			if(player.getPlayerNumber() == 1)displayTrickWinner(player,null);
		});

    	};
    	new Thread(dealTask).start();

    }
    private void updateHandIndices(Player player) {
    	ObservableList<Node> cards = ((HBox)player.getPlayerBox().getChildren().get(1)).getChildren();
    	for(Node b:cards)
    		changeBackgroundOnHoverUsingEvents(b);

    }
    private void playCard(Player player, Button cardButton){
    	int index = ((HBox)player.getPlayerBox().getChildren().get(1)).getChildren().indexOf(cardButton);
    	player.setSelectedIndex(index);
    	if(!player.checksOut()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("You didn't follow suit!");
			alert.setHeaderText("You must follow suit!");
			alert.showAndWait();
    		return;
    	}
    		/*while(!player.game.isNeedsInput()){
    			playCard(player,cardButton);
    		}*/
    	else{
    		//player.game.getTrick().add(player.getCardFromUser((!player.game.isFirstCardOfTrick()) ? player.game.getTrick().get(0) : null));
			player.game.playTurn();
			//remove/add AI cards
    		//if(player.game.getTrick().size() != player.game.getNumberOfPlayers())
    		//	player.game.playTurn();
			//player.sendToTrickBox(cardButton);
			//player.getPlayerBox().getChildren().remove(cardButton);
			player.setSelectedIndex(-1);

    	}
    	if(player.game.isTrickOver())
    		displayTrickWinner(player.game.getTrickWinner(),player.game.getTrick());

    }
    
    public void sendToTrickBox(Button cardButton) {
    	if(handBox.getChildren().size() >= 4)
    		handBox.getChildren().clear();
    	handBox.getChildren().add(cardButton);

    }
    public void displayTrickWinner(Player winner,ArrayList<Card> trick) {
		winner.game.setTrickIsOver(false);
		//Platform.runLater( () -> { 
		//	try{
				((Text)winner.getPlayerBox().getChildren().get(0)).setText(winner.name + ((winner.isAI) ? " (bot)" : "") + " - Score: " + winner.getScore());
		//	}catch(Exception e) {
		//		System.err.println(e.getMessage());
		//	}
		//});
		
		// start the thread
		//new Thread(dealCardTask).start();


		Button continueBtn = new Button();
		continueBtn.setText("Continue");
		continueBtn.setId("centerButtons");
        Text winnerText = new Text("Winner: " + winner.name + " (and " + winner.getPlayerPartner().name + ")");
        winnerText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 13));
		winner.game.playerWithNumber(1).getPlayerBox().setDisable(true);
		if(trick == null){
			winnerText.setText("");
			continueBtn.setText("Begin");
		}


		continueBtn.setOnAction((event)->{
			startBox.getChildren().clear();
			handBox.getChildren().clear();
			winner.game.playerWithNumber(1).getPlayerBox().setDisable(false);

			if(!winner.hand.isEmpty())
				winner.game.playTurn();
			else{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Game Over");
				Player gameWinner = winner.game.winner(winner.game.getNumberOfPlayers()/2);
				alert.setHeaderText(gameWinner.name + " (and " + gameWinner.getPlayerPartner().name + ")" + ", won the game!");
				alert.setContentText("Winning Score: " + (gameWinner.getScore() + gameWinner.getPlayerPartner().getScore()) +  "\nNow what?");
				ButtonType buttonTypeOne = new ButtonType("Restart");
				ButtonType buttonTypeTwo = new ButtonType("Breakdown");
//				buttonTypeTwo.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
				ButtonType buttonTypeThree = new ButtonType("Close");
				ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

				Optional<ButtonType> result = alert.showAndWait();
				alert.setWidth(alert.getWidth()*1.3);
				if (result.get() == buttonTypeOne){
					
				    // ... user chose "One"
                    alert.close();

                    SSBorderPane borderPane = new SSBorderPane();
                    borderPane.setMinSize(940,400);
                    startStage.setMinHeight(startStage.getHeight());

                    createGame(winner.game.playerWithNumber(1).name,borderPane);
                    Scene mainScene = new Scene(borderPane);
                    startStage.minWidthProperty().bind(mainScene.heightProperty().multiply(2.35));
                    startStage.maxWidthProperty().bind(mainScene.heightProperty().multiply(2.35));

                    mainScene.getStylesheets().add(getClass().getResource("/resources/style.css").toExternalForm());
                    //mainScene.getStylesheets().add("file://"+ (new File("resources/style.css").getAbsolutePath()));

                    startStage.setScene(mainScene);
                    startStage.centerOnScreen();

				} else if (result.get() == buttonTypeTwo) {
				    // ... user chose "Two"
					//System.out.println(winner.game.trickStore.toString());
					for(int i = 0; i < winner.game.trickStore.size(); i++) {
						System.out.println("Trick " + i + "\nWinner: " + ((Player)winner.game.trickStore.get(i).get("Winner")).name);
						System.out.println("Winning Card: " + winner.game.trickStore.get(i).get("Winning Card"));
						System.out.println("Trick: \n\t" + winner.game.trickStore.get(i).get("Trick"));
					}
				} else if (result.get() == buttonTypeThree) {
				    // ... user chose "Three"
					System.exit(0);
				} else {
				    // ... user chose CANCEL or closed the dialog
                    alert.close();
				}
			}
		});
    	startBox.getChildren().addAll(winnerText,continueBtn);
    	
    }
    public String chooseName() throws FileNotFoundException {
    	try {
    		String filepath = "/resources/names.xml";
    		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    		Document doc = docBuilder.parse(getClass().getResourceAsStream(filepath));
    		//Document doc = docBuilder.parse(filepath);
     
    		NodeList list = doc.getElementsByTagName("name");
/*
		Scanner input = new Scanner(new FileReader("resources/names.txt"));

    	List<String> lines = new ArrayList<String>();

    	while( input.hasNext() ) {
    	    lines.add(input.nextLine());
    	}
    	input.close();
*/
    	// Choose a random one from the list
    	Random r = new Random();
    	return list.item(r.nextInt(list.getLength())).getTextContent();
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    		return "Bob";
    	}
    }
    private static final String STANDARD_BUTTON_STYLE = "";//"-fx-background-color: #C8C8C8;";
    private static final String HOVERED_BUTTON_STYLE  = "-fx-translate-y:-10;-fx-background-color: #F9F9F9; -fx-scale-x:1.2;-fx-scale-y:1.2;";

    public void changeBackgroundOnHoverUsingEvents( Node node) {
    	int index = ((HBox)node.getParent()).getChildren().indexOf(node);
        node.setStyle(STANDARD_BUTTON_STYLE);
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            node.setStyle(HOVERED_BUTTON_STYLE);

           int buttons = ((HBox)node.getParent()).getChildren().size();
           for(int i = 0; i < buttons; i++){
        	   if(i < index){
         		   TranslateTransition tt = new TranslateTransition(Duration.millis(200),((HBox)node.getParent()).getChildren().get(i));
        		   tt.setToX(-30);
        		   tt.play();
        	   
        		  // ((GridPane)node.getParent()).getChildren().get(i).setTranslateX(-20);
        	   }else if(i > index) {
        		   TranslateTransition tt = new TranslateTransition(Duration.millis(200), ((HBox)node.getParent()).getChildren().get(i));
        		   //tt.setByX(-20);
        		   tt.setToX(30);
        		   tt.play();    	  
        		   //((GridPane)node.getParent()).getChildren().get(i).setTranslateX(20);
        	   }
           }

          }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            node.setStyle(STANDARD_BUTTON_STYLE);
            
            int buttons = ((HBox)node.getParent()).getChildren().size();
            
            for(int i = 0; i < buttons; i++){
     		   TranslateTransition tt = new TranslateTransition(Duration.millis(200), ((HBox)node.getParent()).getChildren().get(i));
    		   tt.setToX(0);
    		   tt.play();    	  
            }

          }
        });
      }    

}