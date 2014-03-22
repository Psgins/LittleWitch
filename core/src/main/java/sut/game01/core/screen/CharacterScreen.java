package sut.game01.core.screen;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import sut.game01.core.ModelShow.MiniSpiritModel;
import sut.game01.core.ModelShow.WitchModel;
import sut.game01.core.Skill.SkillCard;
import sut.game01.core.all_etc.GameContent;
import sut.game01.core.all_etc.ImageStore;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static playn.core.PlayN.*;

/**
 * Created by PSG on 3/2/14.
 */
public class CharacterScreen extends UIScreen {

    private final ScreenStack ss;
    private WitchModel model;
    private MiniSpiritModel pet;

    GameContent gContent = new GameContent();

    private int[] SkillSelected = new int[]{-1,-1,-1,-1};
    private GroupLayer[] positionSkillSlot = new GroupLayer[4];

    private List<Integer> itemList;
    private GroupLayer[][] positionItemSlot = new GroupLayer[3][8];

    private int RuneSelected = 0;
    private List<Integer> runeList;
    private GroupLayer positionRune  = PlayN.graphics().createGroupLayer();

    public CharacterScreen(ScreenStack ss)
    {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        ImageLayer bgLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/CharacterScreen.png"));
        layer.add(bgLayer);

        ImageLayer backLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/BackButton.png"));
        backLayer.setTranslation(640f - 220f,480f - 100f);
        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.remove(CharacterScreen.this);
            }
        });
        layer.add(backLayer);

        ImageLayer upLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/Up.png"));
        upLayer.setTranslation(640f - 275f,480f - 170f);
        layer.add(upLayer);

        ImageLayer downLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/Down.png"));
        downLayer.setTranslation(640f - 275f,480f - 60f);
        layer.add(downLayer);

        // Add model for show
        model = new WitchModel(layer,125f,150f);

        // Add pet Model
        pet = new MiniSpiritModel(layer,540f,180f);
        pet.layer().addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.push(new PetScreen(ss,gContent));
            }
        });

        // Skill Selected
        for(int i = 0;i< 4;i++)
        {
            positionSkillSlot[i] = graphics().createGroupLayer();
            layer.add(positionSkillSlot[i]);
        }

        positionSkillSlot[0].setTranslation(59,244);
        positionSkillSlot[1].setTranslation(146, 244);
        positionSkillSlot[2].setTranslation(228, 244);
        positionSkillSlot[3].setTranslation(307, 244);

        int base_height = 311;
        int base_width = 58;

        for(int i=0;i < 3 ;i++)
        {
            for(int j=0; j<6; j++)
            {
                positionItemSlot[i][j] = graphics().createGroupLayer();
                positionItemSlot[i][j].setTranslation(base_width + (j*50),base_height + (i*50));
                layer.add(positionItemSlot[i][j]);
            }
        }

        positionRune.setTranslation(488,245);
        layer.add(positionRune);

        gContent.load();

        this.itemList = gContent.getItem();
        this.RuneSelected = gContent.getRune();

        // start button
        ImageLayer startLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/MapButton.png"));
        startLayer.setTranslation(640f - 220f,480f - 170f);

        startLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                gContent.setSkill(SkillSelected);
                ss.push(new MapScreen(ss,gContent));
            }
        });
        layer.add(startLayer);
    }

    @Override
    public void wasShown() {
        positionRune.removeAll();

        if(gContent.getRune() > -1)
        {
            ImageLayer runeIcon = graphics().createImageLayer(ImageStore.RuneIcon[gContent.getRune()]);
            runeIcon.addListener(new Pointer.Adapter(){
                @Override
                public void onPointerEnd(Pointer.Event event) {
                    ss.push(new PetScreen(ss,gContent));
                }
            });
            positionRune.add(runeIcon);
        }

        gContent.Refresh();
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        model.update(delta);
        pet.update(delta);

        updateSkillSlot();
        updateItemSlot();
    }

    public void updateSkillSlot()
    {
        for(int i =0;i<4;i++)
        {
            positionSkillSlot[i].removeAll();

            if(SkillSelected[i]< 0) continue;

            final int slotid = i;
            ImageLayer sIcon = graphics().createImageLayer(ImageStore.itemIcon[SkillSelected[i]]);
            sIcon.addListener(new Pointer.Adapter(){
                @Override
                public void onPointerEnd(Pointer.Event event) {
                    itemList.add(SkillSelected[slotid]);
                    SkillSelected[slotid] = -1;
                }
            });
            sIcon.setSize(50,50);
            positionSkillSlot[i].add(sIcon);
        }
    }

    public void updateItemSlot()
    {
        for(int i=0;i < 3;i++)
        {
            for(int j=0;j< 6;j++)
            {
                positionItemSlot[i][j].removeAll();
                final int index = (i*6) + j;

                if (index > itemList.size()-1) return;

                int itemID = itemList.get(index);

                if (itemID < 0)
                {
                    continue;
                }
                else
                {
                    ImageLayer itemIcon = graphics().createImageLayer(ImageStore.itemIcon[itemID]);
                    itemIcon.setSize(50,50);
                    itemIcon.addListener(new Pointer.Adapter(){
                        @Override
                        public void onPointerEnd(Pointer.Event event) {
                            if(selectItem(itemList.get(index)))
                            {
                                itemList.remove(index);
                            }
                        }
                    });
                    positionItemSlot[i][j].add(itemIcon);
                }
            }
        }
    }

    public boolean selectItem(int itemID)
    {
        for(int i=0;i < 4 ;i++)
        {
            if (SkillSelected[i] < 0)
            {
                SkillSelected[i] = itemID;
                return true;
            }
        }
        return false;
    }
}
