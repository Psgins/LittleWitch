package sut.game01.core.Skill;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;

import static playn.core.PlayN.graphics;

/**
 * Created by PSG on 3/17/14.
 */
public class Screeming_eff {
    private boolean alive = true;
    private int e = 0;
    private ImageLayer L;
    private GroupLayer layer;

    private float scale = 1f;

    public Screeming_eff(Image img, GroupLayer layer, float x, float y)
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
            this.L.setScale(scale /= 1.2);
        }

        if (e >= 300)
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
