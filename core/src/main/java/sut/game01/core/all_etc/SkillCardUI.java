package sut.game01.core.all_etc;

import playn.core.GroupLayer;
import sut.game01.core.Skill.Screeming_Card;
import sut.game01.core.Skill.SkillCard;
import static playn.core.PlayN.*;
import sut.game01.core.character.Character;

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

    public SkillCardUI(Character focus,GroupLayer layer,List<DynamicObject> objTemp,SkillCard[] skill)
    {
        this.focus = focus;
        this.objTemp = objTemp;

        for(int i =0;i<4;i++)
        {
            this.skill[i] = skill[i];
        }


        for(int i=0;i<4;i++)
        {
            SkillSlot[i] = graphics().createGroupLayer();
            SkillUI.add(SkillSlot[i]);
        }

        SkillSlot[0].setTranslation(150, 420);
        SkillSlot[1].setTranslation(250, 420);
        SkillSlot[2].setTranslation(350, 420);
        SkillSlot[3].setTranslation(450, 420);

        layer.add(SkillUI);

        for(int i=0;i<4;i++)
        {
            SkillSlot[i].add(skill[i].getIcon());
        }
    }
}
