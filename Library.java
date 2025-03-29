import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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

    public String getCn() {
        return cn;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", Control Number: " + cn;
    }
}

public class Library extends JFrame implements ActionListener {

    private ArrayList<Book> books = new ArrayList<>();
    private DefaultTableModel bookTableModel;
    private JTable bookTable;

    private JTextField titleField;
    private JTextField authorField;
    private JTextField cnField;
    private JButton addButton;
    private JButton searchButton;
    private JButton deleteButton;
    private JTextField searchField;
    private JTextField deleteField;

    public Library() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage;

            {
                try {
                    backgroundImage = ImageIO.read(new File("C:\\Users\\babal\\OneDrive\\Pictures\\library bg.jpg"));
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

        JLabel deleteLabel = new JLabel("Delete by Title:");
        deleteField = new JTextField(20);
        deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(this);

        String[] columnNames = {"Title", "Author", "Control Number"};
        bookTableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        bookTable.setOpaque(false);
        bookTable.setBackground(new Color(0, 0, 0, 0));
        bookTable.setShowGrid(false);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Set table header text color to white
        JTableHeader header = bookTable.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(0, 0, 0, 0));
        header.setForeground(Color.WHITE);

        // Custom cell renderer for white text and transparent background
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(0, 0, 0, 0));
                c.setForeground(Color.WHITE); // Set text color to white
                if (isSelected) {
                    c.setForeground(table.getSelectionForeground()); // Keep selection foreground
                }
                return c;
            }
        };
        for (int i = 0; i < bookTable.getColumnCount(); i++) {
            bookTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(titleLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(titleField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(authorLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(authorField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(cnLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(cnField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deletePanel.setOpaque(false);
        deletePanel.add(deleteLabel);
        deletePanel.add(deleteField);
        deletePanel.add(deleteButton);

        // Create a new panel to hold searchPanel and deletePanel
        JPanel searchDeletePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchDeletePanel.setOpaque(false);
        searchDeletePanel.add(searchPanel);
        searchDeletePanel.add(deletePanel);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(searchDeletePanel, BorderLayout.CENTER); // Added the combined panel
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
                Object[] rowData = {newBook.getTitle(), newBook.getAuthor(), newBook.getCn()};
                bookTableModel.addRow(rowData);
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all book details.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == searchButton) {
            String searchTerm = searchField.getText().trim().toLowerCase();
            bookTableModel.setRowCount(0);

            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(searchTerm) ||
                    book.getAuthor().toLowerCase().contains(searchTerm) ||
                    book.getCn().toLowerCase().contains(searchTerm)) {
                    Object[] rowData = {book.getTitle(), book.getAuthor(), book.getCn()};
                    bookTableModel.addRow(rowData);
                }
            }

            if (bookTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No books found matching your search.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            String deleteTitle = deleteField.getText().trim().toLowerCase();

            if (!deleteTitle.isEmpty()) {
                boolean found = false;
                for (int i = 0; i < books.size(); i++) {
                    if (books.get(i).getTitle().toLowerCase().equals(deleteTitle)) {
                        books.remove(i);
                        bookTableModel.removeRow(i);
                        found = true;
                        break;
                    }
                }

                if (found) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Enter a title to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearInputFields() {
        titleField.setText("");
        authorField.setText("");
        cnField.setText("");
        deleteField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Library());
    }
}
