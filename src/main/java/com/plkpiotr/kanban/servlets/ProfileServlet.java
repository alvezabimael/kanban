package com.plkpiotr.kanban.servlets;

import com.plkpiotr.kanban.dao.EmployeeDAO;
import com.plkpiotr.kanban.domain.Employee;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Serves view of a profile.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("employee") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setCharacterEncoding("UTF-8");
            EmployeeDAO employeeDAO = (EmployeeDAO) request.getAttribute("employeeDAO");
            Employee employee = (Employee) session.getAttribute("employee");

            List allTasks = employeeDAO.getAllTasks(employee.getId());
            List todoTasks = employeeDAO.getTasksByCategory(allTasks, "todo");
            List doingTasks = employeeDAO.getTasksByCategory(allTasks, "doing");
            List doneTasks = employeeDAO.getTasksByCategory(allTasks, "done");

            int todoPercent = 0;
            int doingPercent = 0;
            int donePercent = 0;

            if (allTasks.size() != 0) {
                todoPercent = 100 * todoTasks.size() / allTasks.size();
                doingPercent = 100 * doingTasks.size() / allTasks.size();
                donePercent = 100 * doneTasks.size() / allTasks.size();
            }

            request.setAttribute("allTasks", allTasks);
            request.setAttribute("todoTasks", todoTasks);
            request.setAttribute("doingTasks", doingTasks);
            request.setAttribute("doneTasks", doneTasks);

            request.setAttribute("todoPercent", todoPercent);
            request.setAttribute("doingPercent", doingPercent);
            request.setAttribute("donePercent", donePercent);

            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
        }
    }
}
