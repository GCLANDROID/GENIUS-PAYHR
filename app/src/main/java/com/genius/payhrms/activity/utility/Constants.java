package com.genius.payhrms.activity.utility;

public class Constants {
	public static final int NOTIFICATION_ID_FOREGROUND_SERVICE = 8466503;
	public static final String ARG_USERS = "users";
	public static final String ARG_RECEIVER = "receiver";
	public static final String ARG_RECEIVER_UID = "receiver_uid";
	public static final String ARG_CHAT_ROOMS = "chat_rooms";
	public static final String ARG_GROUP_CHAT_ROOMS = "group_chat_rooms";
	public static final String ARG_FIREBASE_TOKEN = "firebaseToken";
	public static final String ARG_FRIENDS = "friends";
	public static final String ARG_UID = "uid";
	public static final String ARG_GROUPS = "GROUPS";
	public static final String URL_POST = "https://fcm.googleapis.com/fcm/send";
	public static final String SERVER_KEY = "AAAAfDkwYPg:APA91bFUxAKlOhagWuUvfXciNfmYMAo7wblneNkBHvdYIGxr_GTVY4IOvGDoAWlLLxl5Labgt0KD83KrWZqhHTkbCRHyjWxn2g-nFYMBgr7cnyU3D6mjPq4OjyL-8DEWqMtTHIC0Sxpl";
	public static final String ARG_TO = "TO";
	public static final String ARG_SENDER = "SENDER";
	public static final String ARG_GROUPID = "groupid";
	public static final String ARG_GROUPNAME = "groupname";
	public static final String ARG_MESSAGE = "message";
	public static final String ARG_EMAILVERIFIED = "emailverified";
	public static final String ARG_URL = "imguri";

	public static class ACTION {
		public static final String MAIN_ACTION = "test.action.main";
		public static final String START_ACTION = "test.action.start";
		public static final String STOP_ACTION = "test.action.stop";
		public static final String LATITUDE = "test.action.LAT";
		public static final String LONGITUDE = "test.action.LON";
	}

	public static class STATE_SERVICE {
		public static final int CONNECTED = 10;
		public static final int NOT_CONNECTED = 0;
	}

}
