package com.digitalborrowtracker;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (email != null) {
            email = email.trim();
        }

        if (username != null) {
            username = username.trim();
        }

        /*
         * Hash the password entered by the user.
         * The database already contains the hashed password.
         */
        String hashedPassword = PasswordUtil.hashPassword(password);

        String sql =
                "SELECT id, username, email FROM users "
                + "WHERE email = ? AND username = ? AND password = ?";

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, email);
            statement.setString(2, username);
            statement.setString(3, hashedPassword);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                // Create login session
                HttpSession session =
                        request.getSession();

                session.setAttribute(
                        "userId",
                        resultSet.getInt("id")
                );

                session.setAttribute(
                        "username",
                        resultSet.getString("username")
                );

                session.setAttribute(
                        "email",
                        resultSet.getString("email")
                );

                /*
                 * Send login email through EmailJS.
                 * If email sending fails, login still continues.
                 */
                try {

                    EmailService.sendLoginEmail(
                            resultSet.getString("email"),
                            resultSet.getString("username")
                    );

                } catch (Exception emailException) {

                    emailException.printStackTrace();
                }

                // Login successful
                response.sendRedirect("dashboard");

            } else {

                // Login failed
                response.setContentType("text/html;charset=UTF-8");

                response.getWriter().println(
                    "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Login Failed</title>"
                    + "<style>"
                    + "body{"
                    + "font-family:Arial,sans-serif;"
                    + "background:linear-gradient(135deg,#eef5ff,#f8fbff);"
                    + "display:flex;"
                    + "justify-content:center;"
                    + "align-items:center;"
                    + "min-height:100vh;"
                    + "margin:0;"
                    + "padding:20px;"
                    + "}"
                    + ".card{"
                    + "background:white;"
                    + "width:100%;"
                    + "max-width:450px;"
                    + "padding:40px;"
                    + "text-align:center;"
                    + "border-radius:16px;"
                    + "box-shadow:0 10px 30px rgba(0,0,0,0.12);"
                    + "}"
                    + ".icon{"
                    + "font-size:50px;"
                    + "margin-bottom:15px;"
                    + "}"
                    + "h2{"
                    + "color:#dc3545;"
                    + "margin-bottom:15px;"
                    + "}"
                    + "p{"
                    + "color:#666;"
                    + "font-size:16px;"
                    + "}"
                    + ".button{"
                    + "display:inline-block;"
                    + "margin-top:20px;"
                    + "padding:12px 25px;"
                    + "background:#2563eb;"
                    + "color:white;"
                    + "text-decoration:none;"
                    + "border-radius:8px;"
                    + "font-weight:bold;"
                    + "}"
                    + ".button:hover{"
                    + "background:#1d4ed8;"
                    + "}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='card'>"
                    + "<div class='icon'>⚠️</div>"
                    + "<h2>Login Failed</h2>"
                    + "<p>Invalid email, username or password.</p>"
                    + "<p>Please check your login details and try again.</p>"
                    + "<a href='login.html' class='button'>Back to Login</a>"
                    + "</div>"
                    + "</body>"
                    + "</html>"
                );
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                "<h2>Error while logging in.</h2>"
            );

            response.getWriter().println(
                "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}