package cn.net.chenbao.qbyp.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import cn.net.chenbao.qbyp.bean.PhoneInfo;


/**
 * 用于获取联系人的手机号码以及姓名
 * 
 * @author ZXJ
 **/
public class GetPhoneInfo {

	public static PhoneInfo getContactPhone(Cursor cursor, Context context) {
		// TODO Auto-generated method stub
		int phoneColumn = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		PhoneInfo result = null;
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人电话的cursor
			Cursor phone = context.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
							+ contactId, null, null);
			if (phone.moveToFirst()) {
				for (; !phone.isAfterLast(); phone.moveToNext()) {

					int nameFieldColumnIndex = phone
							.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
					String name = phone.getString(nameFieldColumnIndex);
					int index = phone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					String phoneNumber = phone.getString(index);
					result = new PhoneInfo(phoneNumber, name);
				}
				if (!phone.isClosed()) {
					phone.close();
				}
			}
		}
		return result;
	}
}
