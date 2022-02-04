package tr.elite.eliterising.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <h1>ColorLang</h1>
 * <p>
 *     This is language of COLOR COMPONENTS for v1.1
 * </p>
 */
public class ColorLang {
    private static final HashMap<String, String> dictionary = new HashMap<>();

    public void init() {
        // KEY=VALUE
        dictionary.put("RED","Kırmızı");
        dictionary.put("BLUE","Mavi");
        dictionary.put("GREEN","Yeşil");
        dictionary.put("YELLOW","Sarı");
        dictionary.put("DARK_RED","Koyu Kırmızı");
        dictionary.put("DARK_BLUE","Koyu Mavi");
        dictionary.put("DARK_GREEN","Koyu Yeşil");
        dictionary.put("GOLD","Altın");
        dictionary.put("AQUA","Açık Mavi");
        dictionary.put("LIGHT_PURPLE","Açık Mor");
        dictionary.put("DARK_PURPLE","Mor");
        dictionary.put("GRAY","Gri");
        dictionary.put("WHITE","Beyaz");
        dictionary.put("DARK_AQUA","Turkuaz");
        dictionary.put("DARK_GRAY","Koyu Gri");
        dictionary.put("BLACK","Siyah");
    }

    public static String translateToTR(String str) {
        return dictionary.get(str);
    }

    public static String translateToEN(String str) {
        ArrayList<String> keys = new ArrayList<>(dictionary.keySet());
        ArrayList<String> vals = new ArrayList<>(dictionary.values());
        int i = vals.indexOf(str);
        return keys.get(i);
    }
}
