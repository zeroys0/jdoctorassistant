package com.jxj.jdoctorassistant.main.contact;

import com.jxj.jdoctorassistant.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ToolsContactsActivity extends Activity {

	private Context context;

	@Bind(R.id.contacts_btn)
	RadioButton contactsBtn;
	@Bind(R.id.relatives_btn)
	RadioButton relativesBtn;
	@Bind(R.id.emery_contacts_btn)
	RadioButton emeyContactsBtn;
	@Bind(R.id.contacts_radiogroup)
	RadioGroup contactsRadioGroup;

	@OnClick({ R.id.tools_backbtn })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.tools_backbtn:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tools_contacts);
		context = this;
		ButterKnife.bind(this);
		initviews();

	}

	private void initviews() {

		Fragment toolsFragment = new FragmentToolsContacts();
		FragmentTransaction toolsFt = getFragmentManager().beginTransaction();
		toolsFt.replace(R.id.contacts_ll, toolsFragment);
		toolsFt.commit();

		contactsRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int id) {
						Fragment fragment = null;
						switch (id) {
						case R.id.contacts_btn:

							fragment = new FragmentToolsContacts();
							FragmentTransaction ft = getFragmentManager()
									.beginTransaction();
							ft.replace(R.id.contacts_ll, fragment);
							ft.commit();

							contactsBtn.setTextColor(getResources().getColor(
									R.color.login_loginBtn_bgColor));
							emeyContactsBtn.setTextColor(getResources()
									.getColor(R.color.white_bg));
							relativesBtn.setTextColor(getResources().getColor(
									R.color.white_bg));
							break;

						case R.id.relatives_btn:
							fragment = new FragmentToolsRelatives();
							FragmentTransaction relaft = getFragmentManager()
									.beginTransaction();
							relaft.replace(R.id.contacts_ll, fragment);
							relaft.commit();
							relativesBtn.setTextColor(getResources().getColor(
									R.color.login_loginBtn_bgColor));
							contactsBtn.setTextColor(getResources().getColor(
									R.color.white_bg));
							emeyContactsBtn.setTextColor(getResources()
									.getColor(R.color.white_bg));
							break;
						case R.id.emery_contacts_btn:
							fragment = new FragmentToolsEmeyContacts();
							FragmentTransaction emeyft = getFragmentManager()
									.beginTransaction();
							emeyft.replace(R.id.contacts_ll, fragment);
							emeyft.commit();
							emeyContactsBtn.setTextColor(getResources()
									.getColor(R.color.login_loginBtn_bgColor));
							relativesBtn.setTextColor(getResources().getColor(
									R.color.white_bg));
							contactsBtn.setTextColor(getResources().getColor(
									R.color.white_bg));

							break;

						default:
							break;
						}
					}
				});
	}
}
