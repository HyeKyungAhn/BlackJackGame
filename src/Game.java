import java.util.Scanner;

public class Game {
    private Deck deck;
    private Player player;
    private Dealer dealer;
    private GameAssistant ga;
    private static final boolean FINISH_TURN = false;
    private static final boolean FINISH_ROUND = true;
    private static final int YES = 1;
    private static final int NO = 2;

    public void play(){
        System.out.println("게임을 시작합니다.");
        initGame();

        do {
            if(ga.initSet(ga, deck, player, dealer)==FINISH_ROUND){ break; }
            if(playerTurn(ga, player, dealer)==FINISH_ROUND){ continue; }
            dealerTurn(ga, deck, dealer, player);
        } while(anotherGame());
    }

    private boolean anotherGame(){
        System.out.println("게임을 계속 진행하시겠습니까?  네:1, 아니요:2");

        if(isYES()){
            System.out.println("계속해서 게임을 진행합니다.");
            return true;
        }
        System.out.println("게임을 종료합니다.");
        return false;
    }

    private void dealerTurn(GameAssistant ga, Deck deck, Dealer dealer, Player player) {
        dealer.totalOpen();

        do{
            int count = ga.count(dealer.getReceivedCards());
            if(dealer.isUnder17(count)){
                Card card = deck.giveOneCard();
                dealer.hit(card);
                continue;
            }

            Dealer.setCount(count);
            dealer.open(); //17이 넘을 때 두번 open됨
            break;
        } while(true);

        if(ga.isBusted(Dealer.getCount())){
            ga.playerWin(player);
            return;
        }

        ga.score(player, Dealer.getCount());
    }

    private boolean playerTurn(GameAssistant ga, Player player, Dealer dealer) {

        if(ga.isDealerCardA(dealer)){
            System.out.println("딜러의 카드가 A 입니다. 인셔런스를 지불하시겠습니까? 네:1, 아니요:2");
            if(isYES()){
                player.insure();
            }
        }

        //isBj()안에 count()를 내장? 그럴 필요 없을 듯? count를 저장해서 계속 사용하는 경우도 있음 그러나.. 일단 내장해서 만들자
        if(ga.isBlackJack(ga.count(player.getReceivedCards()))) {
            Player.setFirstTurnBJ(true);

            System.out.println("이븐머니를 하시겠습니까? 네:1, 아니요:2");
            if(isYES()){
                Player.setTakeEvenMoney(true);
                return FINISH_TURN;
            }

            ga.playerWin(player);
            return FINISH_ROUND;
        }

        System.out.println("더블다운을 하시겠습니까? 네:1, 아니요:2");
        if(isYES()){
            Card card = deck.giveOneCard();
            player.hit(card);
            player.open();
            int count = ga.count(player.getReceivedCards());

            if(ga.isBusted(count)){
                ga.playerLoose(player);
                return FINISH_ROUND;
            }

            Player.setCount(count); //필요할까? oo
            Player.setDoubleDown(true);
            return FINISH_TURN;
        }

        do {
            System.out.println("한 장 더 받으시겠습니까? 네:1, 아니요:2");
            if(isYES()){
                Card card = deck.giveOneCard();
                player.hit(card);
                player.open();

                int playerCount = ga.count(player.getReceivedCards());
                if(ga.isBusted(playerCount)){
                    ga.playerLoose(player);
                    return FINISH_ROUND;
                }

                if(ga.isBlackJack(playerCount)){
                    Player.setBlackJack(true);
                    return FINISH_TURN;
                }
            } else {
                return FINISH_TURN;
            }
        } while (true);
    }

    private boolean isYES() {
        Scanner scan = new Scanner(System.in);
        while(true){
            int answer = scan.nextInt();
            if(answer==YES){
                return true;
            } else if(answer==NO){
                return false;
            } else {
                System.out.println("답을 확인하시고 다시 입력해주세요. (숫자만 다시 입력해주세요 ex. 네:1, 아니요:2)");
            }
        }
    }

    private void initGame() {
        ga = GameAssistant.getGameAssistant();
        deck = Deck.getDeck();
        player = Player.playerEnter();
        dealer = Dealer.dealerEnter();
    }
}
