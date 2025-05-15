/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_negocio;
import Capa_persistencia.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
/**
 *
 * @author HP
 */
public class LoginService {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoAppDistiPU");

    public boolean authenticate(String username, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByUsuario", Usuario.class);
            query.setParameter("usuario", username);
            Usuario usuario = query.getSingleResult();
            return usuario.getPassword().equals(password);
        } catch (NoResultException e) {
            return false;
        } finally {
            em.close();
        }
    }
}
