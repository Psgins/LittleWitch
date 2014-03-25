package sut.game01.core.screen;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Pointer;
import sut.game01.core.ModelShow.MiniSpiritModel;
import sut.game01.core.Pet.MiniSpirit;
import sut.game01.core.all_etc.GameContent;
import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.all_etc.SoundStore;
import sut.game01.core.character.MiniGhost;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

import java.util.List;

import static playn.core.PlayN.*;

/**
 * Created by PSG on 3/2/14.
 */
public class PetScreen extends UIScreen {

    final ScreenStack ss;

    private MiniSpiritModel pet;
    private MiniSpiritModel mChoice;
    private MiniSpiritModel cChoice;

    private GroupLayer petPosition = graphics().createGroupLayer();

    private GameContent gContent;
    private int runeSelected = -1;
    private List<Integer> runeList;

    private GroupLayer[][] runeListPosition = new GroupLayer[3][6];
    private GroupLayer runeSelectedPosition = graphics().createGroupLayer();

    public PetScreen(ScreenStack ss,GameContent gContent)
    {
        this.ss = ss;
        this.gContent = gContent;
        this.runeSelected = gContent.getRune();
        this.runeList = gContent.getRuneList();

        for(int i=0;i<3;i++)
        {
            for(int j=0;j<6;j++)
            {
                runeListPosition[i][j] = graphics().createGroupLayer();
            }
        }
    }

    @Override
    public void wasAdded() {
        ImageLayer bgLayer = graphics().createImageLayer(assets().getImage("images/PetScreen/petScreen.png"));
        layer.add(bgLayer);

        ImageLayer backLayer = graphics().createImageLayer(assets().getImage("images/MapScreen/back.png"));
        backLayer.setTranslation(500,15);
        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                gContent.setRune(runeSelected);
                gContent.save();
                ss.remove(PetScreen.this);
            }
        });
        layer.add(backLayer);

        petPosition.setTranslation(200,260f);
        layer.add(petPosition);

        pet = new MiniSpiritModel(petPosition,0,0,gContent.getPetID());
        pet.layer().setScale(1.5f);

        runeSelectedPosition.setTranslation(432,81);
        layer.add(runeSelectedPosition);

        if(runeSelected > -1)
        {
            ImageLayer runeIcon = graphics().createImageLayer(ImageStore.RuneIcon[runeSelected]);
            runeIcon.addListener(new Pointer.Adapter(){
                @Override
                public void onPointerEnd(Pointer.Event event) {
                    if(runeSelected > -1)
                    {
                        runeList.add(runeSelected);
                        runeSelected = -1;
                    }
                }
            });
            runeSelectedPosition.add(runeIcon);
        }

        int width = 310;
        int hieght = 164;

        for(int i=0;i<3;i++)
        {
            for(int j=0;j<6;j++)
            {
                runeListPosition[i][j].setTranslation(width + (j * 50), hieght + (i * 50));
                layer.add(runeListPosition[i][j]);
            }
        }

        GroupLayer miniSpiritChoice = graphics().createGroupLayer();
        mChoice = new MiniSpiritModel(miniSpiritChoice,0,0,0);
        miniSpiritChoice.setTranslation(100,400);
        miniSpiritChoice.setScale(0.8f);
        layer.add(miniSpiritChoice);
        mChoice.layer().addListener(new Pointer.Adapter()
        {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                petPosition.removeAll();
                pet = new MiniSpiritModel(petPosition,0,0,0);
                pet.layer().setScale(1.5f);
                gContent.setPetID(0);
            }
        });

        GroupLayer cloudChoice = graphics().createGroupLayer();
        cChoice = new MiniSpiritModel(cloudChoice,0,0,1);
        cloudChoice.setTranslation(200,400);
        cloudChoice.setScale(0.8f);
        layer.add(cloudChoice);
        cChoice.layer().addListener(new Pointer.Adapter()
        {
            @Override
                public void onPointerEnd(Pointer.Event event) {
                petPosition.removeAll();
                pet = new MiniSpiritModel(petPosition,0,0,1);
                pet.layer().setScale(1.5f);
                gContent.setPetID(1);
            }
        });
    }

    @Override
    public void wasShown() {
        if(SoundStore.stage1Music.isPlaying())
            SoundStore.stage1Music.stop();

        if(!SoundStore.UIScreen.isPlaying())
            SoundStore.UIScreen.play();
    }

    @Override
    public void update(int delta) {
        pet.update(delta);
        mChoice.update(delta);
        cChoice.update(delta);
        updateSelectedRune();
        updateRuneList();
    }

    public void updateSelectedRune()
    {
        runeSelectedPosition.removeAll();
        if(runeSelected < 0) return;
        ImageLayer runeIcon = graphics().createImageLayer(ImageStore.RuneIcon[runeSelected]);
        runeIcon.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                if(runeSelected > -1)
                {
                    runeList.add(runeSelected);
                    runeSelected = -1;
                }
            }
        });
        runeSelectedPosition.add(runeIcon);
    }

    public void updateRuneList()
    {
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<6;j++)
            {
                runeListPosition[i][j].removeAll();

                final int index = (i*6) + j;

                if(index > runeList.size() -1) return;

                int runeID = runeList.get(index);

                if(runeID < 0)
                {
                    continue;
                }
                else
                {
                    ImageLayer runeItem = graphics().createImageLayer(ImageStore.RuneIcon[runeID]);
                    runeItem.addListener(new Pointer.Adapter(){
                        @Override
                        public void onPointerEnd(Pointer.Event event) {
                            if(runeSelected < 0)
                            {
                                runeSelected = runeList.get(index);
                                runeList.remove(index);
                            }
                        }
                    });

                    runeListPosition[i][j].add(runeItem);
                }
            }
        }
    }
}
