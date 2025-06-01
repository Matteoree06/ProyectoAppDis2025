/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pack_negocio;

import Pack_negocio.exceptions.IllegalOrphanException;
import Pack_negocio.exceptions.NonexistentEntityException;
import Pack_negocio.exceptions.PreexistingEntityException;
import Pack_persistencia.Cnomina;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Pack_persistencia.Empleado;
import Pack_persistencia.Dnomina;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author HP
 */
public class CnominaJpaController implements Serializable {

    public CnominaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cnomina cnomina) throws PreexistingEntityException, Exception {
        if (cnomina.getDnominaList() == null) {
            cnomina.setDnominaList(new ArrayList<Dnomina>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = cnomina.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                cnomina.setIdEmpleado(idEmpleado);
            }
            List<Dnomina> attachedDnominaList = new ArrayList<Dnomina>();
            for (Dnomina dnominaListDnominaToAttach : cnomina.getDnominaList()) {
                dnominaListDnominaToAttach = em.getReference(dnominaListDnominaToAttach.getClass(), dnominaListDnominaToAttach.getIdDetalle());
                attachedDnominaList.add(dnominaListDnominaToAttach);
            }
            cnomina.setDnominaList(attachedDnominaList);
            em.persist(cnomina);
            if (idEmpleado != null) {
                idEmpleado.getCnominaList().add(cnomina);
                idEmpleado = em.merge(idEmpleado);
            }
            for (Dnomina dnominaListDnomina : cnomina.getDnominaList()) {
                Cnomina oldIdCabeceraOfDnominaListDnomina = dnominaListDnomina.getIdCabecera();
                dnominaListDnomina.setIdCabecera(cnomina);
                dnominaListDnomina = em.merge(dnominaListDnomina);
                if (oldIdCabeceraOfDnominaListDnomina != null) {
                    oldIdCabeceraOfDnominaListDnomina.getDnominaList().remove(dnominaListDnomina);
                    oldIdCabeceraOfDnominaListDnomina = em.merge(oldIdCabeceraOfDnominaListDnomina);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCnomina(cnomina.getIdCabecera()) != null) {
                throw new PreexistingEntityException("Cnomina " + cnomina + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cnomina cnomina) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cnomina persistentCnomina = em.find(Cnomina.class, cnomina.getIdCabecera());
            Empleado idEmpleadoOld = persistentCnomina.getIdEmpleado();
            Empleado idEmpleadoNew = cnomina.getIdEmpleado();
            List<Dnomina> dnominaListOld = persistentCnomina.getDnominaList();
            List<Dnomina> dnominaListNew = cnomina.getDnominaList();
            List<String> illegalOrphanMessages = null;
            for (Dnomina dnominaListOldDnomina : dnominaListOld) {
                if (!dnominaListNew.contains(dnominaListOldDnomina)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Dnomina " + dnominaListOldDnomina + " since its idCabecera field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                cnomina.setIdEmpleado(idEmpleadoNew);
            }
            List<Dnomina> attachedDnominaListNew = new ArrayList<Dnomina>();
            for (Dnomina dnominaListNewDnominaToAttach : dnominaListNew) {
                dnominaListNewDnominaToAttach = em.getReference(dnominaListNewDnominaToAttach.getClass(), dnominaListNewDnominaToAttach.getIdDetalle());
                attachedDnominaListNew.add(dnominaListNewDnominaToAttach);
            }
            dnominaListNew = attachedDnominaListNew;
            cnomina.setDnominaList(dnominaListNew);
            cnomina = em.merge(cnomina);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getCnominaList().remove(cnomina);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getCnominaList().add(cnomina);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            for (Dnomina dnominaListNewDnomina : dnominaListNew) {
                if (!dnominaListOld.contains(dnominaListNewDnomina)) {
                    Cnomina oldIdCabeceraOfDnominaListNewDnomina = dnominaListNewDnomina.getIdCabecera();
                    dnominaListNewDnomina.setIdCabecera(cnomina);
                    dnominaListNewDnomina = em.merge(dnominaListNewDnomina);
                    if (oldIdCabeceraOfDnominaListNewDnomina != null && !oldIdCabeceraOfDnominaListNewDnomina.equals(cnomina)) {
                        oldIdCabeceraOfDnominaListNewDnomina.getDnominaList().remove(dnominaListNewDnomina);
                        oldIdCabeceraOfDnominaListNewDnomina = em.merge(oldIdCabeceraOfDnominaListNewDnomina);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cnomina.getIdCabecera();
                if (findCnomina(id) == null) {
                    throw new NonexistentEntityException("The cnomina with id " + id + " no longer exists.");
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
            Cnomina cnomina;
            try {
                cnomina = em.getReference(Cnomina.class, id);
                cnomina.getIdCabecera();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cnomina with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Dnomina> dnominaListOrphanCheck = cnomina.getDnominaList();
            for (Dnomina dnominaListOrphanCheckDnomina : dnominaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cnomina (" + cnomina + ") cannot be destroyed since the Dnomina " + dnominaListOrphanCheckDnomina + " in its dnominaList field has a non-nullable idCabecera field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empleado idEmpleado = cnomina.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getCnominaList().remove(cnomina);
                idEmpleado = em.merge(idEmpleado);
            }
            em.remove(cnomina);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cnomina> findCnominaEntities() {
        return findCnominaEntities(true, -1, -1);
    }

    public List<Cnomina> findCnominaEntities(int maxResults, int firstResult) {
        return findCnominaEntities(false, maxResults, firstResult);
    }

    private List<Cnomina> findCnominaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cnomina.class));
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

    public Cnomina findCnomina(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cnomina.class, id);
        } finally {
            em.close();
        }
    }

    public int getCnominaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cnomina> rt = cq.from(Cnomina.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
