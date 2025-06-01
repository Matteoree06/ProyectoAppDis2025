/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pack_negocio;

import Pack_negocio.exceptions.IllegalOrphanException;
import Pack_negocio.exceptions.NonexistentEntityException;
import Pack_negocio.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Pack_persistencia.Dnomina;
import Pack_persistencia.MotivoIngresoEgreso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
        if (motivoIngresoEgreso.getDnominaList() == null) {
            motivoIngresoEgreso.setDnominaList(new ArrayList<Dnomina>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Dnomina> attachedDnominaList = new ArrayList<Dnomina>();
            for (Dnomina dnominaListDnominaToAttach : motivoIngresoEgreso.getDnominaList()) {
                dnominaListDnominaToAttach = em.getReference(dnominaListDnominaToAttach.getClass(), dnominaListDnominaToAttach.getIdDetalle());
                attachedDnominaList.add(dnominaListDnominaToAttach);
            }
            motivoIngresoEgreso.setDnominaList(attachedDnominaList);
            em.persist(motivoIngresoEgreso);
            for (Dnomina dnominaListDnomina : motivoIngresoEgreso.getDnominaList()) {
                MotivoIngresoEgreso oldCodMotivoOfDnominaListDnomina = dnominaListDnomina.getCodMotivo();
                dnominaListDnomina.setCodMotivo(motivoIngresoEgreso);
                dnominaListDnomina = em.merge(dnominaListDnomina);
                if (oldCodMotivoOfDnominaListDnomina != null) {
                    oldCodMotivoOfDnominaListDnomina.getDnominaList().remove(dnominaListDnomina);
                    oldCodMotivoOfDnominaListDnomina = em.merge(oldCodMotivoOfDnominaListDnomina);
                }
            }
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

    public void edit(MotivoIngresoEgreso motivoIngresoEgreso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MotivoIngresoEgreso persistentMotivoIngresoEgreso = em.find(MotivoIngresoEgreso.class, motivoIngresoEgreso.getCodigo());
            List<Dnomina> dnominaListOld = persistentMotivoIngresoEgreso.getDnominaList();
            List<Dnomina> dnominaListNew = motivoIngresoEgreso.getDnominaList();
            List<String> illegalOrphanMessages = null;
            for (Dnomina dnominaListOldDnomina : dnominaListOld) {
                if (!dnominaListNew.contains(dnominaListOldDnomina)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Dnomina " + dnominaListOldDnomina + " since its codMotivo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Dnomina> attachedDnominaListNew = new ArrayList<Dnomina>();
            for (Dnomina dnominaListNewDnominaToAttach : dnominaListNew) {
                dnominaListNewDnominaToAttach = em.getReference(dnominaListNewDnominaToAttach.getClass(), dnominaListNewDnominaToAttach.getIdDetalle());
                attachedDnominaListNew.add(dnominaListNewDnominaToAttach);
            }
            dnominaListNew = attachedDnominaListNew;
            motivoIngresoEgreso.setDnominaList(dnominaListNew);
            motivoIngresoEgreso = em.merge(motivoIngresoEgreso);
            for (Dnomina dnominaListNewDnomina : dnominaListNew) {
                if (!dnominaListOld.contains(dnominaListNewDnomina)) {
                    MotivoIngresoEgreso oldCodMotivoOfDnominaListNewDnomina = dnominaListNewDnomina.getCodMotivo();
                    dnominaListNewDnomina.setCodMotivo(motivoIngresoEgreso);
                    dnominaListNewDnomina = em.merge(dnominaListNewDnomina);
                    if (oldCodMotivoOfDnominaListNewDnomina != null && !oldCodMotivoOfDnominaListNewDnomina.equals(motivoIngresoEgreso)) {
                        oldCodMotivoOfDnominaListNewDnomina.getDnominaList().remove(dnominaListNewDnomina);
                        oldCodMotivoOfDnominaListNewDnomina = em.merge(oldCodMotivoOfDnominaListNewDnomina);
                    }
                }
            }
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

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<Dnomina> dnominaListOrphanCheck = motivoIngresoEgreso.getDnominaList();
            for (Dnomina dnominaListOrphanCheckDnomina : dnominaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MotivoIngresoEgreso (" + motivoIngresoEgreso + ") cannot be destroyed since the Dnomina " + dnominaListOrphanCheckDnomina + " in its dnominaList field has a non-nullable codMotivo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
