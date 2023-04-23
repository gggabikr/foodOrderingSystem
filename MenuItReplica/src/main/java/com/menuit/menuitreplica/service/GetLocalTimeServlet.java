package com.menuit.menuitreplica.service;

import java.io.IOException;
import java.time.LocalTime;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetLocalTimeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the time input value from the request
        String timeInput = request.getParameter("localTimeInput");

        // Parse the time input value into a LocalTime object
        LocalTime localTime = LocalTime.parse(timeInput);

        // Get the local time in the desired format
        String formattedLocalTime = localTime.toString();

        // Send the formatted local time as the response
        response.setContentType("text/plain");
        response.getWriter().write("Local Time: " + formattedLocalTime);
    }
}
