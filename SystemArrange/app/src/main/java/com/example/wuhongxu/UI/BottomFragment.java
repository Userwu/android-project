package com.example.wuhongxu.UI;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongxu.Arrange.DectoryHandle;
import com.example.wuhongxu.Arrange.DectoryValue;
import com.example.wuhongxu.Arrange.OnDectoryChange;
import com.example.wuhongxu.Arrange.OnTimeFinish;
import com.example.wuhongxu.Arrange.TimerPlan;
import com.example.wuhongxu.ValueEnum.Value;
import com.example.wuhongxu.systemarrange.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomFragment extends Fragment implements OnDectoryChange {
    public String TAG = "bottomFragment";
    private View rootView;
    private LinearLayout messageLayout, btnLayout, cancelLayout;
    private Button reNameBtn, copyBtn, moveBtn, cancelBtn, sureBtn;
    private Button[] buttons, cancelButtons;
    private TextView pathView;
    private boolean isShow;

    public boolean isCancelLayoutIsShow() {
        return cancelLayoutIsShow;
    }

    private boolean cancelLayoutIsShow;

    public boolean isBtnLayoutIsShow() {
        return btnLayoutIsShow;
    }

    private boolean btnLayoutIsShow;

    public boolean isMessageLayoutIsShow() {
        return messageLayoutIsShow;
    }

    private boolean messageLayoutIsShow;
    private AnimationSet showAnimationFormessageLayout,
            closeAnimationFormessageLayout;
    private ListView dectoryListView;
    private DectoryAdapter dAdapter;
    private View clickView;
    private int cancelLength, btnLength;
    private List<Map<String, Object>> list;
    private String[] from;
    private int[] to;
    private DectoryHandle dHandle;
    private SearchResultFragement srFragment;
    private Handler handler;
    private boolean isMove;

    public BottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_bottom, container, false);
        init();
        return rootView;
    }

    public void init() {
        isShow = false;
        cancelLayoutIsShow = false;
        btnLayoutIsShow = false;
        messageLayoutIsShow = false;
        isMove = false;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Value.MESSAGELAYOUT_SHOW:
                        messageLayout.setVisibility(View.VISIBLE);
                        break;
                    case Value.MESSAGELAYOUT_CLOSE:
                        messageLayout.setVisibility(View.GONE);
                        break;
                    case Value.BTNLAYOUT_SHOW:
                        btnLayout.setVisibility(View.VISIBLE);
                        break;
                    case Value.BTNLAYOUT_CLOSE:
                        btnLayout.setVisibility(View.GONE);
                        break;
                    case Value.CANCELLAYOUT_SHOW:
                        cancelLayout.setVisibility(View.VISIBLE);
                        break;
                    case Value.CANCELLAYOUT_CLOSE:
                        cancelLayout.setVisibility(View.GONE);
                        break;
                    case Value.LIST_SHOW:
                        dectoryListView.setVisibility(View.VISIBLE);
                        break;
                    case Value.LIST_CLOSE:
                        dectoryListView.setVisibility(View.GONE);
                        break;
                    case Value.showCancelBtn:
                        showBtnLayout(cancelButtons);
                        break;
                    case Value.CREATE_TOAST:
                        Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        messageLayout = (LinearLayout) rootView.findViewById(R.id.message_layout);
        btnLayout = (LinearLayout) rootView.findViewById(R.id.btn_layout);
        cancelLayout = (LinearLayout) rootView.findViewById(R.id.cancel_layout);
        reNameBtn = (Button) rootView.findViewById(R.id.reName_btn);
        copyBtn = (Button) rootView.findViewById(R.id.copy_btn);
        moveBtn = (Button) rootView.findViewById(R.id.move_btn);
        cancelBtn = (Button) rootView.findViewById(R.id.cancel_btn);
        sureBtn = (Button) rootView.findViewById(R.id.sure_btn);
        pathView = (TextView) rootView.findViewById(R.id.path_view);
        buttons = new Button[]{moveBtn, copyBtn, reNameBtn};
        cancelButtons = new Button[]{cancelBtn, sureBtn};
        btnLength = buttons.length;
        cancelLength = cancelButtons.length;
        dectoryListView = (ListView) rootView.findViewById(R.id.dectory_listView);
        dHandle = new DectoryHandle(this);

        list = new ArrayList<Map<String, Object>>();
        from = new String[]{"name", "path", "level"};
        to = new int[]{R.id.dectory_logo_iv, R.id.dectory_name_tv};
        dAdapter = new DectoryAdapter(getActivity(), list, R.layout.dectory_item, from, to);

        dectoryListView.setAdapter(dAdapter);
        dectoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int tag = Integer.valueOf(list.get(position).get(from[4]).toString());
//                dHandle.chooseDectory(tag);
                dHandle.chooseDectory(position);
            }
        });


        AlphaAnimation aAnimation1 = new AlphaAnimation(0, 1);
        aAnimation1.setDuration(Value.LONG_TIME);
        TranslateAnimation tAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
        tAnimation1.setDuration(Value.SHORT_TIME);
        showAnimationFormessageLayout = new AnimationSet(true);
        showAnimationFormessageLayout.addAnimation(aAnimation1);
        showAnimationFormessageLayout.addAnimation(tAnimation1);

        AlphaAnimation aAnimation2 = new AlphaAnimation(1, 0);
        aAnimation2.setDuration(Value.LONG_TIME);
        TranslateAnimation tAnimation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f);
        tAnimation2.setDuration(Value.SHORT_TIME);
        closeAnimationFormessageLayout = new AnimationSet(false);
        closeAnimationFormessageLayout.addAnimation(aAnimation2);
        closeAnimationFormessageLayout.addAnimation(tAnimation2);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dHandle.startDHandle();
                nextStep();
                isMove = false;

            }
        });
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dHandle.startDHandle();
                nextStep();
                isMove = true;
            }
        });
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srFragment = (SearchResultFragement) getActivity().getFragmentManager().findFragmentById(R.id.list_fragment);
                Log.e(TAG, "onClick FileList");
                srFragment.changeFile(isMove, String.valueOf(pathView.getText()));
                srFragment.clearAllCheck();
                BottomFragment.this.close();
            }
        });
    }

    public void cancel() {
        BottomFragment.this.close();
        srFragment = (SearchResultFragement) getActivity().getFragmentManager().findFragmentById(R.id.list_fragment);
        srFragment.clearAllCheck();
    }

    public void show() {
        if (isShow)
            return;
        Log.e(TAG, "showww " + isShow);
        isShow = true;
        showMessageLayout();
        showBtnLayout(buttons);
    }

    public void close() {
        if (!isShow)
            return;
        Log.e(TAG, "showww " + isShow);
        isShow = false;
        closeDView();
        closeMessageLayout();
        if (btnLayoutIsShow)
            closeBtnLayout(buttons);
        else
            closeBtnLayout(cancelButtons);
    }

    public void showMessageLayout() {
        if (messageLayoutIsShow)
            return;
        messageLayout.startAnimation(showAnimationFormessageLayout);
        messageLayout.setVisibility(View.VISIBLE);
        messageLayoutIsShow = true;
    }

    public int closeMessageLayout() {
        if (!messageLayoutIsShow)
            return 0;
        messageLayoutIsShow = false;
        int time = (buttons.length - 1) * Value.SHORT_TIME;
        closeAnimationFormessageLayout.setStartOffset(time);
        new TimerPlan(time + Value.SHORT_TIME, new OnTimeFinish() {
            @Override
            public void plan() {
                Message msg1 = new Message();
                msg1.what = Value.MESSAGELAYOUT_CLOSE;
                handler.sendMessage(msg1);

                Message msg2 = new Message();
                msg2.what = Value.BTNLAYOUT_CLOSE;
                handler.sendMessage(msg2);

            }
        });
        messageLayout.startAnimation(closeAnimationFormessageLayout);
        return buttons.length * Value.SHORT_TIME;
    }

    public int showBtnLayout(Button[] bs) {
        if (bs.length == cancelLength) {
            if (cancelLayoutIsShow)
                return 0;
            cancelLayout.setVisibility(View.VISIBLE);
            cancelLayoutIsShow = true;
        } else {
            if (btnLayoutIsShow)
                return 0;
            btnLayout.setVisibility(View.VISIBLE);
            btnLayoutIsShow = true;
        }
        for (int i = 0; i < bs.length; i++) {

            AlphaAnimation aAnimation1 = new AlphaAnimation(0, 1);
            aAnimation1.setDuration(Value.SHORT_TIME);
            TranslateAnimation tAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
            tAnimation1.setDuration(Value.SHORT_TIME);
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(aAnimation1);
            animationSet.addAnimation(tAnimation1);
            animationSet.setStartOffset(i * Value.SHORT_TIME);
            bs[i].startAnimation(animationSet);
            btnLayout.setVisibility(View.VISIBLE);

        }
        return bs.length * Value.SHORT_TIME;
    }

    public int closeBtnLayout(Button[] bs) {
        if (bs.length == cancelLength) {
            if (!cancelLayoutIsShow)
                return 0;
            new TimerPlan(bs.length * Value.SHORT_TIME, new OnTimeFinish() {
                @Override
                public void plan() {
                    Message msg = new Message();
                    msg.what = Value.CANCELLAYOUT_CLOSE;
                    handler.sendMessage(msg);
                }
            });
            cancelLayoutIsShow = false;
        } else {
            if (!btnLayoutIsShow)
                return 0;
            new TimerPlan(bs.length * Value.SHORT_TIME, new OnTimeFinish() {
                @Override
                public void plan() {
                    Message msg = new Message();
                    msg.what = Value.BTNLAYOUT_CLOSE;
                    handler.sendMessage(msg);

                }
            });

            btnLayoutIsShow = false;
        }
        AnimationSet animationSet = null;
        for (int i = bs.length - 1; i >= 0; i--) {
            AlphaAnimation aAnimation1 = new AlphaAnimation(1, 0);
            aAnimation1.setDuration(Value.SHORT_TIME);
            TranslateAnimation tAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1.0f);
            tAnimation1.setDuration(Value.SHORT_TIME);
            animationSet = new AnimationSet(false);
            animationSet.addAnimation(aAnimation1);
            animationSet.addAnimation(tAnimation1);
            animationSet.setStartOffset((bs.length - i - 1) * Value.SHORT_TIME);
            bs[i].startAnimation(animationSet);
            animationSet.setFillAfter(true);
        }
        return bs.length * Value.SHORT_TIME;
    }

    public int showDView() {
        if (dectoryListView.getVisibility() == View.GONE) {
            AlphaAnimation aAnimation1 = new AlphaAnimation(0, 1);
            aAnimation1.setDuration(Value.LONG_TIME);
            TranslateAnimation tAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
            tAnimation1.setDuration(Value.LONG_TIME);
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(aAnimation1);
            animationSet.addAnimation(tAnimation1);

            dectoryListView.startAnimation(animationSet);
            dectoryListView.setVisibility(View.VISIBLE);
            return Value.LONG_TIME;
        }
        return 0;
    }

    public int closeDView() {
        if (dectoryListView.getVisibility() == View.VISIBLE) {
            list.clear();
            AlphaAnimation aAnimation1 = new AlphaAnimation(1, 0);
            aAnimation1.setDuration(Value.LONG_TIME);
            TranslateAnimation tAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f);
            tAnimation1.setDuration(Value.LONG_TIME);
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(aAnimation1);
            animationSet.addAnimation(tAnimation1);

            dectoryListView.startAnimation(animationSet);
            new TimerPlan(Value.LONG_TIME, new OnTimeFinish() {
                @Override
                public void plan() {
                    Message msg = new Message();
                    msg.what = Value.LIST_CLOSE;
                    handler.sendMessage(msg);
                }
            });
            return Value.LONG_TIME;
        }
        return 0;
    }

    public void nextStep() {
        new TimerPlan(closeBtnLayout(buttons), new OnTimeFinish() {
            @Override
            public void plan() {
                Log.e(TAG, "plan nextStep");
                Message msg = new Message();
                msg.what = Value.showCancelBtn;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void addItem(DectoryValue dv, int position) {
        showDView();
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put(from[0], dv.name);
        mp.put(from[1], dv.path);
        mp.put(from[2], dv.level);
        list.add(position, mp);
        dAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeItem(int position) {
        list.remove(position);
        dAdapter.notifyDataSetChanged();
    }

    @Override
    public void choosePath(String dectoryName) {
        pathView.setText(dectoryName);
    }

    @Override
    public void sendDToast(String s) {
        Message msg = new Message();
        msg.what = Value.CREATE_TOAST;
        msg.obj = s;
        handler.sendMessage(msg);
    }
}
