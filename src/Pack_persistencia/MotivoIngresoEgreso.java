/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pack_persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "MOTIVO_INGRESO_EGRESO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MotivoIngresoEgreso.findAll", query = "SELECT m FROM MotivoIngresoEgreso m")
    , @NamedQuery(name = "MotivoIngresoEgreso.findByCodigo", query = "SELECT m FROM MotivoIngresoEgreso m WHERE m.codigo = :codigo")
    , @NamedQuery(name = "MotivoIngresoEgreso.findByNombre", query = "SELECT m FROM MotivoIngresoEgreso m WHERE m.nombre = :nombre")
    , @NamedQuery(name = "MotivoIngresoEgreso.findByTipo", query = "SELECT m FROM MotivoIngresoEgreso m WHERE m.tipo = :tipo")})
public class MotivoIngresoEgreso implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMotivo")
    private List<ReporteNomina> reporteNominaList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODIGO")
    private Long codigo;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "TIPO")
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codMotivo")
    private List<Dnomina> dnominaList;

    public MotivoIngresoEgreso() {
    }

    public MotivoIngresoEgreso(Long codigo) {
        this.codigo = codigo;
    }

    public MotivoIngresoEgreso(Long codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MotivoIngresoEgreso)) {
            return false;
        }
        MotivoIngresoEgreso other = (MotivoIngresoEgreso) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pack_persistencia.MotivoIngresoEgreso[ codigo=" + codigo + " ]";
    }

    @XmlTransient
    public List<ReporteNomina> getReporteNominaList() {
        return reporteNominaList;
    }

    public void setReporteNominaList(List<ReporteNomina> reporteNominaList) {
        this.reporteNominaList = reporteNominaList;
    }
    
}
