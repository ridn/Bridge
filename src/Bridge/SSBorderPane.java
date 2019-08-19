package Bridge;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class SSBorderPane extends BorderPane {
	
	SSBorderPane() {	
	}
	
	public void set(Node node, int pos){
		switch(pos) {
		case 0:
			this.setCenter(node);
			break;
		case 1:
			this.setBottom(node);
			break;
		case 2:
			this.setRight(node);
			break;
		case 3:
			this.setTop(node);
			break;
		case 4:
			this.setLeft(node);
			break;
		default:
			break;
		}
	}
}
