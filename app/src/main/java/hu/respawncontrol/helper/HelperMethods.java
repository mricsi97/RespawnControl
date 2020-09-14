package hu.respawncontrol.helper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

public class HelperMethods {
    public static MediatorLiveData<List<Long>[]> zipLiveData(final LiveData<List<Long>>... liveDatas){
        final List<Long>[] zippedObjects = new ArrayList[liveDatas.length];
        final MediatorLiveData<List<Long>[]> mediator = new MediatorLiveData<>();
        for(int i = 0; i < liveDatas.length; i++) {
            final int index = i;
            final LiveData<List<Long>> liveData = liveDatas[i];
            mediator.addSource(liveData, new Observer<List<Long>>() {
                @Override
                public void onChanged(@Nullable List<Long> timeList) {
                    zippedObjects[index] = timeList;

                    for(int j = 0; j < zippedObjects.length; j++) {
                        if(zippedObjects[j] == null) {
                            return;
                        }
                    }
                    mediator.setValue(zippedObjects);
                    for(int i = 0; i < liveDatas.length; i++) {
                        mediator.removeSource(liveDatas[i]);
                    }
                }
            });
        }
        return mediator;
    }
}
