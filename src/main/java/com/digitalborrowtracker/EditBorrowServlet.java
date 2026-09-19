package com.digitalborrowtracker;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/edit-borrow")
public class EditBorrowServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Check login
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String id = request.getParameter("id");

        if (id == null || id.trim().isEmpty()) {
            out.println("<h2>Invalid borrow record ID.</h2>");
            return;
        }

        String sql =
                "SELECT * FROM borrow_records "
                + "WHERE id = ? AND user_id = ?";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(1, Integer.parseInt(id));
            statement.setInt(2, userId);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                String itemName =
                        resultSet.getString("item_name");

                String borrowerName =
                        resultSet.getString("borrower_name");

                String borrowDate =
                        resultSet.getString("borrow_date");

                String expectedReturnDate =
                        resultSet.getString("expected_return_date");

                String notes =
                        resultSet.getString("notes");

                if (notes == null) {
                    notes = "";
                }

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");

                out.println("<meta charset='UTF-8'>");

                out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");

                out.println("<title>Edit Borrow Record - Digital Borrow &amp; Return Tracker</title>");

                out.println("<style>");

                out.println("* { box-sizing: border-box; }");

                out.println("body {");
                out.println("font-family: Arial, sans-serif;");
                out.println("background: linear-gradient(135deg, #eef5ff, #f8fbff);");
                out.println("margin: 0;");
                out.println("min-height: 100vh;");
                out.println("color: #1f2937;");
                out.println("}");

                out.println(".navbar {");
                out.println("background: #1f2937;");
                out.println("color: white;");
                out.println("padding: 18px 40px;");
                out.println("display: flex;");
                out.println("justify-content: space-between;");
                out.println("align-items: center;");
                out.println("}");

                out.println(".navbar h2 {");
                out.println("margin: 0;");
                out.println("font-size: 21px;");
                out.println("}");

                out.println(".dashboard-btn {");
                out.println("background: #374151;");
                out.println("color: white;");
                out.println("text-decoration: none;");
                out.println("padding: 10px 16px;");
                out.println("border-radius: 8px;");
                out.println("font-size: 14px;");
                out.println("font-weight: bold;");
                out.println("}");

                out.println(".dashboard-btn:hover {");
                out.println("background: #4b5563;");
                out.println("}");

                out.println(".container {");
                out.println("width: 92%;");
                out.println("max-width: 700px;");
                out.println("margin: 45px auto;");
                out.println("}");

                out.println(".form-card {");
                out.println("background: white;");
                out.println("padding: 40px;");
                out.println("border-radius: 18px;");
                out.println("box-shadow: 0 15px 40px rgba(0,0,0,0.10);");
                out.println("}");

                out.println(".page-icon {");
                out.println("width: 70px;");
                out.println("height: 70px;");
                out.println("background: #e8f0ff;");
                out.println("border-radius: 50%;");
                out.println("display: flex;");
                out.println("align-items: center;");
                out.println("justify-content: center;");
                out.println("font-size: 34px;");
                out.println("margin: 0 auto 20px;");
                out.println("}");

                out.println("h1 {");
                out.println("text-align: center;");
                out.println("font-size: 28px;");
                out.println("margin: 0 0 8px;");
                out.println("color: #111827;");
                out.println("}");

                out.println(".subtitle {");
                out.println("text-align: center;");
                out.println("color: #6b7280;");
                out.println("margin-bottom: 32px;");
                out.println("font-size: 15px;");
                out.println("}");

                out.println(".form-group {");
                out.println("margin-bottom: 22px;");
                out.println("}");

                out.println("label {");
                out.println("display: block;");
                out.println("font-weight: bold;");
                out.println("margin-bottom: 8px;");
                out.println("color: #374151;");
                out.println("}");

                out.println("input, textarea {");
                out.println("width: 100%;");
                out.println("padding: 13px 14px;");
                out.println("border: 1px solid #d1d5db;");
                out.println("border-radius: 9px;");
                out.println("font-size: 15px;");
                out.println("font-family: Arial, sans-serif;");
                out.println("outline: none;");
                out.println("transition: 0.2s;");
                out.println("}");

                out.println("input:focus, textarea:focus {");
                out.println("border-color: #2563eb;");
                out.println("box-shadow: 0 0 0 3px rgba(37,99,235,0.12);");
                out.println("}");

                out.println("textarea {");
                out.println("height: 110px;");
                out.println("resize: vertical;");
                out.println("}");

                out.println(".submit-btn {");
                out.println("width: 100%;");
                out.println("padding: 14px;");
                out.println("background: #2563eb;");
                out.println("color: white;");
                out.println("border: none;");
                out.println("border-radius: 9px;");
                out.println("font-size: 16px;");
                out.println("font-weight: bold;");
                out.println("cursor: pointer;");
                out.println("transition: 0.2s;");
                out.println("}");

                out.println(".submit-btn:hover {");
                out.println("background: #1d4ed8;");
                out.println("transform: translateY(-1px);");
                out.println("}");

                out.println(".back-link {");
                out.println("display: block;");
                out.println("text-align: center;");
                out.println("margin-top: 22px;");
                out.println("text-decoration: none;");
                out.println("color: #2563eb;");
                out.println("font-weight: bold;");
                out.println("}");

                out.println(".back-link:hover {");
                out.println("text-decoration: underline;");
                out.println("}");

                out.println(".footer {");
                out.println("text-align: center;");
                out.println("color: #9ca3af;");
                out.println("font-size: 13px;");
                out.println("margin-top: 25px;");
                out.println("}");

                out.println("@media (max-width: 600px) {");

                out.println(".navbar {");
                out.println("padding: 16px 20px;");
                out.println("}");

                out.println(".navbar h2 {");
                out.println("font-size: 17px;");
                out.println("}");

                out.println(".container {");
                out.println("margin: 25px auto;");
                out.println("}");

                out.println(".form-card {");
                out.println("padding: 28px 22px;");
                out.println("}");

                out.println("h1 {");
                out.println("font-size: 24px;");
                out.println("}");

                out.println("}");

                out.println("</style>");

                out.println("</head>");

                out.println("<body>");

                out.println("<div class='navbar'>");

                out.println("<h2>Digital Borrow &amp; Return Tracker</h2>");

                out.println("<a href='dashboard' class='dashboard-btn'>Dashboard</a>");

                out.println("</div>");

                out.println("<div class='container'>");

                out.println("<div class='form-card'>");

                out.println("<div class='page-icon'>✏</div>");

                out.println("<h1>Edit Borrow Record</h1>");

                out.println("<p class='subtitle'>Update the details of your borrowed item</p>");

                out.println("<form action='edit-borrow' method='post'>");

                out.println("<input type='hidden' name='id' value='"
                        + resultSet.getInt("id")
                        + "'>");

                out.println("<div class='form-group'>");

                out.println("<label for='item_name'>Item Name</label>");

                out.println("<input type='text' "
                        + "id='item_name' "
                        + "name='item_name' "
                        + "value='" + itemName + "' "
                        + "required>");

                out.println("</div>");

                out.println("<div class='form-group'>");

                out.println("<label for='borrower_name'>Borrower Name</label>");

                out.println("<input type='text' "
                        + "id='borrower_name' "
                        + "name='borrower_name' "
                        + "value='" + borrowerName + "' "
                        + "required>");

                out.println("</div>");

                out.println("<div class='form-group'>");

                out.println("<label for='borrow_date'>Borrow Date</label>");

                out.println("<input type='date' "
                        + "id='borrow_date' "
                        + "name='borrow_date' "
                        + "value='" + borrowDate + "' "
                        + "required>");

                out.println("</div>");

                out.println("<div class='form-group'>");

                out.println("<label for='expected_return_date'>Expected Return Date</label>");

                out.println("<input type='date' "
                        + "id='expected_return_date' "
                        + "name='expected_return_date' "
                        + "value='" + expectedReturnDate + "' "
                        + "required>");

                out.println("</div>");

                out.println("<div class='form-group'>");

                out.println("<label for='notes'>Notes</label>");

                out.println("<textarea "
                        + "id='notes' "
                        + "name='notes'>"
                        + notes
                        + "</textarea>");

                out.println("</div>");

                out.println("<button type='submit' class='submit-btn'>");
                out.println("Update Borrow Record");
                out.println("</button>");

                out.println("</form>");

                out.println("<a href='view-borrowed' class='back-link'>");
                out.println("&larr; Back to Borrowed Items");
                out.println("</a>");

                out.println("<div class='footer'>");
                out.println("Digital Borrow &amp; Return Tracker");
                out.println("</div>");

                out.println("</div>");

                out.println("</div>");

                out.println("</body>");

                out.println("</html>");

            } else {

                out.println("<h2>Borrow record not found or you do not have permission to edit it.</h2>");
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Error loading borrow record.</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Check login
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String id = request.getParameter("id");

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
                "UPDATE borrow_records SET "
                + "item_name = ?, "
                + "borrower_name = ?, "
                + "borrow_date = ?, "
                + "expected_return_date = ?, "
                + "notes = ? "
                + "WHERE id = ? AND user_id = ?";

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
            statement.setInt(6, Integer.parseInt(id));
            statement.setInt(7, userId);

            int rowsUpdated = statement.executeUpdate();

            statement.close();
            connection.close();

            if (rowsUpdated > 0) {
                response.sendRedirect("view-borrowed");
            } else {
                response.setContentType("text/html;charset=UTF-8");

                response.getWriter().println(
                    "<h2>Record not found or you do not have permission to edit it.</h2>"
                );

                response.getWriter().println(
                    "<p><a href='view-borrowed'>Back to Borrowed Items</a></p>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                "<h2>Error updating borrow record.</h2>"
            );

            response.getWriter().println(
                "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}