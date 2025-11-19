package br.com.coral.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/login")
public class LoginAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final Gson gson = new Gson();

    private static final String USUARIO_FIXO = "admin";
    private static final String SENHA_FIXA = "admin123";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String jsonRequest = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            LoginRequest loginData = gson.fromJson(jsonRequest, LoginRequest.class);

            if (USUARIO_FIXO.equals(loginData.usuario) && SENHA_FIXA.equals(loginData.senha)) {

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(new LoginResponse(true, "Login bem-sucedido.")));

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(gson.toJson(new LoginResponse(false, "Credenciais inválidas.")));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new LoginResponse(false, "Erro interno: " + e.getMessage())));
        }
    }

    private static class LoginRequest {
        String usuario;
        String senha;
    }

    private static class LoginResponse {
        boolean sucesso;
        String mensagem;

        public LoginResponse(boolean sucesso, String mensagem) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
        }
    }
}