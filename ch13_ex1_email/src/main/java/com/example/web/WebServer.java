package com.example.web;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.User;
import com.example.UserDB;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class WebServer {
    private static final int DEFAULT_PORT = 8080;
    private int port;
    private HttpServer server;

    public static void main(String[] args) throws IOException {
        WebServer webServer = new WebServer();
        webServer.start();
    }

    public WebServer() {
        // Get port from environment variable PORT (set by Render), default to 8080
        String portStr = System.getenv("PORT");
        this.port = portStr != null ? Integer.parseInt(portStr) : DEFAULT_PORT;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.createContext("/", new HomeHandler());
        server.createContext("/users", new UsersHandler());
        server.createContext("/add", new AddHandler());
        server.createContext("/update", new UpdateHandler());
        server.createContext("/delete", new DeleteHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("Web server is running on port " + port);
        System.out.println("Open http://localhost:" + port + " in your browser");
        System.out.println("Press Ctrl+C to stop the server");
    }

    static class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = getHomeHTML();
            exchange.getResponseHeaders().set("Content-type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, response.getBytes("utf-8").length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("utf-8"));
            os.close();
        }

        private String getHomeHTML() {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Email List Application</title>\n" +
                    "    <style>\n" +
                    "        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n" +
                    "        .container { max-width: 900px; margin: 0 auto; background: white; padding: 20px; border-radius: 5px; }\n" +
                    "        h1 { color: #333; }\n" +
                    "        .menu { margin: 20px 0; }\n" +
                    "        .menu a { display: inline-block; margin: 10px 10px 10px 0; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; }\n" +
                    "        .menu a:hover { background-color: #0056b3; }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h1>Email List Application</h1>\n" +
                    "        <p>Welcome to the Email List Application. Use the menu below to manage users.</p>\n" +
                    "        <div class=\"menu\">\n" +
                    "            <a href=\"/users\">View All Users</a>\n" +
                    "            <a href=\"/add\">Add New User</a>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";
        }
    }

    static class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<User> users = UserDB.getUsers();
            String response = getUsersHTML(users);
            exchange.getResponseHeaders().set("Content-type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, response.getBytes("utf-8").length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("utf-8"));
            os.close();
        }

        private String getUsersHTML(List<User> users) {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html>\n");
            html.append("<head>\n");
            html.append("    <title>All Users</title>\n");
            html.append("    <style>\n");
            html.append("        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n");
            html.append("        .container { max-width: 900px; margin: 0 auto; background: white; padding: 20px; border-radius: 5px; }\n");
            html.append("        h1 { color: #333; }\n");
            html.append("        .back-link { margin-bottom: 20px; }\n");
            html.append("        .back-link a { padding: 10px 20px; background-color: #6c757d; color: white; text-decoration: none; border-radius: 5px; }\n");
            html.append("        .back-link a:hover { background-color: #5a6268; }\n");
            html.append("        table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
            html.append("        table, th, td { border: 1px solid #ddd; }\n");
            html.append("        th { background-color: #007bff; color: white; padding: 12px; text-align: left; }\n");
            html.append("        td { padding: 12px; }\n");
            html.append("        tr:hover { background-color: #f9f9f9; }\n");
            html.append("        .actions { display: flex; gap: 10px; }\n");
            html.append("        .edit-btn { padding: 5px 10px; background-color: #28a745; color: white; text-decoration: none; border-radius: 3px; font-size: 12px; }\n");
            html.append("        .delete-btn { padding: 5px 10px; background-color: #dc3545; color: white; text-decoration: none; border-radius: 3px; font-size: 12px; }\n");
            html.append("        .edit-btn:hover { background-color: #218838; }\n");
            html.append("        .delete-btn:hover { background-color: #c82333; }\n");
            html.append("        .empty { text-align: center; color: #666; padding: 20px; }\n");
            html.append("    </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("    <div class=\"container\">\n");
            html.append("        <h1>All Users</h1>\n");
            html.append("        <div class=\"back-link\"><a href=\"/\">Back to Home</a></div>\n");

            if (users.isEmpty()) {
                html.append("        <div class=\"empty\">No users found. <a href=\"/add\">Add a new user</a></div>\n");
            } else {
                html.append("        <table>\n");
                html.append("            <tr>\n");
                html.append("                <th>User ID</th>\n");
                html.append("                <th>Email</th>\n");
                html.append("                <th>First Name</th>\n");
                html.append("                <th>Last Name</th>\n");
                html.append("                <th>Actions</th>\n");
                html.append("            </tr>\n");

                for (User user : users) {
                    html.append("            <tr>\n");
                    html.append("                <td>").append(user.getUserID()).append("</td>\n");
                    html.append("                <td>").append(user.getEmail()).append("</td>\n");
                    html.append("                <td>").append(user.getFirstName()).append("</td>\n");
                    html.append("                <td>").append(user.getLastName()).append("</td>\n");
                    html.append("                <td>\n");
                    html.append("                    <div class=\"actions\">\n");
                    html.append("                        <a href=\"/update?id=").append(user.getUserID()).append("\" class=\"edit-btn\">Edit</a>\n");
                    html.append("                        <a href=\"/delete?id=").append(user.getUserID()).append("\" class=\"delete-btn\" onclick=\"return confirm('Delete this user?')\">Delete</a>\n");
                    html.append("                    </div>\n");
                    html.append("                </td>\n");
                    html.append("            </tr>\n");
                }

                html.append("        </table>\n");
            }

            html.append("    </div>\n");
            html.append("</body>\n");
            html.append("</html>");

            return html.toString();
        }
    }

    static class AddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            if ("POST".equals(method)) {
                byte[] buffer = new byte[1024];
                int len = exchange.getRequestBody().read(buffer);
                String body = new String(buffer, 0, len);
                String email = getParam(body, "email");
                String firstName = getParam(body, "firstName");
                String lastName = getParam(body, "lastName");

                if (!email.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
                    User user = new User(email, firstName, lastName);
                    UserDB.insert(user);
                    redirect(exchange, "/users");
                    return;
                }
            }

            String response = getAddHTML();
            exchange.getResponseHeaders().set("Content-type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, response.getBytes("utf-8").length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("utf-8"));
            os.close();
        }

        private String getAddHTML() {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Add New User</title>\n" +
                    "    <style>\n" +
                    "        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n" +
                    "        .container { max-width: 500px; margin: 0 auto; background: white; padding: 20px; border-radius: 5px; }\n" +
                    "        h1 { color: #333; }\n" +
                    "        .form-group { margin-bottom: 15px; }\n" +
                    "        label { display: block; margin-bottom: 5px; font-weight: bold; color: #333; }\n" +
                    "        input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 3px; box-sizing: border-box; font-size: 14px; }\n" +
                    "        input:focus { outline: none; border-color: #007bff; box-shadow: 0 0 5px rgba(0,123,255,0.3); }\n" +
                    "        .button-group { margin-top: 20px; display: flex; gap: 10px; }\n" +
                    "        button { padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 14px; font-weight: bold; }\n" +
                    "        .btn-submit { background-color: #007bff; color: white; flex: 1; }\n" +
                    "        .btn-submit:hover { background-color: #0056b3; }\n" +
                    "        .btn-cancel { background-color: #6c757d; color: white; flex: 1; }\n" +
                    "        .btn-cancel:hover { background-color: #5a6268; }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h1>Add New User</h1>\n" +
                    "        <form method=\"POST\">\n" +
                    "            <div class=\"form-group\">\n" +
                    "                <label for=\"email\">Email:</label>\n" +
                    "                <input type=\"email\" id=\"email\" name=\"email\" required>\n" +
                    "            </div>\n" +
                    "            <div class=\"form-group\">\n" +
                    "                <label for=\"firstName\">First Name:</label>\n" +
                    "                <input type=\"text\" id=\"firstName\" name=\"firstName\" required>\n" +
                    "            </div>\n" +
                    "            <div class=\"form-group\">\n" +
                    "                <label for=\"lastName\">Last Name:</label>\n" +
                    "                <input type=\"text\" id=\"lastName\" name=\"lastName\" required>\n" +
                    "            </div>\n" +
                    "            <div class=\"button-group\">\n" +
                    "                <button type=\"submit\" class=\"btn-submit\">Add User</button>\n" +
                    "                <button type=\"button\" class=\"btn-cancel\" onclick=\"window.location='/users'\">Cancel</button>\n" +
                    "            </div>\n" +
                    "        </form>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";
        }

        private String getParam(String body, String param) {
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2 && keyValue[0].equals(param)) {
                    return decode(keyValue[1]);
                }
            }
            return "";
        }

        private String decode(String s) {
            return s.replace("+", " ").replace("%20", " ");
        }
    }

    static class UpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Long userId = null;

            if (query != null) {
                String[] params = query.split("=");
                if (params.length == 2) {
                    userId = Long.parseLong(params[1]);
                }
            }

            if (userId == null) {
                redirect(exchange, "/users");
                return;
            }

            User user = UserDB.getUser(userId);
            if (user == null) {
                redirect(exchange, "/users");
                return;
            }

            String method = exchange.getRequestMethod();
            if ("POST".equals(method)) {
                byte[] buffer = new byte[1024];
                int len = exchange.getRequestBody().read(buffer);
                String body = new String(buffer, 0, len);
                String email = getParam(body, "email");
                String firstName = getParam(body, "firstName");
                String lastName = getParam(body, "lastName");

                if (!email.isEmpty()) user.setEmail(email);
                if (!firstName.isEmpty()) user.setFirstName(firstName);
                if (!lastName.isEmpty()) user.setLastName(lastName);

                UserDB.update(user);
                redirect(exchange, "/users");
                return;
            }

            String response = getUpdateHTML(user);
            exchange.getResponseHeaders().set("Content-type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, response.getBytes("utf-8").length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("utf-8"));
            os.close();
        }

        private String getUpdateHTML(User user) {
            return "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Update User</title>\n" +
                    "    <style>\n" +
                    "        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n" +
                    "        .container { max-width: 500px; margin: 0 auto; background: white; padding: 20px; border-radius: 5px; }\n" +
                    "        h1 { color: #333; }\n" +
                    "        .form-group { margin-bottom: 15px; }\n" +
                    "        label { display: block; margin-bottom: 5px; font-weight: bold; color: #333; }\n" +
                    "        input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 3px; box-sizing: border-box; font-size: 14px; }\n" +
                    "        input:focus { outline: none; border-color: #007bff; box-shadow: 0 0 5px rgba(0,123,255,0.3); }\n" +
                    "        .button-group { margin-top: 20px; display: flex; gap: 10px; }\n" +
                    "        button { padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 14px; font-weight: bold; }\n" +
                    "        .btn-submit { background-color: #28a745; color: white; flex: 1; }\n" +
                    "        .btn-submit:hover { background-color: #218838; }\n" +
                    "        .btn-cancel { background-color: #6c757d; color: white; flex: 1; }\n" +
                    "        .btn-cancel:hover { background-color: #5a6268; }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h1>Update User (ID: " + user.getUserID() + ")</h1>\n" +
                    "        <form method=\"POST\">\n" +
                    "            <div class=\"form-group\">\n" +
                    "                <label for=\"email\">Email:</label>\n" +
                    "                <input type=\"email\" id=\"email\" name=\"email\" value=\"" + user.getEmail() + "\">\n" +
                    "            </div>\n" +
                    "            <div class=\"form-group\">\n" +
                    "                <label for=\"firstName\">First Name:</label>\n" +
                    "                <input type=\"text\" id=\"firstName\" name=\"firstName\" value=\"" + user.getFirstName() + "\">\n" +
                    "            </div>\n" +
                    "            <div class=\"form-group\">\n" +
                    "                <label for=\"lastName\">Last Name:</label>\n" +
                    "                <input type=\"text\" id=\"lastName\" name=\"lastName\" value=\"" + user.getLastName() + "\">\n" +
                    "            </div>\n" +
                    "            <div class=\"button-group\">\n" +
                    "                <button type=\"submit\" class=\"btn-submit\">Update User</button>\n" +
                    "                <button type=\"button\" class=\"btn-cancel\" onclick=\"window.location='/users'\">Cancel</button>\n" +
                    "            </div>\n" +
                    "        </form>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";
        }

        private String getParam(String body, String param) {
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2 && keyValue[0].equals(param)) {
                    return decode(keyValue[1]);
                }
            }
            return "";
        }

        private String decode(String s) {
            return s.replace("+", " ").replace("%20", " ");
        }
    }

    static class DeleteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Long userId = null;

            if (query != null) {
                String[] params = query.split("=");
                if (params.length == 2) {
                    userId = Long.parseLong(params[1]);
                }
            }

            if (userId != null) {
                User user = UserDB.getUser(userId);
                if (user != null) {
                    UserDB.delete(user);
                }
            }

            redirect(exchange, "/users");
        }
    }

    private static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }
}
