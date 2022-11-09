import java.util.Scanner;

public class Game {
    private static final boolean FINISH_TURN = false;
    private static final boolean FINISH_ROUND = true;
    private static final boolean FINISH_GAME = true;
    private static final int YES = 1;
    private static final int NO = 2;

    public void play(){
        System.out.println("게임을 시작합니다.");
        GameAssistant ga = new GameAssistant();
        Deck deck = new Deck();
        Player player = new Player();
        Dealer dealer = new Dealer();
        Scanner scan = new Scanner(System.in);

        do {
            if(ga.initSet(ga, deck, player, dealer)==FINISH_GAME){ break; }
            if(playerTurn(ga, deck, player, dealer,  scan)==FINISH_ROUND){ continue; }
            dealerTurn(ga, deck, dealer, player);
        } while(anotherGame(player, scan));
    }

    private boolean playerTurn(GameAssistant ga, Deck deck, Player player, Dealer dealer, Scanner scan) {

        if(ga.isDealerCardA(dealer)){
            System.out.println("딜러의 카드가 A 입니다. 인셔런스를 지불하시겠습니까? 네:1, 아니요:2");
            if(isYES(scan)){
                player.insure();
            }
        }

        if(ga.isBlackJack(ga.count(player.getReceivedCards()))) {
            player.setFirstTurnBJ(true);

            System.out.println("이븐머니를 하시겠습니까? 네:1, 아니요:2");
            if(isYES(scan)){
                player.setEvenMoney(true);
                return FINISH_TURN;
            }

            ga.playerWin(player);
            return FINISH_ROUND;
        }

        if(ga.canDoubleDown(player)){
            System.out.println("더블다운을 하시겠습니까? 네:1, 아니요:2");

            if(isYES(scan)){
                player.doubleDown();
                player.hit(deck.giveOneCard());
                player.open();
                int count = ga.count(player.getReceivedCards());

                if(ga.isBusted(count)){
                    ga.playerLoose(player);
                    return FINISH_ROUND;
                }

                return FINISH_TURN;
            }
        }

        do {
            System.out.println("한 장 더 받으시겠습니까? 네:1, 아니요:2");
            if(isYES(scan)){
                player.hit(deck.giveOneCard());
                player.open();
                int count = ga.count(player.getReceivedCards());

                if(ga.isBusted(count)){
                    ga.playerLoose(player);
                    return FINISH_ROUND;
                }

                if(ga.isBlackJack(count)){
                    player.setBlackJack(true);
                    return FINISH_TURN;
                }
            } else {
                return FINISH_TURN;
            }
        } while (true);
    }

    private void dealerTurn(GameAssistant ga, Deck deck, Dealer dealer, Player player) {
        dealer.totalCardOpen();
        dealer.open();

        if(ga.repeatHitTo17(dealer, deck)){
            dealer.open();
        }

        if(ga.isBusted(dealer.getCount())){
            ga.playerWin(player);
            return;
        }

        int playerCount = ga.count(player.getReceivedCards());
        player.setCount(playerCount);

        ga.score(player, dealer.getCount());
    }

    private boolean isYES(Scanner scan) {
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

    private boolean anotherGame(Player player, Scanner scan){
        System.out.println("현재 가진 돈은 " + player.getMoney() + "입니다.");
        System.out.println("게임을 계속 진행하시겠습니까?  네:1, 아니요:2");

        if(isYES(scan)){
            System.out.println("계속해서 게임을 진행합니다.");
            return true;
        }
        System.out.println("게임을 종료합니다.");
        return false;
    }
}
