package com.happyq.rvn.happyq;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.happyq.rvn.happyq.sigin.Login;
import com.happyq.rvn.happyq.sigin.UserSessionManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.happyq.rvn.happyq.data.Constant;
import com.happyq.rvn.happyq.data.SharedPref;
import com.happyq.rvn.happyq.data.ThisApplication;
import com.happyq.rvn.happyq.data.Tools;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * ATTENTION : To see where list of setting comes is open res/xml/setting_notification.xml
 * */
public class ActivitySetting extends PreferenceActivity {



    private ImageLoader imgloader = ImageLoader.getInstance();
    private View parent_view;
    private Toolbar toolbar;
    private SharedPref sharedPref;
    SwitchPreference pref;
    Context _context;
    UserSessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.setting_notification);

        // return inflater.inflate(R.xml.setting_notification, container, false);

        parent_view = findViewById(android.R.id.content);

        sharedPref = new SharedPref(getApplicationContext());
        pref = (SwitchPreference)findPreference("SWITCH");
        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to their values. When their values change, their summaries are updated to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_ringtone)));

        Preference notifPref = findPreference(getString(R.string.pref_key_notif));
        Preference locPref = findPreference(getString(R.string.pref_key_loc));

        Preference aboutPref = findPreference(getString(R.string.pref_key_about));
        Preference termsServicePref = findPreference(getString(R.string.pref_key_terms_service));

        Preference resetCachePref = findPreference(getString(R.string.pref_key_reset_cache));
        Preference themePref = findPreference(getString(R.string.pref_key_theme));
        Preference ratePref = findPreference(getString(R.string.pref_key_rate));
        Preference logoutPref = findPreference(getString(R.string.pref_key_logout));


        //Preference themePref = findPreference(getString(R.string.pref_key_theme));
        //Preference ratePref = findPreference("key_rate");
        //Preference aboutPref = findPreference("key_about");

      /*  resetCachePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder =new AlertDialog.Builder(ActivitySetting.this);
                builder.setTitle(getString(R.string.dialog_confirm_title));
                builder.setMessage(getString(R.string.message_clear_image_cache));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imgloader.clearDiskCache();
                        imgloader.clearMemoryCache();
                        Snackbar.make(parent_view, getString(R.string.message_after_clear_image_cache), Snackbar.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                return true;
            }
        });
        */

        notifPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean flag = (boolean) o;
                // analytics tracking
                //ThisApplication.getInstance().trackEvent(Constant.Event.NOTIFICATION.name(), (flag ? "ENABLE" : "DISABLE"), "-");
                return true;
            }
        });

        locPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                        boolean flag = (boolean) newValue;

                boolean switched = ((SwitchPreference) preference).isChecked();
                if(!switched) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetting.this);
                    builder.setMessage(R.string.dialog_content_gps);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }
                    });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }
                return true;

            }


        });

        ratePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Tools.rateAction(ActivitySetting.this);
                return true;
            }
        });

        aboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Tools.aboutAction(ActivitySetting.this);
                return true;
            }
        });

         termsServicePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Tools.termsofservicesAction(ActivitySetting.this);
                return true;
            }
        });

        ratePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Tools.rateAction(ActivitySetting.this);
                return true;
            }
        });


        ratePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Tools.sendfeedbackAction(ActivitySetting.this);
                return true;
            }
        });

        logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder =new AlertDialog.Builder(ActivitySetting.this);
                builder.setTitle(getString(R.string.dialog_confirm_title));
                builder.setMessage(getString(R.string.message_logout));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent ia = new Intent(getApplicationContext(), Login.class);
                        ia.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        LoginManager.getInstance().logOut();
                        // Add new Flag to start new Activity
                        ia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ia);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                return true;
            }
        });

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }
       /* themePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //dialogColorChooser(ActivitySetting.this);
                // analytics tracking
                ThisApplication.getInstance().trackEvent(Constant.Event.THEME.name(), "CHANGE", "-");
                return true;
            }
        });

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }
    */
/*
    private void dialogColorChooser(Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_color_theme);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ListView list = (ListView) dialog.findViewById(R.id.list_view);
        final String stringArray[] = getResources().getStringArray(R.array.arr_main_color_name);
        final String colorCode[] = getResources().getStringArray(R.array.arr_main_color_code);
        list.setAdapter(new ArrayAdapter<String>(ActivitySetting.this, android.R.layout.simple_list_item_1, stringArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setBackgroundColor(Color.parseColor(colorCode[position]));
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                sharedPref.setThemeColor(colorCode[pos]);
                toolbar.setBackgroundColor(sharedPref.getThemeColorInt());
                // for system bar in lollipop
                Tools.systemBarLolipop(ActivitySetting.this);
                dialog.dismiss();
                Tools.restartApplication(ActivitySetting.this);
            }
        });

        //global.setIntPref(global.I_PREF_COLOR, global.I_KEY_COLOR, getResources().getColor(R.color.red));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
*/
    /**Binds a preference's summary to its value. More specifically, when the preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also immediately updated upon calling this method. The exact display format is dependent on the type of preference.
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), "")
        );
    }

    /**A preference value change listener that updates the preference's summary to reflect its new value. */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0 ? listPreference.getEntries()[index] : null
                );

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.setting_toolbar, root, false);
            root.addView(toolbar, 0); // insert at top
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);
            root.removeAllViews();
            toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.setting_toolbar, root, false);
            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = toolbar.getHeight();
            }
            content.setPadding(0, height, 0, 0);
            root.addView(content);
            root.addView(toolbar);
        }

        // up arrow coloring
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setBackgroundColor(sharedPref.getThemeColorInt());
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null) {
            return result;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // If we're running pre-L, we need to 'inject' our tint aware Views in place of the standard framework versions
            switch (name) {
                case "EditText": return new AppCompatEditText(this, attrs);
                case "Spinner": return new AppCompatSpinner(this, attrs);
                case "CheckBox": return new AppCompatCheckBox(this, attrs);
                case "RadioButton": return new AppCompatRadioButton(this, attrs);
                case "CheckedTextView": return new AppCompatCheckedTextView(this, attrs);
            }
        }

        return null;
    }
}
