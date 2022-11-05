import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameAssistant {
    private static int betMin;
    private static final int BETMAX = 2100000000;
    private static final boolean SETTING_SUCCESS = true;
    private static final boolean SETTING_FAIL = false;
    private static final boolean FINISH_ROUND = true;
    private static final boolean CONTINUE = false;

    private GameAssistant(){}
    public static GameAssistant getGameAssistant(){ return new GameAssistant(); }

    public void initRoleCard(Deck deck, List<Role> roles) {

        for(Role role : roles){
             for(int i=0; i<roles.size(); i++){
                Card card = deck.giveOneCard();
                role.hit(card);
            }

            role.open();
        }
    }

    public int count(List<Card> cards){
        //객체 생성못해. dealer나 player에 그대로 상속하는데, dealer나 player 둘 다 이 메서드를 사용해야하는데
        // 둘중 한 객체를 골라서 이 메서드를 호출하는게 굉장히 이상한 모양새이다. 아예 다른 곳에 정의하는 것이 맞다. 꼭 여기 있어야할 필요가 없다.
        // dealer, player 의 속성을 사용하지만.. 모르겠당..
        int count = 0;
        int aCount = 0;

        for(Card card : cards){
            char pipOrCourt = card.getPipOrCourt().charAt(0);
            if((int)pipOrCourt >= 65){ //courts(K,Q,J,A)
                if(pipOrCourt=='A'){
                    count += 11;
                    aCount++;
                    continue;
                }
                count += 10;
            } else { //pips(2,...,10)
                if((int)pipOrCourt == 49){ //1일 때 ('1'0)
                    count += 10;
                    continue;
                }
                count += ((int)pipOrCourt - 48);
            }
        }

        if(count>21){
            for(int i=0; i<aCount && count>21; i++){
                count -= 10;
            }
        }

        return count;
    }

    public boolean isBlackJack(int count){ return count==21; }

    public boolean isBusted(int count){
        boolean isBusted = count>21;
        if(isBusted){
            System.out.println("bust! 카드의 합계가 21을 초과하였습니다.");
        }
        return isBusted;
    }

    public boolean isDealerCardA(Dealer dealer){
        List<Card> cards = dealer.getReceivedCards();
        String pipOrCourt = cards.get(1).getPipOrCourt();
        return pipOrCourt.equals("A");
    }

    public boolean setBetting(Player player){
        if(betMin > Player.getMoney()){
            System.out.println("배팅 금액은 전판의 최소 배팅 금액 이상이어야 합니다.");
            System.out.println("배팅 금액 부족으로 게임을 종료합니다.");
            return SETTING_FAIL;
        }

        setTableLimit();
        player.bet(betMin, BETMAX);
        return SETTING_SUCCESS;
    }

    private void setTableLimit() {
        int betMin;
        int money = Player.getMoney();

        if(money > 1000000000){
            betMin = 1000000000;
        } else if(money > 100000000){
            betMin = 100000000;
        } else if(money > 10000000){
            betMin = 10000000;
        } else if(money > 3000000){
            betMin = 3000000;
        } else if(money > 500000){
            betMin = 500000;
        } else if(money > 100000){
            betMin = 100000;
        } else if(money > 10000){
            betMin = 10000;
        } else if(money > 5000){
            betMin = 5000;
        } else if(money > 3000){
            betMin = 3000;
        } else if(money > 1000){
            betMin = 1000;
        } else {
            betMin = 100;
        }

        if(GameAssistant.betMin !=0 && GameAssistant.betMin != betMin){
            System.out.println("배팅 최소 금액이 상향조정 되었습니다.");
        }
        GameAssistant.betMin = betMin;

        System.out.println("수중의 돈 : " + money);
        System.out.println("최소 베팅머니는 " + GameAssistant.betMin + ", 최대 베팅머니는 21억입니다.");
        System.out.println("테이블의 베팅 금액 한도는 21억입니다.");
    }

    public void playerWin(Player player){
        System.out.println("Winner winner chicken dinner!");
        if(!Player.isBlackJack()){
            giveWinnings(player, Player.isInsured(), 2);
        } else {
            if(!Player.isFirstTurnBJ()){
                giveWinnings(player, Player.isInsured(), 2.5);
            } else {
                if(Player.isTakeEvenMoney()){
                    giveWinnings(player, Player.isInsured(), 2.5);
                } else {
                    giveWinnings(player, Player.isInsured(), 2);
                }
            }
        }
    }

    private void giveWinnings(Player player, boolean insured, double rate) {
        System.out.println("player : " + player + ", isInsured : " + insured + ", rate : " + rate);
        if(insured){
            int insurance = Player.getBetMoney()/2;
            player.receiveWinning((int) (Player.getBetMoney() * rate - insurance));
        } else {
            player.receiveWinning((int) (Player.getBetMoney() * rate));
        }
    }

    public void playerLoose(Player player) {
        System.out.println("게임에서 졌습니다. 이번 라운드를 종료합니다.");
        player.initBetMoney();
    }

    public boolean initSet(GameAssistant ga, Deck deck, Player player, Dealer dealer){
        initValues(player, dealer);

        if(ga.setBetting(player)==SETTING_FAIL){
            return FINISH_ROUND;
        }

        initRoleCard(deck, Arrays.asList(player, dealer));

        return CONTINUE;
    }

    private void initValues(Player player, Dealer dealer) {
        //money 제외
        // player : cards, insured, count, dd, takeEve nMoney, FisrtTurnBJ, BlackJack, betMoney
        // dealer : cards, hiddenCard, count
        player.initValues();
        dealer.initValues();
    }

    public void score(Player player, int dealerCount) {
        int playerCount = Player.getCount();
        if(isBlackJack(dealerCount)){
            if(isBlackJack(playerCount)){
                tie(player);
            } else {
                playerLoose(player);
            }
            return;
        }

        if(isPlayerWin(playerCount, dealerCount)){
            playerWin(player);
            return;
        }

        if(isTie(playerCount, dealerCount)){
            tie(player);
        }

        playerLoose(player);
    }

    private boolean isTie(int playerCount, int dealerCount) {
        return playerCount == dealerCount;
    }

    private boolean isPlayerWin(int playerCount, int dealerCount) {
        return playerCount > dealerCount;
    }

    private void tie(Player player) {

    }
}

