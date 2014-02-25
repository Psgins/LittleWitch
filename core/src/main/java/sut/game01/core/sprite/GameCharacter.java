package sut.game01.core.sprite;

import playn.core.util.Clock;

/**
 * Created by PSG on 2/6/14.
 */
public interface GameCharacter {
    abstract public void update(int delta);
    abstract public void paint(Clock clock);
}
