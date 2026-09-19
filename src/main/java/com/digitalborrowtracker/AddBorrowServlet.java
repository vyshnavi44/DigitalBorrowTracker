
package com.digitalborrowtracker;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/add-borrow")
public class AddBorrowServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Get the logged-in user's session
        HttpSession session = request.getSession(false);

        // Make sure the user is logged in
        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        // Get the logged-in user's ID
        int userId = (Integer) session.getAttribute("userId");

        // Get values from the HTML form
        String itemName =
                request.getParameter("item_name");

        String borrowerName =
                request.getParameter("borrower_name");

        String borrowDate =
                request.getParameter("borrow_date");

        String expectedReturnDate =
                request.getParameter("expected_return_date");

        String notes =
                request.getParameter("notes");

        String sql =
                "INSERT INTO borrow_records "
                + "(item_name, borrower_name, borrow_date, "
                + "expected_return_date, notes, status, user_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, itemName);
            statement.setString(2, borrowerName);
            statement.setString(3, borrowDate);
            statement.setString(4, expectedReturnDate);
            statement.setString(5, notes);

            // New records are initially borrowed
            statement.setString(6, "Borrowed");

            // Store the logged-in user's ID
            statement.setInt(7, userId);

            statement.executeUpdate();

            statement.close();
            connection.close();

            // Go to the borrowed-items servlet
            response.sendRedirect("view-borrowed");

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                "<h2>Error while saving borrow record.</h2>"
            );

            response.getWriter().println(
                "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}

