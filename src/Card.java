public class Card {
    private final String suitSymbol; //king, queen, spade, heart
    private final String pipOrCourt; //A,2,3,4,...,J,Q,K

    public Card(String suitSymbol, String pipOrCourt){
        this.suitSymbol = suitSymbol;
        this.pipOrCourt = pipOrCourt;
    }

    public String getPipOrCourt() {
        return pipOrCourt;
    }

    @Override
    public String toString() {
        return suitSymbol + pipOrCourt;
    }
}
