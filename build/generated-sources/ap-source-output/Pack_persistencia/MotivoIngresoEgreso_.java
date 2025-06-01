package Pack_persistencia;

import Pack_persistencia.Dnomina;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-05-25T13:06:05")
@StaticMetamodel(MotivoIngresoEgreso.class)
public class MotivoIngresoEgreso_ { 

    public static volatile SingularAttribute<MotivoIngresoEgreso, Long> codigo;
    public static volatile SingularAttribute<MotivoIngresoEgreso, String> tipo;
    public static volatile ListAttribute<MotivoIngresoEgreso, Dnomina> dnominaList;
    public static volatile SingularAttribute<MotivoIngresoEgreso, String> nombre;

}