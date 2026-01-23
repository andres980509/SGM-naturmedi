package ETI.sgc.config;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class Database {

    public static Jdbi jdbi;

    public static void init() {
        jdbi = Jdbi.create(
                "jdbc:postgresql://localhost:5432/sgc",
                "postgres",
                "123"
        );

        // AGREGA ESTA LÍNEA AQUÍ ABAJO:
        jdbi.installPlugin(new SqlObjectPlugin());
    }
}
