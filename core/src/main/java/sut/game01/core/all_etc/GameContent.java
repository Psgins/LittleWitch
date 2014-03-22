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
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(".savegame"));

            String[] data = new String[6];
            for(int i=0;i<6;i++)
            {
                data[i] = file.readLine();
            }

            file.close();

            // skill reader
            String[] skillSplit = data[0].split("=");
            skillSplit = skillSplit[1].split(",");
            for(int i=0;i<4;i++) skill[i] = Integer.valueOf(skillSplit[i]);

            //ItemList reader
            String[] itemList = data[1].split("=");
            itemList = itemList[1].substring(1,itemList[1].length()-1).split(",");
            this.itemList = new ArrayList<Integer>();
            for(String x : itemList)
            {
                if(x.equals("")) continue;
                int itemID = Integer.valueOf(x);
                this.itemList.add(itemID);
            }

            //level reader
            String[] levelSplit = data[2].split("=");
            this.level = Integer.valueOf(levelSplit[1]);

            //exp reader
            String[] expSplit = data[3].split("=");
            this.exp = Integer.valueOf(expSplit[1]);

            //rune reader
            String[] runeSplit = data[4].split("=");
            this.rune = Integer.valueOf(runeSplit[1]);

            //ItemList reader
            String[] runeList = data[5].split("=");
            runeList = runeList[1].substring(1,runeList[1].length()-1).split(",");
            this.runeList = new ArrayList<Integer>();
            for(String x : runeList)
            {
                if(x.equals("")) continue;
                int runeID = Integer.valueOf(x);
                this.runeList.add(runeID);
            }


        }
        catch (IOException e)
        {
            create();
        }
    }

    public void create(){
        skill = new int[]{-1,-1,-1,-1};
        itemList = new ArrayList<Integer>();
        level = 1;
        exp = 0;
        rune = -1;
        runeList = new ArrayList<Integer>();

        try {
            BufferedWriter wFile = new BufferedWriter(new FileWriter(".savegame",false));
            wFile.write("skill="+skill[0]+","+skill[1]+","+skill[2]+","+ skill[3]+"\n");
            wFile.write("itemList=" + itemList.toString() + "\n");
            wFile.write("level=" + level + "\n");
            wFile.write("exp=" + exp + "\n");
            wFile.write("rune=" + rune + "\n");
            wFile.write("runeList="+ runeList);
            wFile.close();
        }
        catch (Exception ew)
        {
            System.out.println("Creating context file ... failed");
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
