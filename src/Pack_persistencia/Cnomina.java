/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pack_persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "CNOMINA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cnomina.findAll", query = "SELECT c FROM Cnomina c")
    , @NamedQuery(name = "Cnomina.findByIdCabecera", query = "SELECT c FROM Cnomina c WHERE c.idCabecera = :idCabecera")
    , @NamedQuery(name = "Cnomina.findByFechaPago", query = "SELECT c FROM Cnomina c WHERE c.fechaPago = :fechaPago")
    , @NamedQuery(name = "Cnomina.findByTotalPagado", query = "SELECT c FROM Cnomina c WHERE c.totalPagado = :totalPagado")})
public class Cnomina implements Serializable {

    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID_EMPLEADO")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CABECERA")
    private Long idCabecera;
    @Basic(optional = false)
    @Column(name = "FECHA_PAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TOTAL_PAGADO")
    private BigDecimal totalPagado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCabecera")
    private List<Dnomina> dnominaList;

    public Cnomina() {
    }

    public Cnomina(Long idCabecera) {
        this.idCabecera = idCabecera;
    }

    public Cnomina(Long idCabecera, Date fechaPago) {
        this.idCabecera = idCabecera;
        this.fechaPago = fechaPago;
    }

    public Long getIdCabecera() {
        return idCabecera;
    }

    public void setIdCabecera(Long idCabecera) {
        this.idCabecera = idCabecera;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    @XmlTransient
    public List<Dnomina> getDnominaList() {
        return dnominaList;
    }

    public void setDnominaList(List<Dnomina> dnominaList) {
        this.dnominaList = dnominaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCabecera != null ? idCabecera.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cnomina)) {
            return false;
        }
        Cnomina other = (Cnomina) object;
        if ((this.idCabecera == null && other.idCabecera != null) || (this.idCabecera != null && !this.idCabecera.equals(other.idCabecera))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pack_persistencia.Cnomina[ idCabecera=" + idCabecera + " ]";
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
}
