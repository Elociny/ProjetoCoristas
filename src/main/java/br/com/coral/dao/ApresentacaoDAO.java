package br.com.coral.dao;

import br.com.coral.model.Apresentacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class ApresentacaoDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("coristas-unit");

    public void inserir(Apresentacao apresentacao) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(apresentacao);
            em.getTransaction().commit();
            System.out.println("✅ Apresentação '" + apresentacao.getTitulo() + "' agendada com sucesso!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erro ao agendar apresentação: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Apresentacao> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<Apresentacao> apresentacoes = null;
        try {
            apresentacoes = em.createQuery("SELECT a FROM Apresentacao a ORDER BY a.dataHora", Apresentacao.class).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar apresentações: " + e.getMessage());
        } finally {
            em.close();
        }
        return apresentacoes;
    }
}