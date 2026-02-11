package com.jomi;

//import java.util.List;

import com.jomi.Handlers.Init.ProgramLoader;
//import com.jomi.Handlers.Init.modifiers.Mod;
import com.jomi.Handlers.registry.ModRegistry;
import com.jomi.Util.ModRoller;
import com.jomi.Util.ModRoller.ModType;

public class Main {
    public static void main(String[] args) {
        ProgramLoader programLoader = new ProgramLoader();
        programLoader.start();


        //List<Mod> searcresult = ModRegistry.getItemByClass("weapons/talisman");
        //System.out.println(searcresult);
        int weight = ModRegistry.getTotalWeightForItemClass("weapons/talisman");

        System.out.println(weight);

        for (int x = 0 ; x < 1 ; x++) {
            System.out.println(ModRoller.rollRandomTier("weapons/talisman", 81, ModType.PREFIX));
        }
        
        

        //to do
        //Mod roller pitäis ottaa huomioon myös 
    }
}