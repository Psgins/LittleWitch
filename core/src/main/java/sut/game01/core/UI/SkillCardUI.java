package sut.game01.core.UI;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Pointer;
import sut.game01.core.Rune.Rune;
import sut.game01.core.Skill.Fireball;
import sut.game01.core.Skill.SkillCard;
import static playn.core.PlayN.*;

import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.all_etc.GameEnvirontment;
import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.all_etc.VariableConstant;
import sut.game01.core.character.Character;
import sut.game01.core.character.Witch;
import sut.game01.core.screen.Stage1;

import java.util.List;

/**
 * Created by PSG on 3/17/14.
 */
public class SkillCardUI {

    private Character focus;
    private List<DynamicObject> objTemp;

    private SkillCard[] skill = new SkillCard[4];
    private Rune rune;

    private GroupLayer SkillUI = graphics().createGroupLayer();
    private GroupLayer[] SkillSlot = new GroupLayer[4];
    private GroupLayer[] CooldownSlot = new GroupLayer[4];

    private GameEnvirontment gEnvir;

    public SkillCardUI(Character focus,GroupLayer layer,final List<DynamicObject> objTemp,SkillCard[] skill,final GameEnvirontment gEnvir)
    {
        this.focus = focus;
        this.objTemp = objTemp;
        this.gEnvir = gEnvir;

        for(int i=0;i<4;i++)
        {

            // Create GroupLayer
            SkillSlot[i] = graphics().createGroupLayer();
            CooldownSlot[i] = graphics().createGroupLayer();

            if(skill[i]==null)
            {
                SkillSlot[i].add(graphics().createImageLayer(ImageStore.NullSkill));
            }
            else
            {
                // set up skill variable
                this.skill[i] = skill[i];

                // Add Skill ICON to SkillSlot
                ImageLayer icon = skill[i].getIcon();
                SkillSlot[i].add(icon);

                final int skillID = i;
                icon.addListener(new Pointer.Adapter(){
                    @Override
                    public void onPointerEnd(Pointer.Event event) {
                        switch (gEnvir.main.getState())
                        {
                            case atkR:
                            case runR:
                            case idleR:
                                Shoot(false,skillID);
                                gEnvir.main.setState(Witch.State.atkR);
                                break;
                            case atkL:
                            case runL:
                            case idleL:
                                Shoot(true,skillID);
                                gEnvir.main.setState(Witch.State.atkL);
                                break;
                        }

                    }
                });

                // Add Cooldown Position to SkillSlot
                this.skill[i].setCover(CooldownSlot[i]);
                SkillSlot[i].add(CooldownSlot[i]);
            }

            // Add SkillSlot to SkillUI
            SkillUI.add(SkillSlot[i]);
        }

        SkillSlot[0].setTranslation(190, 420);
        SkillSlot[1].setTranslation(265, 420);
        SkillSlot[2].setTranslation(345, 420);
        SkillSlot[3].setTranslation(420, 420);

        layer.add(SkillUI);

        // Arrow Control

        ImageLayer leftButton = graphics().createImageLayer(ImageStore.LeftArrow);
        leftButton.setTranslation(30,350);
        layer.add(leftButton);
        leftButton.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerStart(Pointer.Event event) {
                gEnvir.main.setState(Witch.State.runL);
            }

            @Override
            public void onPointerEnd(Pointer.Event event) {
                gEnvir.main.setState(Witch.State.idleL);
            }
        });

        ImageLayer rightButton = graphics().createImageLayer(ImageStore.RightArrow);
        rightButton.setTranslation(100,400);
        layer.add(rightButton);
        rightButton.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerStart(Pointer.Event event) {
                gEnvir.main.setState(Witch.State.runR);
            }

            @Override
            public void onPointerEnd(Pointer.Event event) {
                gEnvir.main.setState(Witch.State.idleR);
            }
        });


        ImageLayer AButton = graphics().createImageLayer(ImageStore.AButton);
        AButton.setTranslation(500,400);
        AButton.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerStart(Pointer.Event event) {
                switch (gEnvir.main.getState())
                {
                    case atkR:
                    case runR:
                    case idleR:
                        objTemp.add(new Fireball(gEnvir.world, gEnvir.layer, (gEnvir.main.getBody().getPosition().x / VariableConstant.worldScale) + 25f, (gEnvir.main.getBody().getPosition().y / VariableConstant.worldScale), DynamicObject.Owner.Ally, false, gEnvir.main.getAttack()));
                        gEnvir.main.setState(Witch.State.atkR);
                        break;
                    case atkL:
                    case runL:
                    case idleL:
                        objTemp.add(new Fireball(gEnvir.world, gEnvir.layer, (gEnvir.main.getBody().getPosition().x / VariableConstant.worldScale) - 25f, (gEnvir.main.getBody().getPosition().y / VariableConstant.worldScale), DynamicObject.Owner.Ally, true, gEnvir.main.getAttack()));
                        gEnvir.main.setState(Witch.State.atkL);
                        break;
                }
            }
        });
        layer.add(AButton);

        ImageLayer jumpButton = graphics().createImageLayer(ImageStore.jumpButton);
        jumpButton.setTranslation(570,350);
        jumpButton.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerStart(Pointer.Event event) {
                gEnvir.main.jump();
            }
        });
        layer.add(jumpButton);

    }

    public boolean Shoot(boolean isLeft,int index)
    {
        if(skill[index] != null)
            return skill[index].Shoot(focus,isLeft,objTemp);
        else
            return false;
    }

    public void update(int delta)
    {
        for(int i =0;i<4;i++)
        {
            if(skill[i] != null)
                skill[i].update(delta);
        }
    }
}
