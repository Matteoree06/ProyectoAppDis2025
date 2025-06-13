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
import Pack_persistencia.Empleado;
import Pack_persistencia.MotivoIngresoEgreso;
import Pack_persistencia.ReporteNomina;
import Pack_persistencia.ValoresPagar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HP
 */
public class ReporteNominaJpaController implements Serializable {

    public ReporteNominaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReporteNomina reporteNomina) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = reporteNomina.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                reporteNomina.setIdEmpleado(idEmpleado);
            }
            MotivoIngresoEgreso idMotivo = reporteNomina.getIdMotivo();
            if (idMotivo != null) {
                idMotivo = em.getReference(idMotivo.getClass(), idMotivo.getCodigo());
                reporteNomina.setIdMotivo(idMotivo);
            }
            ValoresPagar idValor = reporteNomina.getIdValor();
            if (idValor != null) {
                idValor = em.getReference(idValor.getClass(), idValor.getIdValor());
                reporteNomina.setIdValor(idValor);
            }
            em.persist(reporteNomina);
            if (idEmpleado != null) {
                idEmpleado.getReporteNominaList().add(reporteNomina);
                idEmpleado = em.merge(idEmpleado);
            }
            if (idMotivo != null) {
                idMotivo.getReporteNominaList().add(reporteNomina);
                idMotivo = em.merge(idMotivo);
            }
            if (idValor != null) {
                idValor.getReporteNominaList().add(reporteNomina);
                idValor = em.merge(idValor);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReporteNomina(reporteNomina.getIdReporte()) != null) {
                throw new PreexistingEntityException("ReporteNomina " + reporteNomina + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReporteNomina reporteNomina) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ReporteNomina persistentReporteNomina = em.find(ReporteNomina.class, reporteNomina.getIdReporte());
            Empleado idEmpleadoOld = persistentReporteNomina.getIdEmpleado();
            Empleado idEmpleadoNew = reporteNomina.getIdEmpleado();
            MotivoIngresoEgreso idMotivoOld = persistentReporteNomina.getIdMotivo();
            MotivoIngresoEgreso idMotivoNew = reporteNomina.getIdMotivo();
            ValoresPagar idValorOld = persistentReporteNomina.getIdValor();
            ValoresPagar idValorNew = reporteNomina.getIdValor();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                reporteNomina.setIdEmpleado(idEmpleadoNew);
            }
            if (idMotivoNew != null) {
                idMotivoNew = em.getReference(idMotivoNew.getClass(), idMotivoNew.getCodigo());
                reporteNomina.setIdMotivo(idMotivoNew);
            }
            if (idValorNew != null) {
                idValorNew = em.getReference(idValorNew.getClass(), idValorNew.getIdValor());
                reporteNomina.setIdValor(idValorNew);
            }
            reporteNomina = em.merge(reporteNomina);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getReporteNominaList().remove(reporteNomina);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getReporteNominaList().add(reporteNomina);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            if (idMotivoOld != null && !idMotivoOld.equals(idMotivoNew)) {
                idMotivoOld.getReporteNominaList().remove(reporteNomina);
                idMotivoOld = em.merge(idMotivoOld);
            }
            if (idMotivoNew != null && !idMotivoNew.equals(idMotivoOld)) {
                idMotivoNew.getReporteNominaList().add(reporteNomina);
                idMotivoNew = em.merge(idMotivoNew);
            }
            if (idValorOld != null && !idValorOld.equals(idValorNew)) {
                idValorOld.getReporteNominaList().remove(reporteNomina);
                idValorOld = em.merge(idValorOld);
            }
            if (idValorNew != null && !idValorNew.equals(idValorOld)) {
                idValorNew.getReporteNominaList().add(reporteNomina);
                idValorNew = em.merge(idValorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = reporteNomina.getIdReporte();
                if (findReporteNomina(id) == null) {
                    throw new NonexistentEntityException("The reporteNomina with id " + id + " no longer exists.");
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
            ReporteNomina reporteNomina;
            try {
                reporteNomina = em.getReference(ReporteNomina.class, id);
                reporteNomina.getIdReporte();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reporteNomina with id " + id + " no longer exists.", enfe);
            }
            Empleado idEmpleado = reporteNomina.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getReporteNominaList().remove(reporteNomina);
                idEmpleado = em.merge(idEmpleado);
            }
            MotivoIngresoEgreso idMotivo = reporteNomina.getIdMotivo();
            if (idMotivo != null) {
                idMotivo.getReporteNominaList().remove(reporteNomina);
                idMotivo = em.merge(idMotivo);
            }
            ValoresPagar idValor = reporteNomina.getIdValor();
            if (idValor != null) {
                idValor.getReporteNominaList().remove(reporteNomina);
                idValor = em.merge(idValor);
            }
            em.remove(reporteNomina);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReporteNomina> findReporteNominaEntities() {
        return findReporteNominaEntities(true, -1, -1);
    }

    public List<ReporteNomina> findReporteNominaEntities(int maxResults, int firstResult) {
        return findReporteNominaEntities(false, maxResults, firstResult);
    }

    private List<ReporteNomina> findReporteNominaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReporteNomina.class));
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

    public ReporteNomina findReporteNomina(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReporteNomina.class, id);
        } finally {
            em.close();
        }
    }

    public int getReporteNominaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReporteNomina> rt = cq.from(ReporteNomina.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
