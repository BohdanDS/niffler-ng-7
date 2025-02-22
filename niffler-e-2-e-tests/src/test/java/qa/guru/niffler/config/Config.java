package qa.guru.niffler.config;

public interface Config {

    static Config getInstance() {
        return "docker".equals(System.getProperty("test.env"))
                ? DockerConfig.INSTANCE
                : LocalConfig.INSTANCE;

    }

    String frontUrl();

    String spendUrl();

    String spendJdbcUrl();

    String authUrl();

    String authJdbcUrl();

    String gateWayUrl();

    String userDataUrl();

    String userDataJdbcUrl();

    String currencyJdbcUrl();

    String ghUrl();
}
