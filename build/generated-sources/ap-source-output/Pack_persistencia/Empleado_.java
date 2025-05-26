package Pack_persistencia;

import Pack_persistencia.Cnomina;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-05-25T13:06:05")
@StaticMetamodel(Empleado.class)
public class Empleado_ { 

    public static volatile SingularAttribute<Empleado, BigDecimal> idEmpleado;
    public static volatile SingularAttribute<Empleado, Date> ingreso;
    public static volatile SingularAttribute<Empleado, Long> cedula;
    public static volatile SingularAttribute<Empleado, BigInteger> sueldo;
    public static volatile ListAttribute<Empleado, Cnomina> cnominaList;
    public static volatile SingularAttribute<Empleado, String> nombre;

}