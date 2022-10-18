import java.util.*;

public class Table {
    static long betMin = 0;
    static long betMax = 0;
    static Deck deck = null;
    static Player player = null;
    static Dealer dealer = null;
    static boolean continueGame = true;
    static boolean didPlayerWinInLastGame = false; //플레이어 전판 승리 - true, 패배 - false / betMoney 설정에 필요

    public static void main(String[] args){
        while (startNewGame()) {
            secondLoop:
            do {
                if (!setBetMoney()) continue;

                System.out.println("얼마를 베팅하시겠습니까?(배팅머니는 100단위 입니다. 숫자만 작성하세요.)");
                player.bet(betMin, betMax);

                first2Hit();
                open();

                if (dealer.cards.get(0).contains("A")) {
                    System.out.println("A 입니다. 인셔런스를 지불하시겠습니까? 네:1, 아니요:2");
                    if (isYes(new Scanner(System.in))) {
                        Player.insure();
                    }
                    System.out.println("게임을 진행하겠습니다.");
                }

                if (isBlackJack(player)) {
                    System.out.println("이븐머니를 하시겠습니까? 네:1, 아니요:2");
                    if (isYes(new Scanner(System.in))) {
                        dealerCardOpen(true, true);
                    } else {
                        playerWin(true, true, false);
                    }
                    continue;
                }

                System.out.println("더블다운을 하시겠습니까? 네:1, 아니오:2");
                if (isYes(new Scanner(System.in))) {
                    if (player.doubleDown()) {
                        System.out.println("배팅금액을 2배인" + Player.betMoney + "로 늘렸습니다. 게임을 계속 진행합니다.");
                        hit(player);
                        open();
                        if (player.isBusted()) {
                            playerLose(false);
                            continue;
                        }
                        dealerCardOpen(false, false);
                        continue;
                    } else {
                        System.out.println("게임머니가 부족하여 더블다운을 할 수 없습니다.");
                        System.out.println("게임을 진행하겠습니다.");
                    }
                }

                do {
                    System.out.println("한 장 더 받으시겠습니까? 네:1, 아니오:2");
                    if (isYes(new Scanner(System.in))) {
                        hit(player);
                        open();
                        if (player.isBusted()) {
                            playerLose(false);
                            continue secondLoop;
                        }
                        if (isBlackJack(player)) break;
                        continue;
                    }
                    break;
                } while (true);

                dealerCardOpen(false, false);
            } while (continueGame);
        }
    }

    public static boolean startNewGame(){
        System.out.println("게임을 새로 시작하시겠습니까? 네:1, 아니오:2");
        if (new Scanner(System.in).nextLine().equals("2")) {
            System.out.println("게임에서 퇴장합니다.");
            return false;
        }

        if(player==null||Player.money==0){
            player = Player.playerEnter();
            dealer = Dealer.dealerEnter();
        }

        deck = Deck.getInstance();
        deck.shuffle();
        return true;
    }

    private static void continueGame() {
        if(dontHaveMoney()) return;

        System.out.println("남은 돈 : " + Player.money);
        System.out.println("게임을 계속 하시겠습니까? 네:1, 아니오:2");
        Scanner scan = new Scanner(System.in);
        if(scan.nextLine().trim().equals("2")) {
            continueGame = false;
            return;
        }

        player.cards.clear();
        dealer.cards.clear();
        Player.isInsured = false;
        player.isFisrtBlackJack = false;
    }

    public static void open(){
        player.open();
        dealer.open();
    }

    private static boolean isYes(Scanner scan) {
        return scan.nextLine().equals("1");
    }

    public static boolean setBetMoney(){
        if(dontHaveMoney()) return false;

        if(betMin == 0){
            betMin = 100;
            betMax = 200;
        } else if(didPlayerWinInLastGame) {
            betMin = Player.betMoney;
            betMax = Player.betMoney * 2;
        }

        System.out.println("수중의 돈 : " + Player.money);
        System.out.println("최소 베팅머니는 " + betMin + ", 최대 베팅머니는 " + betMax + "입니다.");
        return true;
    }

    private static boolean dontHaveMoney(){
        if(Player.money==0 || Player.money < betMin){
            System.out.println("판돈을 다 잃으셨군요. 다음에 뵙겠습니다. 아디오스");
            continueGame = false;
            return true;
        }
        return false;
    }

    //상금계산
    private static boolean isBlackJack(Role role) {
        role.count();
        return role.cnt == 21;
    }

    private static void push() {
        Player.money += Player.betMoney;
    }

    private static void takeInsurance() {
        long insurance = Player.betMoney/2;
        Player.money += insurance;
    }

    private static void returnPrincipal(){
        Player.money += Player.betMoney;
    }

    private static void returnDoubleNHalf() {
        Player.money += (Player.betMoney * 2.5);
    }

    private static void returnDouble() {
        Player.money += (Player.betMoney * 2);
    }

    private static void returnDoubleInsure() {
        long insurance = Player.betMoney/2;
        Player.money += Player.betMoney * 2 - insurance;

    }

    //hit
    private static void first2Hit() {
        hit(player);
        hit(player);
        Dealer.firstCard = deck.hit();
        hit(dealer);
    }

    private static void hit(Role role) {
        role.cards.add(deck.hit());
    }

    //승패//
    private static void playerWin(boolean isBJ, boolean isFirstBJ, boolean didEvenMoney) {
        System.out.println("Winner winner chicken dinner!");
        didPlayerWinInLastGame = true;
        if(!isBJ){
            if(Player.isInsured){
                returnDoubleInsure();
            } else {
                returnDouble();
            }
        } else {
            if(!isFirstBJ){
                if(Player.isInsured){
                    returnDouble();
                } else {
                    returnDoubleNHalf();
                }
            } else {
                if(didEvenMoney){
                    if(Player.isInsured){
                        returnDouble();
                    } else {
                        returnDoubleNHalf();
                    }
                } else {
                    if(Player.isInsured){
                        returnDoubleInsure();
                    } else {
                        returnDouble();
                    }
                }
            }
        }
        continueGame();
    }

    private static void playerLose(boolean isDealerBJ) {
        System.out.println("loooooooooose!!");
        didPlayerWinInLastGame = false;
        if(isDealerBJ && Player.isInsured) {
            System.out.println("보험덕에 원금은 건졌습니다.");
            returnPrincipal();
        }
        continueGame();
    }

    private static void tie() {
        System.out.println("비겼습니다.");
        didPlayerWinInLastGame = false;
        if(Player.isInsured){
            takeInsurance();
        } else {
            push();
        }
        continueGame();
    }

    private static boolean isTie() {
        return player.cnt == dealer.cnt;
    }

    private static boolean isPlayerWin() {
        player.count();
        return player.cnt > dealer.cnt;
    }

    private static void isUnder17() {
        dealer.count();
        while(dealer.cnt<17){
            hit(dealer);
            dealer.open();
            dealer.count();
        }
    }

    private static void dealerCardOpen(boolean isFirstBJ, boolean didEvenMoney) {
        dealer.totalOpen();

        isUnder17();

        if(dealer.isBusted()){
            playerWin(isBlackJack(player), isFirstBJ, didEvenMoney);
            return;
        }

        if(isBlackJack(dealer)){
            if(isBlackJack(player)){
                tie();
                return;
            }
            playerLose(true);
            return;
        }

        if(isPlayerWin()){
            playerWin(isBlackJack(player), isFirstBJ, didEvenMoney);
            return;
        }

        if(isTie()){
            tie();
            return;
        }

        playerLose(false);
    }
}

