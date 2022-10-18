public class Dealer extends Role{
    static String firstCard = "";
    private Dealer(){

    }

    public static Dealer dealerEnter(){
        return new Dealer();
    }

    @Override
    public void open(){
        if(!"".equals(firstCard)){
            System.out.print("Dealer : 비밀카드, ");
        }
        System.out.println(cards);
    }

    public void totalOpen(){
        cards.add(firstCard);
        firstCard = "";
        open();
    }
}
