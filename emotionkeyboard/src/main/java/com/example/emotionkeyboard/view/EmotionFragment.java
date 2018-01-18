package com.example.emotionkeyboard.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.emotionkeyboard.EmotionPopup;
import com.example.emotionkeyboard.R;
import com.example.emotionkeyboard.adapter.recycleradaper.BaseRecyclerAdapter;
import com.example.emotionkeyboard.entity.EmotionEntity;
import com.example.emotionkeyboard.entity.SingleEmotion;
import com.example.emotionkeyboard.util.EmotionUtil;
import com.example.emotionkeyboard.util.GenericTools;
import com.example.emotionkeyboard.view.manager.LocalPicManagerActivity;

import java.util.ArrayList;

/**
 * Created by chasing on 2017/9/15.
 * 显示一页表情
 */
public class EmotionFragment extends Fragment {
    public static final String INTENT_EMOTION_DATA = "emotionData";
    public static final String INTENT_BACK_HAD_CHANGE = "hadChangeLocalPic";

    public static final int REQUEST_CODE_PIC_MANAGER = 2;

    //表情的行列数
    public static final int COLUMN_COUNT_EMOTION = 7;
    public static final int ROW_COUNT_EMOTION = 3;
    //图片的行列数
    public static final int COLUMN_COUNT_IMG = 4;
    public static final int ROW_COUNT_IMG = 2;

    private int mColumnCount = COLUMN_COUNT_IMG;
    private View mRootView;
    private RecyclerView mRcvEmotion;
    private EmotionAdapter mEmotionAdapter;
    private EditText mEtInput;
    private OnClickPicListener mOnClickPicListener;
    private OnChangeLocalPicListener mOnChangeLocalPicListener;

    // 长按的弹窗
    private EmotionPopup mEmotionPopup;

    /**
     * 注意：
     * 传入进来的表情/图片的数量不能超过设置的行列数积，否则多余的部分将不会显示
     * 表情的总数需要在行列数积的基础上-1，因为最后一个需要添加"删除"按钮
     */
    private EmotionEntity mEmotionEntity;
    private int mPageTotalCount = 1;

