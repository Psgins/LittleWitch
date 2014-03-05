package sut.game01.core.all_etc;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;

import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.*;

/**
 * Created by PSG on 3/5/14.
 */
public class FloatLabel {

    private Image[] num = new Image[10];
    private List<GroupLayer> collection = new ArrayList<GroupLayer>();
    private List<GroupLayer> trash = new ArrayList<GroupLayer>();

    private GroupLayer layer;

    public FloatLabel(GroupLayer layer)
    {
        this.layer = layer;

        for(int i =0;i<num.length; i++)
        {
            num[i] = assets().getImage("images/Number/"+i+".png");
        }
    }

    public void update(int delta)
    {
        for(GroupLayer x : collection)
        {
            if(x.alpha() > 0)
            {
                x.setAlpha(x.alpha() - 0.1f);
                x.setTranslation(x.tx(),x.ty()-5f);
            }
            else
                trash.add(x);
        }

        for(GroupLayer x : trash)
        {
            layer.remove(x);
            collection.remove(x);
        }
        trash.clear();
    }

    public void CreateText(int number,float x, float y)
    {
        String[] txtNumber =  String.valueOf(number).split("");

        GroupLayer LabelSet = graphics().createGroupLayer();

        float space = 0;
        for(int i = txtNumber.length -1;i > 0;i--)
        {
            Image tmp = num[Integer.valueOf(txtNumber[i])];
            ImageLayer numImg = graphics().createImageLayer(tmp);
            numImg.setOrigin(tmp.width()/2f,tmp.width()/2f);
            numImg.setTranslation(x-(space += 10),y);
            LabelSet.add(numImg);
        }

        collection.add(LabelSet);
        layer.add(LabelSet);
    }
}
