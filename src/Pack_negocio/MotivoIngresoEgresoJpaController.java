/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pack_negocio;

import Pack_negocio.exceptions.NonexistentEntityException;
import Pack_negocio.exceptions.PreexistingEntityException;
import Pack_persistencia.MotivoIngresoEgreso;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author HP
 */
public class MotivoIngresoEgresoJpaController implements Serializable {

    public MotivoIngresoEgresoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MotivoIngresoEgreso motivoIngresoEgreso) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(motivoIngresoEgreso);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMotivoIngresoEgreso(motivoIngresoEgreso.getCodigo()) != null) {
                throw new PreexistingEntityException("MotivoIngresoEgreso " + motivoIngresoEgreso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MotivoIngresoEgreso motivoIngresoEgreso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            motivoIngresoEgreso = em.merge(motivoIngresoEgreso);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = motivoIngresoEgreso.getCodigo();
                if (findMotivoIngresoEgreso(id) == null) {
                    throw new NonexistentEntityException("The motivoIngresoEgreso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MotivoIngresoEgreso motivoIngresoEgreso;
            try {
                motivoIngresoEgreso = em.getReference(MotivoIngresoEgreso.class, id);
                motivoIngresoEgreso.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The motivoIngresoEgreso with id " + id + " no longer exists.", enfe);
            }
            em.remove(motivoIngresoEgreso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MotivoIngresoEgreso> findMotivoIngresoEgresoEntities() {
        return findMotivoIngresoEgresoEntities(true, -1, -1);
    }

    public List<MotivoIngresoEgreso> findMotivoIngresoEgresoEntities(int maxResults, int firstResult) {
        return findMotivoIngresoEgresoEntities(false, maxResults, firstResult);
    }

    private List<MotivoIngresoEgreso> findMotivoIngresoEgresoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MotivoIngresoEgreso.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MotivoIngresoEgreso findMotivoIngresoEgreso(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MotivoIngresoEgreso.class, id);
        } finally {
            em.close();
        }
    }

    public int getMotivoIngresoEgresoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MotivoIngresoEgreso> rt = cq.from(MotivoIngresoEgreso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
