/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package plugin.android.ss.com.testsettings.settings.deviceinfo;
import com.android.settings.R;
import android.os.SystemProperties;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;

import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.DeviceInfoUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import android.os.Environment;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.app.AlertDialog;
import android.app.Dialog;
public class StartWizardPreferenceController extends AbstractPreferenceController implements
        PreferenceControllerMixin {
	
    private static final String KEY_START_WIZARD = "start_wizard";
    private static final String PACKAGE = "com.mbx.settingsmbox";
    private static final String START_ACTIVITY = "com.mbx.settingsmbox.OobeActivity";
	
    private final Fragment mHost;

    public StartWizardPreferenceController(Context context, Fragment host) {
        super(context);
        mHost = host;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
	
    @Override
    public String getPreferenceKey() {
        return KEY_START_WIZARD;
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), KEY_START_WIZARD)) {
            return false;
        }
		enableAndStartupWizard(mHost.getContext());
        return true;
    }
	
       // @sz_add_begin. start wizard.
       private void enableAndStartupWizard(Context context) {
               final PackageManager pm = context.getPackageManager();
               final ComponentName name = new ComponentName(PACKAGE, START_ACTIVITY);
               pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
               int num;
              num = 0;
               while(!getWizardStatus()) {
                       num++;
                       if ( 5 == num ) break;
                       try {
                               Thread.sleep(1000);
                       } catch (InterruptedException e) {
                               e.printStackTrace();
                       }
               }
               if ( 5 == num )
               {
               } else {
                       if (getWizardStatus()) {
                               try {
                                       Thread.sleep(2000);
                               } catch (InterruptedException e) {
                                       e.printStackTrace();
                               }
                               showSuccessMenu();
                       } else {
                       }
               }
             num = 0;
		}
	   
       public void showSuccessMenu(){
               new AlertDialog.Builder(mHost.getContext()).setTitle("Start Wizard Set").setMessage("Set Start Wizard Success").show();
       }

       public boolean getWizardStatus() {
               File dataDir = Environment.getDataDirectory();
               File mSystemDir = new File(dataDir, "system");
               int userId = 0;
               File userDir = new File(new File(mSystemDir, "users"), Integer.toString(userId));
               File userPackagesStateFile = new File(userDir, "package-restrictions.xml");

               try {
                       FileInputStream str = new FileInputStream(userPackagesStateFile);
                       final XmlPullParser parser = Xml.newPullParser();
                       parser.setInput(str, StandardCharsets.UTF_8.name());

                       int type;
                       //解析根元素
                       while ((type = parser.next()) != XmlPullParser.START_TAG
                                       && type != XmlPullParser.END_DOCUMENT) {
                               ;
                       }

                       //解析一级子元素
                       int outerDepth = parser.getDepth();
                       while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
                                       && (type != XmlPullParser.END_TAG
                                               || parser.getDepth() > outerDepth)) {
                               if (type == XmlPullParser.END_TAG
                                               || type == XmlPullParser.TEXT) {
                                       continue;
                               }
                               String tagName = parser.getName();
                               if (tagName.equals("pkg")) {
                                       String name = parser.getAttributeValue(null, "name");
                                       if(name.equals("com.mbx.settingsmbox")) {
                                               //解析二级子元素
                                               int packageDepth = parser.getDepth();
                                               while ((type=parser.next()) != XmlPullParser.END_DOCUMENT
                                                               && (type != XmlPullParser.END_TAG
                                                                       || parser.getDepth() > packageDepth)) {
                                                       if (type == XmlPullParser.END_TAG
                                                                       || type == XmlPullParser.TEXT) {
                                                               continue;
                                                       }
                                                       tagName = parser.getName();
                                                       if (tagName.equals("enabled-components")) {
                                                               return true;
                                                       } else if (tagName.equals("disabled-components")) {
                                                               return false;
                                                       }
                                               }
                                       }
                               }
                       }
                       str.close();
               } catch (FileNotFoundException e) {
                       e.printStackTrace();
               } catch (XmlPullParserException e) {
                       e.printStackTrace();
               } catch (IOException e) {
                       e.printStackTrace();
               }
               return false;
       }
}
