/*
 * Copyright (C) 2012 Lightbox
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

package tip.tdc.com.tip.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/** 
 * FileUtils 
 * @author Nilesh Patel
 */
public class FileUtils {
//	static boolean isInited = false;
	public static final String TypePng = ".png";
	public static final String TypeJpg = ".jpg";
	public static final String TypeBitmap = ".bitamp";
	public static String TypeFrame = ".";
	public static String TypeFrameThumb = ".";
	public static String httpSouce = "";
	public static String http = "http://";
	
	public static String PrefixNamePhoto = "TinTin_";
	
	/** Used to tag logs */
	@SuppressWarnings("unused")
	private static final String TAG = FileUtils.class.getSimpleName();
	
	public static byte[] readFileToByteArray(File file) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024 * 4];
			int n = 0;
			while (-1 != (n = inputStream.read(buffer))) {
				output.write(buffer, 0, n);
			}
			return output.toByteArray();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// Do nothing
			}
		}
	}
	public static String getRamdomString(){
		return String.valueOf(Calendar.getInstance().getTimeInMillis());
	}
	
	static {
		TypeFrame = ".fr";
		TypeFrameThumb = ".frth";
		httpSouce = "/";
	}
}
