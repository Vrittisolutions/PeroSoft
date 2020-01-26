/***************************************************************************
 * 
 * This file is part of the 'NDEF Tools for Android' project at
 * http://code.google.com/p/ndef-tools-for-android/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ****************************************************************************/

package org.ndeftools.util.activity;

import org.ndeftools.Message;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.util.Log;


/**
 * 
 * Abstract {@link Activity} for reading NFC messages - both via a tag and via Beam (push)
 * 
 * @author Thomas Rorvik Skjolberg
 *
 */

public abstract class NfcReaderActivity extends NfcDetectorActivity {

	private static final String TAG = NfcReaderActivity.class.getName();

	@Override
	public void nfcIntentDetected(Intent intent, String action) {
		 try { 
			 readNdefUID(ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
         }
         catch (Exception e) { 
             Log.e(TAG, e.getLocalizedMessage());         
             readNdefUID("");
         }
	}
	
	 private String ByteArrayToHexString(byte[] inarray) { // converts byte arrays to string
	        int i, j, in;
	        String[] hex = {
	                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"
	        };
	        String out = "";

	        for (j = 0; j < inarray.length; ++j) {
	            in = inarray[j] & 0xff;
	            i = (in >> 4) & 0x0f;
	            out += hex[i];
	            i = in & 0x0f;
	            out += hex[i];
	        }
	        return out;
	    }
	protected abstract void readNdefUID(String UID);
	/**
	 * An NDEF message was read and parsed
	 * 
	 * @param message the message
	 */
	
	protected abstract void readNdefMessage(Message message);

	/**
	 * An empty NDEF message was read.
	 * 
	 */
	
	protected abstract void readEmptyNdefMessage();

	/**
	 * 
	 * Something was read via NFC, but it was not an NDEF message. 
	 * 
	 * Handling this situation is out of scope of this project.
	 * 
	 */
	
	protected abstract void readNonNdefMessage();

}
