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

@WebServlet("/returned-items")
public class ReturnedItemsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        // Check login
        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        int userId =
                (Integer) session.getAttribute("userId");

        String sql =
                "SELECT * FROM borrow_records "
                + "WHERE status = 'Returned' "
                + "AND user_id = ? "
                + "ORDER BY actual_return_date DESC";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(1, userId);

            ResultSet resultSet =
                    statement.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<meta name='viewport' "
                    + "content='width=device-width, initial-scale=1.0'>");

            out.println("<title>Returned Items - "
                    + "Digital Borrow &amp; Return Tracker</title>");

            out.println("<style>");

            out.println("* { box-sizing: border-box; }");

            out.println("body {");
            out.println("font-family: Arial, sans-serif;");
            out.println("background: linear-gradient(135deg, #eef5ff, #f8fbff);");
            out.println("margin: 0;");
            out.println("min-height: 100vh;");
            out.println("color: #1f2937;");
            out.println("}");

            /* NAVBAR */

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

            /* CONTAINER */

            out.println(".container {");
            out.println("width: 94%;");
            out.println("max-width: 1250px;");
            out.println("margin: 40px auto;");
            out.println("}");

            /* HEADER */

            out.println(".page-header {");
            out.println("text-align: center;");
            out.println("margin-bottom: 30px;");
            out.println("}");

            out.println(".page-icon {");
            out.println("width: 70px;");
            out.println("height: 70px;");
            out.println("background: #dcfce7;");
            out.println("border-radius: 50%;");
            out.println("display: flex;");
            out.println("align-items: center;");
            out.println("justify-content: center;");
            out.println("margin: 0 auto 18px;");
            out.println("font-size: 34px;");
            out.println("}");

            out.println("h1 {");
            out.println("margin: 0 0 10px;");
            out.println("font-size: 30px;");
            out.println("color: #111827;");
            out.println("}");

            out.println(".description {");
            out.println("color: #6b7280;");
            out.println("font-size: 15px;");
            out.println("margin: 0;");
            out.println("}");

            /* TABLE CARD */

            out.println(".table-card {");
            out.println("background: white;");
            out.println("border-radius: 18px;");
            out.println("box-shadow: 0 15px 40px rgba(0,0,0,0.10);");
            out.println("overflow-x: auto;");
            out.println("padding: 10px;");
            out.println("}");

            out.println("table {");
            out.println("width: 100%;");
            out.println("border-collapse: collapse;");
            out.println("min-width: 950px;");
            out.println("}");

            out.println("th {");
            out.println("background: #1f2937;");
            out.println("color: white;");
            out.println("padding: 15px;");
            out.println("text-align: left;");
            out.println("font-size: 14px;");
            out.println("}");

            out.println("th:first-child {");
            out.println("border-radius: 8px 0 0 0;");
            out.println("}");

            out.println("th:last-child {");
            out.println("border-radius: 0 8px 0 0;");
            out.println("}");

            out.println("td {");
            out.println("padding: 15px;");
            out.println("border-bottom: 1px solid #e5e7eb;");
            out.println("font-size: 14px;");
            out.println("color: #374151;");
            out.println("}");

            out.println("tr:hover td {");
            out.println("background: #f9fafb;");
            out.println("}");

            /* STATUS */

            out.println(".status {");
            out.println("display: inline-block;");
            out.println("background: #dcfce7;");
            out.println("color: #15803d;");
            out.println("padding: 6px 12px;");
            out.println("border-radius: 20px;");
            out.println("font-size: 13px;");
            out.println("font-weight: bold;");
            out.println("}");

            /* EMPTY STATE */

            out.println(".empty-state {");
            out.println("text-align: center;");
            out.println("padding: 55px 20px;");
            out.println("}");

            out.println(".empty-icon {");
            out.println("font-size: 48px;");
            out.println("margin-bottom: 15px;");
            out.println("}");

            out.println(".empty-state h2 {");
            out.println("color: #374151;");
            out.println("margin-bottom: 8px;");
            out.println("}");

            out.println(".empty-state p {");
            out.println("color: #6b7280;");
            out.println("}");

            /* BACK BUTTON */

            out.println(".back-btn {");
            out.println("display: inline-block;");
            out.println("margin-top: 25px;");
            out.println("background: #2563eb;");
            out.println("color: white;");
            out.println("padding: 12px 20px;");
            out.println("border-radius: 8px;");
            out.println("text-decoration: none;");
            out.println("font-weight: bold;");
            out.println("transition: 0.2s;");
            out.println("}");

            out.println(".back-btn:hover {");
            out.println("background: #1d4ed8;");
            out.println("transform: translateY(-1px);");
            out.println("}");

            /* FOOTER */

            out.println(".footer {");
            out.println("text-align: center;");
            out.println("color: #9ca3af;");
            out.println("font-size: 13px;");
            out.println("margin: 30px 0;");
            out.println("}");

            /* MOBILE */

            out.println("@media (max-width: 600px) {");

            out.println(".navbar {");
            out.println("padding: 16px 20px;");
            out.println("}");

            out.println(".navbar h2 {");
            out.println("font-size: 17px;");
            out.println("}");

            out.println(".container {");
            out.println("width: 94%;");
            out.println("margin: 25px auto;");
            out.println("}");

            out.println("h1 {");
            out.println("font-size: 25px;");
            out.println("}");

            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            /* NAVBAR */

            out.println("<div class='navbar'>");

            out.println("<h2>");
            out.println("Digital Borrow &amp; Return Tracker");
            out.println("</h2>");

            out.println("<a href='dashboard' class='dashboard-btn'>");
            out.println("Dashboard");
            out.println("</a>");

            out.println("</div>");

            /* MAIN */

            out.println("<div class='container'>");

            out.println("<div class='page-header'>");

            out.println("<div class='page-icon'>✓</div>");

            out.println("<h1>Returned Items</h1>");

            out.println("<p class='description'>");
            out.println("Items that have been successfully returned.");
            out.println("</p>");

            out.println("</div>");

            out.println("<div class='table-card'>");

            boolean found = false;

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Item</th>");
            out.println("<th>Borrower</th>");
            out.println("<th>Borrow Date</th>");
            out.println("<th>Expected Return</th>");
            out.println("<th>Actual Return</th>");
            out.println("<th>Notes</th>");
            out.println("<th>Status</th>");

            out.println("</tr>");

            while (resultSet.next()) {

                found = true;

                out.println("<tr>");

                out.println("<td>");
                out.println(resultSet.getString("item_name"));
                out.println("</td>");

                out.println("<td>");
                out.println(resultSet.getString("borrower_name"));
                out.println("</td>");

                out.println("<td>");
                out.println(resultSet.getString("borrow_date"));
                out.println("</td>");

                out.println("<td>");
                out.println(resultSet.getString("expected_return_date"));
                out.println("</td>");

                out.println("<td>");
                out.println(resultSet.getString("actual_return_date"));
                out.println("</td>");

                out.println("<td>");

                String notes =
                        resultSet.getString("notes");

                if (notes == null ||
                    notes.trim().isEmpty()) {

                    out.println("-");

                } else {

                    out.println(notes);
                }

                out.println("</td>");

                out.println("<td>");

                out.println("<span class='status'>");
                out.println("✓ Returned");
                out.println("</span>");

                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            if (!found) {

                out.println("<div class='empty-state'>");

                out.println("<div class='empty-icon'>📭</div>");

                out.println("<h2>No Returned Items</h2>");

                out.println("<p>");
                out.println("You don't have any returned records yet.");
                out.println("</p>");

                out.println("</div>");
            }

            out.println("</div>");

            out.println("<a class='back-btn' href='dashboard'>");
            out.println("&larr; Back to Dashboard");
            out.println("</a>");

            out.println("<div class='footer'>");
            out.println("Digital Borrow &amp; Return Tracker");
            out.println("</div>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            out.println("<h2>Error loading returned items.</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
        }
    }
}