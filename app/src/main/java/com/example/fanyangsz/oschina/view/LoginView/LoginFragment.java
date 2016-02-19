package com.example.fanyangsz.oschina.view.LoginView;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Beans.LoginUserBean;
import com.example.fanyangsz.oschina.R;

/**
 * Created by fanyang.sz on 2016/1/13.
 */
public class LoginFragment extends Fragment {

    View view, loginedView, unloginView;
    ImageView userIcon,userGender,userCode;
    TextView userName,userScore,userFavorite,userFollowing,userFollower;
    static LoginUserBean loginUserBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person_information, container, false);
        loginedView = view.findViewById(R.id.ll_user_container);
        unloginView = view.findViewById(R.id.rl_user_unlogin);
        bindView();
        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        /*if (loginUserBean != null && loginUserBean.getResult().OK()) {

            InitLoginedView(loginUserBean);
            unloginView.setVisibility(View.GONE);
            loginedView.setVisibility(View.VISIBLE);

        }else*/ if(preferences.contains("name")){
            InitLoginedView(preferences);
            unloginView.setVisibility(View.GONE);
            loginedView.setVisibility(View.VISIBLE);
        }else{
            unloginView.setVisibility(View.VISIBLE);
            loginedView.setVisibility(View.GONE);
        }
        unloginView.setOnClickListener(new myOnClickListener());

        userCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
            }
        });
        return view;
    }

    private void bindView(){
        userIcon = (ImageView)view.findViewById(R.id.iv_avatar);
        userGender = (ImageView)view.findViewById(R.id.iv_gender);
        userName = (TextView)view.findViewById(R.id.tv_name);
        userCode = (ImageView)view.findViewById(R.id.iv_qr_code);
        userScore = (TextView)view.findViewById(R.id.tv_score);
        userFavorite = (TextView)view.findViewById(R.id.tv_favorite);
        userFollowing = (TextView)view.findViewById(R.id.tv_following);
        userFollower = (TextView)view.findViewById(R.id.tv_follower);
    }

    private void InitLoginedView(LoginUserBean data) {


        //头像的问题！！！
        userName.setText(data.getUser().getName());
        if(data.getUser().getGender().equals("1")){
            userGender.setImageResource(R.drawable.userinfo_icon_male);
        }else{
            userGender.setImageResource(R.drawable.userinfo_icon_female);
        }
        userCode.setImageResource(R.drawable.icon_qr_code);

        userScore.setText(data.getUser().getScore()+"");
        userFavorite.setText(data.getUser().getFavoritecount()+"");
        userFollowing.setText(data.getUser().getFollowers()+"");
        userFollower.setText(data.getUser().getFans()+"");
    }

    private void InitLoginedView(SharedPreferences preferences) {


        //头像的问题！！！
        userName.setText(preferences.getString("name",null));
        if(preferences.getString("gender","-1").equals("1")){
            userGender.setImageResource(R.drawable.userinfo_icon_male);
        }else{
            userGender.setImageResource(R.drawable.userinfo_icon_female);
        }
        userCode.setImageResource(R.drawable.icon_qr_code);

        userScore.setText(preferences.getInt("score",-1)+"");
        userFavorite.setText(preferences.getInt("favorite",-1)+"");
        userFollowing.setText(preferences.getInt("following",-1)+"");
        userFollower.setText(preferences.getInt("follower",-1)+"");
    }
    private void InitUnLoginView() {

    }

    private class myOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null)
            return;
        switch(resultCode){
            case 0:
                Bundle bundle = data.getExtras();
                loginUserBean =(LoginUserBean) bundle.getSerializable("loginUserBean");
                if(loginUserBean.getResult().OK()){

                    InitLoginedView(loginUserBean);

                    loginedView.setVisibility(View.VISIBLE);
                    unloginView.setVisibility(View.GONE);

                    SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = preferences.edit();
                    mEditor.putString("name",loginUserBean.getUser().getName());
                    mEditor.putString("gender",loginUserBean.getUser().getGender());
                    mEditor.putInt("score", loginUserBean.getUser().getScore());
                    mEditor.putInt("favorite", loginUserBean.getUser().getFavoritecount());
                    mEditor.putInt("following", loginUserBean.getUser().getFollowers());
                    mEditor.putInt("follower", loginUserBean.getUser().getFans());
                    mEditor.commit();
                }
                break;
            default: break;
        }
    }

}
