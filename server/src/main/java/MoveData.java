import pl.edu.platinum.archiet.jchess3man.engine.Desc;
import pl.edu.platinum.archiet.jchess3man.engine.FigType;
import pl.edu.platinum.archiet.jchess3man.engine.FromTo;

/**
 * Created by Michał Krzysztof Feiler on 01.04.17.
 */
public class MoveData {
    private Long id;
    private FromTo fromTo;
    private Long beforeGame;
    private Long afterGame;
    private FigType promotion;
    private Long who;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FromTo getFromTo() {
        return fromTo;
    }

    public void setFromTo(FromTo fromTo) {
        this.fromTo = fromTo;
    }

    public Desc getFromToProm() {
        if (fromTo == null) return null;
        return new Desc(fromTo.from, fromTo.to, promotion);
    }

    public Long getBeforeGame() {
        return beforeGame;
    }

    public void setBeforeGame(Long beforeGame) {
        this.beforeGame = beforeGame;
    }

    public Long getAfterGame() {
        return afterGame;
    }

    public void setAfterGame(Long afterGame) {
        this.afterGame = afterGame;
    }

    public FigType getFigPromotion() {
        return promotion;
    }

    public int getPromotion() {
        return promotion.toInt();
    }

    public void setFigPromotion(FigType promotion) {
        this.promotion = promotion;
    }

    public void setPromotion(int promotion) {
        this.promotion = FigType.fromIndex(promotion);
    }

    public Long getWho() {
        return who;
    }

    public void setWho(Long who) {
        this.who = who;
    }
}
