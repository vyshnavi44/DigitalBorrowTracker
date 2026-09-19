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

@WebServlet("/return-borrow")
public class ReturnBorrowServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Check login
        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        int userId =
                (Integer) session.getAttribute("userId");

        // Get record ID
        String id = request.getParameter("id");

        if (id == null || id.trim().isEmpty()) {

            showMessage(
                response,
                "Invalid Borrow Record",
                "The borrow record ID is missing.",
                "view-borrowed"
            );

            return;
        }

        String sql =
                "UPDATE borrow_records "
                + "SET status = 'Returned', "
                + "actual_return_date = CURDATE() "
                + "WHERE id = ? "
                + "AND user_id = ? "
                + "AND status = 'Borrowed'";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            int recordId =
                    Integer.parseInt(id);

            statement.setInt(1, recordId);
            statement.setInt(2, userId);

            int rowsUpdated =
                    statement.executeUpdate();

            statement.close();
            connection.close();

            if (rowsUpdated > 0) {

                showMessage(
                    response,
                    "Item Returned Successfully!",
                    "The item has been marked as returned and today's date has been recorded.",
                    "view-borrowed"
                );

            } else {

                showMessage(
                    response,
                    "Unable to Return Item",
                    "The record was not found, does not belong to your account, or has already been returned.",
                    "view-borrowed"
                );
            }

        } catch (NumberFormatException e) {

            showMessage(
                response,
                "Invalid Record ID",
                "The record ID is not valid.",
                "view-borrowed"
            );

        } catch (Exception e) {

            e.printStackTrace();

            showMessage(
                response,
                "Error While Returning Item",
                e.getMessage(),
                "view-borrowed"
            );
        }
    }

    private void showMessage(HttpServletResponse response,
                             String title,
                             String message,
                             String backLink)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        response.getWriter().println(
            "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<meta charset='UTF-8'>"
            + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
            + "<title>" + title + "</title>"

            + "<style>"

            + "body{"
            + "font-family:Arial,sans-serif;"
            + "background:linear-gradient(135deg,#eef5ff,#f8fbff);"
            + "margin:0;"
            + "min-height:100vh;"
            + "display:flex;"
            + "align-items:center;"
            + "justify-content:center;"
            + "padding:20px;"
            + "}"

            + ".card{"
            + "background:white;"
            + "width:100%;"
            + "max-width:500px;"
            + "padding:40px 35px;"
            + "border-radius:18px;"
            + "text-align:center;"
            + "box-shadow:0 15px 40px rgba(0,0,0,0.12);"
            + "}"

            + ".icon{"
            + "width:75px;"
            + "height:75px;"
            + "background:#dcfce7;"
            + "color:#16a34a;"
            + "border-radius:50%;"
            + "display:flex;"
            + "align-items:center;"
            + "justify-content:center;"
            + "margin:0 auto 22px;"
            + "font-size:40px;"
            + "font-weight:bold;"
            + "}"

            + "h1{"
            + "color:#111827;"
            + "font-size:27px;"
            + "margin-bottom:12px;"
            + "}"

            + "p{"
            + "color:#6b7280;"
            + "font-size:16px;"
            + "line-height:1.6;"
            + "margin-bottom:25px;"
            + "}"

            + ".back-btn{"
            + "display:inline-block;"
            + "background:#2563eb;"
            + "color:white;"
            + "text-decoration:none;"
            + "padding:13px 25px;"
            + "border-radius:8px;"
            + "font-weight:bold;"
            + "}"

            + ".back-btn:hover{"
            + "background:#1d4ed8;"
            + "}"

            + ".brand{"
            + "margin-top:25px;"
            + "font-size:13px;"
            + "color:#9ca3af;"
            + "}"

            + "</style>"
            + "</head>"

            + "<body>"

            + "<div class='card'>"

            + "<div class='icon'>✓</div>"

            + "<h1>" + title + "</h1>"

            + "<p>" + message + "</p>"

            + "<a href='" + backLink + "' class='back-btn'>"
            + "Back to Borrowed Items"
            + "</a>"

            + "<div class='brand'>"
            + "Digital Borrow &amp; Return Tracker"
            + "</div>"

            + "</div>"

            + "</body>"
            + "</html>"
        );
    }
}