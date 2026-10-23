import javax.swing.SwingUtilities;

import view.ScreenSaver;

public class App {
    public static void main(String[] args) throws Exception {
        // Garante que a interface gráfica seja criada dentro da Thread de Eventos do Swing
        SwingUtilities.invokeLater(() -> {
            ScreenSaver saver = new ScreenSaver();
            saver.start();
        });
    }
}
