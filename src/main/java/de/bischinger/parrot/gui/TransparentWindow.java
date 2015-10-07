package de.bischinger.parrot.gui;

import de.bischinger.parrot.network.DroneController;
import de.bischinger.parrot.network.HandshakeRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import static java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager;
import static java.awt.event.KeyEvent.*;

public class TransparentWindow extends JFrame implements Runnable, KeyEventDispatcher {

    public static final int SPEED_CONSTANT = 40;
    private DroneController droneController;
    private boolean isUpPressed;
    private boolean isDownPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isJumpPressed;

    public TransparentWindow() throws Exception {

        droneController = new DroneController("192.168.2.1", 44444,
                                              new HandshakeRequest("JumpingSumo-b152298",
                                                                   "_arsdk-0902._udp"));
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setOpacity(0.8f);
        setSize(200, 200);
        setVisible(true);
        new Thread(this).start();

        getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    public static void main(String[] args) throws Exception {
        TransparentWindow tw = new TransparentWindow();
    }

    @Override public void run() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);

                if (isJumpPressed) {
                    droneController.jump();
                    TimeUnit.SECONDS.sleep(2);
                    isJumpPressed = false;
                } else {
                    //set speed
                    int speed = 0;
                    if (isUpPressed) speed = SPEED_CONSTANT;
                    else if (isDownPressed) speed = -1 * SPEED_CONSTANT;

                    //set direction
                    int direction = 0;
                    if (isLeftPressed) direction = -10;
                    else if (isRightPressed) direction = 10;

                    droneController.pcmd(speed, direction);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override public boolean dispatchKeyEvent(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (e.getID() == KEY_PRESSED) {
            handleKeyPressed(keyCode);
        } else if (e.getID() == KEY_RELEASED) {
            handleKeyReleased(keyCode);
        }

        return true;
    }

    private void handleKeyReleased(int keyCode) {
        switch (keyCode) {
        case VK_UP:
            isUpPressed = false;
            break;
        case VK_DOWN:
            isDownPressed = false;
            break;
        case VK_LEFT:
            isLeftPressed = false;
            break;
        case VK_RIGHT:
            isRightPressed = false;
            break;
        }
    }

    private void handleKeyPressed(int keyCode) {
        switch (keyCode) {
        case VK_UP:
            isUpPressed = true;
            break;
        case VK_DOWN:
            isDownPressed = true;
            break;
        case VK_LEFT:
            isLeftPressed = true;
            break;
        case VK_RIGHT:
            isRightPressed = true;
            break;
        case VK_J:
            isJumpPressed = true;
            break;
        }
    }
}