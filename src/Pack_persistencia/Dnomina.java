/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pack_persistencia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "DNOMINA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dnomina.findAll", query = "SELECT d FROM Dnomina d")
    , @NamedQuery(name = "Dnomina.findByIdDetalle", query = "SELECT d FROM Dnomina d WHERE d.idDetalle = :idDetalle")
    , @NamedQuery(name = "Dnomina.findBySueldoBase", query = "SELECT d FROM Dnomina d WHERE d.sueldoBase = :sueldoBase")
    , @NamedQuery(name = "Dnomina.findBySeguro", query = "SELECT d FROM Dnomina d WHERE d.seguro = :seguro")
    , @NamedQuery(name = "Dnomina.findByTotalPagar", query = "SELECT d FROM Dnomina d WHERE d.totalPagar = :totalPagar")})
public class Dnomina implements Serializable {

    @OneToMany(mappedBy = "idDetalle")
    private List<ValoresPagar> valoresPagarList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_DETALLE")
    private Long idDetalle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "SUELDO_BASE")
    private BigDecimal sueldoBase;
    @Column(name = "SEGURO")
    private BigDecimal seguro;
    @Column(name = "TOTAL_PAGAR", insertable = false, updatable = false)
    private BigDecimal totalPagar;
    @JoinColumn(name = "ID_CABECERA", referencedColumnName = "ID_CABECERA")
    @ManyToOne(optional = false)
    private Cnomina idCabecera;
    @JoinColumn(name = "COD_MOTIVO", referencedColumnName = "CODIGO")
    @ManyToOne(optional = false)
    private MotivoIngresoEgreso codMotivo;

    public Dnomina() {
    }

    public Dnomina(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Dnomina(Long idDetalle, BigDecimal sueldoBase) {
        this.idDetalle = idDetalle;
        this.sueldoBase = sueldoBase;
    }

    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public BigDecimal getSueldoBase() {
        return sueldoBase;
    }

    public void setSueldoBase(BigDecimal sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    public BigDecimal getSeguro() {
        return seguro;
    }

    public void setSeguro(BigDecimal seguro) {
        this.seguro = seguro;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    public void setTotalPagar(BigDecimal totalPagar) {
        this.totalPagar = totalPagar;
    }

    public Cnomina getIdCabecera() {
        return idCabecera;
    }

    public void setIdCabecera(Cnomina idCabecera) {
        this.idCabecera = idCabecera;
    }

    public MotivoIngresoEgreso getCodMotivo() {
        return codMotivo;
    }

    public void setCodMotivo(MotivoIngresoEgreso codMotivo) {
        this.codMotivo = codMotivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalle != null ? idDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dnomina)) {
            return false;
        }
        Dnomina other = (Dnomina) object;
        if ((this.idDetalle == null && other.idDetalle != null) || (this.idDetalle != null && !this.idDetalle.equals(other.idDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pack_persistencia.Dnomina[ idDetalle=" + idDetalle + " ]";
    }

    @XmlTransient
    public List<ValoresPagar> getValoresPagarList() {
        return valoresPagarList;
    }

    public void setValoresPagarList(List<ValoresPagar> valoresPagarList) {
        this.valoresPagarList = valoresPagarList;
    }
    
}
