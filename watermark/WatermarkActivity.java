package eebochina.com.testtechniques.watermark;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eebochina.com.testtechniques.R;
import eebochina.com.testtechniques.nestedScroll.NestedScrollActivity;

public class WatermarkActivity extends AppCompatActivity {
    
    private RecyclerView mRecyclerView;
    private LayoutInflater mInflater;
    private List<String> mContentDatas;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watermark);
        mInflater = LayoutInflater.from(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.watermark_content);
        
        mContentDatas = new ArrayList<>();
        final LinearLayoutManager contentManager = new LinearLayoutManager(this);
        contentManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(contentManager);
        for (int i = 0; i < 50; i++) {
            mContentDatas.add("测试水印" + i);
        }
        mRecyclerView.setAdapter(new ContentAdapter());
        setWatermark();
    }
    
    private WatermarkDecoration mWatermarkDecoration;
    
    private void setWatermark() {
//        WatermarkDecoration.Builder builder = new WatermarkDecoration.Builder("单个水印")
//                .setColumnNum(3)
//                .setTextColor(Color.GRAY)
//                .setTextSize(35);
        //多个水印
        WatermarkDecoration.Builder builder = new WatermarkDecoration.Builder(getMultiple())
                .setColumnNum(3)
                .setTextColor(Color.GRAY)
                .setTextSize(35);
        
        mWatermarkDecoration = builder.builder();
        
        mRecyclerView.addItemDecoration(mWatermarkDecoration);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //设置水印滚动位置
                mWatermarkDecoration.setScrollY(dy);
            }
        });
    }
    
    private List<String> getMultiple() {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add("多个水印" + i);
        }
        return strings;
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
