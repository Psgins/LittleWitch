package sut.game01.core.all_etc;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import sun.security.krb5.internal.crypto.crc32;
import sut.game01.core.screen.Stage1;

import static playn.core.PlayN.*;

import java.util.List;

/**
 * Created by PSG on 3/17/14.
 */
public class Countdown {

    private GroupLayer layer;

    private GroupLayer timer = graphics().createGroupLayer();
    private GroupLayer minute = graphics().createGroupLayer();
    private GroupLayer secound = graphics().createGroupLayer();

    private int min,sec;
    private int e = 0;

    private boolean timeout = false;

    public Countdown(GroupLayer layer,int min,int sec)
    {
        this.min = min;
        this.sec = sec;
        this.layer = layer;

        minute.setTranslation(10,0);
        secound.setTranslation(45,0);

        ImageLayer clock = graphics().createImageLayer(assets().getImage("images/ui/clock.png"));
        clock.setOrigin(55/2,35/2);
        timer.add(clock);

        ImageLayer colon = graphics().createImageLayer(ImageStore.colon);
        colon.setTranslation(35,-7);
        timer.add(colon);

        minute.add(Create(min));
        secound.add(Create(sec));

        timer.add(minute);
        timer.add(secound);
        timer.setTranslation(400,37);

        layer.add(timer);
    }

    public void update(int delta)
    {
        if(timeout) return;

        e += delta;

        if(e >= 1000)
        {
            sec--;
            if(sec < 0){
                sec = 59;
                min--;

                if(min < 0)
                {
                    timeout = true;
                    return;
                }

                UpdateMin();
            }
            UpdateSec();
            e = 0;
        }
    }

    public void UpdateSec()
    {
        secound.removeAll();
        secound.add(Create(sec));
    }

    public void UpdateMin()
    {
        minute.removeAll();
        minute.add(Create(min));
    }

    public static GroupLayer Create(int time)
    {
        String[] txtTime = String.valueOf(time).split("");

        GroupLayer tmpGroup = graphics().createGroupLayer();

        float space = 0;
        for(int i = 1;i < txtTime.length;i++)
        {
            Image tmp = ImageStore.number[Integer.valueOf(txtTime[i])];
            ImageLayer numImg = graphics().createImageLayer(tmp);
            numImg.setOrigin(tmp.width()/2f,tmp.width()/2f);
            numImg.setTranslation((space += 10),0);
            tmpGroup.add(numImg);
        }

        return  tmpGroup;
    }

    public boolean isTimeout()
    {
        return timeout;
    }
}
