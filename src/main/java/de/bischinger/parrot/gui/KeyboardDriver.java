package de.bischinger.parrot.gui;

import de.bischinger.parrot.network.DroneController;
import de.bischinger.parrot.network.HandshakeRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager;
import static java.awt.event.KeyEvent.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class KeyboardDriver extends JFrame implements Runnable, KeyEventDispatcher {

    public static final int SPEED_CONSTANT = 40;
    public static final int TURN_CONSTANT = 10;
    private DroneController droneController;
    private boolean isUpPressed;
    private boolean isDownPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;

    public KeyboardDriver() throws Exception {

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

    @Override
    public void run() {
        while (true) {
            try {
                MILLISECONDS.sleep(100);

                // set speed
                int speed = 0;
                if (isUpPressed) speed = SPEED_CONSTANT;
                else if (isDownPressed) speed = -1 * SPEED_CONSTANT;

                // set direction
                int direction = 0;
                if (isLeftPressed) direction = -TURN_CONSTANT;
                else if (isRightPressed) direction = TURN_CONSTANT;

                //optimization
                if (!(speed == 0 && direction == 0)) {
                    droneController.pcmd(speed, direction);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (e.getID() == KEY_PRESSED) {
            try {
                handleKeyPressed(keyCode);
            } catch (IOException | InterruptedException e1) {
                e1.printStackTrace();
            }
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

    private void handleKeyPressed(int keyCode) throws IOException, InterruptedException {
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
        case VK_H:
            droneController.jump(true);
            SECONDS.sleep(2);
            break;
        case VK_J:
            droneController.jump(true);
            break;
        case VK_1:
            droneController.spin();
            SECONDS.sleep(2);
            break;
        case VK_2:
            droneController.tap();
            SECONDS.sleep(2);
            break;
        case VK_3:
            droneController.slowshake();
            SECONDS.sleep(2);
            break;
        case VK_4:
            droneController.metronome();
            SECONDS.sleep(2);
            break;
        case VK_5:
            droneController.ondulation();
            SECONDS.sleep(2);
            break;
        case VK_6:
            droneController.spinjump();
            SECONDS.sleep(2);
            break;
        case VK_7:
            droneController.spintoposture();
            SECONDS.sleep(2);
            break;
        case VK_8:
            droneController.spiral();
            SECONDS.sleep(2);
            break;
        case VK_9:
            droneController.slalom();
            SECONDS.sleep(2);
            break;
        case VK_0:
            droneController.stopAnnimation();
            break;
        case VK_A:
            turn90Left();
            break;
        case VK_D:
            turn90Right();
            break;
        case VK_S:
            turn180();
            break;
        }
    }

    private void turn90Right() throws IOException {
        droneController.pcmd(0, 25);
    }

    private void turn90Left() throws IOException {
        droneController.pcmd(0, -25);
    }

    private void turn180() throws IOException {
        droneController.pcmd(0, 50);
    }
}