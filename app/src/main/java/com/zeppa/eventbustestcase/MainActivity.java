package com.zeppa.eventbustestcase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.zeppa.eventbustestcase.events.AsyncEvent;
import com.zeppa.eventbustestcase.events.DataEvent;
import com.zeppa.eventbustestcase.events.StartEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private final EventBus bus = EventBus.getDefault();
    private RecyclerView list;
    private Adapter adapter;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        start = findViewById(R.id.start);
        adapter = new Adapter();
        list.setAdapter(adapter);
        list.setLayoutManager(new GridLayoutManager(list.getContext(), 5));
    }

    @Override
    protected void onStart() {
        super.onStart();
        start.setOnClickListener(v -> {
            adapter.clear();
            bus.post(new StartEvent());
        });
        bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.post(new StartEvent());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onStart(StartEvent event) {
        for (int i = 0; i < 40; i++) {
            bus.post(new AsyncEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAsyncEvent(AsyncEvent event) {
        bus.post(DataEvent.newInstance());
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onDataEvent(DataEvent event) {
        Log.d(TAG, event.toString());
        adapter.add(event);
    }
}
