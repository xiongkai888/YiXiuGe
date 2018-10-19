package com.lanmei.yixiu.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lanmei.yixiu.R;
import com.lanmei.yixiu.api.YiXiuGeApi;
import com.lanmei.yixiu.bean.EducationBean;
import com.lanmei.yixiu.helper.AddressAsyncTask;
import com.lanmei.yixiu.helper.CameraHelper;
import com.lanmei.yixiu.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
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
import java.util.List;

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

    @InjectView(R.id.ll_school)
    RelativeLayout llSchool;//毕业院校
    @InjectView(R.id.ll_student_type)
    RelativeLayout llStudentType;//学生类型
    @InjectView(R.id.ll_student_nature)
    RelativeLayout llStudentNature;//学员性质
    @InjectView(R.id.ll_learned_subject)
    RelativeLayout llLearnedSubject;//已学科室
    @InjectView(R.id.ll_learn_subject)
    RelativeLayout llLearnSubject;//要学科室

    private String pic;//头像
    private String name;//姓名
    private String phone;//手机号码
    private String weixin;//微信号
    private String email;
    private String sex;//性别
    private String chooseSex;
    private String area;//地址
    private String address;//详细地址
    private String unit;//单位
    private String school;//学校
    private String education;//学历
    private String technicalPost;//职称
    private String political;//政治面貌

    @InjectView(R.id.weixin_tv)
    TextView weixinTv;
    @InjectView(R.id.unit_tv)
    TextView unitTv;
    @InjectView(R.id.education_tv)
    TextView educationTv;//学历
    @InjectView(R.id.school_tv)
    TextView schoolTv;//学校
    @InjectView(R.id.student_type_tv)
    TextView studentTypeTv;//学生类型
    @InjectView(R.id.student_nature_tv)
    TextView studentNatureTv;//学员性质
    @InjectView(R.id.learned_subject_tv)
    TextView learnedSubjectTv;//已学
    @InjectView(R.id.learn_subject_tv)
    TextView learnSubjectTv;//要学
    @InjectView(R.id.technical_post_tv)
    TextView technicalPostTv;//职称
    @InjectView(R.id.sex_tv)
    TextView sexTv;//性别
    @InjectView(R.id.politics_status_tv)
    TextView politicsStatusTv;//政治面貌

    private CameraHelper cameraHelper;
    private UserBean bean;
    private List<EducationBean> educationList;//学历列表
    private List<EducationBean> technicalPostList;//职称列表
    private List<EducationBean> politicalList;//政治面貌列表
    private int technicalPostIndex = -1;//职称下标
    private int politicalIndex = -1;//政治面貌

    private AddressAsyncTask addressAsyncTask;
    private OptionPicker educationPicker;//学历选择器
    private OptionPicker technicalPostPicker;//职称选择器
    private OptionPicker politicalPicker;//政治面貌选择器


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
        addressAsyncTask.setAddressAsyncTaskListener(new AddressAsyncTask.AddressAsyncTaskListener() {
            @Override
            public void setAddressList(ArrayList<Province> result) {
                initAddressPicker(result);
            }
        });
        addressAsyncTask.execute();

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
            school = bean.getSchool();
            unit = bean.getUnit();
            education = bean.getEducation();
            technicalPost = bean.getPosition();
            political = bean.getPolitical();
            if (StringUtils.isSame(sex, CommonUtils.isZero)) {
                mRadgroup.setVisibility(View.VISIBLE);
                mRadgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        if (checkedId == R.id.btnMan) {
                            sex = CommonUtils.isOne;
                        } else if (checkedId == R.id.btnWoman) {
                            sex = CommonUtils.isTwo;
                        }
                        dataIsChange();
                    }
                });
            } else {
                if (StringUtils.isSame(sex, CommonUtils.isOne)) {
                    sexTv.setText(R.string.man);
                } else if (StringUtils.isSame(sex, CommonUtils.isTwo)) {
                    sexTv.setText(R.string.woman);
                }
            }

            nameTv.setText(name);
            emailTv.setText(email);
            addressTv.setText(area);
            phoneTv.setText(phone);
            weixinTv.setText(weixin);
            addressDetailsTv.setText(address);
            learnedSubjectTv.setText(bean.getLearned());
            learnSubjectTv.setText(bean.getNolearned());
            schoolTv.setText(school);
            unitTv.setText(unit);
            educationTv.setText(education);
            studentTypeTv.setText(bean.getStudent_type());
            studentNatureTv.setText(bean.getStudent_nature());
            initEducation(6);//学历
            if (StringUtils.isEmpty(technicalPost)) {//为空可以选择
                initEducation(4);//职称
                setCompoundDrawables(technicalPostTv, R.drawable.in_right);
            }else {
                technicalPostTv.setText(technicalPost);
            }
            if (StringUtils.isEmpty(political)) {//为空可以选择
                initEducation(8);//政治面貌
                setCompoundDrawables(politicsStatusTv, R.drawable.in_right);
            }else {
                politicsStatusTv.setText(political);
            }
            pic = bean.getPic();
            cameraHelper.setHeadPathStr(pic);
            ImageHelper.load(this, pic, mAvatarIv, null, true, R.drawable.default_pic, R.drawable.default_pic);

            if (!CommonUtils.isStudent(this)) {//是教师隐藏
                llSchool.setVisibility(View.GONE);
                llStudentType.setVisibility(View.GONE);
                llStudentNature.setVisibility(View.GONE);
                llLearnedSubject.setVisibility(View.GONE);
                llLearnSubject.setVisibility(View.GONE);
            }
        }
        mSaveButton.setEnabled(false);
        mSaveButton.setBackgroundResource(R.drawable.button_unable);
        initEducationPicker();
    }

    /**
     * @param drawableId 项目图片id
     */
    private void setCompoundDrawables(TextView textView, int drawableId) {
        Drawable img = getResources().getDrawable(drawableId);
// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        textView.setCompoundDrawables(null, null, img, null); //设置右图标
    }

    //学历列表
    private void initEducation(final int type) {
        YiXiuGeApi api = new YiXiuGeApi("app/geteducation");
        api.addParams("pid", type);//4|6=>职称|学历
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<EducationBean>>() {
            @Override
            public void onResponse(NoPageListBean<EducationBean> response) {
                if (isFinishing()) {
                    return;
                }
                switch (type){
                    case 4:
                        technicalPostList = response.data;
                        initPositionPicker();
                        break;
                    case 6:
                        educationList = response.data;
                        initEducationPicker();
                        break;
                    case 8:
                        politicalList = response.data;
                        initPoliticalPicker();
                        break;
                }
            }
        });
    }


    private AddressPicker addressPicker;

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


    //初始化学历选择器
    private void initEducationPicker() {
        if (StringUtils.isEmpty(educationList)) {
            return;
        }
        int size = educationList.size();
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = educationList.get(i).getName();
        }
        educationPicker = new OptionPicker(this, strings);
        educationPicker.setOffset(3);
        educationPicker.setSelectedIndex(1);
        educationPicker.setTextSize(18);
        educationPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                educationTv.setText(item);
                dataIsChange();
            }
        });
    }

    //初始化职称选择器
    private void initPositionPicker() {
        if (StringUtils.isEmpty(technicalPostList)) {
            return;
        }
        int size = technicalPostList.size();
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = technicalPostList.get(i).getName();
        }
        technicalPostPicker = new OptionPicker(this, strings);
        technicalPostPicker.setOffset(3);
        technicalPostPicker.setSelectedIndex(1);
        technicalPostPicker.setTextSize(18);
        technicalPostPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                technicalPostIndex = index;
                technicalPostTv.setText(item);
                dataIsChange();
            }
        });
    }

    //政治面貌选择器
    private void initPoliticalPicker() {
        if (StringUtils.isEmpty(politicalList)) {
            return;
        }
        int size = politicalList.size();
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] = politicalList.get(i).getName();
        }
        politicalPicker = new OptionPicker(this, strings);
        politicalPicker.setOffset(3);
        politicalPicker.setSelectedIndex(1);
        politicalPicker.setTextSize(18);
        politicalPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                politicalIndex = index;
                politicsStatusTv.setText(item);
                dataIsChange();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @OnClick({R.id.ll_personal_icons,R.id.ll_politics_status, R.id.ll_name, R.id.ll_address, R.id.ll_phone, R.id.save_bt, R.id.ll_email, R.id.ll_address_details
            , R.id.ll_weixin, R.id.ll_unit, R.id.ll_education, R.id.ll_school, R.id.ll_technical_post})
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
                startActivityPersonal(106, CommonUtils.getStringByTextView(unitTv));
                break;
            case R.id.ll_education://学历
                if (educationPicker != null) {
                    educationPicker.show();
                }
                break;
            case R.id.ll_politics_status://政治面貌
                if (politicalPicker != null) {
                    politicalPicker.show();
                }
                break;
            case R.id.ll_school://毕业院校
                startActivityPersonal(105, CommonUtils.getStringByTextView(schoolTv));
                break;
            case R.id.ll_technical_post://职称
                if (technicalPostPicker != null) {
                    technicalPostPicker.show();
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
            case 105:
                setTextView(schoolTv, compile);
                break;
            case 106:
                setTextView(unitTv, compile);
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
        String cArea = CommonUtils.getStringByTextView(addressTv);
        String cSchool = CommonUtils.getStringByTextView(schoolTv);
        String cUnit = CommonUtils.getStringByTextView(unitTv);
        String cEducation = CommonUtils.getStringByTextView(educationTv);
        String cPolitical = CommonUtils.getStringByTextView(politicsStatusTv);
        String cTechnicalPost = CommonUtils.getStringByTextView(technicalPostTv);
        String cAddress = CommonUtils.getStringByTextView(addressDetailsTv);
        if (!StringUtils.isSame(cName, name)
                || !StringUtils.isSame(cArea, area)
                || !StringUtils.isSame(cAddress, address)
                || !StringUtils.isSame(cAddress, address)
                || !StringUtils.isSame(cWeixin, weixin)
                || !StringUtils.isSame(sex, chooseSex)
                || !StringUtils.isSame(cSchool, school)
                || !StringUtils.isSame(cUnit, unit)
                || !StringUtils.isSame(cPolitical, political)
                || !StringUtils.isSame(cEducation, education)
                || !StringUtils.isSame(cTechnicalPost, technicalPost)
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
        if (technicalPostIndex != -1 && !StringUtils.isEmpty(technicalPostList)) {//职称
            api.addParams("position", technicalPostList.get(technicalPostIndex).getId());
        }
        if (politicalIndex != -1 && !StringUtils.isEmpty(politicalList)) {//政治面貌
            api.addParams("political", politicalList.get(politicalIndex).getId());
        }
        api.addParams("weixin", CommonUtils.getStringByTextView(weixinTv));
        api.addParams("unit", CommonUtils.getStringByTextView(unitTv));
        api.addParams("school", CommonUtils.getStringByTextView(schoolTv));
        api.addParams("education", CommonUtils.getStringByTextView(educationTv));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addressAsyncTask != null && addressAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            addressAsyncTask.cancel(true);
        }
        addressAsyncTask = null;
        educationPicker = null;
        technicalPostPicker = null;
    }
}
