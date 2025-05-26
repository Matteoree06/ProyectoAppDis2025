/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pack_negocio;

import Pack_negocio.exceptions.NonexistentEntityException;
import Pack_negocio.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Pack_persistencia.Cnomina;
import Pack_persistencia.Dnomina;
import Pack_persistencia.MotivoIngresoEgreso;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HP
 */
public class DnominaJpaController implements Serializable {

    public DnominaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dnomina dnomina) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cnomina idCabecera = dnomina.getIdCabecera();
            if (idCabecera != null) {
                idCabecera = em.getReference(idCabecera.getClass(), idCabecera.getIdCabecera());
                dnomina.setIdCabecera(idCabecera);
            }
            MotivoIngresoEgreso codMotivo = dnomina.getCodMotivo();
            if (codMotivo != null) {
                codMotivo = em.getReference(codMotivo.getClass(), codMotivo.getCodigo());
                dnomina.setCodMotivo(codMotivo);
            }
            em.persist(dnomina);
            if (idCabecera != null) {
                idCabecera.getDnominaList().add(dnomina);
                idCabecera = em.merge(idCabecera);
            }
            if (codMotivo != null) {
                codMotivo.getDnominaList().add(dnomina);
                codMotivo = em.merge(codMotivo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDnomina(dnomina.getIdDetalle()) != null) {
                throw new PreexistingEntityException("Dnomina " + dnomina + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dnomina dnomina) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dnomina persistentDnomina = em.find(Dnomina.class, dnomina.getIdDetalle());
            Cnomina idCabeceraOld = persistentDnomina.getIdCabecera();
            Cnomina idCabeceraNew = dnomina.getIdCabecera();
            MotivoIngresoEgreso codMotivoOld = persistentDnomina.getCodMotivo();
            MotivoIngresoEgreso codMotivoNew = dnomina.getCodMotivo();
            if (idCabeceraNew != null) {
                idCabeceraNew = em.getReference(idCabeceraNew.getClass(), idCabeceraNew.getIdCabecera());
                dnomina.setIdCabecera(idCabeceraNew);
            }
            if (codMotivoNew != null) {
                codMotivoNew = em.getReference(codMotivoNew.getClass(), codMotivoNew.getCodigo());
                dnomina.setCodMotivo(codMotivoNew);
            }
            dnomina = em.merge(dnomina);
            if (idCabeceraOld != null && !idCabeceraOld.equals(idCabeceraNew)) {
                idCabeceraOld.getDnominaList().remove(dnomina);
                idCabeceraOld = em.merge(idCabeceraOld);
            }
            if (idCabeceraNew != null && !idCabeceraNew.equals(idCabeceraOld)) {
                idCabeceraNew.getDnominaList().add(dnomina);
                idCabeceraNew = em.merge(idCabeceraNew);
            }
            if (codMotivoOld != null && !codMotivoOld.equals(codMotivoNew)) {
                codMotivoOld.getDnominaList().remove(dnomina);
                codMotivoOld = em.merge(codMotivoOld);
            }
            if (codMotivoNew != null && !codMotivoNew.equals(codMotivoOld)) {
                codMotivoNew.getDnominaList().add(dnomina);
                codMotivoNew = em.merge(codMotivoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = dnomina.getIdDetalle();
                if (findDnomina(id) == null) {
                    throw new NonexistentEntityException("The dnomina with id " + id + " no longer exists.");
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
            Dnomina dnomina;
            try {
                dnomina = em.getReference(Dnomina.class, id);
                dnomina.getIdDetalle();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dnomina with id " + id + " no longer exists.", enfe);
            }
            Cnomina idCabecera = dnomina.getIdCabecera();
            if (idCabecera != null) {
                idCabecera.getDnominaList().remove(dnomina);
                idCabecera = em.merge(idCabecera);
            }
            MotivoIngresoEgreso codMotivo = dnomina.getCodMotivo();
            if (codMotivo != null) {
                codMotivo.getDnominaList().remove(dnomina);
                codMotivo = em.merge(codMotivo);
            }
            em.remove(dnomina);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dnomina> findDnominaEntities() {
        return findDnominaEntities(true, -1, -1);
    }

    public List<Dnomina> findDnominaEntities(int maxResults, int firstResult) {
        return findDnominaEntities(false, maxResults, firstResult);
    }

    private List<Dnomina> findDnominaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dnomina.class));
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

    public Dnomina findDnomina(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dnomina.class, id);
        } finally {
            em.close();
        }
    }

    public int getDnominaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dnomina> rt = cq.from(Dnomina.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
