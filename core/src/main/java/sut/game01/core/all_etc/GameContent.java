package sut.game01.core.all_etc;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by PSG on 3/21/14.
 */
public class GameContent {
    private int[] skill = new int[4];
    private List<Integer> itemList;
    private int level;
    private int exp;

    private int rune;
    private List<Integer> runeList;

    public void setSkill(int[] skill){this.skill = skill;}
    public int[] getSkill(){return skill;}

    public void setItem(List<Integer> itemList){this.itemList = itemList;}
    public List<Integer> getItem() {return itemList;}

    public void setLevel(int level){this.level = level;}
    public int getLevel(){return level;}

    public void setExp(int exp){this.exp = exp;}
    public int getExp(){return exp;}

    public void setRune(int rune) { this.rune = rune;}
    public int getRune(){return rune;}

    public void setRuneList(List<Integer> runeList){this.runeList = runeList;}
    public List<Integer> getRuneList() {return runeList;}

    public void save(){}

    public void load(){
        BufferedReader file = null;
        try
        {
            file = new BufferedReader(new FileReader(".savegame"));
            System.out.println("FileFound:");
        }
        catch (IOException e)
        {
            create();
        }
        finally {

            try {
                if(file != null) file.close();
            }
            catch ( Exception ex)
            {
                System.out.println(ex.getMessage());
            }

        }
    }

    public void create(){
        skill = new int[]{-1,-1,-1,-1};
        itemList = new ArrayList<Integer>();
        itemList.add(1);
        itemList.add(2);
        level = 1;
        exp = 0;
        rune = 0;

        BufferedWriter wFile = null;
        try {
            wFile = new BufferedWriter(new FileWriter(".savegame",false));
            wFile.write("skill = "+skill[0]+","+skill[1]+","+skill[2]+","+ skill[3]);
            wFile.newLine();
            wFile.write("itemList = " + itemList.toString());
            wFile.newLine();
            wFile.write("level = " + level);
            wFile.newLine();
            wFile.write("exp = " + exp);
            wFile.newLine();
            wFile.write("rune = " + rune);
            System.out.println("Create new game context file.");
        }
        catch (Exception ew)
        {
            System.out.println("writefile error");
        }
        finally {
            if(wFile != null)
            {
                try {
                    wFile.close();
                }
                catch (Exception ec)
                {

                }
            }
        }
    }

    public void Refresh()
    {
        // ITEM UNIQUE
        for(int i=0;i<4;i++)
        {
            if(skill[i] > -1)
            {
                itemList.add(skill[i]);
            }
        }

        HashSet<Integer> tmp = new HashSet<Integer>();
        tmp.addAll(itemList);
        itemList.clear();
        itemList.addAll(tmp);

        for(int i=0;i<4;i++)
        {
            if(skill[i] > -1)
            {
                itemList.remove((Object)skill[i]);
            }
        }

        //RUNE UNIQUE
        runeList.add(rune);
        tmp.clear();
        tmp.addAll(runeList);
        runeList.clear();
        runeList.addAll(tmp);
        runeList.remove((Object)rune);

    }
}
