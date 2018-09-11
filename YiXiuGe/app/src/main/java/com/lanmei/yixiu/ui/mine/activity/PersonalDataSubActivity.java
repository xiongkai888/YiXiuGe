package com.lanmei.yixiu.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.helper.CameraHelper;
import com.lanmei.yixiu.utils.AssetsUtils;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;
import com.xson.common.widget.CircleImageView;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.OptionPicker;


/**
 * 个人资料
 */
public class PersonalDataSubActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.avatar_iv)
    CircleImageView mAvatarIv;//头像
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.phone_tv)
    TextView phoneTv;
    @InjectView(R.id.email_tv)
    TextView emailTv;//邮箱
    @InjectView(R.id.address_tv)
    TextView addressTv;//地址
    @InjectView(R.id.address_details_tv)
    TextView addressDetailsTv;//详细地址

    @InjectView(R.id.save_bt)
    Button mSaveButton;//保存
    @InjectView(R.id.radioGroup)
    RadioGroup mRadgroup;
    @InjectView(R.id.btnMan)
    RadioButton btnMan;
    @InjectView(R.id.btnWoman)
    RadioButton btnWoman;

    String pic;//头像
    String name;//姓名
    String phone;//手机号码
    String weixin;//微信号
    String email;
    String sex;//性别
    String chooseSex;
    String area;//地址
    String address;//详细地址

    @InjectView(R.id.weixin_tv)
    TextView weixinTv;
    @InjectView(R.id.unit_tv)
    TextView unitTv;
    @InjectView(R.id.education_tv)
    TextView educationTv;//学历
    @InjectView(R.id.school_tv)
    TextView schoolTv;
    @InjectView(R.id.student_type_tv)
    TextView studentTypeTv;
    @InjectView(R.id.student_nature_tv)
    TextView studentNatureTv;
    @InjectView(R.id.learn_subject_tv)
    TextView learnSubjectTv;
    @InjectView(R.id.technical_post_tv)
    TextView technicalPostTv;

    private CameraHelper cameraHelper;
    private UserBean bean;

    private AddressAsyncTask addressAsyncTask;
    private OptionPicker educationPicker;//学历选择器
    private OptionPicker studentNaturePicker;//学员性质选择器
    private OptionPicker learnSubjectPicker;//要学科室选择器


    @Override
    public int getContentViewId() {
        return R.layout.activity_personal_data_sub;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.personal_data);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        addressAsyncTask = new AddressAsyncTask();//异步获取省市区列表
        addressAsyncTask.execute();

//        List<String> list = new ArrayList<>();
//        list.add("http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-11%2010%3A34%3A39-0.jpg");
//        list.add("http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-11%2010%3A34%3A39-1.jpg");
//        CommonUtils.deleteOssObjectList(list);

        cameraHelper = new CameraHelper(this);
        cameraHelper.setHeadUrlListener(new CameraHelper.HeadUrlListener() {
            @Override
            public void getUrl(String url) {
                if (isFinishing()) {
                    return;
                }
                loadUpDate(url);
            }
        });

        bean = UserHelper.getInstance(this).getUserBean();
        if (bean != null) {
            name = bean.getNickname();
            email = bean.getEmail();
            phone = bean.getPhone();
            weixin = bean.getWeixin();
            area = bean.getArea();
            address = bean.getAddress();
            pic = bean.getPic();
            chooseSex = sex = bean.getSex();
            if (!StringUtils.isEmpty(sex)) {
                if (StringUtils.isSame(sex, CommonUtils.isOne)) {
                    btnMan.setChecked(true);
                } else if (StringUtils.isSame(sex, CommonUtils.isTwo)) {
                    btnWoman.setChecked(true);
                }
            }

            nameTv.setText(name);
            emailTv.setText(email);
            addressTv.setText(area);
            phoneTv.setText(phone);
            weixinTv.setText(weixin);
            addressDetailsTv.setText(address);

            pic = bean.getPic();
            cameraHelper.setHeadPathStr(pic);

            ImageHelper.load(this, pic, mAvatarIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
        }
        mSaveButton.setEnabled(false);
        mSaveButton.setBackgroundResource(R.drawable.button_unable);


        mRadgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.btnMan) {
                    sex = CommonUtils.isOne;
                } else if (checkedId == R.id.btnWoman) {
                    sex = CommonUtils.isTwo;
                }
                L.d("BeanRequest", sex + "");
                dataIsChange();
            }
        });

        initOptionPicker();
