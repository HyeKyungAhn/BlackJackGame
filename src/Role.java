import java.util.*;

public abstract class Role {
    abstract void hit(Card card);
    abstract void open();
    abstract List<Card> getReceivedCards();
    abstract void initValues();
}
