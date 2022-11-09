import java.util.ArrayList;
import java.util.List;


public class Dealer extends Role{
    private static List<Card> cards;
    private static Card hiddenCard;
    private static int count;

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Dealer.count = count;
    }

    private Dealer(){
        cards = new ArrayList<>();
    }

    public static Dealer dealerEnter(){
        return new Dealer();
    }

    @Override
    public void hit(Card card){
        if(null==hiddenCard){
            hiddenCard = card;

            cards.add(new Card("비밀카드", ""));
            return;
        }
        cards.add(card);
    }

    @Override
    public List<Card> getReceivedCards(){
        return cards;
    }

    @Override
    void initValues() {

        cards = new ArrayList<>();
        hiddenCard = null;
        count = 0;
    }

    @Override
    public void open(){
        System.out.println("Dealer 카드: " + cards);
    }

    public void totalCardOpen(){
        System.out.println("딜러카드를 오픈합니다.");
        if(null==hiddenCard){
            System.out.println("숨겨진 딜러카드가 없습니다.");
            return;
        }

        cards.set(0, hiddenCard);
    }
}
