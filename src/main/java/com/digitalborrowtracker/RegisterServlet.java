package com.digitalborrowtracker;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Remove accidental spaces
        if (fullName != null) {
            fullName = fullName.trim();
        }

        if (email != null) {
            email = email.trim();
        }

        if (username != null) {
            username = username.trim();
        }

        response.setContentType("text/html;charset=UTF-8");

        try {

            // Check whether passwords match
            if (password == null || confirmPassword == null
                    || !password.equals(confirmPassword)) {

                response.getWriter().println(
                    "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Registration Error</title>"
                    + "<style>"
                    + "body{"
                    + "font-family:Arial,sans-serif;"
                    + "background:#f4f7fb;"
                    + "display:flex;"
                    + "justify-content:center;"
                    + "align-items:center;"
                    + "min-height:100vh;"
                    + "margin:0;"
                    + "}"
                    + ".card{"
                    + "background:white;"
                    + "width:90%;"
                    + "max-width:450px;"
                    + "padding:40px;"
                    + "text-align:center;"
                    + "border-radius:15px;"
                    + "box-shadow:0 10px 30px rgba(0,0,0,0.12);"
                    + "}"
                    + ".icon{"
                    + "font-size:50px;"
                    + "margin-bottom:15px;"
                    + "}"
                    + "h2{color:#dc3545;margin-bottom:15px;}"
                    + "p{color:#666;font-size:16px;}"
                    + ".button{"
                    + "display:inline-block;"
                    + "margin-top:20px;"
                    + "padding:12px 25px;"
                    + "background:#007bff;"
                    + "color:white;"
                    + "text-decoration:none;"
                    + "border-radius:8px;"
                    + "font-weight:bold;"
                    + "}"
                    + ".button:hover{background:#0056b3;}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='card'>"
                    + "<div class='icon'>⚠️</div>"
                    + "<h2>Passwords Do Not Match</h2>"
                    + "<p>Please make sure both passwords are the same.</p>"
                    + "<a href='register.html' class='button'>Back to Registration</a>"
                    + "</div>"
                    + "</body>"
                    + "</html>"
                );

                return;
            }

            /*
             * IMPORTANT:
             * Hash the password only AFTER checking that both
             * passwords match.
             */
            String hashedPassword = PasswordUtil.hashPassword(password);

            Connection connection = DatabaseConnection.getConnection();

            // Check whether username already exists
            String checkUsernameSql =
                    "SELECT id FROM users WHERE username = ?";

            PreparedStatement usernameStatement =
                    connection.prepareStatement(checkUsernameSql);

            usernameStatement.setString(1, username);

            ResultSet usernameResult =
                    usernameStatement.executeQuery();

            if (usernameResult.next()) {

                usernameResult.close();
                usernameStatement.close();
                connection.close();

                response.getWriter().println(
                    "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Registration Error</title>"
                    + "<style>"
                    + "body{font-family:Arial,sans-serif;background:#f4f7fb;"
                    + "display:flex;justify-content:center;align-items:center;"
                    + "min-height:100vh;margin:0;}"
                    + ".card{background:white;width:90%;max-width:450px;"
                    + "padding:40px;text-align:center;border-radius:15px;"
                    + "box-shadow:0 10px 30px rgba(0,0,0,0.12);}"
                    + ".icon{font-size:50px;margin-bottom:15px;}"
                    + "h2{color:#dc3545;margin-bottom:15px;}"
                    + "p{color:#666;font-size:16px;}"
                    + ".button{display:inline-block;margin-top:20px;"
                    + "padding:12px 25px;background:#007bff;color:white;"
                    + "text-decoration:none;border-radius:8px;font-weight:bold;}"
                    + ".button:hover{background:#0056b3;}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='card'>"
                    + "<div class='icon'>⚠️</div>"
                    + "<h2>Username Already Exists</h2>"
                    + "<p>Please choose another username.</p>"
                    + "<a href='register.html' class='button'>Back to Registration</a>"
                    + "</div>"
                    + "</body>"
                    + "</html>"
                );

                return;
            }

            usernameResult.close();
            usernameStatement.close();

            // Check whether email already exists
            String checkEmailSql =
                    "SELECT id FROM users WHERE email = ?";

            PreparedStatement emailStatement =
                    connection.prepareStatement(checkEmailSql);

            emailStatement.setString(1, email);

            ResultSet emailResult =
                    emailStatement.executeQuery();

            if (emailResult.next()) {

                emailResult.close();
                emailStatement.close();
                connection.close();

                response.getWriter().println(
                    "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Registration Error</title>"
                    + "<style>"
                    + "body{font-family:Arial,sans-serif;background:#f4f7fb;"
                    + "display:flex;justify-content:center;align-items:center;"
                    + "min-height:100vh;margin:0;}"
                    + ".card{background:white;width:90%;max-width:450px;"
                    + "padding:40px;text-align:center;border-radius:15px;"
                    + "box-shadow:0 10px 30px rgba(0,0,0,0.12);}"
                    + ".icon{font-size:50px;margin-bottom:15px;}"
                    + "h2{color:#dc3545;margin-bottom:15px;}"
                    + "p{color:#666;font-size:16px;}"
                    + ".button{display:inline-block;margin-top:20px;"
                    + "padding:12px 25px;background:#007bff;color:white;"
                    + "text-decoration:none;border-radius:8px;font-weight:bold;}"
                    + ".button:hover{background:#0056b3;}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='card'>"
                    + "<div class='icon'>⚠️</div>"
                    + "<h2>Email Already Registered</h2>"
                    + "<p>Please use a different email address.</p>"
                    + "<a href='register.html' class='button'>Back to Registration</a>"
                    + "</div>"
                    + "</body>"
                    + "</html>"
                );

                return;
            }

            emailResult.close();
            emailStatement.close();

            // Insert new user
            String insertSql =
                    "INSERT INTO users (username, password, email) "
                    + "VALUES (?, ?, ?)";

            PreparedStatement insertStatement =
                    connection.prepareStatement(insertSql);

            insertStatement.setString(1, username);

            // Store HASHED password instead of original password
            insertStatement.setString(2, hashedPassword);

            insertStatement.setString(3, email);

            insertStatement.executeUpdate();

            insertStatement.close();
            connection.close();

            // Registration successful
            response.getWriter().println(
                "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>Registration Successful</title>"
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
                + ".success-card{"
                + "background:white;"
                + "width:100%;"
                + "max-width:500px;"
                + "padding:45px 35px;"
                + "text-align:center;"
                + "border-radius:18px;"
                + "box-shadow:0 15px 40px rgba(0,0,0,0.12);"
                + "}"
                + ".success-icon{"
                + "width:75px;"
                + "height:75px;"
                + "background:#28a745;"
                + "color:white;"
                + "border-radius:50%;"
                + "display:flex;"
                + "justify-content:center;"
                + "align-items:center;"
                + "margin:0 auto 25px;"
                + "font-size:45px;"
                + "font-weight:bold;"
                + "}"
                + "h1{"
                + "color:#222;"
                + "font-size:28px;"
                + "margin-bottom:15px;"
                + "}"
                + ".welcome{"
                + "font-size:19px;"
                + "font-weight:bold;"
                + "color:#333;"
                + "margin-bottom:10px;"
                + "}"
                + ".message{"
                + "font-size:16px;"
                + "color:#666;"
                + "line-height:1.6;"
                + "}"
                + ".login-button{"
                + "display:inline-block;"
                + "margin-top:25px;"
                + "padding:13px 30px;"
                + "background:#007bff;"
                + "color:white;"
                + "text-decoration:none;"
                + "border-radius:8px;"
                + "font-size:16px;"
                + "font-weight:bold;"
                + "transition:0.3s;"
                + "}"
                + ".login-button:hover{"
                + "background:#0056b3;"
                + "transform:translateY(-2px);"
                + "}"
                + ".brand{"
                + "margin-top:25px;"
                + "font-size:14px;"
                + "color:#999;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='success-card'>"
                + "<div class='success-icon'>✓</div>"
                + "<h1>Account Created Successfully!</h1>"
                + "<p class='welcome'>Welcome, "
                + fullName
                + "!</p>"
                + "<p class='message'>"
                + "Your account has been registered successfully."
                + "<br>"
                + "You can now log in and start using the Digital Borrow & Return Tracker."
                + "</p>"
                + "<a href='login.html' class='login-button'>"
                + "Go to Login"
                + "</a>"
                + "<p class='brand'>"
                + "Digital Borrow & Return Tracker"
                + "</p>"
                + "</div>"
                + "</body>"
                + "</html>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Error while creating account.</h2>"
            );

            response.getWriter().println(
                "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}