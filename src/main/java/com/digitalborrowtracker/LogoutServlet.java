
package com.digitalborrowtracker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Get the current login session
        HttpSession session = request.getSession(false);

        // Destroy the session if it exists
        if (session != null) {
            session.invalidate();
        }

        // Send the user back to the login page
        response.sendRedirect("login.html");
    }
}

