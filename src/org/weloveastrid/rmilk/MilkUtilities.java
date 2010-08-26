/**
 * See the file "LICENSE" for the full license governing this code.
 */
package org.weloveastrid.rmilk;

import com.todoroo.astrid.sync.SyncProviderUtilities;

/**
 * Constants and preferences for RTM plugin
 *
 * @author Tim Su <tim@todoroo.com>
 *
 */
@SuppressWarnings("nls")
public class MilkUtilities extends SyncProviderUtilities {

    // --- constants

    /** add-on identifier */
    public static final String IDENTIFIER = "rmilk";

    public static final MilkUtilities INSTANCE = new MilkUtilities();

    // --- utilities boilerplate

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public int getSyncIntervalKey() {
        return R.string.rmilk_MPr_interval_key;
    }

}
