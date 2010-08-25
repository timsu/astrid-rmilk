package org.weloveastrid.rmilk;

import org.weloveastrid.misc.SyncProviderPreferences;
import org.weloveastrid.misc.SyncProviderUtilities;
import org.weloveastrid.rmilk.sync.MilkSyncProvider;

/**
 * Displays synchronization preferences and an action panel so users can
 * initiate actions from the menu.
 *
 * @author Tim Su <tim@todoroo.com>
 *
 */
public class MilkPreferences extends SyncProviderPreferences {

    @Override
    public int getPreferenceResource() {
        return R.xml.preferences_rmilk;
    }

    @Override
    public void startSync() {
        new MilkSyncProvider().synchronize(this);
    }

    @Override
    public void logOut() {
        new MilkSyncProvider().signOut(this);
    }

    @Override
    public SyncProviderUtilities getUtilities() {
        return MilkUtilities.INSTANCE;
    }

}