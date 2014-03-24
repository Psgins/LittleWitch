package sut.game01.core.screen;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import sut.game01.core.ModelShow.MiniSpiritModel;
import sut.game01.core.ModelShow.WitchModel;
import sut.game01.core.Skill.SkillCard;
import sut.game01.core.all_etc.Countdown;
import sut.game01.core.all_etc.GameContent;
import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.all_etc.VariableConstant;
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

    // pet
    private MiniSpiritModel pet;
    private GroupLayer petPosition = graphics().createGroupLayer();

    //char
    private WitchModel model;
    private GroupLayer charPosition = graphics().createGroupLayer();

    private GameContent gContent = new GameContent();

    private int[] SkillSelected;
    private GroupLayer[] positionSkillSlot = new GroupLayer[4];

    private List<Integer> itemList;
    private GroupLayer[][] positionItemSlot = new GroupLayer[3][8];

    private int RuneSelected = 0;
    private List<Integer> runeList;
    private GroupLayer positionRune  = PlayN.graphics().createGroupLayer();

    //Status
    GroupLayer Information = graphics().createGroupLayer();

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
                gContent.setSkill(SkillSelected);
                gContent.save();
                ss.remove(CharacterScreen.this);
            }
        });
        layer.add(backLayer);

        // Game Context loader
        gContent.load();
        this.SkillSelected = gContent.getSkill();
        this.itemList = gContent.getItem();
        this.RuneSelected = gContent.getRune();

        // Add model for show
        charPosition.setTranslation(125,150);
        layer.add(charPosition);

        // Add pet Model
        petPosition.setTranslation(540,180);
        layer.add(petPosition);

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

        // start button
        ImageLayer startLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/MapButton.png"));
        startLayer.setTranslation(640f - 220f,480f - 170f);

        startLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                gContent.setSkill(SkillSelected);
                gContent.save();
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
                    gContent.setSkill(SkillSelected);
                    gContent.save();
                    ss.push(new PetScreen(ss,gContent));
                }
            });
            positionRune.add(runeIcon);
        }

        gContent.Refresh();

        Information.removeAll();
        Information.setTranslation(200,85);
        ImageLayer info = graphics().createImageLayer(assets().getImage("images/CharacterScreen/information.png"));
        Information.add(info);

        float atk = 20 + VariableConstant.dmgLVL[gContent.getLevel()-1];
        GroupLayer atknum = Countdown.Create((int)atk);
        atknum.setTranslation(80,13);
        Information.add(atknum);

        float def = 0 + VariableConstant.defLVL[gContent.getLevel()-1];
        GroupLayer defnum = Countdown.Create((int)def);
        defnum.setTranslation(80,35);
        Information.add(defnum);

        float hp = 150 + VariableConstant.hpLVL[gContent.getLevel()-1];
        GroupLayer hpnum = Countdown.Create((int)hp);
        hpnum.setTranslation(80,57);
        Information.add(hpnum);

        float exp = gContent.getExp();
        GroupLayer expnum = Countdown.Create((int)exp);
        expnum.setTranslation(80, 79);
        Information.add(expnum);

        float lvl = gContent.getLevel();
        GroupLayer lvlnum = Countdown.Create((int)lvl);
        lvlnum.setTranslation(80,101);
        Information.add(lvlnum);
        layer.add(Information);

        // Pet Refresh
        petPosition.removeAll();
        pet = new MiniSpiritModel(petPosition,0,0,gContent.getPetID());
        pet.layer().addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                gContent.setSkill(SkillSelected);
                gContent.save();
                ss.push(new PetScreen(ss,gContent));
            }
        });

        //Character Refresh
        charPosition.removeAll();
        model = new WitchModel(charPosition,0,0,gContent.getCharID());
        model.layer().addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.push(new CharacterSelect(ss,gContent));
            }
        });

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
