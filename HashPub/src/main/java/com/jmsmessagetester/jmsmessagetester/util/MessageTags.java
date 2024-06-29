package com.jmsmessagetester.jmsmessagetester.util;

public interface MessageTags {
    public static final String TAG_1_EVENT_TYPE = "1";
    public static final String TAG_2_SENDER_PORT = "2";
    public static final String TAG_3_RECEIVER_PORT = "3";
    public static final String TAG_4_STATUS = "4";
    public static final String TAG_5_NODE_TYPE = "5";
    public static final String TAG_6_HASH_VAL = "6";
    public static final String TAG_7_VAL = "7";
    public static final String TAG_8_DATA_RETREIVAL_REQ_ID = "8";

    public static final int NODE_STATUS_START = 1;

    public static final int EVENT_TYPE_NEW_NODE = 1;
//    public static final int EVENT_TYPE_REPLY_FOR_NEW_NODE = 2;
    public static final int EVENT_TYPE_UPDATE_NODE_TYPE = 3;
    public static final int EVENT_TYPE_AKC_FOR_NODE_TYPE_UPDATE = 4;
    public static final int EVENT_TYPE_SEND_DATA_TO_RECEIVER = 5;
    public static final int EVENT_TYPE_ACK_FOR_DATA_STORE_REQUEST = 6;
    public static final int EVENT_TYPE_RETREIVE_DATA = 7;
    public static final int EVENT_TYPE_ACK_FOR_RETREIVE_DATA = 8;

    public static final int STATUS_FORWARD_MESSAGE = 1;
    public static final int STATUS_REPLY_MESSAGE = 2;

    public static final String TAG_SEPERATOR = "|";
    public static final String TAG_EQUAL = "=";
}
