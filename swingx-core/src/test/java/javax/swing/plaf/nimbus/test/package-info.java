/**
 * <a href="https://github.com/homebeaver/SwingSet/issues/39">Feature 39</a>: 
 * replace default icons with nimbus painted
 * 
 */
/*

Die nimbus implementierungen sind in package javax.swing.plaf.nimbus final und nicht public definiert. 
Beispiele:

public abstract class AbstractRegionPainter implements Painter<JComponent> { ...
    protected static class PaintContext { ...

final class ArrowButtonPainter extends AbstractRegionPainter { ...

final class OptionPanePainter extends AbstractRegionPainter { ...

AbstractRegionPainter ist zwar public, aber entält viele Teile die nicht sichtbar sind (PaintContext), 
also sich nicht einfach überschreiben lassen.

Um so viel wie möglich wiederzuverwenden nutze ich ein Subpackage von javax.swing.plaf.nimbus
und experimentiere etwas.

 */
package javax.swing.plaf.nimbus.test;