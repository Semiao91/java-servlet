package dao;

import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private String jbdcURL;
    private String jbdcUsername;
    private String jbdcPassword;
    private Connection jbdcConnection;

    public BookDAO(String jbdcURL, String jbdcUsername, String jbdcPassword) {
        this.jbdcURL = jbdcURL;
        this.jbdcUsername = jbdcUsername;
        this.jbdcPassword = jbdcPassword;
    }

    protected void connect() throws SQLException {
        if (jbdcConnection == null || jbdcConnection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            jbdcConnection = DriverManager.getConnection(jbdcURL, jbdcUsername, jbdcPassword);
        }
    }

    protected void disconnect() throws SQLException {
        if (jbdcConnection != null && !jbdcConnection.isClosed()) {
            jbdcConnection.close();
        }
    }

    public boolean insertBook(Book book) throws SQLException {
        String sql = "INSERT INTO book (title, author, price) VALUES (?, ?, ?)";
        connect();

        PreparedStatement ps = jbdcConnection.prepareStatement(sql);
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor());
        ps.setDouble(3, book.getPrice());

        boolean rowInserted = ps.executeUpdate() > 0;
        ps.close();
        disconnect();
        return rowInserted;
    }

    public List<Book> listAllBooks() throws SQLException {
        ArrayList<Book> listBook = new ArrayList<>();
        String sql = "SELECT * FROM book";
        connect();

        Statement statement = jbdcConnection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("book_id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            float price = rs.getFloat("price");

            Book book = new Book(id, title, author, price);
            listBook.add(book);
        }

        rs.close();
        statement.close();
        disconnect();
        return listBook;
    }

    public boolean deleteBook(Book book) throws SQLException {
        String sql = "DELETE FROM book where book_id = ?";

        connect();

        PreparedStatement ps = jbdcConnection.prepareStatement(sql);
        ps.setInt(1, book.getId());

        boolean rowDeleted = ps.executeUpdate() > 0;
        ps.close();
        disconnect();
        return rowDeleted;
    }

    public boolean updateBook(Book book) throws SQLException {
        String sql = "UPDATE book SET title = ?, author = ?, price = ?";
        sql += " WHERE book_id = ?";
        connect();

        PreparedStatement ps = jbdcConnection.prepareStatement(sql);
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor());
        ps.setFloat(3, book.getPrice());
        ps.setInt(4, book.getId());

        boolean rowUpdated = ps.executeUpdate() > 0;
        ps.close();
        disconnect();
        return  rowUpdated;
    }

    public Book getBookById(int id) throws SQLException {
        Book book = null;
        String sql = "Select * FROM book WHERE book_id = ?";

        connect();

        PreparedStatement ps = jbdcConnection.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            String title = rs.getString("title");
            String author = rs.getString("author");
            float price = rs.getFloat("price");

            book = new Book(id, title, author, price);
        }

        ps.close();
        rs.close();
        disconnect();
        return book;
    }
}
