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

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;

import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.DeviceInfoUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
public class WidevinePreferenceController extends AbstractPreferenceController implements
        PreferenceControllerMixin {

    private static final String KEY_DEVICE_MODEL = "widevine_status";
	Context mContext;
    public WidevinePreferenceController(Context context) {
        super(context);
		mContext = context;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        final Preference pref = screen.findPreference(KEY_DEVICE_MODEL);
        if (pref != null ) {
	execRootCmdSilent("echo widevinekeybox > /sys/class/unifykeys/name\n");
	String wvkey = execRootCmd("cat /sys/class/unifykeys/read\n");
	if (wvkey != null && wvkey != ""){
	pref.setSummary(mContext.getResources().getString(R.string.widevine_status_ok));								
	}else{
	pref.setSummary(mContext.getResources().getString(R.string.widevine_status_Nook));
	}
    }
    }

    @Override
    public String getPreferenceKey() {
        return KEY_DEVICE_MODEL;
    }
    public static int execRootCmdSilent(String cmd) {
	            int result = -1;
		        DataOutputStream dos = null;
			    try {
		              Process p = Runtime.getRuntime().exec("su");
		            dos = new DataOutputStream(p.getOutputStream());
		                dos.writeBytes(cmd + "\n");
			            dos.flush();
		                dos.writeBytes("exit\n");
			            dos.flush();
		                p.waitFor();
		            result = p.exitValue();
			    } catch (Exception e) {
			       e.printStackTrace();
			    } finally {
				    if (dos != null) {
					    try {
			                   dos.close();
					    } catch (IOException e) {
					    e.printStackTrace();
					    }
				    }
			    }
	            return result;
    }

    public static String execRootCmd(String cmd) {
	            String result = "";
		            DataOutputStream dos = null;
			            DataInputStream dis = null;
				    try {
	                Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
		            dos = new DataOutputStream(p.getOutputStream());
	                dis = new DataInputStream(p.getInputStream());
		            dos.writeBytes(cmd + "\n");
		                dos.flush();
		            dos.writeBytes("exit\n");
	                dos.flush();
	            byte[] re = new byte[1024];
	                String line = null;
			while (dis.read(re) != -1) {
	                result += new String(re);
				}
	            p.waitFor();
		    } catch (Exception e) {
		               e.printStackTrace();
		    } finally {
		    if (dos != null) {
		    try {
	                dos.close();
		    } catch (IOException e) {
                        e.printStackTrace();
			    }
		    }
		    if (dis != null) {
	    try {
                       dis.close();
		    } catch (IOException e) {
                      e.printStackTrace();
	    }
		    }
	    }
           return result;
					        
    }

}
