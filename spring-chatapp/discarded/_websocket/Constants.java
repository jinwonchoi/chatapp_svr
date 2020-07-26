package com.gencode.issuetool._websocket;

public class Constants {
    public static final String SECURED_CHAT = "/secured/chat"; //브로드캐스팅 broker
    public static final String SECURED_MAPPING_TEST = "/secured/test"; //브로드캐스팅 broker
    public static final String SECURED_CHAT_HISTORY = "/secured/history"; //브로디캐스팅 user dest
    public static final String SECURED_CHAT_ROOM = "/secured/room"; //1:1 broker
    public static final String SECURED_CHAT_SPECIFIC_USER = "/secured/user/queue/specific-user"; //1:1 user dest
    
    public static final String SECURED_CHAT_GROUP = "/secured/group/{uuid}"; //group broker
    public static final String SECURED_CHAT_GROUP_USER = "/secured/group/queue/{uuid}"; //group user dest
    public static final String SECURED_CHAT_GROUP2 = "/secured/group2//{uuid}"; //group broker
    public static final String SECURED_CHAT_GROUP_USER2 = "/secured/group2/queue/{uuid}"; //group user dest
    
    public static final String APP_DEST = "/issuetool";
    public static final String DEST_PREFIX = "/secured/user";
}
