package qa.guru.niffler.jupiter.extention;

import qa.guru.niffler.data.tpl.Connections;

public class DatabasesExtension implements SuiteExtension {
    @Override
    public void afterSuit() {
        Connections.closeAllConnections();
    }
}
