package sut.game01.core.UI;

import playn.core.GroupLayer;
import sut.game01.core.Skill.SkillCard;
import static playn.core.PlayN.*;

import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.character.Character;
import sut.game01.core.screen.Stage1;

import java.util.List;

/**
 * Created by PSG on 3/17/14.
 */
public class SkillCardUI {

    private Character focus;
    private List<DynamicObject> objTemp;
    private SkillCard[] skill = new SkillCard[4];

    private GroupLayer SkillUI = graphics().createGroupLayer();
    private GroupLayer[] SkillSlot = new GroupLayer[4];
    private GroupLayer[] CooldownSlot = new GroupLayer[4];

    public SkillCardUI(Character focus,GroupLayer layer,List<DynamicObject> objTemp,SkillCard[] skill)
    {
        this.focus = focus;
        this.objTemp = objTemp;

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
                SkillSlot[i].add(skill[i].getIcon());

                // Add Cooldown Position to SkillSlot
                this.skill[i].setCover(CooldownSlot[i]);
                SkillSlot[i].add(CooldownSlot[i]);
            }

            // Add SkillSlot to SkillUI
            SkillUI.add(SkillSlot[i]);
        }

        SkillSlot[0].setTranslation(150, 420);
        SkillSlot[1].setTranslation(250, 420);
        SkillSlot[2].setTranslation(350, 420);
        SkillSlot[3].setTranslation(450, 420);

        layer.add(SkillUI);
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
