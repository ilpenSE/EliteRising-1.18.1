package tr.elite.eliterising.Utilities;

import org.bukkit.Location;

/**
 * This class is copy of TheFloorIsLava.
 * DO NOT CHANGE THIS CLASS!
 */
public class LavaUtils {
    public Location bottomRight;
    public Location topLeft;

    public int currentLevel;
    public int increase;

    public LavaUtils(Location bottomRight, Location topLeft, int currentLevel, int increaseAmount) {
        super();
        this.bottomRight = bottomRight;
        this.topLeft = topLeft;
        this.currentLevel = currentLevel;
        this.increase = increaseAmount;


        this.bottomRight.setY(this.currentLevel);
        this.topLeft.setY(this.currentLevel + increaseAmount - 1);
    }

    public void IncreaseCurrentLevel() {
        this.currentLevel += this.increase;

        this.bottomRight.setY(this.currentLevel);
        this.topLeft.setY(this.currentLevel + this.increase - 1);
    }
}
