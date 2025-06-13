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
@Table(name = "REPORTE_NOMINA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReporteNomina.findAll", query = "SELECT r FROM ReporteNomina r")
    , @NamedQuery(name = "ReporteNomina.findByIdReporte", query = "SELECT r FROM ReporteNomina r WHERE r.idReporte = :idReporte")
    , @NamedQuery(name = "ReporteNomina.findByValorPagar", query = "SELECT r FROM ReporteNomina r WHERE r.valorPagar = :valorPagar")
    , @NamedQuery(name = "ReporteNomina.findByFechaEntrada", query = "SELECT r FROM ReporteNomina r WHERE r.fechaEntrada = :fechaEntrada")})
public class ReporteNomina implements Serializable {

    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID_EMPLEADO")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;
    @JoinColumn(name = "ID_MOTIVO", referencedColumnName = "CODIGO")
    @ManyToOne(optional = false)
    private MotivoIngresoEgreso idMotivo;
    @JoinColumn(name = "ID_VALOR", referencedColumnName = "ID_VALOR")
    @ManyToOne(optional = false)
    private ValoresPagar idValor;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_REPORTE")
    private Long idReporte;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "VALOR_PAGAR")
    private BigDecimal valorPagar;
    @Basic(optional = false)
    @Column(name = "FECHA_ENTRADA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrada;

    public ReporteNomina() {
    }

    public ReporteNomina(Long idReporte) {
        this.idReporte = idReporte;
    }

    public ReporteNomina(Long idReporte, BigDecimal valorPagar, Date fechaEntrada) {
        this.idReporte = idReporte;
        this.valorPagar = valorPagar;
        this.fechaEntrada = fechaEntrada;
    }

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public BigDecimal getValorPagar() {
        return valorPagar;
    }

    public void setValorPagar(BigDecimal valorPagar) {
        this.valorPagar = valorPagar;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReporte != null ? idReporte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReporteNomina)) {
            return false;
        }
        ReporteNomina other = (ReporteNomina) object;
        if ((this.idReporte == null && other.idReporte != null) || (this.idReporte != null && !this.idReporte.equals(other.idReporte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pack_persistencia.ReporteNomina[ idReporte=" + idReporte + " ]";
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public MotivoIngresoEgreso getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(MotivoIngresoEgreso idMotivo) {
        this.idMotivo = idMotivo;
    }

    public ValoresPagar getIdValor() {
        return idValor;
    }

    public void setIdValor(ValoresPagar idValor) {
        this.idValor = idValor;
    }
    
}
