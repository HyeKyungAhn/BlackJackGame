import java.util.*;

public abstract class Role {
    List<String> cards = new ArrayList<>(); //static이 되도 되나?
    int cnt = 0;
    int aCnt = 0;

    public Role(){}

    public abstract void open();

    public void count(){
        cnt = 0;
        aCnt = 0;

        for(String card : cards){
            char lastLetter = card.charAt(card.length()-1);
            if((int)lastLetter >= 65){ //문자일 때
                if(lastLetter=='A'){
                    cnt += 11;
                    aCnt++;
                    continue;
                }
                cnt += 10;
            } else { //숫자일 때
                if((int)lastLetter == 48){ //0일 때
                    cnt += 10;
                    continue;
                }
                cnt += ((int)lastLetter - 48);
            }
        }

        if(cnt>21 && aCnt>0){
            if(aCnt == 1){
                cnt -= 10; // A : 11 -> 1
                return;
            }

            for(int i=0; i<aCnt; i++){
                cnt -= 10;
                if(!(cnt>21)) break;
            }
        }
    }

    public boolean isBusted() {
        count();
        return cnt>21;
    }
}
