import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

class Book {
    private String title, author, cn, status;

    public Book(String title, String author, String cn) {
        this.title = title;
        this.author = author;
        this.cn = cn;
        this.status = "Available";
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCn() { return cn; }
    public String getStatus() { return status; }
    public void checkOut() { this.status = "Checked Out"; }
    public void returnBook() { this.status = "Available"; }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    
    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println("Background image not found.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            setBackground(Color.DARK_GRAY);
        }
    }
}

public class Library extends JFrame implements ActionListener {
    private ArrayList<Book> books = new ArrayList<>();
    private DefaultTableModel bookTableModel;
    private JTable bookTable;
    private JTextField titleField, authorField, cnField, searchField;
    private JButton addButton, searchButton, deleteButton, checkOutButton, returnButton, sortButton;

    public Library() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\ADMIN\\Downloads\\library_bg.jpg");
        backgroundPanel.setLayout(new BorderLayout());

        bookTableModel = new DefaultTableModel(new String[]{"Title", "Author", "Control Number", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        bookTable = new JTable(bookTableModel);
        bookTable.setForeground(Color.WHITE);
        bookTable.setBackground(Color.DARK_GRAY);

        JScrollPane tableScrollPane = new JScrollPane(bookTable);
        tableScrollPane.getViewport().setOpaque(false);

        JTableHeader header = bookTable.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);

        titleField = new JTextField(15);
        authorField = new JTextField(15);
        cnField = new JTextField(15);
        searchField = new JTextField(15);

        addButton = new JButton("Add Book");
        searchButton = new JButton("Search");
        deleteButton = new JButton("Delete");
        checkOutButton = new JButton("Check Out");
        returnButton = new JButton("Return");
        sortButton = new JButton("Sort by Title");

        addButton.addActionListener(this);
        searchButton.addActionListener(this);
        deleteButton.addActionListener(this);
        checkOutButton.addActionListener(this);
        returnButton.addActionListener(this);
        sortButton.addActionListener(this);

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.add(new JLabel("Title:")); inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:")); inputPanel.add(authorField);
        inputPanel.add(new JLabel("Control Number:")); inputPanel.add(cnField);
        inputPanel.add(addButton);

        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.add(checkOutButton);
        actionPanel.add(returnButton);
        actionPanel.add(deleteButton);
        actionPanel.add(sortButton);

        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(searchPanel, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);
        contentPanel.add(tableScrollPane, BorderLayout.EAST);

        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) addBook();
        else if (e.getSource() == searchButton) searchBook();
        else if (e.getSource() == deleteButton) deleteBook();
        else if (e.getSource() == checkOutButton) checkOutBook();
        else if (e.getSource() == returnButton) returnBook();
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String cn = cnField.getText().trim();
        if (title.isEmpty() || author.isEmpty() || cn.isEmpty()) return;
        for (Book book : books) {
            if (book.getCn().equals(cn) || (book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author))) return;
        }
        books.add(new Book(title, author, cn));
        sortBooks();
        refreshTable();
    }

    private void sortBooks() {
        for (int i = 1; i < books.size(); i++) {
            Book key = books.get(i);
            int j = i - 1;
            while (j >= 0 && books.get(j).getTitle().compareToIgnoreCase(key.getTitle()) > 0) {
                books.set(j + 1, books.get(j));
                j--;
            }
            books.set(j + 1, key);
        }
    }

    private void searchBook() {
        String query = searchField.getText().trim().toLowerCase();
        bookTableModel.setRowCount(0);
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(query) || book.getAuthor().toLowerCase().contains(query)) {
                bookTableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getCn(), book.getStatus()});
            }
        }
    }

        private void deleteBook() {
        int row = bookTable.getSelectedRow();
        if (row == -1) return;
        books.remove(row);
        refreshTable();
    }

    private void checkOutBook() {
        int row = bookTable.getSelectedRow();
        if (row == -1) return;
        books.get(row).checkOut();
        refreshTable();
    }

    private void returnBook() {
        int row = bookTable.getSelectedRow();
        if (row == -1) return;
        books.get(row).returnBook();
        refreshTable();
    }

    private void refreshTable() {
        bookTableModel.setRowCount(0);
        for (Book book : books) {
            bookTableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getCn(), book.getStatus()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Library::new);
    }
}
