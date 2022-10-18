import java.util.Scanner;

public class Player extends Role{
    static long money = 0;
    static long betMoney = 0;
    static boolean isInsured = false;
    boolean isFisrtBlackJack = false;


    private Player(){
        money = 1000;
    }

    public static Player playerEnter(){
        return new Player();
    }

    @Override
    public void open() {
        System.out.print("Player : ");
        System.out.println(cards);
    }

    public static void insure(){
        if(betMoney/2 > money){
            System.out.println("베팅머니가 부족해 인슈어런스를 지불할 수 없습니다.");
            System.out.println("게임을 진행하겠습니다.");
            return;
        }

        money -= betMoney/2;
        System.out.println("인슈어런스를 지불하였습니다.");
    }

    public void bet(long betMin, long betMax){
        do {
            Scanner scan = new Scanner(System.in);
            long betMoney = scan.nextLong();

            if(money>=betMoney){
                if(betMin<=betMoney && betMax>=betMoney){
                    if(betMoney%100!=0) {
                        System.out.println("베팅머니는 100단위 입니다. 다시 입력해주세요");
                        continue;
                    }
                    money -= betMoney;
                    Player.betMoney = betMoney;
                    break;
                } else {
                    System.out.println("최소, 최대 베팅 금액을 다시 한 번 확인해주세요.");
                }

            } else {
                System.out.println("베팅머니가 부족합니다. 다시 작성해주세요.");
            }
        } while(true);
    }

    public boolean doubleDown(){
        if(money>=betMoney){
            money -= betMoney;
            betMoney *= 2;
            return true;
        }
        return false;
    }
}
