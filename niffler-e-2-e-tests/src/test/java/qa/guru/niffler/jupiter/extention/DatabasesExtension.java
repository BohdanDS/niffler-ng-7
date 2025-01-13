package qa.guru.niffler.jupiter.extention;

import qa.guru.niffler.data.Databases;

public class DatabasesExtension implements SuiteExtension {
    @Override
    public void afterSuit() {
        Databases.closeAllConnections();
    }
}
