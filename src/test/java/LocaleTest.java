import org.junit.Test;

import java.util.Locale;

public class LocaleTest {

//    @Test
    public static void main(String... ar) {
//        Locale locale = new Locale("en", "US");
        Locale locale = Locale.forLanguageTag("sv-SE");
        System.out.println(locale.getDisplayName(locale));
    }
}
