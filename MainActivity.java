package com.lisahuaying.healthandbmi;

import android.database.DataSetObserver;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.lisahuaying.healthandbmi.R;

import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity  {


    private ViewPager mViewPager;
    private ViewAdapter mViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bmi);
        setViewPager();
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int heightPixels = displayMetrics.heightPixels;
        int widthPixels = displayMetrics.widthPixels;
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



    private void setViewPager() {
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewAdapter = new ViewAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewAdapter);
    }

    public class ViewAdapter extends FragmentPagerAdapter {
        public ViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Fragment1();
                case 1:
                    return new Fragment2();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public static class Fragment1 extends Fragment implements View.OnClickListener{
        private TextView mTextViewHeight;
        private EditText mEditText;
        private TextView mTextViewWeight;
        private EditText mEditText2;
        private TextView mTextViewGender;
        private RadioButton mRadioButtonM;
        private RadioButton mRadioButtonF;
        private Button mButtonBMI;
        private TextView mTextViewResult;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment1,null);
            mEditText = (EditText) view.findViewById(R.id.editText);
            mEditText2 = (EditText) view.findViewById(R.id.editText2);
            mRadioButtonM = (RadioButton) view.findViewById(R.id.radioButtonM);
            mRadioButtonF = (RadioButton) view.findViewById(R.id.radioButtonF);
            mButtonBMI = (Button) view.findViewById(R.id.buttonBMI);
            mButtonBMI.setOnClickListener(this);
            mTextViewResult = (TextView) view.findViewById(R.id.textViewResult);
            mTextViewResult.setMovementMethod(ScrollingMovementMethod.getInstance());
            return view;
        }

        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(mEditText.getText())) {
                Toast.makeText(getActivity(), R.string.enterheight, Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(mEditText2.getText())) {
                Toast.makeText(getActivity(), R.string.enterweight, Toast.LENGTH_LONG).show();
                return;
            }


            double h, w, bmi, stdweight;
            h = Double.parseDouble(mEditText.getText().toString()) ;
            w = Double.parseDouble(mEditText2.getText().toString());
            bmi = w * 100 * 100 / (h * h);

            if (mRadioButtonM.isChecked() == true)
                stdweight = (h-80) * 0.7;
            else
                stdweight = (h-70) * 0.6;
            DecimalFormat nf = new DecimalFormat("0.0");
            mTextViewResult.setText(getString(R.string.textResult1) + String.valueOf(nf.format(bmi)) +
                    " \n" + getString(R.string.textResult2) + String.valueOf(nf.format(stdweight)) +
                    "\n" + "\n" + getString(R.string.standard) + "\n" + "\n" + "\n" +
                    getString(R.string.wish) + "\n" + "\n");
        }
    }

    public static class Fragment2 extends Fragment implements View.OnClickListener{
        private Spinner mSpinner;
        private EditText mEditTextEnterWeight;
        private Button mButtonPlusH;
        private Button mButtonPlusM;
        private Button mButtonPlusS;
        private Button mButtonMinusH;
        private Button mButtonMinusM;
        private Button mButtonMinusS;
        private TextView mTextView;
        private Button mButtonStart;
        private Button mButtonPause;
        private Button mButtonStop;
        private TextView mTextViewResult;
        private Button mButtonCheck;
        private int mHour = 0;
        private int mMinutes = 0;
        private int mSeconds = 0;
        private  CountDownTimer mCountDownTimer;

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (mCountDownTimer!= null) {
                mCountDownTimer.cancel();
            }
        }

        @Nullable
        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment2, null);

            mSpinner = (Spinner) view.findViewById(R.id.spinner);
            MyAdapter adp = new MyAdapter();
            mSpinner.setAdapter(adp);
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) return;
                    if (position == 0){
                        Toast.makeText(getActivity(), "請選擇運動方式", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "你選的是：" + ((MyAdapter.ViewHolder) view.getTag()).textView.getText(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mEditTextEnterWeight = (EditText) view.findViewById(R.id.editTextEnterWeight);
            mButtonPlusH = (Button) view.findViewById(R.id.buttonPlusH);
            mButtonPlusH.setOnClickListener(this);
            mButtonPlusM = (Button) view.findViewById(R.id.buttonPlusM);
            mButtonPlusM.setOnClickListener(this);
            mButtonPlusS = (Button) view.findViewById(R.id.buttonPlusS);
            mButtonPlusS.setOnClickListener(this);
            mButtonMinusH = (Button) view.findViewById(R.id.buttonMinusH);
            mButtonMinusH.setOnClickListener(this);
            mButtonMinusM = (Button) view.findViewById(R.id.buttonMinusM);
            mButtonMinusM.setOnClickListener(this);
            mButtonMinusS = (Button) view.findViewById(R.id.buttonMinusS);
            mButtonMinusS.setOnClickListener(this);
            mTextView = (TextView) view.findViewById(R.id.textView);
            mButtonStart = (Button) view.findViewById(R.id.buttonStart);
            mButtonStart.setOnClickListener(this);
            mButtonPause = (Button) view.findViewById(R.id.buttonPause);
            mButtonPause.setOnClickListener(this);
            mButtonStop = (Button) view.findViewById(R.id.buttonStop);
            mButtonStop.setOnClickListener(this);
            mTextViewResult = (TextView) view.findViewById(R.id.textViewResult);
            mButtonCheck = (Button) view.findViewById(R.id.buttonCheck);
            mButtonCheck.setOnClickListener(this);

            return view;
        }


        @Override
        public void onClick(View v) {
            int position = v.getId();
            switch (position) {
                case R.id.buttonPlusH:
                    if (mHour == 98) {
                        mButtonPlusH.setEnabled(false);
                    }
                    if (mHour == 0) {
                        mButtonMinusH.setEnabled(true);
                    }
                    mHour = mHour +1;
                    setTime();
                    break;
                case R.id.buttonPlusM:
                    if (mMinutes == 58) {
                        mButtonPlusM.setEnabled(false);
                    }
                    if (mMinutes == 0) {
                        mButtonMinusM.setEnabled(true);
                    }
                    mMinutes = mMinutes +1;
                    setTime();
                    break;
                case R.id.buttonPlusS:
                    if (mSeconds == 58) {
                        mButtonPlusS.setEnabled(false);
                    }
                    if (mSeconds == 0) {
                        mButtonMinusS.setEnabled(true);
                    }
                    mSeconds = mSeconds +1;
                    setTime();
                    break;
                case R.id.buttonMinusH:
                    if (mHour == 1) {
                        mButtonMinusH.setEnabled(false);
                    }
                    if (mHour == 99) {
                        mButtonPlusH.setEnabled(true);
                    }
                    mHour = mHour -1;
                    setTime();
                    break;
                case R.id.buttonMinusM:
                    if (mMinutes == 1) {
                        mButtonMinusM.setEnabled(false);
                    }
                    if (mMinutes == 59) {
                        mButtonPlusM.setEnabled(true);
                    }
                    mMinutes = mMinutes -1;
                    setTime();
                    break;
                case R.id.buttonMinusS:
                    if (mSeconds == 1) {
                        mButtonMinusS.setEnabled(false);
                    }
                    if (mSeconds == 59) {
                        mButtonPlusS.setEnabled(true);
                    }
                    mSeconds = mSeconds -1;
                    setTime();
                    break;
                case R.id.buttonStart:

                    if (mCountDownTimer != null) {
                        mCountDownTimer.cancel();
                    }

                    if (mHour == 0&&mMinutes == 0&&mSeconds == 0) return;
                    mCountDownTimer = new Timer(mHour*3600+mMinutes*60+mSeconds+2);
                    mCountDownTimer.start();
                    mButtonStart.setEnabled(false);
                    mButtonPlusH.setEnabled(false);
                    mButtonPlusM.setEnabled(false);
                    mButtonPlusS.setEnabled(false);
                    mButtonMinusH.setEnabled(false);
                    mButtonMinusM.setEnabled(false);
                    mButtonMinusS.setEnabled(false);
                    break;
                case R.id.buttonPause:
                    if (mCountDownTimer == null ) return;
                    mButtonStart.setEnabled(true);
                    mCountDownTimer.cancel();
                    mButtonMinusS.setEnabled(true);
                    mButtonPlusS.setEnabled(true);
                    mButtonMinusM.setEnabled(true);
                    mButtonPlusM.setEnabled(true);
                    mButtonMinusH.setEnabled(true);
                    mButtonPlusH.setEnabled(true);

                    if (mSeconds == 0) {
                        mButtonMinusS.setEnabled(false);
                    }

                    if (mSeconds == 59) {
                        mButtonPlusS.setEnabled(false);
                    }

                    if (mMinutes == 0) {
                        mButtonMinusM.setEnabled(false);
                    }

                    if (mMinutes == 59) {
                        mButtonPlusM.setEnabled(false);
                    }

                    if (mHour == 0) {
                        mButtonMinusH.setEnabled(false);
                    }

                    if (mHour == 99) {
                        mButtonPlusH.setEnabled(false);
                    }
                    break;
                case R.id.buttonStop:
                    if (mCountDownTimer == null) return;
                    mButtonMinusS.setEnabled(false);
                    mButtonPlusS.setEnabled(true);
                    mButtonMinusM.setEnabled(false);
                    mButtonPlusM.setEnabled(true);
                    mButtonMinusH.setEnabled(false);
                    mButtonPlusH.setEnabled(true);
                    mHour = 0;
                    mMinutes = 0;
                    mSeconds = 0;
                    mCountDownTimer.cancel();
                    setTime();
                    mButtonStart.setEnabled(true);
                    break;


                case R.id.buttonCheck:
                    if (TextUtils.isEmpty(mEditTextEnterWeight.getText().toString())) {
                        Toast.makeText(getActivity(), R.string.enterweight, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double wght, time, carol = 0;
                    wght = Double.parseDouble(mEditTextEnterWeight.getText().toString());
                    time = mHour + Float.valueOf(mMinutes) / 60 + Float.valueOf(mSeconds) / 3600;
                    switch (mSpinner.getSelectedItemPosition()) {
                        case 0:
                            break;
                        case 1:
                            carol = wght * 2.5 * time;
                            break;
                        case 2:
                            carol = wght * 3 * time;
                            break;
                        case 3:
                            carol = wght * 3.1 * time;
                            break;
                        case 4:
                            carol = wght * 4.4 * time;
                            break;
                        case 5:
                            carol = wght * 4.4 * time;
                            break;
                        case 6:
                            carol = wght * 5.0 * time;
                            break;
                        case 7:
                            carol = wght * 5.1 * time;
                            break;
                        case 8:
                            carol = wght * 6.2 * time;
                            break;
                        case 9:
                            carol = wght * 9.4 * time;
                            break;
                        case 10:
                            carol = wght * 12.4 * time;
                            break;
                        case 11:
                            carol = wght * 13.2 * time;
                            break;
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("00.0");
                    mTextViewResult.setText("本次運動消耗" + String.valueOf(decimalFormat.format(carol)) + "大卡");

            }
        }

        private class Timer extends CountDownTimer{
            private boolean first = true;
            public Timer(long millisInFuture, long countDownInterval) {
                super(millisInFuture * 1000, countDownInterval * 1000);

            }

            public Timer(long millisInFuture) {
                this(millisInFuture, 1);

            }


            @Override
            public void onTick(long millisUntilFinished) {

                if (first == true) {
                    first = false;
                    return;
                }

                if (mSeconds>0) {
                    mSeconds = mSeconds -1;
                }else if (mMinutes >0) {
                    mMinutes = mMinutes -1;
                    mSeconds = 59;
                }else if (mHour >0) {
                    mHour = mHour -1;
                    mMinutes = 59;
                    mSeconds = 59;
                }

                setTime();
            }

            @Override
            public void onFinish() {
                mButtonMinusS.setEnabled(false);
                mButtonPlusS.setEnabled(true);
                mButtonMinusM.setEnabled(false);
                mButtonPlusM.setEnabled(true);
                mButtonMinusH.setEnabled(false);
                mButtonPlusH.setEnabled(true);
                mButtonStart.setEnabled(true);
            }
        }

        private void setTime() {
            StringBuilder stringBuilder = new StringBuilder();
            if (mHour<10&&mHour>-1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(mHour).append("：");
            if (mMinutes<10&&mMinutes>-1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(mMinutes).append("：");
            if (mSeconds<10&&mSeconds>-1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(mSeconds);
            mTextView.setText(stringBuilder.toString());
        }

        private class MyAdapter implements SpinnerAdapter {
            private LayoutInflater mInflater = getActivity().getLayoutInflater();

            @Override
            public Object getItem(int position) {
                ViewHolder viewHolder = new ViewHolder();
                setView(viewHolder,position);
                return viewHolder;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return 11;
            }

            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {
                ViewHolder  viewHolder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.spinner,null);
                    final TextView textView = (TextView) convertView.findViewById(R.id.textView);
                    viewHolder = new ViewHolder();
                    viewHolder.textView = textView;
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                setView(viewHolder,position);

                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return getView(position, convertView, parent);
            }

            private class ViewHolder{
                TextView textView;
            }

            private void setView(ViewHolder viewHolder,int position) {
                String string;
                switch (position){
                    case 0:
                        string = "-----------下拉選擇運動方式----------";
                        viewHolder.textView.setText(string);
                        break;
                    case 1:
                        string = "拉筋運動     消耗熱量2.5";
                        viewHolder.textView.setText(string);
                        break;
                    case 2:
                        string = "腳踏車8.8km/hr     消耗熱量3.0";
                        viewHolder.textView.setText(string);
                        break;
                    case 3:
                        string = "慢走4.0km/hr     消耗熱量3.1";
                        viewHolder.textView.setText(string);
                        break;
                    case 4:
                        string = "快走6.0km/hr     消耗熱量4.4";
                        viewHolder.textView.setText(string);
                        break;
                    case 5:
                        string = "游泳0.4km/hr     消耗熱量4.4";
                        viewHolder.textView.setText(string);
                        break;
                    case 6:
                        string = "有氧舞蹈     消耗熱量5.0";
                        viewHolder.textView.setText(string);
                        break;
                    case 7:
                        string = "羽球 / 排球     消耗熱量5.1";
                        viewHolder.textView.setText(string);
                        break;
                    case 8:
                        string = "網球     消耗熱量6.2";
                        viewHolder.textView.setText(string);
                        break;
                    case 9:
                        string = "慢跑8.7km/hr     消耗熱量9.4";
                        viewHolder.textView.setText(string);
                        break;
                    case 10:
                        string = "划船比賽     消耗熱量12.4";
                        viewHolder.textView.setText(string);
                        break;
                    case 11:
                        string = "快跑16.km/hr     消耗熱量13.2";
                        viewHolder.textView.setText(string);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
