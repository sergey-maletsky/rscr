package com.firstlinesoftware.rmrs.client.desktop;

import com.firstlinesoftware.base.client.Base;
import com.firstlinesoftware.base.client.factories.NavigationPanelsFactory;
import com.firstlinesoftware.base.client.models.NavigatorModel;
import com.firstlinesoftware.base.shared.dto.DTO;
import com.firstlinesoftware.rmrs.client.models.RequirementsNavigatorModel;
import com.firstlinesoftware.rmrs.client.places.BrowseRequirementsPlace;
import com.firstlinesoftware.rmrs.shared.dto.Requirement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview_imported.client.SmartCellTree;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.TreeViewModel;

/**
 * User: VAntonov
 * Date: 8/2/12
 * Time: 11:44 AM
 */
public class RequirementsPanel extends SmartCellTree<DTO> implements NavigationPanelsFactory.NavigationPanel {
    //    private static final EventBus eventBus = Base.getInjector().getEventBus();
    public static final RequirementsNavigatorModel model = new RequirementsNavigatorModel();


    public RequirementsPanel() {
        super(initModel(), NavigatorModel.ROOT);
//        expandTree();
    }

    private static TreeViewModel initModel() {
        model.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent selectionChangeEvent) {
                final Requirement selectedFolder = model.getSelectedFolder();
                if (selectedFolder != null) {
                    Base.getInjector().getPlaceController().goTo(new BrowseRequirementsPlace(selectedFolder.id));
                }
            }
        });
        return model;
    }

    @Override
    public void unselect() {
        model.unselect();
    }

    @Override
    public boolean hasSelection() {
        return model.getSelectedFolder() != null;
    }

    @Override
    public HandlerRegistration addSelectionChangeHandler(SelectionChangeEvent.Handler handler) {
        return model.addSelectionChangeHandler(handler);
    }

}
