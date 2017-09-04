package com.firstlinesoftware.rmrs.server.importers;

import com.firstlinesoftware.base.server.importers.AbstractImporter;
import com.firstlinesoftware.base.server.importers.ImportStrategy;
import com.firstlinesoftware.base.server.importers.impl.DirectoriesUpdateStrategy;
import com.firstlinesoftware.base.shared.dto.Directory;
import com.firstlinesoftware.orgstruct.server.importers.impl.OrgstructUpdateByExternalAspectStrategy;
import com.firstlinesoftware.orgstruct.shared.directories.OrgstructDirectories;
import com.firstlinesoftware.orgstruct.shared.dto.Orgstructure;
import com.firstlinesoftware.orgstruct.shared.dto.Position;
import com.firstlinesoftware.rmrs.server.adapters.TezisRestAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class TezisRestOrgstructImporter extends AbstractImporter<Orgstructure, Orgstructure, String> {
    @Autowired
    private OrgstructUpdateByExternalAspectStrategy orgstructUpdateStrategy;

    @Autowired
    private DirectoriesUpdateStrategy directoriesUpdateStrategy;

    @Autowired
    private TezisRestAdapter adapter;

    @Value("${tezis.sync.rest.url}")
    private String url;

    public void doImport() {
        doImport(adapter, new ImportStrategy<Orgstructure>() {
            @Override
            public void startImport() {

            }

            @Override
            public void importItem(Orgstructure item) {
                orgstructUpdateStrategy.importItem(item);
                if(item.positions != null) {
                    final Set<String> names = new HashSet<String>();
                    for (Position position : item.positions) {
                        names.add(position.getName());
                    }
                    final Directory d = new Directory();
                    d.type = OrgstructDirectories.POSITIONS.getType();
                    d.items = new Directory.Items();
                    d.items.items = new ArrayList<>();
                    for (String name : names) {
                        final Directory.Item e = new Directory.Item(name, name);
                        e.type = d.type;
                        d.items.items.add(e);
                    }
                    directoriesUpdateStrategy.importItem(d);
                }
            }

            @Override
            public void stopImport() {

            }
        }, url);
    }
}
