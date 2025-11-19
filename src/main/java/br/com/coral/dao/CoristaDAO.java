package br.com.coral.dao;

import br.com.coral.model.Corista;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Optional; // Usado para buscarPorId

public class CoristaDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("coristas-unit");

    public void inserir(Corista corista) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            em.persist(corista);
            em.getTransaction().commit();
            System.out.println("✅ Corista " + corista.getNome() + " inserido com sucesso! ID: " + corista.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erro ao inserir corista: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Corista> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<Corista> coristas = null;

        try {
            coristas = em.createQuery("SELECT c FROM Corista c", Corista.class).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar coristas: " + e.getMessage());
        } finally {
            em.close();
        }
        return coristas;
    }

    public Optional<Corista> buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        Corista corista = null;

        try {
            corista = em.find(Corista.class, id);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar corista por ID " + id + ": " + e.getMessage());
        } finally {
            em.close();
        }
        return Optional.ofNullable(corista);
    }

    public void atualizar(Corista corista) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            em.merge(corista);
            em.getTransaction().commit();
            System.out.println("✅ Corista ID " + corista.getId() + " atualizado com sucesso.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erro ao atualizar corista ID " + corista.getId() + ": " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void deletar(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            Corista corista = em.find(Corista.class, id);

            if (corista != null) {
                em.remove(corista);
                em.getTransaction().commit();
                System.out.println("✅ Corista ID " + id + " deletado com sucesso.");
            } else {
                System.out.println("⚠️ Corista ID " + id + " não encontrado para deleção.");
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erro ao deletar corista ID " + id + ": " + e.getMessage());
        } finally {
            em.close();
        }
    }
}