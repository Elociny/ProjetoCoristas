package br.com.coral.controller;

import br.com.coral.dao.CoristaDAO;
import br.com.coral.model.Corista;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/coristas")
public class CoristaAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final CoristaDAO coristaDAO = new CoristaDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");

        try {
            String coristasJson;

            if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);

                Optional<Corista> coristaOpt = coristaDAO.buscarPorId(id);

                if (coristaOpt.isPresent()) {
                    coristasJson = gson.toJson(new Corista[]{coristaOpt.get()});
                } else {
                    coristasJson = "[]";
                }

            } else {
                List<Corista> coristas = coristaDAO.listarTodos();
                coristasJson = gson.toJson(coristas);
            }

            response.getWriter().write(coristasJson);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse("ID inválido para busca.")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Erro ao buscar coristas: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            request.setCharacterEncoding("UTF-8");

            String jsonRequest = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            Corista novoCorista = gson.fromJson(jsonRequest, Corista.class);

            coristaDAO.inserir(novoCorista);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(novoCorista));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Falha ao cadastrar corista: " + e.getMessage())));
        }
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse("ID do corista ausente (400).")));
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            coristaDAO.deletar(id);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse("ID inválido (Não é número).")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Falha no servidor ao deletar: " + e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String jsonRequest = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            Corista coristaAtualizado = gson.fromJson(jsonRequest, Corista.class);

            if (coristaAtualizado.getId() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new ErrorResponse("ID do corista ausente na requisição de atualização.")));
                return;
            }

            coristaDAO.atualizar(coristaAtualizado);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(coristaAtualizado));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Falha ao atualizar corista: " + e.getMessage())));
        }
    }

    private static class ErrorResponse {
        String erro;

        public ErrorResponse(String erro) {
            this.erro = erro;
        }
    }
}