package com.example.fanyangsz.oschina.view.LoginView;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanyangsz.oschina.Api.HttpSDK;
import com.example.fanyangsz.oschina.Beans.LoginUserBean;
import com.example.fanyangsz.oschina.R;
import com.example.fanyangsz.oschina.widgets.SharedPreSaveObject;

/**
 * Created by fanyang.sz on 2016/1/13.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    View view, loginedView, unloginView;
    ImageView userIcon,userGender,userCode;
    TextView userName,userScore,userFavorite,userFollowing,userFollower;
//    static LoginUserBean loginUserBean;
    public static String SHARED_USER_LOGIN = "user";
    public static String KEY_USER_LOGIN = "loginStatus";

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

        unloginView.setOnClickListener(this);
        bindView();
//        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        LoginUserBean bean =  getLoginUser(getActivity());
        if(bean != null && bean.getResult().OK()){
            InitLoginedView(bean);
            unloginView.setVisibility(View.GONE);
            loginedView.setVisibility(View.VISIBLE);
        }else{
            unloginView.setVisibility(View.VISIBLE);
            loginedView.setVisibility(View.GONE);
        }

        /*userCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();
            }
        });*/
        return view;
    }

    private void bindView(){
        userIcon = (ImageView)view.findViewById(R.id.iv_avatar);
        userGender = (ImageView)view.findViewById(R.id.iv_gender);
        userName = (TextView)view.findViewById(R.id.tv_name);
        userCode = (ImageView)view.findViewById(R.id.iv_qr_code);
        userCode.setOnClickListener(this);
        userScore = (TextView)view.findViewById(R.id.tv_score);
        userFavorite = (TextView)view.findViewById(R.id.tv_favorite);
        userFollowing = (TextView)view.findViewById(R.id.tv_following);
        userFollower = (TextView)view.findViewById(R.id.tv_follower);
    }

    private void InitLoginedView(LoginUserBean data) {

        //头像的问题！！！
        HttpSDK.newInstance().getTweetImage(data.getUser().getPortrait(), userIcon, HttpSDK.IMAGE_TYPE_1);
        userName.setText(data.getUser().getName());
        if(data.getUser().getGender().equals("1")){
            userGender.setImageResource(R.drawable.userinfo_icon_male);
        }else{
            userGender.setImageResource(R.drawable.userinfo_icon_female);
        }
        userCode.setImageResource(R.drawable.icon_qr_code);

        userScore.setText(String.valueOf(data.getUser().getScore()));
        userFavorite.setText(String.valueOf(data.getUser().getFavoritecount()));
        userFollowing.setText(String.valueOf(data.getUser().getFollowers()));
        userFollower.setText(String.valueOf(data.getUser().getFans()));
    }
    private void InitUnLoginView() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.rl_user_unlogin:
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.iv_qr_code:
                showMyQrCode();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null)
            return;
        switch(resultCode){
            case 0:
                Bundle bundle = data.getExtras();
                LoginUserBean loginUserBean =(LoginUserBean) bundle.getSerializable("loginUserBean");
                if(loginUserBean != null && loginUserBean.getResult().OK()){

                    InitLoginedView(loginUserBean);

                    loginedView.setVisibility(View.VISIBLE);
                    unloginView.setVisibility(View.GONE);

                    SharedPreSaveObject.saveObject(getActivity(), SHARED_USER_LOGIN, KEY_USER_LOGIN, loginUserBean);
                }
                break;
            default: break;
        }
    }

    private void showMyQrCode() {
        LoginUserBean bean = getLoginUser(getActivity());
        if(bean != null){
            MyQrodeDialog dialog = new MyQrodeDialog(getActivity(),bean.getUser().getId());
            dialog.show();
        }

    }

    public static LoginUserBean getLoginUser(Context context){
        LoginUserBean bean = (LoginUserBean)SharedPreSaveObject.readObject(context, SHARED_USER_LOGIN, KEY_USER_LOGIN);
        return bean;
    }

}
