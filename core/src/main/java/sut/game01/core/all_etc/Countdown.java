package sut.game01.core.all_etc;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import sut.game01.core.screen.Stage1;

import static playn.core.PlayN.*;

import java.util.List;

/**
 * Created by PSG on 3/17/14.
 */
public class Countdown {

    private GroupLayer layer;

    public Countdown(GroupLayer layer)
    {
        this.layer = layer;
    }

    public static GroupLayer Create(int time)
    {
        String[] txtTime = String.valueOf(time).split("");

        GroupLayer tmpGroup = graphics().createGroupLayer();

        float space = 0;
        for(int i = 1;i < txtTime.length;i++)
        {
            Image tmp = Stage1.imageStore.number[Integer.valueOf(txtTime[i])];
            ImageLayer numImg = graphics().createImageLayer(tmp);
            numImg.setOrigin(tmp.width()/2f,tmp.width()/2f);
            numImg.setTranslation((space += 10),0);
            tmpGroup.add(numImg);
        }

        return  tmpGroup;
    }
}
