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

import android.util.Log;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import android.util.Log;
public class HDCPPreferenceController extends AbstractPreferenceController implements
        PreferenceControllerMixin {

    private static final String KEY_DEVICE_MODEL = "hdcp_status";
	Context mContext;
    public HDCPPreferenceController(Context context) {
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
	/*	if(getHdcpStatus())
            pref.setSummary(mContext.getResources().getString(R.string.hdcp_status_ok));
		else
		pref.setSummary(mContext.getResources().getString(R.string.device_info_not_available));	

	*/

	String key = getkey_hdcp();
				Log.d("hdcp_key###","key = " + key);
					switch(key){
					case "14":
					pref.setSummary("14");
					break;
					case "22":
					pref.setSummary("22");
					break;
					case "1422":
					pref.setSummary("14+22");
					break;
					default :
					pref.setSummary(mContext.getResources().getString(R.string.device_info_not_available));
					}
        }

    }

    @Override
    public String getPreferenceKey() {
        return KEY_DEVICE_MODEL;
    }

	private boolean getHdcpStatus() {
	   boolean valid = false;
	   BufferedReader br;
	   try {
					   br = new BufferedReader(new FileReader("/sys/class/amhdmitx/amhdmitx0/hdcp_ksv_info"));
					   String oneLine = br.readLine();
					   if (oneLine.contains("HDCP14 BKSV") && oneLine.contains("Valid") && !oneLine.equals("HDCP14 BKSV: 0000000000  Invalid")) {
							   valid = true;
					   }
					   br.close();
	   } catch (FileNotFoundException e) {
					   e.printStackTrace();
	   } catch (IOException e) {
					   e.printStackTrace();
	   }
	   return valid;
     }
	public static String getkey_hdcp() {
	        String result = "";
	        String resultgu = "";
	        ProcessBuilder cmd;
			//if (isfilexists("/sys/class/amhdmitx/amhdmitx0/hdcp_lstore")) {
				try {
	                String[] args = { "/system/bin/cat", "/sys/class/amhdmitx/amhdmitx0/hdcp_lstore"  };
		                cmd = new ProcessBuilder(args);
	                Process process = cmd.start();
	                InputStream in = process.getInputStream();
		    /*byte[] re = new byte[24];
			int len = 0;
			while ((len=in.read(re)) != -1) {
		result = result + new String(re,0,len);
			}*/
	               result =  new BufferedReader(new InputStreamReader(in))
	                     .lines().parallel().collect(Collectors.joining(""));
	                in.close();
			} catch (IOException ex) {
		                ex.printStackTrace();
	                result = "hdcp error";
			}
	       resultgu = result.trim().replaceAll("\r|\n","");
	        //Log.d("getkey_hdcp", "result = " + result);
	        return resultgu;
	}
}
