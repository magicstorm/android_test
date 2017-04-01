package com.lansun.tests.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by ly on 28/03/2017.
 */

public class SnackBar extends FrameLayout{

    public static final String COLOR_WHITE = "#ffffff";
    private String backgroundColor;
    private String actionTextColor ;
    private Context mContext;
    private String infoTextColor ;
    private TextView infoView;
    private TextView actionView;
    private int width;
    private int height;
    private int defaultPadding ;
    private int contentWidth;
    private int contentHeight;
    private float actionWidthFactor;
    private float actionHeightFactor;
    private float infoWidthFactor;
    private float infoHeightFactor;
    private int infoTextSize;
    private int actionTextSize;
    private String infoText;
    private String actionText;

    /* listeners */

    //action click listener
    public interface OnActionClickListener extends OnClickListener{
        @Override
        void onClick(View v);
    }
    private OnActionClickListener onActionClickListener;

    public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
    }


    /* constructors */

    public SnackBar(Context context) {
        this(context, null);
    }


    public SnackBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnackBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context==null?getContext():context;

        //set default params
        setDefault();

        //get params from layout file and calculate final result
        initViewDatas();

        //gen child views
        genInfoView();
        genActionView();

        setListeners();

    }

    private void setListeners(){
        actionView.setOnClickListener(onActionClickListener);
    }

    private void initViewDatas(){
        this.setBackgroundColor(Color.parseColor(backgroundColor));
        this.setClickable(true);
    }

    private void setDefault(){
        //default values can not be modified directly
        defaultPadding = dp2px(15);
        actionWidthFactor = (float) 2 / 9;
        actionHeightFactor = 0.0f;
        infoWidthFactor = (float) 5 / 9;
        infoHeightFactor = 0.0f;


        //default values that can be overrided
        backgroundColor = "#ff5050";
        actionTextColor = COLOR_WHITE;
        infoTextColor = COLOR_WHITE;
        infoTextSize = 16;
        actionTextSize = 16;
        actionText = ">>";
        infoText = "对于刚才的故障，非常抱歉，请继续直播";
    }

    private void genInfoView() {
        infoView = new TextView(mContext);
        infoView.setTextSize(TypedValue.COMPLEX_UNIT_SP, infoTextSize);
        infoView.setTextColor(Color.parseColor(infoTextColor));
        infoView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        infoView.setText(infoText);
        infoView.setTypeface(null, Typeface.BOLD);
        addWrapContentParams(infoView);
    }

    private void genActionView() {
        actionView = new TextView(getContext());
        actionView.setTextSize(TypedValue.COMPLEX_UNIT_SP, actionTextSize);
        actionView.setTextColor(Color.parseColor(actionTextColor));
        actionView.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        actionView.setText(actionText);
        actionView.setTypeface(null, Typeface.BOLD);
        actionView.setClickable(true);
        addWrapContentParams(actionView);
    }

    private void addWrapContentParams(View v){
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
    }

    @Override
    protected void onFinishInflate() {
        this.addView(infoView);
        this.addView(actionView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = this.getMeasuredWidth();
        height = this.getMeasuredHeight();

        contentWidth = width - 2*defaultPadding;
        contentHeight = height;


        measureChildAccordingToParent(actionView, actionWidthFactor, actionHeightFactor);
        measureChildAccordingToParent(infoView, infoWidthFactor, infoHeightFactor);
    }

    private void measureChildAccordingToParent(View child, float widthFactor, float heightFactor) {
        int widthMS;
        int heightMS;
        if(widthFactor==0.0f){
            widthMS = MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), MeasureSpec.EXACTLY);
        }
        else{
            widthMS = MeasureSpec.makeMeasureSpec(((int)(contentWidth*widthFactor)), MeasureSpec.EXACTLY);
        }

        if(heightFactor==0.0f){
            heightMS = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY);
        }else{
            heightMS = MeasureSpec.makeMeasureSpec((int)(contentHeight*heightFactor), MeasureSpec.EXACTLY);
        }

        child.measure(widthMS, heightMS);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutInfoView();
        layoutActionView();
    }

    //show info view vertical center align left of snack bar
    private void layoutInfoView(){
        int left = 0 + defaultPadding;
        int top = getVerticalCenterTop(infoView);
        infoView.layout(left, top, left+infoView.getMeasuredWidth(), top+infoView.getMeasuredHeight());
    }


    //show action view vertical center align right of snack bar
    private void layoutActionView(){
        int right = width - defaultPadding;
        int top = getVerticalCenterTop(actionView);
        actionView.layout(right-actionView.getMeasuredWidth(), top, right, top+actionView.getMeasuredHeight());
    }

    /* helpers */

    private int getVerticalCenterTop(View view) {
        return (height - view.getMeasuredHeight())/2;
    }





    /* getters & setters */

    public int getInfoTextSize() {
        return infoTextSize;
    }

    public void setInfoTextSize(int infoTextSize) {
        this.infoTextSize = infoTextSize;
    }

    public int getActionTextSize() {
        return actionTextSize;
    }

    public void setActionTextSize(int actionTextSize) {
        this.actionTextSize = actionTextSize;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getActionTextColor() {
        return actionTextColor;
    }

    public void setActionTextColor(String actionTextColor) {
        this.actionTextColor = actionTextColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return actionTextColor;
    }

    public void setTextColor(String textColor) {
        this.actionTextColor = textColor;
    }

    public String getInfoTextColor() {
        return infoTextColor;
    }

    public void setInfoTextColor(String infoTextColor) {
        this.infoTextColor = infoTextColor;
    }



    public int dp2px(float dp){
        return (int)(dp * getResources().getDisplayMetrics().density);
    }

}
