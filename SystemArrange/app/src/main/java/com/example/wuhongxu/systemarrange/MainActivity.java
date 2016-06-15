package com.example.wuhongxu.systemarrange;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wuhongxu.UI.BottomFragment;
import com.example.wuhongxu.UI.SimpleListFragment;
import com.example.wuhongxu.ValueEnum.Value;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity {
    private int isHaveAnimation;
    private SlidingMenu menu;
    private String TAG = "mainmainmain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        isHaveAnimation = Value.ANIMATION_OK;
        // 设置滑动菜单的属性值

        menu = new SlidingMenu(this);

        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        menu.setShadowWidthRes(R.dimen.shadow_width);

        menu.setShadowDrawable(R.drawable.shadow);

        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

        menu.setFadeDegree(0.35f);

        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        // 设置滑动菜单的视图界面

        menu.setMenu(R.layout.slidding_menu_frame);
        getFragmentManager().beginTransaction().replace(R.id.menu_frame,new SimpleListFragment()).commit();


    }

    public int getIsHaveAnimation() {
        return isHaveAnimation;
    }

    public void setIsHaveAnimation(int isHave) {
        if (isHave == Value.ANIMATION_OK) {
            isHaveAnimation = Value.ANIMATION_OK;
            return;
        }

        if (isHave == Value.ANIMATION_NO) {
            isHaveAnimation = Value.ANIMATION_NO;
            return;
        }

        throw new NullPointerException("没有对应的参数");
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
    public void onBackPressed() {

        //点击返回键关闭滑动菜单

        if (menu.isMenuShowing()) {
            menu.showContent();
            return ;
        } else if(((BottomFragment)getFragmentManager().findFragmentById(R.id.bottom_fragment)).isMessageLayoutIsShow()){
            ((BottomFragment)getFragmentManager().findFragmentById(R.id.bottom_fragment)).cancel();
            return ;
        }else{
            super.onBackPressed();
        }

    }
}
