package br.com.coral.dao;

import br.com.coral.model.Apresentacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional;

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

    public void deletar(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            Apresentacao apresentacao = em.find(Apresentacao.class, id);

            if (apresentacao != null) {
                em.remove(apresentacao);
                em.getTransaction().commit();
                System.out.println("✅ Apresentação ID " + id + " deletada com sucesso.");
            } else {
                System.out.println("⚠️ Apresentação ID " + id + " não encontrada para deleção.");
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erro ao deletar apresentação ID " + id + ": " + e.getMessage());

            throw new RuntimeException("Erro de persistência ao deletar", e);
        } finally {
            em.close();
        }
    }

    public Optional<Apresentacao> buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        Apresentacao apresentacao = em.find(Apresentacao.class, id);
        em.close();
        return Optional.ofNullable(apresentacao);
    }

    public void atualizar(Apresentacao apresentacao) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            em.merge(apresentacao);
            em.getTransaction().commit();
            System.out.println("✅ Apresentação ID " + apresentacao.getId() + " atualizada com sucesso.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erro ao atualizar apresentação ID " + apresentacao.getId() + ": " + e.getMessage());
            throw new RuntimeException("Erro de persistência ao atualizar", e);
        } finally {
            em.close();
        }
    }
}