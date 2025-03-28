import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Book {
    private String title;
    private String author;
    private String cn;

    public Book(String title, String author, String cn) {
        this.title = title;
        this.author = author;
        this.cn = cn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getcn() {
        return cn;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + cn;
    }
}

public class Library extends JFrame implements ActionListener {

    private List<Book> books = new ArrayList<>();
    private DefaultTableModel bookTableModel;
    private JTable bookTable;

    private JTextField titleField;
    private JTextField authorField;
    private JTextField cnField;
    private JButton addButton;
    private JButton searchButton;
    private JTextField searchField;

    public Library() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);

        
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = ImageIO.read(new File("assets\\library bg.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
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
        backgroundPanel.setBounds(0, 0, 1920, 1080); 
        backgroundPanel.setLayout(new BorderLayout(10, 10));


        JLabel titleLabel = new JLabel("Title:");
        JLabel authorLabel = new JLabel("Author:");
        JLabel cnLabel = new JLabel("Control Number:");

        titleField = new JTextField(20);
        authorField = new JTextField(20);
        cnField = new JTextField(20);

        addButton = new JButton("Add Book");
        addButton.addActionListener(this);

        JLabel searchLabel = new JLabel("Search by Title/Author/Control Number:");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        
        String[] columnNames = {"Title", "Author", "Control Number"};
        bookTableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(authorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(cnLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(cnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setOpaque(false); 
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(searchPanel, BorderLayout.CENTER);
        contentPanel.add(tableScrollPane, BorderLayout.SOUTH);

        backgroundPanel.add(contentPanel, BorderLayout.CENTER); 

        setContentPane(backgroundPanel); 
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String cn = cnField.getText().trim();

            if (!title.isEmpty() && !author.isEmpty() && !cn.isEmpty()) {
                Book newBook = new Book(title, author, cn);
                books.add(newBook);
                Object[] rowData = {newBook.getTitle(), newBook.getAuthor(), newBook.getcn()};
                bookTableModel.addRow(rowData);

                // Clear input fields
                titleField.setText("");
                authorField.setText("");
                cnField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all book details.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == searchButton) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            bookTableModel.setRowCount(0); // Clear the table

            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(searchTerm) ||
                    book.getAuthor().toLowerCase().contains(searchTerm) ||
                    book.getcn().toLowerCase().contains(searchTerm)) {
                    Object[] rowData = {book.getTitle(), book.getAuthor(), book.getcn()};
                    bookTableModel.addRow(rowData);
                }
            }

            if (bookTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No books found matching your search.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Library());
    }
}