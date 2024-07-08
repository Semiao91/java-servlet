package main.servlet;

import dao.BookDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet(name = "HelloServlet", urlPatterns = "/")
public class HelloServlet extends HttpServlet {
    private BookDAO bookDAO;
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        bookDAO = new BookDAO(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/delete":
                    deleteBook(request, response);
                    break;
                case "/insert":
                    insertBook(request, response);
                    break;
                case "/update":
                    updateBook(request, response);
                    break;
                default:
                    listBook(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void listBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        List<Book> listBook = bookDAO.listAllBooks();
        request.setAttribute("listBook", listBook);
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Book existingBook = bookDAO.getBookById(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookForm.jsp");
        request.setAttribute("book", existingBook);
        dispatcher.forward(request, response);
    }

    private void insertBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        float price = Float.parseFloat(request.getParameter("price"));

        Book newBook = new Book(title, author, price);
        bookDAO.insertBook(newBook);
        response.sendRedirect("list");
    }

    private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));

        Book book = new Book(id);
        bookDAO.deleteBook(book);
        response.sendRedirect("list");
    }

    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        float price = Float.parseFloat(request.getParameter("price"));

        Book book = new Book(id, title, author, price);
        bookDAO.updateBook(book);
        response.sendRedirect("list");
    }
}
