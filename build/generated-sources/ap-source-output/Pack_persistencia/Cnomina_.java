package Pack_persistencia;

import Pack_persistencia.Dnomina;
import Pack_persistencia.Empleado;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-05-25T13:06:05")
@StaticMetamodel(Cnomina.class)
public class Cnomina_ { 

    public static volatile SingularAttribute<Cnomina, Long> idCabecera;
    public static volatile SingularAttribute<Cnomina, BigDecimal> totalPagado;
    public static volatile SingularAttribute<Cnomina, Empleado> idEmpleado;
    public static volatile ListAttribute<Cnomina, Dnomina> dnominaList;
    public static volatile SingularAttribute<Cnomina, Date> fechaPago;

}