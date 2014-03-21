package sut.game01.core.all_etc;

import sut.game01.core.Rune.HPRegenSmall;
import sut.game01.core.Rune.Rune;
import sut.game01.core.Skill.Balloon_Card;
import sut.game01.core.Skill.Screeming_Card;
import sut.game01.core.Skill.Skill;
import sut.game01.core.Skill.SkillCard;

/**
 * Created by PSG on 3/21/14.
 */
public class ContentLoader {
    public static SkillCard[] SkillCardLoader(int[] skillID)
    {
        SkillCard[] sCard = new SkillCard[4];

        for(int i=0;i<4;i++)
        {
            if (skillID[i] < 0)
            {
                sCard[i] = null;
            }
            else
            {
                switch (skillID[i])
                {
                    case 0:
                        sCard[i] = new Screeming_Card();
                        break;
                    case 1:
                        sCard[i] = new Balloon_Card();
                        break;
                }
            }
        }

        return sCard;
    }

    public static Rune RuneLoader(int RuneID)
    {
        switch (RuneID)
        {
            case 0:
                return new HPRegenSmall();
            default:
                return null;
        }
    }
}
