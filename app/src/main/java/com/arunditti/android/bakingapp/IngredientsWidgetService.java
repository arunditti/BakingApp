package com.arunditti.android.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by arunditti on 7/5/18.
 */

public class IngredientsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(this.getApplicationContext());
    }
}
