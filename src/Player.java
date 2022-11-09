import java.util.*;

public class Player extends Role{
    private List<Card> cards;
    private int count;
    private int money;
    private int betMoney = 0;
    private boolean insured = false;
    private boolean doubleDown;
    private boolean EvenMoney;
    private boolean firstTurnBJ;
    private boolean blackJack;

    Player(){
        money = 1000;
        cards = new ArrayList<>();
    }

    public int getCount() { return count; }

    public void setCount(int count) { this.count = count; }

    public int getMoney() { return money; }

    public int getBetMoney() { return betMoney; }

    public boolean isInsured() { return insured; }

    public void setInsured(boolean insured) { this.insured = insured; }

    public static boolean isDoubleDown() { return doubleDown; }

    public static void setDoubleDown(boolean doubleDown) {
        Player.doubleDown = doubleDown;
    }

    public boolean isEvenMoney() {
        return EvenMoney;
    }

    public void setEvenMoney(boolean EvenMoney) {
        this.EvenMoney = EvenMoney;
    }

    public boolean isFirstTurnBJ() {
        return firstTurnBJ;
    }

    public void setFirstTurnBJ(boolean firstTurnBJ) {
        this.firstTurnBJ = firstTurnBJ;
    }

    public boolean isBlackJack() {
        return blackJack;
    }

    public void setBlackJack(boolean blackJack) {
        this.blackJack = blackJack;
    }

    @Override
    public void hit(Card card){
        cards.add(card);
    }

    @Override
    public void open() {
        System.out.println("Player : " + cards);
    }

    @Override
    public List<Card> getReceivedCards(){ return cards; }

    @Override
    void initValues() {
        cards = new ArrayList<>();
        betMoney = 0;
        insured = false;
        count = 0;
        doubleDown = false;
        EvenMoney = false;
        firstTurnBJ = false;
        blackJack = false;
    }

    public void insure(){
        if(betMoney/2 > money){
            System.out.println("베팅머니가 부족해 인슈어런스를 지불할 수 없습니다.");
            System.out.println("게임을 진행하겠습니다.");
            return;
        }

        money -= betMoney/2;
        setInsured(true);
        System.out.println("인슈어런스를 지불하였습니다.");
    }

    public void bet(long betMin, long betMax){
        do {
            System.out.println("얼마를 베팅하시겠습니까?(배팅머니는 100단위 입니다. 숫자만 작성하세요.)");
            Scanner scan = new Scanner(System.in);
            int betMoney = scan.nextInt();

            if(money>=betMoney){
                if(betMin<=betMoney && betMax>=betMoney){
                    if(betMoney%100!=0) {
                        System.out.println("베팅머니는 100단위 입니다. 다시 입력해주세요");
                        continue;
                    }
                    money -= betMoney;
                    this.betMoney = betMoney;
                    break;
                } else {
                    System.out.println("최소, 최대 베팅 금액을 다시 한 번 확인해주세요.");
                }
            } else {
                System.out.println("베팅머니가 부족합니다. 다시 작성해주세요.");
            }
        } while(true);
    }

    public void evenMoney(){

    }

    public boolean doubleDown(){
        if(money>=betMoney){
            money -= betMoney;
            betMoney *= 2;
            return true;
        }
        return false;
    }

    public void receiveWinning(int winning){
        money += winning;
    }

    public void initBetMoney(){
        betMoney = 0;
    }
}
