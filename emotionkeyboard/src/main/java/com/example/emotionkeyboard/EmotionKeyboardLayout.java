package com.example.emotionkeyboard;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.emotionkeyboard.adapter.recycleradaper.BaseRecyclerAdapter;
import com.example.emotionkeyboard.decoration.FlexibleDividerDecoration;
import com.example.emotionkeyboard.decoration.VerticalDividerItemDecoration;
import com.example.emotionkeyboard.entity.EmotionEntity;
import com.example.emotionkeyboard.entity.SingleEmotion;
import com.example.emotionkeyboard.util.FileUtil;
import com.example.emotionkeyboard.view.EmotionFragment;
import com.example.emotionkeyboard.view.EmotionTitleAdapter;
import com.example.emotionkeyboard.view.EmotionVpAdapter;
import com.example.emotionkeyboard.view.EmotionVpIndicator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.emotionkeyboard.view.EmotionFragment.COLUMN_COUNT_EMOTION;
import static com.example.emotionkeyboard.view.EmotionFragment.COLUMN_COUNT_IMG;
import static com.example.emotionkeyboard.view.EmotionFragment.OnChangeLocalPicListener;
import static com.example.emotionkeyboard.view.EmotionFragment.OnClickPicListener;
import static com.example.emotionkeyboard.view.EmotionFragment.ROW_COUNT_EMOTION;
import static com.example.emotionkeyboard.view.EmotionFragment.ROW_COUNT_IMG;
import static com.example.emotionkeyboard.view.EmotionFragment.getInstance;


/*
 使用：
     xml布局：
       <RelativeLayout
         android:layout_width="match_parent"
         android:layout_height="58dp"
         android:background="@android:color/darker_gray">

             <ImageButton
             android:id="@+id/ib"
             android:layout_width="48dp"
             android:layout_height="48dp"
             android:layout_centerVertical="true"/>

             <EditText
             android:id="@+id/et_input"
             android:layout_width="200dp"
             android:layout_height="wrap_content"
             android:layout_centerVertical="true"
             android:layout_toRightOf="@id/ib"/>

             <Button
             android:id="@+id/send"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             android:layout_alignParentRight="true"
             android:layout_centerVertical="true"
             android:text="发送"/>
        </RelativeLayout>

         <!-- 键盘布局 -->
         <com.example.testinput.keyboard.EmotionKeyboardLayout
         android:visibility="gone"
         android:id="@+id/emotion_keyboard_layout"
         android:layout_width="wrap_content"
         android:layout_height="wrap_content"/>

    Java代码初始化表情数据及相应点击事件：
         // 一、初始化键盘管理类
        EmotionKeyboardManager.with(this)
                .setEmotionView(mEmotionKeyboardLayout)
                .bindToContent(mRlContent)
                .bindToEditText(mEtInput)
                .bindToEmotionButton(mIbChangeEmotion)
                .build();

        //图片表情的点击事件回调
        EmotionFragment.OnClickPicListener onClickPicListener = new EmotionFragment.OnClickPicListener() {
            @Override
            public void onClickPic(String theme, int resId) {
                mChatListAdapter.add(resId + "");
            }
        };

        // 二、初始化数据源
        mEmotionKeyboardLayout.createEmotionFragment(true, "theme1", EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE1), null);
        mEmotionKeyboardLayout.createEmotionFragment(false, "theme2", EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE2), onClickPicListener);
        mEmotionKeyboardLayout.createEmotionFragment(true, "theme3", EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE3), null);

        // 三、初始化布局加载数据源
        mEmotionKeyboardLayout.loadEmotionVp(getSupportFragmentManager());
 */

/**
 * Created by chasing on 2017/12/14.
 * 表情键盘布局
 * 所在的父布局需要是LinearLayout，内容区weight为1，占满上部分
 * EmotionKeyboardLayout布局在最下面
 */
public class EmotionKeyboardLayout extends ViewGroup {
    private ViewPager mVpEmotion;
    private RecyclerView mRcvEmotionTitle;
    private EmotionVpIndicator mVpIndicator;

    private EditText mEditText;

    private EmotionVpAdapter mEmotionVpAdapter;
    private EmotionTitleAdapter mEmotionTitleAdapter;//主题栏
    private FragmentManager mFragmentManager;

    private List<Fragment> mVpFragments;
    private List<String> mEmotionTitles; //viewIndicator使用
    private List<Map<String, String>> mEmotionTitleWithIcon;//RcvTitle使用
    private ArrayList<EmotionEntity> emotionEntities;//所有表情页
    private List<Fragment> mLocalFragments;
    private ArrayList<EmotionEntity> localEntities;

    public EmotionKeyboardLayout(Context context) {
        super(context);
        init();
    }

