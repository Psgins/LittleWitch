package sut.game01.core.all_etc;

import org.jbox2d.dynamics.Body;

/**
 * Created by PSG on 2/6/14.
 */
public interface ObjectDynamic {
    public void update(int delta);
    public void paint();
    public boolean Alive();
    public Body getBody();
    public void contact(ObjectDynamic A , ObjectDynamic B);
}
