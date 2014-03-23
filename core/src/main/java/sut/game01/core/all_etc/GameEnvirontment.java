package sut.game01.core.all_etc;

import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import sut.game01.core.UI.FloatLabel;
import sut.game01.core.UI.HPBarUI;
import sut.game01.core.character.*;
import sut.game01.core.character.Character;

import java.util.List;

/**
 * Created by PSG on 3/23/14.
 */
public class GameEnvirontment {
    public World world;
    public GroupLayer layer;
    public Witch main;
    public List<DynamicObject> tmpList;
    public FloatLabel fLabel;
    public HPBarUI hpBarUI;
    public GameContent gContent;
}
