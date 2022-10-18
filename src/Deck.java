import java.util.ArrayList;
import java.util.List;

public class Deck {
    //A,2~10,K,Q,J Heart/Spade/Clover/Diamond
    String[] kind = {"Heart", "Spade", "Clover", "Diamond"};
    String[] num = {"A","2","3","4","5","6","7","8","9","10","K","Q","J"};
    static final int round = 4;
    List<String> deck = new ArrayList<>(kind.length * num.length * round);

    private Deck(){
        generateDeck();
    }

    private void generateDeck() {
        for(int i=0; i<4; i++){
            for (String cardKind : kind) {
                for (String cardNum : num) {
                    deck.add(cardKind + cardNum);
                }
            }
        }
    }

    public static Deck getInstance(){
        return new Deck();
    }

    public void shuffle(){
        int ranNum;
        String temp;

        for(int i=0; i<deck.size(); i++){
            ranNum = (int)(Math.random() * deck.size());
            temp = deck.get(ranNum);
            deck.set(ranNum, deck.get(i));
            deck.set(i, temp);
        }
    }

    public String hit(){
        String returnCard = deck.get(deck.size()-1);
        deck.remove(deck.size()-1);
        return returnCard;
    }

    public void print(){
        for(int i=0; i<deck.size(); i++){
            System.out.println(i +": "+ deck.get(i));
        }
    }
}
