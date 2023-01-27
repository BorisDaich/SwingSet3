package javax.swing.plaf.nimbus.test;

import java.awt.Color;

/* Nimbus Primary Colors
defined in javax.swing.plaf.nimbus.initializeDefaults :

        addColor(d, "text", 0, 0, 0, 255);
        addColor(d, "control", 214, 217, 223, 255); // 0xD6D9DF
        addColor(d, "nimbusBase", 51, 98, 140, 255);
        addColor(d, "nimbusBlueGrey", "nimbusBase", 0.032459438f, -0.52518797f, 0.19607842f, 0);
// "nimbusBlueGrey" is a variant of "nimbusBase"
        ...
the approx. color names are taken from https://www.htmlcsscolor.com/hex/29598B (ENDEAVOUR)

(de):
Ich weiss nicht, ob es hilft, die approx Farben zu definieren. Man kann wenigstens lesen,
dass NIMBUSFOCUS ein Blauton ist!
Daher diese Klasse nur im test package.
 */
public interface NimbusColors {

    static final Color ENDEAVOUR          = new Color(0x29598B);
    static final Color NIMBUSBASE         = new Color(0x33628C); // approx ENDEAVOUR, fast schwarz, dunkler als NIGHT_RIDER
    static final Color MISCHKA            = new Color(0xA5A9B2);
    static final Color NIMBUS_BLUEGRAY    = new Color(0xA9B0BE); // approx MISCHKA, grau
    static final Color JORDY_BLUE         = new Color(0x7AAAE0);
    static final Color NIMBUSFOCUS        = new Color(0x73A4D1); // approx JORDY_BLUE
    static final Color BROWN              = new Color(0xA52A2A);
    static final Color NIMBUS_RED         = new Color(0xA92E22); // approx BROWN
    static final Color TENNE              = new Color(0xCD5700);
    static final Color NIMBUS_ORANGE      = new Color(0xBF6204); // approx Tenne (Tawny)
    static final Color CERULEAN_BLUE      = new Color(0x2A52BE);
    static final Color NIMBUS_INFOBLUE    = new Color(0x2F5CB4); // approx CERULEAN_BLUE
    static final Color TURBO              = new Color(0xF5CC23);
    static final Color NIMBUS_ALERTYELLOW = new Color(0xFFDC23); // approx TURBO
    static final Color ASTRAL             = new Color(0x376F89);
    static final Color NIMBUS_SELECTIONBACKGROUND = new Color(0x39698A); // approx ASTRAL
    static final Color NIMBUS_TEXT        = Color.BLACK;
    static final Color HAWKES_BLUE        = new Color(0xD2DAED);
    static final Color NIMBUS_CONTROL     = new Color(0xD6D9DF); // approx Hawkes Blue
    static final Color EARLS_GREEN        = new Color(0xB8A722);
    static final Color NIMBUS_GREEN       = new Color(0xB0B332); // approx Earls Green
    static final Color NIMBUSSELECTEDTEXT = Color.WHITE;
    static final Color MANATEE            = new Color(0x8D90A1);
    static final Color NIMBUSDISABLEDTEXT = new Color(0x8E8F91); // approx Manatee
    static final Color NIMBUS_LIGHTBACKGROUND = Color.WHITE;
    static final Color CUMULUS            = new Color(0xF5F4C1);
    static final Color INFO               = new Color(0xF2F2BD); // approx Cumulus
}
