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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
            List<String> illegalOrphanMessages = null;
            for (Cnomina cnominaListOldCnomina : cnominaListOld) {
                if (!cnominaListNew.contains(cnominaListOldCnomina)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cnomina " + cnominaListOldCnomina + " since its idEmpleado field is not nullable.");
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
