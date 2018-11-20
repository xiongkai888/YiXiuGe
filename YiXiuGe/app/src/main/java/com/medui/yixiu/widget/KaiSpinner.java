package com.medui.yixiu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.medui.yixiu.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by xkai on 2017/11/7.
 * 自定义下拉菜单()
 */

public class KaiSpinner extends FrameLayout {


    Context context;

    @InjectView(R.id.spinner_sub)
    Spinner mSpinner;

    private ArrayAdapter adapter;

    public KaiSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this, this);
    }

    public void setListData(List<String> list) {

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected((String) parent.getItemAtPosition(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (l != null) {
                            l.onClick();
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void setSelection(int position) {
        mSpinner.setSelection(position);
    }

    OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(String itemStr);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener l) {
        onItemSelectedListener = l;
    }


    SpinnerOnTouchListener l;

    public interface SpinnerOnTouchListener {
        void onClick();
    }

    public void setOnSpinnerTouchListener(SpinnerOnTouchListener l) {
        this.l = l;
    }

}