    public static EmotionFragment getInstance(EmotionEntity emotionEntity) {
        EmotionFragment emotionFragment = new EmotionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_EMOTION_DATA, emotionEntity);
        emotionFragment.setArguments(bundle);
        return emotionFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {//vp切换时，会重新调用onCreateView，添加判断避免重复初始化
            mRootView = inflater.inflate(R.layout.frag_emotion, null);
            parseBundle();
            initView(mRootView);
            initData();
        }
        return mRootView;
    }

    /**
     * 绑定输入控件
     */
    public void bindEditText(EditText editText) {
        mEtInput = editText;
    }

    public void setOnClickPicListener(OnClickPicListener onClickPicListener) {
        mOnClickPicListener = onClickPicListener;
    }

    public void setOnChangeLocalPicListener(OnChangeLocalPicListener onChangeLocalPicListener) {
        mOnChangeLocalPicListener = onChangeLocalPicListener;
    }

    private void parseBundle() {
        Bundle arguments = getArguments();
        mEmotionEntity = (EmotionEntity) arguments.getSerializable(INTENT_EMOTION_DATA);
    }

    private void initView(View rootView) {
        mEmotionPopup = new EmotionPopup(getActivity());
        mRcvEmotion = (RecyclerView) rootView.findViewById(R.id.rcv_emotion);
        int rowCount;
        if (mEmotionEntity.isEmotionIcon()) {
            rowCount = ROW_COUNT_EMOTION;
            mPageTotalCount *= ROW_COUNT_EMOTION;
        } else {
            rowCount = ROW_COUNT_IMG;
            mPageTotalCount *= ROW_COUNT_IMG;
        }
        if (mEmotionEntity.isEmotionIcon()) {
            mColumnCount = COLUMN_COUNT_EMOTION;
            mPageTotalCount *= COLUMN_COUNT_EMOTION;
        } else {
            mColumnCount = COLUMN_COUNT_IMG;
            mPageTotalCount *= COLUMN_COUNT_IMG;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), mColumnCount);
        mRcvEmotion.setLayoutManager(layoutManager);
        mEmotionAdapter = new EmotionAdapter(mRcvEmotion, getActivity(), R.layout.item_emotion, rowCount);
        mRcvEmotion.setAdapter(mEmotionAdapter);
        mEmotionAdapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                SingleEmotion singleEmotion = mEmotionAdapter.getItem(position);
                if (mEmotionEntity.isEmotionIcon()) {
                    //是表情则显示在EditText
                    if (position == mPageTotalCount - 1) {
                        //最后一个图标是删除
                        mEtInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    } else {
                        if (singleEmotion.getEmotionResId() <= 0) return;
                        String emotionString = mEtInput.getText().toString() + singleEmotion.getEmotionName();
                        EmotionUtil.setEmotionContent(getActivity(), mEtInput, emotionString);
                        mEtInput.setSelection(emotionString.length());
                    }
                } else {
                    if (singleEmotion.getEmotionName().equals("add")) {
                        Intent intent = new Intent(getActivity(), LocalPicManagerActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_PIC_MANAGER);
                    } else {
                        //是图片则直接进行显示，不在EditText显示
                        //通过回调自行处理
                        if (mOnClickPicListener != null)
                            mOnClickPicListener.onClickPic(mEmotionEntity.getTheme(), singleEmotion.getEmotionResId(), singleEmotion.getEmotionFilePath());
                    }
                }
            }
        });

        mEmotionAdapter.setItemLongClickListener(new BaseRecyclerAdapter.ItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View itemView, int position) {
                SingleEmotion singleEmotion = mEmotionAdapter.getItem(position);
                if (singleEmotion.getEmotionName().equals("add")) return false;

                if (mEmotionEntity.isEmotionIcon() && position == mPageTotalCount - 1) {
                    isDeleteEmotion = true;
                    mHandler.sendEmptyMessage(HANDLER_DELETE_EMOTION);
                    return true;
                } else if (!mEmotionEntity.isEmotionIcon()) {
                    int offsetY = GenericTools.getScreenHeight(getActivity()) - GenericTools.getStatusBarHeight(getActivity()) - mRootView.getHeight() -
                            getContext().getResources().getDimensionPixelSize(R.dimen.emotion_title_height) + itemView.getTop() - 40;
                    int offsetX = itemView.getLeft();
                    if (itemView.getRight() == GenericTools.getScreenWidth(getActivity())) {
                        // 最右边一个
                        offsetX = GenericTools.getScreenWidth(getActivity()) - getContext().getResources().getDimensionPixelSize(R.dimen.emotion_popup);
                    } else if (offsetX != 0) {
                        // 不是最左边一个
                        offsetX -= (getContext().getResources().getDimensionPixelSize(R.dimen.emotion_popup) - mRootView.getWidth() / mColumnCount) / 2;
                    }
                    mEmotionPopup.show(offsetX, offsetY, singleEmotion);
                    return true;
                }
                return false;
            }
        });

        mEmotionAdapter.setItemLongClickReleaseListener(new BaseRecyclerAdapter.ItemLongClickReleaseListener() {
            @Override
            public void onItemLongClickRelease(View itemView, int position) {
                isDeleteEmotion = false;
                mHandler.removeMessages(HANDLER_DELETE_EMOTION);
                mEmotionPopup.hide();
            }
        });
    }

    /**
     * handler处理最后的“删除”按钮长按事件
     * 每隔100ms删除一个字符
     */
    private static final int HANDLER_DELETE_EMOTION = 1;
    private boolean isDeleteEmotion = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_DELETE_EMOTION:
                    removeMessages(HANDLER_DELETE_EMOTION);
                    if (!isDeleteEmotion) return;
                    mEtInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    sendEmptyMessageDelayed(HANDLER_DELETE_EMOTION, 100);
                    break;
            }
        }
    };

    private void initData() {
        ArrayList<SingleEmotion> emotionList = mEmotionEntity.getEmotions();
        if (mEmotionEntity.isEmotionIcon()) {
            //是表情，则页面的最后一个是“删除”按钮，且保证只显示一页，不上下滚动
            for (int i = emotionList.size(); i >= mPageTotalCount; i--) {
                emotionList.remove(i - 1);
            }
            SingleEmotion singleEmotion;
            for (int i = emotionList.size(); i < mPageTotalCount; i++) {
                singleEmotion = new SingleEmotion();
                if (i == mPageTotalCount - 1) {
                    singleEmotion.setEmotionName("删除")
                            .setEmotionResId(R.mipmap.icon_del);
                    emotionList.add(singleEmotion);
                } else {
                    // 直到最后一个“删除”按钮之前都添加空白
                    emotionList.add(singleEmotion);
                }
            }
        } else {
            //是图片，则保证只显示一页，不上下滚动
            for (int i = emotionList.size() - 1; i >= mPageTotalCount; i--) {
                emotionList.remove(i);
            }
        }
        mEmotionAdapter.clear();
        mEmotionAdapter.addAll(emotionList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface OnClickPicListener {
        void onClickPic(String theme, int resId, String path);
    }

    public interface OnChangeLocalPicListener {
        void onChangeLocalPic();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PIC_MANAGER && data != null) {
            boolean hadChange = data.getBooleanExtra(INTENT_BACK_HAD_CHANGE, false);
            if (hadChange) {
                if (mOnChangeLocalPicListener != null)
                    mOnChangeLocalPicListener.onChangeLocalPic();
            }
        }
    }
}
