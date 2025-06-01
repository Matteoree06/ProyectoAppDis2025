/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pack_persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "VALORES_PAGAR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValoresPagar.findAll", query = "SELECT v FROM ValoresPagar v")
    , @NamedQuery(name = "ValoresPagar.findByIdValor", query = "SELECT v FROM ValoresPagar v WHERE v.idValor = :idValor")
    , @NamedQuery(name = "ValoresPagar.findByFechaGeneracion", query = "SELECT v FROM ValoresPagar v WHERE v.fechaGeneracion = :fechaGeneracion")
    , @NamedQuery(name = "ValoresPagar.findByTotal", query = "SELECT v FROM ValoresPagar v WHERE v.total = :total")})
public class ValoresPagar implements Serializable {

    @JoinColumn(name = "ID_DETALLE", referencedColumnName = "ID_DETALLE")
    @ManyToOne
    private Dnomina idDetalle;
    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID_EMPLEADO")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_VALOR")
    private Long idValor;
    @Column(name = "FECHA_GENERACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaGeneracion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "TOTAL")
    private BigDecimal total;

    public ValoresPagar() {
    }

    public ValoresPagar(Long idValor) {
        this.idValor = idValor;
    }

    public ValoresPagar(Long idValor, BigDecimal total) {
        this.idValor = idValor;
        this.total = total;
    }

    public Long getIdValor() {
        return idValor;
    }

    public void setIdValor(Long idValor) {
        this.idValor = idValor;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idValor != null ? idValor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValoresPagar)) {
            return false;
        }
        ValoresPagar other = (ValoresPagar) object;
        if ((this.idValor == null && other.idValor != null) || (this.idValor != null && !this.idValor.equals(other.idValor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pack_persistencia.ValoresPagar[ idValor=" + idValor + " ]";
    }

    public Dnomina getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Dnomina idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
}
