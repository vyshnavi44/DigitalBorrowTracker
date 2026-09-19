package com.digitalborrowtracker;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/view-borrowed")
public class ViewBorrowedServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * CHECK LOGIN
         */

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        int userId =
                (Integer) session.getAttribute("userId");

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        /*
         * SEARCH AND STATUS FILTER
         */

        String search = request.getParameter("search");

        String statusFilter =
                request.getParameter("status");

        if (search == null) {
            search = "";
        }

        if (statusFilter == null ||
            statusFilter.isEmpty()) {

            statusFilter = "All";
        }

        /*
         * SQL QUERY
         */

        StringBuilder sql = new StringBuilder(
            "SELECT * FROM borrow_records "
            + "WHERE user_id = ?"
        );

        if (!search.trim().isEmpty()) {

            sql.append(
                " AND (item_name LIKE ? "
                + "OR borrower_name LIKE ?)"
            );
        }

        if (!statusFilter.equalsIgnoreCase("All")) {

            sql.append(" AND status = ?");
        }

        sql.append(" ORDER BY id DESC");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(
                        sql.toString()
                    );

            int parameterIndex = 1;

            statement.setInt(
                parameterIndex++,
                userId
            );

            /*
             * SEARCH PARAMETERS
             */

            if (!search.trim().isEmpty()) {

                String searchValue =
                    "%" + search.trim() + "%";

                statement.setString(
                    parameterIndex++,
                    searchValue
                );

                statement.setString(
                    parameterIndex++,
                    searchValue
                );
            }

            /*
             * STATUS PARAMETER
             */

            if (!statusFilter.equalsIgnoreCase("All")) {

                statement.setString(
                    parameterIndex,
                    statusFilter
                );
            }

            ResultSet resultSet =
                    statement.executeQuery();

            /*
             * HTML
             */

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println(
                "<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>"
            );

            out.println(
                "<title>Borrowed Items - "
                + "Digital Borrow &amp; Return Tracker</title>"
            );

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

            out.println(".dashboard-link {");
            out.println("background-color: #374151;");
            out.println("color: white;");
            out.println("padding: 9px 16px;");
            out.println("border-radius: 8px;");
            out.println("text-decoration: none;");
            out.println("font-weight: bold;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".dashboard-link:hover {");
            out.println("background-color: #4b5563;");
            out.println("}");

            /*
             * CONTAINER
             */

            out.println(".container {");
            out.println("width: 94%;");
            out.println("max-width: 1400px;");
            out.println("margin: 45px auto;");
            out.println("}");

            /*
             * PAGE HEADER
             */

            out.println(".page-header {");
            out.println("text-align: center;");
            out.println("margin-bottom: 35px;");
            out.println("}");

            out.println(".page-icon {");
            out.println("width: 70px;");
            out.println("height: 70px;");
            out.println("background-color: #2563eb;");
            out.println("color: white;");
            out.println("border-radius: 50%;");
            out.println("display: flex;");
            out.println("align-items: center;");
            out.println("justify-content: center;");
            out.println("margin: 0 auto 18px;");
            out.println("font-size: 32px;");
            out.println("}");

            out.println(".page-header h1 {");
            out.println("font-size: 30px;");
            out.println("margin: 0 0 10px;");
            out.println("}");

            out.println(".description {");
            out.println("color: #666;");
            out.println("margin: 0;");
            out.println("}");

            /*
             * SEARCH CARD
             */

            out.println(".search-box {");
            out.println("background-color: white;");
            out.println("padding: 22px;");
            out.println("border-radius: 16px;");
            out.println("box-shadow: 0 10px 30px rgba(0,0,0,0.08);");
            out.println("margin-bottom: 25px;");
            out.println("}");

            out.println(".search-form {");
            out.println("display: flex;");
            out.println("gap: 10px;");
            out.println("flex-wrap: wrap;");
            out.println("}");

            out.println(".search-input {");
            out.println("flex: 1;");
            out.println("min-width: 250px;");
            out.println("padding: 13px;");
            out.println("border: 1px solid #d1d5db;");
            out.println("border-radius: 8px;");
            out.println("font-size: 15px;");
            out.println("}");

            out.println(".search-input:focus {");
            out.println("outline: none;");
            out.println("border-color: #2563eb;");
            out.println("box-shadow: 0 0 0 3px rgba(37,99,235,0.12);");
            out.println("}");

            out.println(".status-select {");
            out.println("padding: 13px;");
            out.println("border: 1px solid #d1d5db;");
            out.println("border-radius: 8px;");
            out.println("font-size: 15px;");
            out.println("background-color: white;");
            out.println("}");

            out.println(".search-btn {");
            out.println("padding: 13px 22px;");
            out.println("background-color: #2563eb;");
            out.println("color: white;");
            out.println("border: none;");
            out.println("border-radius: 8px;");
            out.println("font-weight: bold;");
            out.println("cursor: pointer;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".search-btn:hover {");
            out.println("background-color: #1d4ed8;");
            out.println("transform: translateY(-2px);");
            out.println("}");

            out.println(".clear-btn {");
            out.println("padding: 13px 22px;");
            out.println("background-color: #6b7280;");
            out.println("color: white;");
            out.println("border-radius: 8px;");
            out.println("text-decoration: none;");
            out.println("font-weight: bold;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".clear-btn:hover {");
            out.println("background-color: #4b5563;");
            out.println("}");

            /*
             * TABLE CARD
             */

            out.println(".table-card {");
            out.println("background-color: white;");
            out.println("padding: 25px;");
            out.println("border-radius: 16px;");
            out.println("box-shadow: 0 10px 30px rgba(0,0,0,0.08);");
            out.println("overflow-x: auto;");
            out.println("}");

            out.println("table {");
            out.println("width: 100%;");
            out.println("border-collapse: collapse;");
            out.println("min-width: 1000px;");
            out.println("}");

            out.println("th {");
            out.println("background-color: #1f2937;");
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
            out.println("}");

            out.println("tr:hover {");
            out.println("background-color: #f8fafc;");
            out.println("}");

            /*
             * STATUS
             */

            out.println(".status {");
            out.println("font-weight: bold;");
            out.println("}");

            out.println(".borrowed {");
            out.println("color: #2563eb;");
            out.println("}");

            out.println(".returned {");
            out.println("color: #16a34a;");
            out.println("}");

            /*
             * DUE STATUS
             */

            out.println(".due-status {");
            out.println("font-weight: bold;");
            out.println("}");

            out.println(".due-normal {");
            out.println("color: #2563eb;");
            out.println("}");

            out.println(".due-soon {");
            out.println("color: #d97706;");
            out.println("}");

            out.println(".overdue {");
            out.println("color: #dc2626;");
            out.println("}");

            out.println(".due-returned {");
            out.println("color: #16a34a;");
            out.println("}");

            /*
             * ACTION BUTTONS
             */

            out.println(".action-btn {");
            out.println("display: inline-block;");
            out.println("padding: 7px 12px;");
            out.println("margin: 2px;");
            out.println("border-radius: 6px;");
            out.println("text-decoration: none;");
            out.println("font-weight: bold;");
            out.println("font-size: 12px;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".edit-btn {");
            out.println("background-color: #2563eb;");
            out.println("color: white;");
            out.println("}");

            out.println(".return-btn {");
            out.println("background-color: #16a34a;");
            out.println("color: white;");
            out.println("}");

            out.println(".delete-btn {");
            out.println("background-color: #dc2626;");
            out.println("color: white;");
            out.println("}");

            out.println(".action-btn:hover {");
            out.println("transform: translateY(-2px);");
            out.println("opacity: 0.9;");
            out.println("}");

            /*
             * EMPTY MESSAGE
             */

            out.println(".empty-message {");
            out.println("text-align: center;");
            out.println("padding: 40px;");
            out.println("color: #666;");
            out.println("}");

            out.println(".empty-icon {");
            out.println("font-size: 45px;");
            out.println("margin-bottom: 10px;");
            out.println("}");

            /*
             * BACK BUTTON
             */

            out.println(".back-btn {");
            out.println("display: inline-block;");
            out.println("margin-top: 25px;");
            out.println("background-color: #1f2937;");
            out.println("color: white;");
            out.println("padding: 12px 20px;");
            out.println("border-radius: 8px;");
            out.println("text-decoration: none;");
            out.println("font-weight: bold;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".back-btn:hover {");
            out.println("background-color: #374151;");
            out.println("transform: translateY(-2px);");
            out.println("}");

            /*
             * FOOTER
             */

            out.println(".footer {");
            out.println("text-align: center;");
            out.println("margin-top: 35px;");
            out.println("color: #999;");
            out.println("font-size: 13px;");
            out.println("}");

            /*
             * MOBILE
             */

            out.println("@media (max-width: 600px) {");

            out.println(".navbar {");
            out.println("padding: 18px 20px;");
            out.println("}");

            out.println(".navbar h2 {");
            out.println("font-size: 17px;");
            out.println("}");

            out.println(".dashboard-link {");
            out.println("padding: 8px 12px;");
            out.println("font-size: 13px;");
            out.println("}");

            out.println(".container {");
            out.println("margin: 30px auto;");
            out.println("}");

            out.println(".page-header h1 {");
            out.println("font-size: 26px;");
            out.println("}");

            out.println(".search-input {");
            out.println("min-width: 100%;");
            out.println("}");

            out.println(".status-select, .search-btn, .clear-btn {");
            out.println("width: 100%;");
            out.println("text-align: center;");
            out.println("}");

            out.println(".table-card {");
            out.println("padding: 15px;");
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

            out.println(
                "<h2>Digital Borrow &amp; Return Tracker</h2>"
            );

            out.println(
                "<a class='dashboard-link' href='dashboard'>"
                + "🏠 Dashboard"
                + "</a>"
            );

            out.println("</div>");

            /*
             * CONTAINER
             */

            out.println("<div class='container'>");

            /*
             * PAGE HEADER
             */

            out.println("<div class='page-header'>");

            out.println("<div class='page-icon'>📋</div>");

            out.println("<h1>Borrowed Items</h1>");

            out.println(
                "<p class='description'>"
                + "Search and manage your borrowed and returned items."
                + "</p>"
            );

            out.println("</div>");

            /*
             * SEARCH FORM
             */

            out.println("<div class='search-box'>");

            out.println(
                "<form class='search-form' "
                + "method='get' action='view-borrowed'>"
            );

            out.println(
                "<input class='search-input' "
                + "type='text' "
                + "name='search' "
                + "placeholder='🔍 Search by item or borrower' "
                + "value='" + search + "'>"
            );

            out.println(
                "<select class='status-select' name='status'>"
            );

            out.println("<option value='All'");

            if (statusFilter.equalsIgnoreCase("All")) {
                out.println(" selected");
            }

            out.println(">All Status</option>");

            out.println("<option value='Borrowed'");

            if (statusFilter.equalsIgnoreCase("Borrowed")) {
                out.println(" selected");
            }

            out.println(">Borrowed</option>");

            out.println("<option value='Returned'");

            if (statusFilter.equalsIgnoreCase("Returned")) {
                out.println(" selected");
            }

            out.println(">Returned</option>");

            out.println("</select>");

            out.println(
                "<button class='search-btn' type='submit'>"
                + "🔍 Search"
                + "</button>"
            );

            out.println(
                "<a class='clear-btn' href='view-borrowed'>"
                + "Clear"
                + "</a>"
            );

            out.println("</form>");

            out.println("</div>");

            /*
             * TABLE
             */

            out.println("<div class='table-card'>");

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Item</th>");
            out.println("<th>Borrower</th>");
            out.println("<th>Borrow Date</th>");
            out.println("<th>Expected Return</th>");
            out.println("<th>Notes</th>");
            out.println("<th>Status</th>");
            out.println("<th>Due Status</th>");
            out.println("<th>Actions</th>");

            out.println("</tr>");

            boolean found = false;

            while (resultSet.next()) {

                found = true;

                int id =
                    resultSet.getInt("id");

                String status =
                    resultSet.getString("status");

                Date expectedReturnDate =
                    resultSet.getDate(
                        "expected_return_date"
                    );

                String notes =
                    resultSet.getString("notes");

                out.println("<tr>");

                /*
                 * ITEM
                 */

                out.println("<td>");

                out.println(
                    resultSet.getString("item_name")
                );

                out.println("</td>");

                /*
                 * BORROWER
                 */

                out.println("<td>");

                out.println(
                    resultSet.getString("borrower_name")
                );

                out.println("</td>");

                /*
                 * BORROW DATE
                 */

                out.println("<td>");

                out.println(
                    resultSet.getString("borrow_date")
                );

                out.println("</td>");

                /*
                 * EXPECTED RETURN
                 */

                out.println("<td>");

                out.println(expectedReturnDate);

                out.println("</td>");

                /*
                 * NOTES
                 */

                out.println("<td>");

                if (notes != null &&
                    !notes.trim().isEmpty()) {

                    out.println(notes);

                } else {

                    out.println("-");
                }

                out.println("</td>");

                /*
                 * STATUS
                 */

                if ("Borrowed".equalsIgnoreCase(status)) {

                    out.println(
                        "<td class='status borrowed'>"
                    );

                    out.println("🟦 Borrowed");

                    out.println("</td>");

                } else {

                    out.println(
                        "<td class='status returned'>"
                    );

                    out.println("🟢 Returned");

                    out.println("</td>");
                }

                /*
                 * DUE STATUS
                 */

                out.println("<td class='due-status'>");

                if ("Returned".equalsIgnoreCase(status)) {

                    out.println(
                        "<span class='due-returned'>"
                    );

                    out.println("✓ Returned");

                    out.println("</span>");

                } else if (expectedReturnDate == null) {

                    out.println(
                        "<span class='due-normal'>"
                    );

                    out.println("No date");

                    out.println("</span>");

                } else {

                    LocalDate today =
                        LocalDate.now();

                    LocalDate returnDate =
                        expectedReturnDate.toLocalDate();

                    long daysRemaining =
                        ChronoUnit.DAYS.between(
                            today,
                            returnDate
                        );

                    if (daysRemaining < 0) {

                        long overdueDays =
                            Math.abs(daysRemaining);

                        out.println(
                            "<span class='overdue'>"
                        );

                        out.println(
                            "⚠ Overdue by " +
                            overdueDays +
                            (overdueDays == 1
                                ? " day"
                                : " days")
                        );

                        out.println("</span>");

                    } else if (daysRemaining == 0) {

                        out.println(
                            "<span class='due-soon'>"
                        );

                        out.println("⚠ Due Today");

                        out.println("</span>");

                    } else if (daysRemaining == 1) {

                        out.println(
                            "<span class='due-soon'>"
                        );

                        out.println("⚠ Due Tomorrow");

                        out.println("</span>");

                    } else {

                        out.println(
                            "<span class='due-normal'>"
                        );

                        out.println(
                            "Due in " +
                            daysRemaining +
                            " days"
                        );

                        out.println("</span>");
                    }
                }

                out.println("</td>");

                /*
                 * ACTIONS
                 */

                out.println("<td>");

                /*
                 * EDIT
                 */

                out.println(
                    "<a class='action-btn edit-btn' "
                    + "href='edit-borrow?id="
                    + id
                    + "'>"
                    + "✏ Edit"
                    + "</a>"
                );

                /*
                 * RETURN
                 */

                if ("Borrowed".equalsIgnoreCase(status)) {

                    out.println(
                        "<a class='action-btn return-btn' "
                        + "href='return-borrow?id="
                        + id
                        + "'>"
                        + "↩ Return"
                        + "</a>"
                    );

                } else {

                    out.println(
                        "<span style='color:#16a34a;"
                        + "font-weight:bold;'>"
                        + "Completed"
                        + "</span>"
                    );
                }

                /*
                 * DELETE
                 */

                out.println(
                    "<a class='action-btn delete-btn' "
                    + "href='delete-borrow?id="
                    + id
                    + "' "
                    + "onclick=\"return confirm("
                    + "'Are you sure you want to delete this record?');\">"
                    + "🗑 Delete"
                    + "</a>"
                );

                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            /*
             * NO RECORDS
             */

            if (!found) {

                out.println(
                    "<div class='empty-message'>"
                );

                out.println(
                    "<div class='empty-icon'>📭</div>"
                );

                out.println(
                    "<strong>No records found.</strong>"
                );

                out.println(
                    "<p>Try changing your search or status filter.</p>"
                );

                out.println("</div>");
            }

            out.println("</div>");

            /*
             * BACK BUTTON
             */

            out.println(
                "<a class='back-btn' href='dashboard'>"
                + "← Back to Dashboard"
                + "</a>"
            );

            /*
             * FOOTER
             */

            out.println(
                "<div class='footer'>"
                + "Digital Borrow &amp; Return Tracker"
                + "</div>"
            );

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            /*
             * CLOSE RESOURCES
             */

            resultSet.close();

            statement.close();

            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                "<h2>Error loading borrowed items.</h2>"
            );

            out.println("<p>");

            out.println(e.getMessage());

            out.println("</p>");
        }
    }
}