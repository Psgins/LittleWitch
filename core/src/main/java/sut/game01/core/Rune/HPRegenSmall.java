package sut.game01.core.Rune;

import sut.game01.core.character.*;
import sut.game01.core.character.Character;

/**
 * Created by PSG on 3/18/14.
 */
public class HPRegenSmall extends Rune {
    @Override
    public void Buff(Character focus)
    {
        float result = focus.getHp() + 5f;
        if(result > focus.getMaxHp())
            result = focus.getMaxHp();
        focus.setHp(result);
    }
}
