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
import Pack_persistencia.Empleado;
import Pack_persistencia.ReporteNomina;
import Pack_persistencia.ValoresPagar;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HP
 */
public class ValoresPagarJpaController implements Serializable {

    public ValoresPagarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ValoresPagar valoresPagar) throws PreexistingEntityException, Exception {
        if (valoresPagar.getReporteNominaList() == null) {
            valoresPagar.setReporteNominaList(new ArrayList<ReporteNomina>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dnomina idDetalle = valoresPagar.getIdDetalle();
            if (idDetalle != null) {
                idDetalle = em.getReference(idDetalle.getClass(), idDetalle.getIdDetalle());
                valoresPagar.setIdDetalle(idDetalle);
            }
            Empleado idEmpleado = valoresPagar.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                valoresPagar.setIdEmpleado(idEmpleado);
            }
            List<ReporteNomina> attachedReporteNominaList = new ArrayList<ReporteNomina>();
            for (ReporteNomina reporteNominaListReporteNominaToAttach : valoresPagar.getReporteNominaList()) {
                reporteNominaListReporteNominaToAttach = em.getReference(reporteNominaListReporteNominaToAttach.getClass(), reporteNominaListReporteNominaToAttach.getIdReporte());
                attachedReporteNominaList.add(reporteNominaListReporteNominaToAttach);
            }
            valoresPagar.setReporteNominaList(attachedReporteNominaList);
            em.persist(valoresPagar);
            if (idDetalle != null) {
                idDetalle.getValoresPagarList().add(valoresPagar);
                idDetalle = em.merge(idDetalle);
            }
            if (idEmpleado != null) {
                idEmpleado.getValoresPagarList().add(valoresPagar);
                idEmpleado = em.merge(idEmpleado);
            }
            for (ReporteNomina reporteNominaListReporteNomina : valoresPagar.getReporteNominaList()) {
                ValoresPagar oldIdValorOfReporteNominaListReporteNomina = reporteNominaListReporteNomina.getIdValor();
                reporteNominaListReporteNomina.setIdValor(valoresPagar);
                reporteNominaListReporteNomina = em.merge(reporteNominaListReporteNomina);
                if (oldIdValorOfReporteNominaListReporteNomina != null) {
                    oldIdValorOfReporteNominaListReporteNomina.getReporteNominaList().remove(reporteNominaListReporteNomina);
                    oldIdValorOfReporteNominaListReporteNomina = em.merge(oldIdValorOfReporteNominaListReporteNomina);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findValoresPagar(valoresPagar.getIdValor()) != null) {
                throw new PreexistingEntityException("ValoresPagar " + valoresPagar + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ValoresPagar valoresPagar) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ValoresPagar persistentValoresPagar = em.find(ValoresPagar.class, valoresPagar.getIdValor());
            Dnomina idDetalleOld = persistentValoresPagar.getIdDetalle();
            Dnomina idDetalleNew = valoresPagar.getIdDetalle();
            Empleado idEmpleadoOld = persistentValoresPagar.getIdEmpleado();
            Empleado idEmpleadoNew = valoresPagar.getIdEmpleado();
            List<ReporteNomina> reporteNominaListOld = persistentValoresPagar.getReporteNominaList();
            List<ReporteNomina> reporteNominaListNew = valoresPagar.getReporteNominaList();
            List<String> illegalOrphanMessages = null;
            for (ReporteNomina reporteNominaListOldReporteNomina : reporteNominaListOld) {
                if (!reporteNominaListNew.contains(reporteNominaListOldReporteNomina)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ReporteNomina " + reporteNominaListOldReporteNomina + " since its idValor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDetalleNew != null) {
                idDetalleNew = em.getReference(idDetalleNew.getClass(), idDetalleNew.getIdDetalle());
                valoresPagar.setIdDetalle(idDetalleNew);
            }
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                valoresPagar.setIdEmpleado(idEmpleadoNew);
            }
            List<ReporteNomina> attachedReporteNominaListNew = new ArrayList<ReporteNomina>();
            for (ReporteNomina reporteNominaListNewReporteNominaToAttach : reporteNominaListNew) {
                reporteNominaListNewReporteNominaToAttach = em.getReference(reporteNominaListNewReporteNominaToAttach.getClass(), reporteNominaListNewReporteNominaToAttach.getIdReporte());
                attachedReporteNominaListNew.add(reporteNominaListNewReporteNominaToAttach);
            }
            reporteNominaListNew = attachedReporteNominaListNew;
            valoresPagar.setReporteNominaList(reporteNominaListNew);
            valoresPagar = em.merge(valoresPagar);
            if (idDetalleOld != null && !idDetalleOld.equals(idDetalleNew)) {
                idDetalleOld.getValoresPagarList().remove(valoresPagar);
                idDetalleOld = em.merge(idDetalleOld);
            }
            if (idDetalleNew != null && !idDetalleNew.equals(idDetalleOld)) {
                idDetalleNew.getValoresPagarList().add(valoresPagar);
                idDetalleNew = em.merge(idDetalleNew);
            }
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getValoresPagarList().remove(valoresPagar);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getValoresPagarList().add(valoresPagar);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            for (ReporteNomina reporteNominaListNewReporteNomina : reporteNominaListNew) {
                if (!reporteNominaListOld.contains(reporteNominaListNewReporteNomina)) {
                    ValoresPagar oldIdValorOfReporteNominaListNewReporteNomina = reporteNominaListNewReporteNomina.getIdValor();
                    reporteNominaListNewReporteNomina.setIdValor(valoresPagar);
                    reporteNominaListNewReporteNomina = em.merge(reporteNominaListNewReporteNomina);
                    if (oldIdValorOfReporteNominaListNewReporteNomina != null && !oldIdValorOfReporteNominaListNewReporteNomina.equals(valoresPagar)) {
                        oldIdValorOfReporteNominaListNewReporteNomina.getReporteNominaList().remove(reporteNominaListNewReporteNomina);
                        oldIdValorOfReporteNominaListNewReporteNomina = em.merge(oldIdValorOfReporteNominaListNewReporteNomina);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = valoresPagar.getIdValor();
                if (findValoresPagar(id) == null) {
                    throw new NonexistentEntityException("The valoresPagar with id " + id + " no longer exists.");
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
            ValoresPagar valoresPagar;
            try {
                valoresPagar = em.getReference(ValoresPagar.class, id);
                valoresPagar.getIdValor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valoresPagar with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ReporteNomina> reporteNominaListOrphanCheck = valoresPagar.getReporteNominaList();
            for (ReporteNomina reporteNominaListOrphanCheckReporteNomina : reporteNominaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ValoresPagar (" + valoresPagar + ") cannot be destroyed since the ReporteNomina " + reporteNominaListOrphanCheckReporteNomina + " in its reporteNominaList field has a non-nullable idValor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Dnomina idDetalle = valoresPagar.getIdDetalle();
            if (idDetalle != null) {
                idDetalle.getValoresPagarList().remove(valoresPagar);
                idDetalle = em.merge(idDetalle);
            }
            Empleado idEmpleado = valoresPagar.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getValoresPagarList().remove(valoresPagar);
                idEmpleado = em.merge(idEmpleado);
            }
            em.remove(valoresPagar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ValoresPagar> findValoresPagarEntities() {
        return findValoresPagarEntities(true, -1, -1);
    }

    public List<ValoresPagar> findValoresPagarEntities(int maxResults, int firstResult) {
        return findValoresPagarEntities(false, maxResults, firstResult);
    }

    private List<ValoresPagar> findValoresPagarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ValoresPagar.class));
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

    public ValoresPagar findValoresPagar(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValoresPagar.class, id);
        } finally {
            em.close();
        }
    }

    public int getValoresPagarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ValoresPagar> rt = cq.from(ValoresPagar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