//        List<String> list = new ArrayList<>();
//        list.add("http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-11%2010%3A34%3A39-0.jpg");
//        list.add("http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-11%2010%3A34%3A39-1.jpg");
//        CommonUtils.deleteOssObject("http://gzyxg.oss-cn-shenzhen.aliyuncs.com/lanmei/yixiuge/img1/2018-09-11-22-00-25-0.jpg");
    }

    private void initOptionPicker() {
        educationPicker = new OptionPicker(this, new String[]{
                "初中", "中专", "高中", "大专", "本科", "研究生", "博士", "临床博士后"
        });
        educationPicker.setOffset(3);
        educationPicker.setSelectedIndex(4);
        educationPicker.setTextSize(18);
        educationPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                educationTv.setText(item);
            }
        });

        studentNaturePicker = new OptionPicker(this, new String[]{
                "院本部（包括分院）", "社会委培", "并轨研究生", "临床博士后"
        });
        studentNaturePicker.setOffset(2);
        studentNaturePicker.setSelectedIndex(1);
        studentNaturePicker.setTextSize(18);
        studentNaturePicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                studentNatureTv.setText(item);
            }
        });

        learnSubjectPicker = new OptionPicker(this, new String[]{
                "普外科（已学）", "神经外科", "胸心外科", "泌尿外科", "整形外科", "骨科", "儿外科"
        });
        learnSubjectPicker.setOffset(2);
        learnSubjectPicker.setSelectedIndex(1);
        learnSubjectPicker.setTextSize(18);
        learnSubjectPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                learnSubjectTv.setText(item);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @OnClick({R.id.ll_personal_icons, R.id.ll_name, R.id.ll_address, R.id.ll_phone, R.id.save_bt, R.id.ll_email, R.id.ll_address_details
            , R.id.ll_weixin, R.id.ll_unit, R.id.ll_education, R.id.ll_school, R.id.ll_student_type, R.id.ll_student_nature, R.id.ll_learn_subject})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_personal_icons://上传头像
                cameraHelper.showDialog();
                break;
            case R.id.ll_name://姓名
                startActivityPersonal(100, CommonUtils.getStringByTextView(nameTv));
                break;
            case R.id.ll_phone://电话
                startActivityPersonal(101, CommonUtils.getStringByTextView(phoneTv));
                break;
            case R.id.ll_address://地址
                if (addressPicker != null) {
                    addressPicker.show();
                }
                break;
            case R.id.ll_address_details://详细地址
                startActivityPersonal(102, CommonUtils.getStringByTextView(addressDetailsTv));
                break;
            case R.id.ll_email://邮箱
                startActivityPersonal(103, CommonUtils.getStringByTextView(emailTv));
                break;
            case R.id.save_bt://保存
                ajaxSaveDate();
                break;
            case R.id.ll_weixin://微信
                startActivityPersonal(104, CommonUtils.getStringByTextView(weixinTv));
                break;
            case R.id.ll_unit://单位
                CommonUtils.developing(this);
                break;
            case R.id.ll_education://学历
                if (educationPicker != null) {
                    educationPicker.show();
                }
                break;
            case R.id.ll_school://毕业院校
                CommonUtils.developing(this);
                break;
            case R.id.ll_student_type://学生类型（不可以修改，只显示，后台可以编辑）(只是学生才有这个选项)
                //无操作
                break;
            case R.id.ll_student_nature://学员性质
                if (studentNaturePicker != null) {
                    studentNaturePicker.show();
                }
                break;
            case R.id.ll_learn_subject://要学科室
                if (learnSubjectPicker != null) {
                    learnSubjectPicker.show();
                }
                break;
        }
    }

    private void startActivityPersonal(int type, String value) {
        IntentUtil.startActivityForResult(this, PersonalCompileSubActivity.class, value, type);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        L.d("onActivityResult", "requestCode:" + requestCode + ":resultCode:" + resultCode);
        String compile = "";
        if (data != null) {
            compile = data.getStringExtra("compile");
        }
        String image;
        switch (requestCode) {
            case CameraHelper.CHOOSE_FROM_GALLAY:
                image = ImageUtils.getImageFileFromPickerResult(this, data);
                cameraHelper.startActionCrop(image);
                break;
            case CameraHelper.CHOOSE_FROM_CAMERA:
                //注意小米拍照后data 为null
                image = cameraHelper.getTempImage().getPath();
                cameraHelper.startActionCrop(image);
                break;
            case CameraHelper.RESULT_FROM_CROP:
                cameraHelper.uploadNewPhoto(mAvatarIv);//
                dataIsChange();
                break;
            case 100:
                setTextView(nameTv, compile);
                break;
            case 101:
                setTextView(phoneTv, compile);
                break;
            case 102:
                setTextView(addressDetailsTv, compile);
                break;
            case 103:
                setTextView(emailTv, compile);
                break;
            case 104:
                setTextView(weixinTv, compile);
                break;
            default:
                break;
        }
    }

    private void setTextView(TextView textView, String compile) {
        textView.setText(compile);
        dataIsChange();
    }


    //资料是否有改动过
    private void dataIsChange() {
        String cName = CommonUtils.getStringByTextView(nameTv);
        String cEmail = CommonUtils.getStringByTextView(emailTv);
        String cWeixin = CommonUtils.getStringByTextView(weixinTv);
        String cPhone = CommonUtils.getStringByTextView(phoneTv);
        String cArea = CommonUtils.getStringByTextView(addressTv);
        String cAddress = CommonUtils.getStringByTextView(addressDetailsTv);
        if (!StringUtils.isSame(cName, name)
                || !StringUtils.isSame(cArea, area)
                || !StringUtils.isSame(cAddress, address)
                || !StringUtils.isSame(cAddress, address)
                || !StringUtils.isSame(cWeixin, weixin)
                || !StringUtils.isSame(sex, chooseSex)
                || !StringUtils.isSame(cPhone, phone)
                || !StringUtils.isSame(cameraHelper.getHeadPathStr(), pic)
                || !StringUtils.isSame(cEmail, email)) {
            mSaveButton.setEnabled(true);
            mSaveButton.setBackgroundResource(R.drawable.button_corners);
        } else {
            mSaveButton.setEnabled(false);
            mSaveButton.setBackgroundResource(R.drawable.button_unable);
        }
    }


    private void ajaxSaveDate() {
        if (StringUtils.isSame(cameraHelper.getHeadPathStr(), pic)) {
            loadUpDate("");
        } else {
            cameraHelper.execute();
        }
    }

    private void loadUpDate(final String url) {
        final String name = CommonUtils.getStringByTextView(nameTv);
        final String email = CommonUtils.getStringByTextView(emailTv);
        final String area = CommonUtils.getStringByTextView(addressTv);
        final String address = CommonUtils.getStringByTextView(addressDetailsTv);

        YiXiuGeApi api = new YiXiuGeApi("app/upuser");
        api.addParams("userid", api.getUserId(this));
        api.addParams("nickname", name);
        api.addParams("area", area);
        api.addParams("sex", sex);
        api.addParams("email", email);
        api.addParams("address", address);
        if (StringUtils.isEmpty(url)) {
            api.addParams("pic", pic);
        } else {
            api.addParams("pic", url);
        }
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                if (!StringUtils.isEmpty(url)) {
                    CommonUtils.deleteOssObject(pic);//更新头像后删除从oss删除旧的头像
                }
                UIHelper.ToastMessage(getContext(), "修改资料成功");
                CommonUtils.loadUserInfo(getContext(), null);
                finish();
            }
        });
    }

    /**
     * 一步消息获取地址 信息
     */
    private class AddressAsyncTask extends AsyncTask<String, Integer, ArrayList<Province>> {

        public AddressAsyncTask() {

        }

        @Override
        protected ArrayList<Province> doInBackground(String... params) {
            ArrayList<Province> data = new ArrayList<Province>();
            String json = AssetsUtils.getStringFromAssert(getApplicationContext(), "city.json");
            data.addAll(JSON.parseArray(json, Province.class));
            return data;
        }

        protected void onPostExecute(ArrayList<Province> result) {
            initAddressPicker(result);
        }
    }

    AddressPicker addressPicker;

    private void initAddressPicker(ArrayList<Province> data) {
        addressPicker = new AddressPicker(this, data);
        addressPicker.setSelectedItem("广东", "广州", "天河");
        addressPicker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(Province province, City city, County county) {
//                area = province.getAreaId() + "," + city.getAreaId() + "," + county.getAreaId();
                addressTv.setText(province.getAreaName() + "" + city.getAreaName() + "" + county.getAreaName());
                dataIsChange();
//                L.d("AddressPicker", province.getAreaId() + "," + city.getAreaId() + "," + county.getAreaId());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addressAsyncTask != null && addressAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            addressAsyncTask.cancel(true);
        }
    }

}
