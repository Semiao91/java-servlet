<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Books Store Application</title>
        <style>
            table {
                margin-left: auto;
                margin-right: auto;
                border-collapse: collapse;
                width: 50%;
            }
            table, th, td {
                border: 1px solid black;
            }
            th, td {
                padding: 10px;
                text-align: center;
            }
            caption {
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>
        <div style="text-align: center;">
            <h1>Books Management</h1>
            <h2>
                <a href="/new">Add New Book</a>
                &nbsp;&nbsp;&nbsp;
                <a href="/list">List All Books</a>
            </h2>
        </div>
        <div>
            <table>
                <caption><h2>List of Books</h2></caption>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
                <c:forEach var="book" items="${listBook}">
                    <tr>
                        <td><c:out value="${book.id}" /></td>
                        <td><c:out value="${book.title}" /></td>
                        <td><c:out value="${book.author}" /></td>
                        <td><c:out value="${book.price}" /></td>
                        <td>
                            <a href="/edit?id=<c:out value='${book.id}' />">Edit</a>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <a href="/delete?id=<c:out value='${book.id}' />">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </body>
</html>
