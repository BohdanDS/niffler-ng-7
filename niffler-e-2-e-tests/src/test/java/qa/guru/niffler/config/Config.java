package qa.guru.niffler.config;

public interface Config {

    static Config getInstance() {
        return LocalConfig.INSTANCE;
    }

    String frontUrl();

    String spendUrl();

    String authUrl();

    String gateWayUrl();

    String userDataUrl();

    String ghUrl();
}
