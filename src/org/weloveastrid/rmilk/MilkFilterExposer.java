/**
 * See the file "LICENSE" for the full license governing this code.
 */
package org.weloveastrid.rmilk;

import org.weloveastrid.rmilk.data.MilkDataService;
import org.weloveastrid.rmilk.data.MilkList;
import org.weloveastrid.rmilk.data.MilkTask;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import com.todoroo.andlib.Criterion;
import com.todoroo.andlib.Join;
import com.todoroo.andlib.QueryTemplate;
import com.todoroo.astrid.api.AstridApiConstants;
import com.todoroo.astrid.api.Filter;
import com.todoroo.astrid.api.FilterCategory;
import com.todoroo.astrid.api.FilterListHeader;
import com.todoroo.astrid.api.FilterListItem;
import com.todoroo.astrid.data.Metadata;
import com.todoroo.astrid.data.StoreObject;
import com.todoroo.astrid.data.Task;
import com.todoroo.astrid.data.MetadataDao.MetadataCriteria;
import com.todoroo.astrid.data.TaskDao.TaskCriteria;

/**
 * Exposes filters based on RTM lists
 *
 * @author Tim Su <tim@todoroo.com>
 *
 */
public class MilkFilterExposer extends BroadcastReceiver {

    private Filter filterFromList(Context context, StoreObject list) {
        String listName = list.getValue(MilkList.NAME);
        String title = context.getString(R.string.rmilk_FEx_list_title,
                listName);
        ContentValues values = new ContentValues();
        values.put(Metadata.KEY.name, MilkTask.METADATA_KEY);
        values.put(MilkTask.LIST_ID.name, list.getValue(MilkList.REMOTE_ID));
        values.put(MilkTask.TASK_SERIES_ID.name, 0);
        values.put(MilkTask.TASK_ID.name, 0);
        values.put(MilkTask.REPEATING.name, 0);
        Filter filter = new Filter(listName, title, new QueryTemplate().join(
                Join.left(Metadata.TABLE, Task.ID.eq(Metadata.TASK))).where(Criterion.and(
                        MetadataCriteria.withKey(MilkTask.METADATA_KEY),
                        TaskCriteria.activeAndVisible(),
                        MilkTask.LIST_ID.eq(list.getValue(MilkList.REMOTE_ID)))),
                values);

        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // if we aren't logged in, don't expose features
        if(!MilkUtilities.isLoggedIn())
            return;

        StoreObject[] lists = MilkDataService.getInstance(context).getLists();

        // If user does not have any tags, don't show this section at all
        if(lists.length == 0)
            return;

        Filter[] listFilters = new Filter[lists.length];
        for(int i = 0; i < lists.length; i++)
            listFilters[i] = filterFromList(context, lists[i]);

        FilterListHeader rtmHeader = new FilterListHeader(context.getString(R.string.rmilk_FEx_header));
        FilterCategory rtmLists = new FilterCategory(context.getString(R.string.rmilk_FEx_list),
                listFilters);

        // transmit filter list
        FilterListItem[] list = new FilterListItem[2];
        list[0] = rtmHeader;
        list[1] = rtmLists;
        Intent broadcastIntent = new Intent(AstridApiConstants.BROADCAST_SEND_FILTERS);
        broadcastIntent.putExtra(AstridApiConstants.EXTRAS_ADDON, MilkUtilities.IDENTIFIER);
        broadcastIntent.putExtra(AstridApiConstants.EXTRAS_RESPONSE, list);
        context.sendBroadcast(broadcastIntent, AstridApiConstants.PERMISSION_READ);
    }

}
