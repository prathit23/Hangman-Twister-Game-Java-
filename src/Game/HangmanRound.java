/**
 * Prathit Pannase: ppannase
 */
package Game;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class HangmanRound extends GameRound{
    // Getter Setter methods for Property variables

    IntegerProperty hitCount = new SimpleIntegerProperty(), missCount= new SimpleIntegerProperty();

    public int getHitCount() {
        return hitCount.get();
    }

    public IntegerProperty hitCountProperty() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount.set(hitCount);
    }

    public int getMissCount() {
        return missCount.get();
    }

    public IntegerProperty missCountProperty() {
        return missCount;
    }

    public void setMissCount(int missCount) {
        this.missCount.set(missCount);
    }
}
