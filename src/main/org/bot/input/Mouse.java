package src.main.org.bot.input;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import static src.main.org.bot.utility.Sleep.sleep;

public class Mouse implements MouseListener, MouseMotionListener {

    private Component component;
    private int x;
    private int y;

    public Mouse(Component component) {
        this.component = component;
    }

    public void click(final int x, final int y, final boolean left) {

        moveMouse(x, y);
        sleep(50);
        pressMouse(x, y, left);
        sleep(50);
        releaseMouse(x, y, left);
        sleep(50);
        clickMouse(x, y, left);
    }

    public void pressMouse(int x, int y, boolean left) {
        MouseEvent me = new MouseEvent(component,
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x,
                y, 1, false, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        for(MouseListener l : component.getMouseListeners()) {
            if(!(l instanceof Mouse)) {
                l.mousePressed(me);
            }
        }
    }

    public void click(final Point p) {
        click(p.x, p.y, true);
    }

    public void clickMouse(int x, int y, boolean left) {
        try {

            MouseEvent me = new MouseEvent(component,
                    MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x,
                    y, 0, false, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
            for(MouseListener l : component.getMouseListeners()) {
                if(!(l instanceof Mouse)) {
                    l.mouseClicked(me);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseMouse(int x, int y, boolean left) {
        try {

            MouseEvent me = new MouseEvent(component,
                    MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x,
                    y, 0, false, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
            for(MouseListener l : component.getMouseListeners()) {
                if(!(l instanceof Mouse)) {
                    l.mouseReleased(me);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveMouse(int x, int y) {
        try {
            MouseEvent me = new MouseEvent(component,
                    MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x,
                    y, 0, false);
            for(MouseMotionListener l : component.getMouseMotionListeners()) {
                if(!(l instanceof Mouse)) {
                    l.mouseMoved(me);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Point getPoint() {
        return new Point(x, y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        System.out.println(x + " " + y);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    public static boolean processEvent(AWTEvent e){
        return true;  //Return true to process player input
    }
}
