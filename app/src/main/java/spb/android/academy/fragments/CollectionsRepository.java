package spb.android.academy.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spb.android.academy.fragments.domain.Collection;

public class CollectionsRepository {

    private static final CollectionsRepository INSTANCE = new CollectionsRepository();

    @NonNull
    public static CollectionsRepository getInstance() {
        return INSTANCE;
    }

    @NonNull
    private SparseArray<Collection> storage;

    private CollectionsRepository() {
        this.storage = new SparseArray<>();
    }

    @Nullable
    public Collection getById(int id) {
        return storage.get(id);
    }

    public void save(Collection collection) {
        storage.put(collection.getId(), collection);
    }

    @NonNull
    public List<Collection> getAll() {
        List<Collection> collections = new ArrayList<>();
        for (int i = 0; i < storage.size(); i++) {
            int key = storage.keyAt(i);
            collections.add(storage.get(key));
        }
        return collections;
    }
}
