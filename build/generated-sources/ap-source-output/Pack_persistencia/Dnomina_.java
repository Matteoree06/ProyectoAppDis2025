package Pack_persistencia;

import Pack_persistencia.Cnomina;
import Pack_persistencia.Empleado;
import Pack_persistencia.MotivoIngresoEgreso;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-05-25T13:06:05")
@StaticMetamodel(Dnomina.class)
public class Dnomina_ { 

    public static volatile SingularAttribute<Dnomina, BigDecimal> bono;
    public static volatile SingularAttribute<Dnomina, Cnomina> idCabecera;
    public static volatile SingularAttribute<Dnomina, Long> idDetalle;
    public static volatile SingularAttribute<Dnomina, Empleado> idEmpleado;
    public static volatile SingularAttribute<Dnomina, BigDecimal> descuento;
    public static volatile SingularAttribute<Dnomina, MotivoIngresoEgreso> codMotivo;
    public static volatile SingularAttribute<Dnomina, BigDecimal> sueldoBase;
    public static volatile SingularAttribute<Dnomina, BigDecimal> totalPagar;

}