package qa.guru.niffler.config;

enum DockerConfig implements Config {
    INSTANCE;

    @Override
    public String frontUrl() {
        return "";
    }

    @Override
    public String spendUrl() {
        return "";
    }

    @Override
    public String authUrl() {
        return "";
    }

    @Override
    public String gateWayUrl() {
        return "";
    }

    @Override
    public String userDataUrl() {
        return "";
    }

    @Override
    public String spendJdbcUrl() {
        return "";
    }

    @Override
    public String authJdbcUrl() {
        return "";
    }

    @Override
    public String userDataJdbcUrl() {
        return "";
    }

    @Override
    public String currencyJdbcUrl() {
        return "";
    }

    @Override
    public String ghUrl() {
        return "";
    }
}


