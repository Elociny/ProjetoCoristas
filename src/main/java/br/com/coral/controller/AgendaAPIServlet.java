package br.com.coral.controller;

import br.com.coral.dao.ApresentacaoDAO;
import br.com.coral.model.Apresentacao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/agenda")
public class AgendaAPIServlet extends HttpServlet {
    private final ApresentacaoDAO agendaDAO = new ApresentacaoDAO();

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Apresentacao> apresentacoes = agendaDAO.listarTodos();
            response.getWriter().write(gson.toJson(apresentacoes));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Erro ao listar agenda: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String jsonRequest = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

            Apresentacao novaApresentacao = gson.fromJson(jsonRequest, Apresentacao.class);

            // FUTURAMENTE: Inserir a lógica de CoristaService aqui para validar o agendamento

            agendaDAO.inserir(novaApresentacao);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(novaApresentacao));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Falha ao agendar: " + e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String jsonRequest = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Apresentacao apresentacaoAtualizada = gson.fromJson(jsonRequest, Apresentacao.class);

            if (apresentacaoAtualizada.getId() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(new ErrorResponse("ID da apresentação ausente na requisição de atualização.")));
                return;
            }

            agendaDAO.atualizar(apresentacaoAtualizada);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(apresentacaoAtualizada));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Falha ao atualizar a apresentação: " + e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            response.getWriter().write(gson.toJson(new ErrorResponse("ID da apresentação ausente.")));
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            agendaDAO.deletar(id);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(new ErrorResponse("ID inválido.")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(new ErrorResponse("Falha ao deletar apresentação: " + e.getMessage())));
        }
    }

    private static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(FORMATTER.format(value));
            }
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                return LocalDateTime.parse(in.nextString(), FORMATTER);
            }
        }
    }

    private static class ErrorResponse {
        String erro;
        public ErrorResponse(String erro) {
            this.erro = erro;
        }
    }
}