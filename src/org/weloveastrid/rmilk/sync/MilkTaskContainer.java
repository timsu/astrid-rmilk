package org.weloveastrid.rmilk.sync;

import java.util.ArrayList;
import java.util.Iterator;

import org.weloveastrid.misc.TaskContainer;
import org.weloveastrid.rmilk.api.data.RtmTaskSeries;
import org.weloveastrid.rmilk.data.MilkTask;

import com.todoroo.astrid.data.Metadata;
import com.todoroo.astrid.data.Task;

/**
 * RTM Task Container
 *
 * @author Tim Su <tim@todoroo.com>
 *
 */
public class MilkTaskContainer extends TaskContainer {
    public long listId, taskSeriesId, taskId;
    public boolean repeating;
    public RtmTaskSeries remote;

    public MilkTaskContainer(Task task, ArrayList<Metadata> metadata,
            long listId, long taskSeriesId, long taskId, boolean repeating,
            RtmTaskSeries remote) {
        this.task = task;
        this.metadata = metadata;
        this.listId = listId;
        this.taskSeriesId = taskSeriesId;
        this.taskId = taskId;
        this.repeating = repeating;
        this.remote = remote;
    }

    public MilkTaskContainer(Task task, ArrayList<Metadata> metadata,
            RtmTaskSeries rtmTaskSeries) {
        this(task, metadata, Long.parseLong(rtmTaskSeries.getList().getId()),
                Long.parseLong(rtmTaskSeries.getId()), Long.parseLong(rtmTaskSeries.getTask().getId()),
                rtmTaskSeries.hasRecurrence(), rtmTaskSeries);
    }

    public MilkTaskContainer(Task task, ArrayList<Metadata> metadata) {
        this(task, metadata, 0, 0, 0, false, null);
        for(Iterator<Metadata> iterator = metadata.iterator(); iterator.hasNext(); ) {
            Metadata item = iterator.next();
            if(MilkTask.METADATA_KEY.equals(item.getValue(Metadata.KEY))) {
                if(item.containsNonNullValue(MilkTask.LIST_ID))
                    listId = item.getValue(MilkTask.LIST_ID);
                if(item.containsNonNullValue(MilkTask.TASK_SERIES_ID))
                    taskSeriesId = item.getValue(MilkTask.TASK_SERIES_ID);
                if(item.containsNonNullValue(MilkTask.TASK_ID))
                    taskId = item.getValue(MilkTask.TASK_ID);
                if(item.containsNonNullValue(MilkTask.REPEATING))
                    repeating = item.getValue(MilkTask.REPEATING) == 1;
                iterator.remove();
                break;
            }
        }
    }


}