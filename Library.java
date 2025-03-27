import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;



public class Library {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(null);

        
        
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage;

            {
                try {
                    // Replace "path/to/your/image.jpg" with the actual path to your image file
                    // It's better to use relative paths from your project's resource directory
                    backgroundImage = ImageIO.read(new File("C:\\Users\\babal\\OneDrive\\Pictures\\library bg.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., load a default image)
                    JOptionPane.showMessageDialog(this, "Error loading background image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setBounds(0, 0, 1920, 1080); // Set the size of the background panel
        backgroundPanel.setLayout(null); // Important: Set layout to null so you can add components with absolute positioning

        JLabel label = new JLabel("Library Management System");
        label.setBounds(10, 10, 500, 50);
        label.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
        backgroundPanel.add(label); // Add the label to the background panel

        frame.setContentPane(backgroundPanel); // Set the background panel as the content pane
        frame.setVisible(true);
    }
}