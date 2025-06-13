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
import Pack_persistencia.Cnomina;
import Pack_persistencia.Empleado;
import java.util.ArrayList;
import java.util.List;
import Pack_persistencia.ValoresPagar;
import Pack_persistencia.ReporteNomina;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HP
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws PreexistingEntityException, Exception {
        if (empleado.getCnominaList() == null) {
            empleado.setCnominaList(new ArrayList<Cnomina>());
        }
        if (empleado.getValoresPagarList() == null) {
            empleado.setValoresPagarList(new ArrayList<ValoresPagar>());
        }
        if (empleado.getReporteNominaList() == null) {
            empleado.setReporteNominaList(new ArrayList<ReporteNomina>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cnomina> attachedCnominaList = new ArrayList<Cnomina>();
            for (Cnomina cnominaListCnominaToAttach : empleado.getCnominaList()) {
                cnominaListCnominaToAttach = em.getReference(cnominaListCnominaToAttach.getClass(), cnominaListCnominaToAttach.getIdCabecera());
                attachedCnominaList.add(cnominaListCnominaToAttach);
            }
            empleado.setCnominaList(attachedCnominaList);
            List<ValoresPagar> attachedValoresPagarList = new ArrayList<ValoresPagar>();
            for (ValoresPagar valoresPagarListValoresPagarToAttach : empleado.getValoresPagarList()) {
                valoresPagarListValoresPagarToAttach = em.getReference(valoresPagarListValoresPagarToAttach.getClass(), valoresPagarListValoresPagarToAttach.getIdValor());
                attachedValoresPagarList.add(valoresPagarListValoresPagarToAttach);
            }
            empleado.setValoresPagarList(attachedValoresPagarList);
            List<ReporteNomina> attachedReporteNominaList = new ArrayList<ReporteNomina>();
            for (ReporteNomina reporteNominaListReporteNominaToAttach : empleado.getReporteNominaList()) {
                reporteNominaListReporteNominaToAttach = em.getReference(reporteNominaListReporteNominaToAttach.getClass(), reporteNominaListReporteNominaToAttach.getIdReporte());
                attachedReporteNominaList.add(reporteNominaListReporteNominaToAttach);
            }
            empleado.setReporteNominaList(attachedReporteNominaList);
            em.persist(empleado);
            for (Cnomina cnominaListCnomina : empleado.getCnominaList()) {
                Empleado oldIdEmpleadoOfCnominaListCnomina = cnominaListCnomina.getIdEmpleado();
                cnominaListCnomina.setIdEmpleado(empleado);
                cnominaListCnomina = em.merge(cnominaListCnomina);
                if (oldIdEmpleadoOfCnominaListCnomina != null) {
                    oldIdEmpleadoOfCnominaListCnomina.getCnominaList().remove(cnominaListCnomina);
                    oldIdEmpleadoOfCnominaListCnomina = em.merge(oldIdEmpleadoOfCnominaListCnomina);
                }
            }
            for (ValoresPagar valoresPagarListValoresPagar : empleado.getValoresPagarList()) {
                Empleado oldIdEmpleadoOfValoresPagarListValoresPagar = valoresPagarListValoresPagar.getIdEmpleado();
                valoresPagarListValoresPagar.setIdEmpleado(empleado);
                valoresPagarListValoresPagar = em.merge(valoresPagarListValoresPagar);
                if (oldIdEmpleadoOfValoresPagarListValoresPagar != null) {
                    oldIdEmpleadoOfValoresPagarListValoresPagar.getValoresPagarList().remove(valoresPagarListValoresPagar);
                    oldIdEmpleadoOfValoresPagarListValoresPagar = em.merge(oldIdEmpleadoOfValoresPagarListValoresPagar);
                }
            }
            for (ReporteNomina reporteNominaListReporteNomina : empleado.getReporteNominaList()) {
                Empleado oldIdEmpleadoOfReporteNominaListReporteNomina = reporteNominaListReporteNomina.getIdEmpleado();
                reporteNominaListReporteNomina.setIdEmpleado(empleado);
                reporteNominaListReporteNomina = em.merge(reporteNominaListReporteNomina);
                if (oldIdEmpleadoOfReporteNominaListReporteNomina != null) {
                    oldIdEmpleadoOfReporteNominaListReporteNomina.getReporteNominaList().remove(reporteNominaListReporteNomina);
                    oldIdEmpleadoOfReporteNominaListReporteNomina = em.merge(oldIdEmpleadoOfReporteNominaListReporteNomina);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleado(empleado.getIdEmpleado()) != null) {
                throw new PreexistingEntityException("Empleado " + empleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdEmpleado());
            List<Cnomina> cnominaListOld = persistentEmpleado.getCnominaList();
            List<Cnomina> cnominaListNew = empleado.getCnominaList();
            List<ValoresPagar> valoresPagarListOld = persistentEmpleado.getValoresPagarList();
            List<ValoresPagar> valoresPagarListNew = empleado.getValoresPagarList();
            List<ReporteNomina> reporteNominaListOld = persistentEmpleado.getReporteNominaList();
            List<ReporteNomina> reporteNominaListNew = empleado.getReporteNominaList();
            List<String> illegalOrphanMessages = null;
            for (Cnomina cnominaListOldCnomina : cnominaListOld) {
                if (!cnominaListNew.contains(cnominaListOldCnomina)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cnomina " + cnominaListOldCnomina + " since its idEmpleado field is not nullable.");
                }
            }
            for (ValoresPagar valoresPagarListOldValoresPagar : valoresPagarListOld) {
                if (!valoresPagarListNew.contains(valoresPagarListOldValoresPagar)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ValoresPagar " + valoresPagarListOldValoresPagar + " since its idEmpleado field is not nullable.");
                }
            }
            for (ReporteNomina reporteNominaListOldReporteNomina : reporteNominaListOld) {
                if (!reporteNominaListNew.contains(reporteNominaListOldReporteNomina)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ReporteNomina " + reporteNominaListOldReporteNomina + " since its idEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cnomina> attachedCnominaListNew = new ArrayList<Cnomina>();
            for (Cnomina cnominaListNewCnominaToAttach : cnominaListNew) {
                cnominaListNewCnominaToAttach = em.getReference(cnominaListNewCnominaToAttach.getClass(), cnominaListNewCnominaToAttach.getIdCabecera());
                attachedCnominaListNew.add(cnominaListNewCnominaToAttach);
            }
            cnominaListNew = attachedCnominaListNew;
            empleado.setCnominaList(cnominaListNew);
            List<ValoresPagar> attachedValoresPagarListNew = new ArrayList<ValoresPagar>();
            for (ValoresPagar valoresPagarListNewValoresPagarToAttach : valoresPagarListNew) {
                valoresPagarListNewValoresPagarToAttach = em.getReference(valoresPagarListNewValoresPagarToAttach.getClass(), valoresPagarListNewValoresPagarToAttach.getIdValor());
                attachedValoresPagarListNew.add(valoresPagarListNewValoresPagarToAttach);
            }
            valoresPagarListNew = attachedValoresPagarListNew;
            empleado.setValoresPagarList(valoresPagarListNew);
            List<ReporteNomina> attachedReporteNominaListNew = new ArrayList<ReporteNomina>();
            for (ReporteNomina reporteNominaListNewReporteNominaToAttach : reporteNominaListNew) {
                reporteNominaListNewReporteNominaToAttach = em.getReference(reporteNominaListNewReporteNominaToAttach.getClass(), reporteNominaListNewReporteNominaToAttach.getIdReporte());
                attachedReporteNominaListNew.add(reporteNominaListNewReporteNominaToAttach);
            }
            reporteNominaListNew = attachedReporteNominaListNew;
            empleado.setReporteNominaList(reporteNominaListNew);
            empleado = em.merge(empleado);
            for (Cnomina cnominaListNewCnomina : cnominaListNew) {
                if (!cnominaListOld.contains(cnominaListNewCnomina)) {
                    Empleado oldIdEmpleadoOfCnominaListNewCnomina = cnominaListNewCnomina.getIdEmpleado();
                    cnominaListNewCnomina.setIdEmpleado(empleado);
                    cnominaListNewCnomina = em.merge(cnominaListNewCnomina);
                    if (oldIdEmpleadoOfCnominaListNewCnomina != null && !oldIdEmpleadoOfCnominaListNewCnomina.equals(empleado)) {
                        oldIdEmpleadoOfCnominaListNewCnomina.getCnominaList().remove(cnominaListNewCnomina);
                        oldIdEmpleadoOfCnominaListNewCnomina = em.merge(oldIdEmpleadoOfCnominaListNewCnomina);
                    }
                }
            }
            for (ValoresPagar valoresPagarListNewValoresPagar : valoresPagarListNew) {
                if (!valoresPagarListOld.contains(valoresPagarListNewValoresPagar)) {
                    Empleado oldIdEmpleadoOfValoresPagarListNewValoresPagar = valoresPagarListNewValoresPagar.getIdEmpleado();
                    valoresPagarListNewValoresPagar.setIdEmpleado(empleado);
                    valoresPagarListNewValoresPagar = em.merge(valoresPagarListNewValoresPagar);
                    if (oldIdEmpleadoOfValoresPagarListNewValoresPagar != null && !oldIdEmpleadoOfValoresPagarListNewValoresPagar.equals(empleado)) {
                        oldIdEmpleadoOfValoresPagarListNewValoresPagar.getValoresPagarList().remove(valoresPagarListNewValoresPagar);
                        oldIdEmpleadoOfValoresPagarListNewValoresPagar = em.merge(oldIdEmpleadoOfValoresPagarListNewValoresPagar);
                    }
                }
            }
            for (ReporteNomina reporteNominaListNewReporteNomina : reporteNominaListNew) {
                if (!reporteNominaListOld.contains(reporteNominaListNewReporteNomina)) {
                    Empleado oldIdEmpleadoOfReporteNominaListNewReporteNomina = reporteNominaListNewReporteNomina.getIdEmpleado();
                    reporteNominaListNewReporteNomina.setIdEmpleado(empleado);
                    reporteNominaListNewReporteNomina = em.merge(reporteNominaListNewReporteNomina);
                    if (oldIdEmpleadoOfReporteNominaListNewReporteNomina != null && !oldIdEmpleadoOfReporteNominaListNewReporteNomina.equals(empleado)) {
                        oldIdEmpleadoOfReporteNominaListNewReporteNomina.getReporteNominaList().remove(reporteNominaListNewReporteNomina);
                        oldIdEmpleadoOfReporteNominaListNewReporteNomina = em.merge(oldIdEmpleadoOfReporteNominaListNewReporteNomina);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = empleado.getIdEmpleado();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cnomina> cnominaListOrphanCheck = empleado.getCnominaList();
            for (Cnomina cnominaListOrphanCheckCnomina : cnominaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Cnomina " + cnominaListOrphanCheckCnomina + " in its cnominaList field has a non-nullable idEmpleado field.");
            }
            List<ValoresPagar> valoresPagarListOrphanCheck = empleado.getValoresPagarList();
            for (ValoresPagar valoresPagarListOrphanCheckValoresPagar : valoresPagarListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the ValoresPagar " + valoresPagarListOrphanCheckValoresPagar + " in its valoresPagarList field has a non-nullable idEmpleado field.");
            }
            List<ReporteNomina> reporteNominaListOrphanCheck = empleado.getReporteNominaList();
            for (ReporteNomina reporteNominaListOrphanCheckReporteNomina : reporteNominaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the ReporteNomina " + reporteNominaListOrphanCheckReporteNomina + " in its reporteNominaList field has a non-nullable idEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
