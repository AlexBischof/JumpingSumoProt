package de.bischinger.parrot.gui;

/**
 * Created by alexanderbischof on 25.10.15.
 */

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class JumpingSumoLang {

    public final static String cmd = "/*\r\n" + "* A simple example demonstrating the basic features.\r\n" + "*/\r\n" + "delay 300; // sleep for 300ms\r\n"
            + "forward 101;\r\n"
            + "backward 100;\r\n"
            + "left 100;\r\n"
            + "right 100;\r\n"
            + "jump high;\r\n"
            + "jump long;\r\n"
            + "jump long;\r\n";

    public static void main(String[] args) throws IOException, AWTException {
        System.out.println(cmd);

        JumpingSumoLang robotLang = new JumpingSumoLang();
        robotLang.execute(null);
    }

    public void execute(Path inputPath) throws IOException, AWTException {

        de.bischinger.parrot.gui.JumpingSumoLexer lexer = new de.bischinger.parrot.gui.JumpingSumoLexer(new ANTLRInputStream(cmd));
        de.bischinger.parrot.gui.JumpingSumoParser parser = new de.bischinger.parrot.gui.JumpingSumoParser(new CommonTokenStream(lexer));
        parser.addParseListener(new de.bischinger.parrot.gui.JumpingSumoBaseListener() {
            @Override
            public void exitInstructionDelay(de.bischinger.parrot.gui.JumpingSumoParser.InstructionDelayContext ctx) {
                int delayParam = Integer.parseInt(ctx.paramMs.getText());
                System.out.println("delay(" + delayParam + ")");
            }

            @Override
            public void exitInstructionForward(de.bischinger.parrot.gui.JumpingSumoParser.InstructionForwardContext ctx) {
                int delayParam = Integer.parseInt(ctx.paramMs.getText());
                System.out.println("forward(" + delayParam + ")");
            }

            @Override
            public void exitInstructionBackward(de.bischinger.parrot.gui.JumpingSumoParser.InstructionBackwardContext ctx) {
                int delayParam = Integer.parseInt(ctx.paramMs.getText());
                System.out.println("backward(" + delayParam + ")");
            }

            @Override
            public void exitInstructionLeft(de.bischinger.parrot.gui.JumpingSumoParser.InstructionLeftContext ctx) {
                int delayParam = Integer.parseInt(ctx.degrees.getText());
                System.out.println("left(" + delayParam + ")");
            }

            @Override
            public void exitInstructionRight(de.bischinger.parrot.gui.JumpingSumoParser.InstructionRightContext ctx) {
                int delayParam = Integer.parseInt(ctx.degrees.getText());
                System.out.println("right(" + delayParam + ")");
            }

            @Override
            public void exitInstructionJump(de.bischinger.parrot.gui.JumpingSumoParser.InstructionJumpContext ctx) {
                System.out.println("jump(" + ctx.type.getText() + ")");
            }
        });
        parser.instructions();
    }
}
