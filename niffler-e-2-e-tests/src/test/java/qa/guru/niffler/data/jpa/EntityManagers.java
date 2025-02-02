package qa.guru.niffler.data.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import qa.guru.niffler.data.tpl.DataSources;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class EntityManagers {
    private EntityManagers() {
    }

    private static final Map<String, EntityManagerFactory> emfs = new ConcurrentHashMap<>();

    public static EntityManager em(String jdbcUrl) {
        return new TreadSafeEntityManager(emfs.computeIfAbsent(
                jdbcUrl,
                kye -> {
                    DataSources.dataSource(jdbcUrl);
                    return Persistence.createEntityManagerFactory(jdbcUrl);
                }
        ).createEntityManager());
    }
}
