package com.example.wuhongxu.UI;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wuhongxu.Arrange.FileHandle;
import com.example.wuhongxu.Arrange.OnSearchChange;
import com.example.wuhongxu.ValueEnum.Value;
import com.example.wuhongxu.systemarrange.MainActivity;
import com.example.wuhongxu.systemarrange.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragement extends Fragment implements View.OnClickListener, OnSearchChange {
    private View rootView;
    private Button startBtn, pauseBtn, clearBtn, cancelBtn;
    private EditText attributeEdt;
    private ListView searchResultLv;
    private SearchResultListAdapter srAdapter;
    private List<Map<String, Object>> list;
    private String[] from;
    private int[] to;
    private Handler handler;
    private FileHandle fileHandle;
    private TranslateAnimation tAnimationForCb, tAnimationForBody;
    private AlphaAnimation aAnimation;
    private String TAG = "SearchResultFragment";
    private BottomFragment bottomFragment;
    private View clickView;
    private ProgressBar progressBar;
    private int clickPosition;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_result_fragement, container, false);
        init();
        return rootView;
    }

    public void init() {
        fileHandle = new FileHandle(this);

        startBtn = (Button) rootView.findViewById(R.id.search_start_btn);
        pauseBtn = (Button) rootView.findViewById(R.id.search_pause_btn);
        clearBtn = (Button) rootView.findViewById(R.id.list_clear_btn);
        cancelBtn = (Button) rootView.findViewById(R.id.cancel_btn);
        initProgress();

        attributeEdt = (EditText) rootView.findViewById(R.id.search_atrribute_editText);
        searchResultLv = (ListView) rootView.findViewById(R.id.search_result_listView);
        searchResultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                clickView = view;
                clickPosition = position;
                fileHandle.chooseFile(Integer.valueOf(list.get(position).get(from[4]).toString()));

            }
        });
        list = new ArrayList<Map<String, Object>>();
        from = new String[]{"checkView", "filelogo", "fileName", "fileSize", "tag"};
        to = new int[]{R.id.check_image_view, R.id.file_logo_iv, R.id.file_name_tv, R.id.file_size_tv};

        srAdapter = new SearchResultListAdapter(getActivity(), list, R.layout.search_result_item_checked, from, to);
        searchResultLv.setAdapter(srAdapter);

        startBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        attributeEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((InputMethodManager) attributeEdt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getActivity()
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    initToSearch();
                    return true;
                }

                return false;
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Value.CREATE_TOAST:
                        Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case Value.ITEM_ADD:
                        list.add((Map<String, Object>) msg.obj);
                        srAdapter.notifyDataSetChanged();
                        break;
                    case Value.ITEM_REMOVE:
                        break;
                    case Value.CLOSEE_EDT_LAYOUT:

                        break;
                    case Value.CLOSE_PROGRESS_LAYOUT:
                        fileHandle.closeSearch();
                        cancelSearchAnimation();
                        break;
                    case Value.SHOW_EDT_LAYOUT:

                        break;
                }
            }
        };
        clearBtn.setOnClickListener(this);
    }

    public void initProgress() {
        File f = Environment.getExternalStorageDirectory();
        if(f == null)
            return ;
        if(!f.exists()){
            sendFToast("没有sd卡");
            return ;
        }
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_progressbar);
        progressBar.setMax((int) f.listFiles().length);
        progressBar.setProgress(0);
    }

    public void initToSearch() {
        clearList();
        initProgress();
        fileHandle.closeSearch();
        fileHandle.setAttribute(attributeEdt.getText().toString());
        fileHandle.startSearch();
        startSearchAnimation();
    }

    public int closeTopLayout(final View v, boolean isLeft, int delay) {
        float toX = isLeft ? -1.0f : 1.0f;
        int t = Value.LONG_TIME;
        AlphaAnimation aAnimation1 = new AlphaAnimation(1, 0);
        aAnimation1.setDuration(t);
        TranslateAnimation tAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, toX,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        tAnimation1.setDuration(t);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(aAnimation1);
        animationSet.addAnimation(tAnimation1);
        animationSet.setDuration(t);
        animationSet.setStartOffset(delay);
        v.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return t + delay;
    }

    public int showTopLayout(View v, int delay) {
        AlphaAnimation aAnimation1 = new AlphaAnimation(0, 1);
        int t = Value.LONG_TIME;
        aAnimation1.setDuration(t);
        TranslateAnimation tAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0);
        tAnimation1.setDuration(t);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(aAnimation1);
        animationSet.addAnimation(tAnimation1);
        animationSet.setFillEnabled(true);
        animationSet.setStartOffset(delay);
        v.startAnimation(animationSet);
        v.setVisibility(View.VISIBLE);
        return t + delay;
    }

    public int startSearchAnimation() {
        showTopLayout(progressBar, closeTopLayout(clearBtn, false, 0));
        showTopLayout(pauseBtn, closeTopLayout(startBtn, false, 0));
        showTopLayout(cancelBtn, closeTopLayout(attributeEdt, true, 0));

        return 0;
    }

    public int cancelSearchAnimation() {
        showTopLayout(attributeEdt, closeTopLayout(cancelBtn, false, 0));
        showTopLayout(startBtn, closeTopLayout(pauseBtn, false, 0));
        showTopLayout(clearBtn, closeTopLayout(progressBar, true, 0));
        return 0;
    }

    public void addAnimation(View v, Animation anim) {
        v.startAnimation(anim);
    }

    public void clearList() {
        list.clear();
        srAdapter.notifyDataSetChanged();
    }

    public void clearAllCheck() {
        fileHandle.clearCheck();
        for (Map<String, Object> m : list) {
            m.put(from[0], false);
        }
        srAdapter.notifyDataSetChanged();
    }

    public boolean closeCheck(final int position) {
        final LinearLayout ll = (LinearLayout) clickView.findViewById(R.id.body_layout);
        final ImageView iv = (ImageView) clickView.findViewById(R.id.check_image_view);
        clickView = null;

        if (((MainActivity) getActivity()).getIsHaveAnimation() == Value.ANIMATION_NO) {
            iv.setVisibility(View.GONE);
            list.get(position).put(from[0], false);
            return false;
        }


        //tAnimationForBody = new TransitionAnimation(200,0,0,0);
        tAnimationForBody = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.13f, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
        tAnimationForBody.setDuration(Value.LONG_TIME);
        aAnimation = new AlphaAnimation(1, 0);
        aAnimation.setDuration(Value.SHORT_TIME);

        aAnimation.setFillBefore(true);
        iv.startAnimation(aAnimation);

        aAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.setVisibility(View.GONE);
                ll.startAnimation(tAnimationForBody);
                list.get(position).put(from[0], false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        return false;
    }

    public void changeFile(boolean isMove, String path) {
        fileHandle.change(isMove, path);
    }

    public boolean openCheck(final int position) {
        final LinearLayout ll = (LinearLayout) clickView.findViewById(R.id.body_layout);
        final ImageView iv = (ImageView) clickView.findViewById(R.id.check_image_view);
        clickView = null;
        if (((MainActivity) getActivity()).getIsHaveAnimation() == Value.ANIMATION_NO) {
            iv.setVisibility(View.VISIBLE);
            list.get(position).put(from[0], true);
            bottomFragment = (BottomFragment) getActivity().getFragmentManager().findFragmentById(R.id.bottom_fragment);
            bottomFragment.show();
            return true;
        }
        bottomFragment = (BottomFragment) getActivity().getFragmentManager().findFragmentById(R.id.bottom_fragment);
        bottomFragment.show();
        //tAnimationForBody = new TranslateAnimation(0,200,0,0);
        tAnimationForBody = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0.13f,
                Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
        tAnimationForBody.setDuration(Value.SHORT_TIME);
        aAnimation = new AlphaAnimation(0, 1);
        aAnimation.setDuration(Value.LONG_TIME);

        tAnimationForBody.setFillEnabled(true);
        ll.startAnimation(tAnimationForBody);
        tAnimationForBody.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv.startAnimation(aAnimation);
                iv.setVisibility(View.VISIBLE);

                list.get(position).put(from[0], true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return true;
    }

    @Override
    public void addItem(String name, String path, long size, int tag, boolean isFile) {
        Map<String, Object> mp = new HashMap<String, Object>();
        mp.put(from[0], false);
        mp.put(from[1], R.drawable.close);
        mp.put(from[2], name);
        mp.put(from[3], size);
        mp.put(from[4], tag);
        Message msg = new Message();
        msg.what = Value.ITEM_ADD;
        msg.obj = mp;
        handler.sendMessage(msg);

    }

    @Override
    public void removeItem(int tag) {

    }

    @Override
    public void sendFToast(String content) {
        Message msg = new Message();
        msg.what = Value.CREATE_TOAST;
        msg.obj = content;
        handler.sendMessage(msg);
    }

    @Override
    public boolean chooseItem(int tag) {
        if (clickView == null)
            return false;
        if (Boolean.valueOf(list.get(tag).get(from[0]).toString()))
            return closeCheck(tag);
        return openCheck(tag);
    }

    @Override
    public void loading(String reason) {

    }

    @Override
    public void closeLoading() {
        Message msg = new Message();
        msg.what = Value.CLOSE_PROGRESS_LAYOUT;
        handler.sendMessage(msg);
    }

    @Override
    public void reFreshItem(String name, String path, long size, int tag, boolean isFile) {
        for (Map<String, Object> m : list) {
            if (m.get(from[4]).equals(tag)) {
                m.put(from[0], false);
                m.put(from[1], name);
                m.put(from[2], path);
                m.put(from[3], size);
                m.put(from[4], tag);
                srAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onChooseEmpty() {
        bottomFragment = (BottomFragment) getActivity().getFragmentManager().findFragmentById(R.id.bottom_fragment);
        bottomFragment.close();
    }

    @Override
    public void addProgress() {
        int p = progressBar.getProgress();
        if (p < progressBar.getMax()) {
            progressBar.setProgress(p + 1);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_start_btn:
                initToSearch();
                break;
            case R.id.cancel_btn:
                Message msg = new Message();
                msg.what = Value.CLOSE_PROGRESS_LAYOUT;
                handler.sendMessage(msg);
                break;
            case R.id.search_pause_btn:

                fileHandle.setIsPause(!fileHandle.getIsPause());
                boolean isPause = fileHandle.getIsPause();
                Button b = (Button) v;
                b.setText(isPause ? "继续" : "暂停");
                break;
            case R.id.list_clear_btn:
                clearList();
                attributeEdt.setText("");
                break;
        }
    }
}
