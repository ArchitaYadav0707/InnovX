import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JWindow loading = createLoadingScreen();
            loading.setVisible(true);
            
            Thread startupThread = new Thread(() -> {
                new SubmissionServer().startServer();
                try { Thread.sleep(850); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                SwingUtilities.invokeLater(() -> {
                    DashboardFrame app = new DashboardFrame();
                    app.setLocationRelativeTo(null);
                    loading.dispose();
                    app.setVisible(true);
                });
            }, "innovx-startup-loader");
            startupThread.setDaemon(true);
            startupThread.start();
        });
    }

    private static JWindow createLoadingScreen() {
        JWindow window = new JWindow();
        JPanel panel = new JPanel(new BorderLayout(10, 10)); panel.setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36)); panel.setBackground(new Color(24, 41, 70));
        JLabel title = new JLabel("InnovX", SwingConstants.CENTER); title.setForeground(Color.WHITE); title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        JLabel message = new JLabel("Preparing secure idea analysis…", SwingConstants.CENTER); message.setForeground(Color.WHITE);
        JProgressBar progress = new JProgressBar(); progress.setIndeterminate(true);
        panel.add(title, BorderLayout.NORTH); panel.add(message, BorderLayout.CENTER); panel.add(progress, BorderLayout.SOUTH);
        window.setContentPane(panel); window.pack(); window.setLocationRelativeTo(null); return window;
    }
}