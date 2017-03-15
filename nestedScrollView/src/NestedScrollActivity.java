package eebochina.com.testtechniques.nestedScroll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eebochina.com.testtechniques.R;

public class NestedScrollActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LayoutInflater mInflater;
    private List<String> mContentDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll);

        mRecyclerView = (RecyclerView) findViewById(R.id.nested_scroll_recycler);

        mInflater = LayoutInflater.from(this);
        mContentDatas = new ArrayList<>();
        final LinearLayoutManager contentManager = new LinearLayoutManager(this);
        contentManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(contentManager);
        for (int i = 0; i < 50; i++) {
            mContentDatas.add("str" + i);
        }
        mRecyclerView.setAdapter(new ContentAdapter());
    }


    class ContentAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContentViewHolder(mInflater.inflate(R.layout.item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ContentViewHolder viewHolder = (ContentViewHolder) holder;
            viewHolder.mTextView.setText(mContentDatas.get(position));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NestedScrollActivity.this, mContentDatas.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mContentDatas.size();
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_layout_text);
        }
    }
}
