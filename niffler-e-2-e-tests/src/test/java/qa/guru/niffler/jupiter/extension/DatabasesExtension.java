package qa.guru.niffler.jupiter.extension;

import qa.guru.niffler.data.jpa.EntityManagers;
import qa.guru.niffler.data.tpl.Connections;

public class DatabasesExtension implements SuiteExtension {
    @Override
    public void afterSuit() {
        Connections.closeAllConnections();
        EntityManagers.closeAllEmfs();
    }
}
