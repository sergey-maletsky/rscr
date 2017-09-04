package com.firstlinesoftware.rmrs.client.activities;

import com.firstlinesoftware.ecm.client.views.viewers.CompositeDocumentViewImpl;
import com.firstlinesoftware.rmrs.shared.dto.Requirement;
import com.firstlinesoftware.route.client.activities.ViewAbstractRouteActivity;

/**
 * User: VAntonov
 * Date: 15.12.2010
 * Time: 15:57:57
 */
public class ViewRequirementActivity extends ViewAbstractRouteActivity<Requirement, CompositeDocumentViewImpl<Requirement>> {

    public ViewRequirementActivity(final Requirement requirement) {
        super(requirement);
    }
}
