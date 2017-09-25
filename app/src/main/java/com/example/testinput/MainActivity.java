package com.example.testinput;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.testinput.adapter.recycleradaper.BaseRecyclerAdapter;
import com.example.testinput.decoration.VerticalDividerItemDecoration;
import com.example.testinput.keyboard.entity.EmotionEntity;
import com.example.testinput.keyboard.EmotionFragment;
import com.example.testinput.keyboard.util.EmotionKeyboardManager;
import com.example.testinput.keyboard.EmotionTitleAdapter;
import com.example.testinput.keyboard.EmotionVpIndicator;
import com.example.testinput.keyboard.EmotionVpAdapter;
import com.example.testinput.keyboard.util.EmotionUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mRlContent;
    private RelativeLayout mRlEmotionKeyboard;
    private LinearLayout mRlInput;
    private ImageButton mIbChangeEmotion;
    private EditText mEtInput;
    private ViewPager mVpEmotion;
    private List<Fragment> mVpFragments;
    private EmotionVpAdapter mEmotionVpAdapter;
    private EmotionVpIndicator mVpIndicator;
    private RecyclerView mRcvEmotionTitle;
    private EmotionTitleAdapter mEmotionTitleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRlEmotionKeyboard = (RelativeLayout) findViewById(R.id.rl_emotion_keyboard);
        mRlInput = (LinearLayout) findViewById(R.id.rl_input);
        mRlContent = (RelativeLayout) findViewById(R.id.rl_content);
        mIbChangeEmotion = (ImageButton) findViewById(R.id.ib);
        mEtInput = (EditText) findViewById(R.id.et_input);
        showSoftInput();
        initRcvEmotionTitle();
        initVp();

        EmotionKeyboardManager.with(this)
                .setEmotionView(mRlEmotionKeyboard)
                .bindToContent(mRlContent)
                .bindToEditText(mEtInput)
                .bindToEmotionButton(mIbChangeEmotion)
                .build();
    }

    private List<String> mEmotionTitles;

    private void initRcvEmotionTitle() {
        mEmotionTitles = new ArrayList<>();
        mEmotionTitleAdapter = new EmotionTitleAdapter(this, R.layout.item_emotion_title);
        mRcvEmotionTitle = (RecyclerView) findViewById(R.id.rcv_emotion_title);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRcvEmotionTitle.setLayoutManager(linearLayoutManager);
        mRcvEmotionTitle.addItemDecoration(new VerticalDividerItemDecoration.Builder(this).color(R.color.colorPrimary).build());
        mRcvEmotionTitle.setAdapter(mEmotionTitleAdapter);
        mEmotionTitleAdapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                mVpIndicator.play(mEmotionTitleAdapter.getItem(position));
            }
        });
    }

    private ArrayList<EmotionEntity> emotionEntities;

    private void initVp() {
        mVpEmotion = (ViewPager) findViewById(R.id.vp_emotion);
        mVpIndicator = (EmotionVpIndicator) findViewById(R.id.vp_indicator);
        mVpFragments = new ArrayList<>();
        emotionEntities = new ArrayList<>();

        EmotionEntity theme1 = new EmotionEntity("theme1", EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE1), 1, true);
        EmotionFragment instance1 = EmotionFragment.getInstance(theme1);
        mVpFragments.add(instance1);
        emotionEntities.add(theme1);
        mEmotionTitles.add("theme1");

        EmotionEntity theme2 = new EmotionEntity("theme2", EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE2), 1, false);
        EmotionFragment instance2 = EmotionFragment.getInstance(theme2);
        mVpFragments.add(instance2);
        emotionEntities.add(theme2);
        mEmotionTitles.add("theme2");

        for (int i = 0; i < 3; i++) {
            EmotionEntity theme3 = new EmotionEntity("theme3", EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE3), 3, true);
            EmotionFragment instance3 = EmotionFragment.getInstance(theme3);
            mVpFragments.add(instance3);
            emotionEntities.add(theme3);
        }
        mEmotionTitles.add("theme3");

        mEmotionVpAdapter = new EmotionVpAdapter(getFragmentManager(), mVpFragments);
        mVpEmotion.setAdapter(mEmotionVpAdapter);
        mVpIndicator.bindViewPager(mVpEmotion, emotionEntities);
        mEmotionTitleAdapter.addAll(mEmotionTitles);
    }

    private void showSoftInput() {
        mEtInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEtInput, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideSoftInput() {
        mEtInput.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) { //输入法打开状态
            inputMethodManager.hideSoftInputFromWindow(mEtInput.getWindowToken()
                    , InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
