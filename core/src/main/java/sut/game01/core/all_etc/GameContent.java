package sut.game01.core.all_etc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PSG on 3/21/14.
 */
public class GameContent {
    private int[] skill = new int[4];
    private List<Integer> itemList;
    private int level;
    private int exp;

    public void setSkill(int[] skill){this.skill = skill;}
    public int[] getSkill(){return skill;}

    public void setItem(List<Integer> itemList){this.itemList = itemList;}
    public List<Integer> getItem() {return itemList;}

    public void setLevel(int level){this.level = level;}
    public int getLevel(){return level;}

    public void setExp(int exp){this.exp = exp;}
    public int getExp(){return exp;}

    public void save(){}
    public void load(){}
    public void create(){
        skill = new int[]{-1,-1,-1,-1};
        itemList = new ArrayList<Integer>();
        level = 1;
        exp = 0;
    }
}
