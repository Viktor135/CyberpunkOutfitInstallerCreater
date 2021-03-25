package de.vsc.coi.gui;

import static de.vsc.coi.utils.FileReaderUtils.resourceToUrL;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.vsc.coi.Info;
import de.vsc.coi.utils.FileReaderUtils;

public class Gui extends JFrame {

    private static final Logger LOGGER = LogManager.getLogger(Gui.class);
    public static final Color CYBERPUNK_YELLOW = Color.decode("#f8f102");

    private final GridBagConstraints constraints;
    public InfoPanel infoPanel;
    private JLabel statusLabel;

    public Gui() {
        super("CpOIC");
        this.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
    }

    public static JPanel wrapInPanel(final Component component) {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CYBERPUNK_YELLOW);
        panel.add(component, new GridBagConstraints());
        return panel;
    }

    public void init() throws URISyntaxException, IOException {
        final ImageIcon logo = new ImageIcon(ImageIO.read(resourceToUrL("gui/logo.png")));

        this.setIconImages(getLogos());
        final JLabel logoLabel = new JLabel(logo, JLabel.CENTER);
        this.statusLabel = new JLabel("Starting...", JLabel.CENTER);
        this.statusLabel.setFont(new Font("Serif", Font.PLAIN, 42));
        this.getContentPane().setBackground(CYBERPUNK_YELLOW);

        this.addRow(logoLabel);
        this.addRow(statusLabel);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1218, 609);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void displayError(final String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public InfoPanel showInfoPanel(final Info info) {
        infoPanel = new InfoPanel();
        infoPanel.fromInfo(info);
        this.addRow(infoPanel.scrollable());
        this.pack();
        return infoPanel;
    }

    public Info closeInfoPanel() {
        this.remove(infoPanel.scrollable());
        this.repaint();
        final Info info = this.infoPanel.toInfoIfChanched();
        this.infoPanel = null;
        return info;
    }

    public void finished() {
        this.setStatus("Finished!");
        final JButton button = new JButton("OK");
        button.addActionListener(e -> close());
        this.addRow(wrapInPanel(button));
    }

    public void setStatus(final String message) {
        LOGGER.info("Status: " + message);
        this.statusLabel.setText(message);
    }

    public void close() {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private List<Image> getLogos() throws URISyntaxException, IOException {
        final List<Image> images = new ArrayList<>();
        final String[] logos = FileReaderUtils.resourceToFile("gui/taskbar").list();
        Objects.requireNonNull(logos);
        for (final String x : logos) {
            images.add(ImageIO.read(resourceToUrL("gui/taskbar/" + x)));
        }
        return images;
    }

    public void addRow(final Component comp) {
        constraints.gridy++;
        super.add(comp, constraints);
    }
}