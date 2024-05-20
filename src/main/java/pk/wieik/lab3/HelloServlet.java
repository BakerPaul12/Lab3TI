package pk.wieik.lab3;

import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "pageServlet", urlPatterns = "/")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        URLdelivery(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loginDelivery(request, response);
    }

    private void loginDelivery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("login");
        String password = request.getParameter("password");
        String logout = request.getParameter("logout");

        if (isValidAdmin(username, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("isAdmin", true);
            session.setMaxInactiveInterval(30 * 60);
            response.sendRedirect("Logged.html");
            System.out.println("Admin logged in");
        } else if (isValidUser(username, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setMaxInactiveInterval(30 * 60);
            response.sendRedirect("Logged.html");
            System.out.println("User logged in");
        } else if (logout != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("index.html");
        } else {
            response.sendRedirect("index.html?error=true");
        }
    }

    private boolean isValidAdmin(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }

    private boolean isValidUser(String username, String password) {
        return "user".equals(username) && "user".equals(password);
    }

    private void URLdelivery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String uri = request.getRequestURI();
        String page = "/WEB-INF/jsp/index.jsp";
        if (uri.endsWith("calculator.html")) {
            page = "/WEB-INF/jsp/calculator.jsp";
        } else if (uri.endsWith("Logged.html")) {
            page = "/WEB-INF/jsp/logged.jsp";
        } else if (uri.endsWith("userpage.html")) {
            page = "/WEB-INF/jsp/userpage.jsp";
        }

        String cssLink = determineCssLink(request, response);
        String username = getLoggedUserName(request);
        String dynamicContentArticle = getDynamicContent();
        String errorParam = request.getParameter("error");
        String dynamicNotLogged = getString(errorParam);
        String dynamicMenuLogged = getDynamicMenuLogged();

        request.setAttribute("css", cssLink);
        request.setAttribute("USER", username);
        request.setAttribute("CONTENT", dynamicContentArticle);

        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            request.setAttribute("MENU", dynamicMenuLogged);
        } else {
            request.setAttribute("MENU", dynamicNotLogged);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }

    private String determineCssLink(HttpServletRequest request, HttpServletResponse response) {
        String themeParam = request.getParameter("theme");
        HttpSession session = request.getSession();
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");

        if (isAdmin != null && isAdmin) {
            if ("dark-theme".equals(themeParam)) {
                session.setAttribute("theme", "dark-theme");
                setCssCookie(response, "dark-theme");
                return "<link rel=\"stylesheet\" href=\"css/darkcss.css\">";
            } else if ("white-theme".equals(themeParam)) {
                session.setAttribute("theme", "white-theme");
                setCssCookie(response, "white-theme");
                return "<link rel=\"stylesheet\" href=\"css/styles.css\">";
            }
        }

        String sessionTheme = (String) session.getAttribute("theme");
        if (sessionTheme == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("theme")) {
                        sessionTheme = cookie.getValue();
                        session.setAttribute("theme", sessionTheme);
                        break;
                    }
                }
            }
        }

        if ("dark-theme".equals(sessionTheme)) {
            return "<link rel=\"stylesheet\" href=\"css/darkcss.css\">";
        } else {
            return "<link rel=\"stylesheet\" href=\"css/styles.css\">";
        }
    }

    private void setCssCookie(HttpServletResponse response, String theme) {
        Cookie cookie = new Cookie("theme", theme);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static String getString(String errorParam) {
        String dynamicNotLogged = "<div class=\"button-menu\">\n" +
                "    <label for=\"checkboxMenu\" class=\"checkbox-image\">\n" +
                "        <img src=\"images/menuButton.png\" alt=\"Checkbox\" class=\"imageMenu\">\n" +
                "    </label>\n" +
                "    <input type=\"checkbox\" id=\"checkboxMenu\" style=\"visibility: hidden;\">\n" +
                "    <div class=\"button-container\">\n" +
                "        <a href=\"index.html\" class=\"button\">Home</a>\n" +
                "        <a href=\"calculator.html\" class=\"button\">Calculator</a>\n" +
                "        <a href=\"http://google.pl\" class=\"button\">Google</a>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<div class=\"loginpanel\">\n" +
                "    <form action=\"PageServlet\" method=\"post\">\n" +
                "        Login:\n" +
                "        <input type=\"text\" name=\"login\">\n" +
                "        Password:\n" +
                "        <input type=\"password\" name=\"password\">\n" +
                "        <button type=\"submit\">Login</button>\n" +
                "    </form>\n";
        if (errorParam != null && errorParam.equals("true")) {
            dynamicNotLogged += "<div style=\"color: red;\">Login FAILED</div>";
        }
        dynamicNotLogged += "</div>";
        return dynamicNotLogged;
    }

    private String getLoggedUserName(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        return username != null ? username : "Guest";
    }

    private String getDynamicContent() {
        return "<article>\n" +
                "                <header>\n" +
                "                    <div><b>Artykuł 1</b></div>\n" +
                "                </header>\n" +
                "                <p>Morbi vitae sem eu urna fringilla fermentum mollis vitae eros. In porttitor laoreet ligula, elementum iaculis sapien. Vestibulum nulla magna, lacinia eget sollicitudin vel, iaculis dignissim dolor. Nulla facilisi. Donec hendrerit feugiat libero. Quisque leo eros, vestibulum nec lorem quis, malesuada varius odio. Sed suscipit vestibulum diam, sit amet facilisis dolor consectetur in. Quisque accumsan consectetur dui id cursus. Nulla nec sapien placerat, tincidunt nisi sed, egestas orci. Nullam lacus ligula, maximus sed vulputate at, feugiat ut metus. Nunc quis ullamcorper ante. Cras ex enim, fermentum pharetra lacus ut, pulvinar dapibus sapien.</p>\n" +
                "            </article>\n" +
                "            <article>\n" +
                "                <header>\n" +
                "                    <div><b>Artykuł 2</b></div>\n" +
                "                </header>\n" +
                "                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur quis ligula finibus risus ultrices semper sit amet vel lectus. Aliquam suscipit metus ac tortor placerat luctus. Curabitur nec diam odio. Aliquam erat volutpat. Etiam auctor tortor cursus condimentum efficitur. Cras vulputate vitae ex iaculis pharetra. Curabitur ut augue nunc. Suspendisse porta cursus odio, ac venenatis lorem tempus in. Fusce semper ultricies felis, ac pharetra ante. Vivamus ornare nisl nisl, a efficitur felis feugiat sed. Fusce in eros laoreet, suscipit enim non, vestibulum mi. Ut imperdiet diam lacus, ut suscipit nisi hendrerit tincidunt. Fusce ex justo, posuere ac sapien aliquam, feugiat placerat lorem. Cras malesuada nulla pulvinar dolor laoreet, eget consectetur ipsum accumsan. Nunc fermentum augue accumsan, ultrices enim id, rhoncus arcu.</p>\n" +
                "            </article>\n" +
                "            <article>\n" +
                "                <header>\n" +
                "                    <div><b>Artykuł 3</b></div>\n" +
                "                </header>\n" +
                "                <p>Fusce dictum sem ac diam viverra, sit amet consectetur mi volutpat. Fusce sollicitudin quam ac eleifend vestibulum. Proin vel lacus fringilla, malesuada lectus ut, semper libero. Curabitur bibendum justo nisl, ut blandit dolor ultricies vitae. Mauris ullamcorper, turpis sed malesuada finibus, purus dui convallis velit, sed vulputate elit quam ut sem. In eleifend, sapien ut maximus pulvinar, lectus urna aliquet turpis, vel vulputate erat risus at neque. Nunc imperdiet mi sed ex maximus posuere. Quisque commodo lacus ut commodo blandit. Sed tincidunt sem et dui dignissim, non dignissim velit tempor. Pellentesque vitae sodales nisi, a gravida turpis. Aliquam at erat pretium, fringilla erat eget, tristique odio. Vestibulum eget nunc vitae dui convallis mollis.</p>\n" +
                "            </article>";
    }

    private String getDynamicMenuLogged() {
        return "<div class=\"button-menu\">\n" +
                "    <label for=\"checkboxMenu\" class=\"checkbox-image\">\n" +
                "        <img src=\"images/menuButton.png\" alt=\"Checkbox\" class=\"imageMenu\">\n" +
                "    </label>\n" +
                "    <input type=\"checkbox\" id=\"checkboxMenu\" style=\"visibility: hidden;\">\n" +
                "    <div class=\"button-container\">\n" +
                "        <a href=\"Logged.html\" class=\"button\">User Home</a>\n" +
                "        <a href=\"index.html\" class=\"button\">Home</a>\n" +
                "        <a href=\"calculator.html\" class=\"button\">Calculator</a>\n" +
                "        <a href=\"http://google.pl\" class=\"button\">Google</a>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<div class=\"loginpanel\">\n" +
                "    <form action=\"PageServlet\" method=\"post\">\n" +
                "        <button type=\"Logout\" name=\"logout\" id=\"logout\">Logout</button>\n" +
                "    </form>\n" +
                "</div>";
    }
}
