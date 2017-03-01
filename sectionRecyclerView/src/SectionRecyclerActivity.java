package eebochina.com.testtechniques.pinnedsection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eebochina.com.testtechniques.R;

public class SectionRecyclerActivity extends AppCompatActivity {

    public static final int TITLE_TYPE = 22;
    public static final int ITEM_TYPE = 33;

    private RecyclerView mRecyclerView;

    private TextView mTopTitle;

    private CalendarListAdapter mAdapter;
    private List<TestEntity> mDatas;
    private LinearLayoutManager mLayoutManager;

    private LayoutInflater mInflater;
    private FrameLayout.LayoutParams mTopTitleLayoutParams;
    private int mMaxHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_recycler);

        mRecyclerView = (RecyclerView) findViewById(R.id.section_list);
        mTopTitle = (TextView) findViewById(R.id.item_calendar_list_title_text);

        mDatas = new ArrayList<>();
        mInflater = LayoutInflater.from(this);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CalendarListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mTopTitleLayoutParams = (FrameLayout.LayoutParams) mTopTitle.getLayoutParams();
        addFakeData();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = (mLayoutManager.findFirstVisibleItemPosition() < 0) ? 0 : mLayoutManager.findFirstVisibleItemPosition();
                int totalSize = mLayoutManager.getItemCount();
                if (mAdapter.getItemViewType(position) == TITLE_TYPE) {
                    mTopTitle.setText(mDatas.get(position).content);
                }
                int nextPosition = position + 1;
                if (nextPosition < totalSize) {
                    //获取下个章节标题的位置， 如果小于0说明后面没有章节标题。 如果下个章节标题位置还未显示出来，也不用做改变。
                    int sectionPosition = findNextSection(nextPosition, totalSize);
                    if (sectionPosition < 0 || sectionPosition > (position + mRecyclerView.getChildCount() - 1)) {
                        refreshTopTitle();
                        return;
                    }

                    //获取下一个章节标题的top， 如果top 大于 展示label的高度， 无须做改动。
                    int nextTopMargin = mLayoutManager.findViewByPosition(sectionPosition).getTop();
                    if (nextTopMargin < mMaxHeight) {
                        mTopTitleLayoutParams.topMargin = -(mMaxHeight - nextTopMargin);
                        mTopTitle.setLayoutParams(mTopTitleLayoutParams);
                        //如果当前第一个显示的不是章节标题， 需要显示上一个章节标题
                        if (mAdapter.getItemViewType(position) != TITLE_TYPE) {
                            mTopTitle.setText(findPreSectionText(position));
                        }
                    } else {
                        refreshTopTitle();
                    }
                }
            }
        });
    }

    private void refreshTopTitle() {
        if (mTopTitleLayoutParams.topMargin == 0) return;
        mTopTitleLayoutParams.topMargin = 0;
        mTopTitle.setLayoutParams(mTopTitleLayoutParams);
    }

    private int findNextSection(int position, int size) {
        for (; position < size; position++) {
            if (mAdapter.getItemViewType(position) == TITLE_TYPE) return position;
        }
        return -1;
    }

    private String findPreSectionText(int position) {
        for (; position > -1; position--) {
            if (mAdapter.getItemViewType(position) == TITLE_TYPE) return mDatas.get(position).content;
        }
        return "";
    }


    private void addFakeData() {
        TestEntity testEntity;
        for (int i = 0, j = 6; i < 50; i++) {
            if (i % j == 0) {
                testEntity = new TestEntity("标题" + i, TITLE_TYPE);
            } else {
                testEntity = new TestEntity("内容" + i, ITEM_TYPE);
            }
            mDatas.add(testEntity);
        }
    }

    private boolean isInit = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //当activity可以获取焦点的时候，获取固定标题栏的高度
        if (hasFocus && !isInit) {
            isInit = true;
            mMaxHeight = mTopTitle.getHeight();
        }
    }

    class CalendarListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TITLE_TYPE:
                    return new CalendarTitleViewHolder(mInflater.inflate(R.layout.item_section_list_title, parent, false));
                case ITEM_TYPE:
                    return new CalendarItemViewHolder(mInflater.inflate(R.layout.item_section_list_item, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TestEntity testEntity = mDatas.get(position);
            switch (testEntity.type) {
                case TITLE_TYPE:
                    CalendarTitleViewHolder titleViewHolder = (CalendarTitleViewHolder) holder;
                    titleViewHolder.mTitle.setText(testEntity.content);
                    break;
                case ITEM_TYPE:
                    CalendarItemViewHolder itemViewHolder = (CalendarItemViewHolder) holder;
                    itemViewHolder.mContent.setText(testEntity.content);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mDatas.get(position).type;
        }
    }


    class CalendarTitleViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        public CalendarTitleViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.item_calendar_list_title_text);
        }
    }

    class CalendarItemViewHolder extends RecyclerView.ViewHolder {

        TextView mLabel;
        TextView mContent;

        public CalendarItemViewHolder(View itemView) {
            super(itemView);
            mLabel = (TextView) itemView.findViewById(R.id.item_calendar_list_item_label);
            mContent = (TextView) itemView.findViewById(R.id.item_calendar_list_item_content);
        }
    }

    class TestEntity {
        String content;
        int type;


        public TestEntity(String content, int type) {
            this.content = content;
            this.type = type;
        }
    }
}
