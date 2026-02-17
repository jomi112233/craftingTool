package Util.CurrencyAction;


import com.jomi.Handlers.Init.basicCurrency.Orb;

public class OrbLoaderTest {

    public static Orb orb(
        String id,
        String name,
        String minRarity,
        String newRarity,
        int removeCount,
        int addCount,
        String target
    ) {
        return new Orb(
            id,
            name,
            minRarity,
            newRarity,
            removeCount,
            addCount,
            target,
            "basic"
        );
    }
}
