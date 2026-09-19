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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * CHECK LOGIN SESSION
         */

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        int userId =
                (Integer) session.getAttribute("userId");

        String loggedInUsername =
                (String) session.getAttribute("username");

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        /*
         * SQL QUERIES
         */

        String borrowedSql =
                "SELECT COUNT(*) FROM borrow_records "
                + "WHERE status = 'Borrowed' "
                + "AND user_id = ?";

        String returnedSql =
                "SELECT COUNT(*) FROM borrow_records "
                + "WHERE status = 'Returned' "
                + "AND user_id = ?";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DatabaseConnection.getConnection();

            /*
             * BORROWED COUNT
             */

            PreparedStatement borrowedStatement =
                    connection.prepareStatement(borrowedSql);

            borrowedStatement.setInt(1, userId);

            ResultSet borrowedResult =
                    borrowedStatement.executeQuery();

            int borrowedCount = 0;

            if (borrowedResult.next()) {
                borrowedCount =
                        borrowedResult.getInt(1);
            }

            /*
             * RETURNED COUNT
             */

            PreparedStatement returnedStatement =
                    connection.prepareStatement(returnedSql);

            returnedStatement.setInt(1, userId);

            ResultSet returnedResult =
                    returnedStatement.executeQuery();

            int returnedCount = 0;

            if (returnedResult.next()) {
                returnedCount =
                        returnedResult.getInt(1);
            }

            /*
             * HTML START
             */

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println(
                "<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>"
            );

            out.println("<title>Dashboard - Digital Borrow &amp; Return Tracker</title>");

            /*
             * CSS
             */

            out.println("<style>");

            out.println("* {");
            out.println("box-sizing: border-box;");
            out.println("}");

            out.println("body {");
            out.println("font-family: Arial, sans-serif;");
            out.println("background: linear-gradient(135deg, #eef5ff, #f8fbff);");
            out.println("margin: 0;");
            out.println("min-height: 100vh;");
            out.println("color: #222;");
            out.println("}");

            /*
             * NAVBAR
             */

            out.println(".navbar {");
            out.println("background-color: #1f2937;");
            out.println("color: white;");
            out.println("padding: 18px 40px;");
            out.println("display: flex;");
            out.println("justify-content: space-between;");
            out.println("align-items: center;");
            out.println("box-shadow: 0 3px 12px rgba(0,0,0,0.15);");
            out.println("}");

            out.println(".navbar h2 {");
            out.println("margin: 0;");
            out.println("font-size: 22px;");
            out.println("}");

            /*
             * LOGOUT BUTTON
             */

            out.println(".logout-btn {");
            out.println("background-color: #dc2626;");
            out.println("color: white;");
            out.println("padding: 10px 18px;");
            out.println("text-decoration: none;");
            out.println("border-radius: 8px;");
            out.println("font-weight: bold;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".logout-btn:hover {");
            out.println("background-color: #b91c1c;");
            out.println("transform: translateY(-2px);");
            out.println("}");

            /*
             * CONTAINER
             */

            out.println(".container {");
            out.println("width: 90%;");
            out.println("max-width: 1050px;");
            out.println("margin: 50px auto;");
            out.println("}");

            /*
             * WELCOME SECTION
             */

            out.println(".welcome {");
            out.println("text-align: center;");
            out.println("margin-bottom: 40px;");
            out.println("}");

            out.println(".welcome-icon {");
            out.println("width: 70px;");
            out.println("height: 70px;");
            out.println("background-color: #2563eb;");
            out.println("color: white;");
            out.println("border-radius: 50%;");
            out.println("display: flex;");
            out.println("align-items: center;");
            out.println("justify-content: center;");
            out.println("margin: 0 auto 20px;");
            out.println("font-size: 32px;");
            out.println("}");

            out.println(".welcome h1 {");
            out.println("font-size: 32px;");
            out.println("margin: 0 0 10px;");
            out.println("}");

            out.println(".welcome p {");
            out.println("color: #666;");
            out.println("font-size: 16px;");
            out.println("}");

            /*
             * CARDS
             */

            out.println(".cards {");
            out.println("display: flex;");
            out.println("justify-content: center;");
            out.println("gap: 25px;");
            out.println("flex-wrap: wrap;");
            out.println("}");

            out.println(".card {");
            out.println("background-color: white;");
            out.println("width: 300px;");
            out.println("padding: 30px;");
            out.println("border-radius: 18px;");
            out.println("text-align: center;");
            out.println("box-shadow: 0 12px 30px rgba(0,0,0,0.10);");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".card:hover {");
            out.println("transform: translateY(-5px);");
            out.println("box-shadow: 0 18px 35px rgba(0,0,0,0.14);");
            out.println("}");

            out.println(".card-icon {");
            out.println("font-size: 35px;");
            out.println("margin-bottom: 10px;");
            out.println("}");

            out.println(".card h2 {");
            out.println("font-size: 20px;");
            out.println("margin: 10px 0;");
            out.println("}");

            out.println(".number {");
            out.println("font-size: 48px;");
            out.println("font-weight: bold;");
            out.println("color: #2563eb;");
            out.println("margin: 10px 0;");
            out.println("}");

            out.println(".card p {");
            out.println("color: #666;");
            out.println("margin: 0;");
            out.println("}");

            /*
             * BUTTON SECTION
             */

            out.println(".buttons {");
            out.println("display: flex;");
            out.println("justify-content: center;");
            out.println("flex-wrap: wrap;");
            out.println("gap: 12px;");
            out.println("margin-top: 40px;");
            out.println("}");

            out.println(".btn {");
            out.println("display: inline-block;");
            out.println("padding: 13px 22px;");
            out.println("text-decoration: none;");
            out.println("border-radius: 8px;");
            out.println("font-weight: bold;");
            out.println("background-color: #2563eb;");
            out.println("color: white;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".btn:hover {");
            out.println("background-color: #1d4ed8;");
            out.println("transform: translateY(-2px);");
            out.println("}");

            /*
             * FOOTER
             */

            out.println(".footer {");
            out.println("text-align: center;");
            out.println("margin-top: 45px;");
            out.println("color: #999;");
            out.println("font-size: 13px;");
            out.println("}");

            /*
             * MOBILE DESIGN
             */

            out.println("@media (max-width: 600px) {");

            out.println(".navbar {");
            out.println("padding: 18px 20px;");
            out.println("}");

            out.println(".navbar h2 {");
            out.println("font-size: 17px;");
            out.println("}");

            out.println(".logout-btn {");
            out.println("padding: 8px 12px;");
            out.println("font-size: 13px;");
            out.println("}");

            out.println(".container {");
            out.println("margin: 35px auto;");
            out.println("}");

            out.println(".welcome h1 {");
            out.println("font-size: 26px;");
            out.println("}");

            out.println(".card {");
            out.println("width: 100%;");
            out.println("}");

            out.println("}");

            out.println("</style>");

            out.println("</head>");

            /*
             * BODY
             */

            out.println("<body>");

            /*
             * NAVBAR
             */

            out.println("<div class='navbar'>");

            out.println("<h2>");
            out.println("Digital Borrow &amp; Return Tracker");
            out.println("</h2>");

            out.println(
                "<a class='logout-btn' href='logout'>Logout</a>"
            );

            out.println("</div>");

            /*
             * MAIN CONTAINER
             */

            out.println("<div class='container'>");

            /*
             * WELCOME
             */

            out.println("<div class='welcome'>");

            out.println("<div class='welcome-icon'>");
            out.println("👋");
            out.println("</div>");

            out.println(
                "<h1>Welcome, "
                + loggedInUsername
                + "!</h1>"
            );

            out.println("<p>");
            out.println(
                "Manage your borrowed and returned items easily."
            );
            out.println("</p>");

            out.println("</div>");

            /*
             * CARDS
             */

            out.println("<div class='cards'>");

            /*
             * BORROWED CARD
             */

            out.println("<div class='card'>");

            out.println("<div class='card-icon'>📦</div>");

            out.println("<h2>Currently Borrowed</h2>");

            out.println("<div class='number'>");
            out.println(borrowedCount);
            out.println("</div>");

            out.println("<p>");
            out.println("Items currently with others");
            out.println("</p>");

            out.println("</div>");

            /*
             * RETURNED CARD
             */

            out.println("<div class='card'>");

            out.println("<div class='card-icon'>✅</div>");

            out.println("<h2>Returned Items</h2>");

            out.println("<div class='number'>");
            out.println(returnedCount);
            out.println("</div>");

            out.println("<p>");
            out.println("Items returned to you");
            out.println("</p>");

            out.println("</div>");

            out.println("</div>");

            /*
             * BUTTONS
             */

            out.println("<div class='buttons'>");

            out.println(
                "<a class='btn' href='add-borrow.html'>"
                + "➕ Add Borrow Record"
                + "</a>"
            );

            out.println(
                "<a class='btn' href='view-borrowed'>"
                + "📋 View Borrowed Items"
                + "</a>"
            );

            out.println(
                "<a class='btn' href='returned-items'>"
                + "✅ View Returned Items"
                + "</a>"
            );

            out.println("</div>");

            /*
             * FOOTER
             */

            out.println("<div class='footer'>");

            out.println(
                "Digital Borrow &amp; Return Tracker"
            );

            out.println("</div>");

            out.println("</div>");

            /*
             * HTML END
             */

            out.println("</body>");
            out.println("</html>");

            /*
             * CLOSE DATABASE RESOURCES
             */

            borrowedResult.close();
            borrowedStatement.close();

            returnedResult.close();
            returnedStatement.close();

            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h2>Error loading dashboard.</h2>");

            out.println("<p>");
            out.println(e.getMessage());
            out.println("</p>");
        }
    }
}