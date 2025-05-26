package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MyServletTest {

    private MyServlet myServlet;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() {
        // Initialize mocks annotated with @Mock
        MockitoAnnotations.openMocks(this);
        myServlet = new MyServlet();

        // Configure mock RequestDispatcher to be returned
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    @DisplayName("Should set greeting message for a valid name")
    void testDoPostWithValidName() throws ServletException, IOException {
        // Mock the request parameter
        when(request.getParameter("userName")).thenReturn("Alice");

        // Call the doPost method
        myServlet.doPost(request, response);

        // Verify that setAttribute was called with the correct message
        verify(request, times(1)).setAttribute("greetingMessage", "Hello, Alice!");

        // Verify that the request was forwarded to result.jsp
        verify(request, times(1)).getRequestDispatcher("result.jsp");
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    @DisplayName("Should set greeting message to Guest for an empty name")
    void testDoPostWithEmptyName() throws ServletException, IOException {
        when(request.getParameter("userName")).thenReturn(""); // Empty string
        myServlet.doPost(request, response);
        verify(request, times(1)).setAttribute("greetingMessage", "Hello, Guest!");
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    @DisplayName("Should set greeting message to Guest for null name")
    void testDoPostWithNullName() throws ServletException, IOException {
        when(request.getParameter("userName")).thenReturn(null); // Null
        myServlet.doPost(request, response);
        verify(request, times(1)).setAttribute("greetingMessage", "Hello, Guest!");
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    @DisplayName("Should redirect to index.jsp for GET requests")
    void testDoGetRedirectsToIndex() throws ServletException, IOException {
        myServlet.doGet(request, response);

        // Verify that sendRedirect was called with index.jsp
        verify(response, times(1)).sendRedirect("index.jsp");
        // Ensure no forwarding happened for GET
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
