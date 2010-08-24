package org.weloveastrid.misc;

import java.util.ArrayList;

import com.todoroo.andlib.AndroidUtilities;
import com.todoroo.astrid.data.Metadata;
import com.todoroo.astrid.data.Task;

/**
 * Container class for transmitting tasks and including local and remote
 * metadata. Synchronization Providers can subclass this class if desired.
 *
 * @see {@link SyncProvider}
 * @author Tim Su <tim@todoroo.com>
 *
 */
public class TaskContainer {
    public Task task;
    public ArrayList<Metadata> metadata;

    /**
     * Check if the metadata contains anything with the given key
     * @param key
     * @return first match. or null
     */
    public Metadata findMetadata(String key) {
        for(Metadata item : metadata) {
            if(AndroidUtilities.equals(key, item.getValue(Metadata.KEY)))
                return item;
        }
        return null;
    }

}