    public EmotionKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmotionKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.emotion_keyboard, this);
        mRcvEmotionTitle = (RecyclerView) view.findViewById(R.id.rcv_emotion_title);
        mVpIndicator = (EmotionVpIndicator) view.findViewById(R.id.vp_indicator);
        mVpEmotion = (ViewPager) view.findViewById(R.id.vp_emotion);

        emotionEntities = new ArrayList<>();
        mVpFragments = new ArrayList<>();
        mEmotionTitleWithIcon = new ArrayList<>();
        mEmotionTitles = new ArrayList<>();
        mLocalFragments = new ArrayList<>();
        localEntities = new ArrayList<>();

        initRcvEmotionTitle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        final View child = getChildAt(0);
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            getChildAt(0).measure(
                    MeasureSpec.makeMeasureSpec(
                            getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                            MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(
                            getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
                            MeasureSpec.EXACTLY));
        }
    }

    /**
     * 初始化话底部主题栏
     */
    private void initRcvEmotionTitle() {
        mEmotionTitleAdapter = new EmotionTitleAdapter(getContext(), R.layout.item_emotion_title);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRcvEmotionTitle.setLayoutManager(linearLayoutManager);
        mRcvEmotionTitle.addItemDecoration(new VerticalDividerItemDecoration.Builder(getContext())
                .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int position, RecyclerView parent) {
                        return false;
                    }
                }).colorResId(android.R.color.darker_gray).build());
        mRcvEmotionTitle.setAdapter(mEmotionTitleAdapter);
        mEmotionTitleAdapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Map<String, String> item = mEmotionTitleAdapter.getItem(position);
                mVpIndicator.play(item.get(EmotionTitleAdapter.TITLE));
                mEmotionTitleAdapter.setSelectPosition(position);
            }
        });
    }

    public void bindEditText(EditText editText) {
        mEditText = editText;
    }

    /**
     * 调用之前先设置createEmotionFragment进行配置数据
     */
    public void loadEmotionVp(FragmentManager fragmentManager) {
        if (emotionEntities == null || emotionEntities.size() == 0)
            throw new IllegalArgumentException("Please call createEmotionFragment() method first!");
        mFragmentManager = fragmentManager;
        mEmotionVpAdapter = new EmotionVpAdapter(fragmentManager, mVpFragments);
        mVpEmotion.setAdapter(mEmotionVpAdapter);
        mVpIndicator.bindViewPager(mVpEmotion, emotionEntities, mEmotionTitles);
        mEmotionTitleAdapter.addAll(mEmotionTitleWithIcon);

        mVpEmotion.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mEmotionTitleAdapter.setSelectPosition(mVpIndicator.getCurrentTitlePosition());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 根据表情集合Map进行创建EmotionFragment
     *
     * @param isEmotion          是否是表情 true为表情 false为图片
     * @param title              表情标签主题
     * @param emotionMap         表情集合
     * @param onClickPicListener 图片的点击事件，表情的话传null
     */
    public void createEmotionFragment(boolean isEmotion, String title, int titleIconId, LinkedHashMap<String, Integer> emotionMap,
                                      OnClickPicListener onClickPicListener) {
        Map<String, String> mapTitleIcon = new HashMap<>();
        mapTitleIcon.put(EmotionTitleAdapter.TITLE, title);
        mapTitleIcon.put(EmotionTitleAdapter.ICON, String.valueOf(titleIconId));
        mEmotionTitleWithIcon.add(mapTitleIcon);
        mEmotionTitles.add(title);
        int emotionSize, childPageCount;
        if (isEmotion) {
            emotionSize = COLUMN_COUNT_EMOTION * ROW_COUNT_EMOTION - 1;
        } else {
            emotionSize = COLUMN_COUNT_IMG * ROW_COUNT_IMG;
        }
        if (emotionMap.size() % emotionSize == 0) {
            childPageCount = emotionMap.size() / emotionSize;
        } else {
            childPageCount = emotionMap.size() / emotionSize + 1;
        }
        EmotionEntity entity = null;
        for (Map.Entry<String, Integer> map : emotionMap.entrySet()) {
            SingleEmotion emotion = new SingleEmotion();
            emotion.setEmotionName(map.getKey())
                    .setEmotionResId(map.getValue());
            if (entity == null || entity.getEmotions().size() == emotionSize) {
                entity = createEmotionEntity(title, titleIconId, childPageCount, isEmotion, onClickPicListener);
            }
            entity.getEmotions().add(emotion);
        }
    }

    /**
     * 初始化本地的图片表情
     * 并在最后添加一个“add”按钮
     */
    public void initLocalFileEmotion(OnClickPicListener onClickPicListener) {
        //加载标题
        String title = "other";
        int titleIconId = R.mipmap.add_pic_bg_n;
        Map<String, String> mapTitleIcon = new HashMap<>();
        mapTitleIcon.put(EmotionTitleAdapter.TITLE, title);
        mapTitleIcon.put(EmotionTitleAdapter.ICON, String.valueOf(titleIconId));
        mEmotionTitleWithIcon.add(mapTitleIcon);
        mEmotionTitles.add(title);

        resetLocalFileEmotion(onClickPicListener);
    }

    /**
     * 设置本地图片的fragment
     *
     * @param onClickPicListener 图片的点击事件
     */
    public void resetLocalFileEmotion(final OnClickPicListener onClickPicListener) {
        String title = "other";
        int titleIconId = R.mipmap.add_pic_bg_n;

        File[] emotionFiles = FileUtil.getEmotionFiles();
        int emotionSize = COLUMN_COUNT_IMG * ROW_COUNT_IMG;
        int childPageCount;
        if (emotionFiles != null && emotionFiles.length > 0) {
            int totalCount = emotionFiles.length + 1;
            if (totalCount % emotionSize == 0) {
                childPageCount = totalCount / emotionSize;
            } else {
                childPageCount = totalCount / emotionSize + 1;
            }
        } else {
            childPageCount = 1;
        }

        SingleEmotion emotionAdd = new SingleEmotion();
        emotionAdd.setEmotionName("add")
                .setEmotionResId(R.mipmap.add_pic_bg);
        EmotionEntity entity = createEmotionEntity(title, titleIconId, childPageCount, false, true, onClickPicListener, new OnChangeLocalPicListener() {
            @Override
            public void onChangeLocalPic() {
                // 每次用户添加删除完本地图片之后刷新所有本地图片的fragment
                removeLocalEmotionFragment();
                resetLocalFileEmotion(onClickPicListener);
                mEmotionVpAdapter.setDatas(mVpFragments);
                mVpIndicator.resetEmotionEntity(emotionEntities);
            }
        });
        entity.getEmotions().add(emotionAdd);

        if (emotionFiles != null && emotionFiles.length > 0) {
            SingleEmotion emotion;
            for (File file : emotionFiles) {
                emotion = new SingleEmotion();
                emotion.setEmotionName(file.getName())
                        .setEmotionFilePath(file.getPath());
                if (entity.getEmotions().size() == emotionSize) {
                    entity = createEmotionEntity(title, titleIconId, childPageCount, false, true, onClickPicListener);
                }
                entity.getEmotions().add(emotion);
            }
        }
    }

    /**
     * 移除之前的所有本地图片fragment
     */
    public void removeLocalEmotionFragment() {
        mVpFragments.removeAll(mLocalFragments);
        emotionEntities.removeAll(localEntities);
        if (this.mLocalFragments != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            for (Fragment f : mLocalFragments) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            mFragmentManager.executePendingTransactions();
        }
        mLocalFragments.clear();
        localEntities.clear();
    }

    /**
     * 创建一页表情的实例
     */
    private EmotionEntity createEmotionEntity(String title, int titleIconId, int childCount, boolean isEmotion) {
        return createEmotionEntity(title, titleIconId, childCount, isEmotion, null);
    }

    private EmotionEntity createEmotionEntity(String title, int titleIconId, int childCount, boolean isEmotion,
                                              OnClickPicListener onClickPicListener) {
        return createEmotionEntity(title, titleIconId, childCount, isEmotion, false, onClickPicListener, null);
    }

    private EmotionEntity createEmotionEntity(String title, int titleIconId, int childCount, boolean isEmotion, boolean isLocal,
                                              OnClickPicListener onClickPicListener) {
        return createEmotionEntity(title, titleIconId, childCount, isEmotion, isLocal, onClickPicListener, null);
    }

    private EmotionEntity createEmotionEntity(String title, int titleIconId, int childCount, boolean isEmotion, boolean isLocal,
                                              OnClickPicListener onClickPicListener, OnChangeLocalPicListener onChangeLocalPicListener) {
        EmotionEntity entity = new EmotionEntity(title, titleIconId, childCount, isEmotion);
        ArrayList<SingleEmotion> emotions = new ArrayList<>();
        entity.setEmotions(emotions);
        EmotionFragment instance = getInstance(entity);
        instance.setOnChangeLocalPicListener(onChangeLocalPicListener);
        if (isLocal) {
            mLocalFragments.add(instance);
            localEntities.add(entity);
        }
        instance.bindEditText(mEditText);
        instance.setOnClickPicListener(onClickPicListener);
        mVpFragments.add(instance);
        emotionEntities.add(entity);
        return entity;
    }
}
