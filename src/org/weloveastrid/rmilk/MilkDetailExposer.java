/**
 * See the file "LICENSE" for the full license governing this code.
 */
package org.weloveastrid.rmilk;

import org.weloveastrid.rmilk.data.MilkDataService;
import org.weloveastrid.rmilk.data.MilkNote;
import org.weloveastrid.rmilk.data.MilkTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.todoroo.andlib.TodorooCursor;
import com.todoroo.astrid.api.AstridApiConstants;
import com.todoroo.astrid.data.Metadata;

/**
 * Exposes Task Details for Remember the Milk:
 * - RTM list
 * - RTM repeat information
 * - RTM notes
 *
 * @author Tim Su <tim@todoroo.com>
 *
 */
public class MilkDetailExposer extends BroadcastReceiver {

    public static final String DETAIL_SEPARATOR = " | "; //$NON-NLS-1$

    @Override
    public void onReceive(Context context, Intent intent) {
        // if we aren't logged in, don't expose features
        if(!MilkUtilities.isLoggedIn())
            return;

        long taskId = intent.getLongExtra(AstridApiConstants.EXTRAS_TASK_ID, -1);
        if(taskId == -1)
            return;

        boolean extended = intent.getBooleanExtra(AstridApiConstants.EXTRAS_EXTENDED, false);
        String taskDetail = getTaskDetails(context, taskId, extended);
        if(taskDetail == null)
            return;

        Intent broadcastIntent = new Intent(AstridApiConstants.BROADCAST_SEND_DETAILS);
        broadcastIntent.putExtra(AstridApiConstants.EXTRAS_ADDON, MilkUtilities.IDENTIFIER);
        broadcastIntent.putExtra(AstridApiConstants.EXTRAS_TASK_ID, taskId);
        broadcastIntent.putExtra(AstridApiConstants.EXTRAS_EXTENDED, extended);
        broadcastIntent.putExtra(AstridApiConstants.EXTRAS_RESPONSE, taskDetail);
        context.sendBroadcast(broadcastIntent, AstridApiConstants.PERMISSION_READ);
    }

    public String getTaskDetails(Context context, long id, boolean extended) {
        Metadata metadata = MilkDataService.getInstance(context).getTaskMetadata(id);
        if(metadata == null)
            return null;

        StringBuilder builder = new StringBuilder();

        if(!extended) {
            long listId = metadata.getValue(MilkTask.LIST_ID);
            String listName = MilkDataService.getInstance(context).getListName(listId);
            // RTM list is out of date. don't display RTM stuff
            if(listName == null)
                return null;

            if(listId > 0 && !"Inbox".equals(listName)) { //$NON-NLS-1$
                builder.append("<img src='silk_folder'/> ").append(listName).append(DETAIL_SEPARATOR); //$NON-NLS-1$
            }

            int repeat = metadata.getValue(MilkTask.REPEATING);
            if(repeat != 0) {
                builder.append(context.getString(R.string.rmilk_TLA_repeat)).append(DETAIL_SEPARATOR);
            }
        } else {
            TodorooCursor<Metadata> notesCursor = MilkDataService.getInstance(context).getTaskNotesCursor(id);
            try {
                for(notesCursor.moveToFirst(); !notesCursor.isAfterLast(); notesCursor.moveToNext()) {
                    metadata.readFromCursor(notesCursor);
                    builder.append(MilkNote.toTaskDetail(metadata)).append(DETAIL_SEPARATOR);
                }
            } finally {
                notesCursor.close();
            }
        }

        if(builder.length() == 0)
            return null;
        String result = builder.toString();
        return result.substring(0, result.length() - DETAIL_SEPARATOR.length());
    }

}
