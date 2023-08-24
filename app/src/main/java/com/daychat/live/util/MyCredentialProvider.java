package com.daychat.live.util;

//import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider;
//import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;
//import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
//import com.tencent.qcloud.core.common.QCloudClientException;

//public class MyCredentialProvider extends BasicLifecycleCredentialProvider {
//
//    private final String tmpSecretId;
//    private final String tmpSecretKey;
//    private final String sessionToken;
//    private final long expiredTime;
//    private final long startTime;
//
//    public MyCredentialProvider(String tmpSecretId, String tmpSecretKey, String sessionToken, long expiredTime, long startTime) {
//        this.tmpSecretId = tmpSecretId;
//        this.tmpSecretKey = tmpSecretKey;
//        this.sessionToken = sessionToken;
//        this.expiredTime = expiredTime;
//        this.startTime = startTime;
//    }
//
//    @Override
//    protected QCloudLifecycleCredentials fetchNewCredentials() throws QCloudClientException {
//
//        // 最后返回临时密钥信息对象
//        return new SessionQCloudCredentials(tmpSecretId, tmpSecretKey, sessionToken, startTime, expiredTime);
//    }
//}
