# Android表情键盘 EmotionKeyBoard

## Use
该项目是一个完整的应用程序，可点击直接运行。<br/>
用法参考MainActivity

### 具体使用方法
#### 一、初始化键盘管理类
EmotionKeyboardManager.with(this)<br/>
        .setEmotionView(mEmotionKeyboardLayout)<br/>
        .bindToContent(mRcvContent)<br/>
        .bindToEditText(mEtInput)<br/>
        .bindToEmotionButton(mIbChangeEmotion)<br/>
        .build();<br/>

#### 二、初始化数据源
mEmotionKeyboardLayout.bindEditText(mEtInput);// 绑定输入控件<br/>
EmotionFragment.OnClickPicListener onClickPicListener = new EmotionFragment.OnClickPicListener() {<br/>
    @Override<br/>
    public void onClickPic(String theme, int resId) {<br/>
        mChatListAdapter.add(resId + "");<br/>
        mRcvContent.scrollToPosition(mChatListAdapter.getItemCount() - 1);<br/>
    }<br/>
};<br/>
mEmotionKeyboardLayout.createEmotionFragment(true, "theme1", -1, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE1), null);
mEmotionKeyboardLayout.createEmotionFragment(false, "theme2",R.mipmap.theme2,EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE2),       onClickPicListener);
mEmotionKeyboardLayout.createEmotionFragment(true, "theme3", R.mipmap.theme3,EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE3),       null);
mEmotionKeyboardLayout.createEmotionFragment(false, "theme4",R.mipmap.theme4,EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE4),       onClickPicListener);
mEmotionKeyboardLayout.initLocalFileEmotion(onClickPicListener);//加载本地图片
mEmotionKeyboardLayout.loadEmotionVp(getSupportFragmentManager());

## Thank
[zejian_高仿微信表情输入与键盘输入](http://blog.csdn.net/javazejian/article/details/52126391)
