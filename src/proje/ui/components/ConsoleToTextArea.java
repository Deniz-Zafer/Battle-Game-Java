package proje.ui.components;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

public final class ConsoleToTextArea {

    private ConsoleToTextArea() {}

    public static void redirect(JTextArea area) {
        PrintStream ps = new PrintStream(new OutputStream() {
            @Override public void write(int b) { /* ignore single bytes */ }

            @Override public void write(byte[] b, int off, int len) {
                String text = new String(b, off, len);
                SwingUtilities.invokeLater(() -> {
                    area.append(text);
                    area.setCaretPosition(area.getDocument().getLength());
                });
            }
        }, true);

        System.setOut(ps);
        System.setErr(ps);
    }
}
