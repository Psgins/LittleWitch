package sut.game01.core.ModelShow;

import com.sun.org.apache.xpath.internal.operations.Variable;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.util.Callback;
import sut.game01.core.all_etc.VariableConstant;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

/**
 * Created by PSG on 3/14/14.
 */
public class WitchModel {

    Sprite sprite;
    int spriteIndex = -1;
    boolean hasLoaded = false;
    int e = 0;
    int offset =0;

    public WitchModel(final GroupLayer layer,final float px,final float py,int charID)
    {
        sprite = SpriteLoader.getSprite(VariableConstant.ModelPeth[charID]);
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(150f/2f,168f/2f);
                sprite.layer().setTranslation(px,py);

                layer.add(sprite.layer());

                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });
    }

    public void update(int delta)
    {
        if(!hasLoaded) return;

        e+=delta;

        if(e >= 150)
        {
            spriteIndex = offset + ((spriteIndex+1)%4);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }

    public ImageLayer layer()
    {
        return sprite.layer();
    }

}
