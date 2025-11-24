// Class for fonts

package application;

import javafx.scene.text.Font;


public final class Fonts {

	// FONT CONSTANTS
	public static final String COMING_SOON = "file:Fonts/ComingSoon/ComingSoon.ttf";
    public static final String MONTSERRAT_REGULAR = "file:Fonts/Montserrat/static/Montserrat-Regular.ttf";
    public static final String MONTSERRAT_ITALIC = "file:Fonts/Montserrat/Montserrat-Italic-VariableFont_wght.ttf";
    public static final String MONTSERRAT_BOLD = "file:Fonts/Montserrat/static/Montserrat-Bold.ttf";
    public static final String SENSA_SERIF = "file:Fonts/SensaSerif/sensaserif-regular.ttf";
    public static final String SENSA_WILD = "file:Fonts/SensaWild/sensa-wild-4.otf";

    private Fonts() {}

    // Getters
    public static Font load(String uri, double size) {
        Font f = Font.loadFont(uri, size);
        if (f == null) {
            try {
                return Font.font("Montserrat", size); // default IF HINDI NAGLOAD
            } catch (Exception ex) {
                return Font.getDefault();
            }
        }
        return f;
    }

    public static Font loadComingSoon(double size) { return load(COMING_SOON, size); }
    public static Font loadMontserratRegular(double size) { return load(MONTSERRAT_REGULAR, size); }
    public static Font loadMontserratItalic(double size) { return load(MONTSERRAT_ITALIC, size); }
    public static Font loadMontserratBold(double size) { return load(MONTSERRAT_BOLD, size); }
    public static Font loadSensaSerif(double size) { return load(SENSA_SERIF, size); }
    public static Font loadSensaWild(double size) { return load(SENSA_WILD, size); }
}
