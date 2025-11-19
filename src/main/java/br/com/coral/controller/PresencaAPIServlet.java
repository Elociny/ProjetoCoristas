package br.com.coral.controller;

import br.com.coral.service.CoristaService;
import com.google.gson.Gson;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/presenca")
public class PresencaAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final CoristaService coristaService = new CoristaService();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("coristaId");

        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse("ID do corista ausente para validação.")));
            return;
        }

        try {
            int coristaId = Integer.parseInt(idStr);

            boolean apto = coristaService.podeSeApresentar(coristaId);

            String status = apto ? "APTO" : "IMPEDIDO";

            response.getWriter().write(gson.toJson(new ValidationResponse(status, apto, coristaId)));

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Erro ao validar aptidão: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("coristaId");
        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int coristaId = Integer.parseInt(idStr);
            coristaService.registrarFalta(coristaId);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(new SimpleMessage("Falta registrada com sucesso para o ID " + coristaId)));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Erro ao registrar falta: " + e.getMessage())));
        }
    }

    private static class ErrorResponse {
        String erro;
        public ErrorResponse(String erro) { this.erro = erro; }
    }
    private static class ValidationResponse {
        String status;
        boolean apto;
        int id;
        public ValidationResponse(String status, boolean apto, int id) {
            this.status = status; this.apto = apto; this.id = id;
        }
    }
    private static class SimpleMessage {
        String mensagem;
        public SimpleMessage(String mensagem) { this.mensagem = mensagem; }
    }
}