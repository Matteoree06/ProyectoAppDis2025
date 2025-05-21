/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capa_persistencia;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "EMPLEADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e")
    , @NamedQuery(name = "Empleado.findByIdEmpleado", query = "SELECT e FROM Empleado e WHERE e.idEmpleado = :idEmpleado")
    , @NamedQuery(name = "Empleado.findByCedula", query = "SELECT e FROM Empleado e WHERE e.cedula = :cedula")
    , @NamedQuery(name = "Empleado.findByNombre", query = "SELECT e FROM Empleado e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Empleado.findByIngreso", query = "SELECT e FROM Empleado e WHERE e.ingreso = :ingreso")
    , @NamedQuery(name = "Empleado.findBySueldo", query = "SELECT e FROM Empleado e WHERE e.sueldo = :sueldo")})
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_EMPLEADO")
    private Long idEmpleado;
    @Basic(optional = false)
    @Column(name = "CEDULA")
    private long cedula;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ingreso;
    @Basic(optional = false)
    @Column(name = "SUELDO")
    private BigInteger sueldo;

    public Empleado() {
    }

    public Empleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Empleado(Long idEmpleado, long cedula, String nombre, Date ingreso, BigInteger sueldo) {
        this.idEmpleado = idEmpleado;
        this.cedula = cedula;
        this.nombre = nombre;
        this.ingreso = ingreso;
        this.sueldo = sueldo;
    }

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public long getCedula() {
        return cedula;
    }

    public void setCedula(long cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getIngreso() {
        return ingreso;
    }

    public void setIngreso(Date ingreso) {
        this.ingreso = ingreso;
    }

    public BigInteger getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigInteger sueldo) {
        this.sueldo = sueldo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpleado != null ? idEmpleado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.idEmpleado == null && other.idEmpleado != null) || (this.idEmpleado != null && !this.idEmpleado.equals(other.idEmpleado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capa_persistencia.Empleado[ idEmpleado=" + idEmpleado + " ]";
    }
    
}
