package Bridge;

import java.util.HashMap;
import java.util.Map;

public class Card {
	public enum Suit{
		CLUBS,
		DIAMONDS,
		HEARTS,
		SPADES;
		
		public String toString() {
	        return name().charAt(0) + name().substring(1).toLowerCase();
	    }
	}
	public enum Royal{
		JACK(11),
		QUEEN(12),
		KING(13),
		ACE(14); //ace is high
		private int value;
		private Royal(int value){
			this.value = value;
		}
		public String toString() {
	        return name().charAt(0) + name().substring(1).toLowerCase();
	    }
		public int getValue(){
			return value;
		}
		public static  boolean isInEnum(int value, Class<Royal> enumClass) {
			for (Royal e : enumClass.getEnumConstants())
				  if(e.value == value)
					  return true;
				  return false;
		}
	}
	int value;
	Suit suit;
	
	Card(int value,Suit suit){
		this.suit = suit;
		this.value = value;
	}
	public String suitName() {
		return suit.toString();
	}
    public String valueAsString(){
    	if(Royal.isInEnum(value,Royal.class))
    		return fromInt(value).toString();
    	return Integer.toString(value);
    }
    public String toString() {
        return valueAsString() + " of " + suitName();
    }
    public String toShortString() {
        return ((valueAsString().length() > 2) ? valueAsString().charAt(0) : valueAsString()) + "" + suitName().charAt(0);
    }    private static final Map<Integer, Royal> intToTypeMap = new HashMap<Integer, Royal>();
    static {
        for (Royal type : Royal.values())
            intToTypeMap.put(type.value, type);
    }
    public static Royal fromInt(int i) {
    	Royal type = intToTypeMap.get(Integer.valueOf(i));
        return type;
    }
}
