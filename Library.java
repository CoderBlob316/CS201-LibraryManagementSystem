import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private JTextField searchField;
    private JButton sortByTitleButton;
    private JButton sortByAuthorButton;

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

        sortByTitleButton = new JButton("Sort by Title");
        sortByTitleButton.addActionListener(this);
        sortByAuthorButton = new JButton("Sort by Author");
        sortByAuthorButton.addActionListener(this);

        String[] columnNames = {"Title", "Author", "Control Number"};
        bookTableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(bookTableModel);
        JScrollPane tableScrollPane = new JScrollPane(bookTable);

        // Make table and scroll pane transparent
        bookTable.setOpaque(false);
        bookTable.setBackground(new Color(0, 0, 0, 0));
        bookTable.setShowGrid(false);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

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

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.setOpaque(false);
        sortPanel.add(sortByTitleButton);
        sortPanel.add(sortByAuthorButton);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(inputPanel, BorderLayout.NORTH);
        contentPanel.add(searchPanel, BorderLayout.CENTER);
        contentPanel.add(sortPanel, BorderLayout.SOUTH);
        contentPanel.add(tableScrollPane, BorderLayout.AFTER_LAST_LINE);

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
        } else if (e.getSource() == sortByTitleButton) {
            mergeSort(books, "title");
            updateTable();
        } else if (e.getSource() == sortByAuthorButton) {
            mergeSort(books, "author");
            updateTable();
        }
    }

    private void clearInputFields() {
        titleField.setText("");
        authorField.setText("");
        cnField.setText("");
    }

    private void mergeSort(ArrayList<Book> books, String sortBy) {
        if (books.size() > 1) {
            int mid = books.size() / 2;
            ArrayList<Book> left = new ArrayList<>(books.subList(0, mid));
            ArrayList<Book> right = new ArrayList<>(books.subList(mid, books.size()));

            mergeSort(left, sortBy);
            mergeSort(right, sortBy);

            merge(books, left, right, sortBy);
        }
    }

    private void merge(ArrayList<Book> books, ArrayList<Book> left, ArrayList<Book> right, String sortBy) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (sortBy.equals("title") && left.get(i).getTitle().compareToIgnoreCase(right.get(j).getTitle()) <= 0 ||
                sortBy.equals("author") && left.get(i).getAuthor().compareToIgnoreCase(right.get(j).getAuthor()) <= 0) {
                books.set(k++, left.get(i++));
            } else {
                books.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            books.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            books.set(k++, right.get(j++));
        }
    }

    private void updateTable() {
        bookTableModel.setRowCount(0);
        for (Book book : books) {
            Object[] rowData = {book.getTitle(), book.getAuthor(), book.getCn()};
            bookTableModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Library());
    }
}
