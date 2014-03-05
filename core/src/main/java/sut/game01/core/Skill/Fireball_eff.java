package sut.game01.core.Skill;

import static playn.core.PlayN.*;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;

/**
 * Created by PSG on 2/26/14.
 */
public class Fireball_eff {

    private boolean alive = true;
    private int e = 0;
    private ImageLayer L;
    private GroupLayer layer;

    private float scale = 0.5f;

    public Fireball_eff(Image img,GroupLayer layer,float x,float y)
    {
        this.layer = layer;

        L = graphics().createImageLayer(img);
        L.setOrigin(img.width()/2f,img.height()/2f);
        L.setScale(scale);
        L.setTranslation(x,y);
        layer.add(L);

    }

    public void update(int delta)
    {
        if (!alive) return;
        e += delta;

        if(e > 25)
        {
            this.L.setScale(scale /= 1.5);
        }

        if (e >= 200)
        {
            this.destroy();
        }
    }

    public boolean Alive()
    {
        return alive;
    }

    public void destroy()
    {
        alive = false;
        this.layer.remove(L);
    }

}